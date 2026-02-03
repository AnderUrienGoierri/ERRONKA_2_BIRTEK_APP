package ui;

import db.DB_Konexioa;
import model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.io.File;

/**
 * SaioaHastekoPanela klasea.
 * Aplikazioaren sarrera leihoa (Login).
 * Erabiltzaileak posta eta pasahitza sartuz saioa hasteko aukera ematen du.
 */
public class SaioaHastekoPanela extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel edukiPanela;

    // JComboBox erabiltzen dugu emailak aukeratzeko
    private JComboBox<String> postaEremua;

    private JPasswordField pasahitzaEremua;

    // Testu etiketak
    private JLabel izenburuEtiketa;
    private JLabel postaEtiketa;
    private JLabel pasahitzaEtiketa;
    private JLabel irudiEtiketa;
    private JButton saioaHasiBotoia;

    /**
     * SaioaHastekoPanela eraikitzailea.
     * Leihoaren osagaiak inizializatzen ditu.
     */
    public SaioaHastekoPanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Leihoa zabalagoa egin dugu elementuak ondo sartzeko
        setBounds(100, 100, 900, 400);

        edukiPanela = new JPanel();
        edukiPanela.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(edukiPanela);
        edukiPanela.setLayout(null);

        // --- IRUDIA (EZKERRALDEAN) ---
        irudiEtiketa = new JLabel("");
        irudiEtiketa.setBounds(0, 0, 350, 360);
        try {
            ImageIcon originalaIcon = null;
            // 1. Saiakera: Classpath bidez
            java.net.URL irundiURL = SaioaHastekoPanela.class.getResource("/birtek1.jpeg");

            if (irundiURL != null) {
                originalaIcon = new ImageIcon(irundiURL);
            } else {
                // 2. Saiakera: Fitxategi-sisteman zuzenean
                // ALDAKETA: Irudiak 'irudiak' karpetan daude
                if (new File("irudiak/birtek1.jpeg").exists()) {
                    originalaIcon = new ImageIcon("irudiak/birtek1.jpeg");
                }
            }

            if (originalaIcon != null) {
                Image img = originalaIcon.getImage();
                Image imgBerria = img.getScaledInstance(350, 360, Image.SCALE_SMOOTH);
                irudiEtiketa.setIcon(new ImageIcon(imgBerria));
            } else {
                throw new Exception("Irudia ez da aurkitu");
            }

        } catch (Exception e) {
            irudiEtiketa.setText("Irudia ez da aurkitu / No image found");
            irudiEtiketa.setHorizontalAlignment(SwingConstants.CENTER);
            irudiEtiketa.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            System.err.println("Errorea irudia kargatzean: Ziurtatu 'birtek1.jpeg' src karpetan dagoela.");
        }
        edukiPanela.add(irudiEtiketa);

        // --- LOGIN FORMULARIOA (ESKUINALDEAN) ---
        int desplazamendua = 360;

        // Titulua
        izenburuEtiketa = new JLabel();
        izenburuEtiketa.setFont(new Font("Tahoma", Font.BOLD, 18));
        izenburuEtiketa.setBounds(desplazamendua + 50, 50, 300, 30);
        edukiPanela.add(izenburuEtiketa);

        // Emaila
        postaEtiketa = new JLabel();
        postaEtiketa.setBounds(desplazamendua + 50, 110, 100, 14);
        edukiPanela.add(postaEtiketa);

        postaEremua = new JComboBox<>();
        postaEremua.setBounds(desplazamendua + 150, 107, 300, 25);
        postaEremua.setEditable(false);
        edukiPanela.add(postaEremua);

        // Datuak kargatu ComboBox-ean
        if (!java.beans.Beans.isDesignTime()) {
            postaKargatu();
        }

        // Pasahitza
        pasahitzaEtiketa = new JLabel();
        pasahitzaEtiketa.setBounds(desplazamendua + 50, 150, 100, 14);
        edukiPanela.add(pasahitzaEtiketa);

        pasahitzaEremua = new JPasswordField();
        pasahitzaEremua.setBounds(desplazamendua + 150, 147, 300, 25);
        edukiPanela.add(pasahitzaEremua);

        // Sartu Botoia
        saioaHasiBotoia = new JButton();
        saioaHasiBotoia.setBackground(new Color(0, 128, 128));
        saioaHasiBotoia.setForeground(new Color(0, 0, 0));
        saioaHasiBotoia.addActionListener(e -> saioaHasi());
        saioaHasiBotoia.setBounds(desplazamendua + 150, 200, 120, 30);
        edukiPanela.add(saioaHasiBotoia);

        // Testuak hasieratu
        eguneratuTestuak();
    }

    // METODOA posta-helbideak eta sailak kargatzeko
    /**
     * Posta helbideak eta sailak DBtik kargatu ComboBox-era.
     */
    private void postaKargatu() {
        String galdera = "SELECT l.emaila, s.izena AS saila_izena " +
                "FROM langileak l " +
                "JOIN langile_sailak s ON l.saila_id = s.id_saila " +
                "ORDER BY l.emaila ASC";

        Connection konexioa = DB_Konexioa.konektatu();
        if (konexioa == null) {
            postaEremua.addItem("Errorea: Ezin da konektatu DB-ra");
            return;
        }

        try (Statement stmt = konexioa.createStatement();
                ResultSet rs = stmt.executeQuery(galdera)) {

            postaEremua.removeAllItems();

            while (rs.next()) {
                String email = rs.getString("emaila");
                String saila = rs.getString("saila_izena");
                // "emaila (Saila)" formatua erakutsi
                postaEremua.addItem(email + " (" + saila + ")");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            postaEremua.addItem("Errorea datuak kargatzean");
        } finally {
            try {
                if (konexioa != null)
                    konexioa.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Testu etiketak eguneratu.
     */
    private void eguneratuTestuak() {
        setTitle("Saioa Hasi");
        izenburuEtiketa.setText("Birtek Kudeaketa");
        postaEtiketa.setText("Emaila:");
        pasahitzaEtiketa.setText("Pasahitza:");
        saioaHasiBotoia.setText("Sartu");
    }

    /**
     * Saioa hasteko prozesua.
     * Erabiltzailea eta pasahitza egiaztatzen ditu eta dagokion menua irekitzen du.
     */
    private void saioaHasi() {
        String aukeratutakoa = (String) postaEremua.getSelectedItem();

        if (aukeratutakoa == null || aukeratutakoa.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aukeratu erabiltzaile bat / Seleccione un usuario");
            return;
        }

        // "emaila (Saila)" formatutik emaila bakarrik atera
        String email = aukeratutakoa.split(" \\(")[0];

        String pasahitza = new String(pasahitzaEremua.getPassword());

        String galdera = "SELECT l.id_langilea, l.izena, l.abizena, l.saila_id, s.izena AS saila_izena " +
                "FROM langileak l " +
                "JOIN langile_sailak s ON l.saila_id = s.id_saila " +
                "WHERE l.emaila = ? AND l.pasahitza = ?";

        Connection konexioa = DB_Konexioa.konektatu();
        if (konexioa == null) {
            JOptionPane.showMessageDialog(null, "Errorea: Ezin da konektatu datu-basera (Driverra falta da?)", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PreparedStatement pst = konexioa.prepareStatement(galdera)) {
            pst.setString(1, email);
            pst.setString(2, pasahitza);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Saioaren datuak gorde
                Sesioa.idLangilea = rs.getInt("id_langilea");
                Sesioa.izena = rs.getString("izena");
                Sesioa.abizena = rs.getString("abizena");
                Sesioa.sailaId = rs.getInt("saila_id");
                Sesioa.sailaIzena = rs.getString("saila_izena");

                irekiSailMenua(Sesioa.sailaId);
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Login Error: Pasahitza okerra / Contraseña incorrecta", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errorea konexioan / Error de conexión");
        } finally {
            try {
                if (konexioa != null)
                    konexioa.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sailaren araberako menua ireki.
     * 
     * @param sailaId Sailaren IDa.
     */
    private void irekiSailMenua(int sailaId) {
        switch (sailaId) {
            case 1:
                new MenuZuzendaritza().setVisible(true);
                break;
            case 2:
                new MenuAdministrazioa().setVisible(true);
                break;
            case 3:
                new MenuSalmentak().setVisible(true);
                break;
            case 4:
                new MenuTeknikoa().setVisible(true);
                break;
            case 5:
                new MenuLogistika().setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(null, "ID Ezezaguna / Desconocido");
        }
    }
}
