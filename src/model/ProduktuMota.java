package model;

public enum ProduktuMota {
    ERAMANGARRIA("Eramangarria"),
    MAHAIGAINEKOA("Mahai-gainekoa"),
    MUGIKORRA("Mugikorra"),
    TABLETA("Tableta"),
    ZERBITZARIA("Zerbitzaria"),
    PANTAILA("Pantaila"),
    SOFTWAREA("Softwarea"),
	BESTELAKOA("");
    
 
	

    private String deskribapena;

    ProduktuMota(String deskribapena) {
        this.deskribapena = deskribapena;
    }

    public String getDeskribapena() {
        return deskribapena;
    }

    @Override
    public String toString() {
        return deskribapena;
    }

    public static ProduktuMota fromString(String text) {
        for (ProduktuMota b : ProduktuMota.values()) {
            if (b.deskribapena.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return BESTELAKOA;
    }
}
