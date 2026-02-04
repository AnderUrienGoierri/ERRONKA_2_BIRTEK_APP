-- ========================================================
-- BIRTEK DATU-BASEA - SCRIPT OSOA
-- ========================================================

DROP DATABASE IF EXISTS birtek_db;
CREATE DATABASE IF NOT EXISTS birtek_db;
USE birtek_db;

-- ========================================================
-- 1. GEOGRAFIA
-- ========================================================

CREATE TABLE IF NOT EXISTS herriak (
    id_herria INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    izena VARCHAR(100) NOT NULL,
    lurraldea VARCHAR(100) NOT NULL,
    nazioa VARCHAR(100) NOT NULL
);

-- ========================================================
-- 2. ENPRESA EGITURA (Sailak, Biltegiak)
-- ========================================================

CREATE TABLE IF NOT EXISTS langile_sailak (
    id_saila INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    izena VARCHAR(100) NOT NULL,
    kokapena VARCHAR(100) NOT NULL,
    deskribapena TEXT
);

-- ========================================================
-- 3. ERABILTZAILEAK (Langileak, Bezeroak, Hornitzaileak)
-- ========================================================

CREATE TABLE IF NOT EXISTS langileak (
    id_langilea INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    izena VARCHAR(50) NOT NULL,
    abizena VARCHAR(100) NOT NULL,
    nan VARCHAR(9) UNIQUE,
    jaiotza_data DATE,
    
    -- Kokapena
    herria_id INT unsigned,
    helbidea VARCHAR(150),
    posta_kodea VARCHAR(5),
    telefonoa VARCHAR(20),
    
    -- Login datuak eta Hizkuntza
    emaila VARCHAR(100) UNIQUE NOT NULL,
    hizkuntza ENUM('Euskara', 'Gaztelania', 'Frantsesa', 'Ingelesa') DEFAULT 'Euskara',
    pasahitza VARCHAR(255),
    salto_txartela_uid VARCHAR(50) UNIQUE, -- Langilea identifikatzeko
    
    -- Lan datuak
    alta_data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    eguneratze_data DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    aktibo BOOLEAN NOT NULL DEFAULT 0,
    
    
    saila_id INT unsigned,
    iban VARCHAR(34)UNIQUE,
    
    kurrikuluma MEDIUMBLOB, -- pdf gordetzeko
    
    CONSTRAINT fk_langilea_saila FOREIGN KEY (saila_id) REFERENCES langile_sailak(id_saila),
    CONSTRAINT fk_langilea_herria FOREIGN KEY (herria_id) REFERENCES herriak(id_herria)
);

CREATE TABLE IF NOT EXISTS fitxaketak (
    id_fitxaketa INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    langilea_id INT UNSIGNED NOT NULL,
    data DATE NOT NULL DEFAULT (CURRENT_DATE),
    ordua TIME NOT NULL DEFAULT (CURRENT_TIME),
    mota ENUM('Sarrera', 'Irteera') NOT NULL DEFAULT 'Sarrera',
    eguneratze_data DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_fitxaketa_langilea FOREIGN KEY (langilea_id) REFERENCES langileak(id_langilea)
);

CREATE TABLE IF NOT EXISTS bezeroak (
    id_bezeroa INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    izena_edo_soziala VARCHAR(100) NOT NULL,
    abizena VARCHAR(100),
    ifz_nan VARCHAR(9) UNIQUE NOT NULL,
    jaiotza_data DATE,
    sexua ENUM('gizona', 'emakumea', 'ez-binarioa'),
    
    -- Ordaintzeko
    bezero_ordainketa_txartela VARCHAR(255),
    
    -- Bidalketarako
    helbidea VARCHAR(150) NOT NULL,
    herria_id INT UNSIGNED NOT NULL,
    posta_kodea VARCHAR(5) NOT NULL,
    telefonoa VARCHAR(15),
    
    -- Login eta Hizkuntza
    emaila VARCHAR(255) UNIQUE NOT NULL, -- NVARCHAR ordez VARCHAR erabilita bateragarritasunerako
    hizkuntza ENUM('Euskara', 'Gaztelania', 'Frantsesa', 'Ingelesa') NOT NULL DEFAULT 'Euskara',
    pasahitza VARCHAR(255) NOT NULL,
    
    alta_data DATETIME DEFAULT CURRENT_TIMESTAMP,
    eguneratze_data DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    aktibo BOOLEAN NOT NULL DEFAULT 1,
    
    CONSTRAINT fk_bezeroa_herria FOREIGN KEY (herria_id) REFERENCES herriak(id_herria)
);

CREATE TABLE IF NOT EXISTS hornitzaileak (
    id_hornitzailea INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    izena_soziala VARCHAR(100) NOT NULL,
    ifz_nan VARCHAR(9) UNIQUE NOT NULL,
    
    -- Kontaktu datuak
    kontaktu_pertsona VARCHAR(100),
    helbidea VARCHAR(150) NOT NULL,
    herria_id INT UNSIGNED NOT NULL,
    posta_kodea VARCHAR(5) NOT NULL,
    telefonoa VARCHAR(15),
    
    -- Login
    emaila VARCHAR(255) UNIQUE NOT NULL,
    hizkuntza ENUM('Euskara', 'Gaztelania', 'Frantsesa', 'Ingelesa') NOT NULL DEFAULT 'Gaztelania',
    pasahitza VARCHAR(255) NOT NULL,
    aktibo BOOLEAN NOT NULL DEFAULT 1,
    
    eguneratze_data DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_hornitzailea_herria FOREIGN KEY (herria_id) REFERENCES herriak(id_herria)
);

-- ========================================================
-- 4. PRODUKTU KATALOGOA ETA BILTEGIA
-- ========================================================

CREATE TABLE IF NOT EXISTS produktu_kategoriak (
    id_kategoria INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    izena VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS biltegiak (
    id_biltegia INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    izena VARCHAR(50) NOT NULL,
    biltegi_sku VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS produktuak (
    id_produktua INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    hornitzaile_id INT UNSIGNED NOT NULL,
    kategoria_id INT UNSIGNED NOT NULL,
    izena VARCHAR(255) NOT NULL,
    marka VARCHAR(50) NOT NULL,
    mota ENUM('Eramangarria', 'Mahai-gainekoa', 'Mugikorra', 'Tableta', 'Zerbitzaria', 'Pantaila', 'Softwarea') NOT NULL,
    
    deskribapena TEXT,
    irudia_url VARCHAR(255),
    
    -- Egoera
    biltegi_id INT UNSIGNED NOT NULL,
    produktu_egoera ENUM('Berria', 'Berritua A', 'Berritua B', 'Hondatua', 'Zehazteko') NOT NULL DEFAULT 'Zehazteko',
    produktu_egoera_oharra TEXT,
    salgai BOOLEAN DEFAULT FALSE,
    
    -- Datu Ekonomikoak
    salmenta_prezioa DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    stock INT UNSIGNED DEFAULT 0,
    eskaintza DECIMAL(5, 2) DEFAULT NULL,
    zergak_ehunekoa DECIMAL(5, 2) NOT NULL DEFAULT 21.00,
    
    sortze_data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    eguneratze_data DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_produktua_kategoria FOREIGN KEY (kategoria_id) REFERENCES produktu_kategoriak(id_kategoria),
    CONSTRAINT fk_produktua_hornitzailea FOREIGN KEY (hornitzaile_id) REFERENCES hornitzaileak(id_hornitzailea),
    CONSTRAINT fk_produktua_biltegia FOREIGN KEY (biltegi_id) REFERENCES biltegiak(id_biltegia)
);

-- ========================================================
-- SUBKLASEAK - PRODUKTUAK (HERENTZIA)
-- ========================================================

-- 1. Eramangarriak
CREATE TABLE IF NOT EXISTS eramangarriak (
    id_produktua INT UNSIGNED PRIMARY KEY,
    prozesadorea VARCHAR(100),
    ram_gb INT,
    diskoa_gb INT,
    pantaila_tamaina DECIMAL(4,1),
    bateria_wh INT,
    sistema_eragilea VARCHAR(50),
    pisua_kg DECIMAL(4,2),
    CONSTRAINT fk_eramangarria_produktua FOREIGN KEY eramangarriak(id_produktua) REFERENCES produktuak(id_produktua) ON DELETE CASCADE
);

-- 2. Mahai-gainekoak
CREATE TABLE IF NOT EXISTS mahai_gainekoak (
    id_produktua INT UNSIGNED PRIMARY KEY,
    prozesadorea VARCHAR(100),
    plaka_basea VARCHAR(100),
    ram_gb INT,
    diskoa_gb INT,
    txartel_grafikoa VARCHAR(100),
    elikatze_iturria_w INT,
    kaxa_formatua ENUM('ATX', 'Micro-ATX', 'Mini-ITX', 'E-ATX'),
    CONSTRAINT fk_mahaigainekoa_produktua FOREIGN KEY (id_produktua) REFERENCES produktuak(id_produktua) ON DELETE CASCADE
);

-- 3. Mugikorrak
CREATE TABLE IF NOT EXISTS mugikorrak (
    id_produktua INT UNSIGNED PRIMARY KEY,
    pantaila_teknologia VARCHAR(50),
    pantaila_hazbeteak DECIMAL(3,1),
    biltegiratzea_gb INT,
    ram_gb INT,
    kamera_nagusa_mp INT,
    bateria_mah INT,
    sistema_eragilea VARCHAR(50),
    sareak ENUM('4G', '5G'),
    CONSTRAINT fk_mugikorra_produktua FOREIGN KEY (id_produktua) REFERENCES produktuak(id_produktua) ON DELETE CASCADE
);

-- 4. Tabletak
CREATE TABLE IF NOT EXISTS tabletak (
    id_produktua INT UNSIGNED PRIMARY KEY,
    pantaila_hazbeteak DECIMAL(4,1),
    biltegiratzea_gb INT,
    konektibitatea ENUM('WiFi', 'WiFi + Cellular'),
    sistema_eragilea VARCHAR(50),
    bateria_mah INT,
    arkatzarekin_bateragarria BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_tableta_produktua FOREIGN KEY (id_produktua) REFERENCES produktuak(id_produktua) ON DELETE CASCADE
);

-- 5. Zerbitzariak
CREATE TABLE IF NOT EXISTS zerbitzariak (
    id_produktua INT UNSIGNED PRIMARY KEY,
    prozesadore_nukleoak INT,
    ram_mota ENUM('DDR4', 'DDR5', 'ECC'),
    disko_badiak INT,
    rack_unitateak INT,
    elikatze_iturri_erredundantea BOOLEAN DEFAULT TRUE,
    raid_kontroladora VARCHAR(50),
    CONSTRAINT fk_zerbitzaria_produktua FOREIGN KEY (id_produktua) REFERENCES produktuak(id_produktua) ON DELETE CASCADE
);

-- 6. Pantailak
CREATE TABLE IF NOT EXISTS pantailak (
    id_produktua INT UNSIGNED PRIMARY KEY,
    hazbeteak DECIMAL(4,1),
    bereizmena VARCHAR(20),
    panel_mota ENUM('IPS', 'VA', 'TN', 'OLED'),
    freskatze_tasa_hz INT,
    konexioak VARCHAR(150),
    kurbatura VARCHAR(10),
    CONSTRAINT fk_pantaila_produktua FOREIGN KEY (id_produktua) REFERENCES produktuak(id_produktua) ON DELETE CASCADE
);

-- 7. Softwareak (ZUZENDUA)
CREATE TABLE IF NOT EXISTS softwareak (
    id_produktua INT UNSIGNED PRIMARY KEY,
    software_mota ENUM('Sistema Eragilea', 'Ofimatika', 'Antibirusa', 'Bestelakoak') NOT NULL,
    lizentzia_mota ENUM('OEM', 'Retail', 'Harpidetza', 'OpenSource', 'GPL', 'LGPL') DEFAULT 'Retail',
    bertsioa VARCHAR(50),
    garatzailea VARCHAR(100),
    librea BOOLEAN DEFAULT FALSE, -- Koma gehitu da eta sintaxia zuzendu da

    CONSTRAINT fk_softwarea_produktua FOREIGN KEY (id_produktua) REFERENCES produktuak(id_produktua) ON DELETE CASCADE
);

-- ========================================================
-- 4.1. TAILERRA ETA KONPONKETAK
-- ========================================================

CREATE TABLE IF NOT EXISTS akatsak (
    id_akatsa INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    izena VARCHAR(100) NOT NULL,
    deskribapena TEXT
);

CREATE TABLE IF NOT EXISTS konponketak (
    id_konponketa INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    produktua_id INT UNSIGNED NOT NULL,
    langilea_id INT UNSIGNED NOT NULL,
    hasiera_data DATETIME DEFAULT CURRENT_TIMESTAMP,
    amaiera_data DATETIME,
    konponketa_egoera ENUM('Prozesuan', 'Konponduta', 'Konponezina') NOT NULL DEFAULT 'Prozesuan',
    akatsa_id INT UNSIGNED NOT NULL,
    oharrak TEXT,
    eguneratze_data DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_konponketa_produktua FOREIGN KEY (produktua_id) REFERENCES produktuak(id_produktua),
    CONSTRAINT fk_konponketa_langilea FOREIGN KEY (langilea_id) REFERENCES langileak(id_langilea)
);

-- ========================================================
-- 5. LOGISTIKA (Sarrerak)
-- ========================================================

CREATE TABLE IF NOT EXISTS sarrerak (
    id_sarrera INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    data DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    eguneratze_data DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    hornitzailea_id INT UNSIGNED NOT NULL,
    langilea_id INT UNSIGNED NOT NULL,
    sarrera_egoera ENUM('Bidean', 'Jasota', 'Ezabatua') NOT NULL DEFAULT 'Bidean',
    
    CONSTRAINT fk_sarrera_hornitzailea FOREIGN KEY (hornitzailea_id) REFERENCES hornitzaileak(id_hornitzailea),
    CONSTRAINT fk_sarrera_langilea FOREIGN KEY (langilea_id) REFERENCES langileak(id_langilea)
);

CREATE TABLE IF NOT EXISTS sarrera_lerroak (
    id_sarrera_lerroa INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    sarrera_id INT UNSIGNED NOT NULL,
    produktua_id INT UNSIGNED NOT NULL,
    kantitatea INT UNSIGNED NOT NULL,
    sarrera_lerro_egoera ENUM('Bidean', 'Jasota', 'Ezabatua') NOT NULL DEFAULT 'Bidean',
    CONSTRAINT fk_sl_sarrera FOREIGN KEY (sarrera_id) REFERENCES sarrerak(id_sarrera),
    CONSTRAINT fk_sl_produktua FOREIGN KEY (produktua_id) REFERENCES produktuak(id_produktua)
);

-- ========================================================
-- 6. SALMENTAK (Eskaerak)
-- ========================================================

CREATE TABLE IF NOT EXISTS eskaerak (
    id_eskaera INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    bezeroa_id INT UNSIGNED NOT NULL,
    langilea_id INT UNSIGNED,
    data DATETIME DEFAULT CURRENT_TIMESTAMP,
    eguneratze_data DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    guztira_prezioa DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    
    faktura_zenbakia VARCHAR(20) UNIQUE,
	faktura_url VARCHAR(255),
    
    eskaera_egoera ENUM('Prestatzen', 'Osatua/Bidalita', 'Ezabatua') NOT NULL DEFAULT 'Prestatzen',
    
    CONSTRAINT fk_eskaera_bezeroa FOREIGN KEY (bezeroa_id) REFERENCES bezeroak(id_bezeroa),
    CONSTRAINT fk_eskaera_langilea FOREIGN KEY (langilea_id) REFERENCES langileak(id_langilea)
);

CREATE TABLE IF NOT EXISTS eskaera_lerroak (
    id_eskaera_lerroa INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    eskaera_id INT UNSIGNED NOT NULL,
    produktua_id INT UNSIGNED NOT NULL,
    kantitatea INT UNSIGNED NOT NULL,
    unitate_prezioa DECIMAL(10, 2) NOT NULL,
    
    eskaera_lerro_egoera ENUM('Prestatzen', 'Osatua/Bidalita', 'Ezabatua') NOT NULL DEFAULT 'Prestatzen',
    
    CONSTRAINT fk_el_eskaera FOREIGN KEY (eskaera_id) REFERENCES eskaerak(id_eskaera),
    CONSTRAINT fk_el_produktua FOREIGN KEY (produktua_id) REFERENCES produktuak(id_produktua)
);


-- ========================================================
-- 7. ERABILTZAILEAK ETA BAIMENAK
-- ========================================================

FLUSH PRIVILEGES;

-- ZUZENDARITZA (SysAdmin) 
CREATE USER IF NOT EXISTS 'ander_sysadmin'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'ander_sysadmin'@'localhost' WITH GRANT OPTION;

CREATE USER IF NOT EXISTS 'lander_sysadmin'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'lander_sysadmin'@'localhost' WITH GRANT OPTION;

-- ADMINISTRAZIOA
CREATE USER IF NOT EXISTS 'ane_admin'@'localhost' IDENTIFIED BY '1234';
CREATE USER IF NOT EXISTS 'mikel_admin'@'localhost' IDENTIFIED BY '1234';

GRANT SELECT, INSERT, UPDATE, DELETE ON birtek_db.langileak TO 'ane_admin'@'localhost', 'mikel_admin'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON birtek_db.langile_sailak TO 'ane_admin'@'localhost', 'mikel_admin'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON birtek_db.fitxaketak TO 'ane_admin'@'localhost', 'mikel_admin'@'localhost';
GRANT SELECT, INSERT, UPDATE ON birtek_db.eskaerak TO 'ane_admin'@'localhost', 'mikel_admin'@'localhost'; -- fakturak ikusi behar dituzte
GRANT SELECT, UPDATE, DELETE ON birtek_db.hornitzaileak TO 'ane_admin'@'localhost', 'mikel_admin'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON birtek_db.herriak TO 'ane_admin'@'localhost', 'mikel_admin'@'localhost';

-- SALMENTAK
CREATE USER IF NOT EXISTS 'leire_sales'@'localhost' IDENTIFIED BY '1234';
CREATE USER IF NOT EXISTS 'iker_sales'@'localhost' IDENTIFIED BY '1234';
CREATE USER IF NOT EXISTS 'amaia_sales'@'localhost' IDENTIFIED BY '1234';

GRANT SELECT, INSERT, UPDATE, DELETE ON birtek_db.bezeroak TO 'leire_sales'@'localhost', 'iker_sales'@'localhost', 'amaia_sales'@'localhost';
GRANT SELECT, UPDATE ON birtek_db.produktuak TO 'leire_sales'@'localhost', 'iker_sales'@'localhost', 'amaia_sales'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON birtek_db.eskaerak TO 'leire_sales'@'localhost', 'iker_sales'@'localhost', 'amaia_sales'@'localhost';  -- fakturak orain eskaerak taula kudeatzen dira
GRANT SELECT, INSERT, UPDATE, DELETE ON birtek_db.eskaera_lerroak TO 'leire_sales'@'localhost', 'iker_sales'@'localhost', 'amaia_sales'@'localhost';
GRANT SELECT, INSERT, UPDATE ON birtek_db.herriak TO 'leire_sales'@'localhost', 'iker_sales'@'localhost', 'amaia_sales'@'localhost';
-- TEKNIKOAK SAT
CREATE USER IF NOT EXISTS 'unai_sat'@'localhost' IDENTIFIED BY '1234';
CREATE USER IF NOT EXISTS 'maite_sat'@'localhost' IDENTIFIED BY '1234';
CREATE USER IF NOT EXISTS 'aitor_sat'@'localhost' IDENTIFIED BY '1234';
CREATE USER IF NOT EXISTS 'nerea_sat'@'localhost' IDENTIFIED BY '1234';

GRANT SELECT, INSERT, UPDATE, DELETE ON birtek_db.produktuak TO 'unai_sat'@'localhost', 'maite_sat'@'localhost', 'aitor_sat'@'localhost', 'nerea_sat'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON birtek_db.konponketak TO 'unai_sat'@'localhost', 'maite_sat'@'localhost', 'aitor_sat'@'localhost', 'nerea_sat'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON birtek_db.akatsak TO 'unai_sat'@'localhost', 'maite_sat'@'localhost', 'aitor_sat'@'localhost', 'nerea_sat'@'localhost';
GRANT SELECT, INSERT, UPDATE ON birtek_db.herriak TO 'unai_sat'@'localhost', 'maite_sat'@'localhost', 'aitor_sat'@'localhost', 'nerea_sat'@'localhost';

-- LOGISTIKA
CREATE USER IF NOT EXISTS 'gorka_biltegia'@'localhost' IDENTIFIED BY '1234';
CREATE USER IF NOT EXISTS 'oihane_biltegia'@'localhost' IDENTIFIED BY '1234';
CREATE USER IF NOT EXISTS 'xabier_biltegia'@'localhost' IDENTIFIED BY '1234';

GRANT SELECT, INSERT, UPDATE ON birtek_db.produktuak TO 'gorka_biltegia'@'localhost', 'oihane_biltegia'@'localhost', 'xabier_biltegia'@'localhost';
GRANT SELECT, INSERT, UPDATE ON birtek_db.hornitzaileak TO 'gorka_biltegia'@'localhost', 'oihane_biltegia'@'localhost', 'xabier_biltegia'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON birtek_db.biltegiak TO 'gorka_biltegia'@'localhost', 'oihane_biltegia'@'localhost', 'xabier_biltegia'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON birtek_db.sarrera_lerroak TO 'gorka_biltegia'@'localhost', 'oihane_biltegia'@'localhost', 'xabier_biltegia'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON birtek_db.sarrerak TO 'gorka_biltegia'@'localhost', 'oihane_biltegia'@'localhost', 'xabier_biltegia'@'localhost';
GRANT SELECT, UPDATE ON birtek_db.eskaera_lerroak TO 'gorka_biltegia'@'localhost', 'oihane_biltegia'@'localhost', 'xabier_biltegia'@'localhost';
GRANT SELECT, INSERT, UPDATE ON birtek_db.herriak TO 'gorka_biltegia'@'localhost', 'oihane_biltegia'@'localhost', 'xabier_biltegia'@'localhost';

-- FITXAKETAK (Langile guztieenak)
GRANT SELECT, INSERT ON birtek_db.fitxaketak TO 

-- 'lander_sysadmin'            IADA BADUTE crud osoa
-- 'ander_sysadmin'@'localhost' IADA BADUTE crud osoa
-- 'ane_admin'@'localhost',     IADA BADUTE crud osoa
-- 'mikel_admin'@'localhost';   IADA BADUTE crud osoa
    'leire_sales'@'localhost', 
    'iker_sales'@'localhost', 
    'amaia_sales'@'localhost', 
    'unai_sat'@'localhost', 
    'maite_sat'@'localhost', 
    'aitor_sat'@'localhost', 
    'nerea_sat'@'localhost', 
    'gorka_biltegia'@'localhost', 
    'oihane_biltegia'@'localhost', 
    'xabier_biltegia'@'localhost';

FLUSH PRIVILEGES;


-- ========================================================================================================================================

-- ========================================================================================================================
-- INSERT INTO DATUEN TXERTAKETA
-- ========================================================================================================================

-- 1. OINARRIZKO DATUAK
INSERT INTO biltegiak (id_biltegia, izena, biltegi_sku) VALUES 
(1, 'Harrera Biltegia', 'HAR_BIL'),
(2, 'Biltegi Nagusia', 'BIL_NAG'),
(3, 'Irteera Biltegia', 'IRT_BIL');

INSERT INTO produktu_kategoriak (id_kategoria, izena) VALUES 
(1, 'Ordenagailuak'),
(2, 'Telefonia'),
(3, 'Irudia'),
(4, 'Osagarriak'),
(5, 'Softwarea'),
(6, 'Sareak eta Zerbitzariak');

-- 2. GEOGRAFIA ETA LANGILEAK
INSERT INTO herriak (id_herria, izena, lurraldea, nazioa) VALUES
(1, 'Donostia', 'Gipuzkoa', 'Euskal Herria'),
(2, 'Bilbo', 'Bizkaia', 'Euskal Herria'),
(3, 'Gasteiz', 'Araba', 'Euskal Herria'),
(4, 'Iruña', 'Nafarroa', 'Euskal Herria'),
(5, 'Eibar', 'Gipuzkoa', 'Euskal Herria'),
(6, 'Zarautz', 'Gipuzkoa', 'Euskal Herria'),
(7, 'Irun', 'Gipuzkoa', 'Euskal Herria'),
(8, 'Errenteria', 'Gipuzkoa', 'Euskal Herria'),
(9, 'Hernani', 'Gipuzkoa', 'Euskal Herria'),
(10, 'Tolosa', 'Gipuzkoa', 'Euskal Herria'),
(11, 'Arrasate', 'Gipuzkoa', 'Euskal Herria'),
(12, 'Bergara', 'Gipuzkoa', 'Euskal Herria'),
(13, 'Azpeitia', 'Gipuzkoa', 'Euskal Herria'),
(14, 'Beasain', 'Gipuzkoa', 'Euskal Herria'),
(15, 'Hondarribia', 'Gipuzkoa', 'Euskal Herria'),
(16, 'Getxo', 'Bizkaia', 'Euskal Herria'),
(17, 'Barakaldo', 'Bizkaia', 'Euskal Herria'),
(18, 'Portugalete', 'Bizkaia', 'Euskal Herria'),
(19, 'Santurtzi', 'Bizkaia', 'Euskal Herria'),
(20, 'Basauri', 'Bizkaia', 'Euskal Herria'),
(21, 'Durango', 'Bizkaia', 'Euskal Herria'),
(22, 'Gernika-Lumo', 'Bizkaia', 'Euskal Herria'),
(23, 'Bermeo', 'Bizkaia', 'Euskal Herria'),
(24, 'Mungia', 'Bizkaia', 'Euskal Herria'),
(25, 'Ermua', 'Bizkaia', 'Euskal Herria'),
(26, 'Laudio', 'Araba', 'Euskal Herria'),
(27, 'Amurrio', 'Araba', 'Euskal Herria'),
(28, 'Agurain', 'Araba', 'Euskal Herria'),
(29, 'Guardia', 'Araba', 'Euskal Herria'),
(30, 'Oion', 'Araba', 'Euskal Herria'),
(31, 'Legutio', 'Araba', 'Euskal Herria'),
(32, 'Tutera', 'Nafarroa', 'Euskal Herria'),
(33, 'Lizarra', 'Nafarroa', 'Euskal Herria'),
(34, 'Tafalla', 'Nafarroa', 'Euskal Herria'),
(35, 'Zangoza', 'Nafarroa', 'Euskal Herria'),
(36, 'Altsasu', 'Nafarroa', 'Euskal Herria'),
(37, 'Baztan', 'Nafarroa', 'Euskal Herria'),
(38, 'Corella', 'Nafarroa', 'Euskal Herria'),
(39, 'Zizur Nagusia', 'Nafarroa', 'Euskal Herria'),
(40, 'Baiona', 'Lapurdi', 'Euskal Herria'),
(41, 'Biarritz', 'Lapurdi', 'Euskal Herria'),
(42, 'Angelu', 'Lapurdi', 'Euskal Herria'),
(43, 'Donibane Lohizune', 'Lapurdi', 'Euskal Herria'),
(44, 'Hendaia', 'Lapurdi', 'Euskal Herria'),
(45, 'Azkaine', 'Lapurdi', 'Euskal Herria'),
(46, 'Donibane Garazi', 'Nafarroa Beherea', 'Euskal Herria'),
(47, 'Baigorri', 'Nafarroa Beherea', 'Euskal Herria'),
(48, 'Donapaleu', 'Nafarroa Beherea', 'Euskal Herria'),
(49, 'Maule-Lextarre', 'Zuberoa', 'Euskal Herria'),
(50, 'Atharratze-Sorholüze', 'Zuberoa', 'Euskal Herria'),
(51, 'Barkoxe', 'Zuberoa', 'Euskal Herria'),
(52, 'Lekeitio', 'Bizkaia', 'Euskal Herria'),
(53, 'Ondarroa', 'Bizkaia', 'Euskal Herria'),
(54, 'Zumaia', 'Gipuzkoa', 'Euskal Herria'),
(55, 'Getaria', 'Gipuzkoa', 'Euskal Herria'),
(56, 'Mutriku', 'Gipuzkoa', 'Euskal Herria');

INSERT INTO langile_sailak (id_saila, izena, kokapena, deskribapena) VALUES
(1, 'Zuzendaritza','Goi bulegoa', 'DIR'),
(2, 'Administrazioa','Harrera bulegoa', 'ADMIN'),
(3, 'Salmentak','Harrera', 'COM'),
(4, 'Zerbitzu Teknikoa','Tailerra', 'SAT'),
(5, 'Logistika eta Biltegia','Biltegiak', 'LOG');

INSERT INTO langileak (id_langilea, izena, abizena, nan, jaiotza_data, herria_id, helbidea, posta_kodea, telefonoa, emaila, pasahitza, salto_txartela_uid, saila_id, iban, alta_data, aktibo) VALUES
(1, 'Ander', 'Urien', '72484472H', '1992-03-02', 1, 'Askatasun Hiribidea 5', '20004', '600111222', 'ander@birtek.eus', '1234', 'UID_ADMIN_01', 1, 'ES1234567890123456789001', '2020-01-01', 1),
(2, 'Lander', 'Garmendia', '12345678Z', '2000-03-02', 1, 'Askatasun Hiribidea 5', '20004', '600111222', 'lander@birtek.eus', '1234', 'UID_ADMIN_02', 1, 'ES1234567890123456789002', '2020-01-01', 1),
(3, 'Ane', 'Lasa', '22222222B', '1985-03-20', 1, 'Easo Kalea 12', '20006', '600333444', 'ane.lasa@birtek.eus', '1234', 'UID_ADMIN_03', 2, 'ES1234567890123456789003', '2020-02-15', 1),
(4, 'Mikel', 'Otegi', '33333333C', '1990-11-10', 6, 'Nafarroa Kalea 3', '20800', '600555666', 'mikel.otegi@birtek.eus', '1234', 'UID_ADMIN_04', 2, 'ES1234567890123456789004', '2021-05-20', 1),
(5, 'Leire', 'Mendizabal', '44444444D', '1992-07-08', 2, 'Gran Via 25', '48001', '600777888', 'leire.mendi@birtek.eus', '1234', 'UID_COM_01', 3, 'ES1234567890123456789005', '2021-06-01', 1),
(6, 'Iker', 'Iriondo', '55555555E', '1995-01-30', 2, 'Licenciado Poza 10', '48011', '600999000', 'iker.iriondo@birtek.eus', '1234', 'UID_COM_02', 3, 'ES1234567890123456789006', '2022-03-10', 1),
(7, 'Amaia', 'Goikoetxea', '66666666F', '1988-09-12', 5, 'Isasi Kalea 4', '20600', '600123123', 'amaia.goiko@birtek.eus', '1234', 'UID_COM_03', 3, 'ES1234567890123456789007', '2019-11-05', 1),
(8, 'Unai', 'Zabala', '77777777G', '1993-04-25', 1, 'Tolosa Hiribidea 45', '20018', '600456456', 'unai.zabala@birtek.eus', '1234', 'UID_SAT_01', 4,'ES1234567890123456789008', '2020-09-15', 1),
(9, 'Maite', 'Arregi', '88888888H', '1996-12-05', 3, 'Dato Kalea 15', '01005', '600789789', 'maite.arregi@birtek.eus', '1234', 'UID_SAT_02', 4, 'ES1234567890123456789009', '2023-01-10', 1),
(10, 'Aitor', 'Bilbao', '99999999I', '1991-08-18', 2, 'Indautxu Plaza 2', '48010', '600321321', 'aitor.bilbao@birtek.eus', '1234', 'UID_SAT_03', 4, 'ES1234567890123456789010', '2021-02-28', 1),
(11, 'Nerea', 'Etxaniz', '12345678J', '1994-06-14', 6, 'Malekoia 8', '20800', '600654654', 'nerea.etxaniz@birtek.eus', '1234', 'UID_SAT_04', 4,'ES1234567890123456789011', '2022-07-20', 1),
(12, 'Gorka', 'Ugarte', '87654321K', '1987-02-22', 3, 'Gamarra Atea 4', '01013', '600987987', 'gorka.ugarte@birtek.eus', '1234', 'UID_LOG_01', 5, 'ES1234567890123456789012', '2018-05-12', 1),
(13, 'Oihane', 'Ibarra', '23456789L', '1998-10-30', 3, 'Frantzia Kalea 20', '01002', '600147258', 'oihane.ibarra@birtek.eus', '1234', 'UID_LOG_02', 5, 'ES1234567890123456789013', '2023-06-15', 1),
(14, 'Xabier', 'Larrea', '34567890M', '1990-03-15', 1, 'Intxaurrondo 50', '20015', '600258369', 'xabier.larrea@birtek.eus', '1234', 'UID_LOG_03', 5, 'ES1234567890123456789014', '2020-11-30', 1);

-- 3. HORNITZAILEAK ETA BEZEROAK
INSERT INTO hornitzaileak (id_hornitzailea, izena_soziala, ifz_nan, kontaktu_pertsona, helbidea, herria_id, posta_kodea, telefonoa, emaila, pasahitza) VALUES 
(1, 'PC Componentes Pro', 'A88776655', 'Soporte B2B', 'Poligono Industrial Alhama', 6, '28001', '910000000', 'b2b@pccomponentes.com', 'hash_pcc'),
(2, 'Ingram Micro', 'A11223344', 'Carlos Distribución', 'Calle Tecnología 5', 6, '28002', '910111222', 'pedidos@ingram.com', 'hash_ingram'),
(3, 'Amazon Business', 'W8888888', 'Logistika Zentroa', 'Trapagaran Poligonoa', 1, '48510', '900800700', 'business@amazon.es', 'hash_amazon'),
(4, 'Eusko Tech Solutions', 'B12345678', 'Ainhoa Etxebarria', 'Industrialdea 12', 2, '48001', '944001122', 'info@euskotech.eus', 'hash_4'),
(5, 'Gipuzkoako Logistika', 'A23456789', 'Mikel Iturbe', 'Poligono Belartza 4', 1, '20018', '943556677', 'mikel@giplogi.com', 'hash_5'),
(6, 'Araba Software', 'B34567890', 'Elena Perez', 'Postas kalea 23', 3, '01001', '945889900', 'kontaktua@arabasoft.eus', 'hash_6'),
(7, 'Nafarroa Hornikuntzak', 'A45678901', 'Inaxio Mujika', 'Industrialdea Ezkabarte', 4, '31013', '948112233', 'ventas@nafarhorni.es', 'hash_7'),
(8, 'Bidasoa Web Design', 'B56789012', 'Ane Arana', 'Iparralde Hiribidea 1', 7, '20301', '943665544', 'ane@bidasoaweb.eus', 'hash_8'),
(9, 'Durangoko Mekanizatuak', 'B67890123', 'Jon Ugarte', 'Tabira Industrialdea', 21, '48200', '946201584', 'jugarte@durmek.com', 'hash_9'),
(10, 'Lapurdi Distribución', 'A78901234', 'Maite Etcheverry', 'Rue de la Gare 5', 40, '64100', '055912345', 'maite@lapurdist.fr', 'hash_10'),
(11, 'Debagoiena Erremintak', 'B89012345', 'Karlos Arregi', 'Olandixo kalea z/g', 11, '20500', '943771122', 'erremintak@debagoiena.eus', 'hash_11'),
(12, 'Zuberoa Craft', 'B90123456', 'Pantaléon Aguer', 'Place du Marché', 49, '64130', '055965432', 'pantal@zubecraft.com', 'hash_12'),
(13, 'Bermeoko Arraina', 'A01234567', 'Begoña Lertxundi', 'Kai Zaharra 10', 23, '48370', '946881234', 'bego@bermeoarrain.eus', 'hash_13'),
(14, 'Tolosako Paperak', 'B11112222', 'Joxe Mari Mendizabal', 'San Esteban kalea 2', 10, '20400', '943675000', 'joxemari@tolopap.com', 'hash_14'),
(15, 'Baztan Esnekiak', 'B22223333', 'Irati Elizondo', 'Gaintza bidea', 37, '31700', '948580123', 'irati@baztanesne.eus', 'hash_15'),
(16, 'Goierri Altzariak', 'A33334444', 'Oier Mujika', 'Poligono Itola 8', 14, '20200', '943885522', 'oier@goierrial.com', 'hash_16'),
(17, 'Ermua IT Services', 'B44445555', 'Sara Gomez', 'Zubiaurre 15', 25, '48260', '943171122', 'sgomez@ermuait.es', 'hash_17'),
(18, 'Hernani Garraioak', 'A55556666', 'Patxi Galarraga', 'Poligono Ibaiondo', 9, '20120', '943552211', 'patxi@hernigarrai.eus', 'hash_18'),
(19, 'Getxo Digital', 'B66667777', 'Lucia Martinez', 'Las Arenas kalea 3', 16, '48930', '944631100', 'lucia@getxodigital.es', 'hash_19'),
(20, 'Oion Enologia', 'A77778888', 'Ramón Sáenz', 'Carretera Logroño z/g', 30, '01320', '941334455', 'info@oionenologia.com', 'hash_20'),
(21, 'Lizarra Marketing', 'B88889999', 'Nerea Ruiz', 'San Francisco kalea 12', 33, '31200', '948556677', 'nruiz@lizarramark.es', 'hash_21'),
(22, 'Biarritz Events', 'A99990000', 'Jean-Luc Dubois', 'Avenue de la Reine', 41, '64200', '055977889', 'jeanluc@biarritzevents.fr', 'hash_22'),
(23, 'Azpeitia Metal', 'B12121212', 'Xabier Alkorta', 'Urola Industrialdea', 13, '20730', '943812233', 'xalkorta@azpeitiametal.eus', 'hash_23'),
(24, 'Tutera Baratza', 'A23232323', 'Javier Jimeno', 'Camino del Huerto', 32, '31500', '948821100', 'jjimeno@tuterabaratza.com', 'hash_24'),
(25, 'Barakaldo Komunikazioa', 'B34343434', 'Marta Gonzalez', 'Foruen kalea 5', 17, '48901', '944371122', 'marta@barakom.es', 'hash_25'),
(26, 'Zarautz Surf Supply', 'A45454545', 'Aritz Bergara', 'Malekoi kalea 22', 6, '20800', '943831155', 'aritz@zarautzsurf.eus', 'hash_26'),
(27, 'Donapaleu Agro', 'B56565656', 'Pierre Etxebarne', 'Route de Bayonne', 48, '64120', '055922334', 'pierre@donapagro.fr', 'hash_27'),
(28, 'Bergara Elektrika', 'A67676767', 'Inma Gabilondo', 'Masterreka kalea 1', 12, '20570', '943761100', 'inma@bergelek.com', 'hash_28'),
(29, 'Gernika Bakea Tech', 'B78787878', 'Kepa Bilbao', 'Lurgorri kalea 45', 22, '48300', '946252233', 'kepa@gernikatech.eus', 'hash_29'),
(30, 'Laudio Beira', 'A89898989', 'Txema Luiaondo', 'Zubiaur kalea 8', 26, '01400', '946721155', 'txema@laudiobeira.com', 'hash_30'),
(31, 'Altsasu Trenak', 'B90909090', 'Unai Galan', 'Zelai kalea 10', 36, '31800', '948561122', 'unai@altsasutren.eus', 'hash_31'),
(32, 'Baiona Gazta', 'A01010101', 'Marie Larousse', 'Quai de la Nive', 40, '64100', '055933445', 'marie@baionagazta.fr', 'hash_32'),
(33, 'Eibar Armagintza', 'B12123434', 'Josu Arregui', 'Ipurua kalea 3', 5, '20600', '943201122', 'josu@eibararma.eus', 'hash_33');

INSERT INTO bezeroak (izena_edo_soziala, abizena, ifz_nan, jaiotza_data, sexua, bezero_ordainketa_txartela, helbidea, herria_id, posta_kodea, telefonoa, emaila, hizkuntza, pasahitza, aktibo) VALUES 
('Ane', 'Goikoetxea Lasa', '12345678A', '1990-05-15', 'emakumea', 'tok_visa_4242', 'Askatasunaren Etorbidea 14, 2.B', 1, '20004', '600123456', 'ane.goiko@email.eus', 'Euskara', 'pasahitzaSegurua1', 1),
('Jon', 'Perez Garcia', '87654321B', '1985-11-20', 'gizona', 'tok_mastercard_5555', 'Kale Nagusia 30', 2, '48001', '611222333', 'jon.perez@gmail.com', 'Gaztelania', '123456Jon', 1),
('Teknologia Berriak SL', NULL, 'B99887766', NULL, NULL, 'tok_amex_9090', 'Jundiz Industrialdea, Pab 5', 3, '01015', '945111222', 'info@teknologiaberriak.com', 'Euskara', 'admin2024', 1),
('Alex', 'Etxebarria', '44556677C', '2002-02-10', 'ez-binarioa', NULL, 'Estafeta Kalea 12', 4, '31001', '666777888', 'alex.etxe@protonmail.com', 'Ingelesa', 'alex_pass_secure', 1),
('Sarah', 'Dubois', 'X1234567Z', '1995-07-30', 'emakumea', 'tok_cb_3333', 'Rue du Port 5', 5, '64200', '+33612345678', 'sarah.dubois@orange.fr', 'Frantsesa', 'monmotdepasse', 0),

-- 30 bezero berri
('Mikel', 'Arregi Iturbe', '11223344D', '1988-03-12', 'gizona', 'tok_visa_1111', 'Artekale 5, 1.esk', 2, '48005', '600111222', 'mikel.arregi@euskaltel.net', 'Euskara', 'mikel88pass', 1),
('Ainhoa', 'Zubizarreta Marañon', '22334455E', '1992-09-25', 'emakumea', 'tok_mc_2222', 'Nafarroa Etorbidea 22', 7, '20301', '622333444', 'ainhoa.zubi@gmail.com', 'Euskara', 'zubi92safe', 1),
('Iker', 'Lasa Otegi', '33445566F', '1980-12-05', 'gizona', NULL, 'San Francisco kalea 3', 10, '20400', '633444555', 'iker.lasa@outlook.com', 'Gaztelania', 'iker1980_!', 1),
('Maite', 'Ugarteburu Solozabal', '44556677G', '1998-06-18', 'emakumea', 'tok_visa_4444', 'Urbieta kalea 40', 1, '20006', '644555666', 'maite.uga@email.com', 'Euskara', 'maite_pass', 1),
('Unai', 'Badiola Askasibar', '55667788H', '1975-01-30', 'gizona', 'tok_mc_5555', 'Bidebarrieta 12', 5, '20600', '655666777', 'unai.badiola@terra.es', 'Gaztelania', 'unai75old', 1),
('Amaia', 'Iparragirre Garmendia', '66778899I', '1994-08-14', 'emakumea', NULL, 'Kondeko Aldapa 2', 13, '20730', '666777888', 'amaia.ipar@gmail.com', 'Euskara', 'amaia94!', 1),
('Kepa', 'Bilbao Zenarruzabeitia', '77889900J', '1982-10-22', 'gizona', 'tok_visa_7777', 'Lurgorri kalea 15', 22, '48300', '677888999', 'kepa.bilbao@gernika.eus', 'Euskara', 'kepa_bak', 1),
('Nerea', 'Mendizabal Alkorta', '88990011K', '1991-04-03', 'emakumea', 'tok_mc_8888', 'Zubiaurre kalea 8', 25, '48260', '688999000', 'nerea.mendi@yahoo.com', 'Euskara', 'nerea91sql', 1),
('Gorka', 'Otxoa de Eribe', '99001122L', '1987-11-11', 'gizona', NULL, 'Avenida Gasteiz 55', 3, '01008', '699000111', 'gorka.otxoa@araba.eus', 'Gaztelania', 'gorka87gs', 1),
('Leire', 'Apraiz Larrañaga', '00112233M', '2000-02-28', 'emakumea', 'tok_visa_0000', 'Itxas Aurre 4', 23, '48370', '611000222', 'leire.apraiz@bermeo.com', 'Euskara', 'leire2000', 1),
('Euskal Janariak SL', NULL, 'B11223344', NULL, NULL, 'tok_amex_1212', 'Industrialdea 4, Pab 2', 21, '48200', '946202020', 'pedidos@euskaljanariak.eus', 'Euskara', 'janari_2024', 1),
('Jean-Pierre', 'Etcheberry', 'X9988776W', '1970-05-20', 'gizona', 'tok_cb_9999', 'Rue Mazagran 12', 40, '64100', '+33559112233', 'jp.etche@orange.fr', 'Frantsesa', 'jp_baiona', 1),
('Miren', 'Agirre Ganboa', '22114433N', '1993-07-07', 'emakumea', NULL, 'Kale Berria 45', 33, '31200', '633112244', 'miren.agirre@navarra.es', 'Gaztelania', 'miren93nav', 1),
('Beñat', 'Gaztelumendi Arana', '33225544O', '1986-09-12', 'gizona', 'tok_mc_3322', 'Txirrita kalea 2', 8, '20100', '644223355', 'benat.gazte@bertso.eus', 'Euskara', 'benat_pass', 1),
('Lorea', 'Etxaniz Mendieta', '44336655P', '1997-12-25', 'emakumea', 'tok_visa_4433', 'Donibane kalea 10', 54, '20750', '655334466', 'lorea.etxaniz@gmail.com', 'Euskara', 'lorea97Xmas', 1),
('Joseba', 'Iraola Mujika', '55447766Q', '1968-02-14', 'gizona', NULL, 'Olandixo 1', 11, '20500', '666445577', 'joseba.iraola@mondragon.edu', 'Euskara', 'joseba68', 1),
('Sonia', 'Rodriguez Diez', '66558877R', '1984-11-30', 'emakumea', 'tok_visa_6655', 'Gran Via 22', 2, '48001', '677556688', 'sonia.rodriguez@hotmail.com', 'Gaztelania', 'sonia84bilbo', 1),
('Patxi', 'Lopez de Uralde', '77669988S', '1978-04-18', 'gizona', NULL, 'Postas 14', 3, '01001', '688667799', 'patxi.uralde@euskadi.eus', 'Euskara', 'patxi78_araba', 1),
('Ines', 'Santamaria Ruiz', '88770099T', '1995-10-05', 'emakumea', 'tok_mc_8877', 'Avenida Sancho el Fuerte 2', 4, '31007', '699778800', 'ines.santa@gmail.com', 'Gaztelania', 'ines95pamplona', 1),
('Iñaki', 'Garmendia Otaegi', '99881100U', '1989-06-22', 'gizona', 'tok_visa_9988', 'Idiazabal kalea 5', 14, '20200', '600889911', 'inaki.gar@outlook.es', 'Euskara', 'inaki89beasain', 1),
('Izaskun', 'Larrañaga Azurmendi', '11225566V', '1992-01-15', 'emakumea', NULL, 'Nafarroa Etorbidea 1', 10, '20400', '611990022', 'izaskun.larra@gmail.com', 'Euskara', 'izaskun92', 1),
('Bakartxo', 'Ruiz de Gauna', '22336677W', '1981-05-30', 'emakumea', 'tok_visa_2233', 'Estafeta 50', 4, '31001', '622001133', 'bakartxo.ruiz@nafarroa.eus', 'Euskara', 'bakartxo81', 1),
('Xabier', 'Alberdi Elorza', '33447788X', '1996-08-08', 'gizona', 'tok_mc_3344', 'San Pelayo 14', 6, '20800', '633112244', 'xalberdi@gmail.com', 'Euskara', 'xabi96zarautz', 1),
('Maialen', 'Lujanbio Galarraga', '44558899Y', '1976-11-20', 'emakumea', NULL, 'Kale Nagusia 1', 9, '20120', '644223355', 'maialen.lujan@bertso.eus', 'Euskara', 'maialen76', 1),
('Gaizka', 'Toquero Pinedo', '55669900Z', '1984-08-09', 'gizona', 'tok_visa_5566', 'Zaramaga kalea 10', 3, '01013', '655334466', 'gaizka.toquero@ath.eus', 'Gaztelania', 'gaizka24', 1),
('Edurne', 'Pasaban Lizarribar', '66770011A', '1973-08-01', 'emakumea', 'tok_mc_6677', 'Tolosa etorbidea 5', 1, '20018', '666445577', 'edurne.pasaban@mendia.eus', 'Euskara', 'edurne14x8000', 1),
('Ander', 'Iturraspe Derteano', '77881122B', '1989-03-08', 'gizona', NULL, 'Ibaizabal 4', 2, '48011', '677556688', 'ander.itu@gmail.com', 'Gaztelania', 'ander89bilbo', 1),
('Olatz', 'Salvador Elkano', '88992233C', '1990-10-10', 'emakumea', 'tok_visa_8899', 'Egia kalea 12', 1, '20012', '688667799', 'olatz.salvador@musika.eus', 'Euskara', 'olatz90pass', 1),
('Xabi', 'Prieto Argarate', '99003344D', '1983-08-29', 'gizona', 'tok_mc_9900', 'Anoeta pasealekua 1', 1, '20014', '699778800', 'xabi.prieto@reala.eus', 'Euskara', 'xabi10kap', 1),
('Itziar', 'Ituño Martinez', '11004455E', '1974-06-18', 'emakumea', NULL, 'Basauri kalea 5', 20, '48970', '611889901', 'itziar.ituno@gmail.com', 'Euskara', 'itziar74', 1);

-- 4. PRODUKTUAK ETA SUBKLASEAK (IRUDIEKIN EGUNERATUA)
INSERT INTO produktuak (
    id_produktua, izena, deskribapena, hornitzaile_id, biltegi_id, kategoria_id, 
    marka, mota, produktu_egoera, salmenta_prezioa, stock, salgai, irudia_url, zergak_ehunekoa
) VALUES
(1, 'MacBook Air 11" (2014)', 'Eramangarri ultra-trinkoa, eguneroko lanetarako oraindik balekoa.', 1, 1, 1, 'Apple', 'Eramangarria', 'Berritua B', 350.00, 5, TRUE, '../produktuen_irudiak/1_mac_book_air_2024.jpg', 21.00),
(2, 'Dell XPS 13 Plus', 'Pantaila ia ertz gabea, Windows 11rako optimizatua.', 1, 1, 1, 'Dell', 'Eramangarria', 'Berria', 1499.00, 10, TRUE, '../produktuen_irudiak/2_Lenovo_ThinkPad_X230.jpg', 21.00),
(3, 'Lenovo ThinkPad X1', 'Enpresentzako estandarra, karbono zuntzezko akaberarekin.', 2, 2, 1, 'Lenovo', 'Eramangarria', 'Berria', 1850.00, 20, TRUE, '../produktuen_irudiak/3_lenovo_thinkpad_x1.jpg', 21.00),
(4, 'HP Spectre x360', 'Bihurgarria, tablet moduan erabil daiteke.', 1, 1, 1, 'HP', 'Eramangarria', 'Berritua A', 950.00, 5, TRUE, '../produktuen_irudiak/4_hp_spectre_x360.jpg', 21.00),
(5, 'Asus ROG Zephyrus', 'Gaming eramangarria, RTX 4070 grafikoarekin.', 1, 2, 1, 'Asus', 'Eramangarria', 'Berria', 2300.00, 8, TRUE, '../produktuen_irudiak/5_Asus_ROG_Gaming_Beast.jpg', 21.00),
(6, 'Acer Swift 5', 'Oso arina, bidaiatzeko aproposa.', 2, 1, 1, 'Acer', 'Eramangarria', 'Berria', 899.00, 12, TRUE, '../produktuen_irudiak/6_acer_swift_5.jpg', 21.00),
(7, 'Microsoft Surface Laptop 5', 'Ukimen-pantaila eta akabera metalikoa.', 1, 1, 1, 'Microsoft', 'Eramangarria', 'Berritua B', 750.00, 3, TRUE, '../produktuen_irudiak/7_Acer_Aspire_One_Netbook.jpg', 21.00),
(8, 'Razer Blade 15', 'Diseinu beltza, RGB teklatua eta potentzia gorena.', 1, 2, 1, 'Razer', 'Eramangarria', 'Berria', 2800.00, 4, TRUE, '../produktuen_irudiak/8_razer_blade_15.jpg', 21.00),
(9, 'LG Gram 17', '17 hazbeteko pantaila baina pisu oso txikia.', 2, 1, 1, 'LG', 'Eramangarria', 'Berria', 1350.00, 7, TRUE, '../produktuen_irudiak/9_lg_gram_17.jpg', 21.00),
(10, 'MacBook Air M2', 'Isila, haizagailurik gabea eta bateria luzea.', 1, 1, 1, 'Apple', 'Eramangarria', 'Berria', 1299.00, 25, TRUE, '../produktuen_irudiak/10_macbook_air_m2.jpg', 21.00),
(11, 'HP Omen 45L', 'Gaming dorre aurreratua hozte sistema bereziarekin.', 1, 2, 1, 'HP', 'Mahai-gainekoa', 'Berria', 2499.00, 5, TRUE, '../produktuen_irudiak/11_hp_omen_45l.jpg', 21.00),
(12, 'Dell OptiPlex 7000', 'Bulegorako ordenagailu trinkoa eta fidagarria.', 2, 1, 1, 'Dell', 'Mahai-gainekoa', 'Berria', 650.00, 30, TRUE, '../produktuen_irudiak/12_dell_optiplex_7000.jpg', 21.00),
(13, 'Apple Mac Mini M2', 'Txikia baina matoia, mahaigain garbietarako.', 1, 1, 1, 'Apple', 'Mahai-gainekoa', 'Berria', 699.00, 15, TRUE, '../produktuen_irudiak/13_apple_mac_mini_m2.jpg', 21.00),
(14, 'Lenovo Legion Tower', 'RGB argiak eta RTX grafikoa jokoetarako.', 1, 2, 1, 'Lenovo', 'Mahai-gainekoa', 'Berria', 1200.00, 8, TRUE, '../produktuen_irudiak/14_Alienware_Aurora_R15.jpg', 21.00),
(15, 'Custom PC Creator', 'Eduki sortzaileentzako muntatutako ordenagailua.', 2, 1, 1, 'Custom', 'Mahai-gainekoa', 'Berritua A', 1100.00, 2, TRUE, '../produktuen_irudiak/15_PowerMac_G4_Retro.jpg', 21.00),
(16, 'Corsair One i300', 'Formatu oso txikia (Mini-ITX) baina oso potentea.', 1, 2, 1, 'Corsair', 'Mahai-gainekoa', 'Berria', 3500.00, 2, TRUE, '../produktuen_irudiak/16_corsair_one_i300.jpg', 21.00),
(17, 'Acer Predator Orion', 'Diseinu futurista eta aireztapen bikaina.', 1, 2, 1, 'Acer', 'Mahai-gainekoa', 'Berria', 1800.00, 4, TRUE, '../produktuen_irudiak/17_acer_predator_orion.jpg', 21.00),
(18, 'HP All-in-One 27', 'Ordenagailua eta pantaila dena batean.', 2, 1, 1, 'HP', 'Mahai-gainekoa', 'Berria', 950.00, 10, TRUE, '../produktuen_irudiak/18_iMac_G5_Styled_Desktop.jpg', 21.00),
(19, 'MSI Trident 3', 'Kontsola itxurako PCa, egongelarako.', 1, 2, 1, 'MSI', 'Mahai-gainekoa', 'Berritua B', 800.00, 3, TRUE, '../produktuen_irudiak/19_MSI_Infinite_X.jpg', 21.00),
(20, 'Apple Mac Studio', 'Profesionalentzako errendimendu maximoa.', 1, 1, 1, 'Apple', 'Mahai-gainekoa', 'Berria', 2399.00, 6, TRUE, '../produktuen_irudiak/20_apple_mac_studio.jpg', 21.00),
(21, 'iPhone 15 Pro', 'Titaniozko gorputza eta A17 Pro txipa.', 1, 1, 2, 'Apple', 'Mugikorra', 'Berria', 1209.00, 50, TRUE, '../produktuen_irudiak/21_iphone_15_pro.jpg', 21.00),
(22, 'Samsung Galaxy S24 Ultra', 'AI integratua eta S-Pen arkatza barne.', 2, 1, 2, 'Samsung', 'Mugikorra', 'Berria', 1459.00, 40, TRUE, '../produktuen_irudiak/22_samsung_galaxy_s24_ultra.jpg', 21.00),
(23, 'Nexus 4 Classic', 'Android telefono klasikoa, bildumazaleentzat edo oinarrizko erabilerarako.', 1, 2, 2, 'Google', 'Mugikorra', 'Berritua B', 80.00, 3, TRUE, '../produktuen_irudiak/1_Nexus_Phone_4.jpg', 21.00),
(24, 'Xiaomi 13T Pro', 'Leica kamerak eta karga ultra-azkarra.', 2, 2, 2, 'Xiaomi', 'Mugikorra', 'Berria', 799.00, 25, TRUE, '../produktuen_irudiak/24_xiaomi_13t_pro.jpg', 21.00),
(25, 'OnePlus 11', 'Errendimendu bikaina prezio lehiakorrean.', 2, 1, 2, 'OnePlus', 'Mugikorra', 'Berritua A', 550.00, 10, TRUE, '../produktuen_irudiak/25_oneplus_11.jpg', 21.00),
(26, 'Sony Xperia 1 V', 'Zinemako pantaila formatua (21:9).', 1, 2, 2, 'Sony', 'Mugikorra', 'Berria', 1100.00, 5, TRUE, '../produktuen_irudiak/26_sony_xperia_1_v.jpg', 21.00),
(27, 'Nothing Phone (2)', 'Atzealde gardena eta LED interfazea.', 2, 1, 2, 'Nothing', 'Mugikorra', 'Berria', 650.00, 15, TRUE, '../produktuen_irudiak/27_nothing_phone_2.jpg', 21.00),
(28, 'Samsung Galaxy Z Flip 5', 'Tolestagarria eta poltsikoan eramateko erosoa.', 2, 1, 2, 'Samsung', 'Mugikorra', 'Berria', 999.00, 12, TRUE, '../produktuen_irudiak/28_Galaxy_Z_Fold_5.jpg', 21.00),
(29, 'iPhone 13 Mini', 'Tamaina txikia, esku bakarrarekin erabiltzeko.', 1, 1, 2, 'Apple', 'Mugikorra', 'Berritua B', 450.00, 8, TRUE, '../produktuen_irudiak/29_iphone_13_mini.jpg', 21.00),
(30, 'Motorola Edge 40', 'Diseinu mehea eta pantaila kurbatua.', 2, 2, 2, 'Motorola', 'Mugikorra', 'Berria', 499.00, 20, TRUE, '../produktuen_irudiak/30_motorola_edge_40.jpg', 21.00),
(31, 'Asus Zenfone 10', 'Trinkoa baina oso indartsua.', 1, 1, 2, 'Asus', 'Mugikorra', 'Berria', 750.00, 8, TRUE, '../produktuen_irudiak/31_asus_zenfone_10.jpg', 21.00),
(32, 'Realme GT 3', 'Munduko kargatze azkarrena (240W).', 2, 2, 2, 'Realme', 'Mugikorra', 'Berria', 600.00, 10, TRUE, '../produktuen_irudiak/32_realme_gt_3.jpg', 21.00),
(33, 'iPhone SE (2022)', 'Klasikoa TouchID botoiarekin.', 1, 1, 2, 'Apple', 'Mugikorra', 'Berria', 429.00, 30, TRUE, '../produktuen_irudiak/33_iphone_se_2022.jpg', 21.00),
(34, 'Honor Magic 5 Pro', 'Kamera sistema oso aurreratua.', 2, 2, 2, 'Honor', 'Mugikorra', 'Berria', 900.00, 7, TRUE, '../produktuen_irudiak/34_Huawei_Mate_60_Pro.jpg', 21.00),
(35, 'Oppo Find X5', 'Diseinu futurista eta material premiumak.', 2, 1, 2, 'Oppo', 'Mugikorra', 'Berritua A', 400.00, 5, TRUE, '../produktuen_irudiak/35_oppo_find_x5.jpg', 21.00),
(36, 'iPad Pro 12.9', 'M2 txipa eta Liquid Retina XDR pantaila.', 1, 1, 2, 'Apple', 'Tableta', 'Berria', 1449.00, 10, TRUE, '../produktuen_irudiak/36_ipad_pro_129.jpg', 21.00),
(37, 'Samsung Galaxy Tab S9', 'Urarekiko erresistentzia eta pantaila bikaina.', 2, 1, 2, 'Samsung', 'Tableta', 'Berria', 899.00, 15, TRUE, '../produktuen_irudiak/37_samsung_galaxy_tab_s9.jpg', 21.00),
(38, 'iPad Air 5', 'M1 txipa, oreka perfektua potentzia eta prezioan.', 1, 1, 2, 'Apple', 'Tableta', 'Berria', 769.00, 20, TRUE, '../produktuen_irudiak/38_ipad_air_5.jpg', 21.00),
(39, 'Microsoft Surface Pro 9', 'Ordenagailua eta tableta gailu berean.', 1, 2, 2, 'Microsoft', 'Tableta', 'Berria', 1100.00, 8, TRUE, '../produktuen_irudiak/39_Surface_Pro_X.jpg', 21.00),
(40, 'Lenovo Tab P11 Pro', 'Multimedia kontsumorako aproposa.', 2, 2, 2, 'Lenovo', 'Tableta', 'Berritua A', 350.00, 12, TRUE, '../produktuen_irudiak/40_lenovo_tab_p11_pro.jpg', 21.00),
(41, 'Xiaomi Pad 6', 'Kalitate/prezio erlazio ezin hobea.', 2, 2, 2, 'Xiaomi', 'Tableta', 'Berria', 399.00, 25, TRUE, '../produktuen_irudiak/41_xiaomi_pad_6.jpg', 21.00),
(42, 'iPad Mini 6', 'Oso txikia, oharrak hartzeko perfektua.', 1, 1, 2, 'Apple', 'Tableta', 'Berria', 649.00, 10, TRUE, '../produktuen_irudiak/42_ipad_mini_6.jpg', 21.00),
(43, 'Amazon Fire Max 11', 'Oinarrizko erabilerarako eta irakurketarako.', 2, 1, 2, 'Amazon', 'Tableta', 'Berria', 249.00, 30, TRUE, '../produktuen_irudiak/43_iPad_Mini_1st_Gen.jpg', 21.00),
(44, 'Huawei MatePad Pro', 'HarmonyOS sistema eragilearekin.', 2, 2, 2, 'Huawei', 'Tableta', 'Zehazteko', 500.00, 5, FALSE, '../produktuen_irudiak/44_Galaxy_Tab_101_Old.jpg', 21.00),
(45, 'Google Pixel Tablet', 'Base bozgorailuarekin dator.', 1, 1, 2, 'Google', 'Tableta', 'Berria', 679.00, 7, TRUE, '../produktuen_irudiak/45_Nexus_7_Tablet.jpg', 21.00),
(46, 'Dell PowerEdge R750', 'Rack zerbitzaria datu zentroetarako.', 1, 2, 6, 'Dell', 'Zerbitzaria', 'Berria', 4500.00, 2, TRUE, '../produktuen_irudiak/46_dell_poweredge_r750.jpg', 21.00),
(47, 'HPE ProLiant DL380', 'Munduko zerbitzaririk salduena, fidagarria.', 1, 2, 6, 'HPE', 'Zerbitzaria', 'Berria', 3800.00, 3, TRUE, '../produktuen_irudiak/47_hpe_proliant_dl380.jpg', 21.00),
(48, 'Lenovo ThinkSystem SR650', 'Eskalagarria eta errendimendu altukoa.', 2, 2, 6, 'Lenovo', 'Zerbitzaria', 'Berritua A', 2200.00, 1, TRUE, '../produktuen_irudiak/48_lenovo_thinksystem_sr650.jpg', 21.00),
(49, 'Synology RackStation', 'Biltegiratze masiborako NAS zerbitzaria.', 2, 1, 6, 'Synology', 'Zerbitzaria', 'Berria', 1500.00, 5, TRUE, '../produktuen_irudiak/49_Synology_NAS_Pro.jpg', 21.00),
(50, 'Cisco UCS C220', 'Birtualizaziorako optimizatua.', 1, 2, 6, 'Cisco', 'Zerbitzaria', 'Berritua B', 1200.00, 2, TRUE, '../produktuen_irudiak/50_cisco_ucs_c220.jpg', 21.00),
(51, 'LG UltraGear 27', 'Gaming pantaila azkarra 144Hz-ekin.', 2, 1, 3, 'LG', 'Pantaila', 'Berria', 299.00, 20, TRUE, '../produktuen_irudiak/51_lg_ultragear_27.jpg', 21.00),
(52, 'Dell UltraSharp 32', '4K bereizmena diseinu grafikorako.', 1, 1, 3, 'Dell', 'Pantaila', 'Berria', 850.00, 5, TRUE, '../produktuen_irudiak/52_dell_ultrasharp_32.jpg', 21.00),
(53, 'Samsung Odyssey G9', 'Pantaila ultra-zovala eta kurbatua.', 2, 2, 3, 'Samsung', 'Pantaila', 'Berria', 1200.00, 3, TRUE, '../produktuen_irudiak/53_samsung_odyssey_g9.jpg', 21.00),
(54, 'BenQ PD2700U', 'Kolore zehatzak (sRGB 100%).', 2, 1, 3, 'BenQ', 'Pantaila', 'Berritua A', 350.00, 8, TRUE, '../produktuen_irudiak/54_benq_pd2700u.jpg', 21.00),
(55, 'Asus ProArt', 'Argazkilari eta bideo editoreentzako.', 1, 1, 3, 'Asus', 'Pantaila', 'Berria', 500.00, 6, TRUE, '../produktuen_irudiak/55_Samsung_Odyssey_OLED_G8.jpg', 21.00),
(56, 'HP 24mh', 'Bulegorako oinarrizko pantaila ergonomikoa.', 2, 1, 3, 'HP', 'Pantaila', 'Berria', 149.00, 40, TRUE, '../produktuen_irudiak/56_hp_24mh.jpg', 21.00),
(57, 'MSI Optix MAG', 'Pantaila kurbatua murgiltze esperientziarako.', 1, 2, 3, 'MSI', 'Pantaila', 'Berria', 220.00, 15, TRUE, '../produktuen_irudiak/57_msi_optix_mag.jpg', 21.00),
(58, 'Apple Studio Display', '5K bereizmena eta eraikuntza bikaina.', 1, 1, 3, 'Apple', 'Pantaila', 'Berria', 1779.00, 4, TRUE, '../produktuen_irudiak/58_apple_studio_display.jpg', 21.00),
(59, 'Philips Brilliance', 'Ultrawide formatua produktibitaterako.', 2, 1, 3, 'Philips', 'Pantaila', 'Berritua B', 400.00, 5, TRUE, '../produktuen_irudiak/59_philips_brilliance.jpg', 21.00),
(60, 'AOC 24G2', 'Prezio-kalitate erlazio onena jokoetarako.', 2, 2, 3, 'AOC', 'Pantaila', 'Berria', 179.00, 25, TRUE, '../produktuen_irudiak/60_aoc_24g2.jpg', 21.00),
(61, 'ViewSonic Elite', 'Kolore biziak eta erantzun azkarra.', 1, 2, 3, 'ViewSonic', 'Pantaila', 'Berria', 550.00, 7, TRUE, '../produktuen_irudiak/61_BenQ_DesignVue_4K.jpg', 21.00),
(62, 'Lenovo ThinkVision', 'USB-C konexioa duen dock integratua.', 2, 1, 3, 'Lenovo', 'Pantaila', 'Berria', 320.00, 12, TRUE, '../produktuen_irudiak/62_lenovo_thinkvision.jpg', 21.00),
(63, 'Gigabyte M27Q', 'KVM switch integratua.', 1, 2, 3, 'Gigabyte', 'Pantaila', 'Berria', 340.00, 9, TRUE, '../produktuen_irudiak/63_gigabyte_m27q.jpg', 21.00),
(64, 'Eizo ColorEdge', 'Profesionalentzako kalibrazio zehatza.', 2, 1, 3, 'Eizo', 'Pantaila', 'Zehazteko', 1200.00, 2, FALSE, '../produktuen_irudiak/64_eizo_coloredge.jpg', 21.00),
(65, 'Huawei MateView', 'Formatu karratuagoa (3:2) kodetzeko.', 2, 2, 3, 'Huawei', 'Pantaila', 'Berria', 599.00, 6, TRUE, '../produktuen_irudiak/65_huawei_mateview.jpg', 21.00),
(66, 'Windows 7 Professional', 'Egonkortasunagatik ezaguna den sistema zaharra.', 3, 1, 5, 'Microsoft', 'Softwarea', 'Berritua B', 25.00, 10, TRUE, '../produktuen_irudiak/66_Windows_7_profesional.jpg', 21.00),
(67, 'Office 2010 Student', 'Bertsio klasikoa, harpidetzarik gabea.', 3, 1, 5, 'Microsoft', 'Softwarea', 'Berritua B', 45.00, 15, TRUE, '../produktuen_irudiak/67_office_2010_student.jpg', 21.00),
(68, 'Adobe Photoshop CC', '1 urteko harpidetza diseinatzaileentzat.', 3, 2, 5, 'Adobe', 'Softwarea', 'Berria', 290.00, 100, TRUE, '../produktuen_irudiak/68_VMware_Workstation_Pro.jpg', 21.00),
(69, 'Norton Antivirus 2012', 'Segurtasun pakete klasikoa sistema zaharrentzat.', 3, 1, 5, 'Kaspersky', 'Softwarea', 'Berritua B', 15.00, 20, TRUE, '../produktuen_irudiak/69_norton_antibirus.jpg', 21.00),
(70, 'Windows Server 2022', 'Zerbitzarietarako sistema estandarra.', 3, 2, 5, 'Microsoft', 'Softwarea', 'Berria', 800.00, 20, TRUE, '../produktuen_irudiak/70_windows_server_2022.jpg', 21.00),
(71, 'NordVPN 1 Year', 'Nabigazio segurua eta pribatua.', 3, 1, 5, 'NordSec', 'Softwarea', 'Berria', 59.00, 150, TRUE, '../produktuen_irudiak/71_Cisco_AnyConnect_30.jpg', 21.00),
(72, 'AutoCAD 2007 Classic', 'Diseinu industrialerako bertsio historikoa.', 3, 2, 5, 'Autodesk', 'Softwarea', 'Berritua B', 350.00, 2, TRUE, '../produktuen_irudiak/72_autocad_2007.jpg', 21.00),
(73, 'Norton 360 Deluxe', 'Segurtasuna eta VPN integratua.', 3, 1, 5, 'Norton', 'Softwarea', 'Berria', 39.99, 80, TRUE, '../produktuen_irudiak/73_norton_360_deluxe.jpg', 21.00),
(74, 'Red Hat Enterprise Linux', 'Enpresen zerbitzarietarako Linux.', 3, 2, 5, 'Red Hat', 'Softwarea', 'Berria', 349.00, 10, TRUE, '../produktuen_irudiak/74_red_hat_enterprise_linux.jpg', 21.00),
(75, 'VMware vSphere', 'Birtualizazio plataforma profesionala.', 3, 2, 5, 'VMware', 'Softwarea', 'Berria', 500.00, 8, TRUE, '../produktuen_irudiak/75_Docker_Desktop_Pro.jpg', 21.00),
(76, 'Ubuntu Desktop 24.04', 'Linux banaketa ezagunena eta erabilerraza.', 3, 1, 5, 'Canonical', 'Softwarea', 'Berria', 0.00, 999, TRUE, '../produktuen_irudiak/76_ubuntu_desktop_2404.jpg', 21.00),
(77, 'LibreOffice', 'Ofimatika suite osoa eta kode irekikoa.', 3, 1, 5, 'The Document Foundation', 'Softwarea', 'Berria', 0.00, 999, TRUE, '../produktuen_irudiak/77_libreoffice.jpg', 21.00),
(78, 'Blender 4.0', '3D sorkuntza eta animaziorako suitea.', 3, 2, 5, 'Blender Foundation', 'Softwarea', 'Berria', 0.00, 999, TRUE, '../produktuen_irudiak/78_blender_40.jpg', 21.00),
(79, 'GIMP', 'Irudiak editatzeko tresna profesionala.', 3, 1, 5, 'GIMP Team', 'Softwarea', 'Berria', 0.00, 999, TRUE, '../produktuen_irudiak/79_Pro_Tools_Audio_Studio.jpg', 21.00),
(80, 'VLC Media Player', 'Formatua ia guztiak irakurtzen dituen erreproduzigailua.', 3, 1, 5, 'VideoLAN', 'Softwarea', 'Berria', 0.00, 999, TRUE, '../produktuen_irudiak/80_vlc_media_player.jpg', 21.00);

INSERT INTO eramangarriak (id_produktua, prozesadorea, ram_gb, diskoa_gb, pantaila_tamaina, bateria_wh, sistema_eragilea, pisua_kg) VALUES
(1, 'Apple M3 Pro', 18, 512, 14.2, 70, 'macOS', 1.6),
(2, 'Intel Core i7-1360P', 16, 1024, 13.4, 55, 'Windows 11', 1.23),
(3, 'Intel Core i7-1260P', 32, 1024, 14.0, 57, 'Windows 11 Pro', 1.12),
(4, 'Intel Core i5-1135G7', 8, 256, 13.5, 60, 'Windows 10', 1.3),
(5, 'AMD Ryzen 9 7940HS', 32, 2048, 14.0, 76, 'Windows 11', 1.7),
(6, 'Intel Core i5-1240P', 16, 512, 14.0, 56, 'Windows 11', 1.2),
(7, 'Intel Core i5-1235U', 8, 256, 13.5, 47, 'Windows 11', 1.27),
(8, 'Intel Core i9-13800H', 32, 1024, 15.6, 80, 'Windows 11', 2.01),
(9, 'Intel Core i7-1360P', 16, 1024, 17.0, 80, 'Windows 11', 1.35),
(10, 'Apple M2', 8, 256, 13.6, 52, 'macOS', 1.24);

INSERT INTO mahai_gainekoak (id_produktua, prozesadorea, plaka_basea, ram_gb, diskoa_gb, txartel_grafikoa, elikatze_iturria_w, kaxa_formatua) VALUES
(11, 'Intel Core i9-13900K', 'Z790', 64, 2048, 'RTX 4090', 1200, 'ATX'),
(12, 'Intel Core i5-12500', 'B660', 16, 512, 'Intel UHD 770', 300, 'Micro-ATX'),
(13, 'Apple M2 Chip', 'Integrated', 8, 256, 'Apple GPU 10-core', 150, 'Mini-ITX'),
(14, 'AMD Ryzen 7 5800X', 'B550', 32, 1024, 'RTX 3070', 750, 'ATX'),
(15, 'AMD Ryzen 9 5900X', 'X570', 64, 2048, 'RX 6800 XT', 850, 'ATX'),
(16, 'Intel Core i9-12900K', 'Z690I', 32, 2048, 'RTX 3080 Ti', 750, 'Mini-ITX'),
(17, 'Intel Core i7-12700K', 'Z690', 32, 1024, 'RTX 3080', 800, 'ATX'),
(18, 'Intel Core i7-12700T', 'Custom HP', 16, 512, 'Intel Iris Xe', 180, 'Mini-ITX'),
(19, 'Intel Core i5-11400F', 'H510', 16, 512, 'RTX 3060 Aero', 450, 'Mini-ITX'),
(20, 'Apple M1 Max', 'Integrated', 32, 512, 'Apple GPU 24-core', 370, 'Mini-ITX');

INSERT INTO mugikorrak (id_produktua, pantaila_teknologia, pantaila_hazbeteak, biltegiratzea_gb, ram_gb, kamera_nagusa_mp, bateria_mah, sistema_eragilea, sareak) VALUES
(21, 'OLED', 6.1, 128, 8, 48, 3274, 'iOS', '5G'),
(22, 'AMOLED', 6.8, 256, 12, 200, 5000, 'Android', '5G'),
(23, 'OLED', 6.7, 128, 12, 50, 5050, 'Android', '5G'),
(24, 'AMOLED', 6.6, 256, 12, 50, 5000, 'Android', '5G'),
(25, 'AMOLED', 6.7, 128, 8, 50, 5000, 'Android', '5G'),
(26, 'OLED', 6.5, 256, 12, 48, 5000, 'Android', '5G'),
(27, 'OLED', 6.7, 256, 12, 50, 4700, 'Android', '5G'),
(28, 'AMOLED', 6.7, 256, 8, 12, 3700, 'Android', '5G'),
(29, 'OLED', 5.4, 64, 4, 12, 2438, 'iOS', '5G'),
(30, 'OLED', 6.5, 256, 8, 50, 4400, 'Android', '5G'),
(31, 'AMOLED', 5.9, 128, 8, 50, 4300, 'Android', '5G'),
(32, 'AMOLED', 6.7, 256, 16, 50, 4600, 'Android', '5G'),
(33, 'LCD', 4.7, 64, 4, 12, 2018, 'iOS', '5G'),
(34, 'OLED', 6.8, 512, 12, 50, 5100, 'Android', '5G'),
(35, 'AMOLED', 6.5, 256, 8, 50, 4800, 'Android', '5G');

INSERT INTO tabletak (id_produktua, pantaila_hazbeteak, biltegiratzea_gb, konektibitatea, sistema_eragilea, bateria_mah, arkatzarekin_bateragarria) VALUES
(36, 12.9, 256, 'WiFi + Cellular', 'iPadOS', 10758, TRUE),
(37, 11.0, 128, 'WiFi', 'Android', 8400, TRUE),
(38, 10.9, 64, 'WiFi', 'iPadOS', 7600, TRUE),
(39, 13.0, 256, 'WiFi', 'Windows 11', 6500, TRUE),
(40, 11.2, 128, 'WiFi', 'Android', 8000, TRUE),
(41, 11.0, 128, 'WiFi', 'Android', 8840, TRUE),
(42, 8.3, 64, 'WiFi + Cellular', 'iPadOS', 5124, TRUE),
(43, 11.0, 64, 'WiFi', 'FireOS', 7500, FALSE),
(44, 12.6, 256, 'WiFi', 'HarmonyOS', 10050, TRUE),
(45, 10.9, 128, 'WiFi', 'Android', 7020, TRUE);

INSERT INTO zerbitzariak (id_produktua, prozesadore_nukleoak, ram_mota, disko_badiak, rack_unitateak, elikatze_iturri_erredundantea, raid_kontroladora) VALUES
(46, 24, 'DDR5', 12, 2, TRUE, 'PERC H750'),
(47, 20, 'DDR4', 8, 2, TRUE, 'HPE Smart Array'),
(48, 16, 'DDR4', 16, 2, TRUE, 'ThinkSystem RAID'),
(49, 4, 'DDR4', 12, 2, TRUE, 'Software RAID'),
(50, 12, 'DDR4', 8, 1, TRUE, 'Cisco 12G SAS');

INSERT INTO pantailak (id_produktua, hazbeteak, bereizmena, panel_mota, freskatze_tasa_hz, konexioak, kurbatura) VALUES
(51, 27.0, '2560x1440', 'IPS', 144, 'HDMI, DP', 'Flat'),
(52, 32.0, '3840x2160', 'IPS', 60, 'HDMI, DP, USB-C', 'Flat'),
(53, 49.0, '5120x1440', 'VA', 240, 'HDMI, DP', '1000R'),
(54, 27.0, '3840x2160', 'IPS', 60, 'HDMI, DP', 'Flat'),
(55, 27.0, '2560x1440', 'IPS', 75, 'HDMI, DP, USB-C', 'Flat'),
(56, 23.8, '1920x1080', 'IPS', 75, 'HDMI, VGA', 'Flat'),
(57, 24.0, '1920x1080', 'VA', 165, 'HDMI, DP', '1500R'),
(58, 27.0, '5120x2880', 'IPS', 60, 'Thunderbolt 3', 'Flat'),
(59, 34.0, '3440x1440', 'VA', 100, 'HDMI, DP', '1500R'),
(60, 24.0, '1920x1080', 'IPS', 144, 'HDMI, DP', 'Flat'),
(61, 32.0, '2560x1440', 'IPS', 175, 'HDMI, DP', 'Flat'),
(62, 27.0, '2560x1440', 'IPS', 60, 'USB-C, HDMI', 'Flat'),
(63, 27.0, '2560x1440', 'IPS', 170, 'HDMI, DP, USB-C', 'Flat'),
(64, 27.0, '2560x1440', 'IPS', 60, 'HDMI, DP, DVI', 'Flat'),
(65, 28.2, '3840x2560', 'IPS', 60, 'MiniDP, HDMI', 'Flat');

INSERT INTO softwareak (id_produktua, software_mota, lizentzia_mota, bertsioa, garatzailea, librea) VALUES
(66, 'Sistema Eragilea', 'Retail', '11 Pro', 'Microsoft', FALSE),
(67, 'Ofimatika', 'Retail', '2021', 'Microsoft', FALSE),
(68, 'Bestelakoak', 'Harpidetza', 'CC 2024', 'Adobe', FALSE),
(69, 'Antibirusa', 'Retail', '2024', 'Kaspersky', FALSE),
(70, 'Sistema Eragilea', 'OEM', 'Standard 2022', 'Microsoft', FALSE),
(71, 'Bestelakoak', 'Harpidetza', '1 Urte', 'NordSec', FALSE),
(72, 'Bestelakoak', 'Harpidetza', '2024', 'Autodesk', FALSE),
(73, 'Antibirusa', 'Retail', 'Deluxe', 'Norton', FALSE),
(74, 'Sistema Eragilea', 'Harpidetza', 'RHEL 9', 'Red Hat', FALSE),
(75, 'Bestelakoak', 'Retail', 'Standard 8', 'VMware', FALSE),
(76, 'Sistema Eragilea', 'GPL', '24.04 LTS', 'Canonical', TRUE),
(77, 'Ofimatika', 'LGPL', '7.6', 'Document Foundation', TRUE),
(78, 'Bestelakoak', 'GPL', '4.0', 'Blender Foundation', TRUE),
(79, 'Bestelakoak', 'GPL', '2.10', 'GIMP Team', TRUE),
(80, 'Bestelakoak', 'GPL', '3.0', 'VideoLAN', TRUE);

-- 5. AKATSAK ETA KONPONKETAK
INSERT INTO akatsak (id_akatsa, izena, deskribapena) VALUES
(1, 'Pantaila hautsita', 'Kristala edo panela puskatuta dago.'),
(2, 'Bateria ez da kargatzen', 'Konektatzean ez du kargarik hartzen.'),
(3, 'Disko gogorraren errorea', 'Sistemak ez du diskoa antzematen edo zarata egiten du.'),
(4, 'Sistema eragilea ez da abiarazten', 'Blue Screen edo errorea abiaraztean.'),
(5, 'Teklatua ez dabil', 'Tekla batzuk edo denak ez dute erantzuten.'),
(6, 'Uraren kaltea', 'Likidoa isuri zaio gainean.'),
(7, 'Haizagailuaren zarata', 'Zarata handia edo ez dabil.'),
(8, 'Berotze arazoak', 'Ordenagailua itzali egiten da berotasunagatik.'),
(9, 'WiFi konexio arazoak', 'Ez du sarerik harrapatzen.'),
(10, 'Bluetooth errorea', 'Ez du gailurik aurkitzen.'),
(11, 'Kamera ez dabil', 'Webkamera beltza edo errorea.'),
(12, 'Audioa ez da entzuten', 'Ez dago soinurik bozgorailuetatik.'),
(13, 'Mikrofonoa ez dabil', 'Ez da soinurik grabatzen.'),
(14, 'USB portuak hondatuta', 'Ez dute gailurik antzematen.'),
(15, 'Kargagailuaren konektorea', 'Holgura du edo puskatuta dago.'),
(16, 'Birus edo Malwarea', 'Ordenagailua motel edo publizitatea.'),
(17, 'Datuak berreskuratu', 'Ezabatu edo galdu diren datuak.'),
(18, 'Pasahitza ahaztuta', 'Ezin da sisteman sartu.'),
(19, 'Pantaila keinuka', 'Irudia joan-etorrian dabil.'),
(20, 'Bateriaren iraupen motza', 'Oso azkar agortzen da.'),
(21, 'Trackpad ez dabil', 'Sagua ez da mugitzen.'),
(22, 'Bisagra hautsita', 'Pantaila ez da ondo ixten/irekitzen.'),
(23, 'Karkasa hondatuta', 'Kolpea edo pitzadura plastikoan.'),
(24, 'BIOS blokeatuta', 'Ezin da BIOSera sartu.'),
(25, 'RAM memoriaren errorea', 'Pitidoak abiaraztean.'),
(26, 'Txartel grafikoaren errorea', 'Artefaktuak pantailan.'),
(27, 'Sare txartela hondatuta', 'Ethernet ez dabil.'),
(28, 'Botoiak ez dabiltza', 'Pizteko edo bolumen botoiak.'),
(29, 'Sentsoreak ez dabiltza', 'Giro, hurbiltasun sentsoreak...'),
(30, 'Mantentze orokorra', 'Garbiketa eta pasta termikoa aldatzea.');

INSERT INTO konponketak (produktua_id, langilea_id, hasiera_data, konponketa_egoera, akatsa_id, oharrak) VALUES
(1, 8, '2023-01-10 09:00:00', 'Konponduta', 1, 'Pantaila berria jarri da.'),
(5, 9, '2023-01-12 10:30:00', 'Prozesuan', 8, 'Pasta termikoa aldatu behar da.'),
(12, 10, '2023-01-15 11:00:00', 'Prozesuan', 4, 'Diskoa formatu behar da.'),
(22, 11, '2023-02-01 16:00:00', 'Konponduta', 2, 'Bateria aldatu da.'),
(30, 8, '2023-02-05 09:15:00', 'Konponezina', 6, 'Plaka basea herdoilduta.'),
(45, 9, '2023-02-10 12:00:00', 'Prozesuan', 15, 'Soldadura berria behar du.'),
(55, 10, '2023-02-20 09:30:00', 'Konponduta', 11, 'Driver-ak eguneratu dira.'),
(3, 11, '2023-03-01 10:00:00', 'Konponduta', 30, 'Haizagailuak garbitu dira.'),
(8, 8, '2023-03-05 11:45:00', 'Prozesuan', 5, 'Teklatu osoa eskatu dugu.'),
(15, 9, '2023-03-10 15:30:00', 'Konponduta', 16, 'Malwarebytes pasata.'),
(25, 10, '2023-03-15 17:00:00', 'Prozesuan', 1, 'Aurrekontua bidali zaio bezeroari.'),
(33, 11, '2023-04-01 09:00:00', 'Konponduta', 20, 'Bateria originala jarri da.'),
(40, 8, '2023-04-05 10:30:00', 'Prozesuan', 7, 'Haizagailu berria bidean.'),
(60, 9, '2023-04-10 11:00:00', 'Konponduta', 19, 'Kable flexa ondo konektatu da.'),
(70, 10, '2023-04-15 14:00:00', 'Konponezina', 26, 'Grafikoa integratuta dago, plaka aldatu behar.'),
(2, 11, '2023-05-01 16:30:00', 'Konponduta', 17, 'Recuva erabili dugu datuak ateratzeko.'),
(6, 8, '2023-05-05 09:00:00', 'Prozesuan', 3, 'SSD berria probatu behar da.'),
(9, 9, '2023-05-10 10:00:00', 'Prozesuan', 4, 'Windows berrinstalatzen.'),
(18, 10, '2023-05-15 11:30:00', 'Konponduta', 14, 'USB plaka aldatu da.'),
(28, 11, '2023-06-01 15:00:00', 'Konponduta', 22, 'Bisagra estutu da.'),
(35, 8, '2023-06-05 16:00:00', 'Prozesuan', 23, 'Karkasa berria bilatzen.'),
(42, 9, '2023-06-10 09:30:00', 'Konponduta', 28, 'Botoiaren flexa aldatu da.'),
(50, 10, '2023-06-15 10:45:00', 'Konponduta', 12, 'Audio driverrak.'),
(65, 11, '2023-07-01 12:00:00', 'Prozesuan', 9, 'WiFi txartela aldatzen.'),
(75, 8, '2023-07-05 14:30:00', 'Konponduta', 29, 'Sentsorea kalibratu da.'),
(10, 9, '2023-07-10 16:00:00', 'Konponduta', 2, 'Kargagailua zen arazoa, berria saldu zaio.'),
(20, 10, '2023-07-15 09:00:00', 'Prozesuan', 1, 'Pantaila ez dago stock-ean.'),
(38, 11, '2023-08-01 11:00:00', 'Konponduta', 13, 'Mikrofonoaren iragazkia garbitu.'),
(48, 8, '2023-08-05 13:00:00', 'Konponduta', 25, 'RAM moduluak trukatu dira.'),
(58, 9, '2023-08-10 15:15:00', 'Prozesuan', 18, 'Pasahitza reset egiten.');

-- 6. FITXAKETAK, ETA SARRERAK
INSERT INTO fitxaketak (id_fitxaketa, langilea_id, data, ordua, mota) VALUES
(1, 1, '2024-01-08', '08:00:00', 'Sarrera'), (2, 1, '2024-01-08', '16:00:00', 'Irteera'),
(3, 2, '2024-01-08', '08:05:00', 'Sarrera'), (4, 2, '2024-01-08', '15:00:00', 'Irteera'),
(5, 3, '2024-01-08', '09:00:00', 'Sarrera'), (6, 3, '2024-01-08', '17:00:00', 'Irteera'),
(7, 4, '2024-01-08', '09:10:00', 'Sarrera'), (8, 4, '2024-01-08', '13:00:00', 'Irteera'),
(9, 5, '2024-01-08', '08:55:00', 'Sarrera'), (10, 5, '2024-01-08', '18:00:00', 'Irteera'),
(11, 6, '2024-01-08', '09:00:00', 'Sarrera'), (12, 6, '2024-01-08', '18:00:00', 'Irteera'),
(13, 7, '2024-01-08', '09:05:00', 'Sarrera'), (14, 7, '2024-01-08', '17:30:00', 'Irteera'),
(15, 8, '2024-01-08', '08:00:00', 'Sarrera'), (16, 8, '2024-01-08', '14:00:00', 'Irteera'),
(17, 9, '2024-01-08', '14:00:00', 'Sarrera'), (18, 9, '2024-01-08', '20:00:00', 'Irteera'),
(19, 10,'2024-01-08', '08:00:00', 'Sarrera'), (20, 10, '2024-01-08', '16:00:00', 'Irteera'),
(21, 11,'2024-01-08', '10:00:00', 'Sarrera'), (22, 11, '2024-01-08', '19:00:00', 'Irteera'),
(23, 12,'2024-01-08', '07:00:00', 'Sarrera'), (24, 12, '2024-01-08', '15:00:00', 'Irteera'),
(25, 13,'2024-01-08', '07:30:00', 'Sarrera'), (26, 13, '2024-01-08', '15:30:00', 'Irteera'),
(27, 14,'2024-01-08', '08:00:00', 'Sarrera'), (28, 14, '2024-01-08', '16:00:00', 'Irteera'),
(29, 1, '2024-01-09', '08:00:00', 'Sarrera'), (30, 1, '2024-01-09', '16:00:00', 'Irteera'),
(31, 5, '2024-01-09', '08:50:00', 'Sarrera'), (32, 5, '2024-01-09', '18:10:00', 'Irteera'),
(33, 8, '2024-01-09', '08:02:00', 'Sarrera'), (34, 8, '2024-01-09', '14:05:00', 'Irteera'),
(35, 12,'2024-01-09', '07:05:00', 'Sarrera'), (36, 12, '2024-01-09', '15:00:00', 'Irteera');

INSERT INTO sarrerak (id_sarrera, hornitzailea_id, langilea_id, sarrera_egoera, data) VALUES
(1, 1, 12, 'Jasota', '2023-11-01 09:00:00'),
(2, 2, 13, 'Jasota', '2023-11-02 10:00:00'),
(3, 3, 14, 'Bidean', '2023-11-03 11:30:00'),
(4, 1, 12, 'Jasota', '2023-11-05 08:15:00'),
(5, 2, 12, 'Jasota', '2023-11-10 14:00:00'),
(6, 1, 13, 'Ezabatua', '2023-11-12 16:30:00'),
(7, 3, 14, 'Jasota', '2023-11-15 09:45:00'),
(8, 2, 13, 'Bidean', '2023-11-18 10:00:00'),
(9, 1, 12, 'Jasota', '2023-11-20 11:00:00'),
(10, 3, 14, 'Jasota', '2023-11-25 15:30:00'),
(11, 2, 12, 'Jasota', '2023-12-01 09:10:00'),
(12, 1, 13, 'Jasota', '2023-12-03 10:20:00'),
(13, 1, 12, 'Bidean', '2023-12-05 12:00:00'),
(14, 2, 14, 'Jasota', '2023-12-08 14:45:00'),
(15, 3, 13, 'Jasota', '2023-12-10 09:30:00'),
(16, 1, 12, 'Jasota', '2023-12-12 16:00:00'),
(17, 2, 14, 'Jasota', '2023-12-15 11:15:00'),
(18, 3, 12, 'Bidean', '2023-12-18 08:30:00'),
(19, 1, 13, 'Ezabatua', '2023-12-20 13:45:00'),
(20, 2, 14, 'Jasota', '2023-12-22 10:00:00'),
(21, 3, 12, 'Jasota', '2023-12-24 09:00:00'),
(22, 1, 12, 'Jasota', '2023-12-28 15:00:00'),
(23, 1, 14, 'Jasota', '2024-01-03 11:30:00'),
(24, 2, 13, 'Bidean', '2024-01-05 14:00:00'),
(25, 3, 12, 'Jasota', '2024-01-08 09:15:00'),
(26, 1, 14, 'Jasota', '2024-01-10 10:45:00'),
(27, 2, 12, 'Jasota', '2024-01-12 16:00:00'),
(28, 3, 13, 'Jasota', '2024-01-15 08:30:00'),
(29, 1, 12, 'Bidean', '2024-01-18 13:20:00'),
(30, 2, 14, 'Jasota', '2024-01-20 11:00:00');

INSERT INTO sarrera_lerroak (id_sarrera_lerroa, sarrera_id, produktua_id, kantitatea, sarrera_lerro_egoera) VALUES
(1, 1, 1, 10, 'Jasota'),
(2, 1, 2, 5, 'Jasota'),
(3, 2, 5, 20, 'Jasota'),
(4, 2, 10, 10, 'Jasota'),
(5, 3, 15, 5, 'Bidean'),
(6, 4, 20, 15, 'Jasota'),
(7, 5, 25, 8, 'Jasota'),
(8, 6, 30, 5, 'Ezabatua'),
(9, 7, 35, 12, 'Jasota'),
(10, 7, 40, 4, 'Jasota'),
(11, 8, 45, 10, 'Bidean'),
(12, 9, 50, 2, 'Jasota'),
(13, 10, 55, 6, 'Jasota'),
(14, 11, 60, 20, 'Jasota'),
(15, 12, 65, 5, 'Jasota'),
(16, 13, 70, 15, 'Bidean'),
(17, 14, 75, 4, 'Jasota'),
(18, 15, 80, 10, 'Jasota'),
(19, 16, 60, 8, 'Jasota'), 
(20, 17, 55, 50, 'Jasota'), 
(21, 18, 5, 100, 'Bidean'), 
(22, 19, 25, 20, 'Ezabatua'), 
(23, 20, 5, 10, 'Jasota'),
(24, 21, 15, 5, 'Jasota'),
(25, 22, 25, 8, 'Jasota'),
(26, 23, 35, 12, 'Jasota'),
(27, 24, 45, 10, 'Bidean'),
(28, 25, 55, 6, 'Jasota'),
(29, 26, 65, 5, 'Jasota'),
(30, 27, 75, 4, 'Jasota'),
(31, 28, 60, 8, 'Jasota'), 
(32, 29, 25, 20, 'Bidean'), 
(33, 30, 5, 10, 'Jasota');

/*
CREATE TABLE IF NOT EXISTS eskaerak (
    id_eskaera INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    bezeroa_id INT UNSIGNED NOT NULL,
    langilea_id INT UNSIGNED,
    data DATETIME DEFAULT CURRENT_TIMESTAMP,
    eguneratze_data DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    guztira_prezioa DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    
    faktura_zenbakia VARCHAR(20) UNIQUE NOT NULL,
	faktura_url VARCHAR(255),
    
    eskaera_egoera ENUM('Prestatzen', 'Osatua/Bidalita', 'Ezabatua') NOT NULL DEFAULT 'Prestatzen',
    
    CONSTRAINT fk_eskaera_bezeroa FOREIGN KEY (bezeroa_id) REFERENCES bezeroak(id_bezeroa),
    CONSTRAINT fk_eskaera_langilea FOREIGN KEY (langilea_id) REFERENCES langileak(id_langilea)
);
*/

-- 7. ESKAERAK ETA FAKTURAK
INSERT INTO eskaerak (id_eskaera, bezeroa_id, langilea_id, guztira_prezioa, faktura_zenbakia, eskaera_egoera, data) VALUES
(1, 1, 5, 2199.00, 'FAK-2024-001', 'Osatua/Bidalita', '2024-01-08 10:00:00'),
(2, 2, 6, 1499.00, 'FAK-2024-002', 'Osatua/Bidalita', '2024-01-09 11:30:00'),
(3, 1, 7, 799.00, 'FAK-2024-003', 'Prestatzen', '2024-01-10 09:15:00'),
(4, 3, 5, 4500.00, 'FAK-2024-004',  'Osatua/Bidalita', '2024-01-11 14:00:00'),
(5, 4, 6, 1209.00, 'FAK-2024-005', 'Prestatzen', '2024-01-12 16:45:00'),
(6, 2, 7, 299.00, 'FAK-2024-006', 'Osatua/Bidalita', '2024-01-14 08:30:00'),
(7, 5, 5, 150.00, 'FAK-2024-007', 'Ezabatua', '2024-01-15 10:00:00'),
(8, 1, 6, 99.00, 'FAK-2024-008', 'Osatua/Bidalita', '2024-01-18 13:20:00'),
(9, 3, 7, 3800.00, 'FAK-2024-009', 'Osatua/Bidalita', '2024-01-22 09:10:00'),
(10, 4, 5, 600.00, 'FAK-2024-010', 'Osatua/Bidalita', '2024-01-25 15:40:00'),
(11, 2, 6, 135.00, 'FAK-2024-011', 'Prestatzen', '2024-01-28 11:00:00'),
(12, 1, 7, 450.00, 'FAK-2024-012', 'Osatua/Bidalita', '2024-02-01 10:30:00'),
(13, 3, 5, 2500.00,'FAK-2024-013', 'Osatua/Bidalita', '2024-02-05 14:15:00'),
(14, 5, 6, 89.00, 'FAK-2024-014','Prestatzen', '2024-02-08 09:00:00'),
(15, 2, 7, 120.00, 'FAK-2024-015', 'Osatua/Bidalita', '2024-02-12 16:20:00'),
(16, 4, 5, 2499.00, 'FAK-2024-016',  'Osatua/Bidalita', '2024-02-15 11:00:00'),
(17, 1, 6, 1400.00, 'FAK-2024-017', 'Prestatzen', '2024-02-18 13:45:00'),
(18, 3, 7, 550.00, 'FAK-2024-018', 'Osatua/Bidalita', '2024-02-22 10:15:00'),
(19, 2, 5, 15.00, 'FAK-2024-019', 'Osatua/Bidalita', '2024-02-25 09:30:00'),
(20, 5, 6, 45.00, 'FAK-2024-020', 'Ezabatua', '2024-03-01 15:00:00'),
(21, 1, 7, 1250.00, 'FAK-2024-021', 'Osatua/Bidalita', '2024-03-05 14:30:00'),
(22, 4, 5, 900.00, 'FAK-2024-022', 'Prestatzen', '2024-03-08 11:20:00'),
(23, 2, 6, 320.00, 'FAK-2024-023', 'Osatua/Bidalita', '2024-03-12 09:45:00'),
(24, 3, 7, 1800.00, 'FAK-2024-024', 'Osatua/Bidalita', '2024-03-15 16:10:00'),
(25, 1, 5, 650.00, 'FAK-2024-025', 'Prestatzen', '2024-03-18 10:00:00'),
(26, 5, 6, 75.00, 'FAK-2024-026', 'Osatua/Bidalita', '2024-03-22 13:50:00'),
(27, 2, 7, 1500.00, 'FAK-2024-027', 'Osatua/Bidalita', '2024-03-25 11:15:00'),
(28, 3, 5, 2200.00, 'FAK-2024-028', 'Prestatzen', '2024-03-28 15:30:00'),
(29, 4, 6, 60.00, 'FAK-2024-029', 'Osatua/Bidalita', '2024-04-01 09:20:00'),
(30, 1, 7, 1200.00, 'FAK-2024-030', 'Osatua/Bidalita', '2024-04-05 14:00:00');

INSERT INTO eskaera_lerroak (id_eskaera_lerroa, eskaera_id, produktua_id, kantitatea, unitate_prezioa, eskaera_lerro_egoera) VALUES
(1, 1, 1, 1, 2199.00, 'Osatua/Bidalita'),
(2, 2, 2, 1, 1499.00, 'Osatua/Bidalita'),
(3, 3, 24, 1, 799.00, 'Prestatzen'),
(4, 4, 46, 1, 4500.00, 'Osatua/Bidalita'),
(5, 5, 21, 1, 1209.00, 'Prestatzen'),
(6, 6, 51, 1, 299.00, 'Osatua/Bidalita'),
(7, 7, 57, 1, 150.00, 'Ezabatua'), 
(8, 8, 76, 1, 99.00, 'Osatua/Bidalita'),
(9, 9, 47, 1, 3800.00, 'Osatua/Bidalita'),
(10, 10, 32, 1, 600.00, 'Osatua/Bidalita'),
(11, 11, 58, 1, 135.00, 'Prestatzen'), 
(12, 12, 29, 1, 450.00, 'Osatua/Bidalita'),
(13, 13, 11, 1, 2499.00, 'Osatua/Bidalita'),
(14, 14, 77, 1, 89.00, 'Prestatzen'),
(15, 15, 80, 1, 120.00, 'Osatua/Bidalita'),
(16, 16, 11, 1, 2499.00, 'Osatua/Bidalita'),
(17, 17, 2, 1, 1400.00, 'Prestatzen'),
(18, 18, 61, 1, 550.00, 'Osatua/Bidalita'),
(19, 19, 66, 1, 15.00, 'Osatua/Bidalita'),
(20, 20, 70, 1, 25.00, 'Ezabatua'), 
(21, 20, 71, 2, 10.00, 'Ezabatua'), 
(22, 21, 4, 1, 950.00, 'Osatua/Bidalita'),
(23, 21, 54, 1, 300.00, 'Osatua/Bidalita'),
(24, 22, 34, 1, 900.00, 'Prestatzen'),
(25, 23, 62, 1, 320.00, 'Osatua/Bidalita'),
(26, 24, 17, 1, 1800.00, 'Osatua/Bidalita'),
(27, 25, 27, 1, 650.00, 'Prestatzen'),
(28, 26, 79, 1, 75.00, 'Osatua/Bidalita'),
(29, 27, 49, 1, 1500.00, 'Osatua/Bidalita'),
(30, 28, 48, 1, 2200.00, 'Prestatzen'),
(31, 29, 54, 1, 60.00, 'Osatua/Bidalita'), 
(32, 30, 50, 1, 1200.00, 'Osatua/Bidalita');


