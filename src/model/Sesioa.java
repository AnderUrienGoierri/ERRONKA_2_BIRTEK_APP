package model;

/**
 * Saioa hasi duen langilearen informazioa gordetzeko klasea.
 */
public class Sesioa {
    public static int idLangilea;
    public static String izena;
    public static String abizena;
    public static int sailaId;
    public static String sailaIzena;

    /**
     * Saioa garbitzeko (logout egitean adibidez).
     */
    public static void itxiSaioa() {
        idLangilea = 0;
        izena = null;
        abizena = null;
        sailaId = 0;
        sailaIzena = null;
    }
}
