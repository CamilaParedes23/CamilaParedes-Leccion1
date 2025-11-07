@echo off
echo ========================================
echo   EJECUTAR SERVIDOR SOAP - JAVA
echo ========================================
echo.

echo Compilando proyecto...
call mvn clean compile

if %ERRORLEVEL% NEQ 0 (
    echo Error al compilar el proyecto
    pause
    exit /b 1
)

echo.
echo Iniciando servidor SOAP...
echo.
call mvn exec:java -Dexec.mainClass="ec.universidad.soap.Main"

pause
