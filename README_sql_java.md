# SQL Queries Documentation (Java Project)

This document provides a comprehensive list of all SQL queries used within the Java application, extracted from `PreparedStatement` usages.

## Introduction
The Java application interacts with a MySQL database (`birtek_db`) using JDBC. Most queries are parameterized using `PreparedStatement` to ensure security and performance.

---

## 1. Langileak (Employees)

| Operation | SQL Query | File |
| :--- | :--- | :--- |
| **Login** | `SELECT * FROM langileak l JOIN langile_sailak s ON l.saila_id = s.id_saila WHERE eruptnan = ? AND pasahitza = ? AND aktibo = 1` | `SaioaHastekoPanela.java` |
| **Update Own Data** | `UPDATE langileak SET izena = ?, abizena = ?, jaiotza_data = ?, sexua = ?, herria_id = ?, posta_kodea = ?, telefonoa = ?, emaila = ?, pasahitza = ? WHERE eruptnan = ?` | `NireDatuakDialog.java` |
| **Insert** | `INSERT INTO langileak (eruptnan, izena, abizena, jaiotza_data, sexua, helbidea, herria_id, posta_kodea, telefonoa, emaila, hizkuntza, saila_id, pasahitza, aktibo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)` | `AdministrariLangilea.java` |
| **Update** | `UPDATE langileak SET izena=?, abizena=?, jaiotza_data=?, sexua=?, helbidea=?, herria_id=?, posta_kodea=?, telefonoa=?, emaila=?, hizkuntza=?, saila_id=?, pasahitza=?, aktibo=? WHERE eruptnan=?` | `AdministrariLangilea.java` |
| **Delete (Soft)** | `UPDATE langileak SET aktibo = 0 WHERE eruptnan = ?` | `AdministrariLangilea.java` |
| **Check Clock-in Status** | `SELECT * FROM fitxaketak WHERE langilea_nan = ? ORDER BY data DESC, ordua DESC` | `Langilea.java` |
| **All Employee Clock-ins** | `SELECT f.id_fitxaketa, CONCAT(l.izena, ' ', l.abizena) AS langilea, f.data, CAST(f.ordua AS CHAR) AS ordua, f.mota FROM fitxaketak f JOIN langileak l ON f.langilea_id = l.id_langilea ORDER BY f.id_fitxaketa DESC` | `MenuAdministrazioa.java` |
| **List All Departments** | `SELECT * FROM langile_sailak` | `MenuAdministrazioa.java` |
| **Delete Department** | `DELETE FROM langile_sailak WHERE id_saila = ?` | `MenuAdministrazioa.java` |

---

## 2. Bezeroak (Customers)

| Operation | SQL Query | File |
| :--- | :--- | :--- |
| **Insert** | `INSERT INTO bezeroak (izena_edo_soziala, abizena, ifz_nan, jaiotza_data, sexua, bezero_ordainketa_txartela, helbidea, herria_id, posta_kodea, telefonoa, emaila, hizkuntza, pasahitza, aktibo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)` | `SalmentaLangilea.java` |
| **Update** | `UPDATE bezeroak SET izena_edo_soziala=?, abizena=?, ifz_nan=?, jaiotza_data=?, sexua=?, bezero_ordainketa_txartela=?, helbidea=?, herria_id=?, posta_kodea=?, telefonoa=?, emaila=?, hizkuntza=?, pasahitza=?, aktibo=? WHERE id_bezeroa=?` | `SalmentaLangilea.java` |
| **Delete (Soft)** | `UPDATE bezeroak SET aktibo = 0 WHERE id_bezeroa = ?` | `SalmentaLangilea.java` |
| **List All** | `SELECT * FROM bezeroak` | `SalmentaLangilea.java` |
| **List Active** | `SELECT id_bezeroa, izena_edo_soziala FROM bezeroak WHERE aktibo = 1` | `EskaeraDialog.java` |

---

## 3. Produktuak (Products)

| Operation | SQL Query | File |
| :--- | :--- | :--- |
| **Complex Join** | `SELECT p.id_produktua, p.izena AS Produktua, b.izena AS Biltegia, s.id_sarrera AS 'Sarrera ID', sl.sarrera_lerro_egoera AS Egoera, s.data AS 'Sarrera Data', sl.id_sarrera_lerroa, p.produktu_egoera_oharra AS Oharra FROM sarrera_lerroak sl JOIN sarrerak s ON sl.sarrera_id = s.id_sarrera JOIN produktuak p ON sl.produktua_id = p.id_produktua JOIN biltegiak b ON p.biltegi_id = b.id_biltegia ORDER BY s.data DESC` | `MenuLogistika.java` |
| **Set Warehouse** | `UPDATE produktuak SET biltegi_id = ? WHERE id_produktua = ?` | `BiltegiLangilea.java` |
| **Update Price** | `UPDATE produktuak SET salmenta_prezioa = ?, eskaintza = ? WHERE id_produktua = ?` | `TeknikariLangilea.java` |
| **Stock Check** | `SELECT id_produktua, izena, salmenta_prezioa, stock, eskaintza FROM produktuak WHERE salgai = 1` | `EskaeraDialog.java` |
| **Delete** | `DELETE FROM produktuak WHERE id_produktua = ?` | `TeknikariLangilea.java` |

---

## 4. Eskaerak (Orders)

| Operation | SQL Query | File |
| :--- | :--- | :--- |
| **Insert** | `INSERT INTO eskaerak (bezero_id, data, egoera, eskaera_prezio_totala) VALUES (?, ?, ?, ?)` | `SalmentaLangilea.java` |
| **Update** | `UPDATE eskaerak SET bezero_id = ?, data = ?, egoera = ?, eskaera_prezio_totala = ? WHERE id_eskaera = ?` | `SalmentaLangilea.java` |
| **List with Join** | `SELECT e.id_eskaera, b.izena_edo_soziala, e.data, e.eskaera_prezio_totala, e.egoera FROM eskaerak e JOIN bezeroak b ON e.bezero_id = b.id_bezeroa WHERE e.egoera LIKE ?` | `BiltegiLangilea.java` |
| **Insert Line** | `INSERT INTO eskaera_lerroak (eskaera_id, produktua_id, kantitatea, unitate_prezioa, eskaera_lerro_egoera) VALUES (?, ?, ?, ?, ?)` | `EskaeraLerroa.java` |
| **Update Line** | `UPDATE eskaera_lerroak SET eskaera_id=?, produktua_id=?, kantitatea=?, unitate_prezioa=?, eskaera_lerro_egoera=? WHERE id_eskaera_lerroa=?` | `EskaeraLerroa.java` |
| **Delete Line** | `DELETE FROM eskaera_lerroak WHERE id_eskaera_lerroa=?` | `EskaeraLerroa.java` |
| **List Lines by Order** | `SELECT * FROM eskaera_lerroak WHERE eskaera_id=?` | `EskaeraLerroa.java` |
| **Get Completed Invoices** | `SELECT id_eskaera FROM eskaerak WHERE eskaera_egoera = 'Osatua/Bidalita'` | `MenuSalmentak.java` |
| **List Official Invoices** | `SELECT e.id_eskaera AS id_faktura, e.faktura_zenbakia, CONCAT(e.id_eskaera, ' - ', b.izena_edo_soziala) AS eskaera, e.data, e.faktura_url AS fitxategia_url FROM eskaerak e JOIN bezeroak b ON e.bezeroa_id = b.id_bezeroa WHERE e.faktura_zenbakia IS NOT NULL AND e.faktura_zenbakia != '' ORDER BY e.id_eskaera DESC` | `MenuAdministrazioa.java` |

---

## 5. Konponketak (Repairs)

| Operation | SQL Query | File |
| :--- | :--- | :--- |
| **Insert** | `INSERT INTO konponketak (produktua_id, teknikaria_id, hasiera_data, amaiera_data, konponketa_egoera, akatsa_id, oharrak) VALUES (?, ?, ?, ?, ?, ?, ?)` | `TeknikariLangilea.java` |
| **Update Details** | `UPDATE konponketak SET konponketa_egoera = ?, akatsa_id = ?, oharrak = ? WHERE id_konponketa = ?` | `KonponketaXehetasunaElkarrizketa.java` |
| **List All** | `SELECT * FROM konponketak` | `MenuTeknikoa.java` |

---

## 6. General Infrastructure Queries

| Table | Operation | SQL Query |
| :--- | :--- | :--- |
| **Herriak** | **Insert** | `INSERT INTO herriak (izena, lurraldea, nazioa) VALUES (?, ?, ?)` |
| **Fitxaketak** | **Insert** | `INSERT INTO fitxaketak (langilea_nan, mota, data, ordua) VALUES (?, ?, ?, ?)` |
| **Biltegiak** | **Insert** | `INSERT INTO biltegiak (izena, biltegi_sku) VALUES (?, ?)` |
| **Sarrerak** | **List Join** | `SELECT s.id_sarrera, h.izena_soziala, s.data, s.egoera FROM sarrerak s JOIN hornitzaileak h ON s.hornitzailea_id = h.id_hornitzailea` |

---

#### Generated by Antigravity AI on 2026-02-12
