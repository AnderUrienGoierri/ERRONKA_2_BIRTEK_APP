package model;

public class Akatsa {
    private int idAkatsa;
    private String izena;
    private String deskribapena;

    public Akatsa(int idAkatsa, String izena, String deskribapena) {
        this.idAkatsa = idAkatsa;
        this.izena = izena;
        this.deskribapena = deskribapena;
    }

    public int getIdAkatsa() { return idAkatsa; }
    public void setIdAkatsa(int idAkatsa) { this.idAkatsa = idAkatsa; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }

    public String getDeskribapena() { return deskribapena; }
    public void setDeskribapena(String deskribapena) { this.deskribapena = deskribapena; }
}
