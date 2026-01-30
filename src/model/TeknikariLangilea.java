package model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                        (Integer) rs.getObject("biltegi_id"), // Nullable izan daiteke
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
}
