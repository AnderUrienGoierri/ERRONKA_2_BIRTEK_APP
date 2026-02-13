package model;

/**
 * Herria klasea.
 * Herrien informazioa kudeatzeko klasea.
 * Herriaren izena, lurraldea eta nazioa gordetzen ditu.
 */
public class Herria {
    private int idHerria;
    private String izena;
    private String lurraldea;
    private String nazioa;

    /**
     * Herria eraikitzailea.
     *
     * @param idHerria  Herriaren IDa.
     * @param izena     Herriaren izena.
     * @param lurraldea Probintzia edo lurraldea.
     * @param nazioa    Nazioa edo herrialdea.
     */
    public Herria(int idHerria, String izena, String lurraldea, String nazioa) {
        this.idHerria = idHerria;
        this.izena = izena;
        this.lurraldea = lurraldea;
        this.nazioa = nazioa;
    }

    /**
     * Herriaren IDa lortzen du.
     *
     * @return IDa.
     */
    public int getIdHerria() {
        return idHerria;
    }

    /**
     * Herriaren IDa ezartzen du.
     *
     * @param idHerria ID berria.
     */
    public void setIdHerria(int idHerria) {
        this.idHerria = idHerria;
    }

    /**
     * Herriaren izena lortzen du.
     *
     * @return Izena.
     */
    public String getIzena() {
        return izena;
    }

    /**
     * Herriaren izena ezartzen du.
     *
     * @param izena Izen berria.
     */
    public void setIzena(String izena) {
        this.izena = izena;
    }

    /**
     * Lurraldea lortzen du.
     *
     * @return Lurraldea.
     */
    public String getLurraldea() {
        return lurraldea;
    }

    /**
     * Lurraldea ezartzen du.
     *
     * @param lurraldea Lurralde berria.
     */
    public void setLurraldea(String lurraldea) {
        this.lurraldea = lurraldea;
    }

    /**
     * Nazioa lortzen du.
     *
     * @return Nazioa.
     */
    public String getNazioa() {
        return nazioa;
    }

    /**
     * Nazioa ezartzen du.
     *
     * @param nazioa Nazio berria.
     */
    public void setNazioa(String nazioa) {
        this.nazioa = nazioa;
    }

    /**
     * Herriaren izena itzultzen du String bezala.
     *
     * @return Herriaren izena.
     */
    @Override
    public String toString() {
        return izena;
    }
}
