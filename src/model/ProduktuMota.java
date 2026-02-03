package model;

/**
 * ProduktuMota enumerazioa.
 * Produktuen mota desberdinak definitzen ditu.
 * Adibidez: ERAMANGARRIA, MAHAIGAINEKOA, MUGIKORRA, etab.
 */
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

    /**
     * ProduktuMota eraikitzailea.
     * 
     * @param deskribapena Motaren deskribapena.
     */
    ProduktuMota(String deskribapena) {
        this.deskribapena = deskribapena;
    }

    /**
     * Motaren deskribapena lortzen du.
     * 
     * @return Deskribapena.
     */
    public String getDeskribapena() {
        return deskribapena;
    }

    /**
     * String adierazpena itzultzen du.
     * 
     * @return Deskribapena.
     */
    @Override
    public String toString() {
        return deskribapena;
    }

    /**
     * String batetik abiatuta ProduktuMota lortzen du.
     * 
     * @param text Testua.
     * @return ProduktuMota bat datorrena, edo BESTELAKOA.
     */
    public static ProduktuMota fromString(String text) {
        for (ProduktuMota b : ProduktuMota.values()) {
            if (b.deskribapena.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return BESTELAKOA;
    }
}
