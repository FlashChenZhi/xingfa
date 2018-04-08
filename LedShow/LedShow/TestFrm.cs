using System;
using System.Windows.Forms;

namespace LedShow
{
    unsafe public partial class TestFrm : Form
    {
        Led led;
        //Byte address;

        public TestFrm()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
        }

        private void button3_Click(object sender, EventArgs e)
        {
       
        }

        private void button4_Click(object sender, EventArgs e)
        {
            led.cycleTime = Convert.ToInt32(timeBox.Value * 1000);
            led.speed = Convert.ToInt32(speedBox.Value);
            led.upperScreenMethod = Convert.ToInt32(methodBox.Value);
            led.lowerScreenMethod = Convert.ToInt32(methodBox.Value);
            string[] texts = new string[4];
            texts[0] = textBox1.Text.Substring(0, 20);
            texts[1] = textBox1.Text.Substring(20, 20);
            texts[2] = textBox1.Text.Substring(40, 20);
            texts[3] = textBox1.Text.Substring(60, 20);
            led.show(texts);
        }

        private void button5_Click(object sender, EventArgs e)
        {

        }

        private void TestFrm_Load(object sender, EventArgs e)
        {
            led = new Led("172.1.191.51",0);
        }

        private void button2_Click(object sender, EventArgs e)
        {

        }

  //Dim ARect As rect
  
  //Timer1.Enabled = False
  //MakeRoot ROOT_PLAY, SCREEN_COLOR    '形成发送数据
  //AddLeaf 1000                           '追加一个页面
  //SetRect ARect, 0, -2, Picture1.ScaleWidth, Picture1.ScaleHeight
  //'AddWindow Picture1.hDC, Picture1.ScaleWidth, ScaleHeight, ARect, 1, 1, 1  '发送一幅图片
  //AddText "ABCDEFGHIJKL", ARect, 1, 1, 1, "Arial", 12, RGB(255, 255, 0)
  //Me.Caption = "正在发送数据..."
  //LED_SendToScreen dev, Address, "192.168.0.110", 6666
    }
}