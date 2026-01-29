package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Zerbitzaria extends Produktua {
    private int prozesadoreNukleoak;
    private String ramMota;
    private int diskoBadiak;
    private int rackUnitateak;
    private boolean elikatzeIturriErredundantea;
    private String raidKontroladora;

    public Zerbitzaria(int idProduktua, int hornitzaileId, int kategoriaId, String izena, String marka, String mota,
            String deskribapena, String irudiaUrl, Integer biltegiId, String produktuEgoera,
            String produktuEgoeraOharra, boolean salgai, BigDecimal salmentaPrezioa, int stock,
            BigDecimal eskaintza, BigDecimal zergakEhunekoa, Timestamp sortzeData, Timestamp eguneratzeData,
            int prozesadoreNukleoak, String ramMota, int diskoBadiak, int rackUnitateak,
            boolean elikatzeIturriErredundantea, String raidKontroladora) {
        super(idProduktua, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl, biltegiId,
                produktuEgoera, produktuEgoeraOharra, salgai, salmentaPrezioa, stock, eskaintza, zergakEhunekoa,
                sortzeData, eguneratzeData);
        this.prozesadoreNukleoak = prozesadoreNukleoak;
        this.ramMota = ramMota;
        this.diskoBadiak = diskoBadiak;
        this.rackUnitateak = rackUnitateak;
        this.elikatzeIturriErredundantea = elikatzeIturriErredundantea;
        this.raidKontroladora = raidKontroladora;
    }

    public int getProzesadoreNukleoak() {
        return prozesadoreNukleoak;
    }

    public void setProzesadoreNukleoak(int prozesadoreNukleoak) {
        this.prozesadoreNukleoak = prozesadoreNukleoak;
    }

    public String getRamMota() {
        return ramMota;
    }

    public void setRamMota(String ramMota) {
        this.ramMota = ramMota;
    }

    public int getDiskoBadiak() {
        return diskoBadiak;
    }

    public void setDiskoBadiak(int diskoBadiak) {
        this.diskoBadiak = diskoBadiak;
    }

    public int getRackUnitateak() {
        return rackUnitateak;
    }

    public void setRackUnitateak(int rackUnitateak) {
        this.rackUnitateak = rackUnitateak;
    }

    public boolean isElikatzeIturriErredundantea() {
        return elikatzeIturriErredundantea;
    }

    public void setElikatzeIturriErredundantea(boolean elikatzeIturriErredundantea) {
        this.elikatzeIturriErredundantea = elikatzeIturriErredundantea;
    }

    public String getRaidKontroladora() {
        return raidKontroladora;
    }

    public void setRaidKontroladora(String raidKontroladora) {
        this.raidKontroladora = raidKontroladora;
    }
}
