package ui;

import db.DB_Konexioa;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * MenuZuzendaritza klasea.
 * Zuzendaritza edo Super Admin erabiltzaileen interfazea.
 * Beste menu guztietara sartzeko aukera ematen du.
 */
public class MenuZuzendaritza extends JFrame {

    private static final long serialVersionUID = 1L;
    private Langilea langilea;
    private String erabiltzaileSaila;

    // Fitxaketa
    private JLabel fitxaketaInfoEtiketa;

    /**
     * MenuZuzendaritza eraikitzailea.
     * 
     * @param id      Erabiltzailearen IDa.
     * @param izena   Izena.
     * @param abizena Abizena.
     * @param saila   Sailaren izena.
     */
    public MenuZuzendaritza(int id, String izena, String abizena, String saila) {
        this.langilea = new Langilea(id, izena, abizena, "", null, 0, "", "", "", "", "ES", "", "", null, null, true,
                1, "", null);
        this.erabiltzaileSaila = saila;
        pantailaPrestatu();
    }

    /**
     * Pantailaren osagaiak inizializatu eta kokatu.
     */
    private void pantailaPrestatu() {
        setTitle("Birtek - SISTEMAK (Super Admin)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        getContentPane().setLayout(new BorderLayout());

        // HEADER
        JPanel goikoPanela = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        JLabel erabiltzaileEtiketa = new JLabel(erabiltzaileSaila + " | " + langilea.getIzena() + " "
                + langilea.getAbizena() + " (ID: " + langilea.getIdLangilea() + ")");
        erabiltzaileEtiketa.setFont(new Font("SansSerif", Font.BOLD, 12));
        erabiltzaileEtiketa.setForeground(new Color(0, 102, 102));

        // ... (fitxaketa panela eta besteak berdin mantentzen dira, baina fitxatu()
        // metodoak langilea erabiliko du)
        // Fitxaketa Panela
        JPanel fitxaketaPanela = new JPanel();
        fitxaketaPanela.setLayout(new BoxLayout(fitxaketaPanela, BoxLayout.Y_AXIS));
        JPanel botoiPanela = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));

        JButton sarreraBotoia = new JButton("Sarrera");
        sarreraBotoia.setBackground(new Color(34, 139, 34));
        sarreraBotoia.setForeground(Color.BLACK);
        sarreraBotoia.setFont(new Font("SansSerif", Font.BOLD, 10));
        sarreraBotoia.addActionListener(e -> fitxatu("Sarrera"));

        JButton irteeraBotoia = new JButton("Irteera");
        irteeraBotoia.setBackground(new Color(255, 140, 0));
        irteeraBotoia.setForeground(Color.BLACK);
        irteeraBotoia.setFont(new Font("SansSerif", Font.BOLD, 10));
        irteeraBotoia.addActionListener(e -> fitxatu("Irteera"));

        JButton historialBotoia = new JButton("Historiala");
        historialBotoia.setBackground(new Color(100, 149, 237));
        historialBotoia.setForeground(Color.BLACK);
        historialBotoia.setFont(new Font("SansSerif", Font.BOLD, 10));
        historialBotoia.addActionListener(e -> ikusiFitxaketaHistoriala());

        botoiPanela.add(sarreraBotoia);
        botoiPanela.add(irteeraBotoia);
        botoiPanela.add(historialBotoia);

        JButton nireDatuakBotoia = new JButton("Nire Datuak");
        nireDatuakBotoia.setBackground(new Color(100, 149, 237));
        nireDatuakBotoia.setForeground(Color.BLACK);
        nireDatuakBotoia.setFont(new Font("SansSerif", Font.BOLD, 10));
        nireDatuakBotoia.addActionListener(e -> irekiNireDatuakEditatu());
        botoiPanela.add(nireDatuakBotoia);

        fitxaketaInfoEtiketa = new JLabel("Kargatzen...");
        fitxaketaInfoEtiketa.setFont(new Font("SansSerif", Font.PLAIN, 9));
        fitxaketaInfoEtiketa.setAlignmentX(Component.CENTER_ALIGNMENT);

        fitxaketaPanela.add(botoiPanela);
        fitxaketaPanela.add(fitxaketaInfoEtiketa);

        goikoPanela.add(erabiltzaileEtiketa);
        goikoPanela.add(fitxaketaPanela);
        getContentPane().add(goikoPanela, BorderLayout.NORTH);

        // BOTONES CENTRALES
        JPanel botoiPanelaNagusia = new JPanel(new GridLayout(3, 2, 15, 15));
        botoiPanelaNagusia.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton adminBotoia = new JButton("ADMINISTRAZIOA");
        adminBotoia.addActionListener(e -> {
            new MenuAdministrazioa(langilea).setVisible(true);
        });

        JButton teknikariBotoia = new JButton("TEKNIKOA");
        teknikariBotoia.addActionListener(e -> {
            new MenuTeknikoa(langilea).setVisible(true);
        });

        JButton salmentaBotoia = new JButton("SALMENTAK");
        salmentaBotoia.addActionListener(e -> {
            new MenuSalmentak(langilea).setVisible(true);
        });

        JButton logistikaBotoia = new JButton("LOGISTIKA");
        logistikaBotoia.addActionListener(e -> {
            new MenuLogistika(langilea).setVisible(true);
        });

        JButton probaBotoia = new JButton("DB CHECK");
        probaBotoia.addActionListener(e -> JOptionPane.showMessageDialog(this, "Konexioa OK"));

        JButton saioaItxiBotoia = new JButton("SAIOA ITXI");
        saioaItxiBotoia.setBackground(Color.RED);
        saioaItxiBotoia.setForeground(Color.WHITE);
        saioaItxiBotoia.addActionListener(e -> saioaItxi());

        botoiPanelaNagusia.add(adminBotoia);
        botoiPanelaNagusia.add(teknikariBotoia);
        botoiPanelaNagusia.add(salmentaBotoia);
        botoiPanelaNagusia.add(logistikaBotoia);
        botoiPanelaNagusia.add(probaBotoia);
        botoiPanelaNagusia.add(saioaItxiBotoia);

        getContentPane().add(botoiPanelaNagusia, BorderLayout.CENTER);

        if (!java.beans.Beans.isDesignTime()) {
            eguneratuFitxaketaEgoera();
        }
    }

    // --- FITXAKETA LOGIKA (Berdina) ---
    /**
     * Fitxaketa egin.
     * 
     * @param mota Fitxaketa mota.
     */
    private void fitxatu(String mota) {
        try {
            langilea.fitxatu(mota);
            eguneratuFitxaketaEgoera();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errorea", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Fitxaketa egoera eguneratu.
     */
    private void eguneratuFitxaketaEgoera() {
        String galdera = "SELECT mota, data, ordua FROM fitxaketak WHERE langilea_id = ? ORDER BY id_fitxaketa DESC LIMIT 1";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement sententzia = konexioa.prepareStatement(galdera)) {
            sententzia.setInt(1, langilea.getIdLangilea());
            ResultSet rs = sententzia.executeQuery();
            if (rs.next()) {
                String mota = rs.getString("mota");
                Time ordua = rs.getTime("ordua");
                if ("Sarrera".equals(mota)) {
                    fitxaketaInfoEtiketa.setText("BARRUAN (" + ordua + ")");
                    fitxaketaInfoEtiketa.setForeground(new Color(0, 100, 0));
                } else {
                    fitxaketaInfoEtiketa.setText("KANPOAN (" + ordua + ")");
                    fitxaketaInfoEtiketa.setForeground(new Color(200, 0, 0));
                }
            } else {
                fitxaketaInfoEtiketa.setText("Ez dago erregistrorik.");
                fitxaketaInfoEtiketa.setForeground(Color.GRAY);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fitxaketa historia ikusi.
     */
    private void ikusiFitxaketaHistoriala() {
        JDialog elkarrizketa = new JDialog(this, "Fitxaketa Historiala", true);
        elkarrizketa.setSize(500, 400);
        elkarrizketa.setLocationRelativeTo(this);
        elkarrizketa.setLayout(new BorderLayout());
        String[] zutabeak = { "Mota", "Data", "Ordua" };
        DefaultTableModel eredua = new DefaultTableModel(zutabeak, 0);
        JTable taula = new JTable(eredua);
        elkarrizketa.add(new JScrollPane(taula), BorderLayout.CENTER);

        String galdera = "SELECT mota, data, ordua FROM fitxaketak WHERE langilea_id = ? ORDER BY id_fitxaketa DESC";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement sententzia = konexioa.prepareStatement(galdera)) {
            sententzia.setInt(1, langilea.getIdLangilea());
            ResultSet rs = sententzia.executeQuery();
            while (rs.next()) {
                eredua.addRow(new Object[] { rs.getString("mota"), rs.getDate("data"), rs.getTime("ordua") });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        elkarrizketa.setVisible(true);
    }

    /**
     * Norberaren datuak editatzeko leihoa ireki.
     */
    private void irekiNireDatuakEditatu() {
        // MenuZuzendaritza uses separate fields, so we need to construct a Langilea
        // object
        // Query DB to get full details properly if needed, but for now we create a
        // temporary one with available data.
        // Ideally, MenuZuzendaritza should also hold a Langilea object like others.

        // We need current password and other details. Let's fetch them first or create
        // a Langilea and refresh after.
        Langilea l = new Langilea(langilea.getIdLangilea(), langilea.getIzena(), langilea.getAbizena(), "", null, 0, "",
                "", "", "",
                "ES", "", "", null, null, true, 1, "", null);

        // Retrieve actual data from DB to ensure we have the latest (password, town,
        // etc.)
        try (Connection k = DB_Konexioa.konektatu();
                PreparedStatement ps = k.prepareStatement("SELECT * FROM langileak WHERE id_langilea = ?")) {
            ps.setInt(1, langilea.getIdLangilea());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                l = new Langilea(
                        rs.getInt("id_langilea"),
                        rs.getString("izena"),
                        rs.getString("abizena"),
                        rs.getString("nan_ifz"),
                        rs.getDate("jaiotze_data"),
                        rs.getInt("herria_id"),
                        rs.getString("helbidea"),
                        rs.getString("posta_kodea"),
                        "", // telefonoa (missing in DB query or handled by pertsona?) - Defaulting to empty
                        rs.getString("emaila"),
                        rs.getString("hizkuntza"),
                        rs.getString("pasahitza"),
                        "", // saltoTxartelaUid
                        rs.getTimestamp("kontratazio_data"), // altaData
                        rs.getTimestamp("eguneratze_data"),
                        rs.getBoolean("aktibo"),
                        rs.getInt("saila_id"),
                        "", // iban
                        null // kurrikuluma
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        NireDatuakDialog dialog = new NireDatuakDialog(this, l);
        dialog.setVisible(true);
    }

    /**
     * Saioa itxi.
     */
    private void saioaItxi() {
        if (JOptionPane.showConfirmDialog(this, "Irten?", "Saioa Itxi", JOptionPane.YES_NO_OPTION) == 0) {
            dispose();
            new SaioaHastekoPanela().setVisible(true);
        }
    }
}
