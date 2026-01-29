package model;

import java.math.BigDecimal;

public class EskaeraLerroa {
    private int idEskaeraLerroa;
    private int eskaeraId;
    private int produktuaId;
    private int kantitatea;
    private BigDecimal unitatePrezioa;
    private String eskaeraLerroEgoera;

    public EskaeraLerroa(int idEskaeraLerroa, int eskaeraId, int produktuaId, int kantitatea, BigDecimal unitatePrezioa,
                         String eskaeraLerroEgoera) {
        this.idEskaeraLerroa = idEskaeraLerroa;
        this.eskaeraId = eskaeraId;
        this.produktuaId = produktuaId;
        this.kantitatea = kantitatea;
        this.unitatePrezioa = unitatePrezioa;
        this.eskaeraLerroEgoera = eskaeraLerroEgoera;
    }

    public int getIdEskaeraLerroa() { return idEskaeraLerroa; }
    public void setIdEskaeraLerroa(int idEskaeraLerroa) { this.idEskaeraLerroa = idEskaeraLerroa; }

    public int getEskaeraId() { return eskaeraId; }
    public void setEskaeraId(int eskaeraId) { this.eskaeraId = eskaeraId; }

    public int getProduktuaId() { return produktuaId; }
    public void setProduktuaId(int produktuaId) { this.produktuaId = produktuaId; }

    public int getKantitatea() { return kantitatea; }
    public void setKantitatea(int kantitatea) { this.kantitatea = kantitatea; }

    public BigDecimal getUnitatePrezioa() { return unitatePrezioa; }
    public void setUnitatePrezioa(BigDecimal unitatePrezioa) { this.unitatePrezioa = unitatePrezioa; }

    public String getEskaeraLerroEgoera() { return eskaeraLerroEgoera; }
    public void setEskaeraLerroEgoera(String eskaeraLerroEgoera) { this.eskaeraLerroEgoera = eskaeraLerroEgoera; }
}
