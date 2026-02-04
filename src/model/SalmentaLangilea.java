package model;

import db.DB_Konexioa;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private static final String FAKTURA_BIDEA = "C:\\Xampp\\htdocs\\fakturak";

    public File fakturaSortu(int idEskaera) throws Exception {
        // Faktura karpeta ziurtatu
        File karpeta = new File(FAKTURA_BIDEA);
        if (!karpeta.exists()) {
            karpeta.mkdirs();
        }

        File fakturaFitxategia = new File(karpeta, "faktura_" + idEskaera + ".pdf");

        // Datuak lortu
        Eskaera eskaera = eskaeraIkusi(idEskaera);
        if (eskaera == null) {
            throw new Exception("Ez da eskaera aurkitu: " + idEskaera);
        }

        Bezeroa bezeroa = bezeroaIkusi(eskaera.getBezeroaId());
        if (bezeroa == null) {
            throw new Exception("Ez da bezeroa aurkitu eskaerarentzat: " + idEskaera);
        }

        List<EskaeraLerroa> lerroak = eskaeraLerroakIkusi(idEskaera);

        // PDF Sortu
        FakturaPDF.sortu(fakturaFitxategia.getAbsolutePath(), eskaera, bezeroa, lerroak);

        // DBan gorde (Eskaera taulan)
        String fakturaZenbakia = "FAK-" + idEskaera + "-" + System.currentTimeMillis();
        String sqlUpdate = "UPDATE eskaerak SET faktura_zenbakia = ?, faktura_url = ? WHERE id_eskaera = ?";

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pstUpdate = konexioa.prepareStatement(sqlUpdate)) {
            pstUpdate.setString(1, fakturaZenbakia);
            pstUpdate.setString(2, fakturaFitxategia.getAbsolutePath());
            pstUpdate.setInt(3, idEskaera);
            pstUpdate.executeUpdate();
        }

        return fakturaFitxategia;
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
        String sqlSelect = "SELECT faktura_url FROM eskaerak WHERE id_eskaera = ?";
        String sqlUpdate = "UPDATE eskaerak SET faktura_zenbakia = NULL, faktura_url = NULL WHERE id_eskaera = ?";

        try (Connection konexioa = DB_Konexioa.konektatu()) {
            // 1. Fitxategia lortu eta ezabatu
            try (PreparedStatement pstSelect = konexioa.prepareStatement(sqlSelect)) {
                pstSelect.setInt(1, idEskaera);
                try (ResultSet rs = pstSelect.executeQuery()) {
                    if (rs.next()) {
                        String bidea = rs.getString("faktura_url");
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

            // 2. DBtik eguneratu (ezabatu beharrean)
            try (PreparedStatement pstUpdate = konexioa.prepareStatement(sqlUpdate)) {
                pstUpdate.setInt(1, idEskaera);
                pstUpdate.executeUpdate();
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
        // In current schema, idFaktura is equivalent to idEskaera
        fakturaEzabatu(idFaktura);
    }

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
        Object bIdObj = rs.getObject("biltegi_id");
        Integer biltegiId = (bIdObj == null) ? null : ((Number) bIdObj).intValue();
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
                    Object langileaIdObj = rs.getObject("langilea_id");
                    Integer langileaId = (langileaIdObj != null) ? ((Number) langileaIdObj).intValue() : null;

                    eskaerak.add(new Eskaera(
                            rs.getInt("id_eskaera"),
                            rs.getInt("bezeroa_id"),
                            langileaId,
                            rs.getTimestamp("data"),
                            rs.getTimestamp("eguneratze_data"),
                            rs.getBigDecimal("guztira_prezioa"),
                            rs.getString("faktura_zenbakia"),
                            rs.getString("faktura_url"),
                            rs.getString("eskaera_egoera")));
                }
            }
        }
        return eskaerak;
    }

    /**
     * Eskaera bat ikusi.
     * 
     * @param idEskaera Eskaeraren IDa
     * @return Eskaera objektua
     * @throws Exception
     */
    public Eskaera eskaeraIkusi(int idEskaera) throws Exception {
        Eskaera eskaera = null;
        String sql = "SELECT * FROM eskaerak WHERE id_eskaera=?";

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idEskaera);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Object langileaIdObj = rs.getObject("langilea_id");
                    Integer langileaId = (langileaIdObj != null) ? ((Number) langileaIdObj).intValue() : null;

                    eskaera = new Eskaera(
                            rs.getInt("id_eskaera"),
                            rs.getInt("bezeroa_id"),
                            langileaId,
                            rs.getTimestamp("data"),
                            rs.getTimestamp("eguneratze_data"),
                            rs.getBigDecimal("guztira_prezioa"),
                            rs.getString("faktura_zenbakia"),
                            rs.getString("faktura_url"),
                            rs.getString("eskaera_egoera"));
                }
            }
        }
        return eskaera;
    }

    /**
     * Eskaera osoa (eskaera + lerroak) sortzen du transakzio bakarrean.
     *
     * @param e       Eskaera objektua.
     * @param lerroak Eskaera lerroen zerrenda.
     * @return Sortutako eskaeraren IDa.
     * @throws SQLException Errorea datu-basean.
     */
    public int eskaeraOsoaSortu(Eskaera e, List<EskaeraLerroa> lerroak) throws SQLException {
        Connection konexioa = null;
        int idEskaera = -1;
        try {
            konexioa = DB_Konexioa.konektatu();
            konexioa.setAutoCommit(false);

            String sqlEskaera = "INSERT INTO eskaerak (bezeroa_id, langilea_id, data, guztira_prezioa, eskaera_egoera) VALUES (?, ?, NOW(), ?, ?)";
            try (PreparedStatement pst = konexioa.prepareStatement(sqlEskaera, Statement.RETURN_GENERATED_KEYS)) {
                pst.setInt(1, e.getBezeroaId());
                if (e.getLangileaId() != null) {
                    pst.setInt(2, e.getLangileaId());
                } else {
                    pst.setNull(2, java.sql.Types.INTEGER);
                }
                pst.setBigDecimal(3, e.getGuztiraPrezioa());
                pst.setString(4, e.getEskaeraEgoera());
                pst.executeUpdate();

                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    idEskaera = rs.getInt(1);
                } else {
                    throw new SQLException("Ez da eskaera IDrik sortu.");
                }
            }

            String sqlLerroa = "INSERT INTO eskaera_lerroak (eskaera_id, produktua_id, kantitatea, unitate_prezioa, eskaera_lerro_egoera) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstLerroa = konexioa.prepareStatement(sqlLerroa)) {
                for (EskaeraLerroa l : lerroak) {
                    pstLerroa.setInt(1, idEskaera);
                    pstLerroa.setInt(2, l.getProduktuaId());
                    pstLerroa.setInt(3, l.getKantitatea());
                    pstLerroa.setBigDecimal(4, l.getUnitatePrezioa());
                    pstLerroa.setString(5, e.getEskaeraEgoera());
                    pstLerroa.addBatch();
                }
                pstLerroa.executeBatch();
            }

            konexioa.commit();
            return idEskaera;
        } catch (SQLException ex) {
            if (konexioa != null)
                konexioa.rollback();
            throw ex;
        } finally {
            if (konexioa != null) {
                konexioa.setAutoCommit(true);
                konexioa.close();
            }
        }
    }

    /**
     * Eskaera osoa (eskaera + lerroak) editatzen du transakzio bakarrean.
     * Lerro zaharrak ezabatu eta berriak sartzen ditu.
     *
     * @param e       Eskaera objektua (id-a barne).
     * @param lerroak Eskaera lerroen zerrenda berria.
     * @throws SQLException Errorea datu-basean.
     */
    public void eskaeraOsoaEditatu(Eskaera e, List<EskaeraLerroa> lerroak) throws SQLException {
        Connection konexioa = null;
        try {
            konexioa = DB_Konexioa.konektatu();
            konexioa.setAutoCommit(false);

            // 1. Eskaera eguneratu
            String sqlUpdate = "UPDATE eskaerak SET bezeroa_id = ?, guztira_prezioa = ?, eskaera_egoera = ?, eguneratze_data = NOW() WHERE id_eskaera = ?";
            try (PreparedStatement pst = konexioa.prepareStatement(sqlUpdate)) {
                pst.setInt(1, e.getBezeroaId());
                pst.setBigDecimal(2, e.getGuztiraPrezioa());
                pst.setString(3, e.getEskaeraEgoera());
                pst.setInt(4, e.getIdEskaera());
                pst.executeUpdate();
            }

            // 2. Lerro zaharrak ezabatu
            String sqlDeleteLerroak = "DELETE FROM eskaera_lerroak WHERE eskaera_id = ?";
            try (PreparedStatement pstDelete = konexioa.prepareStatement(sqlDeleteLerroak)) {
                pstDelete.setInt(1, e.getIdEskaera());
                pstDelete.executeUpdate();
            }

            // 3. Lerro berriak sartu
            String sqlInsertLerroa = "INSERT INTO eskaera_lerroak (eskaera_id, produktua_id, kantitatea, unitate_prezioa, eskaera_lerro_egoera) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstLerroa = konexioa.prepareStatement(sqlInsertLerroa)) {
                for (EskaeraLerroa l : lerroak) {
                    pstLerroa.setInt(1, e.getIdEskaera());
                    pstLerroa.setInt(2, l.getProduktuaId());
                    pstLerroa.setInt(3, l.getKantitatea());
                    pstLerroa.setBigDecimal(4, l.getUnitatePrezioa());
                    pstLerroa.setString(5, e.getEskaeraEgoera());
                    pstLerroa.addBatch();
                }
                pstLerroa.executeBatch();
            }

            konexioa.commit();
        } catch (SQLException ex) {
            if (konexioa != null)
                konexioa.rollback();
            throw ex;
        } finally {
            if (konexioa != null) {
                konexioa.setAutoCommit(true);
                konexioa.close();
            }
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
        Connection konexioa = null;
        try {
            konexioa = DB_Konexioa.konektatu();
            konexioa.setAutoCommit(false);

            // 1. Eskaera lerroak ezabatu (Foreign Key constraint dela eta)
            String sqlLerroak = "DELETE FROM eskaera_lerroak WHERE eskaera_id = ?";
            try (PreparedStatement pstLerroak = konexioa.prepareStatement(sqlLerroak)) {
                pstLerroak.setInt(1, idEskaera);
                pstLerroak.executeUpdate();
            }

            // 2. Eskaera bera ezabatu
            String sqlEskaera = "DELETE FROM eskaerak WHERE id_eskaera = ?";
            try (PreparedStatement pstEskaera = konexioa.prepareStatement(sqlEskaera)) {
                pstEskaera.setInt(1, idEskaera);
                pstEskaera.executeUpdate();
            }

            konexioa.commit();
        } catch (SQLException ex) {
            if (konexioa != null) {
                konexioa.rollback();
            }
            throw ex;
        } finally {
            if (konexioa != null) {
                konexioa.setAutoCommit(true);
                konexioa.close();
            }
        }
    }
}
