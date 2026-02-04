package model;

/**
 * ProduktuKategoria klasea.
 * Produktu kategorien informazioa kudeatzeko klasea.
 * Kategoriaren IDa eta izena gordetzen ditu.
 */
public class ProduktuKategoria {
    private int idKategoria;
    private String izena;

    /**
     * ProduktuKategoria eraikitzailea.
     *
     * @param idKategoria Kategoriaren IDa.
     * @param izena       Kategoriaren izena.
     */
    public ProduktuKategoria(int idKategoria, String izena) {
        this.idKategoria = idKategoria;
        this.izena = izena;
    }

    /**
     * Kategoriaren IDa lortzen du.
     * 
     * @return IDa.
     */
    public int getIdKategoria() {
        return idKategoria;
    }

    public void setIdKategoria(int idKategoria) {
        this.idKategoria = idKategoria;
    }

    /**
     * Kategoriaren izena lortzen du.
     * 
     * @return Izena.
     */
    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }
}
