@echo off
setlocal

if not defined JAVA_HOME (
    if exist "C:\Program Files\Java\jdk-17\bin\java.exe" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-17"
    )
)

if not exist "%JAVA_HOME%\bin\java.exe" (
    echo JAVA_HOME is not set to a valid JDK.
    echo Install JDK 17 or set JAVA_HOME before running this script.
    exit /b 1
)

set "PATH=%JAVA_HOME%\bin;%PATH%"
call "%~dp0mvnw.cmd" -pl FrontEnd javafx:run
