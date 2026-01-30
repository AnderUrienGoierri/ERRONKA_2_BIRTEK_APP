package model;

import db.DB_Konexioa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdministrariLangilea extends Langilea {

    public AdministrariLangilea(Langilea l) {
        super(l.getIdLangilea(), l.getIzena(), l.getAbizena(), l.getNan(), l.getJaiotzaData(), l.getHerriaId(),
                l.getHelbidea(), l.getPostaKodea(), l.getTelefonoa(), l.getEmaila(), l.getHizkuntza(),
                l.getPasahitza(), l.getSaltoTxartelaUid(), l.getAltaData(), l.getEguneratzeData(),
                l.isAktibo(), l.getSailaId(), l.getIban(), l.getKurrikuluma());
    }

    public void langileaSortu(String izena, String abizena, String nan, String emaila, String pasahitza, int sailaId,
            String helbidea, int herriaId, String postaKodea) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "INSERT INTO langileak (izena, abizena, nan_ifz, emaila, pasahitza, saila_id, helbidea, herria_id, posta_kodea) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, izena);
            pst.setString(2, abizena);
            pst.setString(3, nan);
            pst.setString(4, emaila);
            pst.setString(5, pasahitza);
            pst.setInt(6, sailaId);
            pst.setString(7, helbidea);
            pst.setInt(8, herriaId);
            pst.setString(9, postaKodea);
            pst.executeUpdate();
        }
    }

    public void langileaEzabatu(int idLangilea) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "DELETE FROM langileak WHERE id_langilea = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idLangilea);
            pst.executeUpdate();
        }
    }

    public void langileaEditatu(int idLangilea, String izena, String abizena, String nan, String emaila, int sailaId,
            String helbidea, int herriaId, String postaKodea) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "UPDATE langileak SET izena = ?, abizena = ?, nan_ifz = ?, emaila = ?, saila_id = ?, helbidea = ?, herria_id = ?, posta_kodea = ?, eguneratze_data = NOW() WHERE id_langilea = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, izena);
            pst.setString(2, abizena);
            pst.setString(3, nan);
            pst.setString(4, emaila);
            pst.setInt(5, sailaId);
            pst.setString(6, helbidea);
            pst.setInt(7, herriaId);
            pst.setString(8, postaKodea);
            pst.setInt(9, idLangilea);
            pst.executeUpdate();
        }
    }

    public Langilea langileaIkusi(int idLangilea) throws SQLException {
        Langilea langilea = null;
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "SELECT * FROM langileak WHERE id_langilea = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idLangilea);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                langilea = new Langilea(
                        rs.getInt("id_langilea"),
                        rs.getString("izena"),
                        rs.getString("abizena"),
                        rs.getString("nan_ifz"),
                        rs.getDate("jaiotza_data"),
                        rs.getInt("herria_id"),
                        rs.getString("helbidea"),
                        rs.getString("posta_kodea"),
                        rs.getString("telefonoa"),
                        rs.getString("emaila"),
                        rs.getString("hizkuntza"),
                        rs.getString("pasahitza"),
                        rs.getString("salto_txartela_uid"),
                        rs.getTimestamp("alta_data"),
                        rs.getTimestamp("eguneratze_data"),
                        rs.getBoolean("aktibo"),
                        rs.getInt("saila_id"),
                        rs.getString("iban"),
                        rs.getBytes("kurrikuluma"));
            }
        }
        return langilea;
    }

    public BezeroFaktura bezeroaFakturaIkusi(int idFaktura) throws SQLException {
        BezeroFaktura faktura = null;
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "SELECT * FROM bezero_fakturak WHERE id_faktura = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idFaktura);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                faktura = new BezeroFaktura(
                        rs.getInt("id_faktura"),
                        rs.getString("faktura_zenbakia"),
                        rs.getInt("eskaera_id"),
                        rs.getDate("data"),
                        rs.getBigDecimal("zergak_ehunekoa"),
                        rs.getString("fitxategia_url"));
            }
        }
        return faktura;
    }

    public void langileSailaSortu(String izena, String kokapena, String deskribapena) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "INSERT INTO langile_sailak (izena, kokapena, deskribapena) VALUES (?, ?, ?)";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, izena);
            pst.setString(2, kokapena);
            pst.setString(3, deskribapena);
            pst.executeUpdate();
        }
    }

    public void langileSailaEzabatu(int idSaila) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "DELETE FROM langile_sailak WHERE id_saila = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idSaila);
            pst.executeUpdate();
        }
    }

    public void langileSailaEditatu(int idSaila, String izena, String kokapena, String deskribapena)
            throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "UPDATE langile_sailak SET izena = ?, kokapena = ?, deskribapena = ? WHERE id_saila = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, izena);
            pst.setString(2, kokapena);
            pst.setString(3, deskribapena);
            pst.setInt(4, idSaila);
            pst.executeUpdate();
        }
    }

    public LangileSaila langileSailaikusi(int idSaila) throws SQLException {
        LangileSaila saila = null;
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "SELECT * FROM langile_sailak WHERE id_saila = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idSaila);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                saila = new LangileSaila(
                        rs.getInt("id_saila"),
                        rs.getString("izena"),
                        rs.getString("kokapena"),
                        rs.getString("deskribapena"));
            }
        }
        return saila;
    }

    public java.util.ArrayList<Fitxaketa> fitxaketaGuztiakIkusi() {
        java.util.ArrayList<Fitxaketa> zerrenda = new java.util.ArrayList<>();
        String galdera = "SELECT * FROM fitxaketak ORDER BY id_fitxaketa DESC";
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(galdera);
                java.sql.ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                zerrenda.add(new Fitxaketa(
                        rs.getInt("id_fitxaketa"),
                        rs.getInt("langilea_id"),
                        rs.getDate("data"),
                        rs.getTime("ordua"),
                        rs.getString("mota"),
                        rs.getTimestamp("eguneratze_data")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zerrenda;
    }

    public void fitxaketaEzabatu(int idFitxaketa) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "DELETE FROM fitxaketak WHERE id_fitxaketa = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idFitxaketa);
            pst.executeUpdate();
        }
    }
}
