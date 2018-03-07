using System;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.StockRegist
{
    public partial class WavesCommunicatingForm : Form
    {
        private string ticketNo;
        private DateTime startTime;
        private int timeout;
        public destinationInstructionRFT instruction;
        private MessageHelper msgHelper;

        public WavesCommunicatingForm(string ticketNo)
        {
            InitializeComponent();
            this.ticketNo = ticketNo;
        }

        private void WavesCommunicatingForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);

            startTime = DateTime.Now;
            timeout = 30;
            timer1.Enabled = true;
            
            msgHelper = new MessageHelper(lblMessage);
            msgHelper.showInfo("通讯中");
        }

        private void btnInterrupt_Click(object sender, EventArgs e)
        {
            timer1.Enabled = false;
            Close();
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            try
            {
                instruction = ServiceFactorySmart.getCurrentService().getNotProcessedDestinationInstructionByTicketNo(ticketNo);


                if (instruction != null)
                {
                    btnInterrupt_Click(null, null);
                }
                else
                {
                    if ((DateTime.Now - startTime).TotalSeconds > timeout)
                    {
                        timer1.Enabled = false;
                        ServiceFactorySmart.getCurrentService().printWavesTimeOutLabel2(ticketNo);
                        msgHelper.showError("time out");
                    }
                }
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }

        }

        private void WavesCommunicatingForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    btnInterrupt.Focus();
                }
                else if (e.KeyCode == Keys.Up)
                {
                    btnInterrupt.Focus();
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnInterrupt_Click(btnInterrupt, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnInterrupt.Focused)
                    {
                        btnInterrupt_Click(btnInterrupt, eventArgs);
                    }                    
                }
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }
    }
}