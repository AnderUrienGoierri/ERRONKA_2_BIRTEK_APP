package ui;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 * TaulaModelatzailea klasea.
 * ResultSet batetik JTable baterako DefaultTableModel sortzeko utilitatea.
 */
public class TaulaModelatzailea {

    /**
     * ResultSet datuekin TableModel bat eraikitzen du.
     * 
     * @param emaitza SQL kontsultaren emaitza (ResultSet).
     * @return Datuak dituen DefaultTableModel objektua.
     * @throws SQLException SQL errorea gertatzen bada.
     */
    public static DefaultTableModel ereduaEraiki(ResultSet emaitza) throws SQLException {
        ResultSetMetaData metaDatuak = emaitza.getMetaData();

        // 1. Zutabeen izenak lortu
        Vector<String> zutabeIzenak = new Vector<String>();
        int zutabeKopurua = metaDatuak.getColumnCount();
        for (int zutabea = 1; zutabea <= zutabeKopurua; zutabea++) {
            zutabeIzenak.add(metaDatuak.getColumnName(zutabea));
        }

        // 2. Errenkaden datuak lortu
        Vector<Vector<Object>> datuak = new Vector<Vector<Object>>();
        while (emaitza.next()) {
            Vector<Object> lerroa = new Vector<Object>();
            for (int zutabeIndizea = 1; zutabeIndizea <= zutabeKopurua; zutabeIndizea++) {
                lerroa.add(emaitza.getObject(zutabeIndizea));
            }
            datuak.add(lerroa);
        }

        return new DefaultTableModel(datuak, zutabeIzenak) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (getRowCount() > 0 && getValueAt(0, columnIndex) != null) {
                    return getValueAt(0, columnIndex).getClass();
                }
                return Object.class;
            }
        };
    }
}
