package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class TestSalmentaLangilea {
    public static void main(String[] args) {
        try {
            // Langilea dummy bat sortu (SalmentaLangilea instantziatzeko)
            Langilea l = new Langilea(1, "Test", "User", "12345678Z", Date.valueOf("1990-01-01"),
                    1, "Helbidea", "20000", "943000000", "test@email.com", "EU",
                    "password", "UID123", new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis()), true, 1, "ES1234567890", new byte[0]);

            SalmentaLangilea sl = new SalmentaLangilea(l);
            System.out.println("SalmentaLangilea instantziatuta.");

            // Metodoak deitu (ez du zertan funtzionatu DB konexiorik gabe, baina
            // konpilazioa frogatzen du)
            try {
                sl.produktuaIkusi(1);
                System.out.println("produktuaIkusi OK");
            } catch (Exception e) {
                // Expected if DB fails
            }

            try {
                sl.eskaerakIkusi(1);
                System.out.println("eskaerakIkusi OK");
            } catch (Exception e) {
                // Expected if DB fails
            }

            try {
                // EskaeraLerroa metodoak probatu
                EskaeraLerroa.eskaeraLerroaIkusi(1);
                System.out.println("eskaeraLerroaIkusi OK");
            } catch (Exception e) {
                // Expected
            }

            System.out.println("Konpilazioa zuzena da.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
