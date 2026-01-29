package model;

public class SarreraLerroa {
    private int idSarreraLerroa;
    private int sarreraId;
    private int produktuaId;
    private int kantitatea;
    private String sarreraLerroEgoera;

    public SarreraLerroa(int idSarreraLerroa, int sarreraId, int produktuaId, int kantitatea, String sarreraLerroEgoera) {
        this.idSarreraLerroa = idSarreraLerroa;
        this.sarreraId = sarreraId;
        this.produktuaId = produktuaId;
        this.kantitatea = kantitatea;
        this.sarreraLerroEgoera = sarreraLerroEgoera;
    }

    public int getIdSarreraLerroa() { return idSarreraLerroa; }
    public void setIdSarreraLerroa(int idSarreraLerroa) { this.idSarreraLerroa = idSarreraLerroa; }

    public int getSarreraId() { return sarreraId; }
    public void setSarreraId(int sarreraId) { this.sarreraId = sarreraId; }

    public int getProduktuaId() { return produktuaId; }
    public void setProduktuaId(int produktuaId) { this.produktuaId = produktuaId; }

    public int getKantitatea() { return kantitatea; }
    public void setKantitatea(int kantitatea) { this.kantitatea = kantitatea; }

    public String getSarreraLerroEgoera() { return sarreraLerroEgoera; }
    public void setSarreraLerroEgoera(String sarreraLerroEgoera) { this.sarreraLerroEgoera = sarreraLerroEgoera; }
}
