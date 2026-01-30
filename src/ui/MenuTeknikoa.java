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
    private JTable konponketaTaula, produktuTaula, akatsTaula;
    private JTextField bilatuTestua;
    private TableRowSorter<DefaultTableModel> konponketaOrdenatzailea, produktuOrdenatzailea, akatsOrdenatzailea,
            unekoOrdenatzailea;

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

        pestainaPanela.addChangeListener(e -> {
            bilatuTestua.setText("");
            int index = pestainaPanela.getSelectedIndex();
            if (index == 0)
                unekoOrdenatzailea = konponketaOrdenatzailea;
            else if (index == 1)
                unekoOrdenatzailea = produktuOrdenatzailea;
            else if (index == 2)
                unekoOrdenatzailea = akatsOrdenatzailea;
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

    private void datuakKargatu() {
        try (Connection konexioa = DB_Konexioa.konektatu()) {
            DefaultTableModel m1 = TaulaModelatzailea
                    .ereduaEraiki(konexioa.prepareStatement("SELECT * FROM konponketak").executeQuery());
            konponketaTaula.setModel(m1);
            konponketaOrdenatzailea = new TableRowSorter<>(m1);
            konponketaTaula.setRowSorter(konponketaOrdenatzailea);

            DefaultTableModel m2 = TaulaModelatzailea.ereduaEraiki(konexioa
                    .prepareStatement("SELECT id_produktua, izena, produktu_egoera FROM produktuak").executeQuery());
            produktuTaula.setModel(m2);
            produktuOrdenatzailea = new TableRowSorter<>(m2);
            produktuTaula.setRowSorter(produktuOrdenatzailea);

            DefaultTableModel m3 = TaulaModelatzailea.ereduaEraiki(konexioa
                    .prepareStatement("SELECT * FROM akatsak").executeQuery());
            akatsTaula.setModel(m3);
            akatsOrdenatzailea = new TableRowSorter<>(m3);
            akatsTaula.setRowSorter(akatsOrdenatzailea);

            if (unekoOrdenatzailea == null)
                unekoOrdenatzailea = konponketaOrdenatzailea;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ezabatuElementua(int index) {
        JTable t = (index == 0) ? konponketaTaula : (index == 1) ? produktuTaula : akatsTaula;
        String taula = (index == 0) ? "konponketak" : (index == 1) ? "produktuak" : "akatsak";
        String idCol = (index == 0) ? "id_konponketa" : (index == 1) ? "id_produktua" : "id_akatsa";

        if (t.getSelectedRow() == -1)
            return;
        Object id = t.getModel().getValueAt(t.convertRowIndexToModel(t.getSelectedRow()), 0);

        if (JOptionPane.showConfirmDialog(this, "Ziur ID " + id + " ezabatu nahi duzula?", "Ezabatu",
                JOptionPane.YES_NO_OPTION) == 0) {
            try (Connection kon = DB_Konexioa.konektatu()) {
                PreparedStatement pst = kon.prepareStatement("DELETE FROM " + taula + " WHERE " + idCol + " = ?");
                pst.setObject(1, id);
                pst.executeUpdate();
                datuakKargatu();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
            }
        }
    }

    private void gehituElementua(int index) {
        if (index == 2) { // Akatsak
            String desc = JOptionPane.showInputDialog(this, "Sartu akatsaren deskribapena:");
            if (desc != null && !desc.trim().isEmpty()) {
                try (Connection kon = DB_Konexioa.konektatu()) {
                    PreparedStatement pst = kon.prepareStatement("INSERT INTO akatsak (deskribapena) VALUES (?)");
                    pst.setString(1, desc);
                    pst.executeUpdate();
                    datuakKargatu();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
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
            if (r == -1)
                return;
            int rm = akatsTaula.convertRowIndexToModel(r);
            Object id = akatsTaula.getModel().getValueAt(rm, 0);
            String descZ = (String) akatsTaula.getModel().getValueAt(rm, 1);
            String descB = JOptionPane.showInputDialog(this, "Eguneratu deskribapena:", descZ);
            if (descB != null) {
                try (Connection kon = DB_Konexioa.konektatu()) {
                    PreparedStatement pst = kon
                            .prepareStatement("UPDATE akatsak SET deskribapena = ? WHERE id_akatsa = ?");
                    pst.setString(1, descB);
                    pst.setObject(2, id);
                    pst.executeUpdate();
                    datuakKargatu();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Funtzio hau oraindik ez dago erabilgarri.");
        }
    }

    private void irekiKonponketaXehetasuna(int idKonponketa) {
        // Balioak lortu taulatik (suposatuz ordena: id, egoera, oharrak...)
        int errenkada = konponketaTaula.getSelectedRow();
        String egoera = konponketaTaula.getValueAt(errenkada, 5).toString(); // suposatuz egoera 5. zutabea dela
        String oharrak = konponketaTaula.getValueAt(errenkada, 6).toString(); // suposatuz oharrak 6. zutabea dela

        KonponketaXehetasunaElkarrizketa elkarrizketa = new KonponketaXehetasunaElkarrizketa(idKonponketa, egoera,
                oharrak);
        elkarrizketa.setModal(true);
        elkarrizketa.setVisible(true);
        datuakKargatu();
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
}
