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
    private JTextArea oharrakTestua;
    private int konponketaId;

    public KonponketaXehetasunaElkarrizketa(int id, String unekoEgoera, String unekoOharrak) {
        this.konponketaId = id;
        setTitle(Hizkuntza.lortu("dialog_rep_title") + " (ID: " + id + ")");
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        edukiPanela.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(edukiPanela, BorderLayout.CENTER);
        edukiPanela.setLayout(null);

        JLabel egoeraEtiketa = new JLabel(Hizkuntza.lortu("status"));
        egoeraEtiketa.setBounds(20, 30, 80, 14);
        edukiPanela.add(egoeraEtiketa);

        String[] egoerak = {"Zain", "Konpontzen", "Konponduta", "Ezin da konpondu"};
        egoeraHautatzailea = new JComboBox<>(egoerak);
        egoeraHautatzailea.setBounds(110, 27, 200, 22);
        egoeraHautatzailea.setSelectedItem(unekoEgoera);
        edukiPanela.add(egoeraHautatzailea);

        JLabel oharrakEtiketa = new JLabel(Hizkuntza.lortu("notes"));
        oharrakEtiketa.setBounds(20, 80, 80, 14);
        edukiPanela.add(oharrakEtiketa);

        oharrakTestua = new JTextArea();
        oharrakTestua.setBounds(110, 75, 280, 100);
        oharrakTestua.setText(unekoOharrak);
        edukiPanela.add(oharrakTestua);

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
        String oharBerriak = oharrakTestua.getText();

        String galdera = "UPDATE konponketak SET egoera = ?, oharrak = ? WHERE id_konponketa = ?";

        try (Connection konexioa = DB_Konexioa.konektatu();
             PreparedStatement sententziaPrestatua = konexioa.prepareStatement(galdera)) {

            sententziaPrestatua.setString(1, egoeraBerria);
            sententziaPrestatua.setString(2, oharBerriak);
            sententziaPrestatua.setInt(3, konponketaId);

            int emaitza = sententziaPrestatua.executeUpdate();
            if (emaitza > 0) {
                // JOptionPane.showMessageDialog(this, "Ongi eguneratu da!");
                dispose();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errorea eguneratzean.");
        }
    }
}
