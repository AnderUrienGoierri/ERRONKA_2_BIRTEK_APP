package model;

/**
 * LangileSaila klasea.
 * Langileen sailen informazioa kudeatzeko klasea.
 * Sailaren IDa, izena, kokapena eta deskribapena gordetzen ditu.
 */
public class LangileSaila {
    private int idSaila;
    private String izena;
    private String kokapena;
    private String deskribapena;

    /**
     * LangileSaila eraikitzailea.
     *
     * @param idSaila      Sailaren IDa.
     * @param izena        Sailaren izena.
     * @param kokapena     Sailaren kokapena.
     * @param deskribapena Sailaren deskribapena.
     */
    public LangileSaila(int idSaila, String izena, String kokapena, String deskribapena) {
        this.idSaila = idSaila;
        this.izena = izena;
        this.kokapena = kokapena;
        this.deskribapena = deskribapena;
    }

    /**
     * Sailaren IDa lortzen du.
     * 
     * @return IDa.
     */
    public int getIdSaila() {
        return idSaila;
    }

    public void setIdSaila(int idSaila) {
        this.idSaila = idSaila;
    }

    /**
     * Sailaren izena lortzen du.
     * 
     * @return Izena.
     */
    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    /**
     * Sailaren kokapena lortzen du.
     * 
     * @return Kokapena.
     */
    public String getKokapena() {
        return kokapena;
    }

    public void setKokapena(String kokapena) {
        this.kokapena = kokapena;
    }

    /**
     * Sailaren deskribapena lortzen du.
     * 
     * @return Deskribapena.
     */
    public String getDeskribapena() {
        return deskribapena;
    }

    public void setDeskribapena(String deskribapena) {
        this.deskribapena = deskribapena;
    }
}
