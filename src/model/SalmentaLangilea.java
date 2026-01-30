package model;

import db.DB_Konexioa;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.FakturaPDF.BezeroDatuak;
import model.FakturaPDF.LerroDatuak;

public class SalmentaLangilea extends Langilea {

    public SalmentaLangilea(Langilea l) {
        super(l.getIdLangilea(), l.getIzena(), l.getAbizena(), l.getNan(), l.getJaiotzaData(), l.getHerriaId(),
                l.getHelbidea(), l.getPostaKodea(), l.getTelefonoa(), l.getEmaila(), l.getHizkuntza(),
                l.getPasahitza(), l.getSaltoTxartelaUid(), l.getAltaData(), l.getEguneratzeData(),
                l.isAktibo(), l.getSailaId(), l.getIban(), l.getKurrikuluma());
    }

    /**
     * Faktura bat sortu eskaera batetik abiatuta.
     * 
     * @param idEskaera Eskaeraren IDa
     * @return Sortutako PDF fitxategia
     * @throws Exception
     */
    public File fakturaSortu(int idEskaera) throws Exception {
        try (Connection konexioa = DB_Konexioa.konektatu()) {
            // Faktura karpeta ziurtatu
            File karpeta = new File(FakturaPDF.FAKTURA_BIDEA);
            if (!karpeta.exists()) {
                karpeta.mkdirs();
            }

            File fakturaFitxategia = new File(karpeta, "faktura_" + idEskaera + ".pdf");

            // Eskaera burua lortu
            String sqlBurua = "SELECT e.id_eskaera, e.data, b.izena_edo_soziala, b.ifz_nan, b.helbidea, b.emaila " +
                    "FROM eskaerak e JOIN bezeroak b ON e.bezeroa_id = b.id_bezeroa WHERE e.id_eskaera = ?";

            try (PreparedStatement pst = konexioa.prepareStatement(sqlBurua)) {
                pst.setInt(1, idEskaera);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        // Bezero datuak
                        BezeroDatuak bezeroa = new BezeroDatuak(
                                rs.getString("izena_edo_soziala"),
                                rs.getString("ifz_nan"),
                                rs.getString("helbidea"),
                                rs.getString("emaila"));

                        Timestamp data = rs.getTimestamp("data");

                        // Lerroak lortu
                        String sqlLerroak = "SELECT p.izena, el.kantitatea, el.unitate_prezioa " +
                                "FROM eskaera_lerroak el JOIN produktuak p ON el.produktua_id = p.id_produktua " +
                                "WHERE el.eskaera_id = ?";

                        try (PreparedStatement pstLerroak = konexioa.prepareStatement(sqlLerroak)) {
                            pstLerroak.setInt(1, idEskaera);
                            try (ResultSet rsLerroak = pstLerroak.executeQuery()) {
                                List<LerroDatuak> lerroak = new ArrayList<>();
                                BigDecimal oinarria = BigDecimal.ZERO;

                                while (rsLerroak.next()) {
                                    String pIzena = rsLerroak.getString("izena");
                                    int kantitatea = rsLerroak.getInt("kantitatea");
                                    BigDecimal prezioa = rsLerroak.getBigDecimal("unitate_prezioa");
                                    BigDecimal totala = prezioa.multiply(new BigDecimal(kantitatea));

                                    oinarria = oinarria.add(totala);
                                    lerroak.add(new LerroDatuak(pIzena, kantitatea, prezioa, totala));
                                }

                                BigDecimal guztira = oinarria.setScale(2, java.math.RoundingMode.HALF_UP);
                                oinarria = oinarria.setScale(2, java.math.RoundingMode.HALF_UP);

                                // PDF Sortu
                                FakturaPDF.sortu(fakturaFitxategia.getAbsolutePath(), idEskaera, data, bezeroa, lerroak,
                                        guztira);

                                // DBan gorde
                                String fakturaZenbakia = "FAK-" + idEskaera + "-" + System.currentTimeMillis();
                                String sqlInsert = "INSERT INTO bezero_fakturak (faktura_zenbakia, eskaera_id, fitxategia_url, data) VALUES (?, ?, ?, NOW()) "
                                        +
                                        "ON DUPLICATE KEY UPDATE faktura_zenbakia = VALUES(faktura_zenbakia), fitxategia_url = VALUES(fitxategia_url), data = NOW()";

                                try (PreparedStatement pstInsert = konexioa.prepareStatement(sqlInsert)) {
                                    pstInsert.setString(1, fakturaZenbakia);
                                    pstInsert.setInt(2, idEskaera);
                                    pstInsert.setString(3, fakturaFitxategia.getAbsolutePath());
                                    pstInsert.executeUpdate();
                                }

                                return fakturaFitxategia;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // BEZEROEN KUDEAKETA
    // -------------------------------------------------------------------------

    /**
     * Bezero berria sortu.
     * 
     * @param b Bezero objektua
     */
    public void bezeroBerriaSortu(Bezeroa b) throws Exception {
        String sql = "INSERT INTO bezeroak (izena_edo_soziala, abizena, ifz_nan, jaiotza_data, sexua, " +
                "bezero_ordainketa_txartela, helbidea, herria_id, posta_kodea, telefonoa, emaila, " +
                "hizkuntza, pasahitza, aktibo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {

            pst.setString(1, b.getIzenaEdoSoziala());
            pst.setString(2, b.getAbizena());
            pst.setString(3, b.getIfzNan());
            pst.setDate(4, b.getJaiotzaData());
            pst.setString(5, b.getSexua());
            pst.setString(6, b.getBezeroOrdainketaTxartela());
            pst.setString(7, b.getHelbidea());
            pst.setInt(8, b.getHerriaId());
            pst.setString(9, b.getPostaKodea());
            pst.setString(10, b.getTelefonoa());
            pst.setString(11, b.getEmaila());
            pst.setString(12, b.getHizkuntza());
            pst.setString(13, b.getPasahitza());
            pst.setBoolean(14, b.isAktibo());

            pst.executeUpdate();
        }
    }

    /**
     * Bezeroaren informazioa editatu.
     * 
     * @param b Bezero objektua (id-a barne)
     */
    public void bezeroaEditatu(Bezeroa b) throws Exception {
        String sql = "UPDATE bezeroak SET izena_edo_soziala=?, abizena=?, ifz_nan=?, jaiotza_data=?, " +
                "sexua=?, bezero_ordainketa_txartela=?, helbidea=?, herria_id=?, posta_kodea=?, " +
                "telefonoa=?, emaila=?, hizkuntza=?, pasahitza=?, aktibo=? WHERE id_bezeroa=?";

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {

            pst.setString(1, b.getIzenaEdoSoziala());
            pst.setString(2, b.getAbizena());
            pst.setString(3, b.getIfzNan());
            pst.setDate(4, b.getJaiotzaData());
            pst.setString(5, b.getSexua());
            pst.setString(6, b.getBezeroOrdainketaTxartela());
            pst.setString(7, b.getHelbidea());
            pst.setInt(8, b.getHerriaId());
            pst.setString(9, b.getPostaKodea());
            pst.setString(10, b.getTelefonoa());
            pst.setString(11, b.getEmaila());
            pst.setString(12, b.getHizkuntza());
            pst.setString(13, b.getPasahitza());
            pst.setBoolean(14, b.isAktibo());
            pst.setInt(15, b.getIdBezeroa());

            pst.executeUpdate();
        }
    }

    /**
     * Bezeroa kendu (ezabatu).
     * 
     * @param idBezeroa Bezeroaren IDa
     */
    public void bezeroaKendu(int idBezeroa) throws Exception {
        String sql = "DELETE FROM bezeroak WHERE id_bezeroa=?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idBezeroa);
            pst.executeUpdate();
        }
    }

    /**
     * Bezeroaren informazioa ikusi.
     * 
     * @param idBezeroa Bezeroaren IDa
     * @return Bezero objektua
     */
    public Bezeroa bezeroaIkusi(int idBezeroa) throws Exception {
        String sql = "SELECT * FROM bezeroak WHERE id_bezeroa=?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idBezeroa);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Bezeroa(
                            rs.getInt("id_bezeroa"),
                            rs.getString("izena_edo_soziala"),
                            rs.getString("abizena"),
                            rs.getString("ifz_nan"),
                            rs.getDate("jaiotza_data"),
                            rs.getString("sexua"),
                            rs.getString("bezero_ordainketa_txartela"),
                            rs.getString("helbidea"),
                            rs.getInt("herria_id"),
                            rs.getString("posta_kodea"),
                            rs.getString("telefonoa"),
                            rs.getString("emaila"),
                            rs.getString("hizkuntza"),
                            rs.getString("pasahitza"),
                            rs.getTimestamp("alta_data"),
                            rs.getTimestamp("eguneratze_data"),
                            rs.getBoolean("aktibo"));
                }
            }
        }
        return null; // Ez da aurkitu
    }

    // -------------------------------------------------------------------------
    // FAKTUREN KUDEAKETA
    // -------------------------------------------------------------------------

    /**
     * Bezeroaren faktura ezabatu.
     * 
     * @param idFaktura Fakturaren IDa
     */
    public void bezeroFakturaEzabatu(int idFaktura) throws Exception {
        String sql = "DELETE FROM bezero_fakturak WHERE id_faktura=?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idFaktura);
            pst.executeUpdate();
        }
    }

    // -------------------------------------------------------------------------
    // PRODUKTUEN KUDEAKETA
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // PRODUKTUEN KUDEAKETA
    // -------------------------------------------------------------------------

    /**
     * Produktuak ikusi motaren arabera iragaziz.
     * 
     * @param mota Produktu mota (adib. 'Eramangarria', 'Mugikorra'...). Null edo
     *             hutsik bada, denak itzuli.
     * @return Produktu zerrenda
     */
    public List<Produktua> produktuakIkusi(String mota) throws Exception {
        List<Produktua> produktuak = new ArrayList<>();
        String sql = "SELECT * FROM produktuak";

        // Iragazkia aplikatu
        if (mota != null && !mota.trim().isEmpty() && !mota.equalsIgnoreCase("Guztiak")) {
            sql += " WHERE mota = ?";
        }

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {

            if (mota != null && !mota.trim().isEmpty() && !mota.equalsIgnoreCase("Guztiak")) {
                pst.setString(1, mota);
            }

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    produktuak.add(instanziatuProduktua(rs));
                }
            }
        }
        return produktuak;
    }

    /**
     * Produktua ikusi.
     * 
     * @param idProduktua Produktuaren IDa
     * @return Produktua objektua
     */
    public Produktua produktuaIkusi(int idProduktua) throws Exception {
        String sql = "SELECT * FROM produktuak WHERE id_produktua=?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idProduktua);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return instanziatuProduktua(rs);
                }
            }
        }
        return null;
    }

    /**
     * ResultSet-etik 'Produktua' azpiklase egokia sortzen duen metodo pribatua.
     * Oharra: Soilik 'produktuak' taulako datuak erabiltzen dira.
     * Azpiklaseen eremu espezifikoak null/0 balioekin hasieratzen dira.
     * 
     * @param rs ResultSet kurtsorea (dagoeneko .next() eginda egon behar du)
     * @return Produktuaren azpiklasearen instantzia (Eramangarria, Mugikorra...)
     * @throws Exception
     */
    private Produktua instanziatuProduktua(ResultSet rs) throws Exception {
        String mota = rs.getString("mota");

        // Komunak diren datuak (Base Constructor arguments)
        int id = rs.getInt("id_produktua");
        int hornitzaileId = rs.getInt("hornitzaile_id");
        int kategoriaId = rs.getInt("kategoria_id");
        String izena = rs.getString("izena");
        String marka = rs.getString("marka");
        // mota already retrieved
        String deskribapena = rs.getString("deskribapena");
        String irudiaUrl = rs.getString("irudia_url");
        Integer biltegiId = (Integer) rs.getObject("biltegi_id");
        String egoera = rs.getString("produktu_egoera");
        String egoeraOharra = rs.getString("produktu_egoera_oharra");
        boolean salgai = rs.getBoolean("salgai");
        BigDecimal prezioa = rs.getBigDecimal("salmenta_prezioa");
        int stock = rs.getInt("stock");
        BigDecimal eskaintza = rs.getBigDecimal("eskaintza");
        BigDecimal zergak = rs.getBigDecimal("zergak_ehunekoa");
        Timestamp sortzeData = rs.getTimestamp("sortze_data");
        Timestamp eguneratzeData = rs.getTimestamp("eguneratze_data");

        switch (mota) {
            case "Eramangarria":
                return new Eramangarria(id, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl,
                        biltegiId, egoera, egoeraOharra, salgai, prezioa, stock, eskaintza, zergak, sortzeData,
                        eguneratzeData,
                        null, 0, 0, null, 0, null, null);
            case "Mugikorra":
                return new Mugikorra(id, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl,
                        biltegiId, egoera, egoeraOharra, salgai, prezioa, stock, eskaintza, zergak, sortzeData,
                        eguneratzeData,
                        null, null, 0, 0, 0, 0, null, null);
            case "Mahai-gainekoa":
                return new MahaiGainekoa(id, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl,
                        biltegiId, egoera, egoeraOharra, salgai, prezioa, stock, eskaintza, zergak, sortzeData,
                        eguneratzeData,
                        null, null, 0, 0, null, 0, null);
            case "Tableta":
                return new Tableta(id, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl,
                        biltegiId, egoera, egoeraOharra, salgai, prezioa, stock, eskaintza, zergak, sortzeData,
                        eguneratzeData,
                        null, 0, null, null, 0, false);
            case "Pantaila":
                return new Pantaila(id, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl,
                        biltegiId, egoera, egoeraOharra, salgai, prezioa, stock, eskaintza, zergak, sortzeData,
                        eguneratzeData,
                        null, null, null, 0, null, null);
            case "Softwarea":
                return new Softwarea(id, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl,
                        biltegiId, egoera, egoeraOharra, salgai, prezioa, stock, eskaintza, zergak, sortzeData,
                        eguneratzeData,
                        null, null, null, null);
            case "Zerbitzaria":
                return new Zerbitzaria(id, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl,
                        biltegiId, egoera, egoeraOharra, salgai, prezioa, stock, eskaintza, zergak, sortzeData,
                        eguneratzeData,
                        0, null, 0, 0, false, null);
            default:
                // Fallback: aurkitu ez den mota bada, edozein kasuan Produktua abstraktua denez
                // klase anonimo batekin itzuliko dugu, behintzat oinarrizko datuak edukitzeko.
                return new Produktua(id, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl,
                        biltegiId, egoera, egoeraOharra, salgai, prezioa, stock, eskaintza, zergak, sortzeData,
                        eguneratzeData) {
                };
        }
    }

    /**
     * Produktuari eskaintza aldatu.
     * 
     * @param idProduktua Produktuaren IDa
     * @param eskaintza   Eskaintza berria
     */
    public void produktuariEskaintzaAldatzeko(int idProduktua, BigDecimal eskaintza) throws Exception {
        String sql = "UPDATE produktuak SET eskaintza=? WHERE id_produktua=?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setBigDecimal(1, eskaintza);
            pst.setInt(2, idProduktua);
            pst.executeUpdate();
        }
    }

    /**
     * Produktua salgai jarri (soilik prezioa definituta badu).
     * 
     * @param idProduktua Produktuaren IDa
     */
    public void produktuaSalgaijarri(int idProduktua) throws Exception {
        // Lehenengo prezioa daukala egiaztatu
        String checkSql = "SELECT salmenta_prezioa FROM produktuak WHERE id_produktua=?";
        boolean prezioaDu = false;

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(checkSql)) {
            pst.setInt(1, idProduktua);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    BigDecimal prezioa = rs.getBigDecimal("salmenta_prezioa");
                    if (prezioa != null && prezioa.compareTo(BigDecimal.ZERO) > 0) {
                        prezioaDu = true;
                    }
                }
            }
        }

        if (prezioaDu) {
            String updateSql = "UPDATE produktuak SET salgai=1 WHERE id_produktua=?";
            try (Connection konexioa = DB_Konexioa.konektatu();
                    PreparedStatement pst = konexioa.prepareStatement(updateSql)) {
                pst.setInt(1, idProduktua);
                pst.executeUpdate();
            }
        } else {
            // Aukerakoa: Salbuespena bota edo mezua erakutsi
            // throw new Exception("Produktuak ez du preziorik eta ezin da salgai jarri.");
        }
    }

    /**
     * Produktuari prezioa jarri.
     * 
     * @param idProduktua Produktuaren IDa
     * @param prezioa     Prezio berria
     */
    public void produktuariPrezioaJarri(int idProduktua, BigDecimal prezioa) throws Exception {
        String sql = "UPDATE produktuak SET salmenta_prezioa=? WHERE id_produktua=?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setBigDecimal(1, prezioa);
            pst.setInt(2, idProduktua);
            pst.executeUpdate();
        }
    }

    // -------------------------------------------------------------------------
    // ESKAERA LERROEN KUDEAKETA
    // -------------------------------------------------------------------------

    /**
     * Eskaera baten lerroak (produktuak) ikusi.
     * 
     * @param idEskaera Eskaeraren IDa
     * @return Eskaera lerroen zerrenda
     */
    public List<EskaeraLerroa> eskaeraLerroakIkusi(int idEskaera) throws SQLException {
        return EskaeraLerroa.eskaeraLerroaIkusi(idEskaera);
    }

    /**
     * Eskaera bati lerro (produktu) berri bat gehitu.
     * 
     * @param idEskaera   Eskaeraren IDa
     * @param idProduktua Produktuaren IDa
     * @param kantitatea  Kantitatea
     * @param prezioa     Unitateko prezioa
     */
    public void eskaeraLerroaGehitu(int idEskaera, int idProduktua, int kantitatea, BigDecimal prezioa)
            throws SQLException {
        // IDa 0 jartzen dugu, DBak autoincrement bidez sortuko baitu
        EskaeraLerroa el = new EskaeraLerroa(0, idEskaera, idProduktua, kantitatea, prezioa, "Prestatzen");
        EskaeraLerroa.eskaeraLerroaSortu(el);
    }

    /**
     * Eskaera lerro bat editatu.
     * 
     * @param idEskaeraLerroa Lerroaren IDa
     * @param idEskaera       Eskaeraren IDa
     * @param idProduktua     Produktuaren IDa
     * @param kantitatea      Kantitatea
     * @param prezioa         Unitateko prezioa
     */
    public void eskaeraLerroakEditatu(int idEskaeraLerroa, int idEskaera, int idProduktua, int kantitatea,
            BigDecimal prezioa) throws SQLException {
        EskaeraLerroa el = new EskaeraLerroa(idEskaeraLerroa, idEskaera, idProduktua, kantitatea, prezioa,
                "Prestatzen");
        EskaeraLerroa.eskaeralerroaEditatu(el);
    }

    /**
     * Eskaera lerro bat ezabatu.
     * 
     * @param idEskaeraLerroa Lerroaren IDa
     */
    public void eskaeraLerroaEzabatu(int idEskaeraLerroa) throws SQLException {
        EskaeraLerroa.eskaeraLerroaEzabatu(idEskaeraLerroa);
    }

    /**
     * Produktuari prezioa aldatu (produktuariPrezioaJarri-ren berdina).
     * 
     * @param idProduktua Produktuaren IDa
     * @param prezioa     Prezio berria
     */
    public void produktuariPrezioaAldatu(int idProduktua, BigDecimal prezioa) throws Exception {
        produktuariPrezioaJarri(idProduktua, prezioa);
    }

    // -------------------------------------------------------------------------
    // ESKAEREN KUDEAKETA
    // -------------------------------------------------------------------------

    /**
     * Eskaeren informazioa ikusi (bezero zehatz batentzako).
     * 
     * @param idBezeroa Bezeroaren IDa
     * @return Eskaera zerrenda
     */
    public List<Eskaera> eskaerakIkusi(int idBezeroa) throws Exception {
        List<Eskaera> eskaerak = new ArrayList<>();
        String sql = "SELECT * FROM eskaerak WHERE bezeroa_id=?";

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idBezeroa);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    eskaerak.add(new Eskaera(
                            rs.getInt("id_eskaera"),
                            rs.getInt("bezeroa_id"),
                            (Integer) rs.getObject("langilea_id"),
                            rs.getTimestamp("data"),
                            rs.getTimestamp("eguneratze_data"),
                            rs.getBigDecimal("guztira_prezioa"),
                            rs.getString("eskaera_egoera")));
                }
            }
        }
        return eskaerak;
    }

    /**
     * Eskaera sortu.
     * README.md
     * 
     * @param e Eskaera objektua
     */
    public void eskaeraSortu(Eskaera e) throws Exception {
        String sql = "INSERT INTO eskaerak (bezeroa_id, langilea_id, data, guztira_prezioa, eskaera_egoera) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {

            pst.setInt(1, e.getBezeroaId());
            if (e.getLangileaId() != null) {
                pst.setInt(2, e.getLangileaId());
            } else {
                pst.setNull(2, java.sql.Types.INTEGER);
            }
            pst.setTimestamp(3, e.getData());
            pst.setBigDecimal(4, e.getGuztiraPrezioa());
            pst.setString(5, e.getEskaeraEgoera());

            pst.executeUpdate();
        }
    }

    /**
     * Eskaera editatu.
     * 
     * @param e Eskaera objektua
     */
    public void eskaeraEditatu(Eskaera e) throws Exception {
        String sql = "UPDATE eskaerak SET bezeroa_id=?, langilea_id=?, data=?, guztira_prezioa=?, " +
                "eskaera_egoera=? WHERE id_eskaera=?";

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {

            pst.setInt(1, e.getBezeroaId());
            if (e.getLangileaId() != null) {
                pst.setInt(2, e.getLangileaId());
            } else {
                pst.setNull(2, java.sql.Types.INTEGER);
            }
            pst.setTimestamp(3, e.getData());
            pst.setBigDecimal(4, e.getGuztiraPrezioa());
            pst.setString(5, e.getEskaeraEgoera());
            pst.setInt(6, e.getIdEskaera());

            pst.executeUpdate();
        }
    }

    /**
     * Eskaera ezabatu.
     * 
     * @param idEskaera Eskaeraren IDa
     */
    public void eskaeraEzabatu(int idEskaera) throws Exception {
        String sql = "DELETE FROM eskaerak WHERE id_eskaera=?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idEskaera);
            pst.executeUpdate();
        }
    }
}
