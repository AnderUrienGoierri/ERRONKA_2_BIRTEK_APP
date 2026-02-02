package ui;

import db.DB_Konexioa;
import model.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class KonponketaXehetasunaElkarrizketa extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel edukiPanela = new JPanel();
    private JComboBox<String> egoeraHautatzailea;
    private JComboBox<ComboItem> akatsaHautatzailea;
    private JTextArea oharrakTestua;
    private int konponketaId;

    public KonponketaXehetasunaElkarrizketa(int id, String unekoEgoera, String unekoOharrak, int unekoAkatsaId,
            java.util.List<Akatsa> akatsakList) {
        this.konponketaId = id;
        setTitle(Hizkuntza.lortu("dialog_rep_title") + " (ID: " + id + ")");
        setBounds(100, 100, 450, 400); // Increased height
        getContentPane().setLayout(new BorderLayout());
        edukiPanela.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(edukiPanela, BorderLayout.CENTER);
        edukiPanela.setLayout(null);

        // Egoera
        JLabel egoeraEtiketa = new JLabel(Hizkuntza.lortu("status"));
        egoeraEtiketa.setBounds(20, 30, 80, 14);
        edukiPanela.add(egoeraEtiketa);

        String[] egoerak = { "Prozesuan", "Konponduta", "Konponezina" };
        egoeraHautatzailea = new JComboBox<>(egoerak);
        egoeraHautatzailea.setBounds(110, 27, 200, 22);
        egoeraHautatzailea.setSelectedItem(unekoEgoera);
        edukiPanela.add(egoeraHautatzailea);

        // Akatsa
        JLabel akatsaEtiketa = new JLabel("Akatsa:");
        akatsaEtiketa.setBounds(20, 70, 80, 14);
        edukiPanela.add(akatsaEtiketa);

        akatsaHautatzailea = new JComboBox<>();
        for (Akatsa a : akatsakList) {
            ComboItem item = new ComboItem(a.getIdAkatsa(), a.getIzena());
            akatsaHautatzailea.addItem(item);
            if (a.getIdAkatsa() == unekoAkatsaId) {
                akatsaHautatzailea.setSelectedItem(item);
            }
        }
        akatsaHautatzailea.setBounds(110, 67, 200, 22);
        edukiPanela.add(akatsaHautatzailea);

        // Oharrak
        JLabel oharrakEtiketa = new JLabel(Hizkuntza.lortu("notes"));
        oharrakEtiketa.setBounds(20, 120, 80, 14);
        edukiPanela.add(oharrakEtiketa);

        oharrakTestua = new JTextArea();
        oharrakTestua.setBounds(110, 115, 280, 100);
        oharrakTestua.setText(unekoOharrak);
        edukiPanela.add(oharrakTestua);

        // Botoiak
        JPanel botoiPanela = new JPanel();
        botoiPanela.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(botoiPanela, BorderLayout.SOUTH);

        JButton gordeBotoia = new JButton(Hizkuntza.lortu("save"));
        gordeBotoia.addActionListener(e -> gordeDatuak());
        botoiPanela.add(gordeBotoia);

        JButton utziBotoia = new JButton("Utzi");
        utziBotoia.addActionListener(e -> dispose());
        botoiPanela.add(utziBotoia);
    }

    private void gordeDatuak() {
        String egoeraBerria = (String) egoeraHautatzailea.getSelectedItem();
        ComboItem aukeratutakoAkatsa = (ComboItem) akatsaHautatzailea.getSelectedItem();
        String oharBerriak = oharrakTestua.getText();

        if (aukeratutakoAkatsa == null) {
            JOptionPane.showMessageDialog(this, "Akatsa aukeratu behar da.");
            return;
        }

        String galdera = "UPDATE konponketak SET konponketa_egoera = ?, akatsa_id = ?, oharrak = ? WHERE id_konponketa = ?";

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement sententziaPrestatua = konexioa.prepareStatement(galdera)) {

            sententziaPrestatua.setString(1, egoeraBerria);
            sententziaPrestatua.setInt(2, aukeratutakoAkatsa.getId());
            sententziaPrestatua.setString(3, oharBerriak);
            sententziaPrestatua.setInt(4, konponketaId);

            int emaitza = sententziaPrestatua.executeUpdate();
            if (emaitza > 0) {
                dispose();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errorea eguneratzean: " + ex.getMessage());
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
