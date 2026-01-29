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
}
