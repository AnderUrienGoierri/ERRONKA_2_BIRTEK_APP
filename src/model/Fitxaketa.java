package model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Fitxaketa {
    private int idFitxaketa;
    private int langileaId;
    private Date data;
    private Time ordua;
    private String mota;
    private Timestamp eguneratzeData;

    public Fitxaketa(int idFitxaketa, int langileaId, Date data, Time ordua, String mota, Timestamp eguneratzeData) {
        this.idFitxaketa = idFitxaketa;
        this.langileaId = langileaId;
        this.data = data;
        this.ordua = ordua;
        this.mota = mota;
        this.eguneratzeData = eguneratzeData;
    }

    public int getIdFitxaketa() { return idFitxaketa; }
    public void setIdFitxaketa(int idFitxaketa) { this.idFitxaketa = idFitxaketa; }

    public int getLangileaId() { return langileaId; }
    public void setLangileaId(int langileaId) { this.langileaId = langileaId; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public Time getOrdua() { return ordua; }
    public void setOrdua(Time ordua) { this.ordua = ordua; }

    public String getMota() { return mota; }
    public void setMota(String mota) { this.mota = mota; }

    public Timestamp getEguneratzeData() { return eguneratzeData; }
    public void setEguneratzeData(Timestamp eguneratzeData) { this.eguneratzeData = eguneratzeData; }
}
