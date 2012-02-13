@echo off
setlocal ENABLEDELAYEDEXPANSION
REM Use like:
REM
REM camomile.bat
REM
REM and this will run camomile.
REM

REM Make sure the base is the full path 
set BASE=.
set SRC=%BASE%\src

echo Creating CLASSPATH...

set CLASSPATH=.

REM Add on extra jar files to CLASSPATH
REM External
for %%i in (%BASE%\connectors\*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%i
)

REM Server
for %%i in (%SRC%\lib\*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%i
)

REM These are the additions to the CLASSPATH required to compile the packages.
set CLASSPATH=%CLASSPATH%;%SRC%\classes
set CLASSPATH=%CLASSPATH%;%SRC%\classes\org

echo CLASSPATH created.

REM Do the run
echo Run Camomile...

java CamomileServer
