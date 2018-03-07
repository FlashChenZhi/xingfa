@echo off
color A

set BATPATH=D:\bat\utilbat
echo CommunicationLauncher开始启动！
start %BATPATH%\CommunicationLauncher.bat
echo 正在启动......
@echo off
ping 127.0.0.1 -n 8
::if errorlevel == 0 (echo CommunicationLauncher.bat运行结果:%errorlevel%;CommunicationLauncher.bat启动成功!) else (goto CommunicationLauncherEnd)


echo XmlCommunicationLauncher开始启动！
start %BATPATH%\XmlCommunicationLauncher.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo XmlCommunicationLauncher.bat运行结果:%errorlevel%;XmlCommunicationLauncher.bat启动成功!) else (goto XmlCommunicationLauncherEnd)

echo AsrsJobCenter开始启动！
start %BATPATH%\AsrsJobCenter.bat

ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo AsrsJobCenter.bat运行结果:%errorlevel%;AsrsJobCenter.bat启动成功!) else (goto AsrsJobCenterEnd)

echo StartBlockThread开始启动！
start %BATPATH%\StartBlockThread.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo StartBlockThread.bat运行结果:%errorlevel%;StartBlockThread.bat启动成功!) else (goto StartBlockThreadEnd)

echo Msg10Send开始启动！
start %BATPATH%\Msg10Send.bat 
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo Msg10Send.bat运行结果:%errorlevel%;Msg10Send.bat启动成功!) else (goto Msg10SendEnd)

echo Msg06Send开始启动！
start %BATPATH%\Msg06Send.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo Msg06Send.bat运行结果:%errorlevel%;Msg06Send.bat启动成功!) else (goto Msg06SendEnd)

echo XmlThread开始启动！
start %BATPATH%\XmlThread.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo XmlThread.bat运行结果:%errorlevel%;XmlThread.bat启动成功!) else (goto XmlThreadEnd)

echo ThreadMain开始启动！
start %BATPATH%\ThreadMain.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo ThreadMain.bat运行结果:%errorlevel%;ThreadMain.bat启动成功!) else (goto ThreadMainEnd)

echo AsrsJobClearThread开始启动！
start %BATPATH%\AsrsJobClearThread.bat
ping 127.0.0.1 -n 5

echo InputRetrievalOrderLineThread开始启动！
start %BATPATH%\InputRetrievalOrderLineThread.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo InputRetrievalOrderLineThread.bat运行结果:%errorlevel%;InputRetrievalOrderLineThread.bat启动成功!) else (goto InputRetrievalOrderLineThreadEnd)

echo InputSkuThread开始启动！
start %BATPATH%\InputSkuThread.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo InputSkuThread.bat运行结果:%errorlevel%;InputSkuThread.bat启动成功!) else (goto InputSkuThreadEnd)

echo StockOutThread开始启动！
start %BATPATH%\StockOutThread.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo StockOutThread.bat运行结果:%errorlevel%;StockOutThread.bat启动成功!) else (goto StockOutThreadEnd)

echo 启动完成 & pause >nul
exit
:rmiEnd
echo rmi运行结果:%errorlevel%;rmi.bat启动失败! & pause >nul
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
:InputRetrievalOrderLineThreadEnd
echo InputRetrievalOrderLineThread运行结果:%errorlevel%;InputRetrievalOrderLineThread.bat启动失败! & pause >nul
exit
:InputSkuThreadEnd
echo InputSkuThread运行结果:%errorlevel%;InputSkuThread.bat启动失败! & pause >nul
exit
:StockOutThreadEnd
echo StockOutThread运行结果:%errorlevel%;StockOutThread.bat启动失败! & pause >nul
exit


