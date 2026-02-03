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

    /**
     * Pertsona eraikitzailea.
     *
     * @param id             IDa.
     * @param izena          Izena.
     * @param abizena        Abizena.
     * @param nanIfz         IFZ edo NAN.
     * @param jaiotzaData    Jaiotza data.
     * @param helbidea       Helbidea.
     * @param herriaId       Herriaren IDa.
     * @param postaKodea     Posta kodea.
     * @param telefonoa      Telefonoa.
     * @param emaila         Emaila.
     * @param hizkuntza      Hizkuntza.
     * @param pasahitza      Pasahitza.
     * @param aktibo         Aktibo dagoen.
     * @param altaData       Alta data.
     * @param eguneratzeData Eguneratze data.
     */
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
    /**
     * IDa lortzen du.
     * 
     * @return IDa.
     */
    public int getId() {
        return id;
    }

    /**
     * IDa ezartzen du.
     * 
     * @param id ID berria.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Izena lortzen du.
     * 
     * @return Izena.
     */
    public String getIzena() {
        return izena;
    }

    /**
     * Izena ezartzen du.
     * 
     * @param izena Izen berria.
     */
    public void setIzena(String izena) {
        this.izena = izena;
    }

    /**
     * Abizena lortzen du.
     * 
     * @return Abizena.
     */
    public String getAbizena() {
        return abizena;
    }

    /**
     * Abizena ezartzen du.
     * 
     * @param abizena Abizen berria.
     */
    public void setAbizena(String abizena) {
        this.abizena = abizena;
    }

    /**
     * NAN edo IFZ lortzen du.
     * 
     * @return NAN edo IFZ.
     */
    public String getNanIfz() {
        return nanIfz;
    }

    /**
     * NAN edo IFZ ezartzen du.
     * 
     * @param nanIfz NAN edo IFZ berria.
     */
    public void setNanIfz(String nanIfz) {
        this.nanIfz = nanIfz;
    }

    /**
     * Jaiotza data lortzen du.
     * 
     * @return Jaiotza data.
     */
    public Date getJaiotzaData() {
        return jaiotzaData;
    }

    /**
     * Jaiotza data ezartzen du.
     * 
     * @param jaiotzaData Jaiotza data berria.
     */
    public void setJaiotzaData(Date jaiotzaData) {
        this.jaiotzaData = jaiotzaData;
    }

    /**
     * Helbidea lortzen du.
     * 
     * @return Helbidea.
     */
    public String getHelbidea() {
        return helbidea;
    }

    /**
     * Helbidea ezartzen du.
     * 
     * @param helbidea Helbide berria.
     */
    public void setHelbidea(String helbidea) {
        this.helbidea = helbidea;
    }

    /**
     * Herriaren IDa lortzen du.
     * 
     * @return Herriaren IDa.
     */
    public int getHerriaId() {
        return herriaId;
    }

    /**
     * Herriaren IDa ezartzen du.
     * 
     * @param herriaId Herriaren ID berria.
     */
    public void setHerriaId(int herriaId) {
        this.herriaId = herriaId;
    }

    /**
     * Posta kodea lortzen du.
     * 
     * @return Posta kodea.
     */
    public String getPostaKodea() {
        return postaKodea;
    }

    /**
     * Posta kodea ezartzen du.
     * 
     * @param postaKodea Posta kode berria.
     */
    public void setPostaKodea(String postaKodea) {
        this.postaKodea = postaKodea;
    }

    /**
     * Telefonoa lortzen du.
     * 
     * @return Telefonoa.
     */
    public String getTelefonoa() {
        return telefonoa;
    }

    /**
     * Telefonoa ezartzen du.
     * 
     * @param telefonoa Telefono berria.
     */
    public void setTelefonoa(String telefonoa) {
        this.telefonoa = telefonoa;
    }

    /**
     * Emaila lortzen du.
     * 
     * @return Emaila.
     */
    public String getEmaila() {
        return emaila;
    }

    /**
     * Emaila ezartzen du.
     * 
     * @param emaila Email berria.
     */
    public void setEmaila(String emaila) {
        this.emaila = emaila;
    }

    /**
     * Hizkuntza lortzen du.
     * 
     * @return Hizkuntza.
     */
    public String getHizkuntza() {
        return hizkuntza;
    }

    /**
     * Hizkuntza ezartzen du.
     * 
     * @param hizkuntza Hizkuntza berria.
     */
    public void setHizkuntza(String hizkuntza) {
        this.hizkuntza = hizkuntza;
    }

    /**
     * Pasahitza lortzen du.
     * 
     * @return Pasahitza.
     */
    public String getPasahitza() {
        return pasahitza;
    }

    /**
     * Pasahitza ezartzen du.
     * 
     * @param pasahitza Pasahitz berria.
     */
    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }

    /**
     * Aktibo dagoen lortzen du.
     * 
     * @return True aktibo badago, false bestela.
     */
    public boolean isAktibo() {
        return aktibo;
    }

    /**
     * Aktibo egoera ezartzen du.
     * 
     * @param aktibo Aktibo egoera berria.
     */
    public void setAktibo(boolean aktibo) {
        this.aktibo = aktibo;
    }

    /**
     * Alta data lortzen du.
     * 
     * @return Alta data.
     */
    public Timestamp getAltaData() {
        return altaData;
    }

    /**
     * Alta data ezartzen du.
     * 
     * @param altaData Alta data berria.
     */
    public void setAltaData(Timestamp altaData) {
        this.altaData = altaData;
    }

    /**
     * Eguneratze data lortzen du.
     * 
     * @return Eguneratze data.
     */
    public Timestamp getEguneratzeData() {
        return eguneratzeData;
    }

    /**
     * Eguneratze data ezartzen du.
     * 
     * @param eguneratzeData Eguneratze data berria.
     */
    public void setEguneratzeData(Timestamp eguneratzeData) {
        this.eguneratzeData = eguneratzeData;
    }
}
