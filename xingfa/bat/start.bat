@echo off
color A

set BATPATH=D:\bat\utilbat
echo CommunicationLauncher��ʼ������
start %BATPATH%\CommunicationLauncher.bat
echo ��������......
@echo off
ping 127.0.0.1 -n 8
::if errorlevel == 0 (echo CommunicationLauncher.bat���н��:%errorlevel%;CommunicationLauncher.bat�����ɹ�!) else (goto CommunicationLauncherEnd)


echo XmlCommunicationLauncher��ʼ������
start %BATPATH%\XmlCommunicationLauncher.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo XmlCommunicationLauncher.bat���н��:%errorlevel%;XmlCommunicationLauncher.bat�����ɹ�!) else (goto XmlCommunicationLauncherEnd)

echo AsrsJobCenter��ʼ������
start %BATPATH%\AsrsJobCenter.bat

ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo AsrsJobCenter.bat���н��:%errorlevel%;AsrsJobCenter.bat�����ɹ�!) else (goto AsrsJobCenterEnd)

echo StartBlockThread��ʼ������
start %BATPATH%\StartBlockThread.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo StartBlockThread.bat���н��:%errorlevel%;StartBlockThread.bat�����ɹ�!) else (goto StartBlockThreadEnd)

echo Msg10Send��ʼ������
start %BATPATH%\Msg10Send.bat 
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo Msg10Send.bat���н��:%errorlevel%;Msg10Send.bat�����ɹ�!) else (goto Msg10SendEnd)

echo Msg06Send��ʼ������
start %BATPATH%\Msg06Send.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo Msg06Send.bat���н��:%errorlevel%;Msg06Send.bat�����ɹ�!) else (goto Msg06SendEnd)

echo XmlThread��ʼ������
start %BATPATH%\XmlThread.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo XmlThread.bat���н��:%errorlevel%;XmlThread.bat�����ɹ�!) else (goto XmlThreadEnd)

echo ThreadMain��ʼ������
start %BATPATH%\ThreadMain.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo ThreadMain.bat���н��:%errorlevel%;ThreadMain.bat�����ɹ�!) else (goto ThreadMainEnd)

echo AsrsJobClearThread��ʼ������
start %BATPATH%\AsrsJobClearThread.bat
ping 127.0.0.1 -n 5

echo InputRetrievalOrderLineThread��ʼ������
start %BATPATH%\InputRetrievalOrderLineThread.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo InputRetrievalOrderLineThread.bat���н��:%errorlevel%;InputRetrievalOrderLineThread.bat�����ɹ�!) else (goto InputRetrievalOrderLineThreadEnd)

echo InputSkuThread��ʼ������
start %BATPATH%\InputSkuThread.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo InputSkuThread.bat���н��:%errorlevel%;InputSkuThread.bat�����ɹ�!) else (goto InputSkuThreadEnd)

echo StockOutThread��ʼ������
start %BATPATH%\StockOutThread.bat
ping 127.0.0.1 -n 5
::if errorlevel == 0 (echo StockOutThread.bat���н��:%errorlevel%;StockOutThread.bat�����ɹ�!) else (goto StockOutThreadEnd)

echo ������� & pause >nul
exit
:rmiEnd
echo rmi���н��:%errorlevel%;rmi.bat����ʧ��! & pause >nul
exit
:CommunicationLauncherEnd
echo CommunicationLauncher���н��:%errorlevel%;CommunicationLauncher.bat����ʧ��! & pause >nul
exit
:XmlCommunicationLauncherEnd
echo XmlCommunicationLauncher���н��:%errorlevel%;XmlCommunicationLauncher.bat����ʧ��! & pause >nul
exit
:AsrsJobCenterEnd
echo AsrsJobCenter���н��:%errorlevel%;AsrsJobCenter.bat����ʧ��! & pause >nul
exit
:StartBlockThreadEnd
echo StartBlockThread���н��:%errorlevel%;StartBlockThread.bat����ʧ��! & pause >nul
exit
:Msg10SendEnd
echo Msg10Send���н��:%errorlevel%;Msg10Send.bat����ʧ��! & pause >nul
exit
:Msg06SendEnd
echo Msg06Send���н��:%errorlevel%;Msg06Send.bat����ʧ��! & pause >nul
exit
:XmlThreadEnd
echo XmlThread���н��:%errorlevel%;XmlThread.bat����ʧ��! & pause >nul
exit
:ThreadMainEnd
echo ThreadMain���н��:%errorlevel%;ThreadMain.bat����ʧ��! & pause >nul
exit
:InputRetrievalOrderLineThreadEnd
echo InputRetrievalOrderLineThread���н��:%errorlevel%;InputRetrievalOrderLineThread.bat����ʧ��! & pause >nul
exit
:InputSkuThreadEnd
echo InputSkuThread���н��:%errorlevel%;InputSkuThread.bat����ʧ��! & pause >nul
exit
:StockOutThreadEnd
echo StockOutThread���н��:%errorlevel%;StockOutThread.bat����ʧ��! & pause >nul
exit


