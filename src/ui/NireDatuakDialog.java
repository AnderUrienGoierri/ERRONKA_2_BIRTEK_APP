package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.Herria;
import model.Langilea;

/**
 * NireDatuakDialog klasea.
 * Erabiltzaileak bere datu pertsonalak (Pasahitza, Hizkuntza, Herria,
 * Telefonoa, Helbidea) editatzeko elkarrizketa leihoa.
 */
public class NireDatuakDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JPasswordField pasahitzaField;
    private JComboBox<String> hizkuntzaBox;
    private JComboBox<Herria> herriaBox;
    private JButton herriaGehituBtn;
    private JTextField telefonoaField;
    private JTextField helbideaField;

    // Modeloa
    private Langilea langilea;

    /**
     * Create the dialog.
     */
    /**
     * Eraikitzailea.
     * 
     * @param parent   Guraso leihoa.
     * @param langilea Editatu beharreko langile objektua.
     */
    public NireDatuakDialog(JFrame parent, Langilea langilea) {
        super(parent, "Nire Datuak Editatu", true);
        this.langilea = langilea;

        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new GridLayout(0, 2, 10, 10));

        // --- PASAHITZA ---
        contentPanel.add(new JLabel("Pasahitza Berria:"));
        pasahitzaField = new JPasswordField();
        contentPanel.add(pasahitzaField);

        // --- HIZKUNTZA ---
        contentPanel.add(new JLabel("Hizkuntza:"));
        String[] hizkuntzak = { "Euskara", "Gaztelania", "Frantsesa", "Ingelesa" };
        hizkuntzaBox = new JComboBox<>(hizkuntzak);
        contentPanel.add(hizkuntzaBox);

        // --- HERRIA ---
        contentPanel.add(new JLabel("Herria:"));
        JPanel herriaPanela = new JPanel(new BorderLayout());
        herriaBox = new JComboBox<>();
        herriaGehituBtn = new JButton("+");
        herriaGehituBtn.setToolTipText("Gehitu Herria");
        herriaPanela.add(herriaBox, BorderLayout.CENTER);
        herriaPanela.add(herriaGehituBtn, BorderLayout.EAST);
        contentPanel.add(herriaPanela);

        // --- TELEFONOA ---
        contentPanel.add(new JLabel("Telefonoa:"));
        telefonoaField = new JTextField();
        contentPanel.add(telefonoaField);

        // --- HELBIDEA ---
        contentPanel.add(new JLabel("Helbidea:"));
        helbideaField = new JTextField();
        contentPanel.add(helbideaField);

        // --- LOGIKA ---
        herriaGehituBtn.addActionListener(e -> herriaBerriaGehitu());

        // Datuak kargatu
        hasierakoDatuakJarri();

        // --- BOTOIAK ---
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("Gorde Aldaketak");
        okButton.addActionListener(e -> gordeDatuak());
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Ezeztatu");
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);
    }

    /**
     * Hasierako datuak kargatu interfazean (DBtik freskatuz).
     */
    private void hasierakoDatuakJarri() {
        // DBtik datuak freskatu ziurtatzeko
        try (java.sql.Connection kon = db.DB_Konexioa.konektatu();
                java.sql.PreparedStatement pst = kon
                        .prepareStatement("SELECT * FROM langileak WHERE id_langilea = ?")) {
            pst.setInt(1, langilea.getIdLangilea());
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                pasahitzaField.setText(rs.getString("pasahitza"));
                hizkuntzaBox.setSelectedItem(rs.getString("hizkuntza"));
                telefonoaField.setText(rs.getString("telefonoa"));
                helbideaField.setText(rs.getString("helbidea"));

                // Update local object to match DB
                langilea.setPasahitza(rs.getString("pasahitza"));
                langilea.setHizkuntza(rs.getString("hizkuntza"));
                langilea.setTelefonoa(rs.getString("telefonoa"));
                langilea.setHelbidea(rs.getString("helbidea"));
                langilea.setHerriaId(rs.getInt("herria_id"));
            } else {
                // Fallback if DB fail (shouldn't happen)
                pasahitzaField.setText(langilea.getPasahitza());
                hizkuntzaBox.setSelectedItem(langilea.getHizkuntza());
                telefonoaField.setText(langilea.getTelefonoa());
                helbideaField.setText(langilea.getHelbidea());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback
            pasahitzaField.setText(langilea.getPasahitza());
            hizkuntzaBox.setSelectedItem(langilea.getHizkuntza());
            telefonoaField.setText(langilea.getTelefonoa());
            helbideaField.setText(langilea.getHelbidea());
        }

        // Herriak DBtik kargatu
        herriakKargatu();

        // Langilearen herria hautatu ID bidez
        int hId = langilea.getHerriaId();
        for (int i = 0; i < herriaBox.getItemCount(); i++) {
            if (herriaBox.getItemAt(i).getIdHerria() == hId) {
                herriaBox.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * Herrien zerrenda kargatu ComboBoxean.
     */
    private void herriakKargatu() {
        herriaBox.removeAllItems();
        if (langilea != null) {
            List<Herria> zerrenda = langilea.herriakLortu();
            for (Herria h : zerrenda) {
                herriaBox.addItem(h);
            }
        }
    }

    /**
     * Herri berria gehitzeko leihoa ireki eta sortu.
     */
    private void herriaBerriaGehitu() {
        JTextField izenaF = new JTextField();
        JTextField lurraldeaF = new JTextField();
        JTextField nazioaF = new JTextField();

        Object[] mezua = {
                "Herria:", izenaF,
                "Lurraldea:", lurraldeaF,
                "Nazioa:", nazioaF
        };

        int opt = JOptionPane.showConfirmDialog(this, mezua, "Gehitu Herria", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            if (izenaF.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Izena derrigorrezkoa da.");
                return;
            }
            try {
                Herria hBerria = new Herria(0, izenaF.getText(), lurraldeaF.getText(), nazioaF.getText());
                langilea.herriaSortu(hBerria);
                herriakKargatu();
                // Aukeratu berria
                for (int i = 0; i < herriaBox.getItemCount(); i++) {
                    Herria h = herriaBox.getItemAt(i);
                    if (h.getIzena().equalsIgnoreCase(izenaF.getText())) {
                        herriaBox.setSelectedIndex(i);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errorea herria sortzean: " + e.getMessage());
            }
        }
    }

    /**
     * Datuak gorde datu-basean.
     */
    private void gordeDatuak() {
        String pass = new String(pasahitzaField.getPassword());
        String hizkuntza = (String) hizkuntzaBox.getSelectedItem();
        Herria aukeratua = (Herria) herriaBox.getSelectedItem();
        String tlf = telefonoaField.getText().trim();
        String helbidea = helbideaField.getText().trim();

        if (pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pasahitza ezin da hutsik egon.");
            return;
        }
        if (aukeratua == null) {
            JOptionPane.showMessageDialog(this, "Herria aukeratu behar da.");
            return;
        }

        try {
            langilea.nireLangileDatuakEditatu(pass, hizkuntza, aukeratua.getIdHerria(), tlf, helbidea);
            JOptionPane.showMessageDialog(this, "Datuak ondo eguneratu dira.");
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errorea gordetzean: " + e.getMessage());
        }
    }
}
