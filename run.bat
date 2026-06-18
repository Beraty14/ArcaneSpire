@echo off
echo Arcane Spire Derleniyor...
javac --release 8 -cp "src" src/views/*.java src/controllers/*.java src/models/*.java src/Main.java -d bin
if %errorlevel% neq 0 (
    echo.
    echo Derleme hatasi! Lutfen kodlari kontrol et.
    pause
    exit /b %errorlevel%
)
echo Derleme basarili. Oyun baslatiliyor...
java -cp bin Main
pause
