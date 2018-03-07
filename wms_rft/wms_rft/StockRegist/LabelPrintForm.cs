using System;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.Helper.Smart;

namespace wms_rft.StockRegist
{
    public partial class LabelPrintForm : Form
    {
        private string ticketNo;
        private string bucketNo;
        private MessageHelper msgHelper;

        public LabelPrintForm(string ticketNo, string bucketNo)
        {
            InitializeComponent();

            this.ticketNo = ticketNo;
            this.bucketNo = bucketNo;

            lblMessage.Text = string.Empty;

            msgHelper = new MessageHelper(lblMessage);
        }

        private void btnConfirm_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void btnReissue_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                ServiceFactorySmart.getCurrentService().reprintBucketCarryingInstrction1_11(ticketNo, bucketNo);

                msgHelper.showInfo("reissue ok");
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void LabelPrintForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (btnReissue.Focused)
                    {
                        btnConfirm.Focus();
                    }
                    else if (btnConfirm.Focused)
                    {
                        btnReissue.Focus();
                    }                  
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (btnReissue.Focused)
                    {
                        btnConfirm.Focus();
                    }
                    else if (btnConfirm.Focused)
                    {
                        btnReissue.Focus();
                    }  
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnConfirm_Click(btnConfirm, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnReissue.Focused)
                    {
                        btnReissue_Click(btnReissue, eventArgs);
                    }
                    else if (btnConfirm.Focused)
                    {
                        btnConfirm_Click(btnConfirm, eventArgs);
                    }                   
                }
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void LabelPrintForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);
        }        
    }
}