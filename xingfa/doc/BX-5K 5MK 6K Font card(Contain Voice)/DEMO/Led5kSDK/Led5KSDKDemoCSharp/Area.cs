using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using ONNONLed5KSDKD;

namespace Led5KSDKDemoCSharp
{
    public partial class Area : Form
    {
        public Area()
        {
            InitializeComponent();
            comboBox1.SelectedIndex = 0;
            comboBox2.SelectedIndex = 0;
            comboBox3.SelectedIndex = 0;
            comboBox4.SelectedIndex = 0;
            comboBox5.SelectedIndex = 0;
        }
            
        //public uint i;
        public static byte[] AreaText;
       
        public Led5kSDK.bx_5k_area_header bx_5k;
        private void button1_Click(object sender, EventArgs e)
        {
            bx_5k.AreaType = 0x06;
            bx_5k.AreaX = Convert.ToInt16(textBox1.Text);
            bx_5k.AreaX /= 8;
            bx_5k.AreaY = Convert.ToInt16(textBox2.Text);
            bx_5k.AreaWidth = Convert.ToInt16(textBox3.Text);
            bx_5k.AreaWidth /= 8;
            bx_5k.AreaHeight = Convert.ToInt16(textBox4.Text);

            bx_5k.Lines_sizes = Convert.ToByte(textBox5.Text);

            byte[] RunMode_list = new byte[3];
            RunMode_list[0] = 0;
            RunMode_list[1] = 1;
            RunMode_list[2] = 2;
            int rl = comboBox3.SelectedIndex;
            bx_5k.RunMode = RunMode_list[rl];
            //bx_5k.RunMode = Convert.ToByte(comboBox3.SelectedIndex+1);

            bx_5k.Timeout = Convert.ToInt16(textBox7.Text);


            bx_5k.Reserved1 = 0;
            bx_5k.Reserved2 = 0;
            bx_5k.Reserved3 = 0;

            byte[] SingleLine_list = new byte[2];
            SingleLine_list[0] = 0x01;
            SingleLine_list[1] = 0x02;
            int sll = comboBox1.SelectedIndex;
            bx_5k.SingleLine = SingleLine_list[sll];
            //bx_5k.SingleLine = Convert.ToByte(comboBox1.SelectedIndex);

            byte []NewLine_list=new byte[2];
            NewLine_list[0] = 0x01;
            NewLine_list[1] = 0x02;
            int nl = comboBox2.SelectedIndex;
            bx_5k.NewLine = NewLine_list[nl];
            //bx_5k.NewLine = Convert.ToByte(comboBox2.SelectedIndex);


            byte[] DisplayMode_list=new byte[6];
            DisplayMode_list[0] = 0x01;
            DisplayMode_list[1] = 0x02;
            DisplayMode_list[2] = 0x03;
            DisplayMode_list[3] = 0x04;
            DisplayMode_list[4] = 0x05;
            DisplayMode_list[5] = 0x06;
            int dml = comboBox4.SelectedIndex;
            bx_5k.DisplayMode = DisplayMode_list[dml];
            //bx_5k.DisplayMode = Convert.ToByte(comboBox4.SelectedIndex);

            bx_5k.ExitMode = 0x00;


            bx_5k.Speed =(byte) comboBox5.SelectedIndex;
            //bx_5k.Speed=Convert.ToByte(comboBox5.SelectedIndex);

            bx_5k.StayTime = Convert.ToByte(textBox8.Text);

            List<byte[]> Byte_Area = new List<byte[]>();
            int Byte_t = 0;
            string[] str = textBox6.Text.Trim().Split('\\');
            for (int i = 0; i < str.Length; i++) 
            {
                if (str[i].Length > 5)
                {
                    if (str[i].Substring(0, 2).Equals("FK"))
                    {
                        str[i] = str[i].Remove(0, 5);
                        byte[] sss = System.Text.Encoding.Unicode.GetBytes(str[i]);
                        for (int k = 0; k < sss.Length/2; k++) 
                        {
                            byte a =sss[k*2];
                            sss[k*2]=sss[k*2+1];
                            sss[k * 2 + 1] = a;
                        }
                        Byte_Area.Add(sss);
                        Byte_t += sss.Length;
                    }
                    else if (str[i].Substring(0, 2).Equals("FE") || str[i].Substring(0, 2).Equals("FO"))
                    {
                        str[i]= "\\"+str[i];
                        byte[] sss = System.Text.Encoding.Default.GetBytes(str[i]);
                        Byte_Area.Add(sss);
                        Byte_t += sss.Length;
                    }
                }
                else
                {
                    if (str[i+1].Substring(0, 2).Equals("FK"))
                    {
                        str[i] += "\\" + str[i + 1].Substring(0, 5);
                    } 
                    byte[] sss = System.Text.Encoding.Default.GetBytes(str[i]);
                    Byte_Area.Add(sss);
                    Byte_t += sss.Length;
                }
            }
            byte[] nn = new byte[Byte_t];
            int g=0;
            for (int i = 0; i < Byte_Area.Count(); i++) 
            {
                if (i > 0) 
                {
                    for (int j = 0; j < Byte_Area[i].Length; j++) 
                    {
                        nn[g + j] = Byte_Area[i][j];
                    }
                    g += Byte_Area[i].Length;
                }
                else
                {
                    for (int j = 0; j < Byte_Area[i].Length; j++)
                    {
                        nn[j] = Byte_Area[i][j];
                    }
                    g += Byte_Area[i].Length;
                }
            }
            AreaText = nn;
            bx_5k.DataLen = AreaText.Length;
            this.Close();
        }
        private void Area_Load(object sender, EventArgs e)
        {
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Close();
            this.Dispose();
        }
    }
}
