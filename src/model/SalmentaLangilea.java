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
}
