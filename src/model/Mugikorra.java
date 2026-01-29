package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Mugikorra extends Produktua {
    private String pantailaTeknologia;
    private BigDecimal pantailaHazbeteak;
    private int biltegiratzeaGb;
    private int ramGb;
    private int kameraNagusaMp;
    private int bateriaMah;
    private String sistemaEragilea;
    private String sareak;

    public Mugikorra(int idProduktua, int hornitzaileId, int kategoriaId, String izena, String marka, String mota,
            String deskribapena, String irudiaUrl, Integer biltegiId, String produktuEgoera,
            String produktuEgoeraOharra, boolean salgai, BigDecimal salmentaPrezioa, int stock,
            BigDecimal eskaintza, BigDecimal zergakEhunekoa, Timestamp sortzeData, Timestamp eguneratzeData,
            String pantailaTeknologia, BigDecimal pantailaHazbeteak, int biltegiratzeaGb,
            int ramGb, int kameraNagusaMp, int bateriaMah, String sistemaEragilea, String sareak) {
        super(idProduktua, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl, biltegiId,
                produktuEgoera, produktuEgoeraOharra, salgai, salmentaPrezioa, stock, eskaintza, zergakEhunekoa,
                sortzeData, eguneratzeData);
        this.pantailaTeknologia = pantailaTeknologia;
        this.pantailaHazbeteak = pantailaHazbeteak;
        this.biltegiratzeaGb = biltegiratzeaGb;
        this.ramGb = ramGb;
        this.kameraNagusaMp = kameraNagusaMp;
        this.bateriaMah = bateriaMah;
        this.sistemaEragilea = sistemaEragilea;
        this.sareak = sareak;
    }

    public String getPantailaTeknologia() {
        return pantailaTeknologia;
    }

    public void setPantailaTeknologia(String pantailaTeknologia) {
        this.pantailaTeknologia = pantailaTeknologia;
    }

    public BigDecimal getPantailaHazbeteak() {
        return pantailaHazbeteak;
    }

    public void setPantailaHazbeteak(BigDecimal pantailaHazbeteak) {
        this.pantailaHazbeteak = pantailaHazbeteak;
    }

    public int getBiltegiratzeaGb() {
        return biltegiratzeaGb;
    }

    public void setBiltegiratzeaGb(int biltegiratzeaGb) {
        this.biltegiratzeaGb = biltegiratzeaGb;
    }

    public int getRamGb() {
        return ramGb;
    }

    public void setRamGb(int ramGb) {
        this.ramGb = ramGb;
    }

    public int getKameraNagusaMp() {
        return kameraNagusaMp;
    }

    public void setKameraNagusaMp(int kameraNagusaMp) {
        this.kameraNagusaMp = kameraNagusaMp;
    }

    public int getBateriaMah() {
        return bateriaMah;
    }

    public void setBateriaMah(int bateriaMah) {
        this.bateriaMah = bateriaMah;
    }

    public String getSistemaEragilea() {
        return sistemaEragilea;
    }

    public void setSistemaEragilea(String sistemaEragilea) {
        this.sistemaEragilea = sistemaEragilea;
    }

    public String getSareak() {
        return sareak;
    }

    public void setSareak(String sareak) {
        this.sareak = sareak;
    }
}
