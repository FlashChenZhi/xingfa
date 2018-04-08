using System;
using System.Drawing;

namespace LedShow
{
    public class Led
    {
        private const byte address = 0;
        private const int port = 6666;
        
        private readonly string host;        
        
        private readonly int dev;

        public int cycleTime = 5000;
        public int upperScreenMethod = 1;
        public int lowerScreenMethod = 2;
        public int fullScreenMethod = 2;
        public int speed = 1;

        public Led(string host, int cycleTime)
        {
            DEVICEPARAM devParam = new DEVICEPARAM { devType = LedFuncs.DEVICE_TYPE_UDP };
            this.host = host;
            this.cycleTime = cycleTime;

            dev = LedFuncs.LED_Open(ref devParam, 0, 0, 1025);
            if (dev == -1)
            {
                throw new Exception("³õÊ¼»¯LEDÊ§°Ü£¡");
            }
        }

        public void ShowFullScreen(string text)
        {
            LedFuncs.MakeRoot(LedFuncs.ROOT_PLAY, LedFuncs.SCREEN_COLOR);
            LedFuncs.AddLeaf(cycleTime);
            Rectangle rect1 = new Rectangle(0, -1, 128, 32);

            LedFuncs.AddText(text, ref rect1, fullScreenMethod, speed * 3, 1, "Arial", 24, Color.Red.ToArgb());
            LedFuncs.LED_SendToScreen(dev, address, host, port);
        }

        public void show(string[] text)
        {
            LedFuncs.MakeRoot(LedFuncs.ROOT_PLAY, LedFuncs.SCREEN_COLOR);
            LedFuncs.AddLeaf(cycleTime);
            Rectangle rect1 = new Rectangle(0, -1, 128, 16);
            Rectangle rect2 = new Rectangle(0, 15, 128, 32);

            int returnValue;
            if (text.Length > 0)
            {
                returnValue = LedFuncs.AddText(text[0], ref rect1, upperScreenMethod, speed, 1, "Arial", 12, Color.Red.ToArgb());                
            }
            if (text.Length > 1)
            {
                returnValue = LedFuncs.AddText(text[1], ref rect2, lowerScreenMethod, speed, 1, "Arial", 12, Color.Red.ToArgb());
            }
            returnValue = LedFuncs.LED_SendToScreen(dev, address, host, port);

            Console.WriteLine(returnValue);
        }

        public void Clear()
        {
            LedFuncs.MakeRoot(LedFuncs.ROOT_PLAY, LedFuncs.SCREEN_COLOR);
            LedFuncs.AddLeaf(cycleTime);
            LedFuncs.LED_SendToScreen(dev, address, host, port);
        }
    }
}
