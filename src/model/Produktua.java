package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

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

    public int getIdProduktua() {
        return idProduktua;
    }

    public void setIdProduktua(int idProduktua) {
        this.idProduktua = idProduktua;
    }

    public int getHornitzaileId() {
        return hornitzaileId;
    }

    public void setHornitzaileId(int hornitzaileId) {
        this.hornitzaileId = hornitzaileId;
    }

    public int getKategoriaId() {
        return kategoriaId;
    }

    public void setKategoriaId(int kategoriaId) {
        this.kategoriaId = kategoriaId;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public ProduktuMota getMotaEnum() {
        return ProduktuMota.fromString(this.mota);
    }

    public void setMotaEnum(ProduktuMota mota) {
        this.mota = mota.getDeskribapena();
    }

    public String getDeskribapena() {
        return deskribapena;
    }

    public void setDeskribapena(String deskribapena) {
        this.deskribapena = deskribapena;
    }

    public String getIrudiaUrl() {
        return irudiaUrl;
    }

    public void setIrudiaUrl(String irudiaUrl) {
        this.irudiaUrl = irudiaUrl;
    }

    public Integer getBiltegiId() {
        return biltegiId;
    }

    public void setBiltegiId(Integer biltegiId) {
        this.biltegiId = biltegiId;
    }

    public String getProduktuEgoera() {
        return produktuEgoera;
    }

    public void setProduktuEgoera(String produktuEgoera) {
        this.produktuEgoera = produktuEgoera;
    }

    public String getProduktuEgoeraOharra() {
        return produktuEgoeraOharra;
    }

    public void setProduktuEgoeraOharra(String produktuEgoeraOharra) {
        this.produktuEgoeraOharra = produktuEgoeraOharra;
    }

    public boolean isSalgai() {
        return salgai;
    }

    public void setSalgai(boolean salgai) {
        this.salgai = salgai;
    }

    public BigDecimal getSalmentaPrezioa() {
        return salmentaPrezioa;
    }

    public void setSalmentaPrezioa(BigDecimal salmentaPrezioa) {
        this.salmentaPrezioa = salmentaPrezioa;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public BigDecimal getEskaintza() {
        return eskaintza;
    }

    public void setEskaintza(BigDecimal eskaintza) {
        this.eskaintza = eskaintza;
    }

    public BigDecimal getZergakEhunekoa() {
        return zergakEhunekoa;
    }

    public void setZergakEhunekoa(BigDecimal zergakEhunekoa) {
        this.zergakEhunekoa = zergakEhunekoa;
    }

    public Timestamp getSortzeData() {
        return sortzeData;
    }

    public void setSortzeData(Timestamp sortzeData) {
        this.sortzeData = sortzeData;
    }

    public Timestamp getEguneratzeData() {
        return eguneratzeData;
    }

    public void setEguneratzeData(Timestamp eguneratzeData) {
        this.eguneratzeData = eguneratzeData;
    }
}
