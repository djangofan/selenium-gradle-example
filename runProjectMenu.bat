@ECHO off
SET TITLETEXT=Gradle Run Script
TITLE %TITLETEXT%

:: Uncomment the next line to override JAVA_HOME on your system
::set JAVA_HOME=C:\Java\jdk1.6.0_33_x32

IF NOT DEFINED JAVA_HOME (
  ECHO JAVA_HOME must be defined as an environment variable.
  GOTO :END
)

IF NOT DEFINED GRADLE_HOME (
  ECHO GRADLE_HOME must be defined as an environment variable.
  ECHO Also, add GRADLE_HOME\bin to your path.
  ECHO.
  GOTO :END
)

IF DEFINED GROOVY_HOME (
  SET "PATH=%GROOVY_HOME%\bin;%PATH%"
) ELSE (
  ECHO You might want to define GROOVY_HOME as an environment variable.
)

SET "PATH=%JAVA_HOME%\bin;%GRADLE_HOME%\bin;%PATH%"

::-------------------------------------------------------------------
::  Parse first script argument for test action
::  If no args are passed then prompt user for test to run
::-------------------------------------------------------------------

:PICK
ECHO Current directory is "%CD%"
ECHO.
SET CHOICE=
ECHO.&ECHO.&ECHO.
SET CHOICE=%~1
IF "%~1"=="" (
  ECHO.
  ECHO [1] Run ^"Etsy^" tests
  ECHO [2] Run ^"Parallel Windows^" tests
  ECHO [3] List all projects
  ECHO [4] Display ^"Etsy^" tasks
  ECHO [5] Display ^"Parallel Windows^" tasks
  ECHO [6] Display ^"CommonLib^" tasks
  ECHO [7] Run a custom task
  ECHO [8] Run Gradle GUI
  ECHO [X] EXIT
  ECHO.
)
IF "%~1"=="" SET /P "CHOICE=Please enter a action you want to perform [1]: "
IF "%CHOICE%"=="" (
  SET CHOICE=1
)
IF "%CHOICE%"=="X" (
  GOTO :END
)
IF "%CHOICE%"=="x" (
  GOTO :END
)

::-------------------------------------------------------------------
::  Run action
::-------------------------------------------------------------------

IF "%CHOICE%"=="1" (
  ECHO gradle.bat etsy:clean etsy:runParallelTestsInFirefox etsy:runFrameTestInIE -info --stacktrace
  CALL gradle.bat etsy:clean etsy:runParallelTestsInFirefox etsy:runFrameTestInIE -info --stacktrace
  GOTO :END
) ELSE IF "%CHOICE%"=="2" (
  ECHO gradle.bat parallelwindows:clean parallelwindows:runAllTestsInFirefox parallelwindows:runAllTestsInIE parallelwindows:runAllTestsInChrome -info --stacktrace
  CALL gradle.bat parallelwindows:clean parallelwindows:runAllTestsInFirefox parallelwindows:runAllTestsInIE parallelwindows:runAllTestsInChrome -info --stacktrace
  GOTO :END
) ELSE IF "%CHOICE%"=="3" (
  CALL gradle.bat projects
  GOTO :PICK
) ELSE IF "%CHOICE%"=="4" (
  CALL gradle.bat etsy:tasks
  GOTO :PICK
) ELSE IF "%CHOICE%"=="5" (
  CALL gradle.bat parallelwindows:tasks
  GOTO :PICK
  GOTO :PICK
) ELSE IF "%CHOICE%"=="6" (
  CALL gradle.bat commonlib:tasks
  GOTO :PICK
) ELSE IF "%CHOICE%"=="7" (
  ECHO.
  SET /P COMMANDS=Enter a space separated set of Gradle tasks: 
  ECHO.
  CALL gradle.bat "%COMMANDS%" -info
  ECHO.
  GOTO :PICK
) ELSE IF "%CHOICE%"=="8" (
  CALL gradle.bat --gui
  GOTO :END
)  ELSE (
  ECHO Unknown option. Try again.
  GOTO :PICK
)
  
::-------------------------------------------------------------------
::  Functions
::-------------------------------------------------------------------

:END
ECHO Closing gradle_run.bat script
FOR /l %%a in (60,-1,1) do (TITLE %TITLETEXT% -- closing in %%as&PING.exe -n 2 -w 1 127.0.0.1>nul)
TITLE Script gradle_run.bat finished

