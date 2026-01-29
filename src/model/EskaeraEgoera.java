package model;

public enum EskaeraEgoera {
    PRESTATZEN("Prestatzen"),
    OSATUA_BIDALITA("Osatua/Bidalita"),
    EZABATUA("Ezabatua");

    private String deskribapena;

    EskaeraEgoera(String deskribapena) {
        this.deskribapena = deskribapena;
    }

    public String getDeskribapena() {
        return deskribapena;
    }

    @Override
    public String toString() {
        return deskribapena;
    }

    public static EskaeraEgoera fromString(String text) {
        for (EskaeraEgoera b : EskaeraEgoera.values()) {
            if (b.deskribapena.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
