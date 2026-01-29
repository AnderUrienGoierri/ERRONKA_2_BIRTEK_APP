package model;
import db.DB_Konexioa;

import java.sql.Connection;

public class KonexioaProbatu {

    public static void main(String[] args) {
        Connection konexioa = DB_Konexioa.konektatu();
        
        if (konexioa != null) {
            System.out.println("Konexioa ondo dabil.");
        } else {
            System.err.println("Ezin izan da konexioa ezarri.");
        }
    }
}
