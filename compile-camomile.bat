@echo off
setlocal ENABLEDELAYEDEXPANSION
REM Use like:
REM
REM compile-camomile.bat
REM
REM and this will compile camomile.
REM

REM Make sure the base is the full path 
set BASE=camomile
set SRC=%BASE%\src

echo Creating CLASSPATH...

set CLASSPATH=.

REM Add on extra jar files to CLASSPATH
REM External
for %%i in (%BASE%\connectors\*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%i
)

REM jars
for %%i in (%SRC%\lib\*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%i
)

REM These are the additions to the CLASSPATH required to compile the packages.
set CLASSPATH=%CLASSPATH%;%SRC%\classes
set CLASSPATH=%CLASSPATH%;%SRC%\classes\org

echo CLASSPATH created.

echo.
echo %CLASSPATH%
echo.

REM Do the compile
echo Compiling Comomile...

REM find $SRC/classes -name "*.java" | xargs javac
for /f %%i in ('dir /s /b *.java') do (
  javac -cp %CLASSPATH% %%i
)

echo Camomile compiled.
