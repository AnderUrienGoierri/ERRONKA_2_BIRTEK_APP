package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Superklasea langile, bezero eta hornitzaileen atributu komunak gordetzeko.
 */
public abstract class Pertsona {
    protected int id;
    protected String izena;
    protected String abizena;
    protected String nanIfz;
    protected Date jaiotzaData;
    protected String helbidea;
    protected int herriaId;
    protected String postaKodea;
    protected String telefonoa;
    protected String emaila;
    protected String hizkuntza;
    protected String pasahitza;
    protected boolean aktibo;
    protected Timestamp altaData;
    protected Timestamp eguneratzeData;

    public Pertsona(int id, String izena, String abizena, String nanIfz, Date jaiotzaData, String helbidea,
            int herriaId, String postaKodea, String telefonoa, String emaila, String hizkuntza,
            String pasahitza, boolean aktibo, Timestamp altaData, Timestamp eguneratzeData) {
        this.id = id;
        this.izena = izena;
        this.abizena = abizena;
        this.nanIfz = nanIfz;
        this.jaiotzaData = jaiotzaData;
        this.helbidea = helbidea;
        this.herriaId = herriaId;
        this.postaKodea = postaKodea;
        this.telefonoa = telefonoa;
        this.emaila = emaila;
        this.hizkuntza = hizkuntza;
        this.pasahitza = pasahitza;
        this.aktibo = aktibo;
        this.altaData = altaData;
        this.eguneratzeData = eguneratzeData;
    }

    // Getters eta Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public String getAbizena() {
        return abizena;
    }

    public void setAbizena(String abizena) {
        this.abizena = abizena;
    }

    public String getNanIfz() {
        return nanIfz;
    }

    public void setNanIfz(String nanIfz) {
        this.nanIfz = nanIfz;
    }

    public Date getJaiotzaData() {
        return jaiotzaData;
    }

    public void setJaiotzaData(Date jaiotzaData) {
        this.jaiotzaData = jaiotzaData;
    }

    public String getHelbidea() {
        return helbidea;
    }

    public void setHelbidea(String helbidea) {
        this.helbidea = helbidea;
    }

    public int getHerriaId() {
        return herriaId;
    }

    public void setHerriaId(int herriaId) {
        this.herriaId = herriaId;
    }

    public String getPostaKodea() {
        return postaKodea;
    }

    public void setPostaKodea(String postaKodea) {
        this.postaKodea = postaKodea;
    }

    public String getTelefonoa() {
        return telefonoa;
    }

    public void setTelefonoa(String telefonoa) {
        this.telefonoa = telefonoa;
    }

    public String getEmaila() {
        return emaila;
    }

    public void setEmaila(String emaila) {
        this.emaila = emaila;
    }

    public String getHizkuntza() {
        return hizkuntza;
    }

    public void setHizkuntza(String hizkuntza) {
        this.hizkuntza = hizkuntza;
    }

    public String getPasahitza() {
        return pasahitza;
    }

    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }

    public boolean isAktibo() {
        return aktibo;
    }

    public void setAktibo(boolean aktibo) {
        this.aktibo = aktibo;
    }

    public Timestamp getAltaData() {
        return altaData;
    }

    public void setAltaData(Timestamp altaData) {
        this.altaData = altaData;
    }

    public Timestamp getEguneratzeData() {
        return eguneratzeData;
    }

    public void setEguneratzeData(Timestamp eguneratzeData) {
        this.eguneratzeData = eguneratzeData;
    }
}
