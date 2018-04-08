using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace LedShow_Led5k
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
            Led5kSDK.InitSdk(2, 2);
        }

        private void button1_Click(object sender, EventArgs e)
        {
            uint hwnd = Led5kSDK.CreateClient(new byte[] { 1 }, 80, Led5kSDK.bx_5k_card_type.BX_5K1, 30, 0, (hand, err) =>
            {
                MessageBox.Show(string.Format("{0}", err));
            });
            Led5kSDK.OFS_SendFileData()
        }

        private void Form1_FormClosing(object sender, FormClosingEventArgs e)
        {
            Led5kSDK.ReleaseSdk();
        }
    }
}
