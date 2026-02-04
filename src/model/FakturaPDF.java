package model;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import db.DB_Konexioa;

/**
 * FakturaPDF klasea.
 * Eskaera eta bezeroen datuetatik abiatuta PDF formatuko fakturak sortzen
 * dituen klasea.
 * iText liburutegia erabiltzen du PDFak sortzeko.
 */
public class FakturaPDF {

    /**
     * Faktura PDF bat sortzen du emandako datuekin (Domain objects).
     *
     * @param fitxategiPath PDF fitxategia gordeko den bidea.
     * @param eskaera       Eskaera objektua.
     * @param bezeroa       Bezeroa objektua.
     * @param lerroak       Eskaera lerroen zerrenda.
     * @throws DocumentException PDFa sortzean errorea gertatzen bada.
     * @throws IOException       Fitxategia idaztean errorea gertatzen bada.
     */
    public static void sortu(String fitxategiPath, Eskaera eskaera, Bezeroa bezeroa, List<EskaeraLerroa> lerroak)
            throws DocumentException, IOException {

        // Bezero datuak prestatu

        File fitxategia = new File(fitxategiPath);
        if (fitxategia.getParentFile() != null) {
            fitxategia.getParentFile().mkdirs();
        }

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fitxategiPath));

        document.open();

        // Letra tipoak
        Font izenburuaFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Font arruntaFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Font letraTxikia = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
        Font taulaGoiburua = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);

        // GOIBURUA: LOGOA + ENPRESA DATUAK (Taula batekin hobeto lerrokatzeko)
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[] { 1, 1 });
        headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        // LOGOA (Ezkerrean)
        try {
            String logoPath = "irudiak/birtek_logo_zuri_borobila.png";
            File logoFile = new File(logoPath);
            if (logoFile.exists()) {
                Image img = Image.getInstance(logoPath);
                img.scaleToFit(80, 80);
                PdfPCell logoCell = new PdfPCell(img);
                logoCell.setBorder(Rectangle.NO_BORDER);
                logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                headerTable.addCell(logoCell);
            } else {
                // beste bide batetik saiatzen gara
                logoPath = "C:\\Ander\\Workspace\\Java\\eclipse-workspace\\ERRONKA_2_BIRTEK_APP\\irudiak\\birtek_logo_zuri_borobila.png";
                if (new File(logoPath).exists()) {
                    Image img = Image.getInstance(logoPath);
                    img.scaleToFit(80, 80);
                    PdfPCell logoCell = new PdfPCell(img);
                    logoCell.setBorder(Rectangle.NO_BORDER);
                    logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    headerTable.addCell(logoCell);
                } else {
                    headerTable.addCell(""); // Hutsik
                }
            }
        } catch (Exception e) {
            headerTable.addCell(""); // Errorea egonez gero, hutsik
        }

        // ENPRESA DATUAK (Eskuinean)
        Paragraph empresaInfo = new Paragraph(
                "Birtek S.L.\nKale Nagusia 123, Ordizia\nCampus Goierri Campusa\nIFZ: B12345678\nTel: 944 123 456",
                letraTxikia);
        PdfPCell infoCell = new PdfPCell(empresaInfo);
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerTable.addCell(infoCell);

        document.add(headerTable);

        document.add(Chunk.NEWLINE);

        // IZENBURUA
        Paragraph izenburua = new Paragraph("FAKTURA: " + eskaera.getIdEskaera(), izenburuaFont);
        izenburua.setAlignment(Element.ALIGN_CENTER);
        document.add(izenburua);

        Paragraph dataPar = new Paragraph("Data: " + eskaera.getData().toString(), arruntaFont);
        dataPar.setAlignment(Element.ALIGN_CENTER);
        document.add(dataPar);

        document.add(Chunk.NEWLINE);

        // BEZEROA
        document.add(new Paragraph("Bezeroa:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        document.add(new Paragraph(bezeroa.getIzenaEdoSoziala(), arruntaFont));
        document.add(new Paragraph(bezeroa.getIfzNan(), arruntaFont));
        document.add(new Paragraph(bezeroa.getHelbidea(), arruntaFont));
        document.add(new Paragraph(bezeroa.getEmaila(), arruntaFont));

        document.add(Chunk.NEWLINE);

        // TAULA
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 4, 1, 2, 2 }); // Zutabeen zabalerak

        // Taula Goiburuak
        addHeaderCell(table, "Produktua", taulaGoiburua);
        addHeaderCell(table, "Kop.", taulaGoiburua);
        addHeaderCell(table, "Prezioa", taulaGoiburua);
        addHeaderCell(table, "Guztira", taulaGoiburua);

        // Datuak
        BigDecimal calculatedTotal = BigDecimal.ZERO;
        for (EskaeraLerroa lerroa : lerroak) {
            String izena = getProduktuaIzena(lerroa.getProduktuaId());
            BigDecimal guztiraLerroa = lerroa.getUnitatePrezioa().multiply(new BigDecimal(lerroa.getKantitatea()));
            calculatedTotal = calculatedTotal.add(guztiraLerroa);

            table.addCell(new Phrase(izena, arruntaFont));
            table.addCell(new Phrase(String.valueOf(lerroa.getKantitatea()), arruntaFont));
            table.addCell(new Phrase(lerroa.getUnitatePrezioa() + " \u20AC", arruntaFont));
            table.addCell(new Phrase(guztiraLerroa + " \u20AC", arruntaFont));
        }

        document.add(table);

        document.add(Chunk.NEWLINE);

        // TOTALAK
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(40);
        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        addTotalRow(totalTable, "GUZTIRA (BEZ %21 barne):", eskaera.getGuztiraPrezioa() + " \u20AC",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));

        document.add(totalTable);

        document.close();
    }

    private static String getProduktuaIzena(int produktuaId) {
        String izena = "Produktua " + produktuaId; // Fallback
        String sql = "SELECT izena FROM produktuak WHERE id_produktua = ?";
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql)) {
            pst.setInt(1, produktuaId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    izena = rs.getString("izena");
                }
            }
        } catch (SQLException e) {
            System.err.println("Errorea produktu izena lortzean: " + e.getMessage());
        }
        return izena;
    }

    private static void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private static void addTotalRow(PdfPTable table, String label, String value, Font font) {
        PdfPCell cellLabel = new PdfPCell(new Phrase(label, font));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellLabel);

        PdfPCell cellValue = new PdfPCell(new Phrase(value, font));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellValue);
    }

    /**
     * Faktura FTP bidez zerbitzarira igotzeko metodoa.
     * 
     * @param fitxategiPath  Igo nahi den fitxategiaren bide osoa.
     * @param fitxategiIzena Fitxategiak zerbitzarian izango duen izena.
     */
    public static void fakturaIgoZerbitzarira(String fitxategiPath, String fitxategiIzena) {
        String server = "localhost";
        int port = 21;
        String user = "root"; // FTP erabiltzailea
        String pass = "1MG32025"; // FTP pasahitza

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // "htdocs/fakturak" karpetara joan
            // Aldatu direktorioa beharrezkoa bada.
            // XAMPP-en FileZilla-k askotan erroa zuzenean ezartzen du erabiltzailearen
            // home gisa.
            // Hemen suposatzen dugu "htdocs/fakturak" existitzen dela edo sortu behar dela.
            // Baina erabiltzaileak esan du "htdocs/fakturak" karpetan gorde nahi duela.

            // Saiatu direktorioa aldatzen, bestela sortu
            if (!ftpClient.changeWorkingDirectory("htdocs/fakturak")) {
                if (ftpClient.makeDirectory("htdocs")) {
                    ftpClient.changeWorkingDirectory("htdocs");
                    ftpClient.makeDirectory("fakturak");
                    ftpClient.changeWorkingDirectory("fakturak");
                } else {
                    // Agian zuzenean fakturak karpeta erroan dago edo beste egitura bat du
                    ftpClient.makeDirectory("fakturak");
                    ftpClient.changeWorkingDirectory("fakturak");
                }
            }

            File firstLocalFile = new File(fitxategiPath);

            String firstRemoteFile = fitxategiIzena;
            FileInputStream inputStream = new FileInputStream(firstLocalFile);

            System.out.println("Fitxategia igotzen hasten...");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("Fitxategia ondo igo da.");
            } else {
                System.out.println("Errorea fitxategia igotzean.");
            }

        } catch (IOException ex) {
            System.out.println("Errorea: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
