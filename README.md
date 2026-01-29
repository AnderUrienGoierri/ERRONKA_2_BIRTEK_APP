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
  - `nireLangileDatuakEditatu(...)`: Pasahitza, hizkuntza eta herria eguneratzeko.
  - `getFitxaketaEgoera()`: Azken fitxaketaren arabera, "BARRUAN" edo "KANPOAN" dagoen itzultzen du.
  - `autentifikatu(String pasahitza)`: Pasahitza zuzena den egiaztatzen du.

- **`AdministrariLangilea`**: Administrazio lanetarako logika gehigarria.
  - `langileaSortu(...)`: Langile berriak sortu DBan.
  - `langileaEzabatu(...)`: Langileak ezabatu DBtik.

- **`SalmentaLangilea`**: Salmenta eta fakturazio logika.
  - `fakturaSortu(int idEskaera)`: Emandako eskaera IDarekin PDF faktura bat sortzen du eta diskoan gorde.

- **`BiltegiLangilea`**: Logistika lanak.
  - `biltegiaSortu(...)`: Biltegi berriak sortu.
  - `biltegiaEzabatu(...)`: Biltegiak ezabatu.
  - `biltegiaEditatu(...)`: Biltegiaren datuak eguneratu.
  - `hornitzaileBerriaSortu(...)`: Hornitzaile berria erregistratu DBan.
  - `produktuSarreraBerriaSortu(...)`: Sarrera oso bat (hornitzailea, produktuak, lerroak) transakzio bakarrean sortu.
  - `produktuEgoeraOharraJarri(...)`: Produktu bati egoera-oharra gehitu.
  - `produktuarenBiltegiaAldatu(...)`: Produktu bat biltegi batetik bestera mugitu.
  - `produktuSarrerakIkusi(...)`: Sarreren zerrenda lortu egoeraren arabera iragaziz.
  - `produktuSarreraEgoeraAldatu(...)`: Sarrera lerro baten egoera eguneratu (eta sarrera osoarena egiaztatu).
  - `produktuEskaeraEgoeraAldatu(...)`: Eskaera baten egoera aldatu ('Prestatzen', 'Osatua', 'Ezabatua').

- **`TeknikariLangilea`**: Konponketa lanak (etorkizuneko hedapenetarako prestatua).

#### Beste Klaseak

- **`FakturaPDF`**: PDF dokumentuak sortzeko utilitatea (iText liburutegia).
  - `sortu(...)`: Goiburua, logoa, bezero datuak, lerroak eta totalak dituen faktura PDFa sortzen du.
- **Entitateak (POJO)**: Datu-baseko taulen errepresentazio soilak (Getter/Setter-ekin):
  - `Bezeroa`, `Produktua` (Eramangarria, Mugikorra, etab.), `Eskaera`, `EskaeraLerroa`, `Fitxaketa`, `Hornitzailea`, `Konponketa`, `Akatsa`, `Herria`.

### 4. `ui` Paketea (Interfaze Grafikoa)

Erabiltzailearekin interakzioa kudeatzen duten `JFrame` eta `JDialog` klaseak.

- **`SaioaHastekoPanela`**:
  - Erabiltzailea identifikatu eta dagokion menua irekitzen du bere rolaren (Saila) arabera.

- **`MenuSalmentak`**: Salmenta sailaren interfazea.
  - `eskaeraGehitu()`, `eskaeraEditatu()`, `eskaeraEzabatu()`: Eskaeren CRUD osoa.
  - `fakturaSortu()`: Hautatutako eskaeraren faktura sortu eta ireki.
  - `fitxatu(...)`: Langilearen sarrera/irteera botoiak.

- **`MenuLogistika`**: Biltegi sailaren interfazea.
  - `sarreraBerriaTabSortu()`: Sarrera berriak kudeatzeko fitxa dinamikoa.
  - `igoIrudia()`: Produktuei irudiak esleitzeko.
  - `biltegiaKudeatu...`: Stock eta kokapenak kudeatzeko.

- **`MenuTeknikoa`**: SAT / Konponketa sailaren interfazea.
  - `irekiKonponketaXehetasuna()`: Konponketa baten egoera eta oharrak ikusteko/editatzeko elkarrizketa-koadroa.

- **`MenuAdministrazioa`**: Kudeaketa orokorra.
  - `datuakKargatuOsoa()`: Langileak, sailak eta datu-maisuak kudeatzeko taulak (CRUD eragiketekin).
  - `gehituElementua(...)`, `editatuElementua(...)`, `ezabatuElementua(...)`: Datu orokorren kudeaketa.

- **`KonponketaXehetasunaElkarrizketa` & `EskaeraDialog`**:
  - Datu espezifikoak editatzeko lehio laguntzaileak (Pop-up).
- **`TaulaModelatzailea`**:
  - `ereduaEraiki(ResultSet rs)`: SQL emaitzak automatikoki JTable bateragarria den `DefaultTableModel` bihurtzen du.
