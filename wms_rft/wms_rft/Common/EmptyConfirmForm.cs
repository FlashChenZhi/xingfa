using System;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.WmsRftSmart;

namespace wms_rft.Common
{
    public partial class EmptyConfirmForm : Form
    {
        private MessageHelper msgHelper;
        private emptyInfoRFT emptyInfoRft;

        public EmptyConfirmForm(emptyInfoRFT emptyInfoRft)
        {
            InitializeComponent();
            this.emptyInfoRft = emptyInfoRft;
        }

        private void EmptyConfirmForm_Load(object sender, EventArgs e)
        {
            try
            {
                clearAll();
                msgHelper = new MessageHelper(lblMessage);

                if (emptyInfoRft != null)
                {
                    lblEmptyMessage.Text = emptyInfoRft.displayMessage;
                    lblAreaName.Text = emptyInfoRft.areaName;
                    lblLocationNo.Text = emptyInfoRft.locationNo;
                }
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void clearAll()
        {
            lblEmptyMessage.Text = string.Empty;
            lblAreaName.Text = string.Empty;
            lblLocationNo.Text = string.Empty;
            lblMessage.Text = string.Empty;
        }

        private void btnConfirm_Click(object sender, EventArgs e)
        {
            Close();
        }
    }
}