package model;

import db.DB_Konexioa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BiltegiLangilea extends Langilea {

    public BiltegiLangilea(Langilea l) {
        super(l.getIdLangilea(), l.getIzena(), l.getAbizena(), l.getNan(), l.getJaiotzaData(), l.getHerriaId(),
                l.getHelbidea(), l.getPostaKodea(), l.getTelefonoa(), l.getEmaila(), l.getHizkuntza(),
                l.getPasahitza(), l.getSaltoTxartelaUid(), l.getAltaData(), l.getEguneratzeData(),
                l.isAktibo(), l.getSailaId(), l.getIban(), l.getKurrikuluma());
    }

    public void biltegiaSortu(String izena, String sku) throws SQLException {
        String sql = "INSERT INTO biltegiak (izena, biltegi_sku) VALUES (?, ?)";
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql)) {
            pst.setString(1, izena);
            pst.setString(2, sku);
            pst.executeUpdate();
        }
    }

    public void biltegiaEzabatu(int idBiltegia) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            PreparedStatement pstCheck = kon.prepareStatement("SELECT COUNT(*) FROM produktuak WHERE biltegi_id = ?");
            pstCheck.setInt(1, idBiltegia);
            ResultSet rs = pstCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("EZIN DA EZABATU: Produktuak ditu barruan.");
            }
            String sql = "DELETE FROM biltegiak WHERE id_biltegia = ?";
            try (PreparedStatement pst = kon.prepareStatement(sql)) {
                pst.setInt(1, idBiltegia);
                pst.executeUpdate();
            }
        }
    }

    public void biltegiaEditatu(int idBiltegia, String izena, String sku) throws SQLException {
        String sql = "UPDATE biltegiak SET izena = ?, biltegi_sku = ? WHERE id_biltegia = ?";
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql)) {
            pst.setString(1, izena);
            pst.setString(2, sku);
            pst.setInt(3, idBiltegia);
            pst.executeUpdate();
        }
    }

    public int hornitzaileBerriaSortu(String izena, String ifz, String emaila) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            PreparedStatement pstCheck = kon
                    .prepareStatement("SELECT COUNT(*) FROM hornitzaileak WHERE emaila = ? OR ifz_nan = ?");
            pstCheck.setString(1, emaila);
            pstCheck.setString(2, ifz);
            ResultSet rsCheck = pstCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                throw new SQLException("ERROREA: Hornitzaile hori existitzen da jada.");
            }
            String sql = "INSERT INTO hornitzaileak (izena_soziala, ifz_nan, emaila, pasahitza, helbidea, herria_id, posta_kodea) VALUES (?, ?, ?, '1234', 'Zehaztugabea', 1, '00000')";
            try (PreparedStatement pst = kon.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, izena);
                pst.setString(2, ifz);
                pst.setString(3, emaila);
                pst.executeUpdate();
                ResultSet rsKey = pst.getGeneratedKeys();
                if (rsKey.next()) {
                    return rsKey.getInt(1);
                }
            }
        }
        throw new SQLException("Errorea hornitzailea sortzean.");
    }

    public void produktuSarreraBerriaSortu(int hornitzaileaId, List<Produktua> produktuak, List<SarreraLerroa> lerroak)
            throws SQLException {
        Connection con = null;
        try {
            con = DB_Konexioa.konektatu();
            con.setAutoCommit(false);

            // 1. Sarrera sortu
            String sqlSarrera = "INSERT INTO sarrerak (hornitzailea_id, langilea_id, sarrera_egoera) VALUES (?, ?, 'Bidean')";
            PreparedStatement pstSarrera = con.prepareStatement(sqlSarrera, Statement.RETURN_GENERATED_KEYS);
            pstSarrera.setInt(1, hornitzaileaId);
            pstSarrera.setInt(2, this.getIdLangilea());
            pstSarrera.executeUpdate();
            ResultSet rsKeys = pstSarrera.getGeneratedKeys();
            int sarreraId = -1;
            if (rsKeys.next())
                sarreraId = rsKeys.getInt(1);
            else
                throw new SQLException("Ez da sarrera produktu IDrik sortu.");

            // 2. Sortu produktuak eta lerroak
            String sqlProd = "INSERT INTO produktuak (izena, marka, kategoria_id, mota, biltegi_id, hornitzaile_id, stock, produktu_egoera, deskribapena, irudia_url, produktu_egoera_oharra, salgai) VALUES (?, ?, ?, ?, ?, ?, ?, 'Zehazteko', ?, ?, ?, 0)";
            PreparedStatement pstProd = con.prepareStatement(sqlProd, Statement.RETURN_GENERATED_KEYS);
            String sqlLerroa = "INSERT INTO sarrera_lerroak (sarrera_id, produktua_id, kantitatea, sarrera_lerro_egoera) VALUES (?, ?, ?, 'Bidean')";
            PreparedStatement pstLerroa = con.prepareStatement(sqlLerroa);

            for (int i = 0; i < produktuak.size(); i++) {
                Produktua p = produktuak.get(i);
                SarreraLerroa l = lerroak.get(i);

                pstProd.setString(1, p.getIzena());
                pstProd.setString(2, p.getMarka());
                pstProd.setInt(3, p.getKategoriaId());
                pstProd.setString(4, p.getMota());
                pstProd.setInt(5, p.getBiltegiId());
                pstProd.setInt(6, hornitzaileaId);
                pstProd.setInt(7, p.getStock()); // Initial stock = quantity in line? Or 0? Logic in MenuLogistika uses
                                                 // stock = kanti
                pstProd.setString(8, p.getDeskribapena());
                pstProd.setString(9, p.getIrudiaUrl());
                pstProd.setString(10, p.getProduktuEgoeraOharra());
                pstProd.executeUpdate();

                ResultSet rsProdKey = pstProd.getGeneratedKeys();
                int prodId = -1;
                if (rsProdKey.next())
                    prodId = rsProdKey.getInt(1);

                pstLerroa.setInt(1, sarreraId);
                pstLerroa.setInt(2, prodId);
                pstLerroa.setInt(3, l.getKantitatea());
                pstLerroa.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            if (con != null)
                con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    public void produktuEgoeraOharraJarri(int idProduktua, String oharra) throws SQLException {
        // Funtzionalitate hau UI-ko sorkuntzaren zati zen, baina beharbada bereizita
        // behar da?
        // UI-ak ez zuen "Gehitu Oharra" botoi espliziturik lehendik zeuden
        // produktuentzat, baina
        // prompt-ean eskatuta.
        String sql = "UPDATE produktuak SET produktu_egoera_oharra = ? WHERE id_produktua = ?";
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql)) {
            pst.setString(1, oharra);
            pst.setInt(2, idProduktua);
            pst.executeUpdate();
        }
    }

    public void produktuarenBiltegiaAldatu(int idProduktua, int idBiltegia) throws SQLException {
        String sql = "UPDATE produktuak SET biltegi_id = ? WHERE id_produktua = ?";
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql)) {
            pst.setInt(1, idBiltegia);
            pst.setInt(2, idProduktua);
            pst.executeUpdate();
        }
    }
    // Object[] zerrenda bat itzultzea hobeto izan daiteke UI-rako, baina saia
    // gaitezen itzultzen
    // Sarrera objektuak.
    // UI-ak taulak lotzen ditu Hornitzailearen Izena erakusteko. Sarrera modeloak
    // ez du
    // Hornitzailearen Izenik.
    // Barne-klase berri baten zerrenda bat edo DTO bat itzuliko dut, ala Sarrera
    // soilik eta
    // informazio gehigarria geroago lortu?
    // Hasi beharrezkoa denarekin soilik. UI-ak kontsulta pertsonalizatu bat
    // erabiltzen du.
    // Kontsulta exekutatuko dut eta ResultSet logika itzuliko dut, edo beharbada
    // hobeto,
    // TableModel egiturarekin bat datorren Object[] zerrenda bat itzuli.

    public List<Object[]> produktuSarrerakIkusi(String egoeraIragazkia) throws SQLException {
        String baseSql = "SELECT s.id_sarrera, h.izena_soziala AS Hornitzailea, s.data, s.sarrera_egoera FROM sarrerak s JOIN hornitzaileak h ON s.hornitzailea_id = h.id_hornitzailea ";
        String sql = baseSql;
        if ("Bidean".equals(egoeraIragazkia))
            sql += " WHERE s.sarrera_egoera = 'Bidean' ";
        else if ("Jasota".equals(egoeraIragazkia))
            sql += " WHERE s.sarrera_egoera = 'Jasota' ";
        sql += " ORDER BY s.data DESC";

        List<Object[]> emaitza = new ArrayList<>();
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[colCount];
                for (int i = 0; i < colCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                emaitza.add(row);
            }
        }
        return emaitza;
    }

    public void produktuSarreraEditatu(int idSarrera, String egoera) throws SQLException {
        // Cascading: Update Parent -> Update All Lines
        String sql = "UPDATE sarrerak SET sarrera_egoera = ? WHERE id_sarrera = ?";
        String sqlLerroak = "UPDATE sarrera_lerroak SET sarrera_lerro_egoera = ? WHERE sarrera_id = ?";

        Connection kon = null;
        try {
            kon = DB_Konexioa.konektatu();
            kon.setAutoCommit(false);

            try (PreparedStatement pst = kon.prepareStatement(sql)) {
                pst.setString(1, egoera);
                pst.setInt(2, idSarrera);
                pst.executeUpdate();
            }

            try (PreparedStatement pstLerroak = kon.prepareStatement(sqlLerroak)) {
                pstLerroak.setString(1, egoera);
                pstLerroak.setInt(2, idSarrera);
                pstLerroak.executeUpdate();
            }

            kon.commit();
        } catch (SQLException e) {
            if (kon != null)
                kon.rollback();
            throw e;
        } finally {
            if (kon != null) {
                kon.setAutoCommit(true);
                kon.close();
            }
        }
    }

    public void produktuSarreraEgoeraAldatu(int idSarreraLerroa, String egoera, int idSarrera) throws SQLException {
        try (Connection con = DB_Konexioa.konektatu()) {
            PreparedStatement pst = con.prepareStatement(
                    "UPDATE sarrera_lerroak SET sarrera_lerro_egoera = ? WHERE id_sarrera_lerroa = ?");
            pst.setString(1, egoera);
            pst.setInt(2, idSarreraLerroa);
            if (pst.executeUpdate() > 0) {
                // guraso egoera egiaztatu
                PreparedStatement pstCheck = con.prepareStatement(
                        "SELECT COUNT(*) FROM sarrera_lerroak WHERE sarrera_id = ? AND sarrera_lerro_egoera != 'Jasota'");
                pstCheck.setInt(1, idSarrera);
                ResultSet rs = pstCheck.executeQuery();
                if (rs.next()) {
                    String egoeraBerria = (rs.getInt(1) == 0) ? "Jasota" : "Bidean";
                    produktuSarreraEditatu(idSarrera, egoeraBerria);
                }
            }
        }
    }

    public void produktuEskaeraEgoeraAldatu(int idEskaera, String egoera) throws SQLException {
        // Cascading: Update Parent -> Update All Lines
        String sql = "UPDATE eskaerak SET eskaera_egoera = ? WHERE id_eskaera = ?";
        String sqlLerroak = "UPDATE eskaera_lerroak SET eskaera_lerro_egoera = ? WHERE eskaera_id = ?";

        Connection kon = null;
        try {
            kon = DB_Konexioa.konektatu();
            kon.setAutoCommit(false);

            try (PreparedStatement pst = kon.prepareStatement(sql)) {
                pst.setString(1, egoera);
                pst.setInt(2, idEskaera);
                pst.executeUpdate();
            }

            try (PreparedStatement pstLerroak = kon.prepareStatement(sqlLerroak)) {
                pstLerroak.setString(1, egoera);
                pstLerroak.setInt(2, idEskaera);
                pstLerroak.executeUpdate();
            }

            kon.commit();
        } catch (SQLException e) {
            if (kon != null)
                kon.rollback();
            throw e;
        } finally {
            if (kon != null) {
                kon.setAutoCommit(true);
                kon.close();
            }
        }
    }

    public List<Object[]> produktuSarreraLerroakIkusi(int idSarrera) throws SQLException {
        String sql = "SELECT sl.id_sarrera_lerroa, p.izena, p.marka, sl.kantitatea, sl.sarrera_lerro_egoera " +
                "FROM sarrera_lerroak sl " +
                "JOIN produktuak p ON sl.produktua_id = p.id_produktua " +
                "WHERE sl.sarrera_id = ?";
        List<Object[]> emaitza = new ArrayList<>();
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql)) {
            pst.setInt(1, idSarrera);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                emaitza.add(new Object[] {
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5)
                });
            }
        }
        return emaitza;
    }

    public void produktuSarreraLerroEgoeraAldatu(int idSarreraLerroa, String egoera) throws SQLException {
        // Update Line -> Check Peers -> Update Parent (Internal - No Cascade)
        String sql = "UPDATE sarrera_lerroak SET sarrera_lerro_egoera = ? WHERE id_sarrera_lerroa = ?";

        try (Connection kon = DB_Konexioa.konektatu()) {
            // 1. Update line
            try (PreparedStatement pst = kon.prepareStatement(sql)) {
                pst.setString(1, egoera);
                pst.setInt(2, idSarreraLerroa);
                pst.executeUpdate();
            }

            // 2. Get Parent ID
            int idSarrera = -1;
            try (PreparedStatement pstGetId = kon
                    .prepareStatement("SELECT sarrera_id FROM sarrera_lerroak WHERE id_sarrera_lerroa = ?")) {
                pstGetId.setInt(1, idSarreraLerroa);
                ResultSet rs = pstGetId.executeQuery();
                if (rs.next())
                    idSarrera = rs.getInt(1);
            }

            if (idSarrera != -1) {
                // 3. Check peers - IGNORE 'Ezabatua' lines
                boolean allJasota = true;
                try (PreparedStatement pstCheck = kon.prepareStatement(
                        "SELECT COUNT(*) FROM sarrera_lerroak WHERE sarrera_id = ? AND sarrera_lerro_egoera != 'Jasota' AND sarrera_lerro_egoera != 'Ezabatua'")) {
                    pstCheck.setInt(1, idSarrera);
                    ResultSet rs = pstCheck.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        allJasota = false;
                    }
                }

                // 4. Update Parent internally (Prevent infinite cascade)
                String newStatus = allJasota ? "Jasota" : "Bidean";
                try (PreparedStatement pstUpdateParent = kon
                        .prepareStatement("UPDATE sarrerak SET sarrera_egoera = ? WHERE id_sarrera = ?")) {
                    pstUpdateParent.setString(1, newStatus);
                    pstUpdateParent.setInt(2, idSarrera);
                    pstUpdateParent.executeUpdate();
                }
            }
        }
    }

    public void produktuSarreraEzabatu(int idSarrera) throws SQLException {
        String sqlLerroak = "DELETE FROM sarrera_lerroak WHERE sarrera_id = ?";
        String sqlSarrera = "DELETE FROM sarrerak WHERE id_sarrera = ?";

        Connection kon = null;
        try {
            kon = DB_Konexioa.konektatu();
            kon.setAutoCommit(false);

            try (PreparedStatement pstLerroak = kon.prepareStatement(sqlLerroak)) {
                pstLerroak.setInt(1, idSarrera);
                pstLerroak.executeUpdate();
            }

            try (PreparedStatement pstSarrera = kon.prepareStatement(sqlSarrera)) {
                pstSarrera.setInt(1, idSarrera);
                pstSarrera.executeUpdate();
            }

            kon.commit();
        } catch (SQLException e) {
            if (kon != null)
                kon.rollback();
            throw e;
        } finally {
            if (kon != null) {
                kon.setAutoCommit(true);
                kon.close();
            }
        }
    }

    public List<Object[]> produktuEskaerakIkusi(String egoeraIragazkia) throws SQLException {
        String sql = "SELECT e.id_eskaera, b.izena_edo_soziala, e.data, e.guztira_prezioa, e.eskaera_egoera " +
                "FROM eskaerak e " +
                "JOIN bezeroak b ON e.bezeroa_id = b.id_bezeroa ";

        if (egoeraIragazkia != null && !egoeraIragazkia.isEmpty() && !"Denak".equals(egoeraIragazkia)) {
            sql += " WHERE e.eskaera_egoera = ?";
        }
        sql += " ORDER BY e.data DESC";

        List<Object[]> emaitza = new ArrayList<>();
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql)) {

            if (egoeraIragazkia != null && !egoeraIragazkia.isEmpty() && !"Denak".equals(egoeraIragazkia)) {
                pst.setString(1, egoeraIragazkia);
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                emaitza.add(new Object[] {
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getTimestamp(3),
                        rs.getBigDecimal(4),
                        rs.getString(5)
                });
            }
        }
        return emaitza;
    }

    public List<Object[]> produktuEskaeraLerroakIkusi(int idEskaera) throws SQLException {
        String sql = "SELECT el.id_eskaera_lerroa, p.izena, el.kantitatea, el.unitate_prezioa, el.eskaera_lerro_egoera "
                +
                "FROM eskaera_lerroak el " +
                "JOIN produktuak p ON el.produktua_id = p.id_produktua " +
                "WHERE el.eskaera_id = ?";
        List<Object[]> emaitza = new ArrayList<>();
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql)) {
            pst.setInt(1, idEskaera);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                emaitza.add(new Object[] {
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getBigDecimal(4),
                        rs.getString(5)
                });
            }
        }
        return emaitza;
    }

    public void produktuEskaeraLerroEgoeraAldatu(int idEskaeraLerroa, String egoera, int idEskaera)
            throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            // 1. Lerroaren egoera eguneratu
            String sql = "UPDATE eskaera_lerroak SET eskaera_lerro_egoera = ? WHERE id_eskaera_lerroa = ?";
            try (PreparedStatement pst = kon.prepareStatement(sql)) {
                pst.setString(1, egoera);
                pst.setInt(2, idEskaeraLerroa);
                pst.executeUpdate();
            }

            // 2. Eskaera osoaren egoera eguneratu
            // LOGIKA ZUZENKETA:
            // - Lerroren bat "Prestatzen" badago -> Eskaera "Prestatzen"
            // - Bestela (denak "Osatua/Bidalita" edo "Ezabatua" badira) ->
            // "Osatua/Bidalita"

            boolean dagoPrestatzen = false;
            String sqlCheck = "SELECT COUNT(*) FROM eskaera_lerroak WHERE eskaera_id = ? AND eskaera_lerro_egoera = 'Prestatzen'";
            try (PreparedStatement pstABC = kon.prepareStatement(sqlCheck)) {
                pstABC.setInt(1, idEskaera);
                ResultSet rs = pstABC.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    dagoPrestatzen = true;
                }
            }

            if (dagoPrestatzen) {

                try (PreparedStatement pstUpd = kon
                        .prepareStatement("UPDATE eskaerak SET eskaera_egoera = ? WHERE id_eskaera = ?")) {
                    pstUpd.setString(1, "Prestatzen");
                    pstUpd.setInt(2, idEskaera);
                    pstUpd.executeUpdate();
                }
            } else {

                try (PreparedStatement pstUpd = kon
                        .prepareStatement("UPDATE eskaerak SET eskaera_egoera = ? WHERE id_eskaera = ?")) {
                    pstUpd.setString(1, "Osatua/Bidalita");
                    pstUpd.setInt(2, idEskaera);
                    pstUpd.executeUpdate();
                }
            }
        }
    }
}
