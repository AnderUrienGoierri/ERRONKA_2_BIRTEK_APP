
$path = "src"
$out = "java_aplikazioko_kodea.md"
"#" + " Java Aplikazioko Kodea" | Out-File -FilePath $out -Encoding utf8
$files = Get-ChildItem -Path $path -Filter *.java -Recurse
foreach ($file in $files) {
    "" | Out-File -FilePath $out -Append -Encoding utf8
    "## " + $file.Name | Out-File -FilePath $out -Append -Encoding utf8
    "" | Out-File -FilePath $out -Append -Encoding utf8
    "```java" | Out-File -FilePath $out -Append -Encoding utf8
    Get-Content $file.FullName | Out-File -FilePath $out -Append -Encoding utf8
    "```" | Out-File -FilePath $out -Append -Encoding utf8
}
"Done"
