@echo off
color A
set JAVAHOME=D:\Program Files (x86)\Java\jdk1.7.0_51
set XINGFAPATH=D:\gitRepository\xingfa_wcs
set BATPATH=C:\Users\ed_chen\Desktop\bat\utilbat


echo CommunicationLauncher��ʼ������
call %BATPATH%\CommunicationLauncher.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo CommunicationLauncher.bat���н��:%errorlevel%;CommunicationLauncher.bat�����ɹ�!) else (goto CommunicationLauncherEnd)

echo XmlCommunicationLauncher��ʼ������
call %BATPATH%\XmlCommunicationLauncher.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo XmlCommunicationLauncher.bat���н��:%errorlevel%;XmlCommunicationLauncher.bat�����ɹ�!) else (goto XmlCommunicationLauncherEnd)

echo AsrsJobCenter��ʼ������
call %BATPATH%\AsrsJobCenter.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo AsrsJobCenter.bat���н��:%errorlevel%;AsrsJobCenter.bat�����ɹ�!) else (goto AsrsJobCenterEnd)

echo StartBlockThread��ʼ������
call %BATPATH%\StartBlockThread.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo StartBlockThread.bat���н��:%errorlevel%;StartBlockThread.bat�����ɹ�!) else (goto StartBlockThreadEnd)

echo Msg10Send��ʼ������
call %BATPATH%\Msg10Send.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo Msg10Send.bat���н��:%errorlevel%;Msg10Send.bat�����ɹ�!) else (goto Msg10SendEnd)

echo Msg06Send��ʼ������
call %BATPATH%\Msg06Send.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo Msg06Send.bat���н��:%errorlevel%;Msg06Send.bat�����ɹ�!) else (goto Msg06SendEnd)

echo XmlThread��ʼ������
call %BATPATH%\XmlThread.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo XmlThread.bat���н��:%errorlevel%;XmlThread.bat�����ɹ�!) else (goto XmlThreadEnd)

echo ThreadMain��ʼ������
call %BATPATH%\ThreadMain.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo ThreadMain.bat���н��:%errorlevel%;ThreadMain.bat�����ɹ�!) else (goto ThreadMainEnd)

echo InputRetrievalOrderLineThread��ʼ������
call %BATPATH%\InputRetrievalOrderLineThread.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo InputRetrievalOrderLineThread.bat���н��:%errorlevel%;InputRetrievalOrderLineThread.bat�����ɹ�!) else (goto InputRetrievalOrderLineThreadEnd)

echo InputSkuThread��ʼ������
call %BATPATH%\InputSkuThread.bat %JAVAHOME%,%XINGFAPATH%
::if errorlevel == 0 (echo InputSkuThread.bat���н��:%errorlevel%;InputSkuThread.bat�����ɹ�!) else (goto InputSkuThreadEnd)

echo StockOutThread��ʼ������
call %BATPATH%\StockOutThread.bat %JAVAHOME%,%XINGFAPATH%
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