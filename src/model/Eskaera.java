package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private String fakturaZenbakia;
    private String fakturaUrl;
    private String eskaeraEgoera;
    private List<EskaeraLerroa> eskaeraLerroak;

    /**
     * Eskaera eraikitzailea.
     *
     * @param idEskaera       Eskaeraren IDa.
     * @param bezeroaId       Bezeroaren IDa.
     * @param langileaId      Langilearen IDa (aukerakoa).
     * @param data            Eskaeraren data.
     * @param eguneratzeData  Azken eguneratze data.
     * @param guztiraPrezioa  Eskaeraren prezio totala.
     * @param fakturaZenbakia Fakturaren zenbakia (bakarra).
     * @param fakturaUrl      Fakturaren URL edo bidea.
     * @param eskaeraEgoera   Eskaeraren egoera.
     */
    public Eskaera(int idEskaera, int bezeroaId, Integer langileaId, Timestamp data, Timestamp eguneratzeData,
            BigDecimal guztiraPrezioa, String fakturaZenbakia, String fakturaUrl, String eskaeraEgoera) {
        this.idEskaera = idEskaera;
        this.bezeroaId = bezeroaId;
        this.langileaId = langileaId;
        this.data = data;
        this.eguneratzeData = eguneratzeData;
        this.guztiraPrezioa = guztiraPrezioa;
        this.fakturaZenbakia = fakturaZenbakia;
        this.fakturaUrl = fakturaUrl;
        this.eskaeraEgoera = eskaeraEgoera;
        this.eskaeraLerroak = new ArrayList<>();
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

    public String getFakturaZenbakia() {
        return fakturaZenbakia;
    }

    public void setFakturaZenbakia(String fakturaZenbakia) {
        this.fakturaZenbakia = fakturaZenbakia;
    }

    public String getFakturaUrl() {
        return fakturaUrl;
    }

    public void setFakturaUrl(String fakturaUrl) {
        this.fakturaUrl = fakturaUrl;
    }

    public String getEskaeraEgoera() {
        return eskaeraEgoera;
    }

    public void setEskaeraEgoera(String eskaeraEgoera) {
        this.eskaeraEgoera = eskaeraEgoera;
    }

    /**
     * Eskaeraren lerroen zerrenda lortzen du.
     *
     * @return EskaeraLerroa objektuen zerrenda.
     */
    public List<EskaeraLerroa> getEskaeraLerroak() {
        return eskaeraLerroak;
    }

    public void setEskaeraLerroak(List<EskaeraLerroa> eskaeraLerroak) {
        this.eskaeraLerroak = eskaeraLerroak;
    }

    /**
     * Lerro berri bat gehitzen dio eskaerari.
     *
     * @param lerroa Gehituko den eskaera lerroa.
     */
    public void addEskaeraLerroa(EskaeraLerroa lerroa) {
        this.eskaeraLerroak.add(lerroa);
    }
}
