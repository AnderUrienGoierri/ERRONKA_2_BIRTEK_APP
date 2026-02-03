package ui;

import db.DB_Konexioa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * FitxaketakKudeatu klasea.
 * Langileen fitxaketak (sarrerak eta irteerak) kudeatzeko interfaze grafikoa.
 * Fitxaketak ikusi, sortu, editatu eta ezabatzeko aukera ematen du.
 */
public class FitxaketakKudeatu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel edukiPanela;
	private JTable taula;
	private DefaultTableModel eredua;

	private class Langilea {
		int id;
		String izena;
		String abizena;

		public Langilea(int id, String izena, String abizena) {
			this.id = id;
			this.izena = izena;
			this.abizena = abizena;
		}

		@Override
		public String toString() {
			return id + " - " + izena + " " + abizena;
		}
	}

	/**
	 * Applikazioa abiarazi.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FitxaketakKudeatu frame = new FitxaketakKudeatu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Leihoa sortu eta inizializatu.
	 */
	public FitxaketakKudeatu() {
		setTitle("Birtek - Langileen Fitxaketak Kudeatu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		edukiPanela = new JPanel();
		edukiPanela.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(edukiPanela);
		edukiPanela.setLayout(new BorderLayout(0, 0));

		// Goiko panela (Izenburua)
		JPanel goikoPanela = new JPanel();
		goikoPanela.setBackground(new Color(0, 102, 102));
		edukiPanela.add(goikoPanela, BorderLayout.NORTH);
		JLabel izenburuEtiketa = new JLabel("FITXAKETEN KUDEAKETA");
		izenburuEtiketa.setForeground(Color.WHITE);
		izenburuEtiketa.setFont(new Font("SansSerif", Font.BOLD, 18));
		goikoPanela.add(izenburuEtiketa);

		// Erdiko panela (Taula)
		JScrollPane korritzePanela = new JScrollPane();
		edukiPanela.add(korritzePanela, BorderLayout.CENTER);
		taula = new JTable();
		korritzePanela.setViewportView(taula);

		// Beheko panela (Botoiak)
		JPanel behekoPanela = new JPanel();
		edukiPanela.add(behekoPanela, BorderLayout.SOUTH);
		behekoPanela.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		JButton kargatuBotoia = new JButton("Kargatu");
		kargatuBotoia.addActionListener(e -> datuakKargatu());
		behekoPanela.add(kargatuBotoia);

		JButton berriaBotoia = new JButton("Berria");
		berriaBotoia.addActionListener(e -> fitxaketaBerria());
		behekoPanela.add(berriaBotoia);

		JButton aldatuBotoia = new JButton("Modifikatu");
		aldatuBotoia.addActionListener(e -> fitxaketaAldatu());
		behekoPanela.add(aldatuBotoia);

		JButton ezabatuBotoia = new JButton("Ezabatu");
		ezabatuBotoia.addActionListener(e -> fitxaketaEzabatu());
		behekoPanela.add(ezabatuBotoia);

		JButton irtenBotoia = new JButton("Atera");
		irtenBotoia.addActionListener(e -> dispose());
		behekoPanela.add(irtenBotoia);

		// Hasierako datuak kargatu
		datuakKargatu();
	}

	/**
	 * Datu-basetik fitxaketa datuak kargatu eta taulan erakutsi.
	 */
	private void datuakKargatu() {
		String galdera = "SELECT id_fitxaketa, langilea_id, data, CAST(ordua AS CHAR) AS ordua, mota FROM fitxaketak ORDER BY id_fitxaketa DESC";
		try (Connection konexioa = DB_Konexioa.konektatu();
				PreparedStatement sententziaPrestatua = konexioa.prepareStatement(galdera);
				ResultSet emaitza = sententziaPrestatua.executeQuery()) {

			eredua = TaulaModelatzailea.ereduaEraiki(emaitza);
			taula.setModel(eredua);

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Errorea datuak kargatzean: " + e.getMessage(), "Errorea",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Langileen zerrenda lortu ComboBox-ean erakusteko.
	 * 
	 * @return Langileen ComboBox eredua.
	 */
	private DefaultComboBoxModel<Langilea> langileakLortu() {
		DefaultComboBoxModel<Langilea> eredua = new DefaultComboBoxModel<>();
		String galdera = "SELECT id_langilea, izena, abizena FROM langileak ORDER BY id_langilea";
		try (Connection konexioa = DB_Konexioa.konektatu();
				PreparedStatement sententziaPrestatua = konexioa.prepareStatement(galdera);
				ResultSet emaitza = sententziaPrestatua.executeQuery()) {
			while (emaitza.next()) {
				eredua.addElement(new Langilea(
						emaitza.getInt("id_langilea"),
						emaitza.getString("izena"),
						emaitza.getString("abizena")));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Errorea langileak kargatzean: " + e.getMessage());
		}
		return eredua;
	}

	/**
	 * Fitxaketa berri bat sortzeko elkarrizketa-leihoa ireki eta datuak gorde.
	 */
	private void fitxaketaBerria() {
		JComboBox<Langilea> langileaHautatzailea = new JComboBox<>(langileakLortu());
		String[] aukerak = { "Sarrera", "Irteera" };
		JComboBox<String> motaHautatzailea = new JComboBox<>(aukerak);

		java.time.LocalDateTime orain = java.time.LocalDateTime.now();
		JTextField dataTestua = new JTextField(orain.toLocalDate().toString());
		JTextField orduaTestua = new JTextField(orain.toLocalTime().withNano(0).toString());

		Object[] mezua = {
				"Langilea:", langileaHautatzailea,
				"Mota:", motaHautatzailea,
				"Data (YYYY-MM-DD):", dataTestua,
				"Ordua (HH:MM:SS):", orduaTestua
		};

		int aukera = JOptionPane.showConfirmDialog(null, mezua, "Fitxaketa Berria", JOptionPane.OK_CANCEL_OPTION);
		if (aukera == JOptionPane.OK_OPTION && langileaHautatzailea.getSelectedItem() != null) {
			try (Connection konexioa = DB_Konexioa.konektatu()) {
				String galdera = "INSERT INTO fitxaketak (langilea_id, mota, data, ordua) VALUES (?, ?, ?, ?)";
				PreparedStatement sententziaPrestatua = konexioa.prepareStatement(galdera);
				Langilea hautatua = (Langilea) langileaHautatzailea.getSelectedItem();
				sententziaPrestatua.setInt(1, hautatua.id);
				sententziaPrestatua.setString(2, (String) motaHautatzailea.getSelectedItem());
				sententziaPrestatua.setString(3, dataTestua.getText());
				sententziaPrestatua.setString(4, orduaTestua.getText());

				if (sententziaPrestatua.executeUpdate() > 0) {
					// JOptionPane.showMessageDialog(this, "Fitxaketa ongi gehitu da.");
					datuakKargatu();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Errorea gordetzean: " + e.getMessage());
			}
		}
	}

	/**
	 * Hautatutako fitxaketa editatzeko elkarrizketa-leihoa ireki eta aldaketak
	 * gorde.
	 */
	private void fitxaketaAldatu() {
		int errenkada = taula.getSelectedRow();
		if (errenkada == -1) {
			JOptionPane.showMessageDialog(this, "Hautatu fitxaketa bat modifikatzeko.");
			return;
		}

		int id = Integer.parseInt(taula.getValueAt(errenkada, 0).toString());
		int langileaIdHeldua = Integer.parseInt(taula.getValueAt(errenkada, 1).toString());
		String dataHeldua = taula.getValueAt(errenkada, 2).toString();
		String orduaHeldua = taula.getValueAt(errenkada, 3).toString();
		String motaHautatua = taula.getValueAt(errenkada, 4).toString();

		JComboBox<Langilea> langileaHautatzailea = new JComboBox<>(langileakLortu());
		// Hautatutako langilea ComboBox-ean bilatu
		for (int i = 0; i < langileaHautatzailea.getItemCount(); i++) {
			if (langileaHautatzailea.getItemAt(i).id == langileaIdHeldua) {
				langileaHautatzailea.setSelectedIndex(i);
				break;
			}
		}

		String[] aukerak = { "Sarrera", "Irteera" };
		JComboBox<String> motaHautatzailea = new JComboBox<>(aukerak);
		motaHautatzailea.setSelectedItem(motaHautatua);

		JTextField dataTestua = new JTextField(dataHeldua);
		JTextField orduaTestua = new JTextField(orduaHeldua);

		Object[] mezua = {
				"Langilea:", langileaHautatzailea,
				"Mota:", motaHautatzailea,
				"Data (YYYY-MM-DD):", dataTestua,
				"Ordua (HH:MM:SS):", orduaTestua
		};

		int aukera = JOptionPane.showConfirmDialog(null, mezua, "Aldatu Fitxaketa", JOptionPane.OK_CANCEL_OPTION);
		if (aukera == JOptionPane.OK_OPTION && langileaHautatzailea.getSelectedItem() != null) {
			try (Connection konexioa = DB_Konexioa.konektatu()) {
				String galdera = "UPDATE fitxaketak SET langilea_id = ?, mota = ?, data = ?, ordua = ? WHERE id_fitxaketa = ?";
				PreparedStatement sententziaPrestatua = konexioa.prepareStatement(galdera);
				Langilea hautatua = (Langilea) langileaHautatzailea.getSelectedItem();
				sententziaPrestatua.setInt(1, hautatua.id);
				sententziaPrestatua.setString(2, (String) motaHautatzailea.getSelectedItem());
				sententziaPrestatua.setString(3, dataTestua.getText());
				sententziaPrestatua.setString(4, orduaTestua.getText());
				sententziaPrestatua.setInt(5, id);

				if (sententziaPrestatua.executeUpdate() > 0) {
					// JOptionPane.showMessageDialog(this, "Fitxaketa ongi eguneratu da.");
					datuakKargatu();
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Errorea eguneratzean: " + e.getMessage());
			}
		}
	}

	/**
	 * Hautatutako fitxaketa ezabatu.
	 */
	private void fitxaketaEzabatu() {
		int errenkada = taula.getSelectedRow();
		if (errenkada == -1) {
			JOptionPane.showMessageDialog(this, "Hautatu fitxaketa bat ezabatzeko.");
			return;
		}

		int id = Integer.parseInt(taula.getValueAt(errenkada, 0).toString());
		int berretsi = JOptionPane.showConfirmDialog(this, "Ziur al zaude fitxaketa hau ezabatu nahi duzula?",
				"Berretsi", JOptionPane.YES_NO_OPTION);

		if (berretsi == JOptionPane.YES_OPTION) {
			try (Connection konexioa = DB_Konexioa.konektatu()) {
				String galdera = "DELETE FROM fitxaketak WHERE id_fitxaketa = ?";
				PreparedStatement sententziaPrestatua = konexioa.prepareStatement(galdera);
				sententziaPrestatua.setInt(1, id);

				if (sententziaPrestatua.executeUpdate() > 0) {
					// JOptionPane.showMessageDialog(this, "Fitxaketa ezabatu da.");
					datuakKargatu();
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(this, "Errorea ezabatzean: " + e.getMessage());
			}
		}
	}
}
