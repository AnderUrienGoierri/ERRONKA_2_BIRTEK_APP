package model;

public class LangileSaila {
    private int idSaila;
    private String izena;
    private String kokapena;
    private String deskribapena;

    public LangileSaila(int idSaila, String izena, String kokapena, String deskribapena) {
        this.idSaila = idSaila;
        this.izena = izena;
        this.kokapena = kokapena;
        this.deskribapena = deskribapena;
    }

    public int getIdSaila() { return idSaila; }
    public void setIdSaila(int idSaila) { this.idSaila = idSaila; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }

    public String getKokapena() { return kokapena; }
    public void setKokapena(String kokapena) { this.kokapena = kokapena; }

    public String getDeskribapena() { return deskribapena; }
    public void setDeskribapena(String deskribapena) { this.deskribapena = deskribapena; }
}
