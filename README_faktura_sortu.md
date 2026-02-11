# Faktura Sortzeko Kode Adibidea

Proiektu honetan fakturak PDF formatuan sortzeko `iText` liburutegia erabiltzen dugu (edo antzekoa, inplementazioaren arabera). Behean `SalmentaLangilea.java` klasean dagoen faktura sortzeko metodo nagusiaren kodea ikus daiteke.

## Kodea: `fakturaSortu`

Metodo honek eskaera baten IDa hartzen du, datuak kargatzen ditu (Eskaera, Bezeroa, Lerroak) eta PDF fitxategia sortzen du `FakturaPDF` klase laguntzailea erabiliz. Azkenik, datu-basean eguneratzen du fakturaren bidea.

```java
    private static final String FAKTURA_BIDEA = "C:\\Xampp\\htdocs\\fakturak";

    public File fakturaSortu(int idEskaera) throws Exception {
        // Faktura karpeta ziurtatu
        File karpeta = new File(FAKTURA_BIDEA);
        if (!karpeta.exists()) {
            karpeta.mkdirs();
        }

        File fakturaFitxategia = new File(karpeta, "faktura_" + idEskaera + ".pdf");

        // Datuak lortu
        Eskaera eskaera = eskaeraIkusi(idEskaera);
        if (eskaera == null) {
            throw new Exception("Ez da eskaera aurkitu: " + idEskaera);
        }

        Bezeroa bezeroa = bezeroaIkusi(eskaera.getBezeroaId());
        if (bezeroa == null) {
            throw new Exception("Ez da bezeroa aurkitu eskaerarentzat: " + idEskaera);
        }

        List<EskaeraLerroa> lerroak = eskaeraLerroakIkusi(idEskaera);

        // PDF Sortu
        FakturaPDF.sortu(fakturaFitxategia.getAbsolutePath(), eskaera, bezeroa, lerroak);

        // DBan gorde (Eskaera taulan)
        String fakturaZenbakia = "FAK-" + idEskaera + "-" + System.currentTimeMillis();
        String sqlUpdate = "UPDATE eskaerak SET faktura_zenbakia = ?, faktura_url = ? WHERE id_eskaera = ?";

        try (Connection konexioa = DB_Konexioa.konektatu();
                PreparedStatement pstUpdate = konexioa.prepareStatement(sqlUpdate)) {
            pstUpdate.setString(1, fakturaZenbakia);
            pstUpdate.setString(2, fakturaFitxategia.getAbsolutePath());
            pstUpdate.setInt(3, idEskaera);
            pstUpdate.executeUpdate();
        }

        return fakturaFitxategia;
    }
```

## Azalpena

1.  **Karpeta Prestatu**: `C:\Xampp\htdocs\fakturak` karpeta existitzen dela ziurtatzen du.
2.  **Datuak Lortu**: Eskaera, Bezeroa eta Eskaera Lerroak datu-basetik irakurtzen ditu.
3.  **PDF Sortu**: `FakturaPDF.sortu(...)` metodo estatikoa deitzen du PDFa sortzeko.
4.  **Datu-basea Eguneratu**: Sortutako fakturaren zenbakia eta fitxategi-bidea `eskaerak` taulan gordetzen ditu.
