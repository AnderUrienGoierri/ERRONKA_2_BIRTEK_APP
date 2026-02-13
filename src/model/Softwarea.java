package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Softwarea klasea.
 * Produktua klasearen azpiklasea da eta software motako produktuen ezaugarriak
 * gordetzen ditu.
 * Adibidez: lizentzia mota, bertsioa, garatzailea.
 */
public class Softwarea extends Produktua {
    private String softwareMota;
    private String lizentziaMota;
    private String bertsioa;
    private String garatzailea;

    /**
     * Softwarea eraikitzailea.
     *
     * @param idProduktua          Produktuaren IDa.
     * @param hornitzaileId        Hornitzailearen IDa.
     * @param kategoriaId          Kategoriaren IDa.
     * @param izena                Izena.
     * @param marka                Marka.
     * @param mota                 Mota.
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
     * @param softwareMota         Software mota (adib. Sistema eragilea,
     *                             Bulegotika...).
     * @param lizentziaMota        Lizentzia mota.
     * @param bertsioa             Bertsioa.
     * @param garatzailea          Garatzailea.
     */
    public Softwarea(int idProduktua, int hornitzaileId, int kategoriaId, String izena, String marka, String mota,
            String deskribapena, String irudiaUrl, Integer biltegiId, String produktuEgoera,
            String produktuEgoeraOharra, boolean salgai, BigDecimal salmentaPrezioa, int stock,
            BigDecimal eskaintza, BigDecimal zergakEhunekoa, Timestamp sortzeData, Timestamp eguneratzeData,
            String softwareMota, String lizentziaMota, String bertsioa, String garatzailea) {
        super(idProduktua, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl, biltegiId,
                produktuEgoera, produktuEgoeraOharra, salgai, salmentaPrezioa, stock, eskaintza, zergakEhunekoa,
                sortzeData, eguneratzeData);
        // hemen soilik softwarearen ezaugarriak ezartzen dira
        this.softwareMota = softwareMota;
        this.lizentziaMota = lizentziaMota;
        this.bertsioa = bertsioa;
        this.garatzailea = garatzailea;
    }

    /**
     * Software mota lortzen du.
     *
     * @return Software mota.
     */
    public String getSoftwareMota() {
        return softwareMota;
    }

    /**
     * Software mota ezartzen du.
     *
     * @param softwareMota Mota berria.
     */
    public void setSoftwareMota(String softwareMota) {
        this.softwareMota = softwareMota;
    }

    /**
     * Lizentzia mota lortzen du.
     *
     * @return Lizentzia mota.
     */
    public String getLizentziaMota() {
        return lizentziaMota;
    }

    /**
     * Lizentzia mota ezartzen du.
     *
     * @param lizentziaMota Lizentzia mota berria.
     */
    public void setLizentziaMota(String lizentziaMota) {
        this.lizentziaMota = lizentziaMota;
    }

    /**
     * Bertsioa lortzen du.
     *
     * @return Bertsioa.
     */
    public String getBertsioa() {
        return bertsioa;
    }

    /**
     * Bertsioa ezartzen du.
     *
     * @param bertsioa Bertsio berria.
     */
    public void setBertsioa(String bertsioa) {
        this.bertsioa = bertsioa;
    }

    /**
     * Garatzailea lortzen du.
     *
     * @return Garatzailea.
     */
    public String getGaratzailea() {
        return garatzailea;
    }

    /**
     * Garatzailea ezartzen du.
     *
     * @param garatzailea Garatzaile berria.
     */
    public void setGaratzailea(String garatzailea) {
        this.garatzailea = garatzailea;
    }
}
