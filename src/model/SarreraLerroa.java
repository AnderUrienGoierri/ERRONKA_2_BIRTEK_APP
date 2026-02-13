package model;

/**
 * SarreraLerroa klasea.
 * Sarrera baten lerro bakoitza irudikatzen du (produktu bat eta kantitatea).
 */
public class SarreraLerroa {
    private int idSarreraLerroa;
    private int sarreraId;
    private Integer produktuaId; // Changed to Integer to allow null
    private int kantitatea;
    private String sarreraLerroEgoera;
    private String produktuBerriaDatuak; // JSON data

    /**
     * SarreraLerroa eraikitzailea.
     *
     * @param idSarreraLerroa      Lerroaren IDa.
     * @param sarreraId            Sarreraren IDa.
     * @param produktuaId          Produktuaren IDa (null izan daiteke).
     * @param kantitatea           Kantitatea.
     * @param sarreraLerroEgoera   Lerroaren egoera.
     * @param produktuBerriaDatuak Produktu berriaren datuak JSON formatuan (null
     *                             izan daiteke).
     */
    public SarreraLerroa(int idSarreraLerroa, int sarreraId, Integer produktuaId, int kantitatea,
            String sarreraLerroEgoera, String produktuBerriaDatuak) {
        this.idSarreraLerroa = idSarreraLerroa;
        this.sarreraId = sarreraId;
        this.produktuaId = produktuaId;
        this.kantitatea = kantitatea;
        this.sarreraLerroEgoera = sarreraLerroEgoera;
        this.produktuBerriaDatuak = produktuBerriaDatuak;
    }

    /**
     * Lerroaren IDa lortzen du.
     *
     * @return IDa.
     */
    public int getIdSarreraLerroa() {
        return idSarreraLerroa;
    }

    public void setIdSarreraLerroa(int idSarreraLerroa) {
        this.idSarreraLerroa = idSarreraLerroa;
    }

    /**
     * Sarreraren IDa lortzen du.
     *
     * @return Sarreraren IDa.
     */
    public int getSarreraId() {
        return sarreraId;
    }

    public void setSarreraId(int sarreraId) {
        this.sarreraId = sarreraId;
    }

    /**
     * Produktuaren IDa lortzen du.
     *
     * @return Produktuaren IDa (Integer, null izan daiteke).
     */
    public Integer getProduktuaId() {
        return produktuaId;
    }

    public void setProduktuaId(Integer produktuaId) {
        this.produktuaId = produktuaId;
    }

    /**
     * Kantitatea lortzen du.
     *
     * @return Kantitatea.
     */
    public int getKantitatea() {
        return kantitatea;
    }

    public void setKantitatea(int kantitatea) {
        this.kantitatea = kantitatea;
    }

    /**
     * Lerroaren egoera lortzen du.
     *
     * @return Egoera.
     */
    public String getSarreraLerroEgoera() {
        return sarreraLerroEgoera;
    }

    public void setSarreraLerroEgoera(String sarreraLerroEgoera) {
        this.sarreraLerroEgoera = sarreraLerroEgoera;
    }

    /**
     * Produktuaren JSON datuak lortzen du.
     *
     * @return JSON string-a.
     */
    public String getProduktuBerriaDatuak() {
        return produktuBerriaDatuak;
    }

    public void setProduktuBerriaDatuak(String produktuBerriaDatuak) {
        this.produktuBerriaDatuak = produktuBerriaDatuak;
    }
}
