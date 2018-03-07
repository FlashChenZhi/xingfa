using System;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.WmsRftEt;

namespace wms_rft.Et
{
    public partial class EtRecoveryForm : Form
    {
//        private BarcodeScanner barcodeScanner;
//        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        public EtRecoveryForm()
        {
            InitializeComponent();
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void EtRecoveryForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.ET);

            clearAll();
            msgHelper = new MessageHelper(lblMessage);

//            try
//            {
//                initializeBarcodeScanner();
//            }
//            catch (Exception ex)
//            {
//                msgHelper.showError(ex.Message);
//            }
        }

        private void clearAll()
        {
            txtBcrNo.Text = string.Empty;
            lblLine1.Text = string.Empty;
            lblLine2.Text = string.Empty;
            lblLine3.Text = string.Empty;
            lblBucketNo.Text = string.Empty;
            lblItemCode.Text = string.Empty;
            lblItemName1.Text = string.Empty;
            lblItemName2.Text = string.Empty;
            lblItemName3.Text = string.Empty;
            lblColorCode.Text = string.Empty;
            lblQty.Text = string.Empty;
            lblWeight.Text = string.Empty;
            lblGroupNo.Text = string.Empty;
            lblMessage.Text = string.Empty;
        }

//        private void initializeBarcodeScanner()
//        {
//            barcodeScanner = BarcodeScannerFacade.GetBarcodeScanner();
//            barcodeScanner.BarcodeScan += barcodeScanner_BarcodeScan;
//        }

//        void barcodeScanner_BarcodeScan(object sender, BarcodeScannerEventArgs e)
//        {
//            Invoke(new setBarcodeDelegate(setBarcode), e.Data, e.Type);
//        }

//        private void setBarcode(string data, string type)
//        {
//        }

        private void EtRecoveryForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
//            try
//            {
//                disposeBarcodeScanner();
//            }
//            catch (Exception ex)
//            {
//                msgHelper.showError(ex.Message);
//            }
        }

//        private void disposeBarcodeScanner()
//        {
//            if (barcodeScanner != null)
//            {
//                barcodeScanner.Dispose();
//            }
//        }

        private void showPage()
        {
            lblLine1.Text = string.Empty;
            lblLine2.Text = string.Empty;
            lblLine3.Text = string.Empty;
            lblBucketNo.Text = string.Empty;
            lblItemCode.Text = string.Empty;
            lblItemName1.Text = string.Empty;
            lblItemName2.Text = string.Empty;
            lblItemName3.Text = string.Empty;
            lblColorCode.Text = string.Empty;
            lblQty.Text = string.Empty;
            lblWeight.Text = string.Empty;
            lblGroupNo.Text = string.Empty;
        }

        private void btnPrev_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();
                showPage();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnNext_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();
                showPage();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnRetry_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnSubmit_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void BucketOrBagChangeForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBcrNo.Focused)
                    {
                        btnPrev.Focus();
                    }
                    else if (btnPrev.Focused)
                    {
                        btnNext.Focus();
                    }
                    else if (btnNext.Focused)
                    {
                        btnRetry.Focus();
                    }
                    else if (btnRetry.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        btnMenu.Focus();
                    }
                    else if (btnMenu.Focused)
                    {
                        txtBcrNo.SelectAll();
                        txtBcrNo.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (txtBcrNo.Focused)
                    {
                        btnMenu.Focus();
                    }
                    else if (btnMenu.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        btnRetry.Focus();
                    }
                    else if (btnRetry.Focused)
                    {
                        btnNext.Focus();
                    }
                    else if (btnNext.Focused)
                    {
                        btnPrev.Focus();
                    }
                    else if (btnPrev.Focused)
                    {
                        txtBcrNo.SelectAll();
                        txtBcrNo.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnMenu_Click(btnMenu, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnPrev.Focused)
                    {
                        btnPrev_Click(btnPrev, eventArgs);
                    }
                    else if (btnNext.Focused)
                    {
                        btnNext_Click(btnNext, eventArgs);
                    }
                    else if (btnRetry.Focused)
                    {
                        btnRetry_Click(btnRetry, eventArgs);
                    }
                    else if (btnSubmit.Focused)
                    {
                        btnSubmit_Click(btnSubmit, eventArgs);
                    }
                    else if (btnMenu.Focused)
                    {
                        btnMenu_Click(btnMenu, eventArgs);
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