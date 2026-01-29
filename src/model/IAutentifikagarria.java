package model;

public interface IAutentifikagarria {
    /**
     * Erabiltzailearen pasahitza balidatzen du.
     * 
     * @param pasahitza Sartutako pasahitza
     * @return true pasahitza zuzena bada, false bestela
     */
    boolean autentifikatu(String pasahitza);
}
