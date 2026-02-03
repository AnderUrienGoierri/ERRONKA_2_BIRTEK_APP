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
import java.sql.Timestamp;
import java.util.List;

/**
 * FakturaPDF klasea.
 * Eskaera eta bezeroen datuetatik abiatuta PDF formatuko fakturak sortzen
 * dituen klasea.
 * iText liburutegia erabiltzen du PDFak sortzeko.
 */
public class FakturaPDF {

    /**
     * BezeroDatuak barne-klasea.
     * Fakturan bistaratu beharreko bezeroaren datuak gordetzeko.
     */
    public static class BezeroDatuak {
        public String izena;
        public String ifz;
        public String helbidea;
        public String emaila;

        /**
         * BezeroDatuak eraikitzailea.
         *
         * @param izena    Bezeroaren izena.
         * @param ifz      Bezeroaren IFZ/NAN.
         * @param helbidea Bezeroaren helbidea.
         * @param emaila   Bezeroaren emaila.
         */
        public BezeroDatuak(String izena, String ifz, String helbidea, String emaila) {
            this.izena = izena;
            this.ifz = ifz;
            this.helbidea = helbidea;
            this.emaila = emaila;
        }
    }

    /**
     * LerroDatuak barne-klasea.
     * Fakturako lerro bakoitzaren datuak gordetzeko.
     */
    public static class LerroDatuak {
        public String produktua;
        public int kantitatea;
        public BigDecimal prezioa;
        public BigDecimal guztira;

        /**
         * LerroDatuak eraikitzailea.
         *
         * @param produktua  Produktuaren izena.
         * @param kantitatea Produktu kantitatea.
         * @param prezioa    Unitateko prezioa.
         * @param guztira    Lerroaren prezio totala.
         */
        public LerroDatuak(String produktua, int kantitatea, BigDecimal prezioa, BigDecimal guztira) {
            this.produktua = produktua;
            this.kantitatea = kantitatea;
            this.prezioa = prezioa;
            this.guztira = guztira;
        }
    }

    // fakturak C/: karpetan XamPP barruan
    public static final String FAKTURA_BIDEA = "C:\\Xampp\\htdocs\\fakturak";

    /**
     * Faktura PDF bat sortzen du emandako datuekin.
     *
     * @param fitxategiPath PDF fitxategia gordeko den bidea.
     * @param idEskaera     Eskaeraren IDa.
     * @param data          Fakturaren data.
     * @param bezeroa       Bezeroaren datuak dituen objektua.
     * @param lerroak       Fakturako lerroen zerrenda.
     * @param guztira       Fakturaren guztizko zenbatekoa.
     * @throws DocumentException PDFa sortzean errorea gertatzen bada.
     * @throws IOException       Fitxategia idaztean errorea gertatzen bada.
     */
    public static void sortu(String fitxategiPath, int idEskaera, Timestamp data, BezeroDatuak bezeroa,
            List<LerroDatuak> lerroak, BigDecimal guztira)
            throws DocumentException, IOException {

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
        Paragraph izenburua = new Paragraph("FAKTURA: " + idEskaera, izenburuaFont);
        izenburua.setAlignment(Element.ALIGN_CENTER);
        document.add(izenburua);

        Paragraph dataPar = new Paragraph("Data: " + data.toString(), arruntaFont);
        dataPar.setAlignment(Element.ALIGN_CENTER);
        document.add(dataPar);

        document.add(Chunk.NEWLINE);

        // BEZEROA
        document.add(new Paragraph("Bezeroa:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        document.add(new Paragraph(bezeroa.izena, arruntaFont));
        document.add(new Paragraph(bezeroa.ifz, arruntaFont));
        document.add(new Paragraph(bezeroa.helbidea, arruntaFont));
        document.add(new Paragraph(bezeroa.emaila, arruntaFont));

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
        for (LerroDatuak lerroa : lerroak) {
            table.addCell(new Phrase(lerroa.produktua, arruntaFont));
            table.addCell(new Phrase(String.valueOf(lerroa.kantitatea), arruntaFont));
            table.addCell(new Phrase(lerroa.prezioa + " \u20AC", arruntaFont));
            table.addCell(new Phrase(lerroa.guztira + " \u20AC", arruntaFont));
        }

        document.add(table);

        document.add(Chunk.NEWLINE);

        // TOTALAK
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(40);
        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        addTotalRow(totalTable, "GUZTIRA (BEZ %21 barne):", guztira + " \u20AC",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));

        document.add(totalTable);

        document.close();
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
}
