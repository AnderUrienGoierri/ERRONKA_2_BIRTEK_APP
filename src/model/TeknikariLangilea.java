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

public class TeknikariLangilea extends Langilea {

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
                        rs.getInt("biltegi_id"), // Nullable izan daiteke
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
     * Produktuari irudia gehitu.
     * 
     * @param idProduktua Produktuaren IDa.
     * @param irudiaUrl   Irudiaren izena edo bidea (jpg).
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
    public void akatsaEzabatu(int idAkatsa) throws SQLException {
        String sql = "DELETE FROM akatsak WHERE id_akatsa = ?";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idAkatsa);
            pst.executeUpdate();
        }
    }
}
