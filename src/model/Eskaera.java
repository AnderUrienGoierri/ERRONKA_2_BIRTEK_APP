package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Eskaera klasea.
 * Bezeroen eskaeren informazioa kudeatzen du.
 * Eskaeraren data, prezioa, egoera eta lotutako bezero/langilea gordetzen ditu.
 */
public class Eskaera {
    private int idEskaera;
    private int bezeroaId;
    private Integer langileaId;
    private Timestamp data;
    private Timestamp eguneratzeData;
    private BigDecimal guztiraPrezioa;
    private String eskaeraEgoera;

    /**
     * Eskaera eraikitzailea.
     *
     * @param idEskaera      Eskaeraren IDa.
     * @param bezeroaId      Bezeroaren IDa.
     * @param langileaId     Langilearen IDa (aukerakoa).
     * @param data           Eskaeraren data.
     * @param eguneratzeData Azken eguneratze data.
     * @param guztiraPrezioa Eskaeraren prezio totala.
     * @param eskaeraEgoera  Eskaeraren egoera.
     */
    public Eskaera(int idEskaera, int bezeroaId, Integer langileaId, Timestamp data, Timestamp eguneratzeData,
            BigDecimal guztiraPrezioa, String eskaeraEgoera) {
        this.idEskaera = idEskaera;
        this.bezeroaId = bezeroaId;
        this.langileaId = langileaId;
        this.data = data;
        this.eguneratzeData = eguneratzeData;
        this.guztiraPrezioa = guztiraPrezioa;
        this.eskaeraEgoera = eskaeraEgoera;
    }

    public int getIdEskaera() {
        return idEskaera;
    }

    public void setIdEskaera(int idEskaera) {
        this.idEskaera = idEskaera;
    }

    public int getBezeroaId() {
        return bezeroaId;
    }

    public void setBezeroaId(int bezeroaId) {
        this.bezeroaId = bezeroaId;
    }

    public Integer getLangileaId() {
        return langileaId;
    }

    public void setLangileaId(Integer langileaId) {
        this.langileaId = langileaId;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public Timestamp getEguneratzeData() {
        return eguneratzeData;
    }

    public void setEguneratzeData(Timestamp eguneratzeData) {
        this.eguneratzeData = eguneratzeData;
    }

    public BigDecimal getGuztiraPrezioa() {
        return guztiraPrezioa;
    }

    public void setGuztiraPrezioa(BigDecimal guztiraPrezioa) {
        this.guztiraPrezioa = guztiraPrezioa;
    }

    public String getEskaeraEgoera() {
        return eskaeraEgoera;
    }

    public void setEskaeraEgoera(String eskaeraEgoera) {
        this.eskaeraEgoera = eskaeraEgoera;
    }

    public EskaeraEgoera getEgoera() {
        return EskaeraEgoera.fromString(this.eskaeraEgoera);
    }

    public void setEgoera(EskaeraEgoera egoera) {
        this.eskaeraEgoera = egoera.getDeskribapena();
    }
}
