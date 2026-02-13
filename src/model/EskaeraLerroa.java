package model;

import java.math.BigDecimal;

/**
 * EskaeraLerroa klasea.
 * Eskaera baten barruko lerro bakoitza kudeatzen du.
 * Produktuaren IDa, kantitatea, prezioa eta egoera gordetzen ditu.
 */
public class EskaeraLerroa {
    private int idEskaeraLerroa;
    private int eskaeraId;
    private int produktuaId;
    private int kantitatea;
    private BigDecimal unitatePrezioa;
    private String eskaeraLerroEgoera;

    /**
     * EskaeraLerroa eraikitzailea.
     *
     * @param idEskaeraLerroa    Lerroaren IDa.
     * @param eskaeraId          Eskaeraren IDa.
     * @param produktuaId        Produktuaren IDa.
     * @param kantitatea         Produktu kantitatea.
     * @param unitatePrezioa     Unitateko prezioa.
     * @param eskaeraLerroEgoera Lerroaren egoera.
     */
    public EskaeraLerroa(int idEskaeraLerroa, int eskaeraId, int produktuaId, int kantitatea, BigDecimal unitatePrezioa,
            String eskaeraLerroEgoera) {
        this.idEskaeraLerroa = idEskaeraLerroa;
        this.eskaeraId = eskaeraId;
        this.produktuaId = produktuaId;
        this.kantitatea = kantitatea;
        this.unitatePrezioa = unitatePrezioa;
        this.eskaeraLerroEgoera = eskaeraLerroEgoera;
    }

    /**
     * Lerroaren IDa lortzen du.
     *
     * @return IDa.
     */
    public int getIdEskaeraLerroa() {
        return idEskaeraLerroa;
    }

    /**
     * Lerroaren IDa ezartzen du.
     *
     * @param idEskaeraLerroa ID berria.
     */
    public void setIdEskaeraLerroa(int idEskaeraLerroa) {
        this.idEskaeraLerroa = idEskaeraLerroa;
    }

    /**
     * Eskaeraren IDa lortzen du.
     *
     * @return Eskaeraren IDa.
     */
    public int getEskaeraId() {
        return eskaeraId;
    }

    /**
     * Eskaeraren IDa ezartzen du.
     *
     * @param eskaeraId Eskaeraren ID berria.
     */
    public void setEskaeraId(int eskaeraId) {
        this.eskaeraId = eskaeraId;
    }

    /**
     * Produktuaren IDa lortzen du.
     *
     * @return Produktuaren IDa.
     */
    public int getProduktuaId() {
        return produktuaId;
    }

    /**
     * Produktuaren IDa ezartzen du.
     *
     * @param produktuaId Produktuaren ID berria.
     */
    public void setProduktuaId(int produktuaId) {
        this.produktuaId = produktuaId;
    }

    /**
     * Kantitatea lortzen du.
     *
     * @return Kantitatea.
     */
    public int getKantitatea() {
        return kantitatea;
    }

    /**
     * Kantitatea ezartzen du.
     *
     * @param kantitatea Kantitate berria.
     */
    public void setKantitatea(int kantitatea) {
        this.kantitatea = kantitatea;
    }

    /**
     * Unitateko prezioa lortzen du.
     *
     * @return Prezioa.
     */
    public BigDecimal getUnitatePrezioa() {
        return unitatePrezioa;
    }

    /**
     * Unitateko prezioa ezartzen du.
     *
     * @param unitatePrezioa Prezio berria.
     */
    public void setUnitatePrezioa(BigDecimal unitatePrezioa) {
        this.unitatePrezioa = unitatePrezioa;
    }

    /**
     * Lerroaren egoera lortzen du.
     *
     * @return Egoera.
     */
    public String getEskaeraLerroEgoera() {
        return eskaeraLerroEgoera;
    }

    /**
     * Lerroaren egoera ezartzen du.
     *
     * @param eskaeraLerroEgoera Egoera berria.
     */
    public void setEskaeraLerroEgoera(String eskaeraLerroEgoera) {
        this.eskaeraLerroEgoera = eskaeraLerroEgoera;
    }

    // -------------------------------------------------------------------------
    // KUDEAKETA METODOAK (STATIC)
    // -------------------------------------------------------------------------

    /**
     * Eskaera lerroa sortu.
     *
     * @param el EskaeraLerroa objektua
     */
    /**
     * Eskaera lerroa sortu datu-basean.
     *
     * @param el EskaeraLerroa objektua.
     * @throws java.sql.SQLException Datu-basean errorea gertatzen bada.
     */
    public static void eskaeraLerroaSortu(EskaeraLerroa el) throws java.sql.SQLException {
        String sql = "INSERT INTO eskaera_lerroak (eskaera_id, produktua_id, kantitatea, unitate_prezioa, eskaera_lerro_egoera) "
                    +"VALUES (?, ?, ?, ?, ?)";
        try (java.sql.Connection konexioa = db.DB_Konexioa.konektatu();
                java.sql.PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, el.getEskaeraId());
            pst.setInt(2, el.getProduktuaId());
            pst.setInt(3, el.getKantitatea());
            pst.setBigDecimal(4, el.getUnitatePrezioa());
            pst.setString(5, el.getEskaeraLerroEgoera());
            pst.executeUpdate();
        }
    }

    /**
     * Eskaera lerroa editatu.
     *
     * @param el EskaeraLerroa objektua (id-a barne)
     */
    /**
     * Eskaera lerroa editatu datu-basean.
     *
     * @param el EskaeraLerroa objektua (id-a barne).
     * @throws java.sql.SQLException Datu-basean errorea gertatzen bada.
     */
    public static void eskaeralerroaEditatu(EskaeraLerroa el) throws java.sql.SQLException {
        String sql = "UPDATE eskaera_lerroak "
                    +"SET eskaera_id=?, produktua_id=?, kantitatea=?, unitate_prezioa=?, eskaera_lerro_egoera=? "
                    +"WHERE id_eskaera_lerroa=?";
        try (java.sql.Connection konexioa = db.DB_Konexioa.konektatu();
                java.sql.PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, el.getEskaeraId());
            pst.setInt(2, el.getProduktuaId());
            pst.setInt(3, el.getKantitatea());
            pst.setBigDecimal(4, el.getUnitatePrezioa());
            pst.setString(5, el.getEskaeraLerroEgoera());
            pst.setInt(6, el.getIdEskaeraLerroa());
            pst.executeUpdate();
        }
    }

    /**
     * Eskaera lerroa ezabatu.
     *
     * @param idEskaeraLerroa Eskaera lerroaren IDa
     */
    /**
     * Eskaera lerroa ezabatu datu-basetik.
     *
     * @param idEskaeraLerroa Eskaera lerroaren IDa.
     * @throws java.sql.SQLException Datu-basean errorea gertatzen bada.
     */
    public static void eskaeraLerroaEzabatu(int idEskaeraLerroa) throws java.sql.SQLException {
        String sql = "DELETE FROM eskaera_lerroak WHERE id_eskaera_lerroa=?";
        try (java.sql.Connection konexioa = db.DB_Konexioa.konektatu();
                java.sql.PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idEskaeraLerroa);
            pst.executeUpdate();
        }
    }

    /**
     * Eskaera lerroen informazioa ikusi (Eskaera batenak).
     *
     * @param idEskaera Eskaeraren IDa
     * @return Lerro zerrenda
     */
    /**
     * Eskaera lerroen informazioa ikusi (Eskaera batenak).
     *
     * @param idEskaera Eskaeraren IDa.
     * @return Eskaera lerro zerrenda.
     * @throws java.sql.SQLException Datu-basean errorea gertatzen bada.
     */
    public static java.util.List<EskaeraLerroa> eskaeraLerroaIkusi(int idEskaera) throws java.sql.SQLException {
        java.util.List<EskaeraLerroa> lerroak = new java.util.ArrayList<>();
        String sql = "SELECT * FROM eskaera_lerroak WHERE eskaera_id=?";
        try (java.sql.Connection konexioa = db.DB_Konexioa.konektatu();
                java.sql.PreparedStatement pst = konexioa.prepareStatement(sql)) {
            pst.setInt(1, idEskaera);
            try (java.sql.ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    lerroak.add(new EskaeraLerroa(
                            rs.getInt("id_eskaera_lerroa"),
                            rs.getInt("eskaera_id"),
                            rs.getInt("produktua_id"),
                            rs.getInt("kantitatea"),
                            rs.getBigDecimal("unitate_prezioa"),
                            rs.getString("eskaera_lerro_egoera")));
                }
            }
        }
        return lerroak;
    }
}
