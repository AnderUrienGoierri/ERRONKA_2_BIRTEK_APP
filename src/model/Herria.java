package model;

public class Herria {
    private int idHerria;
    private String izena;
    private String lurraldea;
    private String nazioa;

    public Herria(int idHerria, String izena, String lurraldea, String nazioa) {
        this.idHerria = idHerria;
        this.izena = izena;
        this.lurraldea = lurraldea;
        this.nazioa = nazioa;
    }

    public int getIdHerria() { return idHerria; }
    public void setIdHerria(int idHerria) { this.idHerria = idHerria; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }

    public String getLurraldea() { return lurraldea; }
    public void setLurraldea(String lurraldea) { this.lurraldea = lurraldea; }

    public String getNazioa() { return nazioa; }
    public void setNazioa(String nazioa) { this.nazioa = nazioa; }
}
