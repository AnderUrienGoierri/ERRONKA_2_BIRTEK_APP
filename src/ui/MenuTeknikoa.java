package ui;

import db.DB_Konexioa;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MenuTeknikoa extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable konponketaTaula, produktuTaula, akatsTaula, nireFitxaketaTaula;
    private JTextField bilatuTestua;
    private TableRowSorter<DefaultTableModel> konponketaOrdenatzailea, produktuOrdenatzailea, akatsOrdenatzailea,
            nireFitxaketaOrdenatzailea, unekoOrdenatzailea;

    // Fitxaketa
    private JLabel fitxaketaInfoEtiketa;

    // Erabiltzaile datuak
    private TeknikariLangilea langilea;

    /**
     * Eraikitzailea eguneratua.
     */
    public MenuTeknikoa(Langilea oinarrizkoLangilea) {
        this.langilea = new TeknikariLangilea(oinarrizkoLangilea);

        setTitle("Birtek - TEKNIKOA (SAT)");
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

        JLabel erabiltzaileEtiketa = new JLabel(langilea.getSailaId() + " | " + langilea.getIzena() + " "
                + langilea.getAbizena());
        erabiltzaileEtiketa.setFont(new Font("SansSerif", Font.BOLD, 12));
        erabiltzaileEtiketa.setForeground(new Color(0, 102, 102));

        // Fitxaketa
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

        // Atzera botoia logikoki hemen egon daiteke

        eskuinekoPanela.add(erabiltzaileEtiketa);
        eskuinekoPanela.add(fitxaketaPanela);
        eskuinekoPanela.add(saioaItxiBotoia);

        goikoPanela.add(eskuinekoPanela, BorderLayout.EAST);

        JTabbedPane pestainaPanela = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(pestainaPanela, BorderLayout.CENTER);

        // --- KONPONKETAK TAB ---
        JPanel konponketaPanela = new JPanel(new BorderLayout());
        konponketaTaula = new JTable();

        konponketaTaula.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int errenkada = konponketaTaula.getSelectedRow();
                    if (errenkada != -1) {
                        int errenkadaModeloa = konponketaTaula.convertRowIndexToModel(errenkada);
                        Object idObj = konponketaTaula.getModel().getValueAt(errenkadaModeloa, 0);
                        if (idObj != null) {
                            int idKonponketa = Integer.parseInt(idObj.toString());
                            irekiKonponketaXehetasuna(idKonponketa);
                        }
                    }
                }
            }
        });

        JLabel informazioEtiketa = new JLabel(
                "Klik bikoitza egin konponketa bat editatzeko / Doble click para editar");
        informazioEtiketa.setHorizontalAlignment(SwingConstants.CENTER);
        informazioEtiketa.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        konponketaPanela.add(informazioEtiketa, BorderLayout.SOUTH);
        konponketaPanela.add(new JScrollPane(konponketaTaula), BorderLayout.CENTER);
        pestainaPanela.addTab("Konponketak", konponketaPanela);

        // --- PRODUKTUAK TAB ---
        JPanel produktuPanela = new JPanel(new BorderLayout());
        produktuTaula = new JTable();
        produktuPanela.add(new JScrollPane(produktuTaula), BorderLayout.CENTER);
        pestainaPanela.addTab("Produktuak", produktuPanela);

        // --- AKATSAK TAB ---
        JPanel akatsPanela = new JPanel(new BorderLayout());
        akatsTaula = new JTable();
        akatsPanela.add(new JScrollPane(akatsTaula), BorderLayout.CENTER);
        pestainaPanela.addTab("Akatsak", akatsPanela);

        // --- NIRE FITXAKETAK TAB ---
        JPanel nireFitxaketakPanela = new JPanel(new BorderLayout());
        nireFitxaketaTaula = new JTable();
        nireFitxaketakPanela.add(new JScrollPane(nireFitxaketaTaula), BorderLayout.CENTER);
        pestainaPanela.addTab("Nire Fitxaketak", nireFitxaketakPanela);

        pestainaPanela.addChangeListener(e -> {
            bilatuTestua.setText("");
            int index = pestainaPanela.getSelectedIndex();
            if (index == 0)
                unekoOrdenatzailea = konponketaOrdenatzailea;
            else if (index == 1)
                unekoOrdenatzailea = produktuOrdenatzailea;
            else if (index == 2)
                unekoOrdenatzailea = akatsOrdenatzailea;
            else if (index == 3)
                unekoOrdenatzailea = nireFitxaketaOrdenatzailea;
        });

        // CRUD Botoiak
        JPanel botoiCrudPanela = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton gehituBotoia = new JButton("Gehitu +");
        JButton editatuBotoia = new JButton("Editatu");
        JButton ezabatuBotoia = new JButton("Ezabatu");

        gehituBotoia.addActionListener(e -> gehituElementua(pestainaPanela.getSelectedIndex()));
        editatuBotoia.addActionListener(e -> editatuElementua(pestainaPanela.getSelectedIndex()));
        ezabatuBotoia.addActionListener(e -> ezabatuElementua(pestainaPanela.getSelectedIndex()));

        botoiCrudPanela.add(gehituBotoia);
        botoiCrudPanela.add(editatuBotoia);
        botoiCrudPanela.add(ezabatuBotoia);

        JButton prezioaBotoia = new JButton("Prezioa Ezarri");
        prezioaBotoia.addActionListener(e -> prezioaEzarriElementua(pestainaPanela.getSelectedIndex()));
        botoiCrudPanela.add(prezioaBotoia);

        JPanel behekoPanela = new JPanel(new BorderLayout());
        behekoPanela.add(botoiCrudPanela, BorderLayout.NORTH);

        JButton kargatuBotoia = new JButton("Kargatu / Berritu");
        kargatuBotoia.addActionListener(e -> datuakKargatu());
        behekoPanela.add(kargatuBotoia, BorderLayout.CENTER);

        getContentPane().add(behekoPanela, BorderLayout.SOUTH);

        pestainaPanela.addChangeListener(e -> {
            bilatuTestua.setText("");
            int index = pestainaPanela.getSelectedIndex();
            if (index == 0)
                unekoOrdenatzailea = konponketaOrdenatzailea;
            else if (index == 1)
                unekoOrdenatzailea = produktuOrdenatzailea;
            else if (index == 2)
                unekoOrdenatzailea = akatsOrdenatzailea;
            else if (index == 3)
                unekoOrdenatzailea = nireFitxaketaOrdenatzailea;
        });

        if (!java.beans.Beans.isDesignTime()) {
            datuakKargatu();
            eguneratuFitxaketaEgoera();
        }
    }

    public MenuTeknikoa() {
        this(new Langilea(Sesioa.idLangilea, Sesioa.izena, Sesioa.abizena, Sesioa.sailaIzena, null, 0, "", "", "", "",
                "ES", "", "", null, null, true, 2, "", null));
    }

    // --- FITXAKETA METODOAK ---
    private void fitxatu(String mota) {
        try {
            if ("Sarrera".equals(mota)) {
                langilea.sarreraFitxaketaEgin();
            } else {
                langilea.irteeraFitxaketaEgin();
            }
            eguneratuFitxaketaEgoera();
            datuakKargatu(); // Refrescar tableros
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
        NireDatuakDialog dialog = new NireDatuakDialog(this, langilea);
        dialog.setVisible(true);
    }

    private void datuakKargatu() {
        try (Connection konexioa = DB_Konexioa.konektatu()) {
            DefaultTableModel m1 = TaulaModelatzailea
                    .ereduaEraiki(konexioa.prepareStatement("SELECT * FROM konponketak").executeQuery());
            konponketaTaula.setModel(m1);
            konponketaOrdenatzailea = new TableRowSorter<>(m1);
            konponketaTaula.setRowSorter(konponketaOrdenatzailea);

            DefaultTableModel m2 = TaulaModelatzailea.ereduaEraiki(konexioa
                    .prepareStatement(
                            "SELECT id_produktua, izena, produktu_egoera, salmenta_prezioa, eskaintza FROM produktuak")
                    .executeQuery());
            produktuTaula.setModel(m2);
            produktuOrdenatzailea = new TableRowSorter<>(m2);
            produktuTaula.setRowSorter(produktuOrdenatzailea);

            DefaultTableModel m3 = TaulaModelatzailea.ereduaEraiki(konexioa
                    .prepareStatement("SELECT * FROM akatsak").executeQuery());
            akatsTaula.setModel(m3);
            akatsOrdenatzailea = new TableRowSorter<>(m3);
            akatsTaula.setRowSorter(akatsOrdenatzailea);

            // Nire Fitxaketak
            PreparedStatement pstNF = konexioa.prepareStatement(
                    "SELECT data, CAST(ordua AS CHAR) AS ordua, mota FROM fitxaketak WHERE langilea_id = ? ORDER BY id_fitxaketa DESC");
            pstNF.setInt(1, langilea.getIdLangilea());
            DefaultTableModel mNF = TaulaModelatzailea.ereduaEraiki(pstNF.executeQuery());
            nireFitxaketaTaula.setModel(mNF);
            nireFitxaketaOrdenatzailea = new TableRowSorter<>(mNF);
            nireFitxaketaTaula.setRowSorter(nireFitxaketaOrdenatzailea);

            if (unekoOrdenatzailea == null)
                unekoOrdenatzailea = konponketaOrdenatzailea;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ezabatuElementua(int index) {
        JTable t = (index == 0) ? konponketaTaula : (index == 1) ? produktuTaula : akatsTaula;

        if (t.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu elementu bat ezabatzeko.");
            return;
        }

        int rm = t.convertRowIndexToModel(t.getSelectedRow());
        Object idObj = t.getModel().getValueAt(rm, 0);
        int id = Integer.parseInt(idObj.toString());

        if (JOptionPane.showConfirmDialog(this, "Ziur ID " + id + " ezabatu nahi duzula?", "Ezabatu",
                JOptionPane.YES_NO_OPTION) == 0) {
            try {
                if (index == 2) { // Akatsak
                    langilea.akatsaEzabatu(id);
                } else {
                    // Besteetarako oraindik SQL zuzena mantentzen dugu momentuz, edo dagokion
                    // metodoa sortu beharko litzateke
                    String taula = (index == 0) ? "konponketak" : "produktuak";
                    String idCol = (index == 0) ? "id_konponketa" : "id_produktua";
                    try (Connection kon = DB_Konexioa.konektatu()) {
                        PreparedStatement pst = kon
                                .prepareStatement("DELETE FROM " + taula + " WHERE " + idCol + " = ?");
                        pst.setInt(1, id);
                        pst.executeUpdate();
                    }
                }
                datuakKargatu();
                JOptionPane.showMessageDialog(this, "Elementua ezabatu da.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
            }
        }
    }

    private void gehituElementua(int index) {
        if (index == 0) { // Konponketak
            try {
                // 1. Datuak lortu
                java.util.List<Produktua> produktuak = langilea.produktuakIkusi();
                java.util.List<Akatsa> akatsak = langilea.akatsaIkusi();

                if (produktuak.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ez dago produkturik sisteman.");
                    return;
                }
                if (akatsak.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ez dago akats motarik sisteman. Sortu bat lehenik.");
                    return;
                }

                // 2. UI Konponenteak
                JComboBox<ComboItem> produktuBox = new JComboBox<>();
                for (Produktua p : produktuak) {
                    produktuBox.addItem(new ComboItem(p.getIdProduktua(), p.getIzena() + " (" + p.getMarka() + ")"));
                }

                JComboBox<ComboItem> akatsBox = new JComboBox<>();
                for (Akatsa a : akatsak) {
                    akatsBox.addItem(new ComboItem(a.getIdAkatsa(), a.getIzena()));
                }

                JTextArea oharrakArea = new JTextArea(5, 20);

                Object[] message = {
                        "Produktua:", produktuBox,
                        "Akatsa:", akatsBox,
                        "Oharrak:", new JScrollPane(oharrakArea)
                };

                int option = JOptionPane.showConfirmDialog(this, message, "Sortu Konponketa Berria",
                        JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    ComboItem selectedProd = (ComboItem) produktuBox.getSelectedItem();
                    ComboItem selectedAkats = (ComboItem) akatsBox.getSelectedItem();
                    String oharrak = oharrakArea.getText();

                    if (selectedProd == null || selectedAkats == null)
                        return;

                    // 3. Konponketa sortu
                    Konponketa k = new Konponketa(
                            0, // ID
                            selectedProd.getId(),
                            langilea.getIdLangilea(),
                            new Timestamp(System.currentTimeMillis()), // Hasiera data
                            null, // Amaiera data
                            "Prozesuan",
                            selectedAkats.getId(),
                            oharrak,
                            null);

                    langilea.konponketaEgin(k);
                    datuakKargatu();
                    JOptionPane.showMessageDialog(this, "Konponketa ondo sortu da!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errorea: " + e.toString() + "\n" + e.getMessage(), "Errorea",
                        JOptionPane.ERROR_MESSAGE);
            }

        } else if (index == 2) { // Akatsak
            JTextField izenaField = new JTextField();
            JTextArea deskribapenaArea = new JTextArea(5, 20);

            Object[] message = {
                    "Izena (Derrigorrezkoa):", izenaField,
                    "Deskribapena:", new JScrollPane(deskribapenaArea)
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Gehitu Akatsa", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String izena = izenaField.getText().trim();
                String deskribapena = deskribapenaArea.getText().trim();

                if (izena.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Izena derrigorrezkoa da.", "Errorea",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Akatsa a = new Akatsa(0, izena, deskribapena);
                    langilea.akatsaSortu(a);
                    datuakKargatu();
                    JOptionPane.showMessageDialog(this, "Akatsa ondo sortu da.");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Errorea sortzean: " + e.getMessage(), "Errorea",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Funtzio hau oraindik ez dago erabilgarri.");
        }
    }

    private void editatuElementua(int index) {
        if (index == 0) { // Konponketak
            int r = konponketaTaula.getSelectedRow();
            if (r == -1)
                return;
            irekiKonponketaXehetasuna(Integer.parseInt(konponketaTaula.getValueAt(r, 0).toString()));
        } else if (index == 1) { // Produktuak
            int r = produktuTaula.getSelectedRow();
            if (r == -1)
                return;
            int rm = produktuTaula.convertRowIndexToModel(r);
            Object id = produktuTaula.getModel().getValueAt(rm, 0);
            String izenZ = (String) produktuTaula.getModel().getValueAt(rm, 1);
            String egoeraZ = (String) produktuTaula.getModel().getValueAt(rm, 2);

            JTextField izenaField = new JTextField(izenZ);
            JTextField egoeraField = new JTextField(egoeraZ);
            Object[] message = { "Izena:", izenaField, "Egoera:", egoeraField };
            int option = JOptionPane.showConfirmDialog(null, message, "Editatu Produktua",
                    JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try (Connection kon = DB_Konexioa.konektatu()) {
                    PreparedStatement pst = kon.prepareStatement(
                            "UPDATE produktuak SET izena = ?, produktu_egoera = ? WHERE id_produktua = ?");
                    pst.setString(1, izenaField.getText());
                    pst.setString(2, egoeraField.getText());
                    pst.setObject(3, id);
                    pst.executeUpdate();
                    datuakKargatu();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
                }
            }
        } else if (index == 2) { // Akatsak
            int r = akatsTaula.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Aukeratu akats bat editatzeko.");
                return;
            }

            // Ereduko balioak lortu (View -> Model konbertsioa)
            int rm = akatsTaula.convertRowIndexToModel(r);
            // Suposatzen dugu zutabeen ordena: [0]=ID, [1]=Izena, [2]=Deskribapena
            // Egiaztatu behar da datuakKargatu() metodoan zutabeen ordena
            Object idObj = akatsTaula.getModel().getValueAt(rm, 0);
            Object izenaObj = akatsTaula.getModel().getValueAt(rm, 1);
            Object deskribapenaObj = akatsTaula.getModel().getValueAt(rm, 2);

            int idAkatsa = Integer.parseInt(idObj.toString());
            String unekoIzena = izenaObj != null ? izenaObj.toString() : "";
            String unekoDeskr = deskribapenaObj != null ? deskribapenaObj.toString() : "";

            JTextField izenaField = new JTextField(unekoIzena);
            JTextArea deskribapenaArea = new JTextArea(unekoDeskr);
            deskribapenaArea.setRows(5);
            deskribapenaArea.setColumns(20);

            Object[] message = {
                    "Izena:", izenaField,
                    "Deskribapena:", new JScrollPane(deskribapenaArea)
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Editatu Akatsa", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String izenaBerria = izenaField.getText().trim();
                String deskribapenaBerria = deskribapenaArea.getText().trim();

                if (izenaBerria.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Izena ezin da hutsik egon.", "Errorea",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Akatsa a = new Akatsa(idAkatsa, izenaBerria, deskribapenaBerria);
                    langilea.akatsaEditatu(a);
                    datuakKargatu();
                    JOptionPane.showMessageDialog(this, "Akatsa eguneratu da.");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Errorea editatzean: " + e.getMessage(), "Errorea",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Funtzio hau oraindik ez dago erabilgarri.");
        }
    }

    private void prezioaEzarriElementua(int index) {
        if (index != 1) { // 1 = Produktuak
            JOptionPane.showMessageDialog(this, "Aukera hau produktuetan bakarrik dago erabilgarri.");
            return;
        }

        int r = produktuTaula.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Aukeratu produktu bat prezioa ezartzeko.");
            return;
        }

        int rm = produktuTaula.convertRowIndexToModel(r);
        Object idObj = produktuTaula.getModel().getValueAt(rm, 0);
        int idProduktua = Integer.parseInt(idObj.toString());

        JTextField prezioaField = new JTextField();
        JTextField eskaintzaField = new JTextField();
        Object[] message = {
                "Salmenta Prezioa (€):", prezioaField,
                "Eskaintza (€) (Aukerazkoa):", eskaintzaField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Ezarri Prezioa", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String pStr = prezioaField.getText().replace(",", ".").trim();
                String eStr = eskaintzaField.getText().replace(",", ".").trim();

                if (pStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Prezioa derrigorrezkoa da.");
                    return;
                }

                java.math.BigDecimal prezioa = new java.math.BigDecimal(pStr);
                java.math.BigDecimal eskaintza = null;
                if (!eStr.isEmpty()) {
                    eskaintza = new java.math.BigDecimal(eStr);
                }

                langilea.prezioaEzarri(idProduktua, prezioa, eskaintza);
                datuakKargatu();
                JOptionPane.showMessageDialog(this, "Prezioa eguneratu da.");

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Mesedez, sartu zenbaki baliodunak (adib. 10.50).", "Errorea",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage(), "Errorea", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void irekiKonponketaXehetasuna(int idKonponketa) {
        try {
            // Balioak lortu taulatik
            int errenkada = konponketaTaula.getSelectedRow();
            String egoera = konponketaTaula.getValueAt(errenkada, 5).toString();
            // 6. zutabea akatsa_id da, 7.a oharrak
            int akatsaId = Integer.parseInt(konponketaTaula.getValueAt(errenkada, 6).toString());
            Object oharrakObj = konponketaTaula.getValueAt(errenkada, 7);
            String oharrak = (oharrakObj != null) ? oharrakObj.toString() : "";

            java.util.List<Akatsa> zerrenda = langilea.akatsaIkusi();

            KonponketaXehetasunaElkarrizketa elkarrizketa = new KonponketaXehetasunaElkarrizketa(
                    idKonponketa, egoera, oharrak, akatsaId, zerrenda);
            elkarrizketa.setModal(true);
            elkarrizketa.setVisible(true);
            datuakKargatu();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errorea datuak kargatzean: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
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

    // Helper class for ComboBox items
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
