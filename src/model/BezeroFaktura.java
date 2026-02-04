package model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * BezeroFaktura klasea.
 * Bezeroen fakturen informazioa gordetzeko klasea.
 * Fakturaren zenbakia, data, zenbatekoa eta fitxategiaren helbidea kudeatzen
 * ditu.
 */
public class BezeroFaktura {
    private int idFaktura;
    private String fakturaZenbakia;
    private int eskaeraId;
    private Date data;
    private BigDecimal zergakEhunekoa;
    private String fitxategiaUrl;

    /**
     * BezeroFaktura eraikitzailea.
     *
     * @param idFaktura       Fakturaren IDa.
     * @param fakturaZenbakia Fakturaren zenbakia.
     * @param eskaeraId       Eskaeraren IDa.
     * @param data            Fakturaren data.
     * @param zergakEhunekoa  Zergen ehunekoa.
     * @param fitxategiaUrl   Faktura fitxategiaren URL edo bidea.
     */
    public BezeroFaktura(int idFaktura, String fakturaZenbakia, int eskaeraId, Date data, BigDecimal zergakEhunekoa,
            String fitxategiaUrl) {
        this.idFaktura = idFaktura;
        this.fakturaZenbakia = fakturaZenbakia;
        this.eskaeraId = eskaeraId;
        this.data = data;
        this.zergakEhunekoa = zergakEhunekoa;
        this.fitxategiaUrl = fitxategiaUrl;
    }

    /**
     * Fakturaren IDa lortzen du.
     * 
     * @return IDa.
     */
    public int getIdFaktura() {
        return idFaktura;
    }

    public void setIdFaktura(int idFaktura) {
        this.idFaktura = idFaktura;
    }

    /**
     * Faktura zenbakia lortzen du.
     * 
     * @return Faktura zenbakia.
     */
    public String getFakturaZenbakia() {
        return fakturaZenbakia;
    }

    public void setFakturaZenbakia(String fakturaZenbakia) {
        this.fakturaZenbakia = fakturaZenbakia;
    }

    /**
     * Eskaeraren IDa lortzen du.
     * 
     * @return Eskaeraren IDa.
     */
    public int getEskaeraId() {
        return eskaeraId;
    }

    public void setEskaeraId(int eskaeraId) {
        this.eskaeraId = eskaeraId;
    }

    /**
     * Fakturaren data lortzen du.
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
     * Zergen ehunekoa lortzen du.
     * 
     * @return Zergen ehunekoa.
     */
    public BigDecimal getZergakEhunekoa() {
        return zergakEhunekoa;
    }

    public void setZergakEhunekoa(BigDecimal zergakEhunekoa) {
        this.zergakEhunekoa = zergakEhunekoa;
    }

    /**
     * Faktura fitxategiaren URLa lortzen du.
     * 
     * @return Fitxategiaren URLa.
     */
    public String getFitxategiaUrl() {
        return fitxategiaUrl;
    }

    public void setFitxategiaUrl(String fitxategiaUrl) {
        this.fitxategiaUrl = fitxategiaUrl;
    }
}
