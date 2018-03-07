using System;
using System.Windows.Forms;
using wms_rft.Helper;

namespace wms_rft.Common
{
    public partial class CompleteConfirmForm : Form
    {
        private MessageHelper msgHelper;

        public CompleteConfirmForm()
        {
            InitializeComponent();
        }

        private void CompleteConfirmForm_Load(object sender, EventArgs e)
        {
            try
            {
                clearAll();
                msgHelper = new MessageHelper(lblMessage);               
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void clearAll()
        {
            lblMessage.Text = string.Empty;
        }

        private void btnConfirm_Click(object sender, EventArgs e)
        {
            Close();
        }
    }
}