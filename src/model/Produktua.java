package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Produktua klase abstraktua.
 * Produktu mota guztien oinarrizko atributuak eta metodoak definitzen ditu.
 * Hornitzailea, kategoria, prezioa, stock-a eta egoera bezalako datuak
 * kudeatzen ditu.
 */
public abstract class Produktua {
    private int idProduktua;
    private int hornitzaileId;
    private int kategoriaId;
    private String izena;
    private String marka;
    private String mota;
    private String deskribapena;
    private String irudiaUrl;
    private Integer biltegiId;
    private String produktuEgoera;
    private String produktuEgoeraOharra;
    private boolean salgai;
    private BigDecimal salmentaPrezioa;
    private int stock;
    private BigDecimal eskaintza;
    private BigDecimal zergakEhunekoa;
    private Timestamp sortzeData;
    private Timestamp eguneratzeData;

    /**
     * Produktua eraikitzailea.
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
     */
    public Produktua(int idProduktua, int hornitzaileId, int kategoriaId, String izena, String marka, String mota,
            String deskribapena, String irudiaUrl, Integer biltegiId, String produktuEgoera,
            String produktuEgoeraOharra, boolean salgai, BigDecimal salmentaPrezioa, int stock,
            BigDecimal eskaintza, BigDecimal zergakEhunekoa, Timestamp sortzeData, Timestamp eguneratzeData) {
        this.idProduktua = idProduktua;
        this.hornitzaileId = hornitzaileId;
        this.kategoriaId = kategoriaId;
        this.izena = izena;
        this.marka = marka;
        this.mota = mota;
        this.deskribapena = deskribapena;
        this.irudiaUrl = irudiaUrl;
        this.biltegiId = biltegiId;
        this.produktuEgoera = produktuEgoera;
        this.produktuEgoeraOharra = produktuEgoeraOharra;
        this.salgai = salgai;
        this.salmentaPrezioa = salmentaPrezioa;
        this.stock = stock;
        this.eskaintza = eskaintza;
        this.zergakEhunekoa = zergakEhunekoa;
        this.sortzeData = sortzeData;
        this.eguneratzeData = eguneratzeData;
    }

    /**
     * Produktuaren IDa lortzen du.
     * 
     * @return IDa.
     */
    public int getIdProduktua() {
        return idProduktua;
    }

    /**
     * Produktuaren IDa ezartzen du.
     * 
     * @param idProduktua ID berria.
     */
    public void setIdProduktua(int idProduktua) {
        this.idProduktua = idProduktua;
    }

    /**
     * Hornitzailearen IDa lortzen du.
     * 
     * @return Hornitzailearen IDa.
     */
    public int getHornitzaileId() {
        return hornitzaileId;
    }

    /**
     * Hornitzailearen IDa ezartzen du.
     * 
     * @param hornitzaileId Hornitzaile ID berria.
     */
    public void setHornitzaileId(int hornitzaileId) {
        this.hornitzaileId = hornitzaileId;
    }

    /**
     * Kategoriaren IDa lortzen du.
     * 
     * @return Kategoriaren IDa.
     */
    public int getKategoriaId() {
        return kategoriaId;
    }

    /**
     * Kategoriaren IDa ezartzen du.
     * 
     * @param kategoriaId Kategoria ID berria.
     */
    public void setKategoriaId(int kategoriaId) {
        this.kategoriaId = kategoriaId;
    }

    /**
     * Izena lortzen du.
     * 
     * @return Izena.
     */
    public String getIzena() {
        return izena;
    }

    /**
     * Izena ezartzen du.
     * 
     * @param izena Izen berria.
     */
    public void setIzena(String izena) {
        this.izena = izena;
    }

    /**
     * Marka lortzen du.
     * 
     * @return Marka.
     */
    public String getMarka() {
        return marka;
    }

    /**
     * Marka ezartzen du.
     * 
     * @param marka Marka berria.
     */
    public void setMarka(String marka) {
        this.marka = marka;
    }

    /**
     * Mota lortzen du.
     * 
     * @return Mota.
     */
    public String getMota() {
        return mota;
    }

    /**
     * Mota ezartzen du.
     * 
     * @param mota Mota berria.
     */
    public void setMota(String mota) {
        this.mota = mota;
    }

    /**
     * Deskribapena lortzen du.
     * 
     * @return Deskribapena.
     */
    public String getDeskribapena() {
        return deskribapena;
    }

    /**
     * Deskribapena ezartzen du.
     * 
     * @param deskribapena Deskribapen berria.
     */
    public void setDeskribapena(String deskribapena) {
        this.deskribapena = deskribapena;
    }

    /**
     * Irudiaren URLa lortzen du.
     * 
     * @return Irudiaren URLa.
     */
    public String getIrudiaUrl() {
        return irudiaUrl;
    }

    /**
     * Irudiaren URLa ezartzen du.
     * 
     * @param irudiaUrl URL berria.
     */
    public void setIrudiaUrl(String irudiaUrl) {
        this.irudiaUrl = irudiaUrl;
    }

    /**
     * Biltegiaren IDa lortzen du.
     * 
     * @return Biltegiaren IDa.
     */
    public Integer getBiltegiId() {
        return biltegiId;
    }

    /**
     * Biltegiaren IDa ezartzen du.
     * 
     * @param biltegiId Biltegi ID berria.
     */
    public void setBiltegiId(Integer biltegiId) {
        this.biltegiId = biltegiId;
    }

    /**
     * Produktuaren egoera lortzen du.
     * 
     * @return Egoera.
     */
    public String getProduktuEgoera() {
        return produktuEgoera;
    }

    /**
     * Produktuaren egoera ezartzen du.
     * 
     * @param produktuEgoera Egoera berria.
     */
    public void setProduktuEgoera(String produktuEgoera) {
        this.produktuEgoera = produktuEgoera;
    }

    /**
     * Produktuaren egoeraren oharra lortzen du.
     * 
     * @return Oharra.
     */
    public String getProduktuEgoeraOharra() {
        return produktuEgoeraOharra;
    }

    /**
     * Produktuaren egoeraren oharra ezartzen du.
     * 
     * @param produktuEgoeraOharra Ohar berria.
     */
    public void setProduktuEgoeraOharra(String produktuEgoeraOharra) {
        this.produktuEgoeraOharra = produktuEgoeraOharra;
    }

    /**
     * Salgai dagoen lortzen du.
     * 
     * @return True salgai badago, false bestela.
     */
    public boolean isSalgai() {
        return salgai;
    }

    /**
     * Salgai egoera ezartzen du.
     * 
     * @param salgai Salgai egoera berria.
     */
    public void setSalgai(boolean salgai) {
        this.salgai = salgai;
    }

    /**
     * Salmenta prezioa lortzen du.
     * 
     * @return Prezioa.
     */
    public BigDecimal getSalmentaPrezioa() {
        return salmentaPrezioa;
    }

    /**
     * Salmenta prezioa ezartzen du.
     * 
     * @param salmentaPrezioa Prezio berria.
     */
    public void setSalmentaPrezioa(BigDecimal salmentaPrezioa) {
        this.salmentaPrezioa = salmentaPrezioa;
    }

    /**
     * Stock kopurua lortzen du.
     * 
     * @return Stock.
     */
    public int getStock() {
        return stock;
    }

    /**
     * Stock kopurua ezartzen du.
     * 
     * @param stock Stock berria.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Eskaintza prezioa lortzen du.
     * 
     * @return Eskaintza.
     */
    public BigDecimal getEskaintza() {
        return eskaintza;
    }

    /**
     * Eskaintza prezioa ezartzen du.
     * 
     * @param eskaintza Eskaintza berria.
     */
    public void setEskaintza(BigDecimal eskaintza) {
        this.eskaintza = eskaintza;
    }

    /**
     * Zergen ehunekoa lortzen du.
     * 
     * @return Zergak.
     */
    public BigDecimal getZergakEhunekoa() {
        return zergakEhunekoa;
    }

    /**
     * Zergen ehunekoa ezartzen du.
     * 
     * @param zergakEhunekoa Zerga ehuneko berria.
     */
    public void setZergakEhunekoa(BigDecimal zergakEhunekoa) {
        this.zergakEhunekoa = zergakEhunekoa;
    }

    /**
     * Sortze data lortzen du.
     * 
     * @return Sortze data.
     */
    public Timestamp getSortzeData() {
        return sortzeData;
    }

    /**
     * Sortze data ezartzen du.
     * 
     * @param sortzeData Data berria.
     */
    public void setSortzeData(Timestamp sortzeData) {
        this.sortzeData = sortzeData;
    }

    /**
     * Eguneratze data lortzen du.
     * 
     * @return Eguneratze data.
     */
    public Timestamp getEguneratzeData() {
        return eguneratzeData;
    }

    /**
     * Eguneratze data ezartzen du.
     * 
     * @param eguneratzeData Data berria.
     */
    public void setEguneratzeData(Timestamp eguneratzeData) {
        this.eguneratzeData = eguneratzeData;
    }
}
