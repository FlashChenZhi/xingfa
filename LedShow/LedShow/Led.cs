using System;
using System.Drawing;
using LedShow_Led5k;

namespace LedShow
{
    public class Led
    {
        private const byte address = 0;
        private const int port = 5005;
        
        private readonly string host;        
        
        private readonly int dev;

        public int cycleTime = 5000;
        public int upperScreenMethod = 1;
        public int lowerScreenMethod = 2;
        public int fullScreenMethod = 2;
        public int speed = 1;

        public Led(string host, int cycleTime)
        {
            Led5kSDK.InitSdk(2, 2);

            this.host = host;
            this.cycleTime = cycleTime;

        }

        //public void ShowFullScreen(string text)
        //{
        //    LedFuncs.MakeRoot(LedFuncs.ROOT_PLAY, LedFuncs.SCREEN_COLOR);
        //    LedFuncs.AddLeaf(cycleTime);
        //    Rectangle rect1 = new Rectangle(0, -1, 128, 32);

        //    LedFuncs.AddText(text, ref rect1, fullScreenMethod, speed * 3, 1, "Arial", 24, Color.Red.ToArgb());
        //    LedFuncs.LED_SendToScreen(dev, address, host, port);
        //}

        public void show(string[] text)
        {

            uint hwnd =  Led5kSDK.CreateClient(new byte[] {address}, port, Led5kSDK.bx_5k_card_type.BX_5K1, 10, 0, (hand, err) =>
            {
                Console.WriteLine(string.Format("{0}", err));
            });

            Led5kProgram ledProgarm = new Led5kProgram();

            if (text.Length > 0)
            {
                Led5kstaticArea area = new Led5kstaticArea();

                area.header = getHeader(0, -1, 128, 16,text[0].Length);
                area.text = text[0];
                ledProgarm.m_arealist.Add(area);
            }
            if (text.Length > 1)
            {
                Led5kstaticArea area = new Led5kstaticArea();

                area.header = getHeader(0, 15, 128, 16, text[1].Length);
                area.text = text[1];
                ledProgarm.m_arealist.Add(area);
            }
            if (text.Length > 2)
            {
                Led5kstaticArea area = new Led5kstaticArea();

                area.header = getHeader(0, 31, 128, 16, text[2].Length);
                area.text = text[1];
                ledProgarm.m_arealist.Add(area);
            }
            if (text.Length > 3)
            {
                Led5kstaticArea area = new Led5kstaticArea();

                area.header = getHeader(0, 47, 128, 16, text[3].Length);
                area.text = text[1];
                ledProgarm.m_arealist.Add(area);
            }

            ledProgarm.AreaNum = Convert.ToByte(ledProgarm.m_arealist.Count);
            ledProgarm.overwrite = true;
            ledProgarm.name = "LED";
            ledProgarm.ProgramWeek = 1;
            ledProgarm.IsPlayOnTime = false;
            ledProgarm.IsValidAlways = true;
            ledProgarm.DisplayType = 0;
            ledProgarm.PlayTimes = Convert.ToByte(1);


            int error = ledProgarm.SendProgram(hwnd);

            if (error != 0)
            {
                Console.WriteLine("·¢ËÍ½ÚÄ¿Ê§°Ü");
            }



            //LedFuncs.MakeRoot(LedFuncs.ROOT_PLAY, LedFuncs.SCREEN_COLOR);
            //LedFuncs.AddLeaf(cycleTime);
            //Rectangle rect1 = new Rectangle(0, -1, 128, 16);
            //Rectangle rect2 = new Rectangle(0, 15, 128, 32);

            //int returnValue;
            //if (text.Length > 0)
            //{
            //    returnValue = LedFuncs.AddText(text[0], ref rect1, upperScreenMethod, speed, 1, "Arial", 12, Color.Red.ToArgb());                
            //}
            //if (text.Length > 1)
            //{
            //    returnValue = LedFuncs.AddText(text[1], ref rect2, lowerScreenMethod, speed, 1, "Arial", 12, Color.Red.ToArgb());
            //}
            //returnValue = LedFuncs.LED_SendToScreen(dev, address, host, port);

            Console.WriteLine(error);
        }

        public void Clear()
        {
            //LedFuncs.MakeRoot(LedFuncs.ROOT_PLAY, LedFuncs.SCREEN_COLOR);
            //LedFuncs.AddLeaf(cycleTime);
            //LedFuncs.LED_SendToScreen(dev, address, host, port);
        }

        private Led5kSDK.bx_5k_area_header getHeader(int x,int y,int width,int height,int dataLength)
        {
            Led5kSDK.bx_5k_area_header bx_5k = new Led5kSDK.bx_5k_area_header();
            bx_5k.AreaX = Convert.ToInt16(x);
            bx_5k.AreaX /= 8;
            bx_5k.AreaY = Convert.ToInt16(y);
            bx_5k.AreaWidth = Convert.ToInt16(width);
            bx_5k.AreaWidth /= 8;
            bx_5k.AreaHeight = Convert.ToInt16(height);

            byte[] SingleLine_list = new byte[2];
            SingleLine_list[0] = 0x01;
            SingleLine_list[1] = 0x02;
            int sll = 0;
            bx_5k.SingleLine = SingleLine_list[sll];

            byte[] NewLine_list = new byte[2];
            NewLine_list[0] = 0x01;
            NewLine_list[1] = 0x02;
            int nl = 0;
            bx_5k.NewLine = NewLine_list[nl];

            bx_5k.Lines_sizes = Convert.ToByte(1);

            bx_5k.Reserved1 = 0;
            bx_5k.Reserved2 = 0;
            bx_5k.Reserved3 = 0;


            byte[] DisplayMode_list = new byte[6];
            DisplayMode_list[0] = 0x01;
            DisplayMode_list[1] = 0x02;
            DisplayMode_list[2] = 0x03;
            DisplayMode_list[3] = 0x04;
            DisplayMode_list[4] = 0x05;
            DisplayMode_list[5] = 0x06;
            int dml = 0;
            bx_5k.DisplayMode = DisplayMode_list[dml];

            bx_5k.DynamicAreaLoc = 0xff;
            bx_5k.RunMode = 0;
            bx_5k.Timeout = 0;
            bx_5k.ExitMode = 0x00;

            bx_5k.Speed = (byte)5;

            bx_5k.StayTime = Convert.ToByte(cycleTime/2);


            bx_5k.DataLen = dataLength;

            return bx_5k;
        }

        public void release()
        {
            Led5kSDK.ReleaseSdk();
        }
    }
}
