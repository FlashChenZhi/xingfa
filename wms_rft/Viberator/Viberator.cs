using System;
using CalibApi = Calib.SystemLibNet.Api;
using CalibDef = Calib.SystemLibNet.Def;

namespace Viberator
{
    public class Viberator
    {       
        public static void testFn()
        {
            CalibApi.SysSetFnKeyLock(false);            
        }

        public static void viberate()
        {
            try
            {
                CalibApi.SysPlayVibrator(CalibDef.B_ALARM, 0, 0, 0);
//                CalibApi.SysSetFnKeyLock(true);
            }
            catch (Exception)
            {
                //ignore exception               
            }
        }

        public static void stop()
        {


            try
            {
                CalibApi.SysPowerOff();

            }
            catch (Exception)
            {
                //ignore exception               
            }
        }
    }
}
