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

/**
 * MenuSalmentak klasea.
 * Salmenta langilearen interfaze nagusia.
 * Bezeroak, eskaerak eta produktuak kudeatzeko aukerak eskaintzen ditu.
 */
public class MenuSalmentak extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable bezeroTaula, eskaeraTaula, eskaeraLerroTaula, produktuTaula;
    private JTextField bilatuTestua;
    private JComboBox<String> kategoriaFiltroa, motaFiltroa;
    private TableRowSorter<DefaultTableModel> bezeroOrdenatzailea, eskaeraOrdenatzailea, produktuOrdenatzailea,
            unekoOrdenatzailea;

    // Fitxaketa
    private JLabel fitxaketaInfoEtiketa;

    // Erabiltzailearen datuak
    // Erabiltzailea (OOP)
    private SalmentaLangilea langilea;

    /**
     * Eraikitzailea eguneratua.
     */
    /**
     * MenuSalmentak eraikitzailea.
     * 
     * @param oinarrizkoLangilea Saioa hasi duen langilea.
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

        // ESKUINALDEA: Erabiltzailea + Fitxaketa + Saioa Itxi
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

        pestainaPanelaRef = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(pestainaPanelaRef, BorderLayout.CENTER);

        JTabbedPane pestainaPanela = pestainaPanelaRef;

        // BEZEROAK PANELA
        JPanel bezeroPanela = new JPanel(new BorderLayout());
        bezeroTaula = new JTable();
        bezeroPanela.add(new JScrollPane(bezeroTaula), BorderLayout.CENTER);

        JPanel bezeroBotoiPanela = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton bezeroGehituBtn = new JButton("Gehitu");
        JButton bezeroEditatuBtn = new JButton("Editatu");
        JButton bezeroEzabatuBtn = new JButton("Ezabatu");
        JButton bezeroIkusiBtn = new JButton("Ikusi");
        JButton bezeroHistorialaBtn = new JButton("Eskaera Historiala Ikusi");

        bezeroGehituBtn.addActionListener(e -> bezeroaGehitu());
        bezeroEditatuBtn.addActionListener(e -> bezeroaEditatu());
        bezeroEzabatuBtn.addActionListener(e -> bezeroaEzabatu());
        bezeroIkusiBtn.addActionListener(e -> bezeroaIkusi());
        bezeroHistorialaBtn.addActionListener(e -> bezeroHistorialaIkusi());

        bezeroBotoiPanela.add(bezeroGehituBtn);
        bezeroBotoiPanela.add(bezeroEditatuBtn);
        bezeroBotoiPanela.add(bezeroEzabatuBtn);
        bezeroBotoiPanela.add(bezeroIkusiBtn);
        bezeroBotoiPanela.add(bezeroHistorialaBtn);

        bezeroPanela.add(bezeroBotoiPanela, BorderLayout.SOUTH);

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
        JButton eskaeraFakturaEzabatuBotoia = new JButton("Faktura Ezabatu");
        JButton eskaeraFakturaGuztiakBotoia = new JButton("Faktura Guztiak sortu");

        eskaeraGehituBotoia.addActionListener(e -> eskaeraGehitu());
        eskaeraEditatuBotoia.addActionListener(e -> eskaeraEditatu());
        eskaeraEzabatuBotoia.addActionListener(e -> eskaeraEzabatu());
        eskaeraFakturaBotoia.addActionListener(e -> fakturaSortu());
        eskaeraFakturaEzabatuBotoia.addActionListener(e -> fakturaEzabatu());
        eskaeraFakturaGuztiakBotoia.addActionListener(e -> fakturaGuztiakSortu());

        JButton eskaeraGarbituBotoia = new JButton("Filtroa Garbitu");
        eskaeraGarbituBotoia.addActionListener(e -> garbituEskaeraFiltroa());

        eskaeraBotoiPanela.add(eskaeraGehituBotoia);
        eskaeraBotoiPanela.add(eskaeraEditatuBotoia);
        eskaeraBotoiPanela.add(eskaeraEzabatuBotoia);
        eskaeraBotoiPanela.add(eskaeraFakturaBotoia);
        eskaeraBotoiPanela.add(eskaeraFakturaEzabatuBotoia);
        eskaeraBotoiPanela.add(eskaeraFakturaGuztiakBotoia);
        eskaeraBotoiPanela.add(eskaeraGarbituBotoia);

        eskaeraPanela.add(eskaeraBotoiPanela, BorderLayout.SOUTH);

        // PRODUKTUAK PANELA
        JPanel produktuPanela = new JPanel(new BorderLayout());
        produktuTaula = new JTable();
        produktuPanela.add(new JScrollPane(produktuTaula), BorderLayout.CENTER);

        JPanel produktuKontrolPanela = new JPanel(new BorderLayout());
        JPanel produktuFiltroPanela = new JPanel(new FlowLayout(FlowLayout.LEFT));
        kategoriaFiltroa = new JComboBox<>();
        kategoriaFiltroa.addItem("Kategoria Denak");
        motaFiltroa = new JComboBox<>();
        motaFiltroa.addItem("Mota Denak");
        // Motak gehitu (Enum-etik datoz DBan)
        String[] motak = { "Eramangarria", "Mahai-gainekoa", "Mugikorra", "Tableta", "Zerbitzaria", "Pantaila",
                "Softwarea" };
        for (String m : motak)
            motaFiltroa.addItem(m);

        kategoriaFiltroa.addActionListener(e -> filtratu());
        motaFiltroa.addActionListener(e -> filtratu());

        produktuFiltroPanela.add(new JLabel("Kategoria:"));
        produktuFiltroPanela.add(kategoriaFiltroa);
        produktuFiltroPanela.add(new JLabel("Mota:"));
        produktuFiltroPanela.add(motaFiltroa);
        produktuKontrolPanela.add(produktuFiltroPanela, BorderLayout.NORTH);

        JPanel produktuBotoiPanela = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prezioaBtn = new JButton("Prezioa Eguneratu");
        JButton salgaiBtn = new JButton("Salgai/Ez");
        JButton eskaintzaBtn = new JButton("Eskaintza Gehitu");

        prezioaBtn.addActionListener(e -> produktuPrezioaAldatu());
        salgaiBtn.addActionListener(e -> produktuSalgaiToggle());
        eskaintzaBtn.addActionListener(e -> produktuEskaintzaJarri());

        produktuBotoiPanela.add(prezioaBtn);
        produktuBotoiPanela.add(salgaiBtn);
        produktuBotoiPanela.add(eskaintzaBtn);
        produktuKontrolPanela.add(produktuBotoiPanela, BorderLayout.SOUTH);

        produktuPanela.add(produktuKontrolPanela, BorderLayout.SOUTH);
        pestainaPanela.addTab("Produktuak", produktuPanela);

        pestainaPanela.addChangeListener(e -> {
            bilatuTestua.setText("");
            int idx = pestainaPanela.getSelectedIndex();
            if (idx == 0)
                unekoOrdenatzailea = bezeroOrdenatzailea;
            else if (idx == 1)
                unekoOrdenatzailea = eskaeraOrdenatzailea;
            else if (idx == 2)
                unekoOrdenatzailea = produktuOrdenatzailea;
        });

        if (!java.beans.Beans.isDesignTime()) {
            datuakKargatu();
            kargatuKategoriak();
            eguneratuFitxaketaEgoera();
        }
    }

    // --- FITXAKETA METODO BERRIAK ---
    /**
     * Fitxaketa bat egin (Sarrera/Irteera).
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
     * Fitxaketa egoera eguneratu interfazeko etiketan.
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
     * Fitxaketa historialaren leihoa ireki.
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

        java.util.List<Fitxaketa> zerrenda = langilea.nireFitxaketakIkusi();
        for (Fitxaketa f : zerrenda) {
            eredua.addRow(new Object[] { f.getMota(), f.getData(), f.getOrdua() });
        }

        elkarrizketa.setVisible(true);
    }

    /**
     * Norberaren datuak editatzeko leihoa ireki.
     */
    private void irekiNireDatuakEditatu() {
        NireDatuakDialog dialog = new NireDatuakDialog(this, langilea);
        dialog.setVisible(true);
    }

    /**
     * Hautatutako eskaeraren lerroak beheko taulan kargatu.
     */
    private void fokatutakoEskaeraKargatu() {
        int aukeratutakoLerroa = eskaeraTaula.getSelectedRow();
        if (aukeratutakoLerroa != -1) {
            aukeratutakoLerroa = eskaeraTaula.convertRowIndexToModel(aukeratutakoLerroa);
            Object idObj = eskaeraTaula.getModel().getValueAt(aukeratutakoLerroa, 0);
            int idEskaera = (idObj instanceof Number) ? ((Number) idObj).intValue()
                    : Integer.parseInt(idObj.toString());

            try {
                java.util.List<EskaeraLerroa> lerroak = langilea.eskaeraLerroakIkusi(idEskaera);
                String[] zutabeak = { "Produktua", "Kantitatea", "P.Unit.", "Guztira", "Egoera" };
                DefaultTableModel model = new DefaultTableModel(zutabeak, 0);

                for (EskaeraLerroa l : lerroak) {
                    Produktua p = langilea.produktuaIkusi(l.getProduktuaId());
                    String pIzena = (p != null) ? p.getIzena() : "ID: " + l.getProduktuaId();
                    model.addRow(new Object[] {
                            pIzena,
                            l.getKantitatea(),
                            l.getUnitatePrezioa(),
                            l.getUnitatePrezioa().multiply(new java.math.BigDecimal(l.getKantitatea())),
                            l.getEskaeraLerroEgoera()
                    });
                }
                eskaeraLerroTaula.setModel(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            eskaeraLerroTaula.setModel(new DefaultTableModel());
        }
    }

    /**
     * Taulak iragazi bilaketa testuaren eta filtroen arabera.
     */
    private void filtratu() {
        int pestainaIdx = pestainaPanelaRef.getSelectedIndex();

        if (pestainaIdx == 2) { // Produktuak
            if (produktuOrdenatzailea != null) {
                java.util.List<RowFilter<Object, Object>> filters = new java.util.ArrayList<>();

                // Testu iragazkia
                String t = bilatuTestua.getText();
                if (!t.isEmpty()) {
                    filters.add(RowFilter.regexFilter("(?i)" + t));
                }

                // Kategoria iragazkia
                String kat = (String) kategoriaFiltroa.getSelectedItem();
                if (kat != null && !"Kategoria Denak".equals(kat)) {
                    filters.add(RowFilter.regexFilter("^" + kat + "$", 1)); // 1 = Kategoria zutabea
                }

                // Mota iragazkia
                String mota = (String) motaFiltroa.getSelectedItem();
                if (mota != null && !"Mota Denak".equals(mota)) {
                    filters.add(RowFilter.regexFilter("^" + mota + "$", 4)); // 4 = Mota zutabea
                }

                produktuOrdenatzailea.setRowFilter(RowFilter.andFilter(filters));
            }
        } else { // Bezeroak edo Eskaerak
            String t = bilatuTestua.getText();
            if (unekoOrdenatzailea != null) {
                if (t.isEmpty())
                    unekoOrdenatzailea.setRowFilter(null);
                else
                    unekoOrdenatzailea.setRowFilter(RowFilter.regexFilter("(?i)" + t));
            }
        }
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

    /**
     * Datu guztiak (Bezeroak, Eskaerak, Produktuak) datu-basetik kargatu.
     */
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

            produktuDatuakKargatu();

            if (unekoOrdenatzailea == null)
                unekoOrdenatzailea = bezeroOrdenatzailea;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Eskaera berria sortzeko prozesua (Dialogo bidez).
     */
    private void eskaeraGehitu() {
        EskaeraDialog dialog = new EskaeraDialog(this, "Gehitu Eskaera", null, "Prestatzen");
        dialog.setVisible(true);
        if (dialog.isOnartua()) {
            try {
                // Eskaera objektua prestatu
                Eskaera e = new Eskaera(0, dialog.getBezeroaId(), langilea.getIdLangilea(), null, null,
                        dialog.getPrezioTotala(), null, null, dialog.getEgoera());

                // Lerroak prestatu
                java.util.List<EskaeraLerroa> lerroak = new java.util.ArrayList<>();
                for (Object[] obj : dialog.getLerroak()) {
                    lerroak.add(new EskaeraLerroa(0, 0, (int) obj[0], (int) obj[1], (java.math.BigDecimal) obj[2],
                            dialog.getEgoera()));
                }

                // Modeloko metodoari deitu
                int idEskaera = langilea.eskaeraOsoaSortu(e, lerroak);

                // Faktura automatikoa sortu
                if ("Osatua/Bidalita".equalsIgnoreCase(dialog.getEgoera()) && idEskaera != -1) {
                    try {
                        langilea.fakturaSortu(idEskaera);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                datuakKargatu();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errorea eskaera sortzean: " + ex.getMessage());
            }
        }
    }

    /**
     * Hautatutako eskaera editatu.
     */
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

        String egoera = (String) model.getValueAt(aukeratutakoLerroa, 8);

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
            try {
                // Eskaera objektua prestatu
                Eskaera e = new Eskaera(idEskaera, dialog.getBezeroaId(), langilea.getIdLangilea(), null, null,
                        dialog.getPrezioTotala(), null, null, dialog.getEgoera());

                // Lerroak prestatu
                java.util.List<EskaeraLerroa> lerroak = new java.util.ArrayList<>();
                for (Object[] obj : dialog.getLerroak()) {
                    lerroak.add(new EskaeraLerroa(0, idEskaera, (int) obj[0], (int) obj[1],
                            (java.math.BigDecimal) obj[2], dialog.getEgoera()));
                }

                // Modeloko metodoari deitu
                langilea.eskaeraOsoaEditatu(e, lerroak);

                // Faktura automatikoa sortu
                if ("Osatua/Bidalita".equalsIgnoreCase(dialog.getEgoera())) {
                    try {
                        langilea.fakturaSortu(idEskaera);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                datuakKargatu();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errorea eskaera editatzean: " + ex.getMessage());
            }
        }
    }

    /**
     * Hautatutako eskaera ezabatu.
     */
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
            try {
                langilea.eskaeraEzabatu(idEskaera);
                datuakKargatu();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errorea eskaera ezabatzean: " + e.getMessage());
            }
        }
    }

    /**
     * Hautatutako eskaeraren faktura sortu (PDF).
     */
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
        String egoera = (String) model.getValueAt(aukeratutakoLerroa, 8);

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

    /**
     * Osatua/Bidalita dauden eskaera guztien fakturak sortu.
     */
    private void fakturaGuztiakSortu() {
        String sql = "SELECT id_eskaera FROM eskaerak WHERE eskaera_egoera = 'Osatua/Bidalita'";
        java.util.List<Integer> idZerrenda = new java.util.ArrayList<>();
        int kontagailua = 0;

        // 1. Fasea: ID-ak lortu (Konexioa ireki eta itxi egiten da bloke honetan)
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                idZerrenda.add(rs.getInt("id_eskaera"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errorea eskaerak bilatzean: " + e.getMessage());
            return;
        }

        // 2. Fasea: Fakturak sortu (Bakoitzak bere konexio kudeaketa dauka barruan)
        for (int idEskaera : idZerrenda) {
            try {
                if (langilea.fakturaSortu(idEskaera) != null) {
                    kontagailua++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JOptionPane.showMessageDialog(this, kontagailua + " faktura sortu dira zuzen.");
    }

    /**
     * Eskaera baten faktura logikoa ezabatu (fitxategia mantendu daiteke).
     */
    private void fakturaEzabatu() {
        int aukeratutakoLerroa = eskaeraTaula.getSelectedRow();
        if (aukeratutakoLerroa == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu eskaera bat faktura ezabatzeko.");
            return;
        }

        aukeratutakoLerroa = eskaeraTaula.convertRowIndexToModel(aukeratutakoLerroa);
        DefaultTableModel model = (DefaultTableModel) eskaeraTaula.getModel();

        Object idObj = model.getValueAt(aukeratutakoLerroa, 0);
        int idEskaera = (idObj instanceof Number) ? ((Number) idObj).intValue()
                : Integer.parseInt(idObj.toString());

        if (JOptionPane.showConfirmDialog(this, "Ziur zaude eskaera honen faktura ezabatu nahi duzula?",
                "Faktura Ezabatu", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                langilea.fakturaEzabatu(idEskaera);
                JOptionPane.showMessageDialog(this, "Faktura ondo ezabatu da.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errorea faktura ezabatzean: " + e.getMessage());
            }
        }
    }

    /**
     * Bezero baten xehetasunak ikusi.
     */
    private void bezeroaIkusi() {
        int r = bezeroTaula.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu bezero bat ikusteko.");
            return;
        }
        int rm = bezeroTaula.convertRowIndexToModel(r);
        Object idObj = bezeroTaula.getModel().getValueAt(rm, 0);
        int idBezeroa = Integer.parseInt(idObj.toString());

        try {
            Bezeroa b = langilea.bezeroaIkusi(idBezeroa);
            if (b != null) {
                BezeroaDialog dialog = new BezeroaDialog(this, "Bezeroa Ikusi", b, langilea);
                dialog.setViewMode(true);
                dialog.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
        }
    }

    /**
     * Bezero berria sortu.
     */
    private void bezeroaGehitu() {
        BezeroaDialog dialog = new BezeroaDialog(this, "Bezero Berria", null, langilea);
        dialog.setVisible(true);
        if (dialog.isOnartua()) {
            try {
                langilea.bezeroBerriaSortu(dialog.getBezeroa());
                datuakKargatu();
                JOptionPane.showMessageDialog(this, "Bezeroa ondo sortu da.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errorea bezeroa sortzean: " + e.getMessage());
            }
        }
    }

    /**
     * Bezero baten datuak editatu.
     */
    private void bezeroaEditatu() {
        int r = bezeroTaula.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu bezero bat editatzeko.");
            return;
        }
        int rm = bezeroTaula.convertRowIndexToModel(r);
        Object idObj = bezeroTaula.getModel().getValueAt(rm, 0);
        int idBezeroa = Integer.parseInt(idObj.toString());

        try {
            Bezeroa b = langilea.bezeroaIkusi(idBezeroa);
            if (b != null) {
                BezeroaDialog dialog = new BezeroaDialog(this, "Bezeroa Editatu", b, langilea);
                dialog.setVisible(true);
                if (dialog.isOnartua()) {
                    Bezeroa bBerria = dialog.getBezeroa();
                    // IDa mantendu
                    bBerria.setIdBezeroa(idBezeroa);
                    langilea.bezeroaEditatu(bBerria);
                    datuakKargatu();
                    JOptionPane.showMessageDialog(this, "Bezeroa eguneratu da.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
        }
    }

    /**
     * Bezero bat ezabatu.
     */
    private void bezeroaEzabatu() {
        int r = bezeroTaula.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu bezero bat ezabatzeko.");
            return;
        }
        int rm = bezeroTaula.convertRowIndexToModel(r);
        Object idObj = bezeroTaula.getModel().getValueAt(rm, 0);
        int idBezeroa = Integer.parseInt(idObj.toString());

        if (JOptionPane.showConfirmDialog(this, "Ziur zaude bezero hau ezabatu nahi duzula?", "Ezabatu",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                langilea.bezeroaKendu(idBezeroa);
                datuakKargatu();
                JOptionPane.showMessageDialog(this, "Bezeroa ezabatu da.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage(), "Ezin da ezabatu",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Klaseko aldagaia JTabbedPane gordetzeko
    private JTabbedPane pestainaPanelaRef;

    /**
     * Bezero baten eskaera historia ikusteko iragazkia aktibatu eskaeren fitxan.
     */
    private void bezeroHistorialaIkusi() {
        int r = bezeroTaula.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu bezero bat historiala ikusteko.");
            return;
        }
        int rm = bezeroTaula.convertRowIndexToModel(r);
        Object idObj = bezeroTaula.getModel().getValueAt(rm, 0);
        // Bezeroaren izena ere lortu dezakegu filtroan erakusteko, baina IDa nahikoa da
        String idStr = idObj.toString();

        if (pestainaPanelaRef != null) {
            pestainaPanelaRef.setSelectedIndex(1); // Eskaerak tab

            if (eskaeraOrdenatzailea != null) {
                // Zehatz mehatz ID hori dutenak (regex hasiera eta amaiera ^ $)
                // eskaerak taulako 2. zutabea (index 1) da bezeroa_id
                eskaeraOrdenatzailea.setRowFilter(RowFilter.regexFilter("^" + idStr + "$", 1));
                bilatuTestua.setText("Bezero ID: " + idStr); // Erabiltzaileari abisatu
            }
        }
    }

    private void kargatuKategoriak() {
        try (Connection konexioa = DB_Konexioa.konektatu();
                Statement stmt = konexioa.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT izena FROM produktu_kategoriak")) {
            while (rs.next()) {
                kategoriaFiltroa.addItem(rs.getString("izena"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void produktuDatuakKargatu() {
        String sql = "SELECT p.id_produktua, pk.izena as kategoria, p.izena, p.marka, p.mota, p.salmenta_prezioa, p.stock, p.salgai, p.eskaintza "
                + "FROM produktuak p JOIN produktu_kategoriak pk ON p.kategoria_id = pk.id_kategoria";
        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            DefaultTableModel model = TaulaModelatzailea.ereduaEraiki(pst.executeQuery());
            produktuTaula.setModel(model);
            produktuOrdenatzailea = new TableRowSorter<>(model);
            produktuTaula.setRowSorter(produktuOrdenatzailea);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void produktuPrezioaAldatu() {
        int row = produktuTaula.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu produktu bat.");
            return;
        }
        row = produktuTaula.convertRowIndexToModel(row);
        Object idObj = produktuTaula.getModel().getValueAt(row, 0);
        int idProd = (idObj instanceof Number) ? ((Number) idObj).intValue() : Integer.parseInt(idObj.toString());
        String izena = (String) produktuTaula.getModel().getValueAt(row, 2);

        String berria = JOptionPane.showInputDialog(this, izena + " - Sartu prezio berria:",
                produktuTaula.getModel().getValueAt(row, 5));
        if (berria != null && !berria.isEmpty()) {
            try {
                langilea.produktuariPrezioaAldatu(idProd, new java.math.BigDecimal(berria));
                produktuDatuakKargatu();
                JOptionPane.showMessageDialog(this, "Prezioa eguneratuta.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
            }
        }
    }

    private void produktuSalgaiToggle() {
        int row = produktuTaula.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu produktu bat.");
            return;
        }
        row = produktuTaula.convertRowIndexToModel(row);
        Object idObj = produktuTaula.getModel().getValueAt(row, 0);
        int idProd = (idObj instanceof Number) ? ((Number) idObj).intValue() : Integer.parseInt(idObj.toString());

        Object salgaiObj = produktuTaula.getModel().getValueAt(row, 7);
        boolean salgai = false;
        if (salgaiObj instanceof Boolean) {
            salgai = (Boolean) salgaiObj;
        } else if (salgaiObj instanceof Number) {
            salgai = ((Number) salgaiObj).intValue() != 0;
        } else if (salgaiObj != null) {
            salgai = "true".equalsIgnoreCase(salgaiObj.toString()) || "1".equals(salgaiObj.toString());
        }

        try {
            // Salgai jarri/kendu logic
            String sql = "UPDATE produktuak SET salgai = ? WHERE id_produktua = ?";
            try (Connection kon = DB_Konexioa.konektatu();
                    PreparedStatement pst = kon.prepareStatement(sql)) {
                pst.setBoolean(1, !salgai);
                pst.setInt(2, idProd);
                pst.executeUpdate();
            }
            produktuDatuakKargatu();
            JOptionPane.showMessageDialog(this, "Egoera aldatuta.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
        }
    }

    private void produktuEskaintzaJarri() {
        int row = produktuTaula.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu produktu bat.");
            return;
        }
        row = produktuTaula.convertRowIndexToModel(row);
        Object idObj = produktuTaula.getModel().getValueAt(row, 0);
        int idProd = (idObj instanceof Number) ? ((Number) idObj).intValue() : Integer.parseInt(idObj.toString());

        String eskaintza = JOptionPane.showInputDialog(this, "Sartu eskaintza (ehunekoa, adib. 10):", "0");
        if (eskaintza != null && !eskaintza.isEmpty()) {
            try {
                langilea.produktuariEskaintzaAldatzeko(idProd, new java.math.BigDecimal(eskaintza));
                produktuDatuakKargatu();
                JOptionPane.showMessageDialog(this, "Eskaintza eguneratuta.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
            }
        }
    }

    private void garbituEskaeraFiltroa() {
        bilatuTestua.setText("");
        if (eskaeraOrdenatzailea != null) {
            eskaeraOrdenatzailea.setRowFilter(null);
        }
    }

}
