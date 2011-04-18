@echo off
if "%OS%" == "Windows_NT" setlocal
rem ###########################################################################
rem
rem Start the Camomile server
rem
rem Use like:
rem
rem camomile [port]
rem
rem E.g.
rem
rem camomile
rem camomile 8181
rem
rem ###########################################################################

cd bin
call awk95 -v OS="WINDOWS" -f json.awk -f connections.awk

set JETTY_PORT=-Djetty.port=%1
if not "%JETTY_PORT%" == "-Djetty.port=" goto gotPort
set JETTY_PORT=""
:gotPort

cd ..\server 
call java %JETTY_PORT% -jar start.jar lib=..\connections OPTIONS=plus
