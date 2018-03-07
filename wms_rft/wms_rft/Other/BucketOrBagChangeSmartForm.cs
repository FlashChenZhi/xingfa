using System;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.Other
{
    public partial class BucketOrBagChangeSmartForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        //        private bucketInfoRFT bucketInfoRft;
        private stockRFT[] stockRfts;
        private int currentPageNo = 0;

        public BucketOrBagChangeSmartForm()
        {
            InitializeComponent();
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void BucketOrBagChangeForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);

            clearAll();
            msgHelper = new MessageHelper(lblMessage);

            try
            {
                initializeBarcodeScanner();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void clearAll()
        {
            txtOriginalBucketNoOrBagNo.Text = string.Empty;
            lblBagNo.Text = string.Empty;
            lblPageNo.Text = string.Empty;
            lblPrNo.Text = string.Empty;
            lblItemCode.Text = string.Empty;
            lblItemName1.Text = string.Empty;
            lblItemName2.Text = string.Empty;
            lblItemName3.Text = string.Empty;
            lblColorCode.Text = string.Empty;
            lblQty.Text = string.Empty;
            lblWeight.Text = string.Empty;
            txtNewBucketNoOrBagNo.Text = string.Empty;
            lblMessage.Text = string.Empty;

            stockRfts = null;
            currentPageNo = 0;
        }

        private void initializeBarcodeScanner()
        {
            barcodeScanner = BarcodeScannerFacade.GetBarcodeScanner();
            barcodeScanner.BarcodeScan += barcodeScanner_BarcodeScan;
        }

        void barcodeScanner_BarcodeScan(object sender, BarcodeScannerEventArgs e)
        {
            Invoke(new setBarcodeDelegate(setBarcode), e.Data, e.Type);
        }

        private void setBarcode(string data, string type)
        {
            if (txtOriginalBucketNoOrBagNo.Focused)
            {
                txtOriginalBucketNoOrBagNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtOriginalBucketNoOrBagNo.SelectAll();
                txtOriginalBucketNoOrBagNo.Focus();
                txtOriginalBucketNoOrBagNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
            else if (txtNewBucketNoOrBagNo.Focused)
            {
                txtNewBucketNoOrBagNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtNewBucketNoOrBagNo.SelectAll();
                txtNewBucketNoOrBagNo.Focus();
                txtNewBucketNoOrBagNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
        }

        private void BucketOrBagChangeForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            try
            {
                disposeBarcodeScanner();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void disposeBarcodeScanner()
        {
            if (barcodeScanner != null)
            {
                barcodeScanner.Dispose();
            }
        }

        private void txtOriginalBucketNoOrBagNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();

                    string temp = txtOriginalBucketNoOrBagNo.Text.Trim();

                    clearAll();
                    txtOriginalBucketNoOrBagNo.Text = temp;

                    //                    if (!CommonHelper.isBucketNo(temp) && !CommonHelper.isBagNo(temp))
                    if (temp.Length != txtOriginalBucketNoOrBagNo.MaxLength)
                    {
                        msgHelper.showWarning("invalid original bucket no or bag no");

                        txtOriginalBucketNoOrBagNo.SelectAll();
                        txtOriginalBucketNoOrBagNo.Focus();
                        return;
                    }

                    if (CommonHelper.isBagNo(temp))
                    {
                        stockRFT stockRft = ServiceFactorySmart.getCurrentService().getStockInfoByBagNoForBagInquiry(temp);
                        if (stockRft != null)
                        {
                            stockRfts = new stockRFT[1];
                            stockRfts[0] = stockRft;
                        }
                    }
                    else
                    {
                        bucketInfoRFT bucketInfoRft = ServiceFactorySmart.getCurrentService().getBucketInfoByBucketNoForBucketInquiry(temp);
                        stockRfts = bucketInfoRft.stockRfts;
                    }

                    currentPageNo = 1;

                    showPage();

                    txtNewBucketNoOrBagNo.SelectAll();
                    txtNewBucketNoOrBagNo.Focus();
                }
                catch (Exception ex)
                {
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void showPage()
        {
            if (stockRfts == null || stockRfts.Length == 0)
            {
                currentPageNo = 0;
            }
            else
            {
                currentPageNo = Math.Max(currentPageNo, 1);
            }

            if (currentPageNo == 0)
            {
                clearAll();
                return;
            }

            currentPageNo = Math.Min(currentPageNo, stockRfts.Length);

            stockRFT stockRft = stockRfts[currentPageNo - 1];

            lblBagNo.Text = stockRft.bagNo;
            lblPrNo.Text = stockRft.ticketNo;
            lblItemCode.Text = stockRft.itemCode;
            lblItemName1.Text = stockRft.itemName1;
            lblItemName2.Text = stockRft.itemName2;
            lblItemName3.Text = stockRft.itemName3;
            lblColorCode.Text = stockRft.colorCode;
            lblQty.Text = stockRft.qty.ToString("0");
            lblWeight.Text = (stockRft.qty * stockRft.unitWeight).ToString("0.####");

            lblPageNo.Text = currentPageNo.ToString() + "/" + stockRfts.Length;
        }

        private void btnPrev_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (stockRfts == null || stockRfts.Length == 0)
                {
                    return;
                }

                currentPageNo--;
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

                if (stockRfts == null || stockRfts.Length == 0)
                {
                    return;
                }

                currentPageNo++;
                showPage();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void txtNewBucketNoOrBagNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();

                    string temp = txtNewBucketNoOrBagNo.Text.Trim();
                    if (!CommonHelper.isBucketNo(temp) && !CommonHelper.isBagNo(temp))
                    {
                        msgHelper.showWarning("invalid new bucket no or bag no");

                        txtNewBucketNoOrBagNo.SelectAll();
                        txtNewBucketNoOrBagNo.Focus();
                        return;
                    }

                    btnSubmit_Click(null, null);
                }
                catch (Exception ex)
                {
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void btnSubmit_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                string originalTemp = txtOriginalBucketNoOrBagNo.Text.Trim();
                //                if (!CommonHelper.isBucketNo(originalTemp) && !CommonHelper.isBagNo(originalTemp))
                if (originalTemp.Length != txtOriginalBucketNoOrBagNo.MaxLength)
                {
                    msgHelper.showWarning("invalid original bucket no or bag no");

                    txtOriginalBucketNoOrBagNo.SelectAll();
                    txtOriginalBucketNoOrBagNo.Focus();
                    return;
                }

                string newTemp = txtNewBucketNoOrBagNo.Text.Trim();
                if (!CommonHelper.isBucketNo(newTemp) && !CommonHelper.isBagNo(newTemp))
                {
                    msgHelper.showWarning("invalid new bucket no or bag no");

                    txtNewBucketNoOrBagNo.SelectAll();
                    txtNewBucketNoOrBagNo.Focus();
                    return;
                }

                if (CommonHelper.isBagNo(originalTemp) && CommonHelper.isBagNo(newTemp))
                {
                    ServiceFactorySmart.getCurrentService().changeBagNo(originalTemp, newTemp);
                }
                //                else if (CommonHelper.isBucketNo(originalTemp) && CommonHelper.isBucketNo(newTemp))
                else if (!CommonHelper.isBagNo(originalTemp) && CommonHelper.isBucketNo(newTemp))
                {
                    ServiceFactorySmart.getCurrentService().changeBucketNo(originalTemp, newTemp);
                }
                else
                {
                    msgHelper.showWarning("container type mismatch");
                    return;
                }
                //                if (originalTemp.StartsWith("71")
                //                    || originalTemp.StartsWith("72")
                //                    || originalTemp.StartsWith("91")
                //                    || originalTemp.StartsWith("92"))
                //                {
                //                    if (newTemp.StartsWith("71")
                //                        || newTemp.StartsWith("72")
                //                        || newTemp.StartsWith("91")
                //                        || newTemp.StartsWith("92"))
                //                    {
                //                        ServiceFactorySmart.getCurrentService().changeBucketNo(originalTemp, newTemp);
                //                    }
                //                    else
                //                    {
                //                        msgHelper.showWarning("container type mismatch");
                //                        return;
                //                    }
                //                }
                //                else if (!newTemp.StartsWith("71")
                //                         && !newTemp.StartsWith("72")
                //                         && !newTemp.StartsWith("91")
                //                         && !newTemp.StartsWith("92"))
                //                {
                //                    ServiceFactorySmart.getCurrentService().changeBagNo(originalTemp, newTemp);
                //                }
                //                else
                //                {
                //                    msgHelper.showWarning("container type mismatch");
                //                    return;
                //                }

                clearAll();
                msgHelper.showInfo("submit ok");

                txtOriginalBucketNoOrBagNo.SelectAll();
                txtOriginalBucketNoOrBagNo.Focus();
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
                    if (txtOriginalBucketNoOrBagNo.Focused)
                    {
                        btnPrev.Focus();
                    }
                    else if (btnPrev.Focused)
                    {
                        btnNext.Focus();
                    }
                    else if (btnNext.Focused)
                    {
                        txtNewBucketNoOrBagNo.SelectAll();
                        txtNewBucketNoOrBagNo.Focus();
                    }
                    else if (txtNewBucketNoOrBagNo.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        btnMenu.Focus();
                    }
                    else if (btnMenu.Focused)
                    {
                        txtOriginalBucketNoOrBagNo.SelectAll();
                        txtOriginalBucketNoOrBagNo.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (txtOriginalBucketNoOrBagNo.Focused)
                    {
                        btnMenu.Focus();
                    }
                    else if (btnMenu.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        txtNewBucketNoOrBagNo.SelectAll();
                        txtNewBucketNoOrBagNo.Focus();
                    }
                    else if (txtNewBucketNoOrBagNo.Focused)
                    {
                        btnNext.Focus();
                    }
                    else if (btnNext.Focused)
                    {
                        btnPrev.Focus();
                    }
                    else if (btnPrev.Focused)
                    {
                        txtOriginalBucketNoOrBagNo.SelectAll();
                        txtOriginalBucketNoOrBagNo.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnMenu_Click(btnMenu, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));


                    if (txtOriginalBucketNoOrBagNo.Focused)
                    {
                        txtOriginalBucketNoOrBagNo_KeyPress(txtOriginalBucketNoOrBagNo, eventArgs);
                    }
                    else if (btnPrev.Focused)
                    {
                        btnPrev_Click(btnPrev, eventArgs);
                    }
                    else if (btnNext.Focused)
                    {
                        btnNext_Click(btnNext, eventArgs);
                    }
                    else if (txtNewBucketNoOrBagNo.Focused)
                    {
                        txtNewBucketNoOrBagNo_KeyPress(txtNewBucketNoOrBagNo, eventArgs);
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