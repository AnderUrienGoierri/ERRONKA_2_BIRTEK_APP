package model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Fitxaketa klasea.
 * Langileen sarrera eta irteera fitxaketak kudeatzen ditu.
 * Fitxaketaren data, ordua, mota eta langilea gordetzen ditu.
 */
public class Fitxaketa {
    private int idFitxaketa;
    private int langileaId;
    private Date data;
    private Time ordua;
    private String mota;
    private Timestamp eguneratzeData;

    /**
     * Fitxaketa eraikitzailea.
     *
     * @param idFitxaketa    Fitxaketaren IDa.
     * @param langileaId     Langilearen IDa.
     * @param data           Data.
     * @param ordua          Ordua.
     * @param mota           Mota (Sarrera/Irteera).
     * @param eguneratzeData Eguneratze data.
     */
    public Fitxaketa(int idFitxaketa, int langileaId, Date data, Time ordua, String mota, Timestamp eguneratzeData) {
        this.idFitxaketa = idFitxaketa;
        this.langileaId = langileaId;
        this.data = data;
        this.ordua = ordua;
        this.mota = mota;
        this.eguneratzeData = eguneratzeData;
    }

    /**
     * Fitxaketaren IDa lortzen du.
     *
     * @return IDa.
     */
    public int getIdFitxaketa() {
        return idFitxaketa;
    }

    public void setIdFitxaketa(int idFitxaketa) {
        this.idFitxaketa = idFitxaketa;
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
     * Data lortzen du.
     *
     * @return Data.
     */
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    /**
     * Ordua lortzen du.
     *
     * @return Ordua.
     */
    public Time getOrdua() {
        return ordua;
    }

    public void setOrdua(Time ordua) {
        this.ordua = ordua;
    }

    /**
     * Fitxaketa mota lortzen du.
     *
     * @return Mota.
     */
    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
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
}
