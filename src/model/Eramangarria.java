package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Eramangarria extends Produktua {
    private String prozesadorea;
    private int ramGb;
    private int diskoaGb;
    private BigDecimal pantailaTamaina;
    private int bateriaWh;
    private String sistemaEragilea;
    private BigDecimal pisuaKg;

    public Eramangarria(int idProduktua, int hornitzaileId, int kategoriaId, String izena, String marka, String mota,
            String deskribapena, String irudiaUrl, Integer biltegiId, String produktuEgoera,
            String produktuEgoeraOharra, boolean salgai, BigDecimal salmentaPrezioa, int stock,
            BigDecimal eskaintza, BigDecimal zergakEhunekoa, Timestamp sortzeData, Timestamp eguneratzeData,
            String prozesadorea, int ramGb, int diskoaGb, BigDecimal pantailaTamaina,
            int bateriaWh, String sistemaEragilea, BigDecimal pisuaKg) {
        super(idProduktua, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl, biltegiId,
                produktuEgoera, produktuEgoeraOharra, salgai, salmentaPrezioa, stock, eskaintza, zergakEhunekoa,
                sortzeData, eguneratzeData);
        this.prozesadorea = prozesadorea;
        this.ramGb = ramGb;
        this.diskoaGb = diskoaGb;
        this.pantailaTamaina = pantailaTamaina;
        this.bateriaWh = bateriaWh;
        this.sistemaEragilea = sistemaEragilea;
        this.pisuaKg = pisuaKg;
    }

    public String getProzesadorea() {
        return prozesadorea;
    }

    public void setProzesadorea(String prozesadorea) {
        this.prozesadorea = prozesadorea;
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

    public BigDecimal getPantailaTamaina() {
        return pantailaTamaina;
    }

    public void setPantailaTamaina(BigDecimal pantailaTamaina) {
        this.pantailaTamaina = pantailaTamaina;
    }

    public int getBateriaWh() {
        return bateriaWh;
    }

    public void setBateriaWh(int bateriaWh) {
        this.bateriaWh = bateriaWh;
    }

    public String getSistemaEragilea() {
        return sistemaEragilea;
    }

    public void setSistemaEragilea(String sistemaEragilea) {
        this.sistemaEragilea = sistemaEragilea;
    }

    public BigDecimal getPisuaKg() {
        return pisuaKg;
    }

    public void setPisuaKg(BigDecimal pisuaKg) {
        this.pisuaKg = pisuaKg;
    }
}
