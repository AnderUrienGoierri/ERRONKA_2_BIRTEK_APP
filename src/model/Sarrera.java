package model;

import java.sql.Timestamp;

public class Sarrera {
    private int idSarrera;
    private Timestamp data;
    private Timestamp eguneratzeData;
    private int hornitzaileaId;
    private int langileaId;
    private String sarreraEgoera;

    public Sarrera(int idSarrera, Timestamp data, Timestamp eguneratzeData, int hornitzaileaId, int langileaId,
                   String sarreraEgoera) {
        this.idSarrera = idSarrera;
        this.data = data;
        this.eguneratzeData = eguneratzeData;
        this.hornitzaileaId = hornitzaileaId;
        this.langileaId = langileaId;
        this.sarreraEgoera = sarreraEgoera;
    }

    public int getIdSarrera() { return idSarrera; }
    public void setIdSarrera(int idSarrera) { this.idSarrera = idSarrera; }

    public Timestamp getData() { return data; }
    public void setData(Timestamp data) { this.data = data; }

    public Timestamp getEguneratzeData() { return eguneratzeData; }
    public void setEguneratzeData(Timestamp eguneratzeData) { this.eguneratzeData = eguneratzeData; }

    public int getHornitzaileaId() { return hornitzaileaId; }
    public void setHornitzaileaId(int hornitzaileaId) { this.hornitzaileaId = hornitzaileaId; }

    public int getLangileaId() { return langileaId; }
    public void setLangileaId(int langileaId) { this.langileaId = langileaId; }

    public String getSarreraEgoera() { return sarreraEgoera; }
    public void setSarreraEgoera(String sarreraEgoera) { this.sarreraEgoera = sarreraEgoera; }
}
