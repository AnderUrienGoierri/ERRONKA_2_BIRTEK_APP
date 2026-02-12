package ui;

import db.DB_Konexioa;
import model.TeknikariLangilea;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Produktu baten atributu guztiak editatzeko leihoa.
 * Oinarrizko datuak eta datu teknikoak (subclass) kudeatzen ditu.
 */
public class ProduktuOsoaEditatuDialog extends JDialog {

    private final TeknikariLangilea teknikaria;
    private final int idProduktua;
    private String motaZaharra;

    // UI konponenteak
    private final JPanel scrollContent;
    private final Map<String, JComponent> xehetasunEremuak = new HashMap<>();
    private final JPanel xehetasunTeknikoakPanela;

    public ProduktuOsoaEditatuDialog(JFrame parent, TeknikariLangilea teknikaria, int idProduktua) {
        super(parent, "Editatu Produktua - ID: " + idProduktua, true);
        this.teknikaria = teknikaria;
        this.idProduktua = idProduktua;

        setSize(500, 700);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(new BorderLayout());

        scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        xehetasunTeknikoakPanela = new JPanel();
        xehetasunTeknikoakPanela.setLayout(new GridLayout(0, 2, 5, 5));
        xehetasunTeknikoakPanela.setBorder(BorderFactory.createTitledBorder("Datu Teknikoak"));

        // Oinarrizko datuen panela kargatu
        kargatuDatuak();

        // Botoiak
        JPanel botoiPanela = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton gordeBtn = new JButton("Gorde");
        gordeBtn.addActionListener(e -> gordeDatuak());
        JButton ezeztatuBtn = new JButton("Ezeztatu");
        ezeztatuBtn.addActionListener(e -> dispose());

        botoiPanela.add(gordeBtn);
        botoiPanela.add(ezeztatuBtn);
        getContentPane().add(botoiPanela, BorderLayout.SOUTH);
    }

    private void kargatuDatuak() {
        try (Connection kon = DB_Konexioa.konektatu()) {
            // 1. Oinarrizko datuak
            String sql = "SELECT * FROM produktuak WHERE id_produktua = ?";
            try (PreparedStatement pst = kon.prepareStatement(sql)) {
                pst.setInt(1, idProduktua);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        motaZaharra = rs.getString("mota");
                        gehituEremua("Izena", "izena", rs.getString("izena"));
                        gehituEremua("Marka", "marka", rs.getString("marka"));

                        // Mota (aldagarria)
                        String[] motak = { "Eramangarria", "Mahai-gainekoa", "Mugikorra", "Tableta", "Zerbitzaria",
                                           "Pantaila", "Softwarea" };
                        JComboBox<String> motaBox = new JComboBox<>(motak);
                        motaBox.setSelectedItem(motaZaharra);
                        motaBox.addActionListener(
                                e -> eguneratuDatuTeknikoenPanela((String) motaBox.getSelectedItem(), true));
                        gehituEremua("Mota", "mota", motaBox);

                        gehituEremua("Kategoria ID", "kategoria_id", String.valueOf(rs.getInt("kategoria_id")));
                        gehituEremua("Hornitzaile ID", "hornitzaile_id", String.valueOf(rs.getInt("hornitzaile_id")));
                        gehituEremua("Biltegi ID", "biltegi_id", String.valueOf(rs.getInt("biltegi_id")));
                        gehituEremua("Stock", "stock", String.valueOf(rs.getInt("stock")));
                        gehituEremua("Prezioa", "salmenta_prezioa", rs.getBigDecimal("salmenta_prezioa").toString());
                        gehituEremua("Zergak %", "zergak_ehunekoa", rs.getBigDecimal("zergak_ehunekoa").toString());

                        String[] egoerak = { "Berria", "Berritua A", "Berritua B", "Hondatua", "Zehazteko" };
                        JComboBox<String> egoeraBox = new JComboBox<>(egoerak);
                        egoeraBox.setSelectedItem(rs.getString("produktu_egoera"));
                        gehituEremua("Egoera", "produktu_egoera", egoeraBox);

                        JCheckBox salgaiCheck = new JCheckBox("Salgai dago", rs.getBoolean("salgai"));
                        gehituEremua("Salgai", "salgai", salgaiCheck);

                        JTextArea deskArea = new JTextArea(rs.getString("deskribapena"), 3, 20);
                        gehituEremua("Deskribapena", "deskribapena", new JScrollPane(deskArea));
                        xehetasunEremuak.put("deskribapena_raw", deskArea); 

                        gehituEremua("Irudia URL", "irudia_url", rs.getString("irudia_url"));

                        scrollContent.add(xehetasunTeknikoakPanela);
                        eguneratuDatuTeknikoenPanela(motaZaharra, false);
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errorea datuak kargatzean: " + e.getMessage());
        }
    }

    private void eguneratuDatuTeknikoenPanela(String mota, boolean garbitu) {
        xehetasunTeknikoakPanela.removeAll();
        // Garbitu teknikoen eremuak
        if (garbitu) {
            // Mantendu oinarrizko eremuak
            Map<String, JComponent> eremuBerriak = new HashMap<>();
            String[] oinarrizkoEremuak = { "izena", "marka", "mota", "kategoria_id", "hornitzaile_id", "biltegi_id", "stock",
                                           "salmenta_prezioa", "zergak_ehunekoa", "produktu_egoera", "salgai", "deskribapena",
                                           "deskribapena_raw", "irudia_url" };
            for (String k : oinarrizkoEremuak)
                if (xehetasunEremuak.containsKey(k))
                    eremuBerriak.put(k, xehetasunEremuak.get(k));
            xehetasunEremuak.clear();
            xehetasunEremuak.putAll(eremuBerriak);
        }

        try (Connection kon = DB_Konexioa.konektatu()) {
            String taula = lortuTaulaIzena(mota);
            if (!taula.isEmpty()) {
                String sql = "SELECT * FROM " + taula + " WHERE id_produktua = ?";
                try (PreparedStatement pst = kon.prepareStatement(sql)) {
                    pst.setInt(1, idProduktua);
                    try (ResultSet rs = pst.executeQuery()) {
                        ResultSetMetaData md = rs.getMetaData();
                        boolean hasData = rs.next();
                        for (int i = 2; i <= md.getColumnCount(); i++) {
                            String col = md.getColumnName(i);
                            String val = hasData ? rs.getString(i) : "";
                            gehituEremuTeknikoa(col.substring(0, 1).toUpperCase() + col.substring(1).replace("_", " "),
                                    col, val);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            // Taula ez bada existitzen dena, hutsaen erakutsi
            //  motaren arabera
            erakutsiEremuHutsakMotarenArabera(mota);
        }

        xehetasunTeknikoakPanela.revalidate();
        xehetasunTeknikoakPanela.repaint();
    }

    private void erakutsiEremuHutsakMotarenArabera(String mota) {
        switch (mota) {
            case "Eramangarria":
                gehituEremuTeknikoa("Prozesadorea", "prozesadorea", "");
                gehituEremuTeknikoa("RAM GB", "ram_gb", "0");
                gehituEremuTeknikoa("Diskoa GB", "diskoa_gb", "0");
                gehituEremuTeknikoa("Pantaila Tamaina", "pantaila_tamaina", "0");
                gehituEremuTeknikoa("Bateria Wh", "bateria_wh", "0");
                gehituEremuTeknikoa("Sistema Eragilea", "sistema_eragilea", "");
                gehituEremuTeknikoa("Pisua Kg", "pisua_kg", "0");
                break;
            case "Mahai-gainekoa":
                gehituEremuTeknikoa("Prozesadorea", "prozesadorea", "");
                gehituEremuTeknikoa("Plaka Basea", "plaka_basea", "");
                gehituEremuTeknikoa("RAM GB", "ram_gb", "0");
                gehituEremuTeknikoa("Diskoa GB", "diskoa_gb", "0");
                gehituEremuTeknikoa("Txartel Grafikoa", "txartel_grafikoa", "");
                gehituEremuTeknikoa("Elikatze Iturria W", "elikatze_iturria_w", "0");
                gehituEremuTeknikoa("Kaxa Formatua", "kaxa_formatua", "ATX");
                break;
            case "Mugikorra":
                gehituEremuTeknikoa("Pantaila Teknologia", "pantaila_teknologia", "");
                gehituEremuTeknikoa("Pantaila Hazbeteak", "pantaila_hazbeteak", "0");
                gehituEremuTeknikoa("Biltegiratzea GB", "biltegiratzea_gb", "0");
                gehituEremuTeknikoa("RAM GB", "ram_gb", "0");
                gehituEremuTeknikoa("Kamera Nagusa MP", "kamera_nagusa_mp", "0");
                gehituEremuTeknikoa("Bateria mAh", "bateria_mah", "0");
                gehituEremuTeknikoa("Sistema Eragilea", "sistema_eragilea", "");
                gehituEremuTeknikoa("Sareak", "sareak", "4G");
                break;
            case "Tableta":
                gehituEremuTeknikoa("Pantaila Hazbeteak", "pantaila_hazbeteak", "0");
                gehituEremuTeknikoa("Biltegiratzea GB", "biltegiratzea_gb", "0");
                gehituEremuTeknikoa("Konektibitatea", "konektibitatea", "WiFi");
                gehituEremuTeknikoa("Sistema Eragilea", "sistema_eragilea", "");
                gehituEremuTeknikoa("Bateria mAh", "bateria_mah", "0");
                gehituEremuTeknikoa("Arkatzarekin Bateragarria", "arkatzarekin_bateragarria", "false");
                break;
            case "Zerbitzaria":
                gehituEremuTeknikoa("Prozesadore Nukleoak", "prozesadore_nukleoak", "0");
                gehituEremuTeknikoa("RAM Mota", "ram_mota", "DDR4");
                gehituEremuTeknikoa("Disko Badiak", "disko_badiak", "0");
                gehituEremuTeknikoa("Rack Unitateak", "rack_unitateak", "0");
                gehituEremuTeknikoa("Elikatze Iturri Erredundantea", "elikatze_iturri_erredundantea", "false");
                gehituEremuTeknikoa("Raid Kontroladora", "raid_kontroladora", "");
                break;
            case "Pantaila":
                gehituEremuTeknikoa("Hazbeteak", "hazbeteak", "0");
                gehituEremuTeknikoa("Bereizmena", "bereizmena", "");
                gehituEremuTeknikoa("Panel Mota", "panel_mota", "IPS");
                gehituEremuTeknikoa("Freskatze Tasa Hz", "freskatze_tasa_hz", "60");
                gehituEremuTeknikoa("Konexioak", "konexioak", "");
                gehituEremuTeknikoa("Kurbatura", "kurbatura", "");
                break;
            case "Softwarea":
                gehituEremuTeknikoa("Software Mota", "software_mota", "");
                gehituEremuTeknikoa("Lizentzia Mota", "lizentzia_mota", "Retail");
                gehituEremuTeknikoa("Bertsioa", "bertsioa", "");
                gehituEremuTeknikoa("Garatzailea", "garatzailea", "");
                gehituEremuTeknikoa("Librea", "librea", "false");
                break;
        }
    }

    private void gehituEremua(String label, String key, String value) {
        JTextField tf = new JTextField(value);
        gehituEremua(label, key, tf);
    }

    private void gehituEremua(String label, String key, JComponent comp) {
        JPanel p = new JPanel(new GridLayout(1, 2));
        p.add(new JLabel(label + ":"));
        p.add(comp);
        scrollContent.add(p);
        xehetasunEremuak.put(key, comp);
    }

    private void gehituEremuTeknikoa(String label, String key, String value) {
        xehetasunTeknikoakPanela.add(new JLabel(label + ":"));
        JTextField tf = new JTextField(value);
        xehetasunTeknikoakPanela.add(tf);
        xehetasunEremuak.put(key, tf);
    }

    private void gordeDatuak() {
        Map<String, String> datuak = new HashMap<>();
        for (Map.Entry<String, JComponent> entry : xehetasunEremuak.entrySet()) {
            String key = entry.getKey();
            JComponent c = entry.getValue();
            if (c instanceof JTextField)
                datuak.put(key, ((JTextField) c).getText());
            else if (c instanceof JComboBox)
                datuak.put(key, ((JComboBox<?>) c).getSelectedItem().toString());
            else if (c instanceof JCheckBox)
                datuak.put(key, String.valueOf(((JCheckBox) c).isSelected()));
            else if (key.equals("deskribapena")) {
                JTextArea ta = (JTextArea) xehetasunEremuak.get("deskribapena_raw");
                datuak.put(key, ta.getText());
            }
        }

        try {
            teknikaria.produktuaOsorikEditatu(idProduktua, datuak);
            JOptionPane.showMessageDialog(this, "Produktua ondo eguneratu da.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Errorea gordetzean: " + e.getMessage(), "Errorea",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String lortuTaulaIzena(String mota) {
        if (mota == null)
            return "";
        switch (mota) {
            case "Eramangarria":
                return "eramangarriak";
            case "Mahai-gainekoa":
                return "mahai_gainekoak";
            case "Mugikorra":
                return "mugikorrak";
            case "Tableta":
                return "tabletak";
            case "Zerbitzaria":
                return "zerbitzariak";
            case "Pantaila":
                return "pantailak";
            case "Softwarea":
                return "softwareak";
            default:
                return "";
        }
    }
}
