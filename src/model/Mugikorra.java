package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Mugikorra klasea.
 * Produktua klasearen azpiklasea da, eta telefono mugikorren informazioa
 * kudeatzen du.
 * Pantaila, biltegiratzea, RAM, kamera, bateria eta sistema eragilea bezalako
 * ezaugarriak gordetzen ditu.
 */
public class Mugikorra extends Produktua {
    private String pantailaTeknologia;
    private BigDecimal pantailaHazbeteak;
    private int biltegiratzeaGb;
    private int ramGb;
    private int kameraNagusaMp;
    private int bateriaMah;
    private String sistemaEragilea;
    private String sareak;

    /**
     * Mugikorra eraikitzailea.
     *
     * @param idProduktua          Produktuaren IDa.
     * @param hornitzaileId        Hornitzailearen IDa.
     * @param kategoriaId          Kategoriaren IDa.
     * @param izena                Izena.
     * @param marka                Marka.
     * @param mota                 Mota (Mugikorra).
     * @param deskribapena         Deskribapena.
     * @param irudiaUrl            Irudiaren URLa.
     * @param biltegiId            Biltegiaren IDa.
     * @param produktuEgoera       Produktuaren egoera.
     * @param produktuEgoeraOharra Egoeraren oharra.
     * @param salgai               Salgai dagoen.
     * @param salmentaPrezioa      Salmenta prezioa.
     * @param stock                Stock kopurua.
     * @param eskaintza            Eskaintza prezioa.
     * @param zergakEhunekoa       Zergen ehunekoa.
     * @param sortzeData           Sortze data.
     * @param eguneratzeData       Eguneratze data.
     * @param pantailaTeknologia   Pantailaren teknologia.
     * @param pantailaHazbeteak    Pantailaren tamaina hazbetetan.
     * @param biltegiratzeaGb      Biltegiratzea GBtan.
     * @param ramGb                RAM memoria GBtan.
     * @param kameraNagusaMp       Kamera nagusia MPtan.
     * @param bateriaMah           Bateriaren kapazitatea mAh-tan.
     * @param sistemaEragilea      Sistema eragilea.
     * @param sareak               Sarea (4G, 5G...).
     */
    public Mugikorra(int idProduktua, int hornitzaileId, int kategoriaId, String izena, String marka, String mota,
            String deskribapena, String irudiaUrl, Integer biltegiId, String produktuEgoera,
            String produktuEgoeraOharra, boolean salgai, BigDecimal salmentaPrezioa, int stock,
            BigDecimal eskaintza, BigDecimal zergakEhunekoa, Timestamp sortzeData, Timestamp eguneratzeData,
            String pantailaTeknologia, BigDecimal pantailaHazbeteak, int biltegiratzeaGb,
            int ramGb, int kameraNagusaMp, int bateriaMah, String sistemaEragilea, String sareak) {
        super(idProduktua, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl, biltegiId,
                produktuEgoera, produktuEgoeraOharra, salgai, salmentaPrezioa, stock, eskaintza, zergakEhunekoa,
                sortzeData, eguneratzeData);
        // hemen soilik mugikorraren ezaugarriak ezartzen dira
        this.pantailaTeknologia = pantailaTeknologia;
        this.pantailaHazbeteak = pantailaHazbeteak;
        this.biltegiratzeaGb = biltegiratzeaGb;
        this.ramGb = ramGb;
        this.kameraNagusaMp = kameraNagusaMp;
        this.bateriaMah = bateriaMah;
        this.sistemaEragilea = sistemaEragilea;
        this.sareak = sareak;
    }

    /**
     * Pantaila teknologia lortzen du.
     *
     * @return Pantaila teknologia.
     */
    public String getPantailaTeknologia() {
        return pantailaTeknologia;
    }

    /**
     * Pantaila teknologia ezartzen du.
     *
     * @param pantailaTeknologia Teknologia berria.
     */
    public void setPantailaTeknologia(String pantailaTeknologia) {
        this.pantailaTeknologia = pantailaTeknologia;
    }

    /**
     * Pantaila tamaina lortzen du.
     *
     * @return Tamaina hazbetetan.
     */
    public BigDecimal getPantailaHazbeteak() {
        return pantailaHazbeteak;
    }

    /**
     * Pantaila tamaina ezartzen du.
     *
     * @param pantailaHazbeteak Tamaina berria.
     */
    public void setPantailaHazbeteak(BigDecimal pantailaHazbeteak) {
        this.pantailaHazbeteak = pantailaHazbeteak;
    }

    /**
     * Biltegiratze kapazitatea lortzen du.
     *
     * @return Biltegiratzea GBtan.
     */
    public int getBiltegiratzeaGb() {
        return biltegiratzeaGb;
    }

    /**
     * Biltegiratze kapazitatea ezartzen du.
     *
     * @param biltegiratzeaGb Kapazitate berria.
     */
    public void setBiltegiratzeaGb(int biltegiratzeaGb) {
        this.biltegiratzeaGb = biltegiratzeaGb;
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
     * @param ramGb RAM memoria berria.
     */
    public void setRamGb(int ramGb) {
        this.ramGb = ramGb;
    }

    /**
     * Kamera nagusiko megapixelak lortzen du.
     *
     * @return Kamera MP.
     */
    public int getKameraNagusaMp() {
        return kameraNagusaMp;
    }

    /**
     * Kamera nagusiko megapixelak ezartzen du.
     *
     * @param kameraNagusaMp Kamera MP berria.
     */
    public void setKameraNagusaMp(int kameraNagusaMp) {
        this.kameraNagusaMp = kameraNagusaMp;
    }

    /**
     * Bateria kapazitatea lortzen du.
     *
     * @return Bateria mAh-tan.
     */
    public int getBateriaMah() {
        return bateriaMah;
    }

    /**
     * Bateria kapazitatea ezartzen du.
     *
     * @param bateriaMah Bateria kapazitate berria.
     */
    public void setBateriaMah(int bateriaMah) {
        this.bateriaMah = bateriaMah;
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
     * Sare motak lortzen ditu.
     *
     * @return Sareak.
     */
    public String getSareak() {
        return sareak;
    }

    /**
     * Sare motak ezartzen ditu.
     *
     * @param sareak Sarea berria.
     */
    public void setSareak(String sareak) {
        this.sareak = sareak;
    }
}
