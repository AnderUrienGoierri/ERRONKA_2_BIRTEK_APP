package model;

/**
 * EskaeraEgoera enumerazioa.
 * Eskaera batek izan ditzakeen egoerak definitzen ditu.
 * PRESTATZEN, OSATUA_BIDALITA eta EZABATUA.
 */
public enum EskaeraEgoera {
    PRESTATZEN("Prestatzen"),
    OSATUA_BIDALITA("Osatua/Bidalita"),
    EZABATUA("Ezabatua");

    private String deskribapena;

    /**
     * EskaeraEgoera eraikitzailea.
     * 
     * @param deskribapena Egoeraren testu deskribapena.
     */
    EskaeraEgoera(String deskribapena) {
        this.deskribapena = deskribapena;
    }

    /**
     * Egoeraren deskribapena lortzen du.
     * 
     * @return Deskribapena String moduan.
     */
    public String getDeskribapena() {
        return deskribapena;
    }

    /**
     * Enumaren String adierazpena itzultzen du.
     * 
     * @return Deskribapena.
     */
    @Override
    public String toString() {
        return deskribapena;
    }

    /**
     * String batetik abiatuta dagokion EskaeraEgoera lortzen du.
     * 
     * @param text Sarrera testua.
     * @return EskaeraEgoera edo null bat etortzen ez bada.
     */
    public static EskaeraEgoera fromString(String text) {
        for (EskaeraEgoera b : EskaeraEgoera.values()) {
            if (b.deskribapena.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
