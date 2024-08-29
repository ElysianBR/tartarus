@if "%DEBUG%"=="" @echo off
set SCRIPT_DIR=%~dp0



if not "%1"=="" set BLOCKCHAIN_NAME=%1
if "%BLOCKCHAIN_NAME%"=="" goto blochainnotdefined



echo *-------------------------------------------*
echo ^| Starting compilation process, please wait ^|
echo *-------------------------------------------*
goto truffleprocess



:truffleprocess
set TRUFFLE_LOG=build\contracts\temp_truffle.log
cd "./%BLOCKCHAIN_NAME%/"
start /b "" cmd /c truffle compile > "%TRUFFLE_LOG%" 2>&1
goto waitForCompletiontruffle



:waitForCompletiontruffle
timeout /t 1 /nobreak >nul
tasklist /FI "IMAGENAME eq truffle.exe" | find /I "truffle.exe" >nul
if %ERRORLEVEL% == 0 (
    goto waitForCompletiontruffle
) else ( goto nodeprocess )



:nodeprocess
set OUT_PATH=%SCRIPT_DIR%%BLOCKCHAIN_NAME%\build\contracts
set NODE_LOG=build\contracts\temp_node.log
set JS_SCRIPT=%SCRIPT_DIR%extract_abi_bin.js
echo node "%JS_SCRIPT%" "%OUT_PATH%"
start /b "" cmd /c node "%JS_SCRIPT%" "%OUT_PATH%" >> "%NODE_LOG%" 2>&1
goto waitForCompletionnode



:waitForCompletionnode
timeout /t 1 /nobreak >nul
tasklist /FI "IMAGENAME eq node.exe" | find /I "node.exe" >nul
if %ERRORLEVEL% == 0 (
    goto waitForCompletionnode
) else ( goto end )



:blochainnotdefined
echo ERROR: BLOCKCHAIN_NAME not defined!
echo Define the BLOCKCHAIN_NAME variable before executing or pass it as the first argument of the script.
goto fail



:fail
exit /b 1



:end
set LOG_PATH=build\contracts\compile.log
type %TRUFFLE_LOG% >> %LOG_PATH%
DEL %TRUFFLE_LOG%
type %NODE_LOG% >> %LOG_PATH%
DEL %NODE_LOG%
echo ^|
echo ^|
echo ^| _ Build successfully!
echo ^| __ Logs available: `%SCRIPT_DIR%%LOG_PATH%`
exit /b 0