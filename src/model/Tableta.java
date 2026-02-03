package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Tableta klasea.
 * Produktua klasearen azpiklasea da eta tableta motako produktuen ezaugarriak
 * gordetzen ditu.
 * Adibidez: pantaila, biltegiratzea, konektibitatea...
 */
public class Tableta extends Produktua {
    private BigDecimal pantailaHazbeteak;
    private int biltegiratzeaGb;
    private String konektibitatea;
    private String sistemaEragilea;
    private int bateriaMah;
    private boolean arkatzarekinBateragarria;

    /**
     * Tableta eraikitzailea.
     *
     * @param idProduktua              Produktuaren IDa.
     * @param hornitzaileId            Hornitzailearen IDa.
     * @param kategoriaId              Kategoriaren IDa.
     * @param izena                    Izena.
     * @param marka                    Marka.
     * @param mota                     Mota.
     * @param deskribapena             Deskribapena.
     * @param irudiaUrl                Irudiaren URLa.
     * @param biltegiId                Biltegiaren IDa.
     * @param produktuEgoera           Produktuaren egoera.
     * @param produktuEgoeraOharra     Egoeraren oharra.
     * @param salgai                   Salgai dagoen.
     * @param salmentaPrezioa          Salmenta prezioa.
     * @param stock                    Stock kopurua.
     * @param eskaintza                Eskaintza prezioa.
     * @param zergakEhunekoa           Zergen ehunekoa.
     * @param sortzeData               Sortze data.
     * @param eguneratzeData           Eguneratze data.
     * @param pantailaHazbeteak        Pantailaren hazbeteak.
     * @param biltegiratzeaGb          Biltegiratzea GBtan.
     * @param konektibitatea           Konektibitatea (WiFi, 4G...).
     * @param sistemaEragilea          Sistema eragilea.
     * @param bateriaMah               Bateriaren edukiera (mAh).
     * @param arkatzarekinBateragarria Arkatzarekin bateragarria den.
     */
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

    /**
     * Pantailaren hazbeteak lortzen ditu.
     * 
     * @return Hazbeteak.
     */
    public BigDecimal getPantailaHazbeteak() {
        return pantailaHazbeteak;
    }

    /**
     * Pantailaren hazbeteak ezartzen ditu.
     * 
     * @param pantailaHazbeteak Hazbete berriak.
     */
    public void setPantailaHazbeteak(BigDecimal pantailaHazbeteak) {
        this.pantailaHazbeteak = pantailaHazbeteak;
    }

    /**
     * Biltegiratzea lortzen du GBtan.
     * 
     * @return Biltegiratzea.
     */
    public int getBiltegiratzeaGb() {
        return biltegiratzeaGb;
    }

    /**
     * Biltegiratzea ezartzen du.
     * 
     * @param biltegiratzeaGb Biltegiratze berria.
     */
    public void setBiltegiratzeaGb(int biltegiratzeaGb) {
        this.biltegiratzeaGb = biltegiratzeaGb;
    }

    /**
     * Konektibitatea lortzen du.
     * 
     * @return Konektibitatea.
     */
    public String getKonektibitatea() {
        return konektibitatea;
    }

    /**
     * Konektibitatea ezartzen du.
     * 
     * @param konektibitatea Konektibitate berria.
     */
    public void setKonektibitatea(String konektibitatea) {
        this.konektibitatea = konektibitatea;
    }

    /**
     * Sistema eragilea lortzen du.
     * 
     * @return Sistema eragilea.
     */
    public String getSistemaEragilea() {
        return sistemaEragilea;
    }

    /**
     * Sistema eragilea ezartzen du.
     * 
     * @param sistemaEragilea Sistema eragile berria.
     */
    public void setSistemaEragilea(String sistemaEragilea) {
        this.sistemaEragilea = sistemaEragilea;
    }

    /**
     * Bateria edukiera lortzen du mAh-tan.
     * 
     * @return Bateria.
     */
    public int getBateriaMah() {
        return bateriaMah;
    }

    /**
     * Bateria edukiera ezartzen du.
     * 
     * @param bateriaMah Bateria berria.
     */
    public void setBateriaMah(int bateriaMah) {
        this.bateriaMah = bateriaMah;
    }

    /**
     * Arkatzarekin bateragarria den lortzen du.
     * 
     * @return True bateragarria bada.
     */
    public boolean isArkatzarekinBateragarria() {
        return arkatzarekinBateragarria;
    }

    /**
     * Arkatzarekin bateragarria den ezartzen du.
     * 
     * @param arkatzarekinBateragarria Bateragarritasuna.
     */
    public void setArkatzarekinBateragarria(boolean arkatzarekinBateragarria) {
        this.arkatzarekinBateragarria = arkatzarekinBateragarria;
    }
}
