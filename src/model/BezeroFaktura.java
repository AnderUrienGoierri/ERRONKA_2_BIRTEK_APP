package model;

import java.math.BigDecimal;
import java.sql.Date;

public class BezeroFaktura {
    private int idFaktura;
    private String fakturaZenbakia;
    private int eskaeraId;
    private Date data;
    private BigDecimal zergakEhunekoa;
    private String fitxategiaUrl;

    public BezeroFaktura(int idFaktura, String fakturaZenbakia, int eskaeraId, Date data, BigDecimal zergakEhunekoa,
                         String fitxategiaUrl) {
        this.idFaktura = idFaktura;
        this.fakturaZenbakia = fakturaZenbakia;
        this.eskaeraId = eskaeraId;
        this.data = data;
        this.zergakEhunekoa = zergakEhunekoa;
        this.fitxategiaUrl = fitxategiaUrl;
    }

    public int getIdFaktura() { return idFaktura; }
    public void setIdFaktura(int idFaktura) { this.idFaktura = idFaktura; }

    public String getFakturaZenbakia() { return fakturaZenbakia; }
    public void setFakturaZenbakia(String fakturaZenbakia) { this.fakturaZenbakia = fakturaZenbakia; }

    public int getEskaeraId() { return eskaeraId; }
    public void setEskaeraId(int eskaeraId) { this.eskaeraId = eskaeraId; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public BigDecimal getZergakEhunekoa() { return zergakEhunekoa; }
    public void setZergakEhunekoa(BigDecimal zergakEhunekoa) { this.zergakEhunekoa = zergakEhunekoa; }

    public String getFitxategiaUrl() { return fitxategiaUrl; }
    public void setFitxategiaUrl(String fitxategiaUrl) { this.fitxategiaUrl = fitxategiaUrl; }
}
