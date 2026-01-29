package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Tableta extends Produktua {
    private BigDecimal pantailaHazbeteak;
    private int biltegiratzeaGb;
    private String konektibitatea;
    private String sistemaEragilea;
    private int bateriaMah;
    private boolean arkatzarekinBateragarria;

    public Tableta(int idProduktua, int hornitzaileId, int kategoriaId, String izena, String marka, String mota,
            String deskribapena, String irudiaUrl, Integer biltegiId, String produktuEgoera,
            String produktuEgoeraOharra, boolean salgai, BigDecimal salmentaPrezioa, int stock,
            BigDecimal eskaintza, BigDecimal zergakEhunekoa, Timestamp sortzeData, Timestamp eguneratzeData,
            BigDecimal pantailaHazbeteak, int biltegiratzeaGb, String konektibitatea,
            String sistemaEragilea, int bateriaMah, boolean arkatzarekinBateragarria) {
        super(idProduktua, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl, biltegiId,
                produktuEgoera, produktuEgoeraOharra, salgai, salmentaPrezioa, stock, eskaintza, zergakEhunekoa,
                sortzeData, eguneratzeData);
        this.pantailaHazbeteak = pantailaHazbeteak;
        this.biltegiratzeaGb = biltegiratzeaGb;
        this.konektibitatea = konektibitatea;
        this.sistemaEragilea = sistemaEragilea;
        this.bateriaMah = bateriaMah;
        this.arkatzarekinBateragarria = arkatzarekinBateragarria;
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

    public String getKonektibitatea() {
        return konektibitatea;
    }

    public void setKonektibitatea(String konektibitatea) {
        this.konektibitatea = konektibitatea;
    }

    public String getSistemaEragilea() {
        return sistemaEragilea;
    }

    public void setSistemaEragilea(String sistemaEragilea) {
        this.sistemaEragilea = sistemaEragilea;
    }

    public int getBateriaMah() {
        return bateriaMah;
    }

    public void setBateriaMah(int bateriaMah) {
        this.bateriaMah = bateriaMah;
    }

    public boolean isArkatzarekinBateragarria() {
        return arkatzarekinBateragarria;
    }

    public void setArkatzarekinBateragarria(boolean arkatzarekinBateragarria) {
        this.arkatzarekinBateragarria = arkatzarekinBateragarria;
    }
}
