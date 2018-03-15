using System;
using System.Runtime.InteropServices;
using System.Windows.Forms;
using Microsoft.Win32;
using wms_rft.Putaway;

namespace wms_rft
{
    static class Program
    {
        [DllImport("coredll.Dll")]
        private static extern int GetLastError();
        [DllImport("coredll.Dll")]
        private static extern int ReleaseMutex(IntPtr hMutex);
        [DllImport("coredll.Dll")]
        private static extern IntPtr CreateMutex(SECURITY_ATTRIBUTES lpMutexAttributes, bool bInitialOwner, string lpName);

        private const int ERROR_ALREADY_EXISTS = 0183;

        [StructLayout(LayoutKind.Sequential)]
        public class SECURITY_ATTRIBUTES { public int nLength; public int lpSecurityDescriptor; public int bInheritHandle;    }

        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [MTAThread]
        static void Main()
        {
            //byte[] ipaddrSmart = new byte[] {10, 244, 164, 1};
            //RegistryKey keySmart = Registry.LocalMachine.CreateSubKey("Comm").CreateSubKey("Tcpip").CreateSubKey("Hosts").CreateSubKey("webservice-smart-server");
            //keySmart.SetValue("ipaddr", ipaddrSmart, RegistryValueKind.Binary);

            //byte[]  ipaddrEt = new byte[] { 10, 244, 164, 251 };
            //RegistryKey keyEt = Registry.LocalMachine.CreateSubKey("Comm").CreateSubKey("Tcpip").CreateSubKey("Hosts").CreateSubKey("webservice-et-server");
            //keyEt.SetValue("ipaddr", ipaddrEt, RegistryValueKind.Binary);

            byte[] ipaddr = new byte[] { 192,168,10,13 };
            RegistryKey key = Registry.LocalMachine.CreateSubKey("Comm").CreateSubKey("Tcpip").CreateSubKey("Hosts").CreateSubKey("webservice-server");
            key.SetValue("ipaddr", ipaddr, RegistryValueKind.Binary);

            IntPtr hMutex = CreateMutex(null, false, "wms_rft");
            if (GetLastError() != ERROR_ALREADY_EXISTS)
            {
                Application.Run(new PutawaySettingForm());
//                Application.Run(new ItemStockOutForm(null,null));
            }
            else
            {
                MessageBox.Show("Program is already running");
                ReleaseMutex(hMutex);
            }
        }
    }
}