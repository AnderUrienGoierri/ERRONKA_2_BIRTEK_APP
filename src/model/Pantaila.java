package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Pantaila extends Produktua {
    private BigDecimal hazbeteak;
    private String bereizmena;
    private String panelMota;
    private int freskatzeTasaHz;
    private String konexioak;
    private String kurbatura;

    public Pantaila(int idProduktua, int hornitzaileId, int kategoriaId, String izena, String marka, String mota,
            String deskribapena, String irudiaUrl, Integer biltegiId, String produktuEgoera,
            String produktuEgoeraOharra, boolean salgai, BigDecimal salmentaPrezioa, int stock,
            BigDecimal eskaintza, BigDecimal zergakEhunekoa, Timestamp sortzeData, Timestamp eguneratzeData,
            BigDecimal hazbeteak, String bereizmena, String panelMota, int freskatzeTasaHz,
            String konexioak, String kurbatura) {
        super(idProduktua, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl, biltegiId,
                produktuEgoera, produktuEgoeraOharra, salgai, salmentaPrezioa, stock, eskaintza, zergakEhunekoa,
                sortzeData, eguneratzeData);
        this.hazbeteak = hazbeteak;
        this.bereizmena = bereizmena;
        this.panelMota = panelMota;
        this.freskatzeTasaHz = freskatzeTasaHz;
        this.konexioak = konexioak;
        this.kurbatura = kurbatura;
    }

    public BigDecimal getHazbeteak() {
        return hazbeteak;
    }

    public void setHazbeteak(BigDecimal hazbeteak) {
        this.hazbeteak = hazbeteak;
    }

    public String getBereizmena() {
        return bereizmena;
    }

    public void setBereizmena(String bereizmena) {
        this.bereizmena = bereizmena;
    }

    public String getPanelMota() {
        return panelMota;
    }

    public void setPanelMota(String panelMota) {
        this.panelMota = panelMota;
    }

    public int getFreskatzeTasaHz() {
        return freskatzeTasaHz;
    }

    public void setFreskatzeTasaHz(int freskatzeTasaHz) {
        this.freskatzeTasaHz = freskatzeTasaHz;
    }

    public String getKonexioak() {
        return konexioak;
    }

    public void setKonexioak(String konexioak) {
        this.konexioak = konexioak;
    }

    public String getKurbatura() {
        return kurbatura;
    }

    public void setKurbatura(String kurbatura) {
        this.kurbatura = kurbatura;
    }
}
