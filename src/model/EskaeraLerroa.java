package model;

import java.math.BigDecimal;

public class EskaeraLerroa {
    private int idEskaeraLerroa;
    private int eskaeraId;
    private int produktuaId;
    private int kantitatea;
    private BigDecimal unitatePrezioa;
    private String eskaeraLerroEgoera;

    public EskaeraLerroa(int idEskaeraLerroa, int eskaeraId, int produktuaId, int kantitatea, BigDecimal unitatePrezioa,
            String eskaeraLerroEgoera) {
        this.idEskaeraLerroa = idEskaeraLerroa;
        this.eskaeraId = eskaeraId;
        this.produktuaId = produktuaId;
        this.kantitatea = kantitatea;
        this.unitatePrezioa = unitatePrezioa;
        this.eskaeraLerroEgoera = eskaeraLerroEgoera;
    }

    public int getIdEskaeraLerroa() {
        return idEskaeraLerroa;
    }

    public void setIdEskaeraLerroa(int idEskaeraLerroa) {
        this.idEskaeraLerroa = idEskaeraLerroa;
    }

    public int getEskaeraId() {
        return eskaeraId;
    }

    public void setEskaeraId(int eskaeraId) {
        this.eskaeraId = eskaeraId;
    }

    public int getProduktuaId() {
        return produktuaId;
    }

    public void setProduktuaId(int produktuaId) {
        this.produktuaId = produktuaId;
    }

    public int getKantitatea() {
        return kantitatea;
    }

    public void setKantitatea(int kantitatea) {
        this.kantitatea = kantitatea;
    }

    public BigDecimal getUnitatePrezioa() {
        return unitatePrezioa;
    }

    public void setUnitatePrezioa(BigDecimal unitatePrezioa) {
        this.unitatePrezioa = unitatePrezioa;
    }

    public String getEskaeraLerroEgoera() {
        return eskaeraLerroEgoera;
    }

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
    public static void eskaeraLerroaSortu(EskaeraLerroa el) throws java.sql.SQLException {
        String sql = "INSERT INTO eskaera_lerroak (eskaera_id, produktua_id, kantitatea, unitate_prezioa, eskaera_lerro_egoera) "
                +
                "VALUES (?, ?, ?, ?, ?)";
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
    public static void eskaeralerroaEditatu(EskaeraLerroa el) throws java.sql.SQLException {
        String sql = "UPDATE eskaera_lerroak SET eskaera_id=?, produktua_id=?, kantitatea=?, " +
                "unitate_prezioa=?, eskaera_lerro_egoera=? WHERE id_eskaera_lerroa=?";
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
