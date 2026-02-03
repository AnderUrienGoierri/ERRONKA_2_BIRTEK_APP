package model;

import java.sql.Timestamp;

/**
 * Konponketa klasea.
 * Produktuen konponketen informazioa kudeatzen du.
 * Produktua, arduraduna (langilea), datak eta egoera gordetzen ditu.
 */
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

    /**
     * Konponketa eraikitzailea.
     *
     * @param idKonponketa     Konponketaren IDa.
     * @param produktuaId      Produktuaren IDa.
     * @param langileaId       Arduradunaren (langilea) IDa.
     * @param hasieraData      Hasiera data.
     * @param amaieraData      Amaiera data.
     * @param konponketaEgoera Konponketaren egoera.
     * @param akatsaId         Akatsaren IDa.
     * @param oharrak          Oharrak.
     * @param eguneratzeData   Eguneratze data.
     */
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

    /**
     * Konponketaren IDa lortzen du.
     * 
     * @return IDa.
     */
    public int getIdKonponketa() {
        return idKonponketa;
    }

    public void setIdKonponketa(int idKonponketa) {
        this.idKonponketa = idKonponketa;
    }

    /**
     * Produktuaren IDa lortzen du.
     * 
     * @return Produktuaren IDa.
     */
    public int getProduktuaId() {
        return produktuaId;
    }

    public void setProduktuaId(int produktuaId) {
        this.produktuaId = produktuaId;
    }

    /**
     * Langilearen IDa lortzen du.
     * 
     * @return Langilearen IDa.
     */
    public int getLangileaId() {
        return langileaId;
    }

    public void setLangileaId(int langileaId) {
        this.langileaId = langileaId;
    }

    /**
     * Hasiera data lortzen du.
     * 
     * @return Hasiera data.
     */
    public Timestamp getHasieraData() {
        return hasieraData;
    }

    public void setHasieraData(Timestamp hasieraData) {
        this.hasieraData = hasieraData;
    }

    /**
     * Amaiera data lortzen du.
     * 
     * @return Amaiera data.
     */
    public Timestamp getAmaieraData() {
        return amaieraData;
    }

    public void setAmaieraData(Timestamp amaieraData) {
        this.amaieraData = amaieraData;
    }

    /**
     * Konponketaren egoera lortzen du.
     * 
     * @return Egoera.
     */
    public String getKonponketaEgoera() {
        return konponketaEgoera;
    }

    public void setKonponketaEgoera(String konponketaEgoera) {
        this.konponketaEgoera = konponketaEgoera;
    }

    /**
     * Akatsaren IDa lortzen du.
     * 
     * @return Akatsaren IDa.
     */
    public int getAkatsaId() {
        return akatsaId;
    }

    public void setAkatsaId(int akatsaId) {
        this.akatsaId = akatsaId;
    }

    /**
     * Oharrak lortzen ditu.
     * 
     * @return Oharrak.
     */
    public String getOharrak() {
        return oharrak;
    }

    public void setOharrak(String oharrak) {
        this.oharrak = oharrak;
    }

    /**
     * Eguneratze data lortzen du.
     * 
     * @return Eguneratze data.
     */
    public Timestamp getEguneratzeData() {
        return eguneratzeData;
    }

    public void setEguneratzeData(Timestamp eguneratzeData) {
        this.eguneratzeData = eguneratzeData;
    }
}
