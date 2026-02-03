package model;

import java.sql.Timestamp;

/**
 * Sarrera klasea.
 * Produktuen sarrerak (hornitzaileengandik) kudeatzeko klasea.
 * Identifikatzaileak, datak eta egoera gordetzen ditu.
 */
public class Sarrera {
    private int idSarrera;
    private Timestamp data;
    private Timestamp eguneratzeData;
    private int hornitzaileaId;
    private int langileaId;
    private String sarreraEgoera;

    /**
     * Sarrera eraikitzailea.
     *
     * @param idSarrera      Sarreraren IDa.
     * @param data           Sarrera data.
     * @param eguneratzeData Eguneratze data.
     * @param hornitzaileaId Hornitzailearen IDa.
     * @param langileaId     Langilearen IDa (sarrera kudeatu duena).
     * @param sarreraEgoera  Sarreraren egoera.
     */
    public Sarrera(int idSarrera, Timestamp data, Timestamp eguneratzeData, int hornitzaileaId, int langileaId,
            String sarreraEgoera) {
        this.idSarrera = idSarrera;
        this.data = data;
        this.eguneratzeData = eguneratzeData;
        this.hornitzaileaId = hornitzaileaId;
        this.langileaId = langileaId;
        this.sarreraEgoera = sarreraEgoera;
    }

    /**
     * Sarreraren IDa lortzen du.
     * 
     * @return IDa.
     */
    public int getIdSarrera() {
        return idSarrera;
    }

    public void setIdSarrera(int idSarrera) {
        this.idSarrera = idSarrera;
    }

    /**
     * Sarrera data lortzen du.
     * 
     * @return Data.
     */
    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    /**
     * Eguneratze data lortzen du.
     * 
     * @return Eguneratze data.
     */
    public Timestamp getEguneratzeData() {
        return eguneratzeData;
    }

    public void setEguneratzeData(Timestamp eguneratzeData) {
        this.eguneratzeData = eguneratzeData;
    }

    /**
     * Hornitzailearen IDa lortzen du.
     * 
     * @return Hornitzailearen IDa.
     */
    public int getHornitzaileaId() {
        return hornitzaileaId;
    }

    public void setHornitzaileaId(int hornitzaileaId) {
        this.hornitzaileaId = hornitzaileaId;
    }

    /**
     * Langilearen IDa lortzen du.
     * 
     * @return Langilearen IDa.
     */
    public int getLangileaId() {
        return langileaId;
    }

    public void setLangileaId(int langileaId) {
        this.langileaId = langileaId;
    }

    /**
     * Sarreraren egoera lortzen du.
     * 
     * @return Egoera.
     */
    public String getSarreraEgoera() {
        return sarreraEgoera;
    }

    public void setSarreraEgoera(String sarreraEgoera) {
        this.sarreraEgoera = sarreraEgoera;
    }
}
