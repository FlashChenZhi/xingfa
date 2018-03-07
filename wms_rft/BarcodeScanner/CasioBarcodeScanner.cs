using System;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;
using Calib;

namespace Barcode
{
    public class CasioBarcodeScanner : BarcodeScanner
    {
        [DllImport("coredll.dll")]
        public static extern IntPtr GetForegroundWindow();

        public static IntPtr HWND;

        private Thread thread;

        static int[] DecodeNum = {
									  OBReadLibNet.Def.OBR_NONDT,
									  OBReadLibNet.Def.OBR_CD39,
									  OBReadLibNet.Def.OBR_NW_7,
									  OBReadLibNet.Def.OBR_WPCA,
									  OBReadLibNet.Def.OBR_WPC,
									  OBReadLibNet.Def.OBR_UPEA,
									  OBReadLibNet.Def.OBR_UPE,
									  OBReadLibNet.Def.OBR_IDF,
									  OBReadLibNet.Def.OBR_ITF,
									  OBReadLibNet.Def.OBR_CD93,
									  OBReadLibNet.Def.OBR_CD128,
									  OBReadLibNet.Def.OBR_MSI,
									  OBReadLibNet.Def.OBR_IATA
								  };

        static string[] DecodeName = {
										 "          ",
										 "OBR_CD39  ",
										 "OBR_NW_7  ",
										 "OBR_WPCA  ",
										 "OBR_WPC   ",
										 "OBR_UPEA  ",
										 "OBR_UPE   ",
										 "OBR_IDF   ",
										 "OBR_ITF   ",
										 "OBR_CD93  ",
										 "OBR_CD128 ",
										 "OBR_MSI   ",
										 "OBR_IATA  "
									 };

        public override bool Initialize()
        {
            int iRet = 0;

            HWND = GetForegroundWindow();
            iRet += OBReadLibNet.Api.OBRLoadConfigFile();		//ini File read default value set
            iRet += OBReadLibNet.Api.OBRSetDefaultSymbology();	//1D(OBR) driver mode will be ini File vallue
            iRet += OBReadLibNet.Api.OBRSetScanningKey(OBReadLibNet.Def.OBR_TRIGGERKEY_L | OBReadLibNet.Def.OBR_TRIGGERKEY_R | OBReadLibNet.Def.OBR_CENTERTRIGGER);
            iRet += OBReadLibNet.Api.OBRSetScanningCode(OBReadLibNet.Def.OBR_ALL);
            iRet += OBReadLibNet.Api.OBRSetBuffType(OBReadLibNet.Def.OBR_BUFOBR);	//1D(OBR) driver mode will be OBR_BUFOBR
            iRet += OBReadLibNet.Api.OBRSetScanningNotification(OBReadLibNet.Def.OBR_EVENT, IntPtr.Zero);	//1D(OBR) driver mode will be OBR_EVENT
//            iRet += OBReadLibNet.Api.OBRSetSuffixChar(OBReadLibNet.Def.OBR_ENDCL);

            if(iRet != OBReadLibNet.Def.OBR_OK)
            {
                return false;
            }

            iRet = OBReadLibNet.Api.OBROpen(HWND, 0);			//OBRDRV open
            if (iRet == OBReadLibNet.Def.OBR_PON)
            {
                if (OBReadLibNet.Api.OBRClose() != OBReadLibNet.Def.OBR_OK)
                {
                    return false;
                }
                SystemLibNet.Api.SysTerminateWaitEvent();
                HWND = IntPtr.Zero;

                HWND = GetForegroundWindow();
                iRet = OBReadLibNet.Api.OBROpen(HWND, 0);
                
                if (iRet != OBReadLibNet.Def.OBR_OK)
                {
                    return false;
                }
            }

            iRet = OBReadLibNet.Api.OBRClearBuff();

            return iRet == OBReadLibNet.Def.OBR_OK;
        }        

        private void start()
        {
            while (true)
            {
                SystemLibNet.Api.SysWaitForEvent(IntPtr.Zero, OBReadLibNet.Def.OBR_NAME_EVENT, SystemLibNet.Def.INFINITE);  //Wait event                

                if (HWND != IntPtr.Zero)
                {
                    int len1 = new int();	//digit number
                    byte len2 = new byte();//digit number
                    int dwrcd = new int();	//barcode type
                    byte lcnt = new byte();
                    byte[] buff = new byte[1024];

                    if (OBReadLibNet.Api.OBRGetStatus(ref len1, ref lcnt) == OBReadLibNet.Def.OBR_OK
                        && len1 != 0 && OBReadLibNet.Api.OBRGets(buff, ref dwrcd, ref len2) == OBReadLibNet.Def.OBR_OK)
                    {
                        Encoding ASCII = Encoding.GetEncoding("ascii");
                        string dataText = ASCII.GetString(buff, 0, len2);

                        string dataType = string.Empty;
                        for (int i = 0; i < 13; i++)
                        {
                            if (DecodeNum[i] == dwrcd)
                            {
                                dataType = DecodeName[i];
                                break;
                            }
                        }

                        OnBarcodeScan(new BarcodeScannerEventArgs(dataText, dataType));
                    }
                    OBReadLibNet.Api.OBRClearBuff();
                }                
            }
        }
       
        public override void Start()
        {            
            thread = new Thread(start);
            thread.Start();   
            
        }

        public override void Stop()
        {
            if (thread != null)
            {
                thread.Abort();
            }
        }

        public override void Terminate()
        {
            OBReadLibNet.Api.OBRClose();				//OBRDRV Close
            SystemLibNet.Api.SysTerminateWaitEvent();	//End SysWaitForEvent function
            HWND = IntPtr.Zero;
        }      
  
        public override void Dispose(bool disposing)
        {
            if (disposing)
            {
                Stop();
                Terminate();
            }
        }
    }
}