using System;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Et;
using wms_rft.WmsRftEt;

namespace wms_rft.StockIn
{
    public partial class BucketNoReadEtForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        private bucketInfoRFT bucketInfoRft;
        private string[] bcrNos;
        private int currentStockPageNo = 0;
        private int currentBcrPageNo = 0;

        public BucketNoReadEtForm()
        {
            InitializeComponent();
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void BucketInquiryForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.ET);

            clearAll();
            msgHelper = new MessageHelper(lblMessage);

            try
            {
                initializeBarcodeScanner();
                bcrNos = ServiceFactoryEt.getCurrentService().getErrorBcrNo();
                currentBcrPageNo = 1;
                showBcrNo();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void clearAll()
        {
            txtBcrNo.Text = string.Empty;
            txtBucketNo.Text = string.Empty;
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
            lblMessage.Text = string.Empty;

            bucketInfoRft = null;
            bcrNos = null;
            currentStockPageNo = 0;
            currentBcrPageNo = 0;
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
            txtBucketNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
            txtBucketNo.SelectAll();
            txtBucketNo.Focus();

            txtBucketNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
        }

        private void BucketInquiryForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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

        private void txtBucketNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();

                    string bucketNo = txtBucketNo.Text.Trim();
                    if (bucketNo.Length != txtBucketNo.MaxLength)
                    {
                        msgHelper.showWarning("invalid bucket no");

                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                        return;
                    }

                    bucketInfoRft = ServiceFactoryEt.getCurrentService().getBucketInfoByBucketNoForBucketInquiry(bucketNo);

                    currentStockPageNo = 1;

                    showPage();
                }
                catch (Exception ex)
                {
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void showPage()
        {
            if (bucketInfoRft == null || bucketInfoRft.stockRfts == null || bucketInfoRft.stockRfts.Length == 0)
            {
                currentStockPageNo = 0;
            }
            else
            {
                currentStockPageNo = Math.Max(currentStockPageNo, 1);
            }

            if (currentStockPageNo == 0)
            {
                clearAll();
                return;
            }

            currentStockPageNo = Math.Min(currentStockPageNo, bucketInfoRft.stockRfts.Length);

            stockRFT stockRft = bucketInfoRft.stockRfts[currentStockPageNo - 1];

            lblBagNo.Text = stockRft.bagNo;
            lblPrNo.Text = stockRft.ticketNo;
            lblItemCode.Text = stockRft.itemCode;
            lblItemName1.Text = stockRft.itemName1;
            lblItemName2.Text = stockRft.itemName2;
            lblItemName3.Text = stockRft.itemName3;
            lblColorCode.Text = stockRft.colorCode;
            lblQty.Text = stockRft.qty.ToString("0");
            lblWeight.Text = (stockRft.qty * stockRft.unitWeight).ToString("0.####");

            lblPageNo.Text = currentStockPageNo.ToString() + "/" + bucketInfoRft.stockRfts.Length;
        }

        private void showBcrNo()
        {
            if (bcrNos == null || bcrNos.Length == 0)
            {
                currentBcrPageNo = 0;
            }
            else
            {
                currentBcrPageNo = Math.Max(currentBcrPageNo, 1);
            }

            if (currentBcrPageNo == 0)
            {
                txtBcrNo.Text = string.Empty;
                return;
            }

            currentBcrPageNo = Math.Min(currentBcrPageNo, bcrNos.Length);

            txtBcrNo.Text = bcrNos[currentBcrPageNo - 1];
        }

        private void btnStockPrev_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (bucketInfoRft == null || bucketInfoRft.stockRfts == null || bucketInfoRft.stockRfts.Length == 0)
                {
                    return;
                }

                currentStockPageNo--;
                showPage();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnStockNext_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (bucketInfoRft == null || bucketInfoRft.stockRfts == null || bucketInfoRft.stockRfts.Length == 0)
                {
                    return;
                }

                currentStockPageNo++;
                showPage();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnBcrPrev_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (bcrNos == null || bcrNos.Length == 0)
                {
                    return;
                }

                currentBcrPageNo--;
                showBcrNo();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnBcrNext_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (bcrNos == null || bcrNos.Length == 0)
                {
                    return;
                }

                currentBcrPageNo++;
                showBcrNo();
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

                string bcrNo = txtBcrNo.Text.Trim();
                if (string.IsNullOrEmpty(bcrNo))
                {
                    msgHelper.showWarning("please choose a bcr");
                    return;
                }

                string bucketNo = txtBucketNo.Text.Trim();
                if (bucketNo.Length != txtBucketNo.MaxLength)
                {
                    msgHelper.showWarning("invalid bucket no");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }

                if (!CommonHelper.isBucketNo(bucketNo))
                {
                    msgHelper.showWarning("invalid dest bucket no");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }

                ServiceFactoryEt.getCurrentService().doBcrNoRead(bcrNo, bucketNo);

                clearAll();
                msgHelper.showInfo("submit ok");

                txtBucketNo.SelectAll();
                txtBucketNo.Focus();

                bcrNos = ServiceFactoryEt.getCurrentService().getErrorBcrNo();
                currentBcrPageNo = 1;
                showBcrNo();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void BucketNoReadForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBcrNo.Focused)
                    {
                        btnBcrPrev.Focus();
                    }
                    else if (btnBcrPrev.Focused)
                    {
                        btnBcrNext.Focus();
                    }
                    else if (btnBcrNext.Focused)
                    {
                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                    }
                    else if (txtBucketNo.Focused)
                    {
                        btnStockPrev.Focus();
                    }
                    else if (btnStockPrev.Focused)
                    {
                        btnStockNext.Focus();
                    }
                    else if (btnStockNext.Focused)
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
                        btnStockNext.Focus();
                    }
                    else if (btnStockNext.Focused)
                    {
                        btnStockPrev.Focus();
                    }
                    else if (btnStockPrev.Focused)
                    {
                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                    }
                    else if (txtBucketNo.Focused)
                    {
                        btnBcrNext.Focus();
                    }
                    else if (btnBcrNext.Focused)
                    {
                        btnBcrPrev.Focus();
                    }
                    else if (btnBcrPrev.Focused)
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

                    if (btnBcrPrev.Focused)
                    {
                        btnBcrPrev_Click(btnBcrPrev, eventArgs);
                    }
                    else if (btnBcrNext.Focused)
                    {
                        btnBcrNext_Click(btnBcrNext, eventArgs);
                    }
                    else if (txtBucketNo.Focused)
                    {
                        txtBucketNo_KeyPress(txtBucketNo, eventArgs);
                    }
                    else if (btnStockPrev.Focused)
                    {
                        btnStockPrev_Click(btnStockPrev, eventArgs);
                    }
                    else if (btnStockNext.Focused)
                    {
                        btnStockNext_Click(btnStockNext, eventArgs);
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