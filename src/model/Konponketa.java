package model;

import java.sql.Timestamp;

public class Konponketa {
    private int idKonponketa;
    private int produktuaId;
    private int langileaId;
    private Timestamp hasieraData;
    private Timestamp amaieraData;
    private String konponketaEgoera;
    private int akatsaId;
    private String oharrak;
    private Timestamp eguneratzeData;

    public Konponketa(int idKonponketa, int produktuaId, int langileaId, Timestamp hasieraData, Timestamp amaieraData,
                      String konponketaEgoera, int akatsaId, String oharrak, Timestamp eguneratzeData) {
        this.idKonponketa = idKonponketa;
        this.produktuaId = produktuaId;
        this.langileaId = langileaId;
        this.hasieraData = hasieraData;
        this.amaieraData = amaieraData;
        this.konponketaEgoera = konponketaEgoera;
        this.akatsaId = akatsaId;
        this.oharrak = oharrak;
        this.eguneratzeData = eguneratzeData;
    }

    public int getIdKonponketa() { return idKonponketa; }
    public void setIdKonponketa(int idKonponketa) { this.idKonponketa = idKonponketa; }

    public int getProduktuaId() { return produktuaId; }
    public void setProduktuaId(int produktuaId) { this.produktuaId = produktuaId; }

    public int getLangileaId() { return langileaId; }
    public void setLangileaId(int langileaId) { this.langileaId = langileaId; }

    public Timestamp getHasieraData() { return hasieraData; }
    public void setHasieraData(Timestamp hasieraData) { this.hasieraData = hasieraData; }

    public Timestamp getAmaieraData() { return amaieraData; }
    public void setAmaieraData(Timestamp amaieraData) { this.amaieraData = amaieraData; }

    public String getKonponketaEgoera() { return konponketaEgoera; }
    public void setKonponketaEgoera(String konponketaEgoera) { this.konponketaEgoera = konponketaEgoera; }

    public int getAkatsaId() { return akatsaId; }
    public void setAkatsaId(int akatsaId) { this.akatsaId = akatsaId; }

    public String getOharrak() { return oharrak; }
    public void setOharrak(String oharrak) { this.oharrak = oharrak; }

    public Timestamp getEguneratzeData() { return eguneratzeData; }
    public void setEguneratzeData(Timestamp eguneratzeData) { this.eguneratzeData = eguneratzeData; }
}
