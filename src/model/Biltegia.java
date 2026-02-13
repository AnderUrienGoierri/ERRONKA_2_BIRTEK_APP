package model;

/**
 * Biltegia klasea.
 * Biltegien informazioa kudeatzeko klasea.
 * Biltegiaren IDa, izena eta SKU kodea gordetzen ditu.
 */
public class Biltegia {
    private int idBiltegia;
    private String izena;
    private String biltegiSku;

    /**
     * Biltegia eraikitzailea.
     *
     * @param idBiltegia Biltegiaren IDa.
     * @param izena      Biltegiaren izena.
     * @param biltegiSku Biltegiaren SKU kodea.
     */
    public Biltegia(int idBiltegia, String izena, String biltegiSku) {
        this.idBiltegia = idBiltegia;
        this.izena = izena;
        this.biltegiSku = biltegiSku;
    }

    /**
     * Biltegiaren IDa lortzen du.
     *
     * @return IDa.
     */
    public int getIdBiltegia() {
        return idBiltegia;
    }

    public void setIdBiltegia(int idBiltegia) {
        this.idBiltegia = idBiltegia;
    }

    /**
     * Biltegiaren izena lortzen du.
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
     * Biltegiaren SKU kodea lortzen du.
     *
     * @return SKU kodea.
     */
    public String getBiltegiSku() {
        return biltegiSku;
    }

    public void setBiltegiSku(String biltegiSku) {
        this.biltegiSku = biltegiSku;
    }
}
