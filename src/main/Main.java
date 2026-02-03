package main;

import ui.SaioaHastekoPanela;
import java.awt.EventQueue;

/**
 * Main klasea.
 * Aplikazioaren sarrera puntua.
 */
public class Main {
    /**
     * Aplikazioa abiarazten duen metodo nagusia.
     * 
     * @param args Komando lerroko argumentuak (ez dira erabiltzen).
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SaioaHastekoPanela frame = new SaioaHastekoPanela();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
