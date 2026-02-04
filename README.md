# Birtek Kudeaketa Aplikazioa

Proiektu hau Birtek enpresaren barne kudeaketarako Java aplikazioa da. Langileen kudeaketa, salmentak, inbentarioa, konponketak eta administrazio lanak errazteko diseinatuta dago, MVC (Model-View-Controller) arkitektura erabiliz.

## Nola martxan jarri

Aplikazioa exekutatzeko, jarraitu urrats hauek:

1. **Datu-basea prestatu**:

   - Sartu `sql` karpetara eta `birtek_db.sql` fitxategia bilatu.
   - Inportatu fitxategi hau zure MySQL/MariaDB zerbitzarian datu-basea eta taulak sortzeko.
   - _Oharra: Datu-basearen izena `birtek_db` da._
2. **Konexioa konfiguratu**:

   - Ireki `src/db/DB_Konexioa.java` fitxategia.
   - Ziurtatu databaseko kredentzialak zuzenak direla (erabiltzailea eta pasahitza).
     - URL: `jdbc:mysql://localhost:3306/birtek_db`
     - Erabiltzailea: `root`
     - Pasahitza: `1MG32025`
3. **Liburutegiak gehitu**:

   - Ziurtatu `lib` karpetako `.jar` fitxategiak Build Path-ean daudela:
     - `mysql-connector-j-9.1.0.jar`
     - `itextpdf-5.5.9.jar`
4. **Exekutatu**:

   - `src/main/Main.java` abiarazi.

## Proiektuaren Egitura

- **`src`**: Java kodea (`model`, `ui`, `db`, `main`).
- **`bin`**: Konpilatutako `.class` fitxategiak.
- **`irudiak`**: Logoak eta bestelako baliabide grafikoak.
- **`sql`**: Datu-base eskema.
- **`lib`**: Kanpo dependentziak.

## Klaseak eta Metodoak

Kodea lau pakete nagusitan banatuta dago:

### 1. `main` Paketea

- **`Main`**: Aplikazioaren sarrera puntua.
  - `main(String[] args)`: Aplikazioaren exekuzioa hasten du eta saioa hasteko lehioa (`SaioaHastekoPanela`) irekitzen du Swing hari seguru batean.

### 2. `db` Paketea

- **`DB_Konexioa`**: Datu-basearekiko konexioa kudeatzen du Singleton patroia erabiliz.
  - `konektatu()`: Konexio aktibo bat itzultzen du. Konexiorik ez badago edo itxita badago, berri bat sortzen du MySQL driver-a erabiliz.

### 3. `model` Paketea (Logika eta Datuak)

Pakete honek entitateak eta negozio-logika biltzen ditu. Langileen hierarkia eta datu-baseko objektuak (POJOak) hemen daude.

#### Langile Hierarkia

- **`Langilea`** (Gurasoa): Langile guztien oinarrizko atributuak eta metodoak.

  - `fitxatu(String mota)`: Sarrera edo Irteera fitxaketak erregistratzen du DBan.
  - `sarreraFitxaketaEgin()`: "Sarrera" motako fitxaketa laguntzailea.
  - `irteeraFitxaketaEgin()`: "Irteera" motako fitxaketa laguntzailea.
  - `nireFitxaketakIkusi()`: Langilearen fitxaketa historiala itzultzen du.
  - `nireLangileDatuakEditatu(...)`: Pasahitza, hizkuntza, herria, telefonoa eta helbidea eguneratzeko.
  - `herriakLortu()`: Herrien zerrenda osoa itzultzen du.
  - `herriaSortu(Herria h)`: Herri berri bat sortzen du DBan.
  - `getFitxaketaEgoera()`: Azken fitxaketaren arabera, "BARRUAN" edo "KANPOAN" dagoen itzultzen du.
  - `autentifikatu(String pasahitza)`: Pasahitza zuzena den egiaztatzen du.
  - `getNan()` / `setNan()`: NAN/IFZ kudeatzaileak.
  - `getSaltoTxartelaUid()` / `setSaltoTxartelaUid()`: Salto txartelaren identifikadorearen kudeaketa.
- **`AdministrariLangilea`**: Administrazio lanetarako logika gehigarria.

  - **Langileak**:
    - `langileaSortu(...)`: Langile berriak sortu DBan.
    - `langileaEditatu(...)`: Langile baten datuak eguneratu.
    - `langileaEzabatu(...)`: Langileak ezabatu DBtik.
    - `langileaIkusi(int id)`: Langile baten datuak lortu.
  - **Sailak**:
    - `langileSailaSortu(...)`: Sail berri bat sortu.
    - `langileSailaEditatu(...)`: Sail baten datuak aldatu.
    - `langileSailaEzabatu(...)`: Sail bat ezabatu.
    - `langileSailaikusi(int id)`: Sail baten informazioa lortu.
  - **Fitxaketak**:
    - `fitxaketaSortu(...)`: Fitxaketa berri bat eskuz sortu.
    - `fitxaketaEditatu(...)`: Fitxaketa baten ordua/data aldatu.
    - `fitxaketaEzabatu(...)`: Fitxaketa bat ezabatu.
    - `fitxaketaGuztiakIkusi()`: Fitxaketa guztien historia ikusi.
  - **Hornitzaileak**:
    - `hornitzaileaEditatu(...)`: Hornitzailearen datuak eguneratu.
    - `hornitzaileaEzabatu(...)`: Hornitzailea ezabatu.
    - `hornitzaileaIkusi(int id)`: Hornitzaile baten fitxa ikusi.
  - **Herriak**:
    - `herriakIkusi()`: Herrien zerrenda osoa lortu.
    - `herriBerriaSortu(...)`: Herri berri bat gehitu DBan.
    - `herriaEditatu(...)`: Herri baten datuak (izena, lurraldea...) aldatu.
    - `herriaEzabatu(...)`: Herri bat sistematik ezabatu.
  - **Bestelakoak**:
    - `bezeroaFakturaIkusi(int id)`: Bezero baten faktura espezifiko bat berreskuratu.
- **`SalmentaLangilea`**: Salmenta eta fakturazio logika.

  - `fakturaSortu(int idEskaera)`: Emandako eskaera IDarekin PDF faktura bat sortzen du eta diskoan gorde.
  - `bezeroBerriaSortu(Bezeroa b)`: Bezero berri bat DBan erregistratu.
  - `bezeroaEditatu(Bezeroa b)`: Bezero baten datuak eguneratu.
  - `bezeroaKendu(int idBezeroa)`: Bezero bat DBtik ezabatu.
  - `bezeroaIkusi(int idBezeroa)`: Bezero baten informazio zehatza lortu.
  - `bezeroFakturaEzabatu(int idFaktura)`: Sortutako faktura bat ezabatu.
  - `produktuaIkusi(int idProduktua)`: Produktu baten xehetasunak lortu.
  - `produktuariEskaintzaAldatzeko(int idProduktua, BigDecimal eskaintza)`: Produktu baten eskaintza prezioa aldatu.
  - `produktuaSalgaijarri(int idProduktua)`: Produktu bat salgai moduan markatu.
  - `produktuariPrezioaJarri(int idProduktua, BigDecimal prezioa)`: Produktuari oinarrizko prezioa ezarri.
  - `produktuariPrezioaAldatu(int idProduktua, BigDecimal prezioa)`: Lehendik dagoen prezioa eguneratu.
  - `eskaerakIkusi(int idBezeroa)`: Bezero baten eskaera guztien zerrenda lortu.
  - `eskaeraSortu(Eskaera e)`: Eskaera berria eta bere lerroak sortu.
  - `eskaeraEditatu(Eskaera e)`: Eskaera baten datuak eguneratu.
  - `eskaeraEzabatu(int idEskaera)`: Eskaera bat DBtik kendu.
  - `eskaeraLerroakIkusi(int idEskaera)`: Eskaera baten produktuak ikusi.
  - `eskaeraLerroaGehitu(int idEskaera, int idProduktua, int kantitatea, BigDecimal prezioa)`: Eskaera bati produktu bat gehitu.
  - `eskaeraLerroakEditatu(int idEskaeraLerroa, int idEskaera, int idProduktua, int kantitatea, BigDecimal prezioa)`: Eskaera lerro bat aldatu.
  - `eskaeraLerroaEzabatu(int idEskaeraLerroa)`: Eskaera lerro bat ezabatu.
- **`BiltegiLangilea`**: Logistika lanak.

  - `biltegiaSortu(String izena, String sku)`: Biltegi berria erregistratu.
  - `biltegiaEzabatu(int idBiltegia)`: Biltegi bat DBtik kendu.
  - `biltegiaEditatu(int idBiltegia, String izena, String sku)`: Biltegi datuak eguneratu.
  - `hornitzaileBerriaSortu(String izena, String ifz, String emaila)`: Hornitzaile berria sortu.
  - `produktuSarreraBerriaSortu(...)`: Sarrera osoa (hornitzailea, produktuak, lerroak) transakzio bakarrean kudeatu.
  - `produktuEgoeraOharraJarri(int idProduktua, String oharra)`: Produktuaren egoerari buruzko oharra gehitu.
  - `produktuarenBiltegiaAldatu(int idProduktua, int idBiltegia)`: Produktu bat biltegi batetik bestera mugitu.
  - `produktuSarrerakIkusi(String egoeraIragazkia)`: Sarreren zerrenda lortu iragazki bidez.
  - `produktuSarreraLerroakIkusi(int idSarrera)`: Sarrera baten lerro guztiak lortu.
  - `produktuSarreraEditatu(int idSarrera, String egoera)`: Sarrera baten egoera orokorra aldatu. Aldaketak lerro guztietara hedatzen dira (Cascading).
  - `produktuSarreraLerroEgoeraAldatu(int idSarreraLerroa, String egoera)`: Sarrera lerro baten egoera eguneratu. Lerro guztiak "Jasota" badaude, sarrera nagusia ere eguneratzen du.
  - `produktuSarreraEzabatu(int idSarrera)`: Sarrera bat eta bere lerro guztiak ezabatu DBtik.
  - `produktuEskaerakIkusi(String egoeraIragazkia)`: Eskaerak zerrendatu egoeraren arabera iragaziz.
  - `produktuEskaeraLerroakIkusi(int idEskaera)`: Eskaera baten lerro guztiak ikusi.
  - `produktuEskaeraEgoeraAldatu(int idEskaera, String egoera)`: Eskaera baten egoera eguneratu. Aldaketak lerro guztietara hedatzen dira.
  - `produktuEskaeraLerroEgoeraAldatu(int idEskaeraLerroa, String egoera, int idEskaera)`: Eskaera lerro bat eguneratu. Lerro bat "Prestatzen" badago, eskaera osoa "Prestatzen" jartzen da.
- **`TeknikariLangilea`**: Konponketa lanak eta produktu teknikoen kudeaketa.

  - `produktuakIkusi()`: Biltegira iritsi diren produktu guztiak bistaratu (salgai daudenak eta ez daudenak).
  - `produktuBatSortu(Produktua p)`: Produktu berri bat sisteman sartu.
  - `produktuaEditatu(int id, boolean salgai, String egoera)`: Produktuaren egoera eta salgai-marka aldatu.
  - `produktuariIrudiaGehitu(int id, String irudiaUrl)`: Produktu bati irudia esleitu.
  - `prezioaEzarri(int id, BigDecimal prezioa, BigDecimal eskaintza)`: Produktuaren salmenta prezioa eta eskaintza eguneratu.
  - `produktuaBorratu(int id)`: Produktu bat sistematik ezabatu.
  - `konponketakIkusi()`: Konponketa guztiak zerrendatu.
  - `konponketaEgin(Konponketa k)`: Konponketa berri bat sortu.
  - `konponketaEditatu(Konponketa k)`: Konponketa baten datuak eguneratu.
  - `konponketaEzabatu(int id)`: Konponketa bat ezabatu.
  - `akatsaIkusi()`: Akatsen zerrenda ikusi.
  - `akatsaSortu(Akatsa a)`: Akats berri bat sortu.
  - `akatsaEditatu(Akatsa a)`: Akats bat editatu.
  - `akatsaEzabatu(int id)`: Akats bat ezabatu.

#### Beste Klaseak

- **`FakturaPDF`**: PDF dokumentuak sortzeko eta kudeatzeko utilitatea (iText liburutegia).
  - `sortu(...)`: Goiburua, logoa, bezero datuak, lerroak eta totalak dituen faktura PDFa sortzen du.
  - `fakturaIgoZerbitzarira(String fitxategiPath, String fitxategiIzena)`: Sortutako faktura FTP bidez zerbitzarira (XAMPP/htdocs/fakturak) igotzen du.
- **Entitateak (POJO)**: Datu-baseko taulen errepresentazio soilak (Getter/Setter-ekin):
  - `Bezeroa`, `Produktua` (Eramangarria, Mugikorra, etab.), `Eskaera`, `EskaeraLerroa`, `Fitxaketa`, `Hornitzailea`, `Konponketa`, `Akatsa`, `Herria`.

### 4. `ui` Paketea (Interfaze Grafikoa)

Erabiltzailearekin interakzioa kudeatzen duten `JFrame` eta `JDialog` klaseak.

- **`SaioaHastekoPanela`**:

  - Erabiltzailea identifikatu eta dagokion menua irekitzen du bere rolaren (Saila) arabera.
- **`MenuSalmentak`**: Salmenta sailaren interfazea.

  - `eskaeraGehitu()`, `eskaeraEditatu()`, `eskaeraEzabatu()`: Eskaeren CRUD osoa.
  - `fakturaSortu()`: Hautatutako eskaeraren faktura sortu eta ireki.
  - `garbituEskaeraFiltroa()`: Eskaeren fitxan bilatzailea eta filtroak garbitzen ditu.
  - `fitxatu(...)`: Langilearen sarrera/irteera botoiak.
- **`MenuLogistika`**: Biltegi sailaren interfazea.

  - `sarreraTabSortu()`: Sarrerak ikusteko eta kudeatzeko fitxa. "Sarrera Berria +" botoia daukan elkarrizketa-leihoa irekitzeko.
  - `ikusiSarreraLerroak()`: Sarrera baten xehetasunak erakusten dituen lehioa ireki.
  - `editatuSarrera()`: Sarreraren egoera aldatzeko lehioa ireki.
  - `ezabatuSarrera()`: Sarrera bat ezabatu.
  - `ikusiEskaeraLerroak()`: Eskaera baten lerroak ikusi eta kudeatzeko lehioa.
  - `editatuProduktuOharra()`: Produktu bati oharra gehitu/aldatu.
  - `igoIrudia()`: Produktuei irudiak esleitzeko fitxategi-hautatzailea.
- **`MenuTeknikoa`**: SAT / Konponketa sailaren interfazea.

  - `irekiKonponketaXehetasuna()`: Konponketa baten egoera eta oharrak ikusteko/editatzeko elkarrizketa-koadroa.
- **`MenuAdministrazioa`**: Kudeaketa orokorra.

  - `datuakKargatuOsoa()`: Langileak, sailak eta datu-maisuak kudeatzeko taulak (CRUD eragiketekin).
  - `gehituElementua(...)`, `editatuElementua(...)`, `ezabatuElementua(...)`: Datu orokorren kudeaketa.
- **`KonponketaXehetasunaElkarrizketa` & `EskaeraDialog`**:

  - Datu espezifikoak editatzeko lehio laguntzaileak (Pop-up).
- **`BezeroaDialog` & `NireDatuakDialog` & `SarreraBerriaDialog`**:

  - `BezeroaDialog`: Bezeroen datuak kudeatzeko (gehitu, editatu, ikusi). Herriak gehitzeko aukera barne.
  - `NireDatuakDialog`: Langilearen profil datuak modu estandarrean editatzeko lehioa.
  - `SarreraBerriaDialog`: Produktu sarrera berriak eta produktuak sortzeko formularioa.
- **`TaulaModelatzailea`**:

  - `ereduaEraiki(ResultSet rs)`: SQL emaitzak automatikoki JTable bateragarria den `DefaultTableModel` bihurtzen du.
