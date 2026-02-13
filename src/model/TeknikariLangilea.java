package model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import db.DB_Konexioa;

/**
 * TeknikariLangilea klasea.
 * Langilea klasearen azpiklasea da, eta teknikari arloko langileen
 * funtzionalitateak kudeatzen ditu.
 * Produktuen mantenua, konponketak eta akatsen kudeaketa egiteko metodoak ditu.
 */
public class TeknikariLangilea extends Langilea {

    /**
     * TeknikariLangilea eraikitzailea.
     * Langilea objektu batetik abiatuta sortzen da.
     *
     * @param l Langilea objektua.
     */
    public TeknikariLangilea(Langilea l) {
        super(l.getIdLangilea(), l.getIzena(), l.getAbizena(), l.getNan(), l.getJaiotzaData(), l.getHerriaId(),
                l.getHelbidea(), l.getPostaKodea(), l.getTelefonoa(), l.getEmaila(), l.getHizkuntza(),
                l.getPasahitza(), l.getSaltoTxartelaUid(), l.getAltaData(), l.getEguneratzeData(),
                l.isAktibo(), l.getSailaId(), l.getIban(), l.getKurrikuluma());
    }

    // -------------------------------------------------------------------------
    // PRODUKTUEN KUDEAKETA
    // -------------------------------------------------------------------------

    /**
     * Biltegira iritsi diren produktu guztiak bistaratu (salgai daudenak eta ez
     * daudenak).
     *
     * @return Produktu guztien zerrenda.
     */
    /**
     * Biltegira iritsi diren produktu guztiak bistaratu (salgai daudenak eta ez
     * daudenak).
     *
     * @return Produktu guztien zerrenda.
     * @throws SQLException Errorea irakurtzean.
     */
    public List<Produktua> produktuakIkusi() throws SQLException {
        List<Produktua> produktuak = new ArrayList<>();
        String sql = "SELECT * FROM produktuak";

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // Produktua abstract denez, klase anonimo bat erabiltzen dugu instantziatzeko
                // edo base-datuak soilik kargatzeko.
                Produktua p = new Produktua(
                        rs.getInt("id_produktua"),
                        rs.getInt("hornitzaile_id"),
                        rs.getInt("kategoria_id"),
                        rs.getString("izena"),
                        rs.getString("marka"),
                        rs.getString("mota"),
                        rs.getString("deskribapena"),
                        rs.getString("irudia_url"),
                        rs.getInt("biltegi_id"), // Null izan daiteke
                        rs.getString("produktu_egoera"),
                        rs.getString("produktu_egoera_oharra"),
                        rs.getBoolean("salgai"),
                        rs.getBigDecimal("salmenta_prezioa"),
                        rs.getInt("stock"),
                        rs.getBigDecimal("eskaintza"),
                        rs.getBigDecimal("zergak_ehunekoa"),
                        rs.getTimestamp("sortze_data"),
                        rs.getTimestamp("eguneratze_data")) {
                }; // Klase anonimoa
                produktuak.add(p);
            }
        }
        return produktuak;
    }

    /**
     * Produktu bat editatu (egoera eta salgai).
     *
     * @param idProduktua Produktuaren IDa.
     * @param salgai      Salgai dagoen ala ez.
     * @param egoera      Produktuaren egoera ('Berria', 'Berritua A', 'Berritua B',
     *                    'Hondatua', 'Zehazteko').
     */
    /**
     * Produktu bat editatu (egoera eta salgai).
     *
     * @param idProduktua Produktuaren IDa.
     * @param salgai      Salgai dagoen ala ez.
     * @param egoera      Produktuaren egoera ('Berria', 'Berritua A', 'Berritua B',
     *                    'Hondatua', 'Zehazteko').
     * @throws SQLException Errorea editatzean.
     */
    public void produktuaEditatu(int idProduktua, boolean salgai, String egoera) throws SQLException {
        String sql = "UPDATE produktuak SET salgai = ?, produktu_egoera = ? WHERE id_produktua = ?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setBoolean(1, salgai);
            pst.setString(2, egoera);
            pst.setInt(3, idProduktua);
            pst.executeUpdate();
        }
    }

    /**
     * Produktu baten datu guztiak editatu, oinarrizkoak eta azpiklasekoak barne.
     *
     * @param idProduktua Produktuaren IDa.
     * @param datuak      Datu guztiak Map batean.
     * @throws SQLException Errorea editatzean.
     */
    public void produktuaOsorikEditatu(int idProduktua, java.util.Map<String, String> datuak) throws SQLException {
        Connection kon = null;
        try {
            kon = DB_Konexioa.konektatu();
            kon.setAutoCommit(false);

            // 1. Zaharraren mota lortu aldaketak detektatzeko
            String motaZaharra = "";
            String sqlMota = "SELECT mota FROM produktuak WHERE id_produktua = ?";
            try (PreparedStatement pstMM = kon.prepareStatement(sqlMota)) {
                pstMM.setInt(1, idProduktua);
                try (ResultSet rsM = pstMM.executeQuery()) {
                    if (rsM.next()) {
                        motaZaharra = rsM.getString(1);
                    }
                }
            }

            String motaBerria = datuak.getOrDefault("mota", motaZaharra);

            // 2. Oinarrizko datuak eguneratu
            String sqlProd = "UPDATE produktuak SET izena=?, marka=?, kategoria_id=?, mota=?, biltegi_id=?, hornitzaile_id=?, stock=?, produktu_egoera=?, deskribapena=?, irudia_url=?, salgai=?, salmenta_prezioa=?, zergak_ehunekoa=? WHERE id_produktua=?";
            try (PreparedStatement pstP = kon.prepareStatement(sqlProd)) {
                pstP.setString(1, datuak.get("izena"));
                pstP.setString(2, datuak.get("marka"));
                pstP.setInt(3, Integer.parseInt(datuak.getOrDefault("kategoria_id", "1")));
                pstP.setString(4, motaBerria);
                pstP.setInt(5, Integer.parseInt(datuak.getOrDefault("biltegi_id", "1")));
                pstP.setInt(6, Integer.parseInt(datuak.getOrDefault("hornitzaile_id", "1")));
                pstP.setInt(7, Integer.parseInt(datuak.getOrDefault("stock", "0")));
                pstP.setString(8, datuak.getOrDefault("produktu_egoera", "Berria"));
                pstP.setString(9, datuak.get("deskribapena"));
                pstP.setString(10, datuak.get("irudia_url"));
                pstP.setBoolean(11, "true".equalsIgnoreCase(datuak.get("salgai")));
                pstP.setBigDecimal(12, new BigDecimal(datuak.getOrDefault("salmenta_prezioa", "0")));
                pstP.setBigDecimal(13, new BigDecimal(datuak.getOrDefault("zergak_ehunekoa", "21")));
                pstP.setInt(14, idProduktua);
                pstP.executeUpdate();
            }

            // 3. Azpiklasea eguneratu
            eguneratuAzpiklasea(kon, idProduktua, motaBerria, motaZaharra, datuak);

            kon.commit();
        } catch (SQLException | NumberFormatException e) {
            if (kon != null)
                kon.rollback();
            throw new SQLException("Errorea produktua editatzean: " + e.getMessage());
        } finally {
            if (kon != null) {
                kon.setAutoCommit(true);
                kon.close();
            }
        }
    }

    private void eguneratuAzpiklasea(Connection kon, int idProduktua, String motaBerria, String motaZaharra,
            java.util.Map<String, String> datuak) throws SQLException {

        // Mota aldatu bada, zaharra ezabatu eta berria txertatu
        if (!motaBerria.equals(motaZaharra)) {
            String taulaZaharra = lortuTaulaIzena(motaZaharra);
            if (!taulaZaharra.isEmpty()) {
                String sqlDel = "DELETE FROM " + taulaZaharra + " WHERE id_produktua = ?";
                try (PreparedStatement pstD = kon.prepareStatement(sqlDel)) {
                    pstD.setInt(1, idProduktua);
                    pstD.executeUpdate();
                }
            }
            katalogatuAzpiklasea(kon, idProduktua, motaBerria, datuak);
        } else {
            // Mota bera bada, UPDATE egin
            eguneratuDatuEspezifikoak(kon, idProduktua, motaBerria, datuak);
        }
    }

    private void eguneratuDatuEspezifikoak(Connection kon, int idProduktua, String mota,
            java.util.Map<String, String> datuak) throws SQLException {
        String sql = "";
        PreparedStatement pst = null;

        switch (mota) {
            case "Eramangarria":
                sql = "UPDATE eramangarriak SET prozesadorea=?, ram_gb=?, diskoa_gb=?, pantaila_tamaina=?, bateria_wh=?, sistema_eragilea=?, pisua_kg=? WHERE id_produktua=?";
                pst = kon.prepareStatement(sql);
                pst.setString(1, datuak.get("prozesadorea"));
                pst.setInt(2, parseIntSafe(datuak.get("ram_gb")));
                pst.setInt(3, parseIntSafe(datuak.get("diskoa_gb")));
                pst.setBigDecimal(4, parseBigDecimalSafe(datuak.get("pantaila_tamaina")));
                pst.setInt(5, parseIntSafe(datuak.get("bateria_wh")));
                pst.setString(6, datuak.get("sistema_eragilea"));
                pst.setBigDecimal(7, parseBigDecimalSafe(datuak.get("pisua_kg")));
                pst.setInt(8, idProduktua);
                break;
            case "Mahai-gainekoa":
                sql = "UPDATE mahai_gainekoak SET prozesadorea=?, plaka_basea=?, ram_gb=?, diskoa_gb=?, txartel_grafikoa=?, elikatze_iturria_w=?, kaxa_formatua=? WHERE id_produktua=?";
                pst = kon.prepareStatement(sql);
                pst.setString(1, datuak.get("prozesadorea"));
                pst.setString(2, datuak.get("plaka_basea"));
                pst.setInt(3, parseIntSafe(datuak.get("ram_gb")));
                pst.setInt(4, parseIntSafe(datuak.get("diskoa_gb")));
                pst.setString(5, datuak.get("txartel_grafikoa"));
                pst.setInt(6, parseIntSafe(datuak.get("elikatze_iturria_w")));
                pst.setString(7, datuak.getOrDefault("kaxa_formatua", "ATX"));
                pst.setInt(8, idProduktua);
                break;
            case "Mugikorra":
                sql = "UPDATE mugikorrak SET pantaila_teknologia=?, pantaila_hazbeteak=?, biltegiratzea_gb=?, ram_gb=?, kamera_nagusa_mp=?, bateria_mah=?, sistema_eragilea=?, sareak=? WHERE id_produktua=?";
                pst = kon.prepareStatement(sql);
                pst.setString(1, datuak.get("pantaila_teknologia"));
                pst.setBigDecimal(2, parseBigDecimalSafe(datuak.get("pantaila_hazbeteak")));
                pst.setInt(3, parseIntSafe(datuak.get("biltegiratzea_gb")));
                pst.setInt(4, parseIntSafe(datuak.get("ram_gb")));
                pst.setInt(5, parseIntSafe(datuak.get("kamera_nagusa_mp")));
                pst.setInt(6, parseIntSafe(datuak.get("bateria_mah")));
                pst.setString(7, datuak.get("sistema_eragilea"));
                pst.setString(8, datuak.getOrDefault("sareak", "4G"));
                pst.setInt(9, idProduktua);
                break;
            case "Tableta":
                sql = "UPDATE tabletak SET pantaila_hazbeteak=?, biltegiratzea_gb=?, konektibitatea=?, sistema_eragilea=?, bateria_mah=?, arkatzarekin_bateragarria=? WHERE id_produktua=?";
                pst = kon.prepareStatement(sql);
                pst.setBigDecimal(1, parseBigDecimalSafe(datuak.get("pantaila_hazbeteak")));
                pst.setInt(2, parseIntSafe(datuak.get("biltegiratzea_gb")));
                pst.setString(3, datuak.getOrDefault("konektibitatea", "WiFi"));
                pst.setString(4, datuak.get("sistema_eragilea"));
                pst.setInt(5, parseIntSafe(datuak.get("bateria_mah")));
                pst.setBoolean(6, "true".equalsIgnoreCase(datuak.get("arkatzarekin_bateragarria")));
                pst.setInt(7, idProduktua);
                break;
            case "Zerbitzaria":
                sql = "UPDATE zerbitzariak SET prozesadore_nukleoak=?, ram_mota=?, disko_badiak=?, rack_unitateak=?, elikatze_iturri_erredundantea=?, raid_kontroladora=? WHERE id_produktua=?";
                pst = kon.prepareStatement(sql);
                pst.setInt(1, parseIntSafe(datuak.get("prozesadore_nukleoak")));
                pst.setString(2, datuak.getOrDefault("ram_mota", "DDR4"));
                pst.setInt(3, parseIntSafe(datuak.get("disko_badiak")));
                pst.setInt(4, parseIntSafe(datuak.get("rack_unitateak")));
                pst.setBoolean(5, "true".equalsIgnoreCase(datuak.get("elikatze_iturri_erredundantea")));
                pst.setString(6, datuak.get("raid_kontroladora"));
                pst.setInt(7, idProduktua);
                break;
            case "Pantaila":
                sql = "UPDATE pantailak SET hazbeteak=?, bereizmena=?, panel_mota=?, freskatze_tasa_hz=?, konexioak=?, kurbatura=? WHERE id_produktua=?";
                pst = kon.prepareStatement(sql);
                pst.setBigDecimal(1, parseBigDecimalSafe(datuak.get("hazbeteak")));
                pst.setString(2, datuak.get("bereizmena"));
                pst.setString(3, datuak.getOrDefault("panel_mota", "IPS"));
                pst.setInt(4, parseIntSafe(datuak.get("freskatze_tasa_hz")));
                pst.setString(5, datuak.get("konexioak"));
                pst.setString(6, datuak.get("kurbatura"));
                pst.setInt(7, idProduktua);
                break;
            case "Softwarea":
                sql = "UPDATE softwareak SET software_mota=?, lizentzia_mota=?, bertsioa=?, garatzailea=?, librea=? WHERE id_produktua=?";
                pst = kon.prepareStatement(sql);
                pst.setString(1, datuak.get("software_mota"));
                pst.setString(2, datuak.getOrDefault("lizentzia_mota", "Retail"));
                pst.setString(3, datuak.get("bertsioa"));
                pst.setString(4, datuak.get("garatzailea"));
                pst.setBoolean(5, "true".equalsIgnoreCase(datuak.get("librea")));
                pst.setInt(6, idProduktua);
                break;
        }

        if (pst != null) {
            pst.executeUpdate();
            pst.close();
        }
    }

    private String lortuTaulaIzena(String mota) {
        if (mota == null)
            return "";
        switch (mota) {
            case "Eramangarria":
                return "eramangarriak";
            case "Mahai-gainekoa":
                return "mahai_gainekoak";
            case "Mugikorra":
                return "mugikorrak";
            case "Tableta":
                return "tabletak";
            case "Zerbitzaria":
                return "zerbitzariak";
            case "Pantaila":
                return "pantailak";
            case "Softwarea":
                return "softwareak";
            default:
                return "";
        }
    }

    /**
     * Produktuari irudia gehitu.
     *
     * @param idProduktua Produktuaren IDa.
     * @param irudiaUrl   Irudiaren izena edo bidea (jpg).
     */
    /**
     * Produktuari irudia gehitu.
     *
     * @param idProduktua Produktuaren IDa.
     * @param irudiaUrl   Irudiaren izena edo bidea (jpg).
     * @throws SQLException Errorea eguneratzean.
     */
    public void produktuariIrudiaGehitu(int idProduktua, String irudiaUrl) throws SQLException {
        String sql = "UPDATE produktuak SET irudia_url = ? WHERE id_produktua = ?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setString(1, irudiaUrl);
            pst.setInt(2, idProduktua);
            pst.executeUpdate();
        }
    }

    /**
     * Produktuari prezioa eta eskaintza ezarri.
     *
     * @param idProduktua Produktuaren IDa.
     * @param prezioa     Salmenta prezioa.
     * @param eskaintza   Eskaintza prezioa (baldin badago).
     */
    /**
     * Produktuari prezioa eta eskaintza ezarri.
     * 
     * @param idProduktua Produktuaren IDa.
     * @param prezioa     Salmenta prezioa.
     * @param eskaintza   Eskaintza prezioa (baldin badago).
     * @throws SQLException Errorea eguneratzean.
     */
    public void prezioaEzarri(int idProduktua, BigDecimal prezioa, BigDecimal eskaintza) throws SQLException {
        String sql = "UPDATE produktuak SET salmenta_prezioa = ?, eskaintza = ? WHERE id_produktua = ?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setBigDecimal(1, prezioa);
            pst.setBigDecimal(2, eskaintza);
            pst.setInt(3, idProduktua);
            pst.executeUpdate();
        }
    }

    /**
     * Produktu bat sistematik ezabatu.
     * 
     * @param idProduktua Produktuaren IDa.
     */
    /**
     * Produktu bat sistematik ezabatu.
     * 
     * @param idProduktua Produktuaren IDa.
     * @throws SQLException Errorea ezabatzean.
     */
    public void produktuaBorratu(int idProduktua) throws SQLException {
        String sql = "DELETE FROM produktuak WHERE id_produktua = ?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idProduktua);
            pst.executeUpdate();
        }
    }

    // -------------------------------------------------------------------------
    // KONPONKETEN KUDEAKETA
    // -------------------------------------------------------------------------

    /**
     * Konponketa guztiak bistaratu.
     * 
     * @return Konponketa zerrenda.
     */
    /**
     * Konponketa guztiak bistaratu.
     * 
     * @return Konponketa zerrenda.
     * @throws SQLException Errorea irakurtzean.
     */
    public List<Konponketa> konponketakIkusi() throws SQLException {
        List<Konponketa> konponketak = new ArrayList<>();
        String sql = "SELECT * FROM konponketak";

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Konponketa k = new Konponketa(
                        rs.getInt("id_konponketa"),
                        rs.getInt("produktua_id"),
                        rs.getInt("langilea_id"),
                        rs.getTimestamp("hasiera_data"),
                        rs.getTimestamp("amaiera_data"),
                        rs.getString("konponketa_egoera"),
                        rs.getInt("akatsa_id"),
                        rs.getString("oharrak"),
                        rs.getTimestamp("eguneratze_data"));
                konponketak.add(k);
            }
        }
        return konponketak;
    }

    /**
     * Konponketa berri bat sortu.
     *
     * @param k Konponketa objektua.
     */
    /**
     * Konponketa berri bat sortu.
     *
     * @param k Konponketa objektua.
     * @throws SQLException Errorea sortzean.
     */
    public void konponketaEgin(Konponketa k) throws SQLException {
        String sql = "INSERT INTO konponketak (produktua_id, langilea_id, hasiera_data, konponketa_egoera, akatsa_id, oharrak) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, k.getProduktuaId());
            pst.setInt(2, k.getLangileaId());
            pst.setTimestamp(3, k.getHasieraData());
            pst.setString(4, k.getKonponketaEgoera());
            pst.setInt(5, k.getAkatsaId());
            pst.setString(6, k.getOharrak());
            pst.executeUpdate();
        }
    }

    /**
     * Sortuta dagoen konponketa baten datuak aldatu.
     * 
     * @param k Konponketa objektua (id-a barne).
     */
    /**
     * Sortuta dagoen konponketa baten datuak aldatu.
     * 
     * @param k Konponketa objektua (id-a barne).
     * @throws SQLException Errorea editatzean.
     */
    public void konponketaEditatu(Konponketa k) throws SQLException {
        String sql = "UPDATE konponketak SET produktua_id = ?, langilea_id = ?, hasiera_data = ?, amaiera_data = ?, konponketa_egoera = ?, akatsa_id = ?, oharrak = ? WHERE id_konponketa = ?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, k.getProduktuaId());
            pst.setInt(2, k.getLangileaId());
            pst.setTimestamp(3, k.getHasieraData());
            pst.setTimestamp(4, k.getAmaieraData());
            pst.setString(5, k.getKonponketaEgoera());
            pst.setInt(6, k.getAkatsaId());
            pst.setString(7, k.getOharrak());
            pst.setInt(8, k.getIdKonponketa());
            pst.executeUpdate();
        }
    }

    /**
     * Konponketa jakin bat ezabatu.
     * 
     * @param idKonponketa Konponketaren IDa.
     */
    /**
     * Konponketa jakin bat ezabatu.
     * 
     * @param idKonponketa Konponketaren IDa.
     * @throws SQLException Errorea ezabatzean.
     */
    public void konponketaEzabatu(int idKonponketa) throws SQLException {
        String sql = "DELETE FROM konponketak WHERE id_konponketa = ?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idKonponketa);
            pst.executeUpdate();
        }
    }

    /**
     * Produktu berri bat sortu (Bakarrik TeknikariLangilea eta BiltegiLangilea-k
     * egin dezakete).
     */
    /**
     * Produktu berri bat sortu (Bakarrik TeknikariLangilea eta BiltegiLangilea-k
     * egin dezakete).
     * 
     * @param p Produktua
     * @throws SQLException Errorea sortzean.
     */
    public void produktuBatSortu(Produktua p) throws SQLException {
        // Oharra: Metodo honek produktu bat sortzen du 'produktuak' taulan.
        // Ez du stock sarrerarik gestionatzen (hori BiltegiLangileak egiten du
        // Sarrera bidez).
        String sql = "INSERT INTO produktuak (izena, marka, kategoria_id, mota, biltegi_id, hornitzaile_id, stock, produktu_egoera, deskribapena, irudia_url, produktu_egoera_oharra, salgai, salmenta_prezioa, zergak_ehunekoa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql)) {

            pst.setString(1, p.getIzena());
            pst.setString(2, p.getMarka());
            pst.setInt(3, p.getKategoriaId());
            pst.setString(4, p.getMota());
            if (p.getBiltegiId() != null) {
                pst.setInt(5, p.getBiltegiId());
            } else {
                pst.setNull(5, Types.INTEGER);
            }
            pst.setInt(6, p.getHornitzaileId());
            pst.setInt(7, p.getStock());
            pst.setString(8, p.getProduktuEgoera());
            pst.setString(9, p.getDeskribapena());
            pst.setString(10, p.getIrudiaUrl());
            pst.setString(11, p.getProduktuEgoeraOharra());
            pst.setBoolean(12, p.isSalgai());
            pst.setBigDecimal(13, p.getSalmentaPrezioa());
            pst.setBigDecimal(14, p.getZergakEhunekoa());

            pst.executeUpdate();
        }
    }
    // -------------------------------------------------------------------------
    // AKATSAK KUDEAKETA
    // -------------------------------------------------------------------------

    /**
     * Akatsen informazioa ikusi.
     *
     * @return Akatsen zerrenda.
     */
    /**
     * Akatsen informazioa ikusi.
     *
     * @return Akatsen zerrenda.
     * @throws SQLException Errorea irakurtzean.
     */
    public List<Akatsa> akatsaIkusi() throws SQLException {
        List<Akatsa> akatsak = new ArrayList<>();
        String sql = "SELECT * FROM akatsak";

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Akatsa a = new Akatsa(
                        rs.getInt("id_akatsa"),
                        rs.getString("izena"),
                        rs.getString("deskribapena"));
                akatsak.add(a);
            }
        }
        return akatsak;
    }

    /**
     * Akats berri bat sortu.
     *
     * @param a Akatsa objektua.
     */
    /**
     * Akats berri bat sortu.
     *
     * @param a Akatsa objektua.
     * @throws SQLException Errorea sortzean.
     */
    public void akatsaSortu(Akatsa a) throws SQLException {
        String sql = "INSERT INTO akatsak (izena, deskribapena) VALUES (?, ?)";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setString(1, a.getIzena());
            pst.setString(2, a.getDeskribapena());
            pst.executeUpdate();
        }
    }

    /**
     * Akats bat editatu.
     *
     * @param a Akatsa objektua (id-a barne).
     */
    /**
     * Akats bat editatu.
     *
     * @param a Akatsa objektua (id-a barne).
     * @throws SQLException Errorea editatzean.
     */
    public void akatsaEditatu(Akatsa a) throws SQLException {
        String sql = "UPDATE akatsak SET izena = ?, deskribapena = ? WHERE id_akatsa = ?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setString(1, a.getIzena());
            pst.setString(2, a.getDeskribapena());
            pst.setInt(3, a.getIdAkatsa());
            pst.executeUpdate();
        }
    }

    /**
     * Akats bat ezabatu.
     *
     * @param idAkatsa Akatsaren IDa.
     */
    /**
     * Akats bat ezabatu.
     *
     * @param idAkatsa Akatsaren IDa.
     * @throws SQLException Errorea ezabatzean.
     */
    public void akatsaEzabatu(int idAkatsa) throws SQLException {
        String sql = "DELETE FROM akatsak WHERE id_akatsa = ?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idAkatsa);
            pst.executeUpdate();
        }
    }
    // -------------------------------------------------------------------------
    // KATALOGATZEA (JSON DATUAK)
    // -------------------------------------------------------------------------

    /**
     * Katalogatu gabeko sarrera lerroak lortu (Produktua ID null dutenak eta egoera
     * 'Jasota').
     *
     * @return SarreraLerroa zerrenda.
     * @throws SQLException Errorea datuak irakurtzean.
     */
    public List<SarreraLerroa> getKatalogatuGabekoLerroak() throws SQLException {
        List<SarreraLerroa> lerroak = new ArrayList<>();
        String sql = "SELECT * FROM sarrera_lerroak WHERE sarrera_lerro_egoera = 'Jasota' AND produktua_id IS NULL";

        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                // Integer produktuaId = (Integer) rs.getObject("produktua_id"); // null izan
                // daiteke
                // JDBC-k null kudeatzen du getInt-ekin 0 itzuliz wasNull() gabe, baina
                // getObject seguruagoa da Integer-erako
                int id = rs.getInt("produktua_id");
                Integer produktuaId = rs.wasNull() ? null : id;

                lerroak.add(new SarreraLerroa(
                        rs.getInt("id_sarrera_lerroa"),
                        rs.getInt("sarrera_id"),
                        produktuaId,
                        rs.getInt("kantitatea"),
                        rs.getString("sarrera_lerro_egoera"),
                        rs.getString("produktu_berria_datuak")));
            }
        }
        return lerroak;
    }

    /**
     * Sarrera lerro bateko JSON datuak erabiliz produktua sortu eta katalogatu.
     *
     * @param sarreraLerroaId Sarrera lerroaren IDa.
     * @param jsonDatuak      Produktuaren datuak JSON formatuan.
     * @throws SQLException Errorea sortzean.
     */
    public void produktuaKatalogatu(int sarreraLerroaId, String jsonDatuak) throws SQLException {
        if (jsonDatuak == null || jsonDatuak.isEmpty()) {
            throw new SQLException("Ez dago produktu daturik (JSON hutsa).");
        }

        // 1. JSON parseatu
        java.util.Map<String, String> datuak = parseJson(jsonDatuak);

        // 2. Oinarrizko datuak atera
        String mota = datuak.get("mota");
        String izena = datuak.get("izena");
        String marka = datuak.get("marka");
        int kIdRaw = 0;
        try {
            kIdRaw = Integer.parseInt(datuak.get("kategoria_id"));
        } catch (Exception e) {
        }
        int kategoriaId = kIdRaw;
        int hornitzaileId = 1;
        int biltegiId = 1;

        int stock = parseIntSafe(datuak.get("stock")); // Edo sarrera_lerrotik hartu kantitatea?
        BigDecimal prezioa = parseBigDecimalSafe(datuak.get("salmenta_prezioa"));
        String deskribapena = datuak.get("deskribapena");
        String irudiaUrl = datuak.get("irudia_url");

        // 3. Produktua sortu DBan eta IDa lortu
        Connection kon = null;
        try {
            kon = DB_Konexioa.konektatu();
            kon.setAutoCommit(false);

            // Balioztatu kategoria IDa (Robustness)
            kategoriaId = lortuKategoriaIdBalidoa(kon, kategoriaId, mota);

            // HORNITZAILEA: Sarrera taulatik lortu (JSONekoa baztertu)
            String sqlGetHornitzailea = "SELECT s.hornitzailea_id FROM sarrerak s JOIN sarrera_lerroak sl ON s.id_sarrera = sl.sarrera_id WHERE sl.id_sarrera_lerroa = ?";
            try (PreparedStatement pstH = kon.prepareStatement(sqlGetHornitzailea)) {
                pstH.setInt(1, sarreraLerroaId);
                try (ResultSet rsH = pstH.executeQuery()) {
                    if (rsH.next()) {
                        hornitzaileId = rsH.getInt(1);
                    }
                }
            }

            // BILTEGIA: 1 (Harrera Biltegia) default
            biltegiId = 1;

            // A. Txertatu produktuak taulan
            String sqlProd = "INSERT INTO produktuak (izena, marka, kategoria_id, mota, biltegi_id, hornitzaile_id, stock, produktu_egoera, deskribapena, irudia_url, salgai, salmenta_prezioa, zergak_ehunekoa) VALUES (?, ?, ?, ?, ?, ?, ?, 'Berria', ?, ?, ?, ?, ?)";
            PreparedStatement pstProd = kon.prepareStatement(sqlProd, java.sql.Statement.RETURN_GENERATED_KEYS);
            pstProd.setString(1, izena);
            pstProd.setString(2, marka);
            pstProd.setInt(3, kategoriaId);
            pstProd.setString(4, mota);
            pstProd.setInt(5, biltegiId);
            pstProd.setInt(6, hornitzaileId);
            pstProd.setInt(7, stock);
            pstProd.setString(8, deskribapena);
            pstProd.setString(9, irudiaUrl);
            pstProd.setBoolean(10, true); // Salgai jarri zuzenean?
            pstProd.setBigDecimal(11, prezioa);
            pstProd.setBigDecimal(12, new BigDecimal("21.00")); // BEZ

            pstProd.executeUpdate();
            ResultSet rsKeys = pstProd.getGeneratedKeys();
            int idProduktua = -1;
            if (rsKeys.next()) {
                idProduktua = rsKeys.getInt(1);
            } else {
                throw new SQLException("Ez da produktuaren IDa sortu.");
            }

            // B. Txertatu azpiklase taulan
            katalogatuAzpiklasea(kon, idProduktua, mota, datuak);

            // C. Eguneratu SarreraLerroa
            String sqlUpdLerroa = "UPDATE sarrera_lerroak SET produktua_id = ? WHERE id_sarrera_lerroa = ?";
            PreparedStatement pstLerroa = kon.prepareStatement(sqlUpdLerroa);
            pstLerroa.setInt(1, idProduktua);
            pstLerroa.setInt(2, sarreraLerroaId);
            pstLerroa.executeUpdate();

            kon.commit();

        } catch (SQLException | NumberFormatException e) {
            if (kon != null) {
                kon.rollback();
            }
            throw new SQLException("Errorea katalogatzean: " + e.getMessage());
        } finally {
            if (kon != null) {
                kon.setAutoCommit(true);
                kon.close();
            }
        }
    }

    private void katalogatuAzpiklasea(Connection kon, int idProduktua, String mota,
            java.util.Map<String, String> datuak)
            throws SQLException {
        String sql = "";
        PreparedStatement pst = null;

        switch (mota) {
            case "Eramangarria":
                sql = "INSERT INTO eramangarriak (id_produktua, prozesadorea, ram_gb, diskoa_gb, pantaila_tamaina, bateria_wh, sistema_eragilea, pisua_kg) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                pst = kon.prepareStatement(sql);
                pst.setInt(1, idProduktua);
                pst.setString(2, datuak.getOrDefault("prozesadorea", ""));
                pst.setInt(3, Integer.parseInt(datuak.getOrDefault("ram_gb", "0")));
                pst.setInt(4, Integer.parseInt(datuak.getOrDefault("diskoa_gb", "0")));
                pst.setBigDecimal(5, new BigDecimal(datuak.getOrDefault("pantaila_tamaina", "0")));
                pst.setInt(6, Integer.parseInt(datuak.getOrDefault("bateria_wh", "0")));
                pst.setString(7, datuak.getOrDefault("sistema_eragilea", ""));
                pst.setBigDecimal(8, new BigDecimal(datuak.getOrDefault("pisua_kg", "0")));
                break;
            // Bestelako motak hemen gehitu daitezke...
            case "Mahai-gainekoa":
                sql = "INSERT INTO mahai_gainekoak (id_produktua, prozesadorea, plaka_basea, ram_gb, diskoa_gb, txartel_grafikoa, elikatze_iturria_w, kaxa_formatua) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                pst = kon.prepareStatement(sql);
                pst.setInt(1, idProduktua);
                pst.setString(2, datuak.getOrDefault("prozesadorea", ""));
                pst.setString(3, datuak.getOrDefault("plaka_basea", ""));
                pst.setInt(4, Integer.parseInt(datuak.getOrDefault("ram_gb", "0")));
                pst.setInt(5, Integer.parseInt(datuak.getOrDefault("diskoa_gb", "0")));
                pst.setString(6, datuak.getOrDefault("txartel_grafikoa", ""));
                pst.setInt(7, Integer.parseInt(datuak.getOrDefault("elikatze_iturria_w", "0")));
                pst.setString(8, datuak.getOrDefault("kaxa_formatua", "ATX"));
                break;
            case "Mugikorra":
                sql = "INSERT INTO mugikorrak (id_produktua, pantaila_teknologia, pantaila_hazbeteak, biltegiratzea_gb, ram_gb, kamera_nagusa_mp, bateria_mah, sistema_eragilea, sareak) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pst = kon.prepareStatement(sql);
                pst.setInt(1, idProduktua);
                pst.setString(2, datuak.getOrDefault("pantaila_teknologia", ""));
                pst.setBigDecimal(3, new BigDecimal(datuak.getOrDefault("pantaila_hazbeteak", "0")));
                pst.setInt(4, Integer.parseInt(datuak.getOrDefault("biltegiratzea_gb", "0")));
                pst.setInt(5, Integer.parseInt(datuak.getOrDefault("ram_gb", "0")));
                pst.setInt(6, Integer.parseInt(datuak.getOrDefault("kamera_nagusa_mp", "0")));
                pst.setInt(7, Integer.parseInt(datuak.getOrDefault("bateria_mah", "0")));
                pst.setString(8, datuak.getOrDefault("sistema_eragilea", ""));
                pst.setString(9, datuak.getOrDefault("sareak", "4G"));
                break;
            case "Tableta":
                sql = "INSERT INTO tabletak (id_produktua, pantaila_hazbeteak, biltegiratzea_gb, konektibitatea, sistema_eragilea, bateria_mah, arkatzarekin_bateragarria) VALUES (?, ?, ?, ?, ?, ?, ?)";
                pst = kon.prepareStatement(sql);
                pst.setInt(1, idProduktua);
                pst.setBigDecimal(2, parseBigDecimalSafe(datuak.get("pantaila_hazbeteak")));
                pst.setInt(3, parseIntSafe(datuak.get("biltegiratzea_gb")));
                pst.setString(4, datuak.getOrDefault("konektibitatea", "WiFi"));
                pst.setString(5, datuak.get("sistema_eragilea"));
                pst.setInt(6, parseIntSafe(datuak.get("bateria_mah")));
                pst.setBoolean(7, "true".equalsIgnoreCase(datuak.get("arkatzarekin_bateragarria")));
                break;
            case "Zerbitzaria":
                sql = "INSERT INTO zerbitzariak (id_produktua, prozesadore_nukleoak, ram_mota, disko_badiak, rack_unitateak, elikatze_iturri_erredundantea, raid_kontroladora) VALUES (?, ?, ?, ?, ?, ?, ?)";
                pst = kon.prepareStatement(sql);
                pst.setInt(1, idProduktua);
                pst.setInt(2, parseIntSafe(datuak.get("prozesadore_nukleoak")));
                pst.setString(3, datuak.getOrDefault("ram_mota", "DDR4"));
                pst.setInt(4, parseIntSafe(datuak.get("disko_badiak")));
                pst.setInt(5, parseIntSafe(datuak.get("rack_unitateak")));
                pst.setBoolean(6, "true".equalsIgnoreCase(datuak.get("elikatze_iturri_erredundantea")));
                pst.setString(7, datuak.get("raid_kontroladora"));
                break;
            case "Pantaila":
                sql = "INSERT INTO pantailak (id_produktua, hazbeteak, bereizmena, panel_mota, freskatze_tasa_hz, konexioak, kurbatura) VALUES (?, ?, ?, ?, ?, ?, ?)";
                pst = kon.prepareStatement(sql);
                pst.setInt(1, idProduktua);
                pst.setBigDecimal(2, parseBigDecimalSafe(datuak.getOrDefault("hazbeteak", "0")));
                pst.setString(3, datuak.get("bereizmena"));
                pst.setString(4, datuak.getOrDefault("panel_mota", "IPS"));
                pst.setInt(5, parseIntSafe(datuak.get("freskatze_tasa_hz")));
                pst.setString(6, datuak.get("konexioak"));
                pst.setString(7, datuak.get("kurbatura"));
                break;
            case "Softwarea":
                sql = "INSERT INTO softwareak (id_produktua, software_mota, lizentzia_mota, bertsioa, garatzailea, librea) VALUES (?, ?, ?, ?, ?, ?)";
                pst = kon.prepareStatement(sql);
                pst.setInt(1, idProduktua);
                pst.setString(2, datuak.get("software_mota"));
                pst.setString(3, datuak.getOrDefault("lizentzia_mota", "Retail"));
                pst.setString(4, datuak.get("bertsioa"));
                pst.setString(5, datuak.get("garatzailea"));
                pst.setBoolean(6, "true".equalsIgnoreCase(datuak.get("librea")));
                break;
            default:
                break;
        }

        if (pst != null) {
            pst.executeUpdate();
            pst.close();
        }
    }

    private java.util.Map<String, String> parseJson(String json) {
        java.util.Map<String, String> map = new java.util.HashMap<>();
        // Kendu giltzak {}
        String content = json.trim();
        if (content.startsWith("{"))
            content = content.substring(1);
        if (content.endsWith("}"))
            content = content.substring(0, content.length() - 1);

        // Sinpleena: komaz banatu (kontuz koma duten string-ekin, baina oraingoz
        // sinple mantenduko dugu)
        // Hobekuntza: Regex erabili \"(.*?)\"\s*:\s*(\".*?\"|[^,]*)
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\"([^\"]+)\"\\s*:\\s*(\"[^\"]*\"|[^,]+)");
        java.util.regex.Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2).trim();

            // String bada, kendu komatxoak
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            map.put(key, value);
        }
        return map;
    }

    /**
     * Kategoria IDa balioztatu eta existitzen ez bada motaren arabera mapatu.
     */
    private int lortuKategoriaIdBalidoa(Connection kon, int kId, String mota) {
        String sql = "SELECT 1 FROM produktu_kategoriak WHERE id_kategoria = ?";
        try (PreparedStatement pst = kon.prepareStatement(sql)) {
            pst.setInt(1, kId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return kId; // Existitzen bada, itzuli
                }
            }
        } catch (SQLException e) {
        }

        if (mota == null)
            return 4;
        switch (mota) {
            case "Eramangarria":
            case "Mahai-gainekoa":
                return 1;
            case "Mugikorra":
            case "Tableta":
                return 2;
            case "Pantaila":
                return 3;
            case "Osagarria":
                return 4;
            case "Softwarea":
                return 5;
            case "Zerbitzaria":
                return 6;
            default:
                return 4;
        }
    }

    private int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    private BigDecimal parseBigDecimalSafe(String s) {
        try {
            return new BigDecimal(s);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
