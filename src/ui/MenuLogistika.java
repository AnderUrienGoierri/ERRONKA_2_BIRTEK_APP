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

/**
 * MenuLogistika klasea.
 * Biltegi langilearen interfaze nagusia.
 * Sarrerak, biltegiak, produktuak, eskaerak eta hornitzaileak kudeatzeko
 * aukerak.
 */
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
    private JTable nireFitxaketaTaula;

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
    private TableRowSorter<DefaultTableModel> nireFitxaketaOrdenatzailea;

    // Eskaerak
    private JTable eskaeraTaula;
    private TableRowSorter<DefaultTableModel> eskaeraOrdenatzailea;
    private JComboBox<String> eskaeraEgoeraIragazkia;

    /**
     * Eraikitzaileak eguneratua.
     */
    /**
     * MenuLogistika eraikitzailea.
     * 
     * @param oinarrizkoLangilea Saioa hasi duen langilea.
     */
    public MenuLogistika(Langilea oinarrizkoLangilea) {
        this.langilea = new BiltegiLangilea(oinarrizkoLangilea);
        pantailaPrestatu();
    }

    /**
     * Eraikitzailea lehenetsia.
     */
    public MenuLogistika() {
        this(new Langilea(Sesioa.idLangilea, Sesioa.izena, Sesioa.abizena, "", null, 0, "", "", "", "", "ES", "", "",
                null, null, true, 2, "", null));
    }

    /**
     * Pantailaren osagaiak prestatu.
     */
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
        eskaeraTabSortu(); // NEW
        sarreraBerriaTabSortu();
        nireFitxaketaTabSortu();

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
                eskaeraDatuakKargatu();
            else if (index == 4)
                sarreraHautatzaileakKargatu();
            else if (index == 5)
                nireFitxaketaDatuakKargatu();
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
    /**
     * Fitxaketa bat egin (Sarrera/Irteera).
     * 
     * @param mota Fitxaketa mota.
     */
    private void fitxatu(String mota) {
        try {
            if ("Sarrera".equals(mota)) {
                langilea.sarreraFitxaketaEgin();
            } else {
                langilea.irteeraFitxaketaEgin();
            }
            eguneratuFitxaketaEgoera();
            nireFitxaketaDatuakKargatu(); // Refresh personal attendance
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errorea", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Fitxaketa egoera eguneratu interfazean.
     */
    private void eguneratuFitxaketaEgoera() {
        fitxaketaInfoEtiketa.setText(langilea.getFitxaketaEgoera());
        if (fitxaketaInfoEtiketa.getText().contains("BARRUAN")) {
            fitxaketaInfoEtiketa.setForeground(new Color(0, 100, 0));
        } else {
            fitxaketaInfoEtiketa.setForeground(new Color(200, 0, 0));
        }
    }

    /**
     * Fitxaketa historia erakutsi.
     */
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

    /**
     * Norberaren datuak editatzeko leihoa ireki.
     */
    private void irekiNireDatuakEditatu() {
        NireDatuakDialog dialog = new NireDatuakDialog(this, langilea);
        dialog.setVisible(true);
    }

    // --- TAB SARRERAK ---
    // --- TAB SARRERAK ---
    /**
     * Sarreren fitxa ("tab") sortu eta konfiguratu.
     */
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

        // NEW BUTTONS
        JButton ikusiLerroakBotoia = new JButton("Ikusi Lerroak");
        ikusiLerroakBotoia.addActionListener(e -> ikusiSarreraLerroak());
        goikoAukeraPanela.add(ikusiLerroakBotoia);

        JButton editatuBotoia = new JButton("Editatu Egoera");
        editatuBotoia.addActionListener(e -> editatuSarrera());
        goikoAukeraPanela.add(editatuBotoia);

        JButton ezabatuBotoia = new JButton("Ezabatu");
        ezabatuBotoia.setBackground(new Color(255, 100, 100));
        ezabatuBotoia.addActionListener(e -> ezabatuSarrera());
        goikoAukeraPanela.add(ezabatuBotoia);

        sarreraPanela.add(goikoAukeraPanela, BorderLayout.NORTH);

        sarreraTaula = new JTable();
        JScrollPane ruli = new JScrollPane(sarreraTaula);
        sarreraPanela.add(ruli, BorderLayout.CENTER);

        pestainaPanela.addTab("Sarrerak", null, sarreraPanela, null);
    }

    // --- TAB BILTEGIAK ---
    /**
     * Biltegien fitxa sortu.
     */
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
    /**
     * Produktuen fitxa sortu.
     */
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

        JButton oharraBotoia = new JButton("Editatu Oharra");
        oharraBotoia.addActionListener(e -> editatuProduktuOharra());
        botoiPanela.add(oharraBotoia);

        produktuPanela.add(botoiPanela, BorderLayout.NORTH);

        produktuTaula = new JTable();
        produktuPanela.add(new JScrollPane(produktuTaula), BorderLayout.CENTER);

        pestainaPanela.addTab("Produktuak eta Kokapena", null, produktuPanela, null);
    }

    // --- TAB ESKAERAK (NEW) ---
    /**
     * Eskaeren fitxa sortu.
     */
    private void eskaeraTabSortu() {
        JPanel eskaeraPanela = new JPanel(new BorderLayout());
        eskaeraPanela.setOpaque(false);

        JPanel goikoAukeraPanela = new JPanel(new FlowLayout(FlowLayout.LEFT));
        goikoAukeraPanela.setOpaque(false);
        goikoAukeraPanela.add(new JLabel("Egoera Iragazi:"));

        eskaeraEgoeraIragazkia = new JComboBox<>();
        eskaeraEgoeraIragazkia.addItem("Denak");
        eskaeraEgoeraIragazkia.addItem("Prestatzen");
        eskaeraEgoeraIragazkia.addItem("Osatua/Bidalita");
        eskaeraEgoeraIragazkia.addActionListener(e -> eskaeraDatuakKargatu());
        goikoAukeraPanela.add(eskaeraEgoeraIragazkia);

        JButton eguneratuBotoia = new JButton("Eguneratu");
        eguneratuBotoia.addActionListener(e -> eskaeraDatuakKargatu());
        goikoAukeraPanela.add(eguneratuBotoia);

        JButton lerroakBotoia = new JButton("Ikusi Lerroak / Kudeatu");
        lerroakBotoia.setFont(new Font("SansSerif", Font.BOLD, 12));
        lerroakBotoia.setBackground(new Color(200, 230, 255));
        lerroakBotoia.addActionListener(e -> ikusiEskaeraLerroak());
        goikoAukeraPanela.add(lerroakBotoia);

        eskaeraPanela.add(goikoAukeraPanela, BorderLayout.NORTH);

        eskaeraTaula = new JTable();
        JScrollPane ruli = new JScrollPane(eskaeraTaula);
        eskaeraPanela.add(ruli, BorderLayout.CENTER);

        pestainaPanela.addTab("Eskaerak", null, eskaeraPanela, null);
    }

    // --- TAB SARRERA BERRIA ---
    /**
     * Sarrera berria egiteko fitxa sortu.
     */
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
    /**
     * Hornitzaile modua aldatu (lehendik dagoena edo berria).
     */
    private void hornitzaileModuaAldatu() {
        boolean berriaDa = hornitzaileBerriaAukera.isSelected();
        hornitzaileBerriaGaitu(berriaDa);
        hornitzaileHautatzailea.setEnabled(!berriaDa);
    }

    /**
     * Hornitzaile berriaren eremuak gaitu edo desgaitu.
     * 
     * @param gaitu True gaitzeko.
     */
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

    /**
     * Sarrera berrirako hautatzaileak (ComboBox) kargatu datu-basetik.
     */
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

    /**
     * Produktu lerro berria gehitu sarrera berriaren taulara.
     */
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

    /**
     * Irudia igo eta proiektuan kopiatu.
     */
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

    /**
     * Sarrera osoa (eta produktuak/lerroak) datu-basean gorde.
     */
    private void gordeSarreraOsoa() {
        if (lerroBerriEredua.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Ez dago produkturik zerrendan.");
            return;
        }

        try {
            int hornitzaileaId = -1;
            if (hornitzaileBerriaAukera.isSelected()) {
                String izena = izenaBerriaTestua.getText().trim();
                String email = postaBerriaTestua.getText().trim();
                String ifz = ifzBerriaTestua.getText().trim();
                if (izena.isEmpty() || email.isEmpty() || ifz.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Bete hornitzaile berriaren datuak.");
                    return;
                }
                hornitzaileaId = langilea.hornitzaileBerriaSortu(izena, ifz, email);
            } else {
                HornitzaileElementua item = (HornitzaileElementua) hornitzaileHautatzailea.getSelectedItem();
                if (item != null)
                    hornitzaileaId = item.id;
                else {
                    JOptionPane.showMessageDialog(this, "Aukeratu hornitzaile bat.");
                    return;
                }
            }

            java.util.List<Produktua> produktuList = new java.util.ArrayList<>();
            java.util.List<SarreraLerroa> lerroList = new java.util.ArrayList<>();

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

                // Using a temporary Produktua object holder. Not all fields are filled.
                // NOTE: Creating Produktua subclass instances would be cleaner if specific
                // types are used,
                // but for now we need a concrete class or use anonymous subclass to hold data.
                // Assuming Produktua is Abstract, so I can't instantiate it directly unless I
                // use anonymous class
                // or a concrete subclass. Since `Mota` is a string, I probably should have a
                // Factory or similar,
                // but to keep it simple and given the constraints, I'll create a simple
                // anonymous subclass holder
                // or just one of the concrete classes like `Eramangarria`.
                // Let's use `Eramangarria` as a generic container since we just need the fields
                // for the method.
                // IMPORTANT: The `mota` field decides the discrimination in DB usually, but
                // here we just pass data.

                Produktua p = new Eramangarria(0, hornitzaileaId, kat.id, izena, marka, mota, desk, imgUrl, bilt.id,
                        "Zehazteko", oharra, false, java.math.BigDecimal.ZERO, kanti, java.math.BigDecimal.ZERO,
                        java.math.BigDecimal.ZERO, null, null, "", 0, 0, java.math.BigDecimal.ZERO, 0, "",
                        java.math.BigDecimal.ZERO);
                produktuList.add(p);

                // Temp SarreraLerroa
                SarreraLerroa l = new SarreraLerroa(0, 0, 0, kanti, "Bidean");
                lerroList.add(l);
            }

            langilea.produktuSarreraBerriaSortu(hornitzaileaId, produktuList, lerroList);

            lerroBerriEredua.setRowCount(0);
            izenaBerriaTestua.setText("");
            postaBerriaTestua.setText("");
            ifzBerriaTestua.setText("");
            hornitzaileBerriaAukera.setSelected(false);
            hornitzaileModuaAldatu();
            sarreraHautatzaileakKargatu();
            JOptionPane.showMessageDialog(this, "Sarrera eta produktuak ondo sortu dira.");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errorea prozesuan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sarreren datuak taulan kargatu.
     */
    private void sarreraDatuakKargatu() {
        try {
            String filter = (String) egoeraIragazkia.getSelectedItem();
            if ("Denak".equals(filter))
                filter = ""; // Or handle null inside logic if preferred
            java.util.List<Object[]> data = langilea.produktuSarrerakIkusi(filter);

            String[] colNames = { "ID", "Hornitzailea", "Data", "Egoera" };
            DefaultTableModel eredua = new DefaultTableModel(colNames, 0);
            for (Object[] row : data) {
                eredua.addRow(row);
            }
            sarreraTaula.setModel(eredua);
            sarreraOrdenatzailea = new TableRowSorter<>(eredua);
            sarreraTaula.setRowSorter(sarreraOrdenatzailea);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Biltegien datuak taulan kargatu.
     */
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

    /**
     * Produktuen datuak taulan kargatu.
     */
    private void produktuDatuakKargatu() {
        String sql = "SELECT p.id_produktua, p.izena AS Produktua, b.izena AS Biltegia, s.id_sarrera AS 'Sarrera ID', sl.sarrera_lerro_egoera AS Egoera, s.data AS 'Sarrera Data', sl.id_sarrera_lerroa, p.produktu_egoera_oharra AS Oharra"
                + "FROM sarrera_lerroak sl"
                + "JOIN sarrerak s ON sl.sarrera_id = s.id_sarrera"
                + "JOIN produktuak p ON sl.produktua_id = p.id_produktua"
                + "JOIN biltegiak b ON p.biltegi_id = b.id_biltegia ORDER BY s.data DESC";
        try (Connection con = DB_Konexioa.konektatu(); PreparedStatement pst = con.prepareStatement(sql)) {
            DefaultTableModel eredua = TaulaModelatzailea.ereduaEraiki(pst.executeQuery());
            produktuTaula.setModel(eredua);
            produktuOrdenatzailea = new TableRowSorter<>(eredua);
            produktuTaula.setRowSorter(produktuOrdenatzailea);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Taulak iragazi bilaketa testuaren arabera.
     */
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
        else if (index == 3)
            unekoOrdenatzailea = eskaeraOrdenatzailea;
        else if (index == 5)
            unekoOrdenatzailea = nireFitxaketaOrdenatzailea;
        if (unekoOrdenatzailea != null) {
            if (testua.trim().length() == 0)
                unekoOrdenatzailea.setRowFilter(null);
            else
                unekoOrdenatzailea.setRowFilter(RowFilter.regexFilter("(?i)" + testua));
        }
    }

    // --- SARRERA LOGIKA DESARROILUA ---

    /**
     * Hautatutako sarreraren lerroak ikusi eta haien egoera aldatzeko leihoa.
     */
    private void ikusiSarreraLerroak() {
        int row = sarreraTaula.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu sarrera bat lehenbizi.");
            return;
        }
        int idSarrera = ((Number) sarreraTaula.getValueAt(row, 0)).intValue();

        JDialog dialog = new JDialog(this, "Sarrera Kodea: " + idSarrera + " - Lerroak", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        String[] columns = { "ID", "Produktua", "Marka", "Kantitatea", "Egoera" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);

        // Logic extracted to method for refreshing
        kargatuSarreraLerroak(idSarrera, model);

        JPanel btnPanel = new JPanel();
        JButton aldatuBtn = new JButton("Aldatu Lerroaren Egoera");
        aldatuBtn.addActionListener(e -> {
            int lRow = table.getSelectedRow();
            if (lRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Aukeratu lerro bat.");
                return;
            }
            int idLerroa = ((Number) table.getValueAt(lRow, 0)).intValue();
            String unekoSt = (String) table.getValueAt(lRow, 4);

            String[] opts = { "Bidean", "Jasota", "Ezabatua" };
            String berria = (String) JOptionPane.showInputDialog(dialog,
                    "Aukeratu egoera:", "Egoera Aldatu",
                    JOptionPane.QUESTION_MESSAGE, null, opts, unekoSt);

            if (berria != null) {
                try {
                    langilea.produktuSarreraLerroEgoeraAldatu(idLerroa, berria);
                    kargatuSarreraLerroak(idSarrera, model);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "Errorea: " + ex.getMessage());
                }
            }
        });

        JButton itxiBtn = new JButton("Itxi");
        itxiBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(aldatuBtn);
        btnPanel.add(itxiBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * Sarrera baten lerroak taulan kargatu.
     * 
     * @param idSarrera Sarreraren IDa.
     * @param model     Taula eredua.
     */
    private void kargatuSarreraLerroak(int idSarrera, DefaultTableModel model) {
        model.setRowCount(0);
        try {
            java.util.List<Object[]> lerroak = langilea.produktuSarreraLerroakIkusi(idSarrera);
            for (Object[] o : lerroak) {
                model.addRow(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errorea datuak kargatzean: " + e.getMessage());
        }
    }

    /**
     * Sarrera baten egoera orokorra editatu.
     */
    private void editatuSarrera() {
        int row = sarreraTaula.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu sarrera bat lehenbizi.");
            return;
        }
        int idSarrera = ((Number) sarreraTaula.getValueAt(row, 0)).intValue();
        String unekoEgoera = (String) sarreraTaula.getValueAt(row, 3);

        String[] options = { "Bidean", "Jasota", "Ezabatua" };
        String berria = (String) JOptionPane.showInputDialog(this,
                "Aukeratu Egoera Berria:", "Egoera Aldatu",
                JOptionPane.QUESTION_MESSAGE, null, options, unekoEgoera);

        if (berria != null && !berria.equals(unekoEgoera)) {
            try {
                langilea.produktuSarreraEditatu(idSarrera, berria);
                sarreraDatuakKargatu();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Errorea eguneratzean: " + e.getMessage());
            }
        }
    }

    /**
     * Sarrera bat eta bere lerro guztiak ezabatu.
     */
    private void ezabatuSarrera() {
        int row = sarreraTaula.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu sarrera bat lehenbizi.");
            return;
        }
        int idSarrera = ((Number) sarreraTaula.getValueAt(row, 0)).intValue();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Ziur zaude sarrera hau eta bere lerro guztiak ezabatu nahi dituzula?",
                "Ezabatu", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                langilea.produktuSarreraEzabatu(idSarrera);
                sarreraDatuakKargatu();
                JOptionPane.showMessageDialog(this, "Sarrera ezabatua.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Errorea ezabatzean: " + e.getMessage());
            }
        }
    }

    // --- ESKAERA LOGIKA DESARROILUA ---

    /**
     * Eskaeren datuak kargatu taulan.
     */
    private void eskaeraDatuakKargatu() {
        try {
            String filter = (String) eskaeraEgoeraIragazkia.getSelectedItem();
            java.util.List<Object[]> data = langilea.produktuEskaerakIkusi(filter);

            String[] colNames = { "ID", "Bezeroa", "Data", "Guztira (€)", "Egoera" };
            DefaultTableModel eredua = new DefaultTableModel(colNames, 0);
            for (Object[] row : data) {
                eredua.addRow(row);
            }
            eskaeraTaula.setModel(eredua);
            eskaeraOrdenatzailea = new TableRowSorter<>(eredua);
            eskaeraTaula.setRowSorter(eskaeraOrdenatzailea);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Eskaera baten lerroak ikusi eta kudeatu.
     */
    private void ikusiEskaeraLerroak() {
        int row = eskaeraTaula.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu eskaera bat lehenbizi.");
            return;
        }
        int idEskaera = ((Number) eskaeraTaula.getValueAt(row, 0)).intValue();

        JDialog dialog = new JDialog(this, "Eskaera Kodea: " + idEskaera + " - Kudeaketa", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        String[] columns = { "ID", "Produktua", "Kantitatea", "P.Unit.(€)", "Egoera" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);

        // Karga Hasieran
        kargatuEskaeraLerroak(idEskaera, model);

        JPanel btnPanel = new JPanel();
        JButton aldatuBtn = new JButton("Aldatu Lerroaren Egoera");
        aldatuBtn.addActionListener(e -> {
            int lRow = table.getSelectedRow();
            if (lRow == -1)
                return;
            int idLerroa = ((Number) table.getValueAt(lRow, 0)).intValue();
            String unekoSt = (String) table.getValueAt(lRow, 4);

            String[] opts = { "Prestatzen", "Osatua/Bidalita", "Ezabatua" };
            String berria = (String) JOptionPane.showInputDialog(dialog,
                    "Aukeratu egoera:", "Egoera Aldatu",
                    JOptionPane.QUESTION_MESSAGE, null, opts, unekoSt);

            if (berria != null) {
                try {
                    langilea.produktuEskaeraLerroEgoeraAldatu(idLerroa, berria, idEskaera);
                    kargatuEskaeraLerroak(idEskaera, model); // Freskatu
                    eskaeraDatuakKargatu(); // Freskatu atzeko taula nagusia ere
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton itxiBtn = new JButton("Itxi");
        itxiBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(aldatuBtn);
        btnPanel.add(itxiBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * Eskaera baten lerroak kargatu.
     * 
     * @param idEskaera Eskaeraren IDa.
     * @param model     Taula eredua.
     */
    private void kargatuEskaeraLerroak(int idEskaera, DefaultTableModel model) {
        model.setRowCount(0);
        try {
            java.util.List<Object[]> lerroak = langilea.produktuEskaeraLerroakIkusi(idEskaera);
            for (Object[] o : lerroak) {
                model.addRow(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Produktu baten oharra editatu.
     */
    private void editatuProduktuOharra() {
        int row = produktuTaula.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu produktu bat lehenbizi.");
            return;
        }

        int idProduktua = ((Number) produktuTaula.getValueAt(row, 0)).intValue();
        // Assuming 'Oharra' is the last column (index 7 based on new SQL)
        String unekoOharra = "";
        Object val = produktuTaula.getValueAt(row, 7);
        if (val != null)
            unekoOharra = val.toString();

        String oharBerria = JOptionPane.showInputDialog(this, "Idatzi oharra:", unekoOharra);
        if (oharBerria != null) {
            try {
                langilea.produktuEgoeraOharraJarri(idProduktua, oharBerria);
                produktuDatuakKargatu();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Errorea oharra gordetzean: " + e.getMessage());
            }
        }
    }

    /**
     * Biltegi berria sortu.
     */
    private void sortuBiltegia() {
        JTextField izenaEremua = new JTextField();
        JTextField skuEremua = new JTextField();
        Object[] mezua = { "Biltegi Izena:", izenaEremua, "SKU Kodea:", skuEremua };
        int aukera = JOptionPane.showConfirmDialog(this, mezua, "Biltegi Berria", JOptionPane.OK_CANCEL_OPTION);
        if (aukera == JOptionPane.OK_OPTION) {
            try {
                langilea.biltegiaSortu(izenaEremua.getText(), skuEremua.getText());
                biltegiDatuakKargatu();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
            }
        }
    }

    /**
     * Biltegi bat ezabatu.
     */
    private void ezabatuBiltegia() {
        int row = biltegiTaula.getSelectedRow();
        if (row == -1)
            return;
        int modelRow = biltegiTaula.convertRowIndexToModel(row);
        Object val = biltegiTaula.getModel().getValueAt(modelRow, 0);
        int id = (val instanceof Number) ? ((Number) val).intValue() : Integer.parseInt(val.toString());
        try {
            if (JOptionPane.showConfirmDialog(this, "Ziur ezabatu nahi duzula?", "Ezabatu",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                langilea.biltegiaEzabatu(id);
                biltegiDatuakKargatu();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errorea", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Biltegi baten datuak (izena, SKU) aldatu.
     */
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
            try {
                langilea.biltegiaEditatu(id, izenaEremua.getText(), skuEremua.getText());
                biltegiDatuakKargatu();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Produktu bat biltegi batetik bestera mugitu.
     */
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
            try {
                langilea.produktuarenBiltegiaAldatu(idProd, ((BiltegiElementua) hautatzailea.getSelectedItem()).id);
                produktuDatuakKargatu();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Produktu baten lerroa "Jasota" markatu.
     */
    private void markatuProduktuaJasota() {
        produktuEgoeraAldatu("Jasota");
    }

    /**
     * Produktu baten lerroa "Bidean" markatu.
     */
    private void markatuProduktuaBidean() {
        produktuEgoeraAldatu("Bidean");
    }

    /**
     * Produktu sarrera baten lerro egoera aldatu (Helper).
     * 
     * @param egoeraBerria Egoera berria.
     */
    private void produktuEgoeraAldatu(String egoeraBerria) {
        int row = produktuTaula.getSelectedRow();
        if (row == -1)
            return;
        int modelRow = produktuTaula.convertRowIndexToModel(row);
        Object valSarrera = produktuTaula.getModel().getValueAt(modelRow, 3);
        Object valLerroa = produktuTaula.getModel().getValueAt(modelRow, 6);
        int idSarrera = Integer.parseInt(valSarrera.toString());
        int idLerroa = Integer.parseInt(valLerroa.toString());
        try {
            langilea.produktuSarreraEgoeraAldatu(idLerroa, egoeraBerria, idSarrera);
            produktuDatuakKargatu();
            sarreraDatuakKargatu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saioa itxi.
     */
    private void saioaItxi() {
        if (JOptionPane.showConfirmDialog(this, "Ziur zaude saioa itxi nahi duzula?", "Logout",
                JOptionPane.YES_NO_OPTION) == 0) {
            dispose();
            new SaioaHastekoPanela().setVisible(true);
        }
    }

    // --- HELPER CLASSES ---
    /**
     * Biltegi objektua ComboBox-erako.
     */
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

    /**
     * Norberaren fitxaketen fitxa sortu.
     */
    private void nireFitxaketaTabSortu() {
        JPanel nireFitxaketaPanela = new JPanel(new BorderLayout());
        nireFitxaketaPanela.setOpaque(false);
        nireFitxaketaTaula = new JTable();
        nireFitxaketaPanela.add(new JScrollPane(nireFitxaketaTaula), BorderLayout.CENTER);
        pestainaPanela.addTab("Nire Fitxaketak", null, nireFitxaketaPanela, null);
    }

    /**
     * Norberaren fitxaketen datuak kargatu.
     */
    private void nireFitxaketaDatuakKargatu() {
        String sql = "SELECT data, CAST(ordua AS CHAR) AS ordua, mota FROM fitxaketak WHERE langilea_id = ? ORDER BY id_fitxaketa DESC";
        try (Connection con = DB_Konexioa.konektatu(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, langilea.getIdLangilea());
            DefaultTableModel eredua = TaulaModelatzailea.ereduaEraiki(pst.executeQuery());
            nireFitxaketaTaula.setModel(eredua);
            nireFitxaketaOrdenatzailea = new TableRowSorter<>(eredua);
            nireFitxaketaTaula.setRowSorter(nireFitxaketaOrdenatzailea);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hornitzaile objektua ComboBox-erako.
     */
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

    /**
     * Kategoria objektua ComboBox-erako.
     */
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

    /**
     * Atzealdeko panela irudiarekin.
     */
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
