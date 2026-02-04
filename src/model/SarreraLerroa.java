package model;

/**
 * SarreraLerroa klasea.
 * Sarrera baten lerro bakoitza irudikatzen du (produktu bat eta kantitatea).
 */
public class SarreraLerroa {
    private int idSarreraLerroa;
    private int sarreraId;
    private int produktuaId;
    private int kantitatea;
    private String sarreraLerroEgoera;

    /**
     * SarreraLerroa eraikitzailea.
     *
     * @param idSarreraLerroa    Lerroaren IDa.
     * @param sarreraId          Sarreraren IDa.
     * @param produktuaId        Produktuaren IDa.
     * @param kantitatea         Kantitatea.
     * @param sarreraLerroEgoera Lerroaren egoera.
     */
    public SarreraLerroa(int idSarreraLerroa, int sarreraId, int produktuaId, int kantitatea,
            String sarreraLerroEgoera) {
        this.idSarreraLerroa = idSarreraLerroa;
        this.sarreraId = sarreraId;
        this.produktuaId = produktuaId;
        this.kantitatea = kantitatea;
        this.sarreraLerroEgoera = sarreraLerroEgoera;
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
     * @return Produktuaren IDa.
     */
    public int getProduktuaId() {
        return produktuaId;
    }

    public void setProduktuaId(int produktuaId) {
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
}
