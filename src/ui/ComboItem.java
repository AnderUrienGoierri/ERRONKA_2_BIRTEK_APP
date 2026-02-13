package ui;

/**
 * ComboBox-etan objektuak gorde eta bistaratzeko klase laguntzailea.
 * IDa eta testua (label) gordetzen ditu.
 */
public class ComboItem {
    private int id;
    private String label;

    /**
     * ComboItem eraikitzailea.
     *
     * @param id    Identifikatzailea.
     * @param label Testua.
     */
    public ComboItem(int id, String label) {
        this.id = id;
        this.label = label;
    }

    /**
     * IDa lortzen du.
     *
     * @return IDa.
     */
    public int getId() {
        return id;
    }

    /**
     * Objektuaren testu adierazpena itzultzen du (ComboBox-ean agertuko dena).
     *
     * @return Labela.
     */
    @Override
    public String toString() {
        return label;
    }
}
