using System;
using System.Runtime.InteropServices;
using System.Drawing;

namespace LedShow
{
    public class LedFuncs
    {
        //通讯方式常量
        public const int DEVICE_TYPE_COM = 0;
        public const int DEVICE_TYPE_UDP = 1;
        public const int DEVICE_TYPE_MODEM = 2;

        //串行通讯速度常量
        public const int SBR_9600 = 0;
        public const int SBR_14400 = 1;
        public const int SBR_19200 = 2;
        public const int SBR_38400 = 3;
        public const int SBR_57600 = 4;
        public const int SBR_115200 = 5;

        //播放类型常量
        public const int ROOT_PLAY = 17;
        public const int ROOT_DOWNLOAD = 18;
        public const int ROOT_PLAY_EXTEND = 22;

        //显示屏类型常量
        public const int SCREEN_UNICOLOR = 1;
        public const int SCREEN_COLOR = 2;
        public const int SCREEN_FULLCOLOR = 3;
        public const int SCREEN_GRAY = 4;

        //响应消息常量
        public const int LM_RX_COMPLETE = 1;
        public const int LM_TX_COMPLETE = 2;
        public const int LM_RESPOND = 3;
        public const int LM_TIMEOUT = 4;
        public const int LM_NOTIFY = 5;
        public const int LM_PARAM = 6;
        public const int LM_TX_PROGRESS = 7;
        public const int LM_RX_PROGRESS = 8;

        //电源状态常量
        public const int LED_POWER_OFF = 0;
        public const int LED_POWER_ON = 1;

        //时间格式常量
        public const int DF_YMD = 1;                 //年月日  "2004年12月31日"
        public const int DF_HN = 2;                  //时分    "19:20"
        public const int DF_HNS = 3;                 //时分秒  "19:20:30"
        public const int DF_Y = 4;                   //年      "2004"
        public const int DF_M = 5;                   //月      "12" "01" 注意：始终显示两位数字
        public const int DF_D = 6;                   //日
        public const int DF_H = 7;                   //时
        public const int DF_N = 8;                   //分
        public const int DF_S = 9 ;                  //秒
        public const int DF_W = 10;                  //星期    "星期三"

        //正计时、倒计时format参数
        public const int CF_DAY = 0;                 //天数
        public const int CF_HOUR = 1;                //小时数
        public const int CF_HMS = 2;                 //时分秒
        public const int CF_HM = 3;                  //时分
        public const int CF_MS = 4;                  //分秒
        public const int CF_S = 5;                   //秒

        public const int FONT_SET_16 = 0;            //16点阵字符
        public const int FONT_SET_24 = 1;            //24点阵字符

        public const int PKC_QUERY = 4;
        public const int PKC_ADJUST_TIME = 6;
        public const int PKC_GET_POWER = 9;
        public const int PKC_SET_POWER = 10;
        public const int PKC_GET_BRIGHT = 11;
        public const int PKC_SET_BRIGHT = 12;


        [DllImport("LEDSender.dll", EntryPoint = "LED_Cleanup", CallingConvention = CallingConvention.StdCall)]
        public static extern void LED_Cleanup();

        [DllImport("LEDSender.dll", EntryPoint = "LED_Open", CallingConvention = CallingConvention.StdCall)]
        public static extern int LED_Open(ref DEVICEPARAM device, int notify, int window, int message);

        [DllImport("LEDSender.dll", EntryPoint = "LED_SendToScreen", CallingConvention = CallingConvention.StdCall)]
        public static extern int LED_SendToScreen(int dev, Byte Address, string Host, int port);

        [DllImport("LEDSender.dll", EntryPoint = "LED_SetPower", CallingConvention = CallingConvention.StdCall)]
        public static extern int LED_SetPower(int dev, Byte Address,string Host, int port, int Power);

        [DllImport("LEDSender.dll", EntryPoint = "MakeRoot", CallingConvention = CallingConvention.StdCall)]
        public static extern int MakeRoot(int RootType, int ScreenType);

        [DllImport("LEDSender.dll", EntryPoint = "AddLeaf", CallingConvention = CallingConvention.StdCall)]
        public static extern int AddLeaf(int DisplayTime);

        [DllImport("LEDSender.dll", EntryPoint = "AddString", CallingConvention = CallingConvention.StdCall)]
        public static extern int AddString(string str, ref Rectangle rect, int method, int speed, int transparent, int fontset, int fontcolor);

        [DllImport("LEDSender.dll", EntryPoint = "AddText", CallingConvention = CallingConvention.StdCall)]
        public static extern int AddText(string str, ref Rectangle rect, int method, int speed, int transparent, string fontname, int fontsize, int fontcolor);
    }
    [StructLayout(LayoutKind.Sequential)]
    public unsafe struct DEVICEPARAM
    {
        public int devType;                  //设备类型
        public int speed;                    //串口通讯速度选择
        public int ComPort;                  //串口号
        public int FlowCon;                 //流控制
        public int locPort;                  //本地端口号
        public int[] reserve;
    }
}
