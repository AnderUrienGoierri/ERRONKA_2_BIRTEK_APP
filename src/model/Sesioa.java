package model;

/**
 * Saioa kudeatzeko klase estatikoa.
 * Uneko saioan logeatu den langilearen informazioa gordetzen du.
 */
public class Sesioa {
    public static int idLangilea;
    public static String izena;
    public static String abizena;
    public static int sailaId;
    public static String sailaIzena;

    /**
     * Saioa ixten du.
     * Gordetako datu guztiak ezabatzen ditu (null edo 0 ezarriz).
     */
    public static void itxiSaioa() {
        idLangilea = 0;
        izena = null;
        abizena = null;
        sailaId = 0;
        sailaIzena = null;
    }
}
