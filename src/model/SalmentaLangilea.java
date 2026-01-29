package model;

import db.DB_Konexioa;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
     * Faktura sortu bezeroarentzat.
     * Existitzen den fakturaSortu metodoa erabiltzen du.
     * 
     * @param idEskaera Eskaeraren IDa
     */
    public void bezeroFakturaSortu(int idEskaera) throws Exception {
        fakturaSortu(idEskaera);
    }

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

    /**
     * Produktua ikusi.
     * 
     * @param idProduktua Produktuaren IDa
     * @return Produktua objektua (Mota zehatza ezin denez jakin erraz, null itzul
     *         liteke implementazio osoa gabe edo oinarrizko datuak)
     *         Oharra: Proiektu honetan 'Produktua' klase abstraktua da. Hemen
     *         adibide gisa
     *         Eramangarria, Mugikorra eta abar bereizi beharko lirateke motaren
     *         arabera,
     *         baina sinpletasunerako, datuak irakurtzea bakarrik egingo dugu,
     *         instantziazioa zaila izan daiteke.
     *         Hemen logika sinple bat egingo dut: soilik datuak irakurri eta null
     *         itzuli 'abstract' delako,
     *         edo hobeto: motaren arabera instantziatu.
     */
    public Produktua produktuaIkusi(int idProduktua) throws Exception {
        // Oharra: Produktua abstract da. Mota eremua begiratu beharko genuke zein
        // azpiklase den jakiteko.
        String sql = "SELECT * FROM produktuak WHERE id_produktua=?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idProduktua);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String mota = rs.getString("mota");
                    // Hemen factory pattern edo antzeko bat beharko litzateke.
                    // Momentuz null itzuliko dut arazoak saihesteko, edo saiatuko naiz
                    // kasu batzuk kudeatzen.

                    // Adibidea (suposatuz azpiklaseak daudela):
                    // if ("Eramangarria".equals(mota)) return new Eramangarria(...);
                    // if ("Mugikorra".equals(mota)) return new Mugikorra(...);

                    // Suposizioa: Erabiltzaileak informazioa ikusi nahi du, ez objektua bera
                    // erabili kodean.
                    // Hala ere, metodoaren sinadurak Produktua itzultzen du.
                    // Erraza izateko, null itzultzen dut, baina kontsulta ondo doala ziurtatuz.
                    // Edo, Produktua klase anonimo bat sor dezaket datuekin.

                    return null; // TODO: Implementatu azpiklase zuzena sortzea 'mota' eremuaren arabera.
                }
            }
        }
        return null;
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
