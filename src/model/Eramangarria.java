package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Eramangarria klasea.
 * Produktua klasearen azpiklasea da, eta ordenagailu eramangarrien ezaugarri
 * espezifikoak gordetzen ditu.
 * Prozesadorea, RAMa, diskoa, pantaila, bateria eta sistema eragilea bezalako
 * datuak kudeatzen ditu.
 */
public class Eramangarria extends Produktua {
    private String prozesadorea;
    private int ramGb;
    private int diskoaGb;
    private BigDecimal pantailaTamaina;
    private int bateriaWh;
    private String sistemaEragilea;
    private BigDecimal pisuaKg;

    /**
     * Eramangarria eraikitzailea.
     * Produktuaren oinarrizko datuez gain, eramangarriaren ezaugarriak
     * inizializatzen ditu.
     *
     * @param idProduktua          Produktuaren IDa.
     * @param hornitzaileId        Hornitzailearen IDa.
     * @param kategoriaId          Kategoriaren IDa.
     * @param izena                Izena.
     * @param marka                Marka.
     * @param mota                 Mota (Eramangarria).
     * @param deskribapena         Deskribapena.
     * @param irudiaUrl            Irudiaren URLa.
     * @param biltegiId            Biltegiaren IDa.
     * @param produktuEgoera       Produktuaren egoera.
     * @param produktuEgoeraOharra Egoeraren oharra.
     * @param salgai               Salgai dagoen edo ez.
     * @param salmentaPrezioa      Salmenta prezioa.
     * @param stock                Stock kopurua.
     * @param eskaintza            Eskaintza prezioa (baldin badago).
     * @param zergakEhunekoa       Zergen ehunekoa.
     * @param sortzeData           Sortze data.
     * @param eguneratzeData       Eguneratze data.
     * @param prozesadorea         Prozesadorea.
     * @param ramGb                RAM memoria GBtan.
     * @param diskoaGb             Diskoaren kapazitatea GBtan.
     * @param pantailaTamaina      Pantailaren tamaina hazbetetan.
     * @param bateriaWh            Bateriaren kapazitatea Wh-tan.
     * @param sistemaEragilea      Sistema eragilea.
     * @param pisuaKg              Pisua KGtan.
     */
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

    /**
     * Prozesadorea lortzen du.
     * 
     * @return Prozesadorea.
     */
    public String getProzesadorea() {
        return prozesadorea;
    }

    /**
     * Prozesadorea ezartzen du.
     * 
     * @param prozesadorea Prozesadore berria.
     */
    public void setProzesadorea(String prozesadorea) {
        this.prozesadorea = prozesadorea;
    }

    /**
     * RAM memoria lortzen du.
     * 
     * @return RAM memoria GBtan.
     */
    public int getRamGb() {
        return ramGb;
    }

    /**
     * RAM memoria ezartzen du.
     * 
     * @param ramGb RAM memoria berria GBtan.
     */
    public void setRamGb(int ramGb) {
        this.ramGb = ramGb;
    }

    /**
     * Diskoaren kapazitatea lortzen du.
     * 
     * @return Diskoa GBtan.
     */
    public int getDiskoaGb() {
        return diskoaGb;
    }

    /**
     * Diskoaren kapazitatea ezartzen du.
     * 
     * @param diskoaGb Disko kapazitate berria GBtan.
     */
    public void setDiskoaGb(int diskoaGb) {
        this.diskoaGb = diskoaGb;
    }

    /**
     * Pantailaren tamaina lortzen du.
     * 
     * @return Pantailaren tamaina hazbetetan.
     */
    public BigDecimal getPantailaTamaina() {
        return pantailaTamaina;
    }

    /**
     * Pantailaren tamaina ezartzen du.
     * 
     * @param pantailaTamaina Pantailaren tamaina berria.
     */
    public void setPantailaTamaina(BigDecimal pantailaTamaina) {
        this.pantailaTamaina = pantailaTamaina;
    }

    /**
     * Bateriaren kapazitatea lortzen du.
     * 
     * @return Bateria Wh-tan.
     */
    public int getBateriaWh() {
        return bateriaWh;
    }

    /**
     * Bateriaren kapazitatea ezartzen du.
     * 
     * @param bateriaWh Bateria kapazitate berria.
     */
    public void setBateriaWh(int bateriaWh) {
        this.bateriaWh = bateriaWh;
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
     * Pisua lortzen du.
     * 
     * @return Pisua KGtan.
     */
    public BigDecimal getPisuaKg() {
        return pisuaKg;
    }

    /**
     * Pisua ezartzen du.
     * 
     * @param pisuaKg Pisu berria.
     */
    public void setPisuaKg(BigDecimal pisuaKg) {
        this.pisuaKg = pisuaKg;
    }
}
