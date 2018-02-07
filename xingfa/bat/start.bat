@echo off
color A
set JAVAHOME=D:\Program Files (x86)\Java\jdk1.7.0_51
set XINGFAPATH=D:\gitRepository\xingfa_wcs
set BATPATH=C:\Users\ed_chen\Desktop\bat\utilbat

echo CommunicationLauncher开始启动！
call %BATPATH%\CommunicationLauncher.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo CommunicationLauncher.bat运行结果:%errorlevel%;CommunicationLauncher.bat启动成功!) else (goto CommunicationLauncherEnd)

echo XmlCommunicationLauncher开始启动！
call %BATPATH%\XmlCommunicationLauncher.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo XmlCommunicationLauncher.bat运行结果:%errorlevel%;XmlCommunicationLauncher.bat启动成功!) else (goto XmlCommunicationLauncherEnd)

echo AsrsJobCenter开始启动！
call %BATPATH%\AsrsJobCenter.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo AsrsJobCenter.bat运行结果:%errorlevel%;AsrsJobCenter.bat启动成功!) else (goto AsrsJobCenterEnd)

echo StartBlockThread开始启动！
call %BATPATH%\StartBlockThread.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo StartBlockThread.bat运行结果:%errorlevel%;StartBlockThread.bat启动成功!) else (goto StartBlockThreadEnd)

echo Msg10Send开始启动！
call %BATPATH%\Msg10Send.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo Msg10Send.bat运行结果:%errorlevel%;Msg10Send.bat启动成功!) else (goto Msg10SendEnd)

echo Msg06Send开始启动！
call %BATPATH%\Msg06Send.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo Msg06Send.bat运行结果:%errorlevel%;Msg06Send.bat启动成功!) else (goto Msg06SendEnd)

echo XmlThread开始启动！
call %BATPATH%\XmlThread.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo XmlThread.bat运行结果:%errorlevel%;XmlThread.bat启动成功!) else (goto XmlThreadEnd)

echo ThreadMain开始启动！
call %BATPATH%\ThreadMain.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo ThreadMain.bat运行结果:%errorlevel%;ThreadMain.bat启动成功!) else (goto ThreadMainEnd)

echo 启动完成 & pause >nul
exit
:CommunicationLauncherEnd
echo CommunicationLauncher运行结果:%errorlevel%;CommunicationLauncher.bat启动失败! & pause >nul
exit
:XmlCommunicationLauncherEnd
echo XmlCommunicationLauncher运行结果:%errorlevel%;XmlCommunicationLauncher.bat启动失败! & pause >nul
exit
:AsrsJobCenterEnd
echo AsrsJobCenter运行结果:%errorlevel%;AsrsJobCenter.bat启动失败! & pause >nul
exit
:StartBlockThreadEnd
echo StartBlockThread运行结果:%errorlevel%;StartBlockThread.bat启动失败! & pause >nul
exit
:Msg10SendEnd
echo Msg10Send运行结果:%errorlevel%;Msg10Send.bat启动失败! & pause >nul
exit
:Msg06SendEnd
echo Msg06Send运行结果:%errorlevel%;Msg06Send.bat启动失败! & pause >nul
exit
:XmlThreadEnd
echo XmlThread运行结果:%errorlevel%;XmlThread.bat启动失败! & pause >nul
exit
:ThreadMainEnd
echo ThreadMain运行结果:%errorlevel%;ThreadMain.bat启动失败! & pause >nul
exit