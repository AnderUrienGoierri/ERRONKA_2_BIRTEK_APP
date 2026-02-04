package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Pantaila klasea.
 * Produktua klasearen azpiklasea da, eta monitor edo pantailen informazioa
 * kudeatzen du.
 * Bereizmena, panel mota, freskatze tasa, konexioak eta kurbatura bezalako
 * ezaugarriak gordetzen ditu.
 */
public class Pantaila extends Produktua {
    private BigDecimal hazbeteak;
    private String bereizmena;
    private String panelMota;
    private int freskatzeTasaHz;
    private String konexioak;
    private String kurbatura;

    /**
     * Pantaila eraikitzailea.
     *
     * @param idProduktua          Produktuaren IDa.
     * @param hornitzaileId        Hornitzailearen IDa.
     * @param kategoriaId          Kategoriaren IDa.
     * @param izena                Izena.
     * @param marka                Marka.
     * @param mota                 Mota (Pantaila).
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
     * @param hazbeteak            Pantailaren tamaina hazbetetan.
     * @param bereizmena           Bereizmena (adib. 1920x1080).
     * @param panelMota            Panel mota (IPS, VA, TN...).
     * @param freskatzeTasaHz      Freskatze tasa Hz-tan.
     * @param konexioak            Konexioak (HDMI, DP...).
     * @param kurbatura            Kurbatura (baldin badauka).
     */
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

    /**
     * Pantaila tamaina lortzen du.
     * 
     * @return Tamaina hazbetetan.
     */
    public BigDecimal getHazbeteak() {
        return hazbeteak;
    }

    /**
     * Pantaila tamaina ezartzen du.
     * 
     * @param hazbeteak Tamaina berria.
     */
    public void setHazbeteak(BigDecimal hazbeteak) {
        this.hazbeteak = hazbeteak;
    }

    /**
     * Bereizmena lortzen du.
     * 
     * @return Bereizmena.
     */
    public String getBereizmena() {
        return bereizmena;
    }

    /**
     * Bereizmena ezartzen du.
     * 
     * @param bereizmena Bereizmen berria.
     */
    public void setBereizmena(String bereizmena) {
        this.bereizmena = bereizmena;
    }

    /**
     * Panel mota lortzen du.
     * 
     * @return Panel mota.
     */
    public String getPanelMota() {
        return panelMota;
    }

    /**
     * Panel mota ezartzen du.
     * 
     * @param panelMota Panel mota berria.
     */
    public void setPanelMota(String panelMota) {
        this.panelMota = panelMota;
    }

    /**
     * Freskatze tasa lortzen du.
     * 
     * @return Freskatze tasa Hz-tan.
     */
    public int getFreskatzeTasaHz() {
        return freskatzeTasaHz;
    }

    /**
     * Freskatze tasa ezartzen du.
     * 
     * @param freskatzeTasaHz Freskatze tasa berria.
     */
    public void setFreskatzeTasaHz(int freskatzeTasaHz) {
        this.freskatzeTasaHz = freskatzeTasaHz;
    }

    /**
     * Konexioak lortzen ditu.
     * 
     * @return Konexioak.
     */
    public String getKonexioak() {
        return konexioak;
    }

    /**
     * Konexioak ezartzen ditu.
     * 
     * @param konexioak Konexio berriak.
     */
    public void setKonexioak(String konexioak) {
        this.konexioak = konexioak;
    }

    /**
     * Kurbatura lortzen du.
     * 
     * @return Kurbatura.
     */
    public String getKurbatura() {
        return kurbatura;
    }

    /**
     * Kurbatura ezartzen du.
     * 
     * @param kurbatura Kurbatura berria.
     */
    public void setKurbatura(String kurbatura) {
        this.kurbatura = kurbatura;
    }
}
