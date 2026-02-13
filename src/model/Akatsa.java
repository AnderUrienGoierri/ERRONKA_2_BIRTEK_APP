package model;

/**
 * Akatsa klasea.
 * Sisteman gertatzen diren akatsak edo erroreak kudeatzeko klasea.
 * Akatsaren IDa, izena eta deskribapena gordetzen ditu.
 */
public class Akatsa {
    private int idAkatsa;
    private String izena;
    private String deskribapena;

    /**
     * Akatsa eraikitzailea.
     *
     * @param idAkatsa     Akatsaren IDa.
     * @param izena        Akatsaren izena.
     * @param deskribapena Akatsaren deskribapena.
     */
    public Akatsa(int idAkatsa, String izena, String deskribapena) {
        this.idAkatsa = idAkatsa;
        this.izena = izena;
        this.deskribapena = deskribapena;
    }

    /**
     * Akatsaren IDa lortzen du.
     *
     * @return Akatsaren IDa.
     */
    public int getIdAkatsa() {
        return idAkatsa;
    }

    /**
     * Akatsaren IDa ezartzen du.
     *
     * @param idAkatsa Akatsaren ID berria.
     */
    public void setIdAkatsa(int idAkatsa) {
        this.idAkatsa = idAkatsa;
    }

    /**
     * Akatsaren izena lortzen du.
     *
     * @return Akatsaren izena.
     */
    public String getIzena() {
        return izena;
    }

    /**
     * Akatsaren izena ezartzen du.
     *
     * @param izena Akatsaren izen berria.
     */
    public void setIzena(String izena) {
        this.izena = izena;
    }

    /**
     * Akatsaren deskribapena lortzen du.
     *
     * @return Akatsaren deskribapena.
     */
    public String getDeskribapena() {
        return deskribapena;
    }

    /**
     * Akatsaren deskribapena ezartzen du.
     *
     * @param deskribapena Akatsaren deskribapen berria.
     */
    public void setDeskribapena(String deskribapena) {
        this.deskribapena = deskribapena;
    }
}
