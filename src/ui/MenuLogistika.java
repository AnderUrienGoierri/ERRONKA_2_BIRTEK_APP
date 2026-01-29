package ui;

import db.DB_Konexioa;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MenuLogistika extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel edukiPanela;
    private JFileChooser fitxategiHautatzailea;
    private JTextField bilatuTestua;

    // Erabiltzailea (OOP)
    private BiltegiLangilea langilea;

    // Fitxaketa informazioa erakusteko etiketa
    private JLabel fitxaketaInfoEtiketa;

    // Taulak
    private JTable sarreraTaula;
    private JTable biltegiTaula;
    private JTable produktuTaula;

    // Sarrera Berria elementuak
    private JComboBox<HornitzaileElementua> hornitzaileHautatzailea;
    private JCheckBox hornitzaileBerriaAukera;
    private JTextField izenaBerriaTestua;
    private JTextField postaBerriaTestua;
    private JTextField ifzBerriaTestua;

    private JTextField produktuIzenaTestua;
    private JTextField markaTestua;
    private JComboBox<KategoriaElementua> kategoriaHautatzailea;
    private JComboBox<String> motaHautatzailea;
    private JComboBox<BiltegiElementua> biltegiHautatzaileaSarrera;
    private JTextField kantitateTestua;
    private JTextField deskribapenaTestua;
    private JTextField irudiaUrlTestua;
    private JTextField egoeraOharraTestua;

    private JTable lerroBerriTaula;
    private DefaultTableModel lerroBerriEredua;

    // Iragazkia
    private JComboBox<String> egoeraIragazkia;

    // Fitxak
    private JTabbedPane pestainaPanela;

    // Sorters
    private TableRowSorter<DefaultTableModel> sarreraOrdenatzailea;
    private TableRowSorter<DefaultTableModel> biltegiOrdenatzailea;
    private TableRowSorter<DefaultTableModel> produktuOrdenatzailea;

    /**
     * Eraikitzaileak eguneratua.
     */
    public MenuLogistika(Langilea oinarrizkoLangilea) {
        this.langilea = new BiltegiLangilea(oinarrizkoLangilea);
        pantailaPrestatu();
    }

    public MenuLogistika() {
        this(new Langilea(Sesioa.idLangilea, Sesioa.izena, Sesioa.abizena, "", null, 0, "", "", "", "", "ES", "", "",
                null, null, true, 2, "", null));
    }

    private void pantailaPrestatu() {
        setTitle("Birtek - LOGISTIKA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1150, 700);

        edukiPanela = new AtzealdekoPanela();
        edukiPanela.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(edukiPanela);
        edukiPanela.setLayout(new BorderLayout(0, 0));

        // --- GOIKO PANELA ---
        JPanel goikoPanela = new JPanel(new BorderLayout()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255, 255, 255, 220));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        goikoPanela.setOpaque(false);
        goikoPanela.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 128, 128), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        edukiPanela.add(goikoPanela, BorderLayout.NORTH);

        // EZKERRA: Bilatzailea
        JPanel bilatzailePanela = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bilatzailePanela.setOpaque(false);
        JLabel bilatuEtiketa = new JLabel("Bilatu: ");
        bilatuEtiketa.setFont(new Font("SansSerif", Font.BOLD, 12));
        bilatzailePanela.add(bilatuEtiketa);

        bilatuTestua = new JTextField();
        bilatuTestua.setColumns(20);
        bilatuTestua.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtratu();
            }
        });
        bilatzailePanela.add(bilatuTestua);
        goikoPanela.add(bilatzailePanela, BorderLayout.WEST);

        // ESKUINA: Erabiltzailea + Fitxaketa + Logout
        JPanel erabiltzaileInfoPanela = new JPanel(new GridBagLayout());
        erabiltzaileInfoPanela.setOpaque(false);

        JLabel erabiltzaileEtiketa = new JLabel(
                langilea.getIzena() + " " + langilea.getAbizena());
        erabiltzaileEtiketa.setFont(new Font("SansSerif", Font.BOLD, 14));
        erabiltzaileEtiketa.setForeground(new Color(0, 102, 102));

        GridBagConstraints gbcUser = new GridBagConstraints();
        gbcUser.insets = new Insets(0, 10, 0, 10);
        gbcUser.fill = GridBagConstraints.VERTICAL;
        gbcUser.gridx = 0;
        gbcUser.gridy = 0;
        erabiltzaileInfoPanela.add(erabiltzaileEtiketa, gbcUser);

        // Fitxaketa Panela (Etiketa)
        fitxaketaInfoEtiketa = new JLabel("Kargatzen...");
        fitxaketaInfoEtiketa.setFont(new Font("SansSerif", Font.PLAIN, 10));
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = 1;
        erabiltzaileInfoPanela.add(fitxaketaInfoEtiketa, gbcLabel);

        // ... Fitxaketa botoiak ...
        JPanel botoiPanela = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        botoiPanela.setOpaque(false);

        JButton sarreraBotoia = new JButton("Sarrera");
        sarreraBotoia.addActionListener(e -> fitxatu("Sarrera"));

        JButton irteeraBotoia = new JButton("Irteera");
        irteeraBotoia.addActionListener(e -> fitxatu("Irteera"));

        JButton historialBotoia = new JButton("Historiala");
        historialBotoia.addActionListener(e -> ikusiFitxaketaHistoriala());

        JButton nireDatuakBotoia = new JButton("Nire Datuak");
        nireDatuakBotoia.addActionListener(e -> irekiNireDatuakEditatu());

        botoiPanela.add(sarreraBotoia);
        botoiPanela.add(irteeraBotoia);
        botoiPanela.add(historialBotoia);
        botoiPanela.add(nireDatuakBotoia);

        GridBagConstraints gbcBtns = new GridBagConstraints();
        gbcBtns.gridx = 1;
        gbcBtns.gridy = 0;
        gbcBtns.gridheight = 2;
        erabiltzaileInfoPanela.add(botoiPanela, gbcBtns);

        // Logout botoia
        JButton saioaItxiBotoia = new JButton("Saioa Itxi");
        saioaItxiBotoia.setBackground(new Color(220, 20, 60));
        saioaItxiBotoia.setForeground(new Color(0, 0, 0));
        saioaItxiBotoia.setFont(new Font("SansSerif", Font.BOLD, 12));
        saioaItxiBotoia.addActionListener(e -> saioaItxi());

        GridBagConstraints gbcLogout = new GridBagConstraints();
        gbcLogout.insets = new Insets(0, 10, 0, 10);
        gbcLogout.fill = GridBagConstraints.VERTICAL;
        gbcLogout.gridx = 2;
        gbcLogout.gridy = 0;
        gbcLogout.gridheight = 2;
        erabiltzaileInfoPanela.add(saioaItxiBotoia, gbcLogout);

        goikoPanela.add(erabiltzaileInfoPanela, BorderLayout.EAST);

        // PESTAINA PANELA (JTabbedPane)
        pestainaPanela = new JTabbedPane(JTabbedPane.TOP);
        edukiPanela.add(pestainaPanela, BorderLayout.CENTER);

        // Tab-ak sortu
        sarreraTabSortu();
        biltegiTabSortu();
        produktuTabSortu();
        sarreraBerriaTabSortu();

        pestainaPanela.addChangeListener(e -> {
            bilatuTestua.setText("");
            int index = pestainaPanela.getSelectedIndex();
            if (index == 0)
                sarreraDatuakKargatu();
            else if (index == 1)
                biltegiDatuakKargatu();
            else if (index == 2)
                produktuDatuakKargatu();
            else if (index == 3)
                sarreraHautatzaileakKargatu();
        });

        // Hasierako karga
        if (!java.beans.Beans.isDesignTime()) {
            sarreraDatuakKargatu();
            biltegiDatuakKargatu();
            produktuDatuakKargatu();
            sarreraHautatzaileakKargatu();
            eguneratuFitxaketaEgoera();
        }
    }

    // --- FITXAKETA LOGIKA ---
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
        JDialog elkarrizketa = new JDialog(this, "Nire Fitxaketa Historiala", true);
        elkarrizketa.setSize(500, 400);
        elkarrizketa.setLocationRelativeTo(this);
        elkarrizketa.setLayout(new BorderLayout());
        String[] zutabeak = { "Mota", "Data", "Ordua" };
        DefaultTableModel eredua = new DefaultTableModel(zutabeak, 0);
        JTable taula = new JTable(eredua);
        taula.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        taula.setRowHeight(25);
        elkarrizketa.add(new JScrollPane(taula), BorderLayout.CENTER);

        java.util.List<Fitxaketa> zerrenda = langilea.nireFitxaketakIkusi();
        for (Fitxaketa f : zerrenda) {
            eredua.addRow(new Object[] { f.getMota(), f.getData(), f.getOrdua() });
        }

        JButton itxiBotoia = new JButton("Itxi");
        itxiBotoia.addActionListener(e -> elkarrizketa.dispose());
        JPanel botoiPanela = new JPanel();
        botoiPanela.add(itxiBotoia);
        elkarrizketa.add(botoiPanela, BorderLayout.SOUTH);
        elkarrizketa.setVisible(true);
    }

    private void irekiNireDatuakEditatu() {
        JPasswordField passField = new JPasswordField(langilea.getPasahitza());
        JTextField hizkuntzaField = new JTextField(langilea.getHizkuntza());
        JTextField herriaIdField = new JTextField(String.valueOf(langilea.getHerriaId()));

        Object[] message = {
                "Pasahitza Berria:", passField,
                "Hizkuntza (ES/EU):", hizkuntzaField,
                "Herria ID:", herriaIdField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Nire Datuak Editatu", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String pass = new String(passField.getPassword());
                String hiz = hizkuntzaField.getText();
                int herria = Integer.parseInt(herriaIdField.getText());

                langilea.nireLangileDatuakEditatu(pass, hiz, herria);
                JOptionPane.showMessageDialog(this, "Datuak eguneratuta!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Herria ID zenbakia izan behar da.", "Errorea",
                        JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Errorea DBan: " + e.getMessage(), "Errorea",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- TAB SARRERAK ---
    private void sarreraTabSortu() {
        JPanel sarreraPanela = new JPanel(new BorderLayout());
        sarreraPanela.setOpaque(false);

        JPanel goikoAukeraPanela = new JPanel(new FlowLayout(FlowLayout.LEFT));
        goikoAukeraPanela.setOpaque(false);
        goikoAukeraPanela.add(new JLabel("Egoera Iragazi:"));

        egoeraIragazkia = new JComboBox<>();
        egoeraIragazkia.addItem("Denak");
        egoeraIragazkia.addItem("Bidean");
        egoeraIragazkia.addItem("Jasota");
        egoeraIragazkia.addActionListener(e -> sarreraDatuakKargatu());
        goikoAukeraPanela.add(egoeraIragazkia);

        JButton eguneratuBotoia = new JButton("Eguneratu Zerrenda");
        eguneratuBotoia.addActionListener(e -> sarreraDatuakKargatu());
        goikoAukeraPanela.add(eguneratuBotoia);

        sarreraPanela.add(goikoAukeraPanela, BorderLayout.NORTH);

        sarreraTaula = new JTable();
        JScrollPane ruli = new JScrollPane(sarreraTaula);
        sarreraPanela.add(ruli, BorderLayout.CENTER);

        pestainaPanela.addTab("Sarrerak", null, sarreraPanela, null);
    }

    // --- TAB BILTEGIAK ---
    private void biltegiTabSortu() {
        JPanel biltegiPanela = new JPanel(new BorderLayout());
        biltegiPanela.setOpaque(false);

        JPanel botoiPanela = new JPanel();
        botoiPanela.setOpaque(false);
        JButton sortuBotoia = new JButton("Sortu Biltegia");
        JButton aldatuBotoia = new JButton("Aldatu");
        JButton ezabatuBotoia = new JButton("Ezabatu");

        sortuBotoia.addActionListener(e -> sortuBiltegia());
        aldatuBotoia.addActionListener(e -> aldatuBiltegia());
        ezabatuBotoia.addActionListener(e -> ezabatuBiltegia());

        botoiPanela.add(sortuBotoia);
        botoiPanela.add(aldatuBotoia);
        botoiPanela.add(ezabatuBotoia);
        biltegiPanela.add(botoiPanela, BorderLayout.NORTH);

        biltegiTaula = new JTable();
        biltegiPanela.add(new JScrollPane(biltegiTaula), BorderLayout.CENTER);

        pestainaPanela.addTab("Biltegiak", null, biltegiPanela, null);
    }

    // --- TAB PRODUKTUAK ---
    private void produktuTabSortu() {
        JPanel produktuPanela = new JPanel(new BorderLayout());
        produktuPanela.setOpaque(false);

        JPanel botoiPanela = new JPanel();
        botoiPanela.setOpaque(false);
        JButton aldatuKokapenaBotoia = new JButton("Aldatu Biltegia");
        JButton jasoBotoia = new JButton("Markatu 'Jasota'");
        JButton bideanBotoia = new JButton("Markatu 'Bidean'");

        aldatuKokapenaBotoia.addActionListener(e -> aldatuProduktuarenBiltegia());
        jasoBotoia.addActionListener(e -> markatuProduktuaJasota());
        bideanBotoia.addActionListener(e -> markatuProduktuaBidean());

        botoiPanela.add(aldatuKokapenaBotoia);
        botoiPanela.add(jasoBotoia);
        botoiPanela.add(bideanBotoia);
        produktuPanela.add(botoiPanela, BorderLayout.NORTH);

        produktuTaula = new JTable();
        produktuPanela.add(new JScrollPane(produktuTaula), BorderLayout.CENTER);

        pestainaPanela.addTab("Produktuak eta Kokapena", null, produktuPanela, null);
    }

    // --- TAB SARRERA BERRIA ---
    private void sarreraBerriaTabSortu() {
        JPanel sarreraBerriaPanela = new JPanel(new BorderLayout());
        sarreraBerriaPanela.setOpaque(false);

        JPanel formularioPanela = new JPanel(new BorderLayout()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255, 255, 255, 230));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        formularioPanela.setOpaque(false);
        formularioPanela.setBorder(BorderFactory.createTitledBorder("Sarrera eta Produktu Berriaren Datuak"));

        JPanel hornitzailePanela = new JPanel(new GridLayout(2, 1));
        hornitzailePanela.setOpaque(false);

        JPanel hornAukeratuPanela = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hornAukeratuPanela.setOpaque(false);
        hornitzaileHautatzailea = new JComboBox<>();
        hornitzaileBerriaAukera = new JCheckBox("Hornitzaile Berria Sortu?");
        hornitzaileBerriaAukera.setOpaque(false);
        hornitzaileBerriaAukera.addActionListener(e -> hornitzaileModuaAldatu());

        hornAukeratuPanela.add(new JLabel("Hornitzailea Aukeratu: "));
        hornAukeratuPanela.add(hornitzaileHautatzailea);
        hornAukeratuPanela.add(hornitzaileBerriaAukera);

        JPanel hornBerriaPanela = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hornBerriaPanela.setOpaque(false);
        izenaBerriaTestua = new JTextField(15);
        postaBerriaTestua = new JTextField(15);
        ifzBerriaTestua = new JTextField(10);

        hornBerriaPanela.add(new JLabel("Izena:"));
        hornBerriaPanela.add(izenaBerriaTestua);
        hornBerriaPanela.add(new JLabel("Emaila:"));
        hornBerriaPanela.add(postaBerriaTestua);
        hornBerriaPanela.add(new JLabel("IFZ:"));
        hornBerriaPanela.add(ifzBerriaTestua);

        hornitzaileBerriaGaitu(false);

        hornitzailePanela.add(hornAukeratuPanela);
        hornitzailePanela.add(hornBerriaPanela);
        formularioPanela.add(hornitzailePanela, BorderLayout.NORTH);

        JPanel formPanela = new JPanel(new GridBagLayout());
        formPanela.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        produktuIzenaTestua = new JTextField(15);
        markaTestua = new JTextField(15);
        kategoriaHautatzailea = new JComboBox<>();
        motaHautatzailea = new JComboBox<>();
        biltegiHautatzaileaSarrera = new JComboBox<>();
        kantitateTestua = new JTextField(5);
        deskribapenaTestua = new JTextField(30);
        irudiaUrlTestua = new JTextField(20);
        egoeraOharraTestua = new JTextField(20);

        motaHautatzailea.addItem("Eramangarria");
        motaHautatzailea.addItem("Mahai-gainekoa");
        motaHautatzailea.addItem("Mugikorra");
        motaHautatzailea.addItem("Tableta");
        motaHautatzailea.addItem("Zerbitzaria");
        motaHautatzailea.addItem("Pantaila");
        motaHautatzailea.addItem("Softwarea");

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanela.add(new JLabel("Prod. Izena:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        formPanela.add(produktuIzenaTestua, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        formPanela.add(new JLabel("Marka:"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        formPanela.add(markaTestua, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanela.add(new JLabel("Kategoria:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        formPanela.add(kategoriaHautatzailea, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanela.add(new JLabel("Mota:"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        formPanela.add(motaHautatzailea, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanela.add(new JLabel("Biltegia:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        formPanela.add(biltegiHautatzaileaSarrera, gbc);
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanela.add(new JLabel("Kantitatea:"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        formPanela.add(kantitateTestua, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanela.add(new JLabel("Deskribapena:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        formPanela.add(deskribapenaTestua, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanela.add(new JLabel("Irudia URL:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;

        JPanel irudiPanela = new JPanel(new BorderLayout());
        irudiPanela.setOpaque(false);
        irudiPanela.add(irudiaUrlTestua, BorderLayout.CENTER);
        JButton igoBotoia = new JButton("Igo");
        igoBotoia.addActionListener(e -> igoIrudia());
        irudiPanela.add(igoBotoia, BorderLayout.EAST);

        formPanela.add(irudiPanela, gbc);
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanela.add(new JLabel("Oharra:"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        formPanela.add(egoeraOharraTestua, gbc);

        JButton gehituBotoia = new JButton("Gehitu Zerrendara +");
        gehituBotoia.addActionListener(e -> gehituLerroaTaulara());

        JPanel erdikoFormPanela = new JPanel(new BorderLayout());
        erdikoFormPanela.setOpaque(false);
        erdikoFormPanela.add(formPanela, BorderLayout.CENTER);

        JPanel botoiHustuPanela = new JPanel();
        botoiHustuPanela.setOpaque(false);
        botoiHustuPanela.add(gehituBotoia);
        erdikoFormPanela.add(botoiHustuPanela, BorderLayout.SOUTH);

        formularioPanela.add(erdikoFormPanela, BorderLayout.CENTER);
        sarreraBerriaPanela.add(formularioPanela, BorderLayout.NORTH);

        String[] zutabeIzenak = { "Izena", "Marka", "Kategoria", "Mota", "Biltegia", "Kantitatea", "Deskribapena",
                "Irudia", "Oharra" };
        lerroBerriEredua = new DefaultTableModel(zutabeIzenak, 0);
        lerroBerriTaula = new JTable(lerroBerriEredua);
        sarreraBerriaPanela.add(new JScrollPane(lerroBerriTaula), BorderLayout.CENTER);

        JButton gordeBotoia = new JButton("GORDE SARRERA ETA SORTU PRODUKTUAK");
        gordeBotoia.setBackground(new Color(0, 128, 0));
        gordeBotoia.setForeground(Color.WHITE);
        gordeBotoia.setFont(new Font("Arial", Font.BOLD, 14));
        gordeBotoia.addActionListener(e -> gordeSarreraOsoa());
        sarreraBerriaPanela.add(gordeBotoia, BorderLayout.SOUTH);

        pestainaPanela.addTab("Sarrera Berria", null, sarreraBerriaPanela, null);
    }

    // --- LAGUNTZAILEAK ---
    private void hornitzaileModuaAldatu() {
        boolean berriaDa = hornitzaileBerriaAukera.isSelected();
        hornitzaileBerriaGaitu(berriaDa);
        hornitzaileHautatzailea.setEnabled(!berriaDa);
    }

    private void hornitzaileBerriaGaitu(boolean gaitu) {
        izenaBerriaTestua.setEnabled(gaitu);
        postaBerriaTestua.setEnabled(gaitu);
        ifzBerriaTestua.setEnabled(gaitu);
        if (!gaitu) {
            izenaBerriaTestua.setText("");
            postaBerriaTestua.setText("");
            ifzBerriaTestua.setText("");
        }
    }

    private void sarreraHautatzaileakKargatu() {
        hornitzaileHautatzailea.removeAllItems();
        kategoriaHautatzailea.removeAllItems();
        biltegiHautatzaileaSarrera.removeAllItems();
        try (Connection konexioa = DB_Konexioa.konektatu()) {
            Statement sententzia = konexioa.createStatement();
            ResultSet rsH = sententzia.executeQuery("SELECT id_hornitzailea, izena_soziala FROM hornitzaileak");
            while (rsH.next())
                hornitzaileHautatzailea.addItem(new HornitzaileElementua(rsH.getInt(1), rsH.getString(2)));
            ResultSet rsK = sententzia.executeQuery("SELECT id_kategoria, izena FROM produktu_kategoriak");
            while (rsK.next())
                kategoriaHautatzailea.addItem(new KategoriaElementua(rsK.getInt(1), rsK.getString(2)));
            ResultSet rsB = sententzia.executeQuery("SELECT id_biltegia, izena FROM biltegiak");
            while (rsB.next())
                biltegiHautatzaileaSarrera.addItem(new BiltegiElementua(rsB.getInt(1), rsB.getString(2)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gehituLerroaTaulara() {
        String izena = produktuIzenaTestua.getText();
        String marka = markaTestua.getText();
        KategoriaElementua kat = (KategoriaElementua) kategoriaHautatzailea.getSelectedItem();
        String mota = (String) motaHautatzailea.getSelectedItem();
        BiltegiElementua bilt = (BiltegiElementua) biltegiHautatzaileaSarrera.getSelectedItem();
        String kantiStr = kantitateTestua.getText();
        if (izena.isEmpty() || marka.isEmpty() || kantiStr.isEmpty() || kat == null || bilt == null) {
            JOptionPane.showMessageDialog(this, "Mesedez, bete produktuaren eremu guztiak.");
            return;
        }
        try {
            int kanti = Integer.parseInt(kantiStr);
            if (kanti <= 0)
                throw new NumberFormatException();

            String desk = deskribapenaTestua.getText();
            String url = irudiaUrlTestua.getText();
            String oharra = egoeraOharraTestua.getText();

            lerroBerriEredua.addRow(new Object[] { izena, marka, kat, mota, bilt, kanti, desk, url, oharra });
            produktuIzenaTestua.setText("");
            markaTestua.setText("");
            kantitateTestua.setText("");
            deskribapenaTestua.setText("");
            irudiaUrlTestua.setText("");
            egoeraOharraTestua.setText("");
            produktuIzenaTestua.requestFocus();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Kantitateak zenbakia izan behar du.");
        }
    }

    private void igoIrudia() {
        if (fitxategiHautatzailea == null) {
            fitxategiHautatzailea = new JFileChooser();
            javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter(
                    "Irudiak (JPG, PNG)", "jpg", "jpeg", "png");
            fitxategiHautatzailea.setFileFilter(filter);
        }

        int result = fitxategiHautatzailea.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fitxategiHautatzailea.getSelectedFile();
            try {
                // Karpetaren bidea lortu
                String baseDir = "src/birtek_interfaze_grafikoa/irudiak/";
                File destDir = new File(baseDir);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                // Izena mantendu (edo garbitu espazioak)
                String fileName = selectedFile.getName().replaceAll("\\s+", "_");
                File destFile = new File(destDir, fileName);

                // Kopiatu
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Testu-eremuan bidea jarri
                irudiaUrlTestua.setText("irudiak/" + fileName);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Errorea irudia igotzean: " + ex.getMessage());
            }
        }
    }

    private void gordeSarreraOsoa() {
        if (lerroBerriEredua.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Ez dago produkturik zerrendan.");
            return;
        }
        int hornitzaileaId = -1;
        if (hornitzaileBerriaAukera.isSelected()) {
            String izena = izenaBerriaTestua.getText().trim();
            String email = postaBerriaTestua.getText().trim();
            String ifz = ifzBerriaTestua.getText().trim();
            if (izena.isEmpty() || email.isEmpty() || ifz.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bete hornitzaile berriaren datuak.");
                return;
            }
            try (Connection konexioa = DB_Konexioa.konektatu()) {
                PreparedStatement pstCheck = konexioa
                        .prepareStatement("SELECT COUNT(*) FROM hornitzaileak WHERE emaila = ? OR ifz_nan = ?");
                pstCheck.setString(1, email);
                pstCheck.setString(2, ifz);
                ResultSet rsCheck = pstCheck.executeQuery();
                if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "ERROREA: Hornitzaile hori existitzen da jada.", "Bikoiztua",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String sqlInsertH = "INSERT INTO hornitzaileak (izena_soziala, ifz_nan, emaila, pasahitza, helbidea, herria_id, posta_kodea) VALUES (?, ?, ?, '1234', 'Zehaztugabea', 1, '00000')";
                PreparedStatement pstH = konexioa.prepareStatement(sqlInsertH, Statement.RETURN_GENERATED_KEYS);
                pstH.setString(1, izena);
                pstH.setString(2, ifz);
                pstH.setString(3, email);
                pstH.executeUpdate();
                ResultSet rsKey = pstH.getGeneratedKeys();
                if (rsKey.next())
                    hornitzaileaId = rsKey.getInt(1);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Errorea hornitzailea sortzean: " + e.getMessage());
                return;
            }
        } else {
            HornitzaileElementua item = (HornitzaileElementua) hornitzaileHautatzailea.getSelectedItem();
            if (item != null)
                hornitzaileaId = item.id;
            else {
                JOptionPane.showMessageDialog(this, "Aukeratu hornitzaile bat.");
                return;
            }
        }

        Connection con = null;
        try {
            con = DB_Konexioa.konektatu();
            con.setAutoCommit(false);
            String sqlSarrera = "INSERT INTO sarrerak (hornitzailea_id, langilea_id, sarrera_egoera) VALUES (?, ?, 'Bidean')";
            PreparedStatement pstSarrera = con.prepareStatement(sqlSarrera, Statement.RETURN_GENERATED_KEYS);
            pstSarrera.setInt(1, hornitzaileaId);
            pstSarrera.setInt(2, langilea.getIdLangilea());
            pstSarrera.executeUpdate();
            ResultSet rsKeys = pstSarrera.getGeneratedKeys();
            int sarreraId = -1;
            if (rsKeys.next())
                sarreraId = rsKeys.getInt(1);
            else
                throw new SQLException("Ez da sarrera IDrik sortu.");

            String sqlProd = "INSERT INTO produktuak (izena, marka, kategoria_id, mota, biltegi_id, hornitzaile_id, stock, produktu_egoera, deskribapena, irudia_url, produktu_egoera_oharra, salgai) VALUES (?, ?, ?, ?, ?, ?, ?, 'Zehazteko', ?, ?, ?, 0)";
            PreparedStatement pstProd = con.prepareStatement(sqlProd, Statement.RETURN_GENERATED_KEYS);
            String sqlLerroa = "INSERT INTO sarrera_lerroak (sarrera_id, produktua_id, kantitatea, sarrera_lerro_egoera) VALUES (?, ?, ?, 'Bidean')";
            PreparedStatement pstLerroa = con.prepareStatement(sqlLerroa);

            for (int i = 0; i < lerroBerriEredua.getRowCount(); i++) {
                String izena = (String) lerroBerriEredua.getValueAt(i, 0);
                String marka = (String) lerroBerriEredua.getValueAt(i, 1);
                KategoriaElementua kat = (KategoriaElementua) lerroBerriEredua.getValueAt(i, 2);
                String mota = (String) lerroBerriEredua.getValueAt(i, 3);
                BiltegiElementua bilt = (BiltegiElementua) lerroBerriEredua.getValueAt(i, 4);
                int kanti = (Integer) lerroBerriEredua.getValueAt(i, 5);
                String desk = (String) lerroBerriEredua.getValueAt(i, 6);
                String imgUrl = (String) lerroBerriEredua.getValueAt(i, 7);
                String oharra = (String) lerroBerriEredua.getValueAt(i, 8);

                pstProd.setString(1, izena);
                pstProd.setString(2, marka);
                pstProd.setInt(3, kat.id);
                pstProd.setString(4, mota);
                pstProd.setInt(5, bilt.id);
                pstProd.setInt(6, hornitzaileaId);
                pstProd.setInt(7, kanti);
                pstProd.setString(8, desk);
                pstProd.setString(9, imgUrl);
                pstProd.setString(10, oharra);
                pstProd.executeUpdate();
                ResultSet rsProdKey = pstProd.getGeneratedKeys();
                int prodId = -1;
                if (rsProdKey.next())
                    prodId = rsProdKey.getInt(1);
                pstLerroa.setInt(1, sarreraId);
                pstLerroa.setInt(2, prodId);
                pstLerroa.setInt(3, kanti);
                pstLerroa.executeUpdate();
            }
            con.commit();
            lerroBerriEredua.setRowCount(0);
            izenaBerriaTestua.setText("");
            postaBerriaTestua.setText("");
            ifzBerriaTestua.setText("");
            hornitzaileBerriaAukera.setSelected(false);
            hornitzaileModuaAldatu();
            sarreraHautatzaileakKargatu();
        } catch (SQLException e) {
            try {
                if (con != null)
                    con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Errorea prozesuan: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sarreraDatuakKargatu() {
        String baseSql = "SELECT s.id_sarrera, h.izena_soziala AS Hornitzailea, s.data, s.sarrera_egoera FROM sarrerak s JOIN hornitzaileak h ON s.hornitzailea_id = h.id_hornitzailea ";
        String filter = (String) egoeraIragazkia.getSelectedItem();
        String sql = baseSql;
        if ("Bidean".equals(filter))
            sql += " WHERE s.sarrera_egoera = 'Bidean' ";
        else if ("Jasota".equals(filter))
            sql += " WHERE s.sarrera_egoera = 'Jasota' ";
        sql += " ORDER BY s.data DESC";
        try (Connection con = DB_Konexioa.konektatu(); PreparedStatement pst = con.prepareStatement(sql)) {
            DefaultTableModel eredua = TaulaModelatzailea.ereduaEraiki(pst.executeQuery());
            sarreraTaula.setModel(eredua);
            sarreraOrdenatzailea = new TableRowSorter<>(eredua);
            sarreraTaula.setRowSorter(sarreraOrdenatzailea);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void biltegiDatuakKargatu() {
        String sql = "SELECT id_biltegia, izena, biltegi_sku FROM biltegiak";
        try (Connection con = DB_Konexioa.konektatu(); PreparedStatement pst = con.prepareStatement(sql)) {
            DefaultTableModel eredua = TaulaModelatzailea.ereduaEraiki(pst.executeQuery());
            biltegiTaula.setModel(eredua);
            biltegiOrdenatzailea = new TableRowSorter<>(eredua);
            biltegiTaula.setRowSorter(biltegiOrdenatzailea);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void produktuDatuakKargatu() {
        String sql = "SELECT p.id_produktua, p.izena AS Produktua, b.izena AS Biltegia, s.id_sarrera AS 'Sarrera ID', sl.sarrera_lerro_egoera AS Egoera, s.data AS 'Sarrera Data', sl.id_sarrera_lerroa FROM sarrera_lerroak sl JOIN sarrerak s ON sl.sarrera_id = s.id_sarrera JOIN produktuak p ON sl.produktua_id = p.id_produktua JOIN biltegiak b ON p.biltegi_id = b.id_biltegia ORDER BY s.data DESC";
        try (Connection con = DB_Konexioa.konektatu(); PreparedStatement pst = con.prepareStatement(sql)) {
            DefaultTableModel eredua = TaulaModelatzailea.ereduaEraiki(pst.executeQuery());
            produktuTaula.setModel(eredua);
            produktuOrdenatzailea = new TableRowSorter<>(eredua);
            produktuTaula.setRowSorter(produktuOrdenatzailea);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filtratu() {
        String testua = bilatuTestua.getText();
        TableRowSorter<DefaultTableModel> unekoOrdenatzailea = null;
        int index = pestainaPanela.getSelectedIndex();
        if (index == 0)
            unekoOrdenatzailea = sarreraOrdenatzailea;
        else if (index == 1)
            unekoOrdenatzailea = biltegiOrdenatzailea;
        else if (index == 2)
            unekoOrdenatzailea = produktuOrdenatzailea;
        if (unekoOrdenatzailea != null) {
            if (testua.trim().length() == 0)
                unekoOrdenatzailea.setRowFilter(null);
            else
                unekoOrdenatzailea.setRowFilter(RowFilter.regexFilter("(?i)" + testua));
        }
    }

    private void sortuBiltegia() {
        JTextField izenaEremua = new JTextField();
        JTextField skuEremua = new JTextField();
        Object[] mezua = { "Biltegi Izena:", izenaEremua, "SKU Kodea:", skuEremua };
        int aukera = JOptionPane.showConfirmDialog(this, mezua, "Biltegi Berria", JOptionPane.OK_CANCEL_OPTION);
        if (aukera == JOptionPane.OK_OPTION) {
            try (Connection con = DB_Konexioa.konektatu();
                    PreparedStatement pst = con
                            .prepareStatement("INSERT INTO biltegiak (izena, biltegi_sku) VALUES (?, ?)")) {
                pst.setString(1, izenaEremua.getText());
                pst.setString(2, skuEremua.getText());
                pst.executeUpdate();
                biltegiDatuakKargatu();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
            }
        }
    }

    private void ezabatuBiltegia() {
        int row = biltegiTaula.getSelectedRow();
        if (row == -1)
            return;
        int modelRow = biltegiTaula.convertRowIndexToModel(row);
        Object val = biltegiTaula.getModel().getValueAt(modelRow, 0);
        int id = (val instanceof Number) ? ((Number) val).intValue() : Integer.parseInt(val.toString());
        try (Connection con = DB_Konexioa.konektatu()) {
            PreparedStatement pstCheck = con.prepareStatement("SELECT COUNT(*) FROM produktuak WHERE biltegi_id = ?");
            pstCheck.setInt(1, id);
            ResultSet rs = pstCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "EZIN DA EZABATU: Produktuak ditu barruan.", "Errorea",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Ziur ezabatu nahi duzula?", "Ezabatu",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                PreparedStatement pst = con.prepareStatement("DELETE FROM biltegiak WHERE id_biltegia = ?");
                pst.setInt(1, id);
                pst.executeUpdate();
                biltegiDatuakKargatu();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void aldatuBiltegia() {
        int row = biltegiTaula.getSelectedRow();
        if (row == -1)
            return;
        int modelRow = biltegiTaula.convertRowIndexToModel(row);
        int id = Integer.parseInt(biltegiTaula.getModel().getValueAt(modelRow, 0).toString());
        String izenaZaharra = (String) biltegiTaula.getModel().getValueAt(modelRow, 1);
        String skuZaharra = (String) biltegiTaula.getModel().getValueAt(modelRow, 2);
        JTextField izenaEremua = new JTextField(izenaZaharra);
        JTextField skuEremua = new JTextField(skuZaharra);
        if (JOptionPane.showConfirmDialog(this, new Object[] { "Izena:", izenaEremua, "SKU:", skuEremua }, "Aldatu",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (Connection con = DB_Konexioa.konektatu();
                    PreparedStatement pst = con.prepareStatement(
                            "UPDATE biltegiak SET izena = ?, biltegi_sku = ? WHERE id_biltegia = ?")) {
                pst.setString(1, izenaEremua.getText());
                pst.setString(2, skuEremua.getText());
                pst.setInt(3, id);
                pst.executeUpdate();
                biltegiDatuakKargatu();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void aldatuProduktuarenBiltegia() {
        int row = produktuTaula.getSelectedRow();
        if (row == -1)
            return;
        int modelRow = produktuTaula.convertRowIndexToModel(row);
        int idProd = Integer.parseInt(produktuTaula.getModel().getValueAt(modelRow, 0).toString());
        JComboBox<BiltegiElementua> hautatzailea = new JComboBox<>();
        try (Connection con = DB_Konexioa.konektatu()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT id_biltegia, izena FROM biltegiak");
            while (rs.next())
                hautatzailea.addItem(new BiltegiElementua(rs.getInt(1), rs.getString(2)));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (JOptionPane.showConfirmDialog(this, hautatzailea, "Aukeratu Biltegi Berria",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (Connection con = DB_Konexioa.konektatu();
                    PreparedStatement pst = con
                            .prepareStatement("UPDATE produktuak SET biltegi_id = ? WHERE id_produktua = ?")) {
                pst.setInt(1, ((BiltegiElementua) hautatzailea.getSelectedItem()).id);
                pst.setInt(2, idProd);
                pst.executeUpdate();
                produktuDatuakKargatu();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void egiaztatuSarreraEgoera(int idSarrera) {
        try (Connection con = DB_Konexioa.konektatu()) {
            PreparedStatement pst = con.prepareStatement(
                    "SELECT COUNT(*) FROM sarrera_lerroak WHERE sarrera_id = ? AND sarrera_lerro_egoera != 'Jasota'");
            pst.setInt(1, idSarrera);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String egoera = (rs.getInt(1) == 0) ? "Jasota" : "Bidean";
                PreparedStatement pstUpd = con
                        .prepareStatement("UPDATE sarrerak SET sarrera_egoera = ? WHERE id_sarrera = ?");
                pstUpd.setString(1, egoera);
                pstUpd.setInt(2, idSarrera);
                pstUpd.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void markatuProduktuaJasota() {
        produktuEgoeraAldatu("Jasota");
    }

    private void markatuProduktuaBidean() {
        produktuEgoeraAldatu("Bidean");
    }

    private void produktuEgoeraAldatu(String egoeraBerria) {
        int row = produktuTaula.getSelectedRow();
        if (row == -1)
            return;
        int modelRow = produktuTaula.convertRowIndexToModel(row);
        Object valSarrera = produktuTaula.getModel().getValueAt(modelRow, 3);
        Object valLerroa = produktuTaula.getModel().getValueAt(modelRow, 6);
        int idSarrera = Integer.parseInt(valSarrera.toString());
        int idLerroa = Integer.parseInt(valLerroa.toString());
        try (Connection con = DB_Konexioa.konektatu()) {
            PreparedStatement pst = con.prepareStatement(
                    "UPDATE sarrera_lerroak SET sarrera_lerro_egoera = ? WHERE id_sarrera_lerroa = ?");
            pst.setString(1, egoeraBerria);
            pst.setInt(2, idLerroa);
            if (pst.executeUpdate() > 0) {
                egiaztatuSarreraEgoera(idSarrera);
                produktuDatuakKargatu();
                sarreraDatuakKargatu();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saioaItxi() {
        if (JOptionPane.showConfirmDialog(this, "Ziur zaude saioa itxi nahi duzula?", "Logout",
                JOptionPane.YES_NO_OPTION) == 0) {
            dispose();
            new SaioaHastekoPanela().setVisible(true);
        }
    }

    // --- HELPER CLASSES ---
    static class BiltegiElementua {
        int id;
        String izena;

        public BiltegiElementua(int id, String izena) {
            this.id = id;
            this.izena = izena;
        }

        public String toString() {
            return izena;
        }
    }

    static class HornitzaileElementua {
        int id;
        String izena;

        public HornitzaileElementua(int id, String izena) {
            this.id = id;
            this.izena = izena;
        }

        public String toString() {
            return izena;
        }
    }

    static class KategoriaElementua {
        int id;
        String izena;

        public KategoriaElementua(int id, String izena) {
            this.id = id;
            this.izena = izena;
        }

        public String toString() {
            return izena;
        }
    }

    static class AtzealdekoPanela extends JPanel {
        private static final long serialVersionUID = 1L;
        private Image irudia;

        public AtzealdekoPanela() {
            try {
                java.net.URL imgURL = MenuLogistika.class.getResource("/birtek_biltegia.png");
                if (imgURL != null)
                    irudia = new ImageIcon(imgURL).getImage();
                else if (new File("src/birtek_biltegia.png").exists())
                    irudia = new ImageIcon("src/birtek_biltegia.png").getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (irudia != null) {
                int panelW = getWidth();
                int panelH = getHeight();
                int imgW = irudia.getWidth(this);
                int imgH = irudia.getHeight(this);
                if (imgW > 0 && imgH > 0) {
                    double eskalatzea = Math.max((double) panelW / imgW, (double) panelH / imgH);
                    int newW = (int) (imgW * eskalatzea);
                    int newH = (int) (imgH * eskalatzea);
                    int x = (panelW - newW) / 2;
                    int y = (panelH - newH) / 2;
                    g.drawImage(irudia, x, y, newW, newH, this);
                } else {
                    g.drawImage(irudia, 0, 0, panelW, panelH, this);
                }
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, panelW, panelH);
            } else {
                g.setColor(new Color(245, 245, 245));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
}
