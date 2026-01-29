package main;

import ui.SaioaHastekoPanela;
import java.awt.EventQueue;

public class Main {
    /**
     * LANGILEEN SaioaHastekoPanela() martxan jarri:
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
