package ui;

import db.DB_Konexioa;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * SarreraBerriaDialog klasea.
 * Produktu sarrera berriak sartzeko elkarrizketa-leihoa.
 */
public class SarreraBerriaDialog extends JDialog {

    private BiltegiLangilea langilea;
    private JComboBox<MenuLogistika.HornitzaileElementua> hornitzaileHautatzailea;
    private JCheckBox hornitzaileBerriaAukera;
    private JTextField izenaBerriaTestua, postaBerriaTestua, ifzBerriaTestua;
    private JTextField produktuIzenaTestua, markaTestua, kantitateTestua, deskribapenaTestua, irudiaUrlTestua,
            egoeraOharraTestua;
    private JComboBox<MenuLogistika.KategoriaElementua> kategoriaHautatzailea;
    private JComboBox<String> motaHautatzailea;
    private JComboBox<MenuLogistika.BiltegiElementua> biltegiHautatzaileaSarrera;
    private JTable lerroBerriTaula;
    private DefaultTableModel lerroBerriEredua;
    private JFileChooser fitxategiHautatzailea;

    public SarreraBerriaDialog(Frame parent, BiltegiLangilea langilea) {
        super(parent, "Sarrera Berria", true);
        this.langilea = langilea;
        pantailaPrestatu();
        sarreraHautatzaileakKargatu();
    }

    private void pantailaPrestatu() {
        setSize(900, 700);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formularioPanela = new JPanel(new BorderLayout());
        formularioPanela.setBorder(BorderFactory.createTitledBorder("Sarrera eta Produktu Berriaren Datuak"));

        // Hornitzaile atala
        JPanel hornitzailePanela = new JPanel(new GridLayout(2, 1));
        JPanel hornAukeratuPanela = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hornitzaileHautatzailea = new JComboBox<>();
        hornitzaileBerriaAukera = new JCheckBox("Hornitzaile Berria Sortu?");
        hornitzaileBerriaAukera.addActionListener(e -> hornitzaileModuaAldatu());
        hornAukeratuPanela.add(new JLabel("Hornitzailea Aukeratu: "));
        hornAukeratuPanela.add(hornitzaileHautatzailea);
        hornAukeratuPanela.add(hornitzaileBerriaAukera);

        JPanel hornBerriaPanela = new JPanel(new FlowLayout(FlowLayout.LEFT));
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

        // Produktu eremuak
        JPanel formPanela = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        produktuIzenaTestua = new JTextField(15);
        markaTestua = new JTextField(15);
        kategoriaHautatzailea = new JComboBox<>();
        motaHautatzailea = new JComboBox<>(new String[] { "Eramangarria", "Mahai-gainekoa", "Mugikorra", "Tableta",
                "Zerbitzaria", "Pantaila", "Softwarea" });
        biltegiHautatzaileaSarrera = new JComboBox<>();
        kantitateTestua = new JTextField(5);
        deskribapenaTestua = new JTextField(30);
        irudiaUrlTestua = new JTextField(20);
        egoeraOharraTestua = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanela.add(new JLabel("Prod. Izena:"), gbc);
        gbc.gridx = 1;
        formPanela.add(produktuIzenaTestua, gbc);
        gbc.gridx = 2;
        formPanela.add(new JLabel("Marka:"), gbc);
        gbc.gridx = 3;
        formPanela.add(markaTestua, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanela.add(new JLabel("Kategoria:"), gbc);
        gbc.gridx = 1;
        formPanela.add(kategoriaHautatzailea, gbc);
        gbc.gridx = 2;
        formPanela.add(new JLabel("Mota:"), gbc);
        gbc.gridx = 3;
        formPanela.add(motaHautatzailea, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanela.add(new JLabel("Biltegia:"), gbc);
        gbc.gridx = 1;
        formPanela.add(biltegiHautatzaileaSarrera, gbc);
        gbc.gridx = 2;
        formPanela.add(new JLabel("Kantitatea:"), gbc);
        gbc.gridx = 3;
        formPanela.add(kantitateTestua, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanela.add(new JLabel("Deskribapena:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanela.add(deskribapenaTestua, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanela.add(new JLabel("Irudia URL:"), gbc);
        JPanel irudiPanela = new JPanel(new BorderLayout());
        irudiPanela.add(irudiaUrlTestua, BorderLayout.CENTER);
        JButton igoBotoia = new JButton("Igo");
        igoBotoia.addActionListener(e -> igoIrudia());
        irudiPanela.add(igoBotoia, BorderLayout.EAST);
        gbc.gridx = 1;
        formPanela.add(irudiPanela, gbc);
        gbc.gridx = 2;
        formPanela.add(new JLabel("Oharra:"), gbc);
        gbc.gridx = 3;
        formPanela.add(egoeraOharraTestua, gbc);

        JButton gehituBotoia = new JButton("Gehitu Zerrendara +");
        gehituBotoia.addActionListener(e -> gehituLerroaTaulara());
        JPanel botoiHustuPanela = new JPanel();
        botoiHustuPanela.add(gehituBotoia);

        JPanel erdikoFormPanela = new JPanel(new BorderLayout());
        erdikoFormPanela.add(formPanela, BorderLayout.CENTER);
        erdikoFormPanela.add(botoiHustuPanela, BorderLayout.SOUTH);

        formularioPanela.add(erdikoFormPanela, BorderLayout.CENTER);
        add(formularioPanela, BorderLayout.NORTH);

        String[] zutabeIzenak = { "Izena", "Marka", "Kategoria", "Mota", "Biltegia", "Kantitatea", "Deskribapena",
                "Irudia", "Oharra" };
        lerroBerriEredua = new DefaultTableModel(zutabeIzenak, 0);
        lerroBerriTaula = new JTable(lerroBerriEredua);
        add(new JScrollPane(lerroBerriTaula), BorderLayout.CENTER);

        JButton gordeBotoia = new JButton("GORDE SARRERA ETA SORTU PRODUKTUAK");
        gordeBotoia.setBackground(new Color(0, 128, 0));
        gordeBotoia.setForeground(Color.WHITE);
        gordeBotoia.setFont(new Font("Arial", Font.BOLD, 14));
        gordeBotoia.addActionListener(e -> gordeSarreraOsoa());
        add(gordeBotoia, BorderLayout.SOUTH);
    }

    private void hornitzaileModuaAldatu() {
        boolean berriaDa = hornitzaileBerriaAukera.isSelected();
        hornitzaileBerriaGaitu(berriaDa);
        hornitzaileHautatzailea.setEnabled(!berriaDa);
    }

    private void hornitzaileBerriaGaitu(boolean gaitu) {
        izenaBerriaTestua.setEnabled(gaitu);
        postaBerriaTestua.setEnabled(gaitu);
        ifzBerriaTestua.setEnabled(gaitu);
    }

    private void sarreraHautatzaileakKargatu() {
        try (Connection konexioa = DB_Konexioa.konektatu()) {
            Statement sententzia = konexioa.createStatement();
            ResultSet rsH = sententzia.executeQuery("SELECT id_hornitzailea, izena_soziala FROM hornitzaileak");
            while (rsH.next())
                hornitzaileHautatzailea
                        .addItem(new MenuLogistika.HornitzaileElementua(rsH.getInt(1), rsH.getString(2)));
            ResultSet rsK = sententzia.executeQuery("SELECT id_kategoria, izena FROM produktu_kategoriak");
            while (rsK.next())
                kategoriaHautatzailea.addItem(new MenuLogistika.KategoriaElementua(rsK.getInt(1), rsK.getString(2)));
            ResultSet rsB = sententzia.executeQuery("SELECT id_biltegia, izena FROM biltegiak");
            while (rsB.next())
                biltegiHautatzaileaSarrera.addItem(new MenuLogistika.BiltegiElementua(rsB.getInt(1), rsB.getString(2)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gehituLerroaTaulara() {
        String izena = produktuIzenaTestua.getText();
        String marka = markaTestua.getText();
        MenuLogistika.KategoriaElementua kat = (MenuLogistika.KategoriaElementua) kategoriaHautatzailea
                .getSelectedItem();
        String mota = (String) motaHautatzailea.getSelectedItem();
        MenuLogistika.BiltegiElementua bilt = (MenuLogistika.BiltegiElementua) biltegiHautatzaileaSarrera
                .getSelectedItem();
        String kantiStr = kantitateTestua.getText();
        if (izena.isEmpty() || marka.isEmpty() || kantiStr.isEmpty() || kat == null || bilt == null) {
            JOptionPane.showMessageDialog(this, "Mesedez, bete produktuaren eremu guztiak.");
            return;
        }
        try {
            int kanti = Integer.parseInt(kantiStr);
            lerroBerriEredua.addRow(new Object[] { izena, marka, kat, mota, bilt, kanti, deskribapenaTestua.getText(),
                    irudiaUrlTestua.getText(), egoeraOharraTestua.getText() });
            produktuIzenaTestua.setText("");
            markaTestua.setText("");
            kantitateTestua.setText("");
            deskribapenaTestua.setText("");
            irudiaUrlTestua.setText("");
            egoeraOharraTestua.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Kantitateak zenbakia izan behar du.");
        }
    }

    private void igoIrudia() {
        if (fitxategiHautatzailea == null) {
            fitxategiHautatzailea = new JFileChooser();
            fitxategiHautatzailea.setFileFilter(
                    new javax.swing.filechooser.FileNameExtensionFilter("Irudiak", "jpg", "jpeg", "png"));
        }
        if (fitxategiHautatzailea.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fitxategiHautatzailea.getSelectedFile();
            try {
                String baseDir = "src/birtek_interfaze_grafikoa/irudiak/";
                File destFile = new File(new File(baseDir), selectedFile.getName().replaceAll("\\s+", "_"));
                if (!destFile.getParentFile().exists())
                    destFile.getParentFile().mkdirs();
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                irudiaUrlTestua.setText("irudiak/" + destFile.getName());
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
        try {
            int hornitzaileaId = -1;
            if (hornitzaileBerriaAukera.isSelected()) {
                hornitzaileaId = langilea.hornitzaileBerriaSortu(izenaBerriaTestua.getText(), ifzBerriaTestua.getText(),
                        postaBerriaTestua.getText());
            } else {
                MenuLogistika.HornitzaileElementua item = (MenuLogistika.HornitzaileElementua) hornitzaileHautatzailea
                        .getSelectedItem();
                if (item != null)
                    hornitzaileaId = item.id;
            }
            if (hornitzaileaId == -1)
                return;

            List<Produktua> produktuList = new ArrayList<>();
            List<SarreraLerroa> lerroList = new ArrayList<>();
            for (int i = 0; i < lerroBerriEredua.getRowCount(); i++) {
                MenuLogistika.KategoriaElementua kat = (MenuLogistika.KategoriaElementua) lerroBerriEredua.getValueAt(i,
                        2);
                MenuLogistika.BiltegiElementua bilt = (MenuLogistika.BiltegiElementua) lerroBerriEredua.getValueAt(i,
                        4);
                int kanti = (Integer) lerroBerriEredua.getValueAt(i, 5);
                Produktua p = new Produktua(0, hornitzaileaId, kat.id, (String) lerroBerriEredua.getValueAt(i, 0),
                        (String) lerroBerriEredua.getValueAt(i, 1), (String) lerroBerriEredua.getValueAt(i, 3),
                        (String) lerroBerriEredua.getValueAt(i, 6), (String) lerroBerriEredua.getValueAt(i, 7), bilt.id,
                        "Zehazteko", (String) lerroBerriEredua.getValueAt(i, 8), false, java.math.BigDecimal.ZERO,
                        kanti, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, null, null) {
                };
                produktuList.add(p);
                lerroList.add(new SarreraLerroa(0, 0, 0, kanti, "Bidean"));
            }
            langilea.produktuSarreraBerriaSortu(hornitzaileaId, produktuList, lerroList);
            JOptionPane.showMessageDialog(this, "Sarrera ondo sortu da.");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Errorea: " + e.getMessage());
        }
    }
}
