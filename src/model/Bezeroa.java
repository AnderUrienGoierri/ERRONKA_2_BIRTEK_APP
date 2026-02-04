package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Bezeroa klasea.
 * Pertsona klasearen azpiklasea da, eta bezeroen informazio espezifikoa
 * gordetzen du.
 * Bezeroen sexua eta ordainketa txartela kudeatzen ditu.
 */
public class Bezeroa extends Pertsona {
    private String sexua;
    private String bezeroOrdainketaTxartela;

    /**
     * Bezeroa eraikitzailea.
     * Bezero berri bat sortzen du emandako datuekin.
     *
     * @param idBezeroa                Bezeroaren IDa.
     * @param izenaEdoSoziala          Bezeroaren izena edo izen soziala.
     * @param abizena                  Bezeroaren abizena.
     * @param ifzNan                   NAN edo IFZ zenbakia.
     * @param jaiotzaData              Jaiotza data.
     * @param sexua                    Bezeroaren sexua.
     * @param bezeroOrdainketaTxartela Ordainketa txartelaren zenbakia.
     * @param helbidea                 Bezeroaren helbidea.
     * @param herriaId                 Herriaren IDa.
     * @param postaKodea               Posta kodea.
     * @param telefonoa                Telefono zenbakia.
     * @param emaila                   Email helbidea.
     * @param hizkuntza                Hizkuntza lehenetsia.
     * @param pasahitza                Pasahitza.
     * @param altaData                 Alta data.
     * @param eguneratzeData           Azken eguneratze data.
     * @param aktibo                   Bezeroa aktibo dagoen edo ez.
     */
    public Bezeroa(int idBezeroa, String izenaEdoSoziala, String abizena, String ifzNan, Date jaiotzaData,
            String sexua, String bezeroOrdainketaTxartela, String helbidea, int herriaId, String postaKodea,
            String telefonoa, String emaila, String hizkuntza, String pasahitza, Timestamp altaData,
            Timestamp eguneratzeData, boolean aktibo) {
        super(idBezeroa, izenaEdoSoziala, abizena, ifzNan, jaiotzaData, helbidea, herriaId, postaKodea,
                telefonoa, emaila, hizkuntza, pasahitza, aktibo, altaData, eguneratzeData);
        this.sexua = sexua;
        this.bezeroOrdainketaTxartela = bezeroOrdainketaTxartela;
    }

    /**
     * Bezeroaren IDa lortzen du.
     * 
     * @return IDa.
     */
    public int getIdBezeroa() {
        return this.id;
    }

    /**
     * Bezeroaren IDa ezartzen du.
     * 
     * @param idBezeroa ID berria.
     */
    public void setIdBezeroa(int idBezeroa) {
        this.id = idBezeroa;
    }

    /**
     * Bezeroaren izena edo izen soziala lortzen du.
     * 
     * @return Izena edo Izen Soziala.
     */
    public String getIzenaEdoSoziala() {
        return this.izena;
    }

    /**
     * Bezeroaren izena edo izen soziala ezartzen du.
     * 
     * @param izenaEdoSoziala Izen berria.
     */
    public void setIzenaEdoSoziala(String izenaEdoSoziala) {
        this.izena = izenaEdoSoziala;
    }

    /**
     * NAN edo IFZ lortzen du.
     * 
     * @return NAN edo IFZ.
     */
    public String getIfzNan() {
        return this.nanIfz;
    }

    /**
     * NAN edo IFZ ezartzen du.
     * 
     * @param ifzNan NAN edo IFZ berria.
     */
    public void setIfzNan(String ifzNan) {
        this.nanIfz = ifzNan;
    }

    /**
     * Bezeroaren sexua lortzen du.
     * 
     * @return Sexua.
     */
    public String getSexua() {
        return sexua;
    }

    /**
     * Bezeroaren sexua ezartzen du.
     * 
     * @param sexua Sexu berria.
     */
    public void setSexua(String sexua) {
        this.sexua = sexua;
    }

    /**
     * Bezeroaren ordainketa txartela lortzen du.
     * 
     * @return Ordainketa txartela.
     */
    public String getBezeroOrdainketaTxartela() {
        return bezeroOrdainketaTxartela;
    }

    /**
     * Bezeroaren ordainketa txartela ezartzen du.
     * 
     * @param bezeroOrdainketaTxartela Txartel zenbaki berria.
     */
    public void setBezeroOrdainketaTxartela(String bezeroOrdainketaTxartela) {
        this.bezeroOrdainketaTxartela = bezeroOrdainketaTxartela;
    }

}
