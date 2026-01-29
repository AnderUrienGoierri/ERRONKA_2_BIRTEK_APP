package model;

import java.sql.Timestamp;

public class Hornitzailea extends Pertsona {
    private String kontaktuPertsona;

    public Hornitzailea(int idHornitzailea, String izenaSoziala, String ifzNan, String kontaktuPertsona,
            String helbidea, int herriaId, String postaKodea, String telefonoa, String emaila,
            String hizkuntza, String pasahitza, boolean aktibo, Timestamp eguneratzeData) {
        super(idHornitzailea, izenaSoziala, null, ifzNan, null, helbidea, herriaId, postaKodea, telefonoa, emaila,
                hizkuntza, pasahitza, aktibo, null, eguneratzeData);
        this.kontaktuPertsona = kontaktuPertsona;
    }

    public int getIdHornitzailea() {
        return this.id;
    }

    public void setIdHornitzailea(int idHornitzailea) {
        this.id = idHornitzailea;
    }

    public String getIzenaSoziala() {
        return this.izena;
    }

    public void setIzenaSoziala(String izenaSoziala) {
        this.izena = izenaSoziala;
    }

    public String getIfzNan() {
        return this.nanIfz;
    }

    public void setIfzNan(String ifzNan) {
        this.nanIfz = ifzNan;
    }

    public String getKontaktuPertsona() {
        return kontaktuPertsona;
    }

    public void setKontaktuPertsona(String kontaktuPertsona) {
        this.kontaktuPertsona = kontaktuPertsona;
    }

}
