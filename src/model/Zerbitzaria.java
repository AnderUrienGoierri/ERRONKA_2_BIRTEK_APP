package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Zerbitzaria klasea.
 * Produktua klasearen azpiklasea da eta zerbitzari motako produktuen
 * ezaugarriak gordetzen ditu.
 * Adibidez: prozesadore nukleoak, RAM mota, disko badiak...
 */
public class Zerbitzaria extends Produktua {
    private int prozesadoreNukleoak;
    private String ramMota;
    private int diskoBadiak;
    private int rackUnitateak;
    private boolean elikatzeIturriErredundantea;
    private String raidKontroladora;

    /**
     * Zerbitzaria eraikitzailea.
     *
     * @param idProduktua                 Produktuaren IDa.
     * @param hornitzaileId               Hornitzailearen IDa.
     * @param kategoriaId                 Kategoriaren IDa.
     * @param izena                       Izena.
     * @param marka                       Marka.
     * @param mota                        Mota.
     * @param deskribapena                Deskribapena.
     * @param irudiaUrl                   Irudiaren URLa.
     * @param biltegiId                   Biltegiaren IDa.
     * @param produktuEgoera              Produktuaren egoera.
     * @param produktuEgoeraOharra        Egoeraren oharra.
     * @param salgai                      Salgai dagoen.
     * @param salmentaPrezioa             Salmenta prezioa.
     * @param stock                       Stock kopurua.
     * @param eskaintza                   Eskaintza prezioa.
     * @param zergakEhunekoa              Zergen ehunekoa.
     * @param sortzeData                  Sortze data.
     * @param eguneratzeData              Eguneratze data.
     * @param prozesadoreNukleoak         Prozesadorearen nukleo kopurua.
     * @param ramMota                     RAM mota.
     * @param diskoBadiak                 Disko badia kopurua.
     * @param rackUnitateak               Rack unitate kopurua (U).
     * @param elikatzeIturriErredundantea Elikatze iturri erredundantea duen.
     * @param raidKontroladora            RAID kontroladora mota.
     */
    public Zerbitzaria(int idProduktua, int hornitzaileId, int kategoriaId, String izena, String marka, String mota,
            String deskribapena, String irudiaUrl, Integer biltegiId, String produktuEgoera,
            String produktuEgoeraOharra, boolean salgai, BigDecimal salmentaPrezioa, int stock,
            BigDecimal eskaintza, BigDecimal zergakEhunekoa, Timestamp sortzeData, Timestamp eguneratzeData,
            int prozesadoreNukleoak, String ramMota, int diskoBadiak, int rackUnitateak,
            boolean elikatzeIturriErredundantea, String raidKontroladora) {
        super(idProduktua, hornitzaileId, kategoriaId, izena, marka, mota, deskribapena, irudiaUrl, biltegiId,
                produktuEgoera, produktuEgoeraOharra, salgai, salmentaPrezioa, stock, eskaintza, zergakEhunekoa,
                sortzeData, eguneratzeData);
        // hemen soilik zerbitzarien ezaugarriak ezartzen dira
        this.prozesadoreNukleoak = prozesadoreNukleoak;
        this.ramMota = ramMota;
        this.diskoBadiak = diskoBadiak;
        this.rackUnitateak = rackUnitateak;
        this.elikatzeIturriErredundantea = elikatzeIturriErredundantea;
        this.raidKontroladora = raidKontroladora;
    }

    /**
     * Prozesadore nukleo kopurua lortzen du.
     *
     * @return Nukleoak.
     */
    public int getProzesadoreNukleoak() {
        return prozesadoreNukleoak;
    }

    /**
     * Prozeadore nukleo kopurua ezartzen du.
     *
     * @param prozesadoreNukleoak Nukleo berriak.
     */
    public void setProzesadoreNukleoak(int prozesadoreNukleoak) {
        this.prozesadoreNukleoak = prozesadoreNukleoak;
    }

    /**
     * RAM mota lortzen du.
     *
     * @return RAM mota.
     */
    public String getRamMota() {
        return ramMota;
    }

    /**
     * RAM mota ezartzen du.
     *
     * @param ramMota Mota berria.
     */
    public void setRamMota(String ramMota) {
        this.ramMota = ramMota;
    }

    /**
     * Disko badiak lortzen ditu.
     *
     * @return Badiak.
     */
    public int getDiskoBadiak() {
        return diskoBadiak;
    }

    /**
     * Disko badiak ezartzen ditu.
     *
     * @param diskoBadiak Badia berriak.
     */
    public void setDiskoBadiak(int diskoBadiak) {
        this.diskoBadiak = diskoBadiak;
    }

    /**
     * Rack unitateak lortzen ditu.
     *
     * @return Rack unitateak.
     */
    public int getRackUnitateak() {
        return rackUnitateak;
    }

    /**
     * Rack unitateak ezartzen ditu.
     *
     * @param rackUnitateak Unitate berriak.
     */
    public void setRackUnitateak(int rackUnitateak) {
        this.rackUnitateak = rackUnitateak;
    }

    /**
     * Elikatze iturri erredundantea duen lortzen du.
     *
     * @return True badu.
     */
    public boolean isElikatzeIturriErredundantea() {
        return elikatzeIturriErredundantea;
    }

    /**
     * Elikatze iturri erredundantearen egoera ezartzen du.
     *
     * @param elikatzeIturriErredundantea Egoera berria.
     */
    public void setElikatzeIturriErredundantea(boolean elikatzeIturriErredundantea) {
        this.elikatzeIturriErredundantea = elikatzeIturriErredundantea;
    }

    /**
     * RAID kontroladora lortzen du.
     *
     * @return RAID kontroladora.
     */
    public String getRaidKontroladora() {
        return raidKontroladora;
    }

    /**
     * RAID kontroladora ezartzen du.
     *
     * @param raidKontroladora Kontroladora berria.
     */
    public void setRaidKontroladora(String raidKontroladora) {
        this.raidKontroladora = raidKontroladora;
    }
}
