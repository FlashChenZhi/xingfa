using Microsoft.Win32;

namespace wms_rft.Helper
{
    public class RegistryHelper
    {
        public static void registWebserviceServer(string systemCode)
        {
            byte[] ipaddr = systemCode == Const.SystemCode.SMART ? 
                new byte[] { 10, 244, 164, 1 } : 
                new byte[] { 10, 244, 164, 251 };

            RegistryKey key = Registry.LocalMachine.CreateSubKey("Comm").CreateSubKey("Tcpip").CreateSubKey("Hosts").CreateSubKey("webservice-server");
            key.SetValue("ipaddr", ipaddr, RegistryValueKind.Binary);
        }
    }
}
