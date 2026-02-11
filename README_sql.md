# SQL Kontsulta Konplexuak - BIRTEK

Dokumentu honetan proiektuan erabili diren edo datu-basearen analisia egiteko erabilgarriak diren SQL kontsulta konplexuak biltzen dira.

## 1. JOIN Kontsultak (Java Kodean Integratuak)

Honako kontsulta hauek Java aplikazioan (`src` karpetan) aurkitzen dira eta taulen arteko erlazioak kudeatzeko erabiltzen dira.

### A. Sarrerak Hornitzaileekin Lotu

**Fitxategia:** `model/BiltegiLangilea.java`
**Helburua:** Sarrera bakoitza bere hornitzailearen izenarekin erakustea.

```sql
SELECT s.id_sarrera, h.izena_soziala AS Hornitzailea, s.data, s.sarrera_egoera
FROM sarrerak s
JOIN hornitzaileak h ON s.hornitzailea_id = h.id_hornitzailea
ORDER BY s.data DESC;
```

### B. Sarrera Lerroak eta Produktuak

**Fitxategia:** `model/BiltegiLangilea.java`
**Helburua:** Sarrera baten lerro bakoitzeko produktuaren datuak lortzea.

```sql
SELECT sl.id_sarrera_lerroa, p.izena, p.marka, sl.kantitatea, sl.sarrera_lerro_egoera
FROM sarrera_lerroak sl
JOIN produktuak p ON sl.produktua_id = p.id_produktua
WHERE sl.sarrera_id = ?;
```

### C. Eskaerak eta Bezeroak

**Fitxategia:** `model/BiltegiLangilea.java`
**Helburua:** Eskaerak bezeroaren izenarekin batera zerrendatzea.

```sql
SELECT e.id_eskaera, b.izena_edo_soziala, e.data, e.guztira_prezioa, e.eskaera_egoera
FROM eskaerak e
JOIN bezeroak b ON e.bezeroa_id = b.id_bezeroa
ORDER BY e.data DESC;
```

### D. Eskaera Lerroak eta Produktuak

**Fitxategia:** `model/BiltegiLangilea.java` / `ui/MenuSalmentak.java`
**Helburua:** Eskaera baten xehetasunak (produktuak) ikustea.

```sql
SELECT el.id_eskaera_lerroa, p.izena, el.kantitatea, el.unitate_prezioa, el.eskaera_lerro_egoera
FROM eskaera_lerroak el
JOIN produktuak p ON el.produktua_id = p.id_produktua
WHERE el.eskaera_id = ?;
```

### E. Fitxaketak eta Langileak

**Fitxategia:** `ui/MenuAdministrazioa.java`
**Helburua:** Fitxaketa bakoitza nork egin duen ikustea (Izena + Abizena).

```sql
SELECT f.id_fitxaketa, CONCAT(l.izena, ' ', l.abizena) AS langilea, f.data, CAST(f.ordua AS CHAR) AS ordua, f.mota
FROM fitxaketak f
JOIN langileak l ON f.langilea_id = l.id_langilea
ORDER BY f.id_fitxaketa DESC;
```

### F. Produktuak, Kategoriak eta Biltegiak (Logistika)

**Fitxategia:** `ui/MenuLogistika.java`
**Helburua:** Sarrera lerroen egoera, produktuaren informazioa eta biltegiaren kokapena lortzea.

```sql
SELECT p.id_produktua, p.izena AS Produktua, b.izena AS Biltegia, s.id_sarrera AS 'Sarrera ID',
       sl.sarrera_lerro_egoera AS Egoera, s.data AS 'Sarrera Data', sl.id_sarrera_lerroa, p.produktu_egoera_oharra AS Oharra
FROM sarrera_lerroak sl
JOIN sarrerak s ON sl.sarrera_id = s.id_sarrera
JOIN produktuak p ON sl.produktua_id = p.id_produktua
JOIN biltegiak b ON p.biltegi_id = b.id_biltegia
ORDER BY s.data DESC;
```

---

## 2. GROUP BY eta HAVING Kontsultak (Datu-base Analisia)

Kontsulta hauek datu-basearen txostenak eta estatistikak sortzeko diseinatuta daude. Kodean oraindik inplementatu ez badira ere, datu-basearen egituraren gainean exekutatu daitezke.

### A. Produktu Stock-a Kategoriaren arabera (GROUP BY)

Kategoria bakoitzean zenbat stock daukagun guztira jakiteko.

```sql
SELECT k.izena AS Kategoria, SUM(p.stock) AS 'Stock Guztira'
FROM produktuak p
JOIN produktu_kategoriak k ON p.kategoria_id = k.id_kategoria
GROUP BY k.izena;
```

### B. Bezeroen Gastu Osoa (GROUP BY + ORDER BY)

Bezero bakoitzak zenbat gastatu duen guztira, gehien gastatu dutenak lehenbizi.

```sql
SELECT b.izena_edo_soziala, SUM(e.guztira_prezioa) AS 'Gastu Totala'
FROM eskaerak e
JOIN bezeroak b ON e.bezeroa_id = b.id_bezeroa
WHERE e.eskaera_egoera != 'Ezabatua'
GROUP BY b.id_bezeroa, b.izena_edo_soziala
ORDER BY 'Gastu Totala' DESC;
```

### C. Hornitzaile Aktiboenak (GROUP BY + HAVING)

Gutxienez 5 produktu desberdin hornitzen dituzten hornitzaileak bilatzeko.

```sql
SELECT h.izena_soziala, COUNT(p.id_produktua) AS 'Produktu Kopurua'
FROM produktuak p
JOIN hornitzaileak h ON p.hornitzaile_id = h.id_hornitzailea
GROUP BY h.id_hornitzailea, h.izena_soziala
HAVING COUNT(p.id_produktua) >= 5;
```

### D. Eskaera Handiak dituzten Bezeroak (GROUP BY + HAVING)

1000€ baino gehiagoko batez besteko eskaera duten bezeroak.

```sql
SELECT b.izena_edo_soziala, AVG(e.guztira_prezioa) AS 'Batez Besteko Eskaera'
FROM eskaerak e
JOIN bezeroak b ON e.bezeroa_id = b.id_bezeroa
GROUP BY b.id_bezeroa, b.izena_edo_soziala
HAVING AVG(e.guztira_prezioa) > 1000;
```
