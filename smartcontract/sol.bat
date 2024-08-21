@if "%DEBUG%"=="" @echo off
set SCRIPT_DIR=%~dp0


if not "%1"=="" set BLOCKCHAIN_NAME=%1
if "%BLOCKCHAIN_NAME%"=="" goto blochainnotdefined


if not "%2"=="" set CONTRACT_NAME=%2
if "%CONTRACT_NAME%"=="" goto contractnamenotdefined


if not "%3"=="" set JAVA_HOME=%3


web3j generate solidity -pt -a "%SCRIPT_DIR%%BLOCKCHAIN_NAME%\build\contracts\%CONTRACT_NAME%.abi" -b "%SCRIPT_DIR%%BLOCKCHAIN_NAME%\build\contracts\%CONTRACT_NAME%.bin" -o "%SCRIPT_DIR%..\src\main\java" -p br.com.elysian.tartarus.model.blockchain.contracts.%BLOCKCHAIN_NAME%
echo *----------------------------------------*
echo ^| Starting class generation, please wait ^|
echo *----------------------------------------*
goto waitForCompletion


:waitForCompletion
timeout /t 1 /nobreak >nul
tasklist /FI "IMAGENAME eq web3j.exe" | find /I "web3j.exe" >nul
if %ERRORLEVEL% == 0 (
    goto waitForCompletion
) else ( goto end )


:blochainnotdefined
echo ERROR: BLOCKCHAIN_NAME not defined!
echo Define the BLOCKCHAIN_NAME variable before executing or pass it as the first argument of the script.
goto fail


:contractnamenotdefined
echo ERROR: CONTRACT_NAME not defined!
echo Define the CONTRACT_NAME variable before executing or pass it as the first argument of the script.
goto fail


:fail
exit /b 1


:end
echo ^|
echo ^|
echo ^| - Build successfully!
rem echo ^| - - View the logs in `%LOG_PATH%`
exit /b 0