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

/**
 * SalmentaLangilea klasea.
 * Langilea klasearen azpiklasea da, eta salmenta arloko langileen
 * funtzionalitateak kudeatzen ditu.
 * Bezeroak, eskaerak, fakturak eta produktuak kudeatzeko metodoak ditu.
 */
public class SalmentaLangilea extends Langilea {

    /**
     * SalmentaLangilea eraikitzailea.
     * Langilea objektu batetik abiatuta sortzen da.
     * 
     * @param l Langilea objektua.
     */
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
    /**
     * Faktura bat sortzen du eskaera batetik abiatuta.
     * PDF fitxategia sortzen du eta datu-basean erregistratzen du.
     *
     * @param idEskaera Eskaeraren IDa.
     * @return Sortutako PDF fitxategia, edo null errorea gertatu bada.
     * @throws Exception Errorea prozesuan.
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

    /**
     * Faktura ezabatu eskaera IDa erabiliz.
     * 
     * @param idEskaera Eskaeraren IDa
     */
    /**
     * Faktura bat ezabatzen du eskaera IDa erabiliz.
     * Fitxategi fisikoa eta datu-baseko erregistroa ezabatzen ditu.
     *
     * @param idEskaera Eskaeraren IDa.
     * @throws Exception Errorea ezabatzean.
     */
    public void fakturaEzabatu(int idEskaera) throws Exception {
        String sqlSelect = "SELECT fitxategia_url FROM bezero_fakturak WHERE eskaera_id = ?";
        String sqlDelete = "DELETE FROM bezero_fakturak WHERE eskaera_id = ?";

        try (Connection konexioa = DB_Konexioa.konektatu()) {
            // 1. Fitxategia lortu eta ezabatu
            try (PreparedStatement pstSelect = konexioa.prepareStatement(sqlSelect)) {
                pstSelect.setInt(1, idEskaera);
                try (ResultSet rs = pstSelect.executeQuery()) {
                    if (rs.next()) {
                        String bidea = rs.getString("fitxategia_url");
                        if (bidea != null) {
                            File fitxategia = new File(bidea);
                            if (fitxategia.exists()) {
                                fitxategia.delete();
                            }
                        }
                    } else {
                        throw new Exception("Eskaera honek ez du fakturarik.");
                    }
                }
            }

            // 2. DBtik ezabatu
            try (PreparedStatement pstDelete = konexioa.prepareStatement(sqlDelete)) {
                pstDelete.setInt(1, idEskaera);
                pstDelete.executeUpdate();
            }
        }
    }

    // -------------------------------------------------------------------------
    // BEZEROEN KUDEAKETA
    // -------------------------------------------------------------------------

    /**
     * Bezero berria sortu.
     * 
     * @param b Bezero objektua
     */
    /**
     * Bezero berri bat sortzen du datu-basean.
     *
     * @param b Bezeroa objektua.
     * @throws Exception Errorea sortzean.
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
    /**
     * Bezero baten informazioa editatzen du.
     *
     * @param b Bezero objektua (id-a barne).
     * @throws Exception Errorea editatzean.
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
     * Bezero bat ezabatu (Order check included)
     * 
     * @param idBezeroa Bezeroaren IDa
     */
    /**
     * Bezero bat ezabatzen du.
     * Lehenik egiaztatzen du ea eskaerarik duen; badu, ezin da ezabatu.
     *
     * @param idBezeroa Bezeroaren IDa.
     * @throws Exception Errorea ezabatzean edo eskaerak baditu.
     */
    public void bezeroaKendu(int idBezeroa) throws Exception {
        try (Connection konexioa = DB_Konexioa.konektatu()) {
            // Check for existing orders
            try (PreparedStatement pstCheck = konexioa
                    .prepareStatement("SELECT COUNT(*) FROM eskaerak WHERE bezeroa_id = ?")) {
                pstCheck.setInt(1, idBezeroa);
                ResultSet rs = pstCheck.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new Exception("Ezin da bezeroa ezabatu: Eskaerak ditu.");
                }
            }

            String sql = "DELETE FROM bezeroak WHERE id_bezeroa=?";
            try (PreparedStatement pst = konexioa.prepareStatement(sql)) {
                pst.setInt(1, idBezeroa);
                pst.executeUpdate();
            }
        }
    }

    /**
     * Bezeroaren informazioa ikusi.
     * 
     * @param idBezeroa Bezeroaren IDa
     * @return Bezero objektua
     */
    /**
     * Bezero baten informazioa lortzen du.
     *
     * @param idBezeroa Bezeroaren IDa.
     * @return Bezero objektua edo null aurkitzen ez bada.
     * @throws Exception Errorea irakurtzean.
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
    /**
     * Bezero baten faktura ezabatzen du IDaren arabera.
     *
     * @param idFaktura Fakturaren IDa.
     * @throws Exception Errorea ezabatzean.
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
    /**
     * Produktuak ikusten ditu, aukeran motaren arabera iragaziz.
     *
     * @param mota Produktu mota (adib. "Eramangarria", "Guztiak"...). Null edo
     *             hutsik bada, denak itzultzen dira.
     * @return Produktu zerrenda.
     * @throws Exception Errorea datuak lortzean.
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
    /**
     * Produktu baten informazio zehatza lortzen du.
     *
     * @param idProduktua Produktuaren IDa.
     * @return Produktu objektua edo null.
     * @throws Exception Errorea lortzean.
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
    /**
     * ResultSet-etik Produktua azpiklase egokia instantziatzen duen metodo
     * laguntzailea.
     *
     * @param rs ResultSet kurtsorea.
     * @return Produktuaren azpiklasearen instantzia (Eramangarria, Mugikorra...).
     * @throws Exception Errorea sortzean.
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
    /**
     * Produktu bati eskaintza prezioa aldatzen dio.
     *
     * @param idProduktua Produktuaren IDa.
     * @param eskaintza   Eskaintza berria.
     * @throws Exception Errorea eguneratzean.
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
    /**
     * Produktua salgai jartzen du (soilik prezioa definituta badu).
     *
     * @param idProduktua Produktuaren IDa.
     * @throws Exception Errorea eguneratzean.
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
    /**
     * Produktuari salmenta prezioa ezartzen dio.
     *
     * @param idProduktua Produktuaren IDa.
     * @param prezioa     Prezio berria.
     * @throws Exception Errorea eguneratzean.
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
    /**
     * Eskaera baten lerroak (produktuak) lortzen ditu.
     *
     * @param idEskaera Eskaeraren IDa.
     * @return Eskaera lerroen zerrenda.
     * @throws SQLException Errorea datu-basean.
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
    /**
     * Eskaera bati lerro (produktu) berri bat gehitzen dio.
     *
     * @param idEskaera   Eskaeraren IDa.
     * @param idProduktua Produktuaren IDa.
     * @param kantitatea  Kantitatea.
     * @param prezioa     Unitateko prezioa.
     * @throws SQLException Errorea gehitzean.
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
    /**
     * Eskaera lerro bat editatzen du.
     *
     * @param idEskaeraLerroa Lerroaren IDa.
     * @param idEskaera       Eskaeraren IDa.
     * @param idProduktua     Produktuaren IDa.
     * @param kantitatea      Kantitatea.
     * @param prezioa         Unitateko prezioa.
     * @throws SQLException Errorea editatzean.
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
    /**
     * Eskaera lerro bat ezabatzen du.
     *
     * @param idEskaeraLerroa Lerroaren IDa.
     * @throws SQLException Errorea ezabatzean.
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
    /**
     * Produktuari prezioa aldatzen dio (produktuariPrezioaJarri-ren berdina).
     *
     * @param idProduktua Produktuaren IDa.
     * @param prezioa     Prezio berria.
     * @throws Exception Errorea eguneratzean.
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
    /**
     * Bezero baten eskaerak ikusten ditu.
     *
     * @param idBezeroa Bezeroaren IDa.
     * @return Eskaera zerrenda.
     * @throws Exception Errorea lortzean.
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
    /**
     * Eskaera berria sortzen du datu-basean.
     *
     * @param e Eskaera objektua.
     * @throws Exception Errorea sortzean.
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
    /**
     * Eskaera baten datuak editatzen ditu.
     *
     * @param e Eskaera objektua.
     * @throws Exception Errorea editatzean.
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
    /**
     * Eskaera bat ezabatzen du.
     *
     * @param idEskaera Eskaeraren IDa.
     * @throws Exception Errorea ezabatzean.
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
