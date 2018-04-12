using System;
using System.Drawing;
using System.Windows.Forms;
using BusinessEntity;
using PersistenceLayer;

namespace LedShow
{
    public partial class LedFrm : Form
    {
        readonly string ledNo;
        readonly string ledSeq;
        int xCount = 5;
        int yCount = 5;
        Led led;
        private bool justStart = true;

        public LedFrm(string ledNo)
        {            
            InitializeComponent();
            this.ledNo = ledNo;            
        }

        public LedFrm(string ledNo, string ledSeq) : this(ledNo)
        {
            this.ledSeq = ledSeq;
        }

        private void LedFrm_Load(object sender, EventArgs e)
        {
            Text = ledNo;
            try
            {
                led = new Led(Config.Get(ledNo), Convert.ToInt32(Config.Get("CycleTime")) * 1000);

                InitDb();

                if (!string.IsNullOrEmpty(ledSeq))
                {
                    xCount = Convert.ToInt32(Config.Get("XCount"));
                    yCount = Convert.ToInt32(Config.Get("YCount"));
                    int no = int.Parse(ledSeq);
                    int x = Size.Width * ((no - 1) % xCount) + ((Screen.PrimaryScreen.WorkingArea.Width - Size.Width * xCount) / (xCount + 1) * ((no - 1) % xCount + 1));
                    int y = Size.Height * ((no - 1) / xCount) + ((Screen.PrimaryScreen.WorkingArea.Height - Size.Height * yCount) / (yCount + 1)) * ((no - 1) / xCount + 1);
                    Location = new Point(x, y);
                }
                timer1.Interval = Convert.ToInt32(Config.Get("RefreshTime")) * 1000;
            }
            catch (Exception ex)
            {
                ExForm.Show(ex);
            }
        }

        public static void InitDb()
        {
            try
            {
                Setting.Instance().DatabaseMapFile = "DatabaseMap.xml";
            }
            catch (Exception ex)
            {
                throw new Exception("≥ı ºªØSPL ß∞‹:" + ex.Message);
            }
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            try
            {
                string[] msgs = getMessages();
                if(msgs == null)
                {
                    return;
                }

                if (string.IsNullOrEmpty(msgs[0]) && string.IsNullOrEmpty(msgs[1]) && string.IsNullOrEmpty(msgs[2]) && string.IsNullOrEmpty(msgs[3]))
                {
                    textBox1.Clear();
                    led.Clear();
                    return;
                }

                textBox1.Text = msgs[0] + "\r\n" + msgs[1] + "\r\n" + msgs[2] + "\r\n" + msgs[3];
                led.show(msgs);
                CommonFuncs.WriteLog(ledNo + ": " + msgs[0] + " " + msgs[1] + " " + msgs[2] + " " + msgs[3]);
            }
            catch (Exception ex)
            {
                CommonFuncs.WriteLog(ledNo + ": " + ex.Message);
            }
        }

        private string[] getMessages()
        {
            LedMessage ledMsg = DbAccess.getLedMessageByLedNo(ledNo, justStart);
            justStart = false;
            if (ledMsg != null)
            {
                ledMsg.Processed = "1";
                ledMsg.Save();
            }
            return ledMsg == null ? null : new[] { ledMsg.Message1, ledMsg.Message2, ledMsg.Message3, ledMsg.Message4};
        }
        
        private void LedFrm_FormClosing(object sender, FormClosingEventArgs e)
        {
            led.Clear();
            led.release();
        }
    }

}