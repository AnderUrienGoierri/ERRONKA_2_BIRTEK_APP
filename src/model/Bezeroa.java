package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Bezeroa extends Pertsona  {
    private String sexua;
    private String bezeroOrdainketaTxartela;

    public Bezeroa(int idBezeroa, String izenaEdoSoziala, String abizena, String ifzNan, Date jaiotzaData,
            String sexua, String bezeroOrdainketaTxartela, String helbidea, int herriaId, String postaKodea,
            String telefonoa, String emaila, String hizkuntza, String pasahitza, Timestamp altaData,
            Timestamp eguneratzeData, boolean aktibo) {
        super(idBezeroa, izenaEdoSoziala, abizena, ifzNan, jaiotzaData, helbidea, herriaId, postaKodea,
                telefonoa, emaila, hizkuntza, pasahitza, aktibo, altaData, eguneratzeData);
        this.sexua = sexua;
        this.bezeroOrdainketaTxartela = bezeroOrdainketaTxartela;
    }

    public int getIdBezeroa() {
        return this.id;
    }

    public void setIdBezeroa(int idBezeroa) {
        this.id = idBezeroa;
    }

    public String getIzenaEdoSoziala() {
        return this.izena;
    }

    public void setIzenaEdoSoziala(String izenaEdoSoziala) {
        this.izena = izenaEdoSoziala;
    }

    public String getIfzNan() {
        return this.nanIfz;
    }

    public void setIfzNan(String ifzNan) {
        this.nanIfz = ifzNan;
    }

    public String getSexua() {
        return sexua;
    }

    public void setSexua(String sexua) {
        this.sexua = sexua;
    }

    public String getBezeroOrdainketaTxartela() {
        return bezeroOrdainketaTxartela;
    }

    public void setBezeroOrdainketaTxartela(String bezeroOrdainketaTxartela) {
        this.bezeroOrdainketaTxartela = bezeroOrdainketaTxartela;
    }

   
}
