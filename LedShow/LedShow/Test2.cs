using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace LedShow
{
    public partial class Test2 : Form
    {
        string stationNo;
        string ledNo;
        string ip;
        int xCount = 5;
        int yCount = 5;
        Led led;

        public Test2(string stationNo)
        {
            InitializeComponent();
            this.stationNo = stationNo;
            ip = GetIp();
            led = new Led(ip, 0);

            textBox1.Text = ip + "-------" + stationNo;
        }

        public Test2(string stationNo, string ledNo)
        {
            InitializeComponent();
            this.stationNo = stationNo;
            this.ledNo = ledNo;
            ip = GetIp();
            led = new Led(ip, 0);

            textBox1.Text = ip + "-------" + stationNo;
        }

        private string GetIp()
        {
            return Config.Get(stationNo);
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            string[] texts = new string[4];
            texts[0] = texts[3] = textBox1.Text;
            texts[1] = texts[2] = textBox2.Text;
            try
            {
                led.show(texts);
            }
            catch (Exception ex)
            {
                ExForm.Show(ex);
            }
        }

        private void Test2_Load(object sender, EventArgs e)
        {
            this.Text = stationNo;
            timer1.Interval = Convert.ToInt32(Config.Get("RefreshTime")) * 1000;
            try
            {
                if (!string.IsNullOrEmpty(ledNo))
                {
                    this.xCount = Convert.ToInt32(Config.Get("XCount"));
                    this.yCount = Convert.ToInt32(Config.Get("YCount"));
                    int no = int.Parse(ledNo);
                    int x = this.Size.Width * ((no - 1) % xCount) + ((Screen.PrimaryScreen.WorkingArea.Width - this.Size.Width * xCount) / (xCount + 1) * ((no - 1) % xCount + 1));
                    int y = this.Size.Height * ((no - 1) / xCount) + ((Screen.PrimaryScreen.WorkingArea.Height - this.Size.Height * yCount) / (yCount + 1)) * ((no - 1) / xCount + 1);
                    this.Location = new Point(x, y);
                }
                //Write2Rom();
            }
            catch (Exception ex)
            {
                ExForm.Show(ex);
            }
        }

        private void Write2Rom()
        {
            string[] texts = new string[2];
            texts[0] = "               !欢迎光临!";
            texts[1] = "            !厦门AUO自动仓库!";
            try
            {
                led.show(texts);
            }
            catch (Exception ex)
            {
                ExForm.Show(ex);
            }
        }
    }
}