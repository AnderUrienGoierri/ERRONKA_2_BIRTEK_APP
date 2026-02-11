package model;

import db.DB_Konexioa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.io.FileOutputStream;
import java.awt.Desktop;

/**
 * AdministrariLangilea klasea.
 * Langilea klasearen azpiklasea da, eta administrazio-lanak egiteko metodoak
 * ditu.
 * Langileak, sailak, fakturak eta bestelako entitateak kudeatzeko aukera ematen
 * du.
 */
public class AdministrariLangilea extends Langilea {

    /**
     * AdministrariLangileaeraikitzailea.
     * Langilea objektu batetik abiatuta AdministrariLangilea sortzen du.
     *
     * @param l Langilea objektua, oinarrizko datuekin.
     */
    public AdministrariLangilea(Langilea l) {
        super(l.getIdLangilea(), l.getIzena(), l.getAbizena(), l.getNan(), l.getJaiotzaData(), l.getHerriaId(),
                l.getHelbidea(), l.getPostaKodea(), l.getTelefonoa(), l.getEmaila(), l.getHizkuntza(),
                l.getPasahitza(), l.getSaltoTxartelaUid(), l.getAltaData(), l.getEguneratzeData(),
                l.isAktibo(), l.getSailaId(), l.getIban(), l.getKurrikuluma());
    }

    /**
     * Langile berri bat sortzen du datu-basean.
     *
     * @param izena            Langilearen izena.
     * @param abizena          Langilearen abizena.
     * @param nan              Langilearen NAN zenbakia.
     * @param emaila           Langilearen email helbidea.
     * @param pasahitza        Langilearen pasahitza.
     * @param sailaId          Langilea dagokion sailaren IDa.
     * @param helbidea         Langilearen helbidea.
     * @param herriaId         Langilea bizi den herriaren IDa.
     * @param postaKodea       Posta kodea.
     * @param telefonoa        Harremanetarako telefonoa.
     * @param jaiotzaData      Jaiotza data (YYYY-MM-DD formatuan).
     * @param hizkuntza        Hizkuntza lehenetsia.
     * @param saltoTxartelaUid Salto sistemako txartelaren UIDa.
     * @param aktibo           Langilea aktibo dagoen edo ez.
     * @param iban             Bankuko kontuaren IBAN zenbakia.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void langileaSortu(String izena, String abizena, String nan, String emaila, String pasahitza, int sailaId,
            String helbidea, int herriaId, String postaKodea, String telefonoa, String jaiotzaData,
            String hizkuntza, String saltoTxartelaUid, boolean aktibo, String iban)
            throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "INSERT INTO langileak (izena, abizena, nan, emaila, pasahitza, saila_id, helbidea, herria_idposta_kodea, telefonoa, jaiotza_data, hizkuntza, salto_txartela_uid, aktibo, iban)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, izena);
            pst.setString(2, abizena);
            pst.setString(3, nan);
            pst.setString(4, emaila);
            pst.setString(5, pasahitza);
            pst.setInt(6, sailaId);
            pst.setString(7, helbidea);
            pst.setInt(8, herriaId);
            pst.setString(9, postaKodea);
            pst.setString(10, telefonoa);
            pst.setString(11, jaiotzaData);
            pst.setString(12, hizkuntza);
            pst.setString(13, saltoTxartelaUid);
            pst.setBoolean(14, aktibo);
            pst.setString(15, iban);
            pst.executeUpdate();
        }
    }

    /**
     * Langile bat ezabatzen du datu-basetik.
     *
     * @param idLangilea Ezabatu nahi den langilearen IDa.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void langileaEzabatu(int idLangilea) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "DELETE FROM langileak WHERE id_langilea = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idLangilea);
            pst.executeUpdate();
        }
    }

    /**
     * Langile baten datuak eguneratzen ditu.
     *
     * @param idLangilea       Aldatu nahi den langilearen IDa.
     * @param izena            Izen berria.
     * @param abizena          Abizen berria.
     * @param nan              NAN berria.
     * @param emaila           Email berria.
     * @param sailaId          Sail berriaren IDa.
     * @param helbidea         Helbide berria.
     * @param herriaId         Herri berriaren IDa.
     * @param postaKodea       Posta kode berria.
     * @param telefonoa        Telefono berria.
     * @param jaiotzaData      Jaiotza data berria.
     * @param hizkuntza        Hizkuntza berria.
     * @param pasahitza        Pasahitz berria.
     * @param saltoTxartelaUid Salto txartelaren UID berria.
     * @param aktibo           Egoera (aktibo/ez-aktibo).
     * @param iban             IBAN berria.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void langileaEditatu(int idLangilea, String izena, String abizena, String nan, String emaila, int sailaId,
            String helbidea, int herriaId, String postaKodea, String telefonoa, String jaiotzaData,
            String hizkuntza, String pasahitza, String saltoTxartelaUid, boolean aktibo, String iban)
            throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "UPDATE langileak SET izena = ?, abizena = ?, nan = ?, emaila = ?, saila_id = ?, helbidea = ?, herria_id = ?, posta_kodea = ?, telefonoa = ?, jaiotza_data = ?, hizkuntza = ?, pasahitza = ?, salto_txartela_uid = ?, aktibo = ?, iban = ?, eguneratze_data = NOW() WHERE id_langilea = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, izena);
            pst.setString(2, abizena);
            pst.setString(3, nan);
            pst.setString(4, emaila);
            pst.setInt(5, sailaId);
            pst.setString(6, helbidea);
            pst.setInt(7, herriaId);
            pst.setString(8, postaKodea);
            pst.setString(9, telefonoa);
            pst.setString(10, jaiotzaData);
            pst.setString(11, hizkuntza);
            pst.setString(12, pasahitza);
            pst.setString(13, saltoTxartelaUid);
            pst.setBoolean(14, aktibo);
            pst.setString(15, iban);
            pst.setInt(16, idLangilea);
            pst.executeUpdate();
        }
    }

    /**
     * Langile baten datuak lortzen ditu bere IDa erabiliz.
     *
     * @param idLangilea Bilatu nahi den langilearen IDa.
     * @return Langilea objektua datuekin, edo null aurkitzen ez bada.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public Langilea langileaIkusi(int idLangilea) throws SQLException {
        Langilea langilea = null;
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "SELECT * FROM langileak WHERE id_langilea = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idLangilea);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                langilea = new Langilea(
                        rs.getInt("id_langilea"),
                        rs.getString("izena"),
                        rs.getString("abizena"),
                        rs.getString("nan"),
                        rs.getDate("jaiotza_data"),
                        rs.getInt("herria_id"),
                        rs.getString("helbidea"),
                        rs.getString("posta_kodea"),
                        rs.getString("telefonoa"),
                        rs.getString("emaila"),
                        rs.getString("hizkuntza"),
                        rs.getString("pasahitza"),
                        rs.getString("salto_txartela_uid"),
                        rs.getTimestamp("alta_data"),
                        rs.getTimestamp("eguneratze_data"),
                        rs.getBoolean("aktibo"),
                        rs.getInt("saila_id"),
                        rs.getString("iban"),
                        rs.getBytes("kurrikuluma"));
            }
        }
        return langilea;
    }

    /**
     * Bezero baten fakturaren datuak ikusteko metodoa.
     *
     * @param idFaktura Fakturaren IDa.
     * @return BezeroFaktura objektua, datuekin.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    /**
     * Eskaera bat ikusteko metodoa (fakturaren datuekin).
     *
     * @param idEskaera Eskaeraren IDa.
     * @return Eskaera objektua (faktura zenbakia eta URL barne).
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public Eskaera eskaeraIkusi(int idEskaera) throws SQLException {
        Eskaera eskaera = null;
        String sql = "SELECT * FROM eskaerak WHERE id_eskaera = ?";
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql)) {
            pst.setInt(1, idEskaera);
            try (java.sql.ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    eskaera = new Eskaera(
                            rs.getInt("id_eskaera"),
                            rs.getInt("bezeroa_id"),
                            (Integer) rs.getObject("langilea_id"),
                            rs.getTimestamp("data"),
                            rs.getTimestamp("eguneratze_data"),
                            rs.getBigDecimal("guztira_prezioa"),
                            rs.getString("faktura_zenbakia"),
                            rs.getString("faktura_url"),
                            rs.getString("eskaera_egoera"));
                }
            }
        }
        return eskaera;
    }

    /**
     * Langile sail berri bat sortzen du.
     *
     * @param izena        Sailaren izena.
     * @param kokapena     Sailaren kokapena.
     * @param deskribapena Sailaren deskribapena.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void langileSailaSortu(String izena, String kokapena, String deskribapena) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "INSERT INTO langile_sailak (izena, kokapena, deskribapena) VALUES (?, ?, ?)";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, izena);
            pst.setString(2, kokapena);
            pst.setString(3, deskribapena);
            pst.executeUpdate();
        }
    }

    /**
     * Langile sail bat ezabatzen du.
     *
     * @param idSaila Ezabatu nahi den sailaren IDa.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void langileSailaEzabatu(int idSaila) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "DELETE FROM langile_sailak WHERE id_saila = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idSaila);
            pst.executeUpdate();
        }
    }

    /**
     * Langile sail baten datuak eguneratzen ditu.
     *
     * @param idSaila      Aldatu nahi den sailaren IDa.
     * @param izena        Sailaren izen berria.
     * @param kokapena     Sailaren kokapen berria.
     * @param deskribapena Sailaren deskribapen berria.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void langileSailaEditatu(int idSaila, String izena, String kokapena, String deskribapena)
            throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "UPDATE langile_sailak SET izena = ?, kokapena = ?, deskribapena = ? WHERE id_saila = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, izena);
            pst.setString(2, kokapena);
            pst.setString(3, deskribapena);
            pst.setInt(4, idSaila);
            pst.executeUpdate();
        }
    }

    /**
     * Sail baten informazioa lortzen du IDaren bidez.
     *
     * @param idSaila Sailaren IDa.
     * @return LangileSaila objektua.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public LangileSaila langileSailaikusi(int idSaila) throws SQLException {
        LangileSaila saila = null;
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "SELECT * FROM langile_sailak WHERE id_saila = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idSaila);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                saila = new LangileSaila(
                        rs.getInt("id_saila"),
                        rs.getString("izena"),
                        rs.getString("kokapena"),
                        rs.getString("deskribapena"));
            }
        }
        return saila;
    }

    /**
     * Sail guztiak zerrendatzen ditu.
     *
     * @return LangileSaila objektuen zerrenda.
     */
    public java.util.ArrayList<LangileSaila> sailakGuztiakIkusi() {
        java.util.ArrayList<LangileSaila> zerrenda = new java.util.ArrayList<>();
        String sql = "SELECT * FROM langile_sailak";
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql);
                java.sql.ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                zerrenda.add(new LangileSaila(
                        rs.getInt("id_saila"),
                        rs.getString("izena"),
                        rs.getString("kokapena"),
                        rs.getString("deskribapena")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zerrenda;
    }

    /**
     * Fitxaketa guztiak ikusteko metodoa.
     *
     * @return Fitxaketa objektuen zerrenda.
     */
    public java.util.ArrayList<Fitxaketa> fitxaketaGuztiakIkusi() {
        java.util.ArrayList<Fitxaketa> zerrenda = new java.util.ArrayList<>();
        String galdera = "SELECT * FROM fitxaketak ORDER BY id_fitxaketa DESC";
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(galdera);
                java.sql.ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                zerrenda.add(new Fitxaketa(
                        rs.getInt("id_fitxaketa"),
                        rs.getInt("langilea_id"),
                        rs.getDate("data"),
                        rs.getTime("ordua"),
                        rs.getString("mota"),
                        rs.getTimestamp("eguneratze_data")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zerrenda;
    }

    /**
     * Fitxaketa bat ezabatzen du.
     *
     * @param idFitxaketa Ezabatu nahi den fitxaketaren IDa.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void fitxaketaEzabatu(int idFitxaketa) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "DELETE FROM fitxaketak WHERE id_fitxaketa = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idFitxaketa);
            pst.executeUpdate();
        }
    }

    /**
     * Fitxaketa baten datuak eguneratzen ditu.
     *
     * @param idFitxaketa Aldatu nahi den fitxaketaren IDa.
     * @param data        Data berria.
     * @param ordua       Ordu berria.
     * @param mota        Mota berria (SARRERA/IRTEERA).
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void fitxaketaEditatu(int idFitxaketa, java.sql.Date data, java.sql.Time ordua, String mota)
            throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "UPDATE fitxaketak SET data = ?, ordua = ?, mota = ?, eguneratze_data = NOW() WHERE id_fitxaketa = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setDate(1, data);
            pst.setTime(2, ordua);
            pst.setString(3, mota);
            pst.setInt(4, idFitxaketa);
            pst.executeUpdate();
        }
    }

    /**
     * Fitxaketa berri bat sortzen du.
     *
     * @param langileaId Fitxaketa egin duen langilearen IDa.
     * @param data       Fitxaketaren data.
     * @param ordua      Fitxaketaren ordua.
     * @param mota       Fitxaketaren mota.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void fitxaketaSortu(int langileaId, java.sql.Date data, java.sql.Time ordua, String mota)
            throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "INSERT INTO fitxaketak (langilea_id, data, ordua, mota) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, langileaId);
            pst.setDate(2, data);
            pst.setTime(3, ordua);
            pst.setString(4, mota);
            pst.executeUpdate();
        }
    }

    /**
     * Hornitzaile baten datuak ikusten ditu.
     *
     * @param idHornitzailea Hornitzailearen IDa.
     * @return Hornitzailea objektua.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public Hornitzailea hornitzaileaIkusi(int idHornitzailea) throws SQLException {
        Hornitzailea hornitzailea = null;
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "SELECT * FROM hornitzaileak WHERE id_hornitzailea = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idHornitzailea);
            java.sql.ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                hornitzailea = new Hornitzailea(
                        rs.getInt("id_hornitzailea"),
                        rs.getString("izena_soziala"),
                        rs.getString("nan_ifz"),
                        rs.getString("kontaktu_pertsona"),
                        rs.getString("helbidea"),
                        rs.getInt("herria_id"),
                        rs.getString("posta_kodea"),
                        rs.getString("telefonoa"),
                        rs.getString("emaila"),
                        rs.getString("hizkuntza"),
                        rs.getString("pasahitza"),
                        rs.getBoolean("aktibo"),
                        rs.getTimestamp("eguneratze_data"));
            }
        }
        return hornitzailea;
    }

    /**
     * Hornitzaile bat ezabatzen du.
     *
     * @param idHornitzailea Ezabatu nahi den hornitzailearen IDa.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void hornitzaileaEzabatu(int idHornitzailea) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "DELETE FROM hornitzaileak WHERE id_hornitzailea = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idHornitzailea);
            pst.executeUpdate();
        }
    }

    /**
     * Hornitzaile baten datuak eguneratzen ditu.
     *
     * @param idHornitzailea   Aldatu nahi den hornitzailearen IDa.
     * @param izenaSoziala     Izen sozial berria.
     * @param nan              NAN edo IFZ berria.
     * @param kontaktuPertsona Kontaktu pertsona berria.
     * @param helbidea         Helbide berria.
     * @param herriaId         Herriaren ID berria.
     * @param postaKodea       Posta kode berria.
     * @param telefonoa        Telefono berria.
     * @param emaila           Email berria.
     * @param hizkuntza        Hizkuntza berria.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void hornitzaileaEditatu(int idHornitzailea, String izenaSoziala, String nan, String kontaktuPertsona,
            String helbidea, int herriaId, String postaKodea, String telefonoa, String emaila, String hizkuntza)
            throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "UPDATE hornitzaileak SET izena_soziala = ?, nan_ifz = ?, kontaktu_pertsona = ?, helbidea = ?, herria_id = ?, posta_kodea = ?, telefonoa = ?, emaila = ?, hizkuntza = ?, eguneratze_data = NOW() WHERE id_hornitzailea = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, izenaSoziala);
            pst.setString(2, nan);
            pst.setString(3, kontaktuPertsona);
            pst.setString(4, helbidea);
            pst.setInt(5, herriaId);
            pst.setString(6, postaKodea);
            pst.setString(7, telefonoa);
            pst.setString(8, emaila);
            pst.setString(9, hizkuntza);
            pst.setInt(10, idHornitzailea);
            pst.executeUpdate();
        }
    }

    /**
     * Herri guztien zerrenda lortzen du.
     *
     * @return Herria objektuen zerrenda.
     */
    public java.util.ArrayList<Herria> herriakIkusi() {
        java.util.ArrayList<Herria> zerrenda = new java.util.ArrayList<>();
        String sql = "SELECT * FROM herriak";
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql);
                java.sql.ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                zerrenda.add(new Herria(
                        rs.getInt("id_herria"),
                        rs.getString("izena"),
                        rs.getString("lurraldea"),
                        rs.getString("nazioa")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zerrenda;
    }

    /**
     * Herri berri bat sortzen du.
     *
     * @param izena     Herriaren izena.
     * @param lurraldea Lurraldea (Probintzia).
     * @param nazioa    Nazioa.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void herriBerriaSortu(String izena, String lurraldea, String nazioa) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "INSERT INTO herriak (izena, lurraldea, nazioa) VALUES (?, ?, ?)";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, izena);
            pst.setString(2, lurraldea);
            pst.setString(3, nazioa);
            pst.executeUpdate();
        }
    }

    /**
     * Herri bat ezabatzen du.
     *
     * @param idHerria Ezabatu nahi den herriaren IDa.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void herriaEzabatu(int idHerria) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "DELETE FROM herriak WHERE id_herria = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setInt(1, idHerria);
            pst.executeUpdate();
        }
    }

    /**
     * Herri baten datuak eguneratzen ditu.
     *
     * @param idHerria  Aldatu nahi den herriaren IDa.
     * @param izena     Izen berria.
     * @param lurraldea Lurralde berria.
     * @param nazioa    Nazio berria.
     * @throws SQLException Datu-basean errorea gertatzen bada.
     */
    public void herriaEditatu(int idHerria, String izena, String lurraldea, String nazioa) throws SQLException {
        try (Connection kon = DB_Konexioa.konektatu()) {
            String sql = "UPDATE herriak SET izena = ?, lurraldea = ?, nazioa = ? WHERE id_herria = ?";
            PreparedStatement pst = kon.prepareStatement(sql);
            pst.setString(1, izena);
            pst.setString(2, lurraldea);
            pst.setString(3, nazioa);
            pst.setInt(4, idHerria);
            pst.executeUpdate();
        }
    }

    /**
     * Langile baten kurrikuluma (PDF BLOB) lortzen du eta irekitzen du.
     *
     * @param idLangilea Langilearen IDa.
     * @throws Exception Errorea datuak lortzean edo fitxategia irekitzean.
     */
    public void kurrikulumaIkusi(int idLangilea) throws Exception {
        String sql = "SELECT kurrikuluma FROM langileak WHERE id_langilea = ?";
        try (Connection kon = DB_Konexioa.konektatu();
                PreparedStatement pst = kon.prepareStatement(sql)) {
            pst.setInt(1, idLangilea);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    byte[] pdfBytes = rs.getBytes("kurrikuluma");
                    if (pdfBytes != null && pdfBytes.length > 0) {
                        File tempFile = File.createTempFile("kurrikuluma_" + idLangilea + "_", ".pdf");
                        tempFile.deleteOnExit();
                        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                            fos.write(pdfBytes);
                        }
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().open(tempFile);
                        } else {
                            throw new Exception(
                                    "Sistemak ez du fitxategiak irekitzeko aukera onartzen (Desktop ez da bateragarria).");
                        }
                    } else {
                        throw new Exception("Ez dago kurrikulumik igota");
                    }
                } else {
                    throw new Exception("Ez da langilea aurkitu.");
                }
            }
        }
    }
}
