@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.3.2
@REM
@REM Optional ENV vars
@REM   JAVA_HOME - location of a JDK home dir
@REM   MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM ----------------------------------------------------------------------------

@REM Begin all REM://- lines with @
@echo off
@REM set title of command window
title %0
@REM enable echoing by setting MAVEN_BATCH_ECHO to 'on'
@if "%MAVEN_BATCH_ECHO%"=="" @echo off

@REM set %HOME% to equivalent of $HOME
if "%HOME%"=="" (set "HOME=%HOMEDRIVE%%HOMEPATH%")

@REM Execute a user defined script before this one
if not "%MAVEN_SKIP_RC%"=="" goto skipRcPre
@REM check for pre script, once with legacy .bat ending and once with .cmd ending
if exist "%USERPROFILE%\mavenrc_pre.bat" call "%USERPROFILE%\mavenrc_pre.bat" %*
if exist "%USERPROFILE%\mavenrc_pre.cmd" call "%USERPROFILE%\mavenrc_pre.cmd" %*
:skipRcPre

set ERROR_CODE=0

@REM To isolate internal variables from possible post scripts, we use another setlocal
@setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%"=="" goto OkJHome
for %%i in (java.exe) do set "JAVACMD=%%~$PATH:i"
goto checkJCmd

:OkJHome
set "JAVACMD=%JAVA_HOME%\bin\java.exe"

:checkJCmd
if exist "%JAVACMD%" goto chkMHome

echo The JAVA_HOME environment variable is not defined correctly, >&2
echo this environment variable is needed to run this program. >&2
goto error

:chkMHome
set "MAVEN_HOME=%HOME%\.m2\wrapper\dists\apache-maven-3.9.9"

if exist "%MAVEN_HOME%\bin\mvn.cmd" goto init

@REM Download Maven
echo Downloading Apache Maven 3.9.9...

set "DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.9/apache-maven-3.9.9-bin.zip"
set "MAVEN_ZIP=%MAVEN_HOME%.zip"

@REM Read properties file if exists
set "WRAPPER_PROPERTIES=%~dp0.mvn\wrapper\maven-wrapper.properties"
if exist "%WRAPPER_PROPERTIES%" (
    for /f "usebackq tokens=1,* delims==" %%a in ("%WRAPPER_PROPERTIES%") do (
        if "%%a"=="distributionUrl" set "DOWNLOAD_URL=%%b"
    )
)

if not exist "%MAVEN_HOME%" mkdir "%MAVEN_HOME%"
if not exist "%MAVEN_HOME%\.." mkdir "%MAVEN_HOME%\.."

@REM Use PowerShell to download
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '%DOWNLOAD_URL%' -OutFile '%MAVEN_ZIP%'}"
if "%ERRORLEVEL%" NEQ "0" (
    echo Error: Failed to download Maven distribution >&2
    goto error
)

@REM Unzip
powershell -Command "& {Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%MAVEN_HOME%' -Force}"
if "%ERRORLEVEL%" NEQ "0" (
    echo Error: Failed to extract Maven distribution >&2
    goto error
)

@REM Move contents up one level
for /d %%i in ("%MAVEN_HOME%\apache-maven-*") do (
    xcopy /s /e /q /y "%%i\*" "%MAVEN_HOME%\" >nul 2>&1
    rd /s /q "%%i" >nul 2>&1
)

:init

set MAVEN_CMD_LINE_ARGS=%*

"%MAVEN_HOME%\bin\mvn.cmd" %MAVEN_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

if not "%MAVEN_BATCH_PAUSE%"=="" pause
if "%MAVEN_BATCH_TERMINATE_AFTER%"=="" goto exit_mvnw
if "%ERROR_CODE%"=="0" goto exit_mvnw
cmd /c exit /b %ERROR_CODE%

:exit_mvnw
cmd /c exit /b %ERROR_CODE%
