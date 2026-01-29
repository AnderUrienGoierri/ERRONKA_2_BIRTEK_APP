package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class MahaiGainekoa extends Produktua {
    private String prozesadorea;
    private String plakaBasea;
    private int ramGb;
    private int diskoaGb;
    private String txartelGrafikoa;
    private int elikatzeIturriaW;
    private String kaxaFormatua;

    public MahaiGainekoa(int idProduktua, int hornitzaileId, int kategoriaId, String izena, String marka, String mota,
            String deskribapena, String irudiaUrl, Integer biltegiId, String produktuEgoera,
            String produktuEgoeraOharra, boolean salgai, BigDecimal salmentaPrezioa, int stock,
            BigDecimal eskaintza, BigDecimal zergakEhunekoa, Timestamp sortzeData, Timestamp eguneratzeData,
            String prozesadorea, String plakaBasea, int ramGb, int diskoaGb,
            String txartelGrafikoa, int elikatzeIturriaW, String kaxaFormatua) {
        super(idProduktua, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl, biltegiId,
                produktuEgoera, produktuEgoeraOharra, salgai, salmentaPrezioa, stock, eskaintza, zergakEhunekoa,
                sortzeData, eguneratzeData);
        this.prozesadorea = prozesadorea;
        this.plakaBasea = plakaBasea;
        this.ramGb = ramGb;
        this.diskoaGb = diskoaGb;
        this.txartelGrafikoa = txartelGrafikoa;
        this.elikatzeIturriaW = elikatzeIturriaW;
        this.kaxaFormatua = kaxaFormatua;
    }

    public String getProzesadorea() {
        return prozesadorea;
    }

    public void setProzesadorea(String prozesadorea) {
        this.prozesadorea = prozesadorea;
    }

    public String getPlakaBasea() {
        return plakaBasea;
    }

    public void setPlakaBasea(String plakaBasea) {
        this.plakaBasea = plakaBasea;
    }

    public int getRamGb() {
        return ramGb;
    }

    public void setRamGb(int ramGb) {
        this.ramGb = ramGb;
    }

    public int getDiskoaGb() {
        return diskoaGb;
    }

    public void setDiskoaGb(int diskoaGb) {
        this.diskoaGb = diskoaGb;
    }

    public String getTxartelGrafikoa() {
        return txartelGrafikoa;
    }

    public void setTxartelGrafikoa(String txartelGrafikoa) {
        this.txartelGrafikoa = txartelGrafikoa;
    }

    public int getElikatzeIturriaW() {
        return elikatzeIturriaW;
    }

    public void setElikatzeIturriaW(int elikatzeIturriaW) {
        this.elikatzeIturriaW = elikatzeIturriaW;
    }

    public String getKaxaFormatua() {
        return kaxaFormatua;
    }

    public void setKaxaFormatua(String kaxaFormatua) {
        this.kaxaFormatua = kaxaFormatua;
    }
}
