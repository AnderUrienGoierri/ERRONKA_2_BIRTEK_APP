# Birtek Kudeaketa Aplikazioa

Proiektu hau Birtek enpresaren barne kudeaketarako Java aplikazioa da. Langileen kudeaketa, salmentak, inbentarioa, konponketak eta administrazio lanak errazteko diseinatuta dago.

## Nola martxan jarri

Aplikazioa exekutatzeko, jarraitu urrats hauek:

1. **Datu-basea prestatu**:

   - Sartu `sql` karpetara eta `birtek_db.sql` fitxategia bilatu.
   - Inportatu fitxategi hau zure MySQL/MariaDB zerbitzarian datu-basea eta taulak sortzeko.
   - _Oharra: Datu-basearen izena `birtek_db` da._
2. **Konexioa konfiguratu**:

   - Ireki `src/db/DB_Konexioa.java` fitxategia.
   - Ziurtatu databaseko kredentzialak zuzenak direla (erabiltzailea eta pasahitza).
   - Lehenetsia:
     - URL: `jdbc:mysql://localhost:3306/birtek_db`
     - User: `root`
     - Pass: `1MG32025`
3. **Liburutegiak gehitu**:

   - Proiektuak kanpo liburutegiak behar ditu funtzionatzeko. Ziurtatu `lib` karpetako `.jar` fitxategiak zure proiektuaren **Classpath**-ean edo **Build Path**-ean gehituta daudela:
     - `mysql-connector-j-9.1.0.jar`: Datu-baserako konexioa.
     - `itextpdf-5.5.9.jar`: PDF fakturak sortzeko.
4. **Exekutatu**:

   - Bilatu etab exekutatu `src/main/Main.java` fitxategia. Honek saioa hasteko lehioa irekiko du.

## Proiektuaren Egitura

Fitxategiak karpeta hauetan antolatuta daude:

- **`src`**: Java iturburu kodea (`.java`).
- **`bin`**: Konpilatutako klaseak (`.class`) eta exekuziorako beharrezko baliabideak.
- **`irudiak`**: Aplikazioan erabiltzen diren logo eta irudiak.
- **`sql`**: Datu-basearen segurtasun kopiak eta script-ak.
- **`lib`**: Kanpo liburutegiak (`.jar`).

## Klaseak eta Funtzio Nagusiak

Kodea paketeetan (packages) banatuta dago, MVC (Model-View-Controller) antzeko egitura bat jarraituz:

### 1. `main` Paketea

- **`Main.java`**: Aplikazioaren sarrera puntua da. `main` metodoak `SaioaHastekoPanela` abiarazten du hari seguru batean (`EventQueue.invokeLater`).

### 2. `db` Paketea

- **`DB_Konexioa.java`**: Datu-basearekin konexioa kudeatzen du.
  - _Singleton_ patroia erabiltzen du (`konektatu()` metodoa) konexio bakarra bermatzeko eta baliabideak optimizatzeko.

### 3. `model` Paketea (Logika eta Datuak)

Hemen daude datuen egiturak eta negozio-logika gehiena.

- **`Langilea`**: Langile guztien klase gurasoa. Fitxaketa metodoak (`fitxatu()`) eta oinarrizko datuak ditu.
  - Azpiklaseak: `AdministrariLangilea`, `SalmentaLangilea`, `BiltegiLangilea`, `TeknikariLangilea`. Bakoitzak bere menu espezifikoa irekitzeko logika du (`menuIreki()`).
- **`FakturaPDF`**: Fakturak PDF formatuan sortzeko arduraduna. `iText` liburutegia erabiltzen du.
- **POJO klaseak**: `Produktua`, `Bezeroa`, `Eskaera`, `EskaeraLerroa`. Datu-baseko taulekin bat datozen objektuak.

### 4. `ui` Paketea (Interfaze Grafikoa)

Erabiltzailearen interfazeak (Swing liburutegia erabiliz).

- **`SaioaHastekoPanela`**: Erabiltzaileak identifikatzeko lehioa.
- **`Menu[Mota]`**: Rol bakoitzerako menu nagusiak. Adibidez:
  - `MenuSalmentak`: Salmentak kudeatu, bezeroak ikusi, fakturak sortu.
  - `MenuLogistika`: Biltegia kudeatu, stock-a ikusi.
  - `MenuTeknikoa`: Konponketak kudeatu.
  - `MenuAdministrazioa`: Langileak kudeatu (CRUD).
- **`TaulaModelatzailea`**: `ResultSet` batetik `JTable` baterako modeloa automatikoki sortzeko erabilgarria.
