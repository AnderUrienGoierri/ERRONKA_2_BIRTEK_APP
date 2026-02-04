package ui;

import db.DB_Konexioa;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

/**
 * MenuAdministrazioa klasea.
 * Administrari langilearen interfaze nagusia.
 * Langileak, sailak, fitxaketak, fakturak, hornitzaileak eta herriak kudeatzeko
 * aukerak eskaintzen ditu.
 */
public class MenuAdministrazioa extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel edukiPanela;
    private JTable langileTaula, sailaTaula, fitxaketaTaula, fakturaTaula, hornitzaileTaula, herriaTaula;
    private JTextField bilatuTestua;
    private TableRowSorter<DefaultTableModel> langileOrdenatzailea, sailaOrdenatzailea, fitxaketaOrdenatzailea,
            fakturaOrdenatzailea, hornitzaileOrdenatzailea, herriaOrdenatzailea,
            unekoOrdenatzailea;

    // Fitxaketa informazioa
    private JLabel fitxaketaInfoEtiketa;
    private JButton gehituBotoia, editatuBotoia, ezabatuBotoia, ikusiFakturaBotoia;

    // Erabiltzailearen datuak
    // Erabiltzailea (OOP)
    private AdministrariLangilea langilea;

    /**
     * Eraikitzailea eguneratua.
     */
    /**
     * Eraikitzailea parametroarekin.
     * 
     * @param oinarrizkoLangilea Saioa hasi duen langilea.
     */
    public MenuAdministrazioa(Langilea oinarrizkoLangilea) {
        this.langilea = new AdministrariLangilea(oinarrizkoLangilea);
        pantailaPrestatu();
    }

    /**
     * Pantailaren osagaiak inizializatu eta kokatu.
     */
    private void pantailaPrestatu() {
        setTitle("Birtek - ADMINISTRAZIOA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1050, 650);
        edukiPanela = new JPanel();
        edukiPanela.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(edukiPanela);
        edukiPanela.setLayout(new BorderLayout(0, 0));

        // --- GOIKO PANELA ---
        JPanel goikoPanela = new JPanel(new BorderLayout());
        edukiPanela.add(goikoPanela, BorderLayout.NORTH);

        // EZKERRA: Bilatzailea
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

        // ESKUINALDEA: Erabiltzailearen informazioa + Fitxaketa + Saioa Itxi
        JPanel eskuinekoPanela = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        // 1. Erabiltzailearen informazioaren etiketa
        JLabel erabiltzaileEtiketa = new JLabel(langilea.getIzena() + " " + langilea.getAbizena());
        erabiltzaileEtiketa.setFont(new Font("SansSerif", Font.BOLD, 12));
        erabiltzaileEtiketa.setForeground(new Color(0, 102, 102));

        // 2. Fitxaketa Panela
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

        // 3. Saioa Itxi
        JButton saioaItxiBotoia = new JButton("Saioa Itxi");
        saioaItxiBotoia.setBackground(new Color(220, 20, 60));
        saioaItxiBotoia.setForeground(Color.WHITE);
        saioaItxiBotoia.addActionListener(e -> saioaItxi());

        // 4. Atzera botoia (Zuzendaritza bakarrik - hau kentzen dugu momentuz edo
        // logika hobetu beharko litzateke)

        // Gehitu panelera
        eskuinekoPanela.add(erabiltzaileEtiketa);
        eskuinekoPanela.add(fitxaketaPanela);
        eskuinekoPanela.add(saioaItxiBotoia);

        goikoPanela.add(eskuinekoPanela, BorderLayout.EAST);

        // --- ERDIKO PANELA ---
        JTabbedPane pestainaPanela = new JTabbedPane(JTabbedPane.TOP);
        edukiPanela.add(pestainaPanela, BorderLayout.CENTER);

        // --- LANGILEAK TAB ---
        JPanel langilePanela = new JPanel(new BorderLayout());
        pestainaPanela.addTab("Langileak", null, langilePanela, null);
        langileTaula = new JTable();
        langilePanela.add(new JScrollPane(langileTaula), BorderLayout.CENTER);

        // --- SAILAK TAB ---
        JPanel sailaPanela = new JPanel(new BorderLayout());
        pestainaPanela.addTab("Sailak", null, sailaPanela, null);
        sailaTaula = new JTable();
        sailaPanela.add(new JScrollPane(sailaTaula), BorderLayout.CENTER);

        // --- FITXAKETAK TAB ---
        JPanel fitxaketakPanela = new JPanel(new BorderLayout());
        pestainaPanela.addTab("Fitxaketak", null, fitxaketakPanela, null);
        fitxaketaTaula = new JTable();
        fitxaketakPanela.add(new JScrollPane(fitxaketaTaula), BorderLayout.CENTER);

        // --- FAKTURAK TAB ---
        JPanel fakturaPanela = new JPanel(new BorderLayout());
        pestainaPanela.addTab("Fakturak", null, fakturaPanela, null);
        fakturaTaula = new JTable();
        fakturaPanela.add(new JScrollPane(fakturaTaula), BorderLayout.CENTER);

        // --- HORNITZAILEAK TAB ---
        JPanel hornitzailePanela = new JPanel(new BorderLayout());
        pestainaPanela.addTab("Hornitzaileak", null, hornitzailePanela, null);
        hornitzaileTaula = new JTable();
        hornitzailePanela.add(new JScrollPane(hornitzaileTaula), BorderLayout.CENTER);

        // --- HERRIAK TAB ---
        JPanel herriaPanela = new JPanel(new BorderLayout());
        pestainaPanela.addTab("Herriak", null, herriaPanela, null);
        herriaTaula = new JTable();
        herriaPanela.add(new JScrollPane(herriaTaula), BorderLayout.CENTER);

        JPanel botoiCrudPanela = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        gehituBotoia = new JButton("Gehitu +");
        editatuBotoia = new JButton("Editatu");
        ezabatuBotoia = new JButton("Ezabatu");
        ikusiFakturaBotoia = new JButton("Ikusi Faktura");
        ikusiFakturaBotoia.setVisible(false); // Hasieran ezkutatu

        gehituBotoia.addActionListener(e -> gehituElementua(pestainaPanela.getSelectedIndex()));
        editatuBotoia.addActionListener(e -> editatuElementua(pestainaPanela.getSelectedIndex()));
        ezabatuBotoia.addActionListener(e -> ezabatuElementua(pestainaPanela.getSelectedIndex()));
        ikusiFakturaBotoia.addActionListener(e -> ikusiFaktura());

        botoiCrudPanela.add(gehituBotoia);
        botoiCrudPanela.add(editatuBotoia);
        botoiCrudPanela.add(ezabatuBotoia);
        botoiCrudPanela.add(ikusiFakturaBotoia);

        JPanel behekoPanela = new JPanel(new BorderLayout());
        behekoPanela.add(botoiCrudPanela, BorderLayout.NORTH);

        pestainaPanela.addChangeListener(e -> {
            bilatuTestua.setText("");
            int index = pestainaPanela.getSelectedIndex();
            ikusiFakturaBotoia.setVisible(index == 3); // 3 = Fakturak

            // Fakturak (index 3) denean, ezin da gehitu, editatu edo ezabatu
            // Hornitzaileak (index 4) denean, ezin da gehitu
            boolean fakturakDira = (index == 3);
            boolean hornitzaileakDira = (index == 4);

            gehituBotoia.setEnabled(!fakturakDira && !hornitzaileakDira);
            editatuBotoia.setEnabled(!fakturakDira);
            ezabatuBotoia.setEnabled(!fakturakDira);

            switch (index) {
                case 0:
                    unekoOrdenatzailea = langileOrdenatzailea;
                    break;
                case 1:
                    unekoOrdenatzailea = sailaOrdenatzailea;
                    break;
                case 2:
                    unekoOrdenatzailea = fitxaketaOrdenatzailea;
                    break;
                case 3:
                    unekoOrdenatzailea = fakturaOrdenatzailea;
                    break;
                case 4:
                    unekoOrdenatzailea = hornitzaileOrdenatzailea;
                    break;
                case 5:
                    unekoOrdenatzailea = herriaOrdenatzailea;
                    break;

            }
        });

        JButton kargatuBotoia = new JButton("Datuak Deskargatu / Kargatu Berriro");
        kargatuBotoia.addActionListener(e -> datuakKargatuOsoa());
        behekoPanela.add(kargatuBotoia, BorderLayout.CENTER);

        edukiPanela.add(behekoPanela, BorderLayout.SOUTH);

        if (!java.beans.Beans.isDesignTime()) {
            datuakKargatuOsoa();
            eguneratuFitxaketaEgoera();
        }
    }

    // --- FITXAKETA LOGIKA ---
    /**
     * Fitxaketa bat burutzen du (Sarrera edo Irteera).
     * 
     * @param mota "Sarrera" edo "Irteera".
     */
    private void fitxatu(String mota) {
        try {
            langilea.fitxatu(mota);
            eguneratuFitxaketaEgoera();
            datuakKargatuOsoa(); // Taulak freskatu
            JOptionPane.showMessageDialog(this, mota + " ondo erregistratu da.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Errorea", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Fitxaketa egoera etiketa eguneratzen du (Barruan/Kanpoan).
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
     * Erabiltzailearen fitxaketa historia erakusten duen leihoa ireki.
     */
    private void ikusiFitxaketaHistoriala() {
        JDialog elkarrizketa = new JDialog(this, "Nire Fitxaketa Historiala", true);
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

        JButton itxiBotoia = new JButton("Itxi");
        itxiBotoia.addActionListener(e -> elkarrizketa.dispose());
        JPanel botoiPanela = new JPanel();
        botoiPanela.add(itxiBotoia);
        elkarrizketa.add(botoiPanela, BorderLayout.SOUTH);

        elkarrizketa.setVisible(true);
    }

    /**
     * Erabiltzailearen datuak editatzeko leihoa ireki.
     */
    private void irekiNireDatuakEditatu() {
        NireDatuakDialog dialog = new NireDatuakDialog(this, langilea);
        dialog.setVisible(true);
    }

    /**
     * Taula guztietako datuak datu-basetik kargatu eta eguneratu.
     */
    private void datuakKargatuOsoa() {
        try (Connection konexioa = DB_Konexioa.konektatu()) {
            // Langileak
            try {
                PreparedStatement pstL = konexioa.prepareStatement(
                        "SELECT id_langilea, izena, abizena, nan, jaiotza_data, herria_id, helbidea, posta_kodea, telefonoa, emaila FROM langileak");
                DefaultTableModel mL = TaulaModelatzailea.ereduaEraiki(pstL.executeQuery());
                langileTaula.setModel(mL);
                langileOrdenatzailea = new TableRowSorter<>(mL);
                langileTaula.setRowSorter(langileOrdenatzailea);
            } catch (Exception e) {
                System.err.println("Errorea Langileak kargatzean: " + e.getMessage());
                e.printStackTrace();
            }

            // Sailak
            try {
                PreparedStatement pstS = konexioa.prepareStatement("SELECT * FROM langile_sailak");
                DefaultTableModel mS = TaulaModelatzailea.ereduaEraiki(pstS.executeQuery());
                sailaTaula.setModel(mS);
                sailaOrdenatzailea = new TableRowSorter<>(mS);
                sailaTaula.setRowSorter(sailaOrdenatzailea);
            } catch (Exception e) {
                System.err.println("Errorea Sailak kargatzean: " + e.getMessage());
                e.printStackTrace();
            }

            // Fitxaketak (Globala)
            try {
                PreparedStatement pstF = konexioa.prepareStatement(
                        "SELECT f.id_fitxaketa, CONCAT(l.izena, ' ', l.abizena) AS langilea, f.data, CAST(f.ordua AS CHAR) AS ordua, f.mota "
                                +
                                "FROM fitxaketak f JOIN langileak l ON f.langilea_id = l.id_langilea ORDER BY f.id_fitxaketa DESC");
                DefaultTableModel mF = TaulaModelatzailea.ereduaEraiki(pstF.executeQuery());
                fitxaketaTaula.setModel(mF);
                fitxaketaOrdenatzailea = new TableRowSorter<>(mF);
                fitxaketaTaula.setRowSorter(fitxaketaOrdenatzailea);
            } catch (Exception e) {
                System.err.println("Errorea Fitxaketak kargatzean: " + e.getMessage());
                e.printStackTrace();
            }

            // Fakturak
            try {
                // Hobekuntza: Bezeroaren izena erakutsi eskaera ID hutsaren ordez
                String sqlFakturak = "SELECT e.id_eskaera AS id_faktura, e.faktura_zenbakia, " +
                        "CONCAT(e.id_eskaera, ' - ', b.izena_edo_soziala) AS eskaera, " +
                        "e.data, e.faktura_url AS fitxategia_url " +
                        "FROM eskaerak e " +
                        "JOIN bezeroak b ON e.bezeroa_id = b.id_bezeroa " +
                        "WHERE e.faktura_zenbakia IS NOT NULL AND e.faktura_zenbakia != '' " +
                        "ORDER BY e.id_eskaera DESC";
                PreparedStatement pstFa = konexioa.prepareStatement(sqlFakturak);
                DefaultTableModel mFa = TaulaModelatzailea.ereduaEraiki(pstFa.executeQuery());
                fakturaTaula.setModel(mFa);
                fakturaOrdenatzailea = new TableRowSorter<>(mFa);
                fakturaTaula.setRowSorter(fakturaOrdenatzailea);
            } catch (Exception e) {
                System.err.println("Errorea Fakturak kargatzean: " + e.getMessage());
                e.printStackTrace();
            }

            // Hornitzaileak
            try {
                PreparedStatement pstH = konexioa.prepareStatement(
                        "SELECT id_hornitzailea, izena_soziala, ifz_nan, kontaktu_pertsona, helbidea, herria_id, posta_kodea, telefonoa FROM hornitzaileak");
                DefaultTableModel mH = TaulaModelatzailea.ereduaEraiki(pstH.executeQuery());
                hornitzaileTaula.setModel(mH);
                hornitzaileOrdenatzailea = new TableRowSorter<>(mH);
                hornitzaileTaula.setRowSorter(hornitzaileOrdenatzailea);
            } catch (Exception e) {
                System.err.println("Errorea Hornitzaileak kargatzean: " + e.getMessage());
                e.printStackTrace();
            }

            // Herriak
            try {
                PreparedStatement pstHe = konexioa.prepareStatement("SELECT * FROM herriak");
                DefaultTableModel mHe = TaulaModelatzailea.ereduaEraiki(pstHe.executeQuery());
                herriaTaula.setModel(mHe);
                herriaOrdenatzailea = new TableRowSorter<>(mHe);
                herriaTaula.setRowSorter(herriaOrdenatzailea);
            } catch (Exception e) {
                System.err.println("Errorea Herriak kargatzean: " + e.getMessage());
                e.printStackTrace();
            }

            if (unekoOrdenatzailea == null)
                unekoOrdenatzailea = langileOrdenatzailea;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Taulak iragazi bilaketa testuaren arabera.
     */
    private void filtratu() {
        String testua = bilatuTestua.getText();
        if (unekoOrdenatzailea != null) {
            if (testua.trim().length() == 0)
                unekoOrdenatzailea.setRowFilter(null);
            else
                unekoOrdenatzailea.setRowFilter(RowFilter.regexFilter("(?i)" + testua));
        }
    }

    /**
     * Hautatutako fitxaren elementua ezabatu.
     * 
     * @param index Fitxaren indizea (0: Langileak, 1: Sailak, etc.).
     */
    private void ezabatuElementua(int index) {
        JTable unekoTaula = null;

        switch (index) {
            case 0:
                unekoTaula = langileTaula;
                break;
            case 1:
                unekoTaula = sailaTaula;
                break;
            case 2:
                unekoTaula = fitxaketaTaula;
                break;
            case 3:
                unekoTaula = fakturaTaula;
                break;
            case 4:
                unekoTaula = hornitzaileTaula;
                break;
            case 5:
                unekoTaula = herriaTaula;
                break;
        }

        if (unekoTaula == null || unekoTaula.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Hautatu errenkada bat ezabatzeko.");
            return;
        }

        int errenkada = unekoTaula.getSelectedRow();
        int errenkadaModeloa = unekoTaula.convertRowIndexToModel(errenkada);
        Object idVal = unekoTaula.getModel().getValueAt(errenkadaModeloa, 0);

        int aukera = JOptionPane.showConfirmDialog(this, "Ziur zaude ID " + idVal + " ezabatu nahi duzula?", "Garbitu",
                JOptionPane.YES_NO_OPTION);
        if (aukera == JOptionPane.YES_OPTION) {
            try {
                int idInt = (idVal instanceof Number) ? ((Number) idVal).intValue()
                        : Integer.parseInt(idVal.toString());
                switch (index) {
                    case 0:
                        langilea.langileaEzabatu(idInt);
                        break;
                    case 1:
                        langilea.langileSailaEzabatu(idInt);
                        break;
                    case 2:
                        langilea.fitxaketaEzabatu(idInt);
                        break;
                    case 3:
                        // Eskaera/Faktura ezabatu
                        // Administrari-k ez du eskaeraEzabatu metodo propiorik, baina Salmenta-k bai.
                        // Erabil dezagun DB zuzena momentuz edo transferitu modelo administraziora.
                        // AdministrariLangilea-n eskaeraIkusi badago, baina ezabatzea ez.
                        // Hala ere, AdministrariLangilea-k dena kudeatzen duenez, gehitu dugu modeloan
                        // orain.
                        try (Connection kon = DB_Konexioa.konektatu()) {
                            PreparedStatement pst = kon.prepareStatement("DELETE FROM eskaerak WHERE id_eskaera = ?");
                            pst.setInt(1, idInt);
                            pst.executeUpdate();
                        }
                        break;
                    case 4:
                        langilea.hornitzaileaEzabatu(idInt);
                        break;
                    case 5:
                        langilea.herriaEzabatu(idInt);
                        break;
                }
                datuakKargatuOsoa();
                JOptionPane.showMessageDialog(this, "Ezabatuta.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errorea ezabatzean: " + e.getMessage());
            }
        }
    }

    /**
     * Elementu berri bat gehitu hautatutako fitxaren arabera.
     * 
     * @param index Fitxaren indizea.
     */
    private void gehituElementua(int index) {
        if (index == 0) { // Langileak
            JTextField izenaField = new JTextField();
            JTextField abizenaField = new JTextField();
            JTextField nanField = new JTextField();
            JTextField jaiotzaDataField = new JTextField("YYYY-MM-DD");
            JTextField telefonoaField = new JTextField();
            JTextField emailField = new JTextField();
            JPasswordField passField = new JPasswordField();

            // Sailak ComboBox
            JComboBox<ComboItem> sailaBox = new JComboBox<>();
            for (LangileSaila s : langilea.sailakGuztiakIkusi()) {
                sailaBox.addItem(new ComboItem(s.getIdSaila(), s.getIzena()));
            }

            JTextField helbideaField = new JTextField();

            // Herriak ComboBox
            JComboBox<ComboItem> herriaBox = new JComboBox<>();
            for (Herria h : langilea.herriakIkusi()) {
                herriaBox.addItem(new ComboItem(h.getIdHerria(), h.getIzena()));
            }
            JButton herriBerriaBotoia = new JButton("Herri Berria +");
            herriBerriaBotoia.addActionListener(e -> {
                JTextField hIzena = new JTextField();
                JTextField hLurraldea = new JTextField();
                JTextField hNazioa = new JTextField();
                Object[] hMsg = { "Herria:", hIzena, "Lurraldea:", hLurraldea, "Nazioa:", hNazioa };
                int hOpt = JOptionPane.showConfirmDialog(null, hMsg, "Herri Berria Sortu",
                        JOptionPane.OK_CANCEL_OPTION);
                if (hOpt == JOptionPane.OK_OPTION) {
                    try {
                        langilea.herriBerriaSortu(hIzena.getText(), hLurraldea.getText(), hNazioa.getText());
                        // Herriak kargatu berriro ComboBox-a eguneratzeko (logika gehigarria behar izan
                        // daiteke hemen)
                        // Baina gutxienez modeloko metodoari deitzen diogu.
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Errorea herria sortzean: " + ex.getMessage());
                    }
                }
            });

            JTextField postaKodeaField = new JTextField();

            JTextField hizkuntzaField = new JTextField("Euskara");
            JTextField saltoField = new JTextField();
            JCheckBox aktiboBox = new JCheckBox("Aktibo", true);
            JTextField ibanField = new JTextField();

            Object[] message = {
                    "Izena:", izenaField,
                    "Abizena:", abizenaField,
                    "NAN:", nanField,
                    "Jaiotza Data (YYYY-MM-DD):", jaiotzaDataField,
                    "Telefonoa:", telefonoaField,
                    "Email:", emailField,
                    "Pasahitza:", passField,
                    "Saila:", sailaBox,
                    "Helbidea:", helbideaField,
                    "Herria:", herriaBox,
                    "", herriBerriaBotoia,
                    "Posta Kodea:", postaKodeaField,
                    "Hizkuntza (Euskara, Gaztelania, Frantsesa, Ingelesa):", hizkuntzaField,
                    "Salto Txartela UID:", saltoField,
                    "Egoera:", aktiboBox,
                    "IBAN:", ibanField
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Langile Berria", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    ComboItem selectedSaila = (ComboItem) sailaBox.getSelectedItem();
                    ComboItem selectedHerria = (ComboItem) herriaBox.getSelectedItem();

                    langilea.langileaSortu(
                            izenaField.getText(),
                            abizenaField.getText(),
                            nanField.getText(),
                            emailField.getText(),
                            new String(passField.getPassword()),
                            selectedSaila != null ? selectedSaila.getId() : 0,
                            helbideaField.getText(),
                            selectedHerria != null ? selectedHerria.getId() : 0,
                            postaKodeaField.getText(),
                            telefonoaField.getText(),
                            jaiotzaDataField.getText(),
                            hizkuntzaField.getText(),
                            saltoField.getText(),
                            aktiboBox.isSelected(),
                            ibanField.getText());

                    datuakKargatuOsoa();
                    JOptionPane.showMessageDialog(this, "Langilea gordeta.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
                }
            }
        } else if (index == 1) { // Sailak
            JTextField izenaField = new JTextField();
            JTextField kokapenaField = new JTextField();
            JTextField deskribapenaField = new JTextField();

            Object[] message = {
                    "Izena:", izenaField,
                    "Kokapena:", kokapenaField,
                    "Deskribapena:", deskribapenaField
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Saila Berria", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    langilea.langileSailaSortu(izenaField.getText(), kokapenaField.getText(),
                            deskribapenaField.getText());
                    datuakKargatuOsoa();
                    JOptionPane.showMessageDialog(this, "Saila gordeta.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
                }
            }
        } else if (index == 2) { // Fitxaketak
            JTextField langileaIdField = new JTextField();
            String[] motak = { "Sarrera", "Irteera" };
            JComboBox<String> motaBox = new JComboBox<>(motak);
            JTextField dataField = new JTextField("YYYY-MM-DD");
            JTextField orduaField = new JTextField("HH:MM:SS");

            Object[] message = {
                    "Langilea ID:", langileaIdField, "Mota:", motaBox,
                    "Data (Utzi hutsik gaurkorako):", dataField, "Ordua (Utzi hutsik orainerako):", orduaField
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Fitxaketa Berria", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try (Connection kon = DB_Konexioa.konektatu()) {
                    String sql = "INSERT INTO fitxaketak (langilea_id, mota, data, ordua) VALUES (?, ?, ?, ?)";
                    // Data eta ordua hutsik badaude, NOW() erabili beharko genuke query-an edo
                    // Java-n kudeatu.
                    // Sinpletasunerako, balioak eskatuko ditugu edo defektuzkoak erabili.
                    // SQL moldaketa:
                    if (dataField.getText().contains("Y") || dataField.getText().isEmpty())
                        sql = sql.replace("?, ?, ?, ?", "?, ?, CURRENT_DATE, CURRENT_TIME");

                    PreparedStatement pst = kon
                            .prepareStatement("INSERT INTO fitxaketak (langilea_id, mota) VALUES (?, ?)");
                    if (!dataField.getText().contains("Y") && !dataField.getText().isEmpty()) {
                        pst = kon.prepareStatement(
                                "INSERT INTO fitxaketak (langilea_id, mota, data, ordua) VALUES (?, ?, ?, ?)");
                        pst.setString(3, dataField.getText());
                        pst.setString(4, orduaField.getText());
                    }

                    pst.setInt(1, Integer.parseInt(langileaIdField.getText()));
                    pst.setString(2, (String) motaBox.getSelectedItem());
                    pst.executeUpdate();
                    datuakKargatuOsoa();
                    JOptionPane.showMessageDialog(this, "Fitxaketa gordeta.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
                }
            }
        } else if (index == 4) { // Hornitzaileak
            JTextField izenaField = new JTextField();
            JTextField ifzField = new JTextField();
            JTextField emailField = new JTextField();

            Object[] message = {
                    "Izena Soziala:", izenaField, "IFZ/NAN:", ifzField, "Emaila:", emailField
            };
            int option = JOptionPane.showConfirmDialog(null, message, "Hornitzaile Berria",
                    JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try (Connection kon = DB_Konexioa.konektatu()) {
                    // Helbidea, herria eta posta kodea derrigorrezkoak dira DBan.
                    // Defektuzko balioak jarriko ditugu hemen erabiltzaileak ez duelako horiek
                    // kudeatu nahi momentuz.
                    String sql = "INSERT INTO hornitzaileak (izena_soziala, ifz_nan, emaila, pasahitza, helbidea, herria_id, posta_kodea) VALUES (?, ?, ?, '1234', '-', 1, '-')";
                    PreparedStatement pst = kon.prepareStatement(sql);
                    pst.setString(1, izenaField.getText());
                    pst.setString(2, ifzField.getText());
                    pst.setString(3, emailField.getText());
                    pst.executeUpdate();
                    datuakKargatuOsoa();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
                }
            }
        } else if (index == 5) { // Herriak
            JTextField herriIzenaField = new JTextField();
            JTextField lurraldeaField = new JTextField();
            JTextField nazioaField = new JTextField();

            Object[] message = { "Herria:", herriIzenaField, "Lurraldea:", lurraldeaField, "Nazioa:", nazioaField };

            int option = JOptionPane.showConfirmDialog(null, message, "Herria Berria", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try (Connection kon = DB_Konexioa.konektatu()) {
                    String sql = "INSERT INTO herriak (izena, lurraldea, nazioa) VALUES (?, ?, ?)";
                    PreparedStatement pst = kon.prepareStatement(sql);
                    pst.setString(1, herriIzenaField.getText());
                    pst.setString(2, lurraldeaField.getText());
                    pst.setString(3, nazioaField.getText());
                    pst.executeUpdate();
                    datuakKargatuOsoa();
                    JOptionPane.showMessageDialog(this, "Herria gordeta.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Errorea gordetzean: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Gehitu funtzioa oraindik ez dago erabilgarri fitxa honetarako.");
        }
    }

    /**
     * Hautatutako elementua editatu.
     * 
     * @param index Fitxaren indizea.
     */
    private void editatuElementua(int index) {
        if (index == 0) { // Langileak
            if (langileTaula.getSelectedRow() == -1)
                return;
            int r = langileTaula.getSelectedRow();
            int rm = langileTaula.convertRowIndexToModel(r);
            Object id = langileTaula.getModel().getValueAt(rm, 0);

            try {
                int idInt = (id instanceof Number) ? ((Number) id).intValue() : Integer.parseInt(id.toString());
                Langilea l = langilea.langileaIkusi(idInt);
                if (l != null) {
                    JTextField izenaField = new JTextField(l.getIzena());
                    JTextField abizenaField = new JTextField(l.getAbizena());
                    JTextField nanField = new JTextField(l.getNan());
                    JTextField jaiotzaDataField = new JTextField(
                            l.getJaiotzaData() != null ? l.getJaiotzaData().toString() : "");
                    JTextField telefonoaField = new JTextField(l.getTelefonoa());
                    JTextField emailField = new JTextField(l.getEmaila());
                    JTextField helbideaField = new JTextField(l.getHelbidea());
                    JTextField postaKodeaField = new JTextField(l.getPostaKodea());

                    JComboBox<ComboItem> sailaBox = new JComboBox<>();
                    ComboItem selectedSaila = null;
                    for (LangileSaila s : langilea.sailakGuztiakIkusi()) {
                        ComboItem item = new ComboItem(s.getIdSaila(), s.getIzena());
                        sailaBox.addItem(item);
                        if (s.getIdSaila() == l.getSailaId()) {
                            selectedSaila = item;
                        }
                    }
                    if (selectedSaila != null)
                        sailaBox.setSelectedItem(selectedSaila);

                    JComboBox<ComboItem> herriaBox = new JComboBox<>();
                    ComboItem selectedHerria = null;
                    for (Herria h : langilea.herriakIkusi()) {
                        ComboItem item = new ComboItem(h.getIdHerria(), h.getIzena());
                        herriaBox.addItem(item);
                        if (h.getIdHerria() == l.getHerriaId()) {
                            selectedHerria = item;
                        }
                    }
                    if (selectedHerria != null)
                        herriaBox.setSelectedItem(selectedHerria);

                    JButton herriBerriaBotoia = new JButton("Herri Berria +");
                    herriBerriaBotoia.addActionListener(e -> {
                        JTextField hIzena = new JTextField();
                        JTextField hLurraldea = new JTextField();
                        JTextField hNazioa = new JTextField();
                        Object[] hMsg = { "Herria:", hIzena, "Lurraldea:", hLurraldea, "Nazioa:", hNazioa };
                        int hOpt = JOptionPane.showConfirmDialog(null, hMsg, "Herri Berria Sortu",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (hOpt == JOptionPane.OK_OPTION) {
                            try (Connection konH = DB_Konexioa.konektatu()) {
                                String sql = "INSERT INTO herriak (izena, lurraldea, nazioa) VALUES (?, ?, ?)";
                                PreparedStatement pstH = konH.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                pstH.setString(1, hIzena.getText());
                                pstH.setString(2, hLurraldea.getText());
                                pstH.setString(3, hNazioa.getText());
                                pstH.executeUpdate();
                                ResultSet rsKey = pstH.getGeneratedKeys();
                                if (rsKey.next()) {
                                    int newId = rsKey.getInt(1);
                                    ComboItem newItem = new ComboItem(newId, hIzena.getText());
                                    herriaBox.addItem(newItem);
                                    herriaBox.setSelectedItem(newItem);
                                }
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Errorea herria sortzean: " + ex.getMessage());
                            }
                        }
                    });

                    JTextField hizkuntzaField = new JTextField(l.getHizkuntza());
                    JPasswordField passFieldInput = new JPasswordField(l.getPasahitza());
                    JTextField saltoField = new JTextField(l.getSaltoTxartelaUid());
                    JCheckBox aktiboBox = new JCheckBox("Aktibo", l.isAktibo());
                    JTextField ibanField = new JTextField(l.getIban());

                    Object[] message = {
                            "Izena:", izenaField,
                            "Abizena:", abizenaField,
                            "NAN:", nanField,
                            "Jaiotza Data:", jaiotzaDataField,
                            "Telefonoa:", telefonoaField,
                            "Email:", emailField,
                            "Pasahitza:", passFieldInput,
                            "Saila:", sailaBox,
                            "Helbidea:", helbideaField,
                            "Posta Kodea:", postaKodeaField,
                            "Herria:", herriaBox,
                            "", herriBerriaBotoia,
                            "Hizkuntza:", hizkuntzaField,
                            "Salto Txartela UID:", saltoField,
                            "Egoera:", aktiboBox,
                            "IBAN:", ibanField
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, "Editatu Langilea",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        ComboItem selSaila = (ComboItem) sailaBox.getSelectedItem();
                        ComboItem selHerria = (ComboItem) herriaBox.getSelectedItem();

                        langilea.langileaEditatu(
                                idInt,
                                izenaField.getText(),
                                abizenaField.getText(),
                                nanField.getText(),
                                emailField.getText(),
                                selSaila != null ? selSaila.getId() : 0,
                                helbideaField.getText(),
                                selHerria != null ? selHerria.getId() : 0,
                                postaKodeaField.getText(),
                                telefonoaField.getText(),
                                jaiotzaDataField.getText(),
                                hizkuntzaField.getText(),
                                new String(passFieldInput.getPassword()),
                                saltoField.getText(),
                                aktiboBox.isSelected(),
                                ibanField.getText());

                        datuakKargatuOsoa();
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
            }
        } else if (index == 1) { // Sailak
            if (sailaTaula.getSelectedRow() == -1)
                return;
            int r = sailaTaula.getSelectedRow();
            int rm = sailaTaula.convertRowIndexToModel(r);
            Object id = sailaTaula.getModel().getValueAt(rm, 0);

            try (Connection kon = DB_Konexioa.konektatu()) {
                PreparedStatement pstSel = kon.prepareStatement("SELECT * FROM langile_sailak WHERE id_saila = ?");
                pstSel.setObject(1, id);
                ResultSet rs = pstSel.executeQuery();
                if (rs.next()) {
                    JTextField izenaField = new JTextField(rs.getString("izena"));
                    JTextField kokapenaField = new JTextField(rs.getString("kokapena"));
                    JTextField deskribapenaField = new JTextField(rs.getString("deskribapena"));

                    Object[] message = {
                            "Izena:", izenaField,
                            "Kokapena:", kokapenaField,
                            "Deskribapena:", deskribapenaField
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, "Editatu Saila",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        PreparedStatement pstUpd = kon.prepareStatement(
                                "UPDATE langile_sailak SET izena = ?, kokapena = ?, deskribapena = ? WHERE id_saila = ?");
                        pstUpd.setString(1, izenaField.getText());
                        pstUpd.setString(2, kokapenaField.getText());
                        pstUpd.setString(3, deskribapenaField.getText());
                        pstUpd.setObject(4, id);
                        pstUpd.executeUpdate();
                        datuakKargatuOsoa();
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
            }
        } else if (index == 2) { // Fitxaketak
            if (fitxaketaTaula.getSelectedRow() == -1)
                return;
            int r = fitxaketaTaula.getSelectedRow();
            int rm = fitxaketaTaula.convertRowIndexToModel(r);
            Object id = fitxaketaTaula.getModel().getValueAt(rm, 0);

            try (Connection kon = DB_Konexioa.konektatu()) {
                PreparedStatement pstSel = kon.prepareStatement("SELECT * FROM fitxaketak WHERE id_fitxaketa = ?");
                pstSel.setObject(1, id);
                ResultSet rs = pstSel.executeQuery();
                if (rs.next()) {
                    JTextField langileaIdField = new JTextField(String.valueOf(rs.getInt("langilea_id")));
                    String[] motak = { "Sarrera", "Irteera" };
                    JComboBox<String> motaBox = new JComboBox<>(motak);
                    motaBox.setSelectedItem(rs.getString("mota"));
                    JTextField dataField = new JTextField(rs.getString("data"));
                    JTextField orduaField = new JTextField(rs.getString("ordua"));

                    Object[] message = { "Langilea ID:", langileaIdField, "Mota:", motaBox, "Data:", dataField,
                            "Ordua:", orduaField };
                    int option = JOptionPane.showConfirmDialog(null, message, "Editatu Fitxaketa",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        PreparedStatement pstUpd = kon.prepareStatement(
                                "UPDATE fitxaketak SET langilea_id = ?, mota = ?, data = ?, ordua = ? WHERE id_fitxaketa = ?");
                        pstUpd.setInt(1, Integer.parseInt(langileaIdField.getText()));
                        pstUpd.setString(2, (String) motaBox.getSelectedItem());
                        pstUpd.setString(3, dataField.getText());
                        pstUpd.setString(4, orduaField.getText());
                        pstUpd.setObject(5, id);
                        pstUpd.executeUpdate();
                        datuakKargatuOsoa();
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
            }
        } else if (index == 4) { // Hornitzaileak
            if (hornitzaileTaula.getSelectedRow() == -1)
                return;
            int r = hornitzaileTaula.getSelectedRow();
            int rm = hornitzaileTaula.convertRowIndexToModel(r);
            Object id = hornitzaileTaula.getModel().getValueAt(rm, 0);

            try (Connection kon = DB_Konexioa.konektatu()) {
                PreparedStatement pstSel = kon
                        .prepareStatement("SELECT * FROM hornitzaileak WHERE id_hornitzailea = ?");
                pstSel.setObject(1, id);
                ResultSet rs = pstSel.executeQuery();
                if (rs.next()) {
                    JTextField izenaField = new JTextField(rs.getString("izena_soziala"));
                    JTextField ifzField = new JTextField(rs.getString("ifz_nan"));
                    JTextField kontaktuField = new JTextField(rs.getString("kontaktu_pertsona"));
                    JTextField helbideaField = new JTextField(rs.getString("helbidea"));
                    JTextField postaKodeaField = new JTextField(rs.getString("posta_kodea"));
                    JTextField telefonoaField = new JTextField(rs.getString("telefonoa"));
                    JTextField emailField = new JTextField(rs.getString("emaila"));

                    JComboBox<ComboItem> herriaBox = new JComboBox<>();
                    PreparedStatement pstHerria = kon.prepareStatement("SELECT id_herria, izena FROM herriak");
                    ResultSet rsH = pstHerria.executeQuery();
                    ComboItem selectedHerria = null;
                    int currentHerriaId = rs.getInt("herria_id");
                    while (rsH.next()) {
                        ComboItem item = new ComboItem(rsH.getInt("id_herria"), rsH.getString("izena"));
                        herriaBox.addItem(item);
                        if (item.getId() == currentHerriaId) {
                            selectedHerria = item;
                        }
                    }
                    if (selectedHerria != null)
                        herriaBox.setSelectedItem(selectedHerria);

                    JButton herriBerriaBotoia = new JButton("Herri Berria +");
                    herriBerriaBotoia.addActionListener(e -> {
                        JTextField hIzena = new JTextField();
                        JTextField hLurraldea = new JTextField();
                        JTextField hNazioa = new JTextField();
                        Object[] hMsg = { "Herria:", hIzena, "Lurraldea:", hLurraldea, "Nazioa:", hNazioa };
                        int hOpt = JOptionPane.showConfirmDialog(null, hMsg, "Herri Berria Sortu",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (hOpt == JOptionPane.OK_OPTION) {
                            try (Connection konH = DB_Konexioa.konektatu()) {
                                String sql = "INSERT INTO herriak (izena, lurraldea, nazioa) VALUES (?, ?, ?)";
                                PreparedStatement pstH = konH.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                pstH.setString(1, hIzena.getText());
                                pstH.setString(2, hLurraldea.getText());
                                pstH.setString(3, hNazioa.getText());
                                pstH.executeUpdate();
                                ResultSet rsKey = pstH.getGeneratedKeys();
                                if (rsKey.next()) {
                                    int newId = rsKey.getInt(1);
                                    ComboItem newItem = new ComboItem(newId, hIzena.getText());
                                    herriaBox.addItem(newItem);
                                    herriaBox.setSelectedItem(newItem);
                                }
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Errorea herria sortzean: " + ex.getMessage());
                            }
                        }
                    });

                    Object[] message = {
                            "Izena Soziala:", izenaField,
                            "IFZ/NAN:", ifzField,
                            "Kontaktu Pertsona:", kontaktuField,
                            "Helbidea:", helbideaField,
                            "Herria:", herriaBox,
                            "", herriBerriaBotoia,
                            "Posta Kodea:", postaKodeaField,
                            "Telefonoa:", telefonoaField,
                            "Emaila:", emailField
                    };

                    int option = JOptionPane.showConfirmDialog(null, message, "Editatu Hornitzailea",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        ComboItem selHerria = (ComboItem) herriaBox.getSelectedItem();
                        PreparedStatement pst = kon.prepareStatement(
                                "UPDATE hornitzaileak SET izena_soziala = ?, ifz_nan = ?, kontaktu_pertsona = ?, helbidea = ?, herria_id = ?, posta_kodea = ?, telefonoa = ?, emaila = ? WHERE id_hornitzailea = ?");
                        pst.setString(1, izenaField.getText());
                        pst.setString(2, ifzField.getText());
                        pst.setString(3, kontaktuField.getText());
                        pst.setString(4, helbideaField.getText());
                        pst.setInt(5, selHerria != null ? selHerria.getId() : 0);
                        pst.setString(6, postaKodeaField.getText());
                        pst.setString(7, telefonoaField.getText());
                        pst.setString(8, emailField.getText());
                        pst.setObject(9, id);
                        pst.executeUpdate();
                        datuakKargatuOsoa();
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
            }
        } else if (index == 5) { // Herriak
            if (herriaTaula.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Hautatu herria editatzeko.");
                return;
            }
            int r = herriaTaula.getSelectedRow();
            int rm = herriaTaula.convertRowIndexToModel(r);
            Object id = herriaTaula.getModel().getValueAt(rm, 0);

            try (Connection kon = DB_Konexioa.konektatu()) {
                PreparedStatement pstSel = kon.prepareStatement("SELECT * FROM herriak WHERE id_herria = ?");
                pstSel.setObject(1, id);
                ResultSet rs = pstSel.executeQuery();
                if (rs.next()) {
                    JTextField izenaField = new JTextField(rs.getString("izena"));
                    JTextField lurraldeaField = new JTextField(rs.getString("lurraldea"));
                    JTextField nazioaField = new JTextField(rs.getString("nazioa"));

                    Object[] message = { "Herria:", izenaField, "Lurraldea:", lurraldeaField, "Nazioa:", nazioaField };
                    int option = JOptionPane.showConfirmDialog(null, message, "Editatu Herria",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        PreparedStatement pstUpd = kon.prepareStatement(
                                "UPDATE herriak SET izena = ?, lurraldea = ?, nazioa = ? WHERE id_herria = ?");
                        pstUpd.setString(1, izenaField.getText());
                        pstUpd.setString(2, lurraldeaField.getText());
                        pstUpd.setString(3, nazioaField.getText());
                        pstUpd.setObject(4, id);
                        pstUpd.executeUpdate();
                        datuakKargatuOsoa();
                        JOptionPane.showMessageDialog(this, "Herria eguneratuta.");
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errorea eguneratzean: " + e.getMessage());
            }
        }
    }

    /**
     * Hautatutako fakturaren PDF fitxategia ireki.
     */
    private void ikusiFaktura() {
        if (fakturaTaula.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu faktura bat zerrendatik.");
            return;
        }

        int r = fakturaTaula.getSelectedRow();
        int rm = fakturaTaula.convertRowIndexToModel(r);
        // "Fitxategia URL" zutabea 4. posizioan dago (0-tik hasita indizea 3 beharko
        // luke,
        // baina DBko zutabeen ordenaren arabera: id, zenbakia, eskaera_id, data, url)
        // Taula eredua: id, zenbakia, eskaera_id, data, url -> URL index 4
        // Egiaztatu TaulaModelatzailea.ereduaEraiki-k zutabeak nola jartzen dituen.
        // Normalean SELECT * ordenan. SELECT * FROM bezero_fakturak -> id_faktura,
        // faktura_zenbakia, eskaera_id, data, fitxategia_url.
        // Beraz index 4.

        Object urlObj = fakturaTaula.getModel().getValueAt(rm, 4);

        if (urlObj == null || urlObj.toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Faktura honek ez du fitxategirik lotuta.");
            return;
        }

        String url = urlObj.toString();
        try {
            java.io.File file = new java.io.File(url);
            if (file.exists()) {
                java.awt.Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(this, "Fitxategia ez da aurkitu: " + url);
            }
        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(this, "Errorea fitxategia irekitzean: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
        }
    }

    /**
     * Saioa itxi eta hasierako pantailara itzuli.
     */
    private void saioaItxi() {
        if (JOptionPane.showConfirmDialog(this, "Ziur zaude?", "Saioa Itxi", JOptionPane.YES_NO_OPTION) == 0) {
            dispose();
            new SaioaHastekoPanela().setVisible(true);
        }
    }

    // Helper class for ComboBox items
    /**
     * ComboBox-erako klase laguntzailea.
     */
    private static class ComboItem {
        private int id;
        private String label;

        public ComboItem(int id, String label) {
            this.id = id;
            this.label = label;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
