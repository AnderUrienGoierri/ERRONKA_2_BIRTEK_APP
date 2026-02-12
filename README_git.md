# Git Gida: Aldaketak Kudeatzeko Komandoak

Gida honek proiektuan erabiliko dituzun oinarrizko Git komandoak laburbiltzen ditu.

## 1. Oinarrizko tyukunketak (Add, Commit, Push)

Zure aldaketak gorde eta GitHub-era igotzeko:

```bash
# 1. Aldaketa guztiak prestatu
git add .

# 2. Aldaketak gorde mezu batekin
git commit -m "zure_mezua_hemen"

# 3. GitHub-era igo
git push
```

## 2. Brantzak (Brantzak sortu eta aldatu)

Lan berri bat hasterakoan brantza bat erabiltzea gomendatzen da:

```bash
# Brantza berri bat sortu eta bertara pasa
git checkout -b brantza_berriaren_izena

# Dauden brantzak zerrendatu
git branch

# Existitzen den brantza batera aldatu
git checkout brantzaren_izena
```

## 3. Merge (Brantzak elkartu)

Lan bat brantza batean amaitutakoan, `main` brantzarekin elkartzeko:

```bash
# 1. Lehenik main brantzara pasa
git checkout main

# 2. Brantza bereziko aldaketak main-era ekarri
git merge brantzaren_izena

# 3. Main eguneratua igo
git push
```

## 4. Laguntza komandoak

```bash
# Egoera ikusi (ze fitxategi aldatu diren)
git status

# Historia ikusi
git log --oneline
```
