package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * MahaiGainekoa klasea.
 * Produktua klasearen azpiklasea da, eta mahai gaineko ordenagailuen
 * informazioa kudeatzen du.
 * Prozesadorea, RAM, diskoa, txartel grafikoa, elikatze iturria eta kaxa
 * formatua bezalako ezaugarriak gordetzen ditu.
 */
public class MahaiGainekoa extends Produktua {
    private String prozesadorea;
    private String plakaBasea;
    private int ramGb;
    private int diskoaGb;
    private String txartelGrafikoa;
    private int elikatzeIturriaW;
    private String kaxaFormatua;

    /**
     * MahaiGainekoa eraikitzailea.
     *
     * @param idProduktua          Produktuaren IDa.
     * @param hornitzaileId        Hornitzailearen IDa.
     * @param kategoriaId          Kategoriaren IDa.
     * @param izena                Izena.
     * @param marka                Marka.
     * @param mota                 Mota (MahaiGainekoa).
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
     * @param prozesadorea         Prozesadorea.
     * @param plakaBasea           Plaka basea.
     * @param ramGb                RAM memoria GBtan.
     * @param diskoaGb             Diskoaren kapazitatea GBtan.
     * @param txartelGrafikoa      Txartel grafikoa.
     * @param elikatzeIturriaW     Elikatze iturria W-tan.
     * @param kaxaFormatua         Kaxaren formatua.
     */
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
     * Plaka basea lortzen du.
     * 
     * @return Plaka basea.
     */
    public String getPlakaBasea() {
        return plakaBasea;
    }

    /**
     * Plaka basea ezartzen du.
     * 
     * @param plakaBasea Plaka base berria.
     */
    public void setPlakaBasea(String plakaBasea) {
        this.plakaBasea = plakaBasea;
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
     * @param diskoaGb Disko kapazitate berria.
     */
    public void setDiskoaGb(int diskoaGb) {
        this.diskoaGb = diskoaGb;
    }

    /**
     * Txartel grafikoa lortzen du.
     * 
     * @return Txartel grafikoa.
     */
    public String getTxartelGrafikoa() {
        return txartelGrafikoa;
    }

    /**
     * Txartel grafikoa ezartzen du.
     * 
     * @param txartelGrafikoa Txartel grafiko berria.
     */
    public void setTxartelGrafikoa(String txartelGrafikoa) {
        this.txartelGrafikoa = txartelGrafikoa;
    }

    /**
     * Elikatze iturria lortzen du.
     * 
     * @return Elikatze iturria W-tan.
     */
    public int getElikatzeIturriaW() {
        return elikatzeIturriaW;
    }

    /**
     * Elikatze iturria ezartzen du.
     * 
     * @param elikatzeIturriaW Elikatze iturri berria.
     */
    public void setElikatzeIturriaW(int elikatzeIturriaW) {
        this.elikatzeIturriaW = elikatzeIturriaW;
    }

    /**
     * Kaxa formatua lortzen du.
     * 
     * @return Kaxa formatua.
     */
    public String getKaxaFormatua() {
        return kaxaFormatua;
    }

    /**
     * Kaxa formatua ezartzen du.
     * 
     * @param kaxaFormatua Kaxa formatu berria.
     */
    public void setKaxaFormatua(String kaxaFormatua) {
        this.kaxaFormatua = kaxaFormatua;
    }
}
