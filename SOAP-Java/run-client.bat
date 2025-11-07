@echo off
echo ========================================
echo   EJECUTAR CLIENTE SOAP - JAVA
echo ========================================
echo.
echo Asegurate de que el servidor este corriendo
echo en http://localhost:8080/ClienteService
echo.
pause

echo.
echo Ejecutando cliente...
echo.
call mvn exec:java -Dexec.mainClass="ec.universidad.soap.client.ClienteSOAPClient"

pause
