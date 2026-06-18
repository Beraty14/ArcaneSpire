@echo off
echo Oyun baslatiliyor... Lutfen bekleyin.
echo (Bu pencere olasi hatalari gormeniz icin acik kalacaktir)
echo.

java -cp "bin;src" Main

if %errorlevel% neq 0 (
    echo.
    echo Oyun beklenmedik bir sekilde sonlandi veya baslatilamadi!
    echo Lutfen yukaridaki hata mesajini kontrol edin.
    echo (Java yuklu olmayabilir veya surumu eski olabilir. Lutfen Java 8 veya uzeri bir surum yukleyin.)
) else (
    echo Oyun sorunsuz calisti.
)

echo.
pause
