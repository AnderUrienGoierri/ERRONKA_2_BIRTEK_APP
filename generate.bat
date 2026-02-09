
@echo off
chcp 65001
echo # Java Aplikazioko Kodea > java_aplikazioko_kodea.md
for /r src %%f in (*.java) do (
    echo. >> java_aplikazioko_kodea.md
    echo ## %%~nxf >> java_aplikazioko_kodea.md
    echo. >> java_aplikazioko_kodea.md
    echo ```java >> java_aplikazioko_kodea.md
    type "%%f" >> java_aplikazioko_kodea.md
    echo. >> java_aplikazioko_kodea.md
    echo ``` >> java_aplikazioko_kodea.md
)
echo Done
