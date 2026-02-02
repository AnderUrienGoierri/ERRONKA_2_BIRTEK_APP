package ui;

import db.DB_Konexioa;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.File;

public class MenuSalmentak extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable bezeroTaula, eskaeraTaula, eskaeraLerroTaula;
    private JTextField bilatuTestua;
    private TableRowSorter<DefaultTableModel> bezeroOrdenatzailea, eskaeraOrdenatzailea, unekoOrdenatzailea;

    // Fitxaketa
    private JLabel fitxaketaInfoEtiketa;

    // Erabiltzailearen datuak
    // Erabiltzailea (OOP)
    private SalmentaLangilea langilea;

    /**
     * Eraikitzailea eguneratua.
     */
    public MenuSalmentak(Langilea oinarrizkoLangilea) {
        this.langilea = new SalmentaLangilea(oinarrizkoLangilea);

        setTitle("Birtek - SALMENTAK");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1050, 650);

        JPanel goikoPanela = new JPanel(new BorderLayout());
        getContentPane().add(goikoPanela, BorderLayout.NORTH);

        JPanel bilatzailePanela = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bilatzailePanela.add(new JLabel("Bilatu: "));
        bilatuTestua = new JTextField(20);
        bilatuTestua.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filtratu();
            }
        });
        bilatzailePanela.add(bilatuTestua);
        goikoPanela.add(bilatzailePanela, BorderLayout.WEST);

        // ESKUINA: Erabiltzailea + Fitxaketa + Logout
        JPanel eskuinekoPanela = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JLabel erabiltzaileEtiketa = new JLabel(langilea.getIzena() + " " + langilea.getAbizena());
        erabiltzaileEtiketa.setFont(new Font("SansSerif", Font.BOLD, 12));
        erabiltzaileEtiketa.setForeground(new Color(0, 102, 102));

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

        JButton nireDatuakBotoia = new JButton("Nire Datuak");
        nireDatuakBotoia.setBackground(new Color(100, 149, 237));
        nireDatuakBotoia.setForeground(Color.BLACK);
        nireDatuakBotoia.setFont(new Font("SansSerif", Font.BOLD, 10));
        nireDatuakBotoia.addActionListener(e -> irekiNireDatuakEditatu());

        botoiPanela.add(sarreraBotoia);
        botoiPanela.add(irteeraBotoia);
        botoiPanela.add(historialBotoia);
        botoiPanela.add(nireDatuakBotoia);

        fitxaketaInfoEtiketa = new JLabel("Kargatzen...");
        fitxaketaInfoEtiketa.setFont(new Font("SansSerif", Font.PLAIN, 9));
        fitxaketaInfoEtiketa.setAlignmentX(Component.CENTER_ALIGNMENT);

        fitxaketaPanela.add(botoiPanela);
        fitxaketaPanela.add(fitxaketaInfoEtiketa);

        JButton saioaItxiBotoia = new JButton("Saioa Itxi");
        saioaItxiBotoia.setBackground(Color.RED);
        saioaItxiBotoia.setForeground(Color.WHITE);
        saioaItxiBotoia.addActionListener(e -> saioaItxi());

        eskuinekoPanela.add(erabiltzaileEtiketa);
        eskuinekoPanela.add(fitxaketaPanela);
        eskuinekoPanela.add(saioaItxiBotoia);

        goikoPanela.add(eskuinekoPanela, BorderLayout.EAST);

        JTabbedPane pestainaPanela = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(pestainaPanela, BorderLayout.CENTER);

        // BEZEROAK PANELA
        JPanel bezeroPanela = new JPanel(new BorderLayout());
        bezeroTaula = new JTable();
        bezeroPanela.add(new JScrollPane(bezeroTaula));
        pestainaPanela.addTab("Bezeroak", bezeroPanela);

        // ESKAERAK PANELA (SplitPane)
        eskaeraTaula = new JTable();
        eskaeraTaula.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fokatutakoEskaeraKargatu();
            }
        });

        eskaeraLerroTaula = new JTable();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(eskaeraTaula),
                new JScrollPane(eskaeraLerroTaula));
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);

        JPanel eskaeraPanela = new JPanel(new BorderLayout());
        eskaeraPanela.add(splitPane, BorderLayout.CENTER);
        pestainaPanela.addTab("Eskaerak", eskaeraPanela);

        // Eskaera botoiak
        JPanel eskaeraBotoiPanela = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton eskaeraGehituBotoia = new JButton("Gehitu");
        JButton eskaeraEditatuBotoia = new JButton("Editatu");
        JButton eskaeraEzabatuBotoia = new JButton("Ezabatu");
        JButton eskaeraFakturaBotoia = new JButton("Faktura");
        JButton eskaeraFakturaGuztiakBotoia = new JButton("Faktura Guztiak sortu");

        eskaeraGehituBotoia.addActionListener(e -> eskaeraGehitu());
        eskaeraEditatuBotoia.addActionListener(e -> eskaeraEditatu());
        eskaeraEzabatuBotoia.addActionListener(e -> eskaeraEzabatu());
        eskaeraFakturaBotoia.addActionListener(e -> fakturaSortu());
        eskaeraFakturaGuztiakBotoia.addActionListener(e -> fakturaGuztiakSortu());

        eskaeraBotoiPanela.add(eskaeraGehituBotoia);
        eskaeraBotoiPanela.add(eskaeraEditatuBotoia);
        eskaeraBotoiPanela.add(eskaeraEzabatuBotoia);
        eskaeraBotoiPanela.add(eskaeraFakturaBotoia);
        eskaeraBotoiPanela.add(eskaeraFakturaGuztiakBotoia);

        eskaeraPanela.add(eskaeraBotoiPanela, BorderLayout.SOUTH);

        pestainaPanela.addChangeListener(e -> {
            bilatuTestua.setText("");
            unekoOrdenatzailea = (pestainaPanela.getSelectedIndex() == 0) ? bezeroOrdenatzailea : eskaeraOrdenatzailea;
        });

        if (!java.beans.Beans.isDesignTime()) {
            datuakKargatu();
            eguneratuFitxaketaEgoera();
        }
    }

    public MenuSalmentak() {
        this(new Langilea(Sesioa.idLangilea, Sesioa.izena, Sesioa.abizena, "", null, 0, "", "", "", "", "ES", "", "",
                null, null, true, 2, "", null));
    }

    // --- FITXAKETA METODO BERRIAK ---
    private void fitxatu(String mota) {
        try {
            if ("Sarrera".equals(mota)) {
                langilea.sarreraFitxaketaEgin();
            } else {
                langilea.irteeraFitxaketaEgin();
            }
            eguneratuFitxaketaEgoera();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errorea", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eguneratuFitxaketaEgoera() {
        fitxaketaInfoEtiketa.setText(langilea.getFitxaketaEgoera());
        if (fitxaketaInfoEtiketa.getText().contains("BARRUAN")) {
            fitxaketaInfoEtiketa.setForeground(new Color(0, 100, 0));
        } else {
            fitxaketaInfoEtiketa.setForeground(new Color(200, 0, 0));
        }
    }

    private void ikusiFitxaketaHistoriala() {
        JDialog elkarrizketa = new JDialog(this, "Fitxaketa Historiala", true);
        elkarrizketa.setSize(500, 400);
        elkarrizketa.setLocationRelativeTo(this);
        elkarrizketa.setLayout(new BorderLayout());
        String[] zutabeak = { "Mota", "Data", "Ordua" };
        DefaultTableModel eredua = new DefaultTableModel(zutabeak, 0);
        JTable taula = new JTable(eredua);
        elkarrizketa.add(new JScrollPane(taula), BorderLayout.CENTER);

        java.util.List<Fitxaketa> zerrenda = langilea.nireFitxaketakIkusi();
        for (Fitxaketa f : zerrenda) {
            eredua.addRow(new Object[] { f.getMota(), f.getData(), f.getOrdua() });
        }

        elkarrizketa.setVisible(true);
    }

    private void irekiNireDatuakEditatu() {
        JPasswordField passField = new JPasswordField(langilea.getPasahitza());

        // Hizkuntza ComboBox
        String[] hizkuntzak = { "Euskara", "Gaztelania", "Ingelesa", "Frantsesa" };
        JComboBox<String> hizkuntzaBox = new JComboBox<>(hizkuntzak);

        // Aurrez hautatu uneko hizkuntza
        String unekoKodea = langilea.getHizkuntza();
        if ("EU".equalsIgnoreCase(unekoKodea) || "Euskara".equalsIgnoreCase(unekoKodea))
            hizkuntzaBox.setSelectedItem("Euskara");
        else if ("ES".equalsIgnoreCase(unekoKodea) || "Gaztelania".equalsIgnoreCase(unekoKodea))
            hizkuntzaBox.setSelectedItem("Gaztelania");
        else if ("EN".equalsIgnoreCase(unekoKodea) || "Ingelesa".equalsIgnoreCase(unekoKodea))
            hizkuntzaBox.setSelectedItem("Ingelesa");
        else if ("FR".equalsIgnoreCase(unekoKodea) || "Frantsesa".equalsIgnoreCase(unekoKodea))
            hizkuntzaBox.setSelectedItem("Frantsesa");
        else
            hizkuntzaBox.setSelectedItem("Gaztelania"); // Lehenetsia

        JTextField herriaIzenaField = new JTextField(langilea.getHerriaIzena());
        JTextField lurraldeaField = new JTextField();
        JTextField nazioaField = new JTextField();

        Object[] message = {
                "Pasahitza Berria:", passField,
                "Hizkuntza:", hizkuntzaBox,
                "Herria (Izena):", herriaIzenaField,
                "Lurraldea (Berria bada):", lurraldeaField,
                "Nazioa (Berria bada):", nazioaField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Nire Datuak Editatu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String pass = new String(passField.getPassword());

                // Orain izen osoa bidaltzen dugu DB-ra
                String hizkuntzaAukeratua = (String) hizkuntzaBox.getSelectedItem();
                String herria = herriaIzenaField.getText();
                String lurraldea = lurraldeaField.getText();
                String nazioa = nazioaField.getText();

                langilea.nireLangileDatuakEditatu(pass, hizkuntzaAukeratua, herria, lurraldea, nazioa);
                JOptionPane.showMessageDialog(this, "Datuak eguneratuta!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Errorea DBan: " + e.getMessage(), "Errorea",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void fokatutakoEskaeraKargatu() {
        int aukeratutakoLerroa = eskaeraTaula.getSelectedRow();
        if (aukeratutakoLerroa != -1) {
            aukeratutakoLerroa = eskaeraTaula.convertRowIndexToModel(aukeratutakoLerroa);
            Object idObj = eskaeraTaula.getModel().getValueAt(aukeratutakoLerroa, 0);
            int idEskaera = (idObj instanceof Number) ? ((Number) idObj).intValue()
                    : Integer.parseInt(idObj.toString());

            try (Connection konexioa = DB_Konexioa.konektatu()) {
                String sql = "SELECT p.izena, el.kantitatea, el.unitate_prezioa, (el.kantitatea * el.unitate_prezioa) as guztira "
                        +
                        "FROM eskaera_lerroak el JOIN produktuak p ON el.produktua_id = p.id_produktua " +
                        "WHERE el.eskaera_id = ?";
                PreparedStatement pst = konexioa.prepareStatement(sql);
                pst.setInt(1, idEskaera);
                eskaeraLerroTaula.setModel(TaulaModelatzailea.ereduaEraiki(pst.executeQuery()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            eskaeraLerroTaula.setModel(new DefaultTableModel());
        }
    }

    private void filtratu() {
        String t = bilatuTestua.getText();
        if (unekoOrdenatzailea != null) {
            if (t.isEmpty())
                unekoOrdenatzailea.setRowFilter(null);
            else
                unekoOrdenatzailea.setRowFilter(RowFilter.regexFilter("(?i)" + t));
        }
    }

    private void saioaItxi() {
        if (JOptionPane.showConfirmDialog(this, "Irten?", "Saioa Itxi", JOptionPane.YES_NO_OPTION) == 0) {
            dispose();
            new SaioaHastekoPanela().setVisible(true);
        }
    }

    private void datuakKargatu() {
        try (Connection konexioa = DB_Konexioa.konektatu()) {
            DefaultTableModel m1 = TaulaModelatzailea.ereduaEraiki(konexioa
                    .prepareStatement("SELECT id_bezeroa, izena_edo_soziala, emaila FROM bezeroak").executeQuery());
            bezeroTaula.setModel(m1);
            bezeroOrdenatzailea = new TableRowSorter<>(m1);
            bezeroTaula.setRowSorter(bezeroOrdenatzailea);

            DefaultTableModel m2 = TaulaModelatzailea
                    .ereduaEraiki(konexioa.prepareStatement("SELECT * FROM eskaerak").executeQuery());
            eskaeraTaula.setModel(m2);
            eskaeraOrdenatzailea = new TableRowSorter<>(m2);
            eskaeraTaula.setRowSorter(eskaeraOrdenatzailea);

            if (unekoOrdenatzailea == null)
                unekoOrdenatzailea = bezeroOrdenatzailea;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eskaeraGehitu() {
        EskaeraDialog dialog = new EskaeraDialog(this, "Gehitu Eskaera", null, "Prestatzen");
        dialog.setVisible(true);
        if (dialog.isOnartua()) {
            String sqlEskaera = "INSERT INTO eskaerak (bezeroa_id, langilea_id, data, eguneratze_data, guztira_prezioa, eskaera_egoera) VALUES (?, ?, NOW(), NOW(), ?, ?)";
            String sqlLerroa = "INSERT INTO eskaera_lerroak (eskaera_id, produktua_id, kantitatea, unitate_prezioa) VALUES (?, ?, ?, ?)";

            try (Connection konexioa = DB_Konexioa.konektatu()) {
                konexioa.setAutoCommit(false); // Transakzioa hasi

                int idEskaera = -1;

                try (PreparedStatement pst = konexioa.prepareStatement(sqlEskaera, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setInt(1, dialog.getBezeroaId());
                    pst.setInt(2, langilea.getIdLangilea());
                    pst.setBigDecimal(3, dialog.getPrezioTotala());
                    pst.setString(4, dialog.getEgoera());
                    pst.executeUpdate();

                    ResultSet rs = pst.getGeneratedKeys();

                    if (rs.next()) {
                        idEskaera = rs.getInt(1);

                        try (PreparedStatement pstLerroa = konexioa.prepareStatement(sqlLerroa)) {
                            for (Object[] lerroa : dialog.getLerroak()) {
                                pstLerroa.setInt(1, idEskaera);
                                pstLerroa.setInt(2, (int) lerroa[0]); // Produktua ID
                                pstLerroa.setInt(3, (int) lerroa[1]); // Kantitatea
                                pstLerroa.setBigDecimal(4, (java.math.BigDecimal) lerroa[2]); // Unitate Prezioa
                                pstLerroa.addBatch();
                            }
                            pstLerroa.executeBatch();
                        }
                    }
                }

                konexioa.commit(); // Transakzioa baieztatu

                // Faktura automatikoa sortu
                if ("Osatua/Bidalita".equalsIgnoreCase(dialog.getEgoera()) && idEskaera != -1) {
                    try {
                        langilea.fakturaSortu(idEskaera);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                datuakKargatu();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errorea eskaera sortzean: " + e.getMessage());
            }
        }
    }

    private void eskaeraEditatu() {
        int aukeratutakoLerroa = eskaeraTaula.getSelectedRow();
        if (aukeratutakoLerroa == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu eskaera bat editatzeko.");
            return;
        }

        // Datuak lortu taulatik
        aukeratutakoLerroa = eskaeraTaula.convertRowIndexToModel(aukeratutakoLerroa);
        DefaultTableModel model = (DefaultTableModel) eskaeraTaula.getModel();

        Object idObj = model.getValueAt(aukeratutakoLerroa, 0);
        int idEskaera = (idObj instanceof Number) ? ((Number) idObj).intValue()
                : Integer.parseInt(idObj.toString());

        Object bezeroObj = model.getValueAt(aukeratutakoLerroa, 1);
        int bezeroaId = (bezeroObj instanceof Number) ? ((Number) bezeroObj).intValue()
                : Integer.parseInt(bezeroObj.toString());

        String egoera = (String) model.getValueAt(aukeratutakoLerroa, 6);

        EskaeraDialog dialog = new EskaeraDialog(this, "Editatu Eskaera", bezeroaId, egoera);

        // Lerro zaharrak kargatu
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(
                        "SELECT el.produktua_id, p.izena, el.unitate_prezioa, el.kantitatea " +
                                "FROM eskaera_lerroak el JOIN produktuak p ON el.produktua_id = p.id_produktua " +
                                "WHERE el.eskaera_id = ?")) {
            pst.setInt(1, idEskaera);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                dialog.addZuzeneanLerroa(
                        rs.getInt("produktua_id"),
                        rs.getString("izena"),
                        rs.getBigDecimal("unitate_prezioa"),
                        rs.getInt("kantitatea"),
                        0.0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dialog.setVisible(true);

        if (dialog.isOnartua()) {
            String sqlUpdate = "UPDATE eskaerak SET bezeroa_id = ?, guztira_prezioa = ?, eskaera_egoera = ?, eguneratze_data = NOW() WHERE id_eskaera = ?";
            String sqlDeleteLerroak = "DELETE FROM eskaera_lerroak WHERE eskaera_id = ?";
            String sqlInsertLerroa = "INSERT INTO eskaera_lerroak (eskaera_id, produktua_id, kantitatea, unitate_prezioa) VALUES (?, ?, ?, ?)";

            try (Connection konexioa = DB_Konexioa.konektatu()) {
                konexioa.setAutoCommit(false);

                // Eskaera eguneratu
                try (PreparedStatement pst = konexioa.prepareStatement(sqlUpdate)) {
                    pst.setInt(1, dialog.getBezeroaId());
                    pst.setBigDecimal(2, dialog.getPrezioTotala());
                    pst.setString(3, dialog.getEgoera());
                    pst.setInt(4, idEskaera);
                    pst.executeUpdate();
                }

                // Lerro zaharrak ezabatu
                try (PreparedStatement pstDalete = konexioa.prepareStatement(sqlDeleteLerroak)) {
                    pstDalete.setInt(1, idEskaera);
                    pstDalete.executeUpdate();
                }

                // Lerro berriak sartu
                try (PreparedStatement pstLerroa = konexioa.prepareStatement(sqlInsertLerroa)) {
                    for (Object[] lerroa : dialog.getLerroak()) {
                        pstLerroa.setInt(1, idEskaera);
                        pstLerroa.setInt(2, (int) lerroa[0]);
                        pstLerroa.setInt(3, (int) lerroa[1]);
                        pstLerroa.setBigDecimal(4, (java.math.BigDecimal) lerroa[2]);
                        pstLerroa.addBatch();
                    }
                    pstLerroa.executeBatch();
                }

                konexioa.commit();

                // Faktura automatikoa sortu
                if ("Osatua/Bidalita".equalsIgnoreCase(dialog.getEgoera())) {
                    try {
                        langilea.fakturaSortu(idEskaera);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                datuakKargatu();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errorea eskaera editatzean: " + e.getMessage());
            }
        }
    }

    private void eskaeraEzabatu() {
        int aukeratutakoLerroa = eskaeraTaula.getSelectedRow();
        if (aukeratutakoLerroa == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu eskaera bat ezabatzeko.");
            return;
        }

        aukeratutakoLerroa = eskaeraTaula.convertRowIndexToModel(aukeratutakoLerroa);
        Object idObj = eskaeraTaula.getModel().getValueAt(aukeratutakoLerroa, 0);
        int idEskaera = (idObj instanceof Number) ? ((Number) idObj).intValue() : Integer.parseInt(idObj.toString());

        if (JOptionPane.showConfirmDialog(this, "Ziur zaude eskaera hau ezabatu nahi duzula?", "Ezabatu",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM eskaerak WHERE id_eskaera = ?";
            try (Connection konexioa = DB_Konexioa.konektatu();
                    PreparedStatement pst = konexioa.prepareStatement(sql)) {
                pst.setInt(1, idEskaera);
                pst.executeUpdate();
                datuakKargatu();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errorea eskaera ezabatzean: " + e.getMessage());
            }
        }
    }

    private void fakturaSortu() {
        int aukeratutakoLerroa = eskaeraTaula.getSelectedRow();
        if (aukeratutakoLerroa == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu eskaera bat faktura sortzeko.");
            return;
        }

        aukeratutakoLerroa = eskaeraTaula.convertRowIndexToModel(aukeratutakoLerroa);
        DefaultTableModel model = (DefaultTableModel) eskaeraTaula.getModel();

        Object idObj = model.getValueAt(aukeratutakoLerroa, 0);
        int idEskaera = (idObj instanceof Number) ? ((Number) idObj).intValue() : Integer.parseInt(idObj.toString());
        String egoera = (String) model.getValueAt(aukeratutakoLerroa, 6);

        if (!"Osatua/Bidalita".equalsIgnoreCase(egoera)) {
            JOptionPane.showMessageDialog(this,
                    "Faktura bakarrik 'Osatua/Bidalita' egoeran dauden eskaeretarako sor daiteke.");
            return;
        }

        try {
            File fakturaFitxategia = langilea.fakturaSortu(idEskaera);
            if (fakturaFitxategia != null) {
                JOptionPane.showMessageDialog(this,
                        "Faktura PDF sortu eta gordeta: " + fakturaFitxategia.getAbsolutePath());
                java.awt.Desktop.getDesktop().open(fakturaFitxategia);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errorea faktura sortzean: " + e.getMessage());
        }
    }

    private void fakturaGuztiakSortu() {
        String sql = "SELECT id_eskaera FROM eskaerak WHERE eskaera_egoera = 'Osatua/Bidalita'";
        int kontagailua = 0;

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int idEskaera = rs.getInt("id_eskaera");
                try {
                    if (langilea.fakturaSortu(idEskaera) != null) {
                        kontagailua++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            JOptionPane.showMessageDialog(this, kontagailua + " faktura sortu dira zuzen.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errorea faktura guztiak sortzean: " + e.getMessage());
        }
    }

}
