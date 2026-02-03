package ui;

import db.DB_Konexioa;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

/**
 * EskaeraDialog klasea.
 * Eskaerak sortzeko eta editatzeko leihoa.
 * Bezeroa aukeratu, produktuak gehitu/kendu eta prezio totala kalkulatzen du.
 */
public class EskaeraDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JComboBox<BezeroItem> bezeroKomboa;
    private JComboBox<String> egoeraKomboa;
    private JLabel prezioTotalaLabel;
    private JTable lerroakTaula;
    private DefaultTableModel lerroakModel;
    private boolean onartua = false;

    // Produktuak gehitzeko elementuak
    private JComboBox<ProduktuaItem> produktuKomboa;
    private JSpinner kantitateSpinner;
    private JTextField deskontuaField;
    private JButton gehituLerroaBotoia;
    private JButton kenduLerroaBotoia;

    // Helper classes
    /**
     * Bezeroa ComboBox-ean erakusteko klase laguntzailea.
     */
    public static class BezeroItem {
        int id;
        String izena;

        public BezeroItem(int id, String izena) {
            this.id = id;
            this.izena = izena;
        }

        @Override
        public String toString() {
            return izena + " (ID: " + id + ")";
        }
    }

    /**
     * Produktua ComboBox-ean erakusteko klase laguntzailea.
     */
    public static class ProduktuaItem {
        int id;
        String izena;
        BigDecimal prezioa;
        int stock;

        public ProduktuaItem(int id, String izena, BigDecimal prezioa, int stock) {
            this.id = id;
            this.izena = izena;
            this.prezioa = prezioa;
            this.stock = stock;
        }

        @Override
        public String toString() {
            return izena + " (" + prezioa + " \u20AC) - Stock: " + stock;
        }
    }

    // Constructor updated (price removed from arguments as it's calculated)
    /**
     * EskaeraDialog eraikitzailea.
     *
     * @param jabea     Guraso leihoa.
     * @param izenburua Leihoaren izenburua.
     * @param bezeroaId Bezeroaren IDa (editatzen bada), bestela null.
     * @param egoera    Eskaeraren egoera (editatzen bada), bestela null.
     */
    public EskaeraDialog(Frame jabea, String izenburua, Integer bezeroaId, String egoera) {
        super(jabea, izenburua, true);
        setLayout(new BorderLayout());
        setSize(800, 600); // Handiagoa egin dugu
        setLocationRelativeTo(jabea);

        // --- GOIKO PANELA (Bezeroa eta Egoera) ---
        JPanel goikoPanela = new JPanel(new GridLayout(2, 2, 10, 10));
        goikoPanela.setBorder(BorderFactory.createTitledBorder("Eskaera Datuak"));

        goikoPanela.add(new JLabel("Bezeroa:"));
        bezeroKomboa = new JComboBox<>();
        kargatuBezeroak();
        if (bezeroaId != null)
            hautatuBezeroa(bezeroaId);
        goikoPanela.add(bezeroKomboa);

        goikoPanela.add(new JLabel("Egoera:"));
        String[] egoerak = { "Prestatzen", "Osatua/Bidalita", "Ezabatua" };
        egoeraKomboa = new JComboBox<>(egoerak);
        if (egoera != null)
            egoeraKomboa.setSelectedItem(egoera);
        goikoPanela.add(egoeraKomboa);

        add(goikoPanela, BorderLayout.NORTH);

        // --- ERDIKO PANELA (Lerroak eta Produktuak) ---
        JPanel erdikoPanela = new JPanel(new BorderLayout());
        erdikoPanela.setBorder(BorderFactory.createTitledBorder("Eskaera Lerroak"));

        // Produktua gehitzeko kontrolak
        JPanel produktuPanela = new JPanel(new FlowLayout(FlowLayout.LEFT));
        produktuKomboa = new JComboBox<>();
        kargatuProduktuak();
        produktuPanela.add(new JLabel("Produktua:"));
        produktuPanela.add(produktuKomboa);

        kantitateSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        produktuPanela.add(new JLabel("Kantitatea:"));
        produktuPanela.add(kantitateSpinner);

        deskontuaField = new JTextField("0", 3);
        produktuPanela.add(new JLabel("Deskontua (%):"));
        produktuPanela.add(deskontuaField);

        gehituLerroaBotoia = new JButton("Gehitu");
        gehituLerroaBotoia.addActionListener(e -> lerroaGehitu());
        produktuPanela.add(gehituLerroaBotoia);

        kenduLerroaBotoia = new JButton("Kendu");
        kenduLerroaBotoia.addActionListener(e -> lerroaKendu());
        produktuPanela.add(kenduLerroaBotoia);

        erdikoPanela.add(produktuPanela, BorderLayout.NORTH);

        // Taula
        String[] zutabeak = { "Produktua ID", "Produktua", "Prezioa", "Kantitatea", "Deskontua (%)", "Guztira" };
        lerroakModel = new DefaultTableModel(zutabeak, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lerroakTaula = new JTable(lerroakModel);
        erdikoPanela.add(new JScrollPane(lerroakTaula), BorderLayout.CENTER);

        add(erdikoPanela, BorderLayout.CENTER);

        // --- BEHEKO PANELA (Totala eta Botoiak) ---
        JPanel behekoPanela = new JPanel(new BorderLayout());

        JPanel totalPanela = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        prezioTotalaLabel = new JLabel("0.00 €");
        prezioTotalaLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalPanela.add(new JLabel("PREZIO TOTALA: "));
        totalPanela.add(prezioTotalaLabel);
        behekoPanela.add(totalPanela, BorderLayout.NORTH);

        JPanel botoiPanela = new JPanel();
        JButton onartuBotoia = new JButton("Gorde Eskaera");
        onartuBotoia.addActionListener(e -> {
            if (balidatu()) {
                onartua = true;
                dispose();
            }
        });
        JButton ezeztatuBotoia = new JButton("Ezeztatu");
        ezeztatuBotoia.addActionListener(e -> dispose());
        botoiPanela.add(onartuBotoia);
        botoiPanela.add(ezeztatuBotoia);
        behekoPanela.add(botoiPanela, BorderLayout.SOUTH);

        add(behekoPanela, BorderLayout.SOUTH);
    }

    /**
     * Bezero aktiboak kargatzen ditu ComboBox-ean.
     */
    private void kargatuBezeroak() {
        try (Connection konexioa = DB_Konexioa.konektatu();
                Statement stmt = konexioa.createStatement();
                ResultSet rs = stmt
                        .executeQuery("SELECT id_bezeroa, izena_edo_soziala FROM bezeroak WHERE aktibo = 1")) {
            while (rs.next()) {
                bezeroKomboa.addItem(new BezeroItem(rs.getInt("id_bezeroa"), rs.getString("izena_edo_soziala")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Salgai dauden produktuak kargatzen ditu ComboBox-ean.
     */
    private void kargatuProduktuak() {
        try (Connection konexioa = DB_Konexioa.konektatu();
                Statement stmt = konexioa.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT id_produktua, izena, salmenta_prezioa, stock FROM produktuak WHERE salgai = 1")) {
            while (rs.next()) {
                produktuKomboa.addItem(new ProduktuaItem(rs.getInt("id_produktua"), rs.getString("izena"),
                        rs.getBigDecimal("salmenta_prezioa"), rs.getInt("stock")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aukeratutako produktua saskiari gehitzen dio.
     * Stock-a egiaztatzen du eta prezioak kalkulatzen ditu.
     */
    private void lerroaGehitu() {
        ProduktuaItem p = (ProduktuaItem) produktuKomboa.getSelectedItem();
        if (p == null)
            return;

        int kantitatea = (int) kantitateSpinner.getValue();

        // Stock balidazioa: Zenbat dago jada taulan?
        int dagoenKantitatea = 0;
        for (int i = 0; i < lerroakModel.getRowCount(); i++) {
            if ((int) lerroakModel.getValueAt(i, 0) == p.id) {
                dagoenKantitatea += (int) lerroakModel.getValueAt(i, 3);
            }
        }

        if (dagoenKantitatea + kantitatea > p.stock) {
            JOptionPane.showMessageDialog(this,
                    "Ezin da stock-a baino gehiago gehitu.\nStock erabilgarria: " + p.stock
                            + "\nZure hautaketa (lehendik dagoena barne): " + (dagoenKantitatea + kantitatea),
                    "Stock mugatua", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double deskontua = 0;
        try {
            deskontua = Double.parseDouble(deskontuaField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Deskontu baliogabea.");
            return;
        }

        BigDecimal prezioOsoa = p.prezioa.multiply(new BigDecimal(kantitatea));
        BigDecimal deskontuZenbatekoa = prezioOsoa.multiply(new BigDecimal(deskontua)).divide(new BigDecimal(100));
        BigDecimal finala = prezioOsoa.subtract(deskontuZenbatekoa);

        lerroakModel.addRow(new Object[] { p.id, p.izena, p.prezioa, kantitatea, deskontua, finala });
        totalaEguneratu();
    }

    /**
     * Aukeratutako lerroa saskitik kentzen du.
     */
    private void lerroaKendu() {
        int selectedRow = lerroakTaula.getSelectedRow();
        if (selectedRow != -1) {
            lerroakModel.removeRow(selectedRow);
            totalaEguneratu();
        }
    }

    /**
     * Saskiko prezio totala birkalkulatzen eta eguneratzen du.
     */
    private void totalaEguneratu() {
        BigDecimal totala = BigDecimal.ZERO;
        for (int i = 0; i < lerroakModel.getRowCount(); i++) {
            totala = totala.add((BigDecimal) lerroakModel.getValueAt(i, 5)); // 5 = Guztira zutabea
        }
        prezioTotalaLabel.setText(String.format("%.2f \u20AC", totala));
    }

    /**
     * Bezero zehatz bat hautatzen du ComboBox-ean (editatzean).
     * 
     * @param id Bezeroaren IDa.
     */
    private void hautatuBezeroa(int id) {
        for (int i = 0; i < bezeroKomboa.getItemCount(); i++) {
            if (bezeroKomboa.getItemAt(i).id == id) {
                bezeroKomboa.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * Eskaera balidatzen du (bezeroa aukeratuta eta lerroak egotea).
     * 
     * @return True baliozkoa bada.
     */
    private boolean balidatu() {
        if (bezeroKomboa.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Aukeratu bezero bat.");
            return false;
        }
        if (lerroakModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Gehitu gutxienez produktu bat.");
            return false;
        }
        return true;
    }

    /**
     * Lerro bat zuzenean gehitzen du taulara (kargatzean erabiltzen da).
     *
     * @param id         Produktu IDa.
     * @param izena      Produktu izena.
     * @param prezioa    Unitate prezioa.
     * @param kantitatea Kantitatea.
     * @param deskontua  Deskontua (%).
     */
    public void addZuzeneanLerroa(int id, String izena, BigDecimal prezioa, int kantitatea, double deskontua) {
        BigDecimal prezioOsoa = prezioa.multiply(new BigDecimal(kantitatea));
        BigDecimal deskontuZenbatekoa = prezioOsoa.multiply(new BigDecimal(deskontua)).divide(new BigDecimal(100));
        BigDecimal finala = prezioOsoa.subtract(deskontuZenbatekoa);

        lerroakModel.addRow(new Object[] { id, izena, prezioa, kantitatea, deskontua, finala });
        totalaEguneratu();
    }

    /**
     * Erabiltzaileak onartu duen egiaztatzen du.
     * 
     * @return True onartu bada.
     */
    public boolean isOnartua() {
        return onartua;
    }

    /**
     * Aukeratutako bezeroaren IDa itzultzen du.
     * 
     * @return Bezero IDa.
     */
    public int getBezeroaId() {
        return ((BezeroItem) bezeroKomboa.getSelectedItem()).id;
    }

    /**
     * Aukeratutako egoera itzultzen du.
     * 
     * @return Egoera.
     */
    public String getEgoera() {
        return (String) egoeraKomboa.getSelectedItem();
    }

    /**
     * Prezio totala itzultzen du.
     * 
     * @return Totala.
     */
    public BigDecimal getPrezioTotala() {
        String t = prezioTotalaLabel.getText().replace(" \u20AC", "").replace(",", ".");
        return new BigDecimal(t);
    }

    /**
     * Saskiko lerroak itzultzen ditu.
     * 
     * @return Objektu array zerrenda: [prodId, kantitatea, unitatePrezioa
     *         (deskontuarekin)].
     */
    public java.util.List<Object[]> getLerroak() {
        java.util.List<Object[]> lista = new java.util.ArrayList<>();
        for (int i = 0; i < lerroakModel.getRowCount(); i++) {
            int prodId = (int) lerroakModel.getValueAt(i, 0);
            int kant = (int) lerroakModel.getValueAt(i, 3);
            BigDecimal totalaRow = (BigDecimal) lerroakModel.getValueAt(i, 5);
            // Unitate prezioa (deskontuarekin) = Totala / Kantitatea
            BigDecimal unitatePrezioa = totalaRow.divide(new BigDecimal(kant), 2, java.math.RoundingMode.HALF_UP);

            lista.add(new Object[] { prodId, kant, unitatePrezioa });
        }
        return lista;
    }
}
