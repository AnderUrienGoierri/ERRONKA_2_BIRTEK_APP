package model;

import db.DB_Konexioa;

import java.sql.Date;
import java.sql.Timestamp;

public class Langilea extends Pertsona implements IAutentifikagarria {
    private String iban;
    private byte[] kurrikuluma; // PDF formatuan
    private int sailaId;
    private String saltoTxartelaUid;

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

    public int getIdLangilea() {
        return this.id;
    }

    public void setIdLangilea(int idLangilea) {
        this.id = idLangilea;
    }

    public String getNan() {
        return this.nanIfz;
    }

    public void setNan(String nan) {
        this.nanIfz = nan;
    }

    public String getSaltoTxartelaUid() {
        return saltoTxartelaUid;
    }

    public void setSaltoTxartelaUid(String saltoTxartelaUid) {
        this.saltoTxartelaUid = saltoTxartelaUid;
    }

    public int getSailaId() {
        return sailaId;
    }

    public void setSailaId(int sailaId) {
        this.sailaId = sailaId;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public byte[] getKurrikuluma() {
        return kurrikuluma;
    }

    public void setKurrikuluma(byte[] kurrikuluma) {
        this.kurrikuluma = kurrikuluma;
    }

    // --- FITXAKETA LOGIKA (OOP) ---

    // Langile batatek fitxaketa egiteko metodoa
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

    public void sarreraFitxaketaEgin() throws java.sql.SQLException {
        fitxatu("Sarrera");
    }

    public void irteeraFitxaketaEgin() throws java.sql.SQLException {
        fitxatu("Irteera");
    }

    // langile batek bere fitxaketa historiala ikusteko metodoa (datuak itzultzen
    // ditu)
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

    public void nireLangileDatuakEditatu(String pasahitza, String hizkuntza, int herriaId)
            throws java.sql.SQLException {
        String sql = "UPDATE langileak SET pasahitza = ?, hizkuntza = ?, herria_id = ?, eguneratze_data = NOW() WHERE id_langilea = ?";
        try (java.sql.Connection konexioa = DB_Konexioa.konektatu();
                java.sql.PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setString(1, pasahitza);
            pst.setString(2, hizkuntza);
            pst.setInt(3, herriaId);
            pst.setInt(4, this.getIdLangilea());
            pst.executeUpdate();

            // Eguneratu objektua ere
            this.setPasahitza(pasahitza);
            this.setHizkuntza(hizkuntza);
            this.setHerriaId(herriaId);
        }
    }

    // langile batek bere fitxaketa historiala ikusteko metodoa (egoera testua):
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

    @Override
    public boolean autentifikatu(String pasahitza) {
        return this.getPasahitza() != null && this.getPasahitza().equals(pasahitza);
    }
}
