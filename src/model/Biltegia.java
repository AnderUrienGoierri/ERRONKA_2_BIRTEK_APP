package model;

public class Biltegia {
    private int idBiltegia;
    private String izena;
    private String biltegiSku;

    public Biltegia(int idBiltegia, String izena, String biltegiSku) {
        this.idBiltegia = idBiltegia;
        this.izena = izena;
        this.biltegiSku = biltegiSku;
    }

    public int getIdBiltegia() { return idBiltegia; }
    public void setIdBiltegia(int idBiltegia) { this.idBiltegia = idBiltegia; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }

    public String getBiltegiSku() { return biltegiSku; }
    public void setBiltegiSku(String biltegiSku) { this.biltegiSku = biltegiSku; }
}
