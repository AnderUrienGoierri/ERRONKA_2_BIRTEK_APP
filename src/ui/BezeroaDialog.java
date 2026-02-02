package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.Bezeroa;
import model.Herria;
import model.SalmentaLangilea;

public class BezeroaDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JTextField izenaField;
    private JTextField abizenaField;
    private JTextField nanField;
    private JTextField jaiotzaDataField;
    private JComboBox<String> sexuaBox;
    private JTextField txartelaField;
    private JTextField helbideaField;

    // Herria kudeaketa
    private JComboBox<Herria> herriaBox;
    private JButton herriaGehituBtn;

    private JTextField postaKodeaField;
    private JTextField telefonoaField;
    private JTextField emailField;
    private JComboBox<String> hizkuntzaBox;
    private JTextField pasahitzaField;
    private JButton okButton;

    private boolean onartua = false;
    private Bezeroa bezeroa;
    private SalmentaLangilea langilea;

    /**
     * Create the dialog.
     */
    public BezeroaDialog(JFrame parent, String title, Bezeroa bezeroaEdizioa, SalmentaLangilea langilea) {
        super(parent, title, true);
        this.bezeroa = bezeroaEdizioa;
        this.langilea = langilea;

        setBounds(100, 100, 500, 550);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new GridLayout(0, 2, 5, 5));

        contentPanel.add(new JLabel("Izena / Soziala:"));
        izenaField = new JTextField();
        contentPanel.add(izenaField);

        contentPanel.add(new JLabel("Abizena:"));
        abizenaField = new JTextField();
        contentPanel.add(abizenaField);

        contentPanel.add(new JLabel("NAN / IFZ:"));
        nanField = new JTextField();
        contentPanel.add(nanField);

        contentPanel.add(new JLabel("Jaiotza Data (YYYY-MM-DD):"));
        jaiotzaDataField = new JTextField();
        contentPanel.add(jaiotzaDataField);

        contentPanel.add(new JLabel("Sexua:"));
        String[] sexuak = { "gizona", "emakumea", "ez-binarioa" };
        sexuaBox = new JComboBox<>(sexuak);
        contentPanel.add(sexuaBox);

        contentPanel.add(new JLabel("Ordainketa Txartela:"));
        txartelaField = new JTextField();
        contentPanel.add(txartelaField);

        contentPanel.add(new JLabel("Helbidea:"));
        helbideaField = new JTextField();
        contentPanel.add(helbideaField);

        // Herria ComboBox eta Botoia panel batean
        contentPanel.add(new JLabel("Herria:"));
        JPanel herriaPanela = new JPanel(new BorderLayout());
        herriaBox = new JComboBox<>();
        herriaGehituBtn = new JButton("+");
        herriaGehituBtn.setToolTipText("Gehitu Herri Berria");
        herriaPanela.add(herriaBox, BorderLayout.CENTER);
        herriaPanela.add(herriaGehituBtn, BorderLayout.EAST);
        contentPanel.add(herriaPanela);

        contentPanel.add(new JLabel("Posta Kodea:"));
        postaKodeaField = new JTextField();
        contentPanel.add(postaKodeaField);

        contentPanel.add(new JLabel("Telefonoa:"));
        telefonoaField = new JTextField();
        contentPanel.add(telefonoaField);

        contentPanel.add(new JLabel("Emaila:"));
        emailField = new JTextField();
        contentPanel.add(emailField);

        contentPanel.add(new JLabel("Hizkuntza:"));
        String[] hizkuntzak = { "Euskara", "Gaztelania", "Frantsesa", "Ingelesa" };
        hizkuntzaBox = new JComboBox<>(hizkuntzak);
        contentPanel.add(hizkuntzaBox);

        contentPanel.add(new JLabel("Pasahitza:"));
        pasahitzaField = new JTextField();
        contentPanel.add(pasahitzaField);

        // Herriak kargatu
        herriakKargatu();

        // Herria gehitu logika
        herriaGehituBtn.addActionListener(e -> herriaBerriaGehitu());

        if (bezeroa != null) {
            datuakKargatu();
        }

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        okButton = new JButton("Gorde");
        okButton.addActionListener(e -> {
            if (balidatu()) {
                onartua = true;
                setVisible(false);
            }
        });
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Ezeztatu");
        cancelButton.addActionListener(e -> {
            onartua = false;
            setVisible(false);
        });
        buttonPane.add(cancelButton);
    }

    private void herriakKargatu() {
        herriaBox.removeAllItems();
        if (langilea != null) {
            List<Herria> zerrenda = langilea.herriakLortu();
            for (Herria h : zerrenda) {
                herriaBox.addItem(h);
            }
        }
    }

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
                // Aukeratu berria (azkena izena arabera ordenatuta egon daiteke, beraz bilatu)
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

    private void datuakKargatu() {
        izenaField.setText(bezeroa.getIzenaEdoSoziala());
        abizenaField.setText(bezeroa.getAbizena());
        nanField.setText(bezeroa.getIfzNan());
        if (bezeroa.getJaiotzaData() != null) {
            jaiotzaDataField.setText(bezeroa.getJaiotzaData().toString());
        }
        sexuaBox.setSelectedItem(bezeroa.getSexua());
        txartelaField.setText(bezeroa.getBezeroOrdainketaTxartela());
        helbideaField.setText(bezeroa.getHelbidea());

        // Herria hautatu ID bidez
        int hId = bezeroa.getHerriaId();
        for (int i = 0; i < herriaBox.getItemCount(); i++) {
            if (herriaBox.getItemAt(i).getIdHerria() == hId) {
                herriaBox.setSelectedIndex(i);
                break;
            }
        }

        postaKodeaField.setText(bezeroa.getPostaKodea());
        telefonoaField.setText(bezeroa.getTelefonoa());
        emailField.setText(bezeroa.getEmaila());
        hizkuntzaBox.setSelectedItem(bezeroa.getHizkuntza());
        pasahitzaField.setText(bezeroa.getPasahitza());
    }

    private boolean balidatu() {
        if (izenaField.getText().trim().isEmpty() || nanField.getText().trim().isEmpty()
                || emailField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Izena, NAN eta Emaila derrigorrezkoak dira.", "Errorea",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (herriaBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Herria aukeratu behar da.", "Errorea", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean isOnartua() {
        return onartua;
    }

    public Bezeroa getBezeroa() {
        int id = (bezeroa != null) ? bezeroa.getIdBezeroa() : 0;
        Date jaiotza = null;
        try {
            if (!jaiotzaDataField.getText().trim().isEmpty()) {
                jaiotza = Date.valueOf(jaiotzaDataField.getText().trim());
            }
        } catch (Exception e) {
        } // Ignoratu formatu okerra

        Herria aukeratua = (Herria) herriaBox.getSelectedItem();
        int herriaId = (aukeratua != null) ? aukeratua.getIdHerria() : 0;

        return new Bezeroa(
                id,
                izenaField.getText().trim(),
                abizenaField.getText().trim(),
                nanField.getText().trim(),
                jaiotza,
                (String) sexuaBox.getSelectedItem(),
                txartelaField.getText().trim(),
                helbideaField.getText().trim(),
                herriaId,
                postaKodeaField.getText().trim(),
                telefonoaField.getText().trim(),
                emailField.getText().trim(),
                (String) hizkuntzaBox.getSelectedItem(),
                pasahitzaField.getText().trim(),
                null, null, true);
    }

    public void setViewMode(boolean viewMode) {
        izenaField.setEditable(!viewMode);
        abizenaField.setEditable(!viewMode);
        nanField.setEditable(!viewMode);
        jaiotzaDataField.setEditable(!viewMode);
        sexuaBox.setEnabled(!viewMode);
        txartelaField.setEditable(!viewMode);
        helbideaField.setEditable(!viewMode);
        herriaBox.setEnabled(!viewMode);
        herriaGehituBtn.setVisible(!viewMode);
        postaKodeaField.setEditable(!viewMode);
        telefonoaField.setEditable(!viewMode);
        emailField.setEditable(!viewMode);
        hizkuntzaBox.setEnabled(!viewMode);
        pasahitzaField.setEditable(!viewMode);

        if (okButton != null) {
            okButton.setVisible(!viewMode);
        }
    }
}
