package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_Konexioa {

    // Konfigurazio datuak
    private static final String URLEA = "jdbc:mysql://localhost:3306/birtek_db";
    private static final String ERABILTZAILEA = "root";
    private static final String PASAHITZA = "1MG32025";

    /**
     * Datu_basera konektatzeko metodoa.
     * 
     * @return Connection objektua edo null errore bat egonez gero.
     */
    private static Connection instance = null;

    /**
     * Datu-basera konektatzeko metodoa (Singleton Eredua).
     * 
     * @return Connection objektua edo null errore bat egonez gero.
     */
    public static Connection konektatu() {
        try {
            if (instance == null || instance.isClosed()) {
                // Driver-a kargatu (aukerakoa JDBC 4.0+ bertsioetan, baina gomendagarria)
                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection(URLEA, ERABILTZAILEA, PASAHITZA);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver-a ez da aurkitu: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errorea datu-basera konektatzean: " + e.getMessage());
        }
        return instance;
    }
}
