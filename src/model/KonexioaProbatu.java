package model;

import db.DB_Konexioa;

import java.sql.Connection;

/**
 * KonexioaProbatu klasea.
 * Datu-baseko konexioa ondo dabilen egiaztatzeko erabilgarria den klasea.
 * Main metodo bat dauka konexioa probatzeko.
 */
public class KonexioaProbatu {

    /**
     * Main metodoa konexioa probatzeko.
     * 
     * @param args Komando lerroko argumentuak (ez dira erabiltzen).
     */
    public static void main(String[] args) {
        Connection konexioa = DB_Konexioa.konektatu();

        if (konexioa != null) {
            System.out.println("Konexioa ondo dabil.");
        } else {
            System.err.println("Ezin izan da konexioa ezarri.");
        }
    }
}
