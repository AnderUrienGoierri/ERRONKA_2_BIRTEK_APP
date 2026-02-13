package model;

import java.sql.Timestamp;

/**
 * Hornitzailea klasea.
 * Pertsona klasearen azpiklasea da, eta hornitzaileen informazioa gordetzen du.
 * Kontakturako pertsonaren izena kudeatzen du.
 */
public class Hornitzailea extends Pertsona {
    private String kontaktuPertsona;

    /**
     * Hornitzailea eraikitzailea.
     *
     * @param idHornitzailea   Hornitzailearen IDa.
     * @param izenaSoziala     Izen soziala.
     * @param ifzNan           IFZ edo NAN.
     * @param kontaktuPertsona Kontaktu pertsona.
     * @param helbidea         Helbidea.
     * @param herriaId         Herriaren IDa.
     * @param postaKodea       Posta kodea.
     * @param telefonoa        Telefonoa.
     * @param emaila           Emaila.
     * @param hizkuntza        Hizkuntza.
     * @param pasahitza        Pasahitza.
     * @param aktibo           Aktibo dagoen.
     * @param eguneratzeData   Eguneratze data.
     */
    public Hornitzailea(int idHornitzailea, String izenaSoziala, String ifzNan, String kontaktuPertsona,
            String helbidea, int herriaId, String postaKodea, String telefonoa, String emaila,
            String hizkuntza, String pasahitza, boolean aktibo, Timestamp eguneratzeData) {
        super(idHornitzailea, izenaSoziala, null, ifzNan, null, helbidea, herriaId, postaKodea, telefonoa, emaila,
                hizkuntza, pasahitza, aktibo, null, eguneratzeData);
        this.kontaktuPertsona = kontaktuPertsona;
    }

    /**
     * Hornitzailearen IDa lortzen du.
     *
     * @return IDa.
     */
    public int getIdHornitzailea() {
        return this.id;
    }

    /**
     * Hornitzailearen IDa ezartzen du.
     *
     * @param idHornitzailea ID berria.
     */
    public void setIdHornitzailea(int idHornitzailea) {
        this.id = idHornitzailea;
    }

    /**
     * Izen soziala lortzen du.
     *
     * @return Izen soziala.
     */
    public String getIzenaSoziala() {
        return this.izena;
    }

    /**
     * Izen soziala ezartzen du.
     *
     * @param izenaSoziala Izen sozial berria.
     */
    public void setIzenaSoziala(String izenaSoziala) {
        this.izena = izenaSoziala;
    }

    /**
     * IFZ edo NAN lortzen du.
     *
     * @return IFZ edo NAN.
     */
    public String getIfzNan() {
        return this.nanIfz;
    }

    /**
     * IFZ edo NAN ezartzen du.
     *
     * @param ifzNan IFZ edo NAN berria.
     */
    public void setIfzNan(String ifzNan) {
        this.nanIfz = ifzNan;
    }

    /**
     * Kontaktu pertsona lortzen du.
     *
     * @return Kontaktu pertsona.
     */
    public String getKontaktuPertsona() {
        return kontaktuPertsona;
    }

    /**
     * Kontaktu pertsona ezartzen du.
     *
     * @param kontaktuPertsona Kontaktu pertsona berria.
     */
    public void setKontaktuPertsona(String kontaktuPertsona) {
        this.kontaktuPertsona = kontaktuPertsona;
    }

}
