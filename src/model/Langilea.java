package model;

import db.DB_Konexioa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Langilea klasea.
 * Pertsona klasearen azpiklasea da, eta langileen informazio espezifikoa
 * kudeatzen du.
 * Fitxaketak egiteko, bere datuak ikusteko eta editatzeko metodoak ditu.
 */
public class Langilea extends Pertsona {
    private String iban;
    private byte[] kurrikuluma; // PDF formatuan
    private int sailaId;
    private String saltoTxartelaUid;

    /**
     * Langilea eraikitzailea.
     *
     * @param idLangilea       Langilearen IDa.
     * @param izena            Izena.
     * @param abizena          Abizena.
     * @param nan              NAN zenbakia.
     * @param jaiotzaData      Jaiotza data.
     * @param herriaId         Herriaren IDa.
     * @param helbidea         Helbidea.
     * @param postaKodea       Posta kodea.
     * @param telefonoa        Telefonoa.
     * @param emaila           Emaila.
     * @param hizkuntza        Hizkuntza.
     * @param pasahitza        Pasahitza.
     * @param saltoTxartelaUid Salto txartelaren UIDa.
     * @param altaData         Alta data.
     * @param eguneratzeData   Eguneratze data.
     * @param aktibo           Aktibo dagoen.
     * @param sailaId          Sailaren IDa.
     * @param iban             IBAN kontu korrontea.
     * @param kurrikuluma      Kurrikuluma PDF formatuan (byte array).
     */
    public Langilea(int idLangilea, String izena, String abizena, String nan, Date jaiotzaData, int herriaId,
            String helbidea, String postaKodea, String telefonoa, String emaila, String hizkuntza,
            String pasahitza, String saltoTxartelaUid, Timestamp altaData, Timestamp eguneratzeData,
            boolean aktibo, int sailaId, String iban, byte[] kurrikuluma) {
        super(idLangilea, izena, abizena, nan, jaiotzaData, helbidea, herriaId, postaKodea, telefonoa, emaila,
                hizkuntza, pasahitza, aktibo, altaData, eguneratzeData);
        this.sailaId = sailaId;
        this.iban = iban;
        this.kurrikuluma = kurrikuluma;
    }

    /**
     * Langilearen IDa lortzen du.
     * 
     * @return IDa.
     */
    public int getIdLangilea() {
        return this.id;
    }

    /**
     * Langilearen IDa ezartzen du.
     * 
     * @param idLangilea ID berria.
     */
    public void setIdLangilea(int idLangilea) {
        this.id = idLangilea;
    }

    /**
     * Langilearen NANa lortzen du.
     * 
     * @return NAN zenbakia.
     */
    public String getNan() {
        return this.nanIfz;
    }

    /**
     * Langilearen NANa ezartzen du.
     * 
     * @param nan NAN berria.
     */
    public void setNan(String nan) {
        this.nanIfz = nan;
    }

    /**
     * Salto txartelaren UIDa lortzen du.
     * 
     * @return UIDa.
     */
    public String getSaltoTxartelaUid() {
        return saltoTxartelaUid;
    }

    /**
     * Salto txartelaren UIDa ezartzen du.
     * 
     * @param saltoTxartelaUid UID berria.
     */
    public void setSaltoTxartelaUid(String saltoTxartelaUid) {
        this.saltoTxartelaUid = saltoTxartelaUid;
    }

    /**
     * Sailaren IDa lortzen du.
     * 
     * @return Sailaren IDa.
     */
    public int getSailaId() {
        return sailaId;
    }

    /**
     * Sailaren IDa ezartzen du.
     * 
     * @param sailaId Sailaren ID berria.
     */
    public void setSailaId(int sailaId) {
        this.sailaId = sailaId;
    }

    /**
     * IBAN kontu korrontea lortzen du.
     * 
     * @return IBANa.
     */
    public String getIban() {
        return iban;
    }

    /**
     * IBAN kontu korrontea ezartzen du.
     * 
     * @param iban IBAN berria.
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * Kurrikuluma lortzen du.
     * 
     * @return Kurrikuluma byte array bezala.
     */
    public byte[] getKurrikuluma() {
        return kurrikuluma;
    }

    /**
     * Kurrikuluma ezartzen du.
     * 
     * @param kurrikuluma Byte array berria.
     */
    public void setKurrikuluma(byte[] kurrikuluma) {
        this.kurrikuluma = kurrikuluma;
    }

    // --- FITXAKETA LOGIKA (OOP) ---

    // Langile batentzat fitxaketa egiteko metodoa
    /**
     * Fitxaketa (Sarrera edo Irteera) bat erregistratzen du.
     * Lehenik egiaztatzen du ea langilea barruan edo kanpoan dagoen, egoera
     * koherentea izateko.
     *
     * @param fitxaketa_mota "Sarrera" edo "Irteera".
     * @throws java.sql.SQLException Datu-basean errorea gertatzen bada edo egoera
     *                               ez bada zuzena (adib. bi aldiz sartzen
     *                               saiatzea).
     */
    public void fitxatu(String fitxaketa_mota) throws java.sql.SQLException {
        String galdera = "SELECT mota FROM fitxaketak WHERE langilea_id = ? ORDER BY id_fitxaketa DESC LIMIT 1";
        try (java.sql.Connection konexioa = DB_Konexioa.konektatu()) {
            boolean barruan = false;
            try (java.sql.PreparedStatement sententzia = konexioa.prepareStatement(galdera)) {
                sententzia.setInt(1, this.getIdLangilea());
                try (java.sql.ResultSet rs = sententzia.executeQuery()) {
                    String azkenMota = null;
                    if (rs.next()) {
                        azkenMota = rs.getString("mota");
                    }

                    // Ezarri barruan aldagaia
                    if ("Sarrera".equals(azkenMota)) {
                        barruan = true;
                    } else {
                        barruan = false;
                    }

                    // Balidazioak barruan aldagaia erabiliz
                    if ("Sarrera".equals(fitxaketa_mota) && barruan) {
                        throw new java.sql.SQLException("Jada barruan zaude.");
                    }
                    if ("Irteera".equals(fitxaketa_mota) && !barruan) {
                        throw new java.sql.SQLException("Jada kanpoan zaude. Ezin duzu irten sartu gabe.");
                    }
                }
            }
            String sartuGaldera = "INSERT INTO fitxaketak (langilea_id, mota, data, ordua) VALUES (?, ?, CURRENT_DATE, CURRENT_TIME)";
            try (java.sql.PreparedStatement pstInsert = konexioa.prepareStatement(sartuGaldera)) {
                pstInsert.setInt(1, this.getIdLangilea());
                pstInsert.setString(2, fitxaketa_mota);
                pstInsert.executeUpdate();
            }
        }
    }

    /**
     * Sarrera fitxaketa bat egiten du.
     * 
     * @throws java.sql.SQLException Errorea gertatzen bada.
     */
    public void sarreraFitxaketaEgin() throws java.sql.SQLException {
        fitxatu("Sarrera");
    }

    /**
     * Irteera fitxaketa bat egiten du.
     * 
     * @throws java.sql.SQLException Errorea gertatzen bada.
     */
    public void irteeraFitxaketaEgin() throws java.sql.SQLException {
        fitxatu("Irteera");
    }

    // langile batek bere fitxaketa historiala ikusteko metodoa (datuak itzultzen
    // ditu)
    /**
     * Langilearen fitxaketa historiala lortzen du.
     *
     * @return Fitxaketa zerrenda bat.
     */
    public java.util.List<Fitxaketa> nireFitxaketakIkusi() {
        java.util.List<Fitxaketa> zerrenda = new java.util.ArrayList<>();
        String galdera = "SELECT id_fitxaketa, langilea_id, data, ordua, mota, eguneratze_data FROM fitxaketak WHERE langilea_id = ? ORDER BY id_fitxaketa DESC";
        try (java.sql.Connection konexioa = DB_Konexioa.konektatu();
                java.sql.PreparedStatement sententzia = konexioa.prepareStatement(galdera)) {
            sententzia.setInt(1, this.getIdLangilea());
            java.sql.ResultSet rs = sententzia.executeQuery();
            while (rs.next()) {
                zerrenda.add(new Fitxaketa(
                        rs.getInt("id_fitxaketa"),
                        rs.getInt("langilea_id"),
                        rs.getDate("data"),
                        rs.getTime("ordua"),
                        rs.getString("mota"),
                        rs.getTimestamp("eguneratze_data")));
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return zerrenda;
    }

    /**
     * Herrien zerrenda lortzen du.
     *
     * @return Herria objektuen zerrenda.
     */
    public List<Herria> herriakLortu() {
        List<Herria> zerrenda = new ArrayList<>();
        String sql = "SELECT * FROM herriak ORDER BY izena";
        try (java.sql.Connection konexioa = DB_Konexioa.konektatu();
                Statement st = konexioa.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                zerrenda.add(new Herria(
                        rs.getInt("id_herria"),
                        rs.getString("izena"),
                        rs.getString("lurraldea"),
                        rs.getString("nazioa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zerrenda;
    }

    /**
     * Herri berri bat sortzen du datu-basean.
     *
     * @param h Herria objektua.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void herriaSortu(Herria h) throws SQLException {
        String sql = "INSERT INTO herriak (izena, lurraldea, nazioa) VALUES (?, ?, ?)";
        try (java.sql.Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setString(1, h.getIzena());
            pst.setString(2, h.getLurraldea());
            pst.setString(3, h.getNazioa());
            pst.executeUpdate();
        }
    }

    /**
     * Langilearen oinarrizko datuak editatzen ditu (perfil aldaketa).
     *
     * @param pasahitza Pasahitz berria.
     * @param hizkuntza Hizkuntza berria.
     * @param herriaId  Herriaren ID berria.
     * @param telefonoa Telefono berria.
     * @param helbidea  Helbide berria.
     * @throws java.sql.SQLException Datu-basean errorea gertatzen bada.
     */
    public void nireLangileDatuakEditatu(String pasahitza, String hizkuntza, int herriaId, String telefonoa,
            String helbidea)
            throws java.sql.SQLException {
        String sql = "UPDATE langileak SET pasahitza = ?, hizkuntza = ?, herria_id = ?, telefonoa = ?, helbidea = ?, eguneratze_data = NOW() "
        		+ "WHERE id_langilea = ?";
        try (java.sql.Connection konexioa = DB_Konexioa.konektatu();
                java.sql.PreparedStatement sententzia = konexioa.prepareStatement(sql)) {
            sententzia.setString(1, pasahitza);
            sententzia.setString(2, hizkuntza);
            sententzia.setInt(3, herriaId);
            sententzia.setString(4, telefonoa);
            sententzia.setString(5, helbidea);
            sententzia.setInt(6, this.getIdLangilea());
            sententzia.executeUpdate();

            // Objektuaren datuak eguneratu
            this.setPasahitza(pasahitza);
            this.setHizkuntza(hizkuntza);
            this.setHerriaId(herriaId);
            this.setTelefonoa(telefonoa);
            this.setHelbidea(helbidea);
        }
    }

    
    // langile batek bere fitxaketak historiala ikusteko metodoa:
    
    /**
     * Langilearen azken fitxaketa egoera lortzen du.
     *
     * @return Testu deskribatzailea ("BARRUAN" edo "KANPOAN" eta ordua).
     */
    public String getFitxaketaEgoera() {
        String galdera = "SELECT mota, data, ordua FROM fitxaketak WHERE langilea_id = ? ORDER BY id_fitxaketa DESC LIMIT 1";
        try (java.sql.Connection konexioa = DB_Konexioa.konektatu();
                java.sql.PreparedStatement sententzia = konexioa.prepareStatement(galdera)) {
            sententzia.setInt(1, this.getIdLangilea());
            java.sql.ResultSet rs = sententzia.executeQuery();
            if (rs.next()) {
                String mota = rs.getString("mota");
                java.sql.Date data = rs.getDate("data");
                java.sql.Time ordua = rs.getTime("ordua");
                if ("Sarrera".equals(mota)) {
                    return " BARRUAN (" + ordua + ") Data: (" + data + ")";
                } else {
                    return " KANPOAN (" + ordua + ") Data: (" + data + ")";
                }
            } else {
                return "Ez dago erregistrorik.";
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return "Errorea egoera lortzean";
        }
    }

}
