using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace LedShow
{
    public partial class ExForm : Form
    {
        private Exception ex;
        public static void Show(Exception ex)
        {
            ExForm frm = new ExForm(ex);
            frm.StartPosition = FormStartPosition.CenterScreen;
            frm.Show();
        }
        public static void Show(Exception ex, Form parent)
        {
            ExForm frm = new ExForm(ex);
            frm.StartPosition = FormStartPosition.CenterScreen;
            frm.ShowDialog(parent);
        }

        public ExForm()
        {
            InitializeComponent();
        }
        public ExForm(Exception ex)
        {

            InitializeComponent();
            this.ex = ex;
            exBox.Text = ex.Message;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            MessageBox.Show(ex.Source);
        }
    }
}