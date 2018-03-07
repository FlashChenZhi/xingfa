using System;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.WmsRftEt;

namespace wms_rft.Common
{
    public partial class EmptyConfirmEtForm2 : Form
    {
        private MessageHelper msgHelper;
        private emptyInfoRFT emptyInfoRft;

        public EmptyConfirmEtForm2(emptyInfoRFT emptyInfoRft)
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
                    List<string> emptyMessages = new List<string>();
                    if (emptyInfoRft.emptyBag)
                    {
                        emptyMessages.Add("Bag Empty !");
                        emptyMessages.Add(emptyInfoRft.bagNo);
                    }
                    if (emptyInfoRft.emptyBucket)
                    {
                        emptyMessages.Add("Bucket Empty !");
                        emptyMessages.Add(emptyInfoRft.bucketNo);
                    }
                    if (emptyInfoRft.emptyPallet)
                    {
                        emptyMessages.Add("Pallet Empty !");
                    }
                    if (emptyInfoRft.emptyLocation)
                    {
                        emptyMessages.Add("Location Empty !");
                        emptyMessages.Add(emptyInfoRft.locationNo);
                    }

                    StringBuilder displayMessage = new StringBuilder();
                    for (int i = 0; i < emptyMessages.Count; i++)
                    {
                        displayMessage.Append(emptyMessages[i]);
                        if (i + 1 != emptyMessages.Count)
                        {
                            displayMessage.Append("\n");
                        }
                    }                    
                    lblEmptyMessage.Text = displayMessage.ToString();
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
            lblMessage.Text = string.Empty;
        }

        private void btnConfirm_Click(object sender, EventArgs e)
        {
            Close();
        }
    }
}