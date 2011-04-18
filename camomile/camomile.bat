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
call awk95 -v OS="WINDOWS" -v PORT=%1 -f json.awk -f camomile.awk
