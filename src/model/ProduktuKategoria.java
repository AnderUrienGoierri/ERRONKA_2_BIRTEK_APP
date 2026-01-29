package model;

public class ProduktuKategoria {
    private int idKategoria;
    private String izena;

    public ProduktuKategoria(int idKategoria, String izena) {
        this.idKategoria = idKategoria;
        this.izena = izena;
    }

    public int getIdKategoria() { return idKategoria; }
    public void setIdKategoria(int idKategoria) { this.idKategoria = idKategoria; }

    public String getIzena() { return izena; }
    public void setIzena(String izena) { this.izena = izena; }
}
