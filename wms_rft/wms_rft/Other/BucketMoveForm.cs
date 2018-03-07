using System;
using System.Windows.Forms;
using Barcode;
using wms_rft.Common;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.Other
{
    public partial class BucketMoveForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        private bucketInfoRFT bucketInfoRft;
        private int currentPageNo = 0;

        public BucketMoveForm()
        {
            InitializeComponent();
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void BucketMoveForm_Load(object sender, EventArgs e)
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
            txtBucketNo.Text = string.Empty;
            lblFromLocationNo.Text = string.Empty;
            lblFromAreaName.Text = string.Empty;
            lblFromPalletNo.Text = string.Empty;
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
            lblDayOfStorage.Text = string.Empty;
            txtLocationNo.Text = string.Empty;
            lblToAreaName.Text = string.Empty;
            lblToPalletNo.Text = string.Empty;
            lblBucketNo1.Text = string.Empty;
            lblBucketNo2.Text = string.Empty;
            lblMessage.Text = string.Empty;

            bucketInfoRft = null;
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
            if (txtBucketNo.Focused)
            {
                txtBucketNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();
                txtBucketNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
            else if (txtLocationNo.Focused)
            {
                txtLocationNo.Text = data;
                txtLocationNo.SelectAll();
                txtLocationNo.Focus();
                txtLocationNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
        }

        private void BucketMoveForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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

                    bucketInfoRft = ServiceFactorySmart.getCurrentService().getBucketInfoByBucketNoForBucketMove(bucketNo);

                    currentPageNo = 1;

                    showPage();

                    txtLocationNo.SelectAll();
                    txtLocationNo.Focus();
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

            currentPageNo = Math.Min(currentPageNo, bucketInfoRft.stockRfts.Length);

            stockRFT stockRft = bucketInfoRft.stockRfts[currentPageNo - 1];

            lblFromLocationNo.Text = CommonHelper.locationFormatter(bucketInfoRft.locationNo);
            lblFromAreaName.Text = bucketInfoRft.areaName;
            lblFromPalletNo.Text = bucketInfoRft.palletNo;
            lblBagNo.Text = stockRft.bagNo;
            lblPrNo.Text = stockRft.ticketNo;
            lblItemCode.Text = stockRft.itemCode;
            lblItemName1.Text = stockRft.itemName1;
            lblItemName2.Text = stockRft.itemName2;
            lblItemName3.Text = stockRft.itemName3;
            lblColorCode.Text = stockRft.colorCode;
            lblQty.Text = stockRft.qty.ToString("0");
            lblWeight.Text = (stockRft.qty * stockRft.unitWeight).ToString("0.####");
            lblDayOfStorage.Text = bucketInfoRft.dayOfStorage;

            lblPageNo.Text = currentPageNo.ToString() + "/" + bucketInfoRft.stockRfts.Length;
        }

        private void btnPrev_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (bucketInfoRft == null || bucketInfoRft.stockRfts == null || bucketInfoRft.stockRfts.Length == 0)
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

                if (bucketInfoRft == null || bucketInfoRft.stockRfts == null || bucketInfoRft.stockRfts.Length == 0)
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

        private void txtLocationNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();

                    string temp = txtLocationNo.Text.Trim().Replace("-", string.Empty);
                    if (temp.Length != txtLocationNo.MaxLength)
                    {
                        msgHelper.showWarning("invalid location no");

                        txtLocationNo.SelectAll();
                        txtLocationNo.Focus();
                        return;
                    }

                    locationRFT locationRft = ServiceFactorySmart.getCurrentService().getLocationInfoByLocationNoForBucketMove(temp);
                    lblToAreaName.Text = locationRft.areaName;
                    lblToPalletNo.Text = locationRft.palletNo;
                    lblBucketNo1.Text = locationRft.bucketNo1;
                    lblBucketNo2.Text = locationRft.bucketNo2;

                    btnSubmit_Click(null, null);

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
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

                string bucketNo = txtBucketNo.Text.Trim();
                if (bucketNo.Length != txtBucketNo.MaxLength)
                {
                    msgHelper.showWarning("invalid bucket no");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }

                string temp = txtLocationNo.Text.Trim().Replace("-", string.Empty);
                if (temp.Length != txtLocationNo.MaxLength)
                {
                    msgHelper.showWarning("invalid location no");

                    txtLocationNo.SelectAll();
                    txtLocationNo.Focus();
                    return;
                }

                bucketInfoRFT biRft = ServiceFactorySmart.getCurrentService().getBucketInfoByBucketNoForBucketInquiry(bucketNo);
                ServiceFactorySmart.getCurrentService().doBucketMove(bucketNo, temp);
//                emptyInfoRFT emptyInfoRft = ServiceFactorySmart.getCurrentService().doBucketMove(bucketNo, temp);
//                if (emptyInfoRft.empty)
//                {
//                    Form form = new EmptyConfirmForm(emptyInfoRft);
//                    form.ShowDialog();
//                }

                clearAll();
                msgHelper.showInfo("submit ok");

                emptyInfoRFT emptyInfoRft = ServiceFactorySmart.getCurrentService().getEmptyInfo(biRft.locationNo.PadRight(13, '0'), biRft.palletId, string.Empty, string.Empty);
                if (emptyInfoRft.emptyLocation
                    || emptyInfoRft.emptyPallet
                    || emptyInfoRft.emptyBucket
                    || emptyInfoRft.emptyBag)
                {
                    Form form = new EmptyConfirmSmartForm2(emptyInfoRft);
                    form.ShowDialog();
                }

                txtBucketNo.SelectAll();
                txtBucketNo.Focus();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            try
            {                
                clearAll();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void BucketMoveForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBucketNo.Focused)
                    {
                        btnPrev.Focus();
                    }
                    else if (btnPrev.Focused)
                    {
                        btnNext.Focus();
                    }
                    else if (btnNext.Focused)
                    {
                        txtLocationNo.SelectAll();
                        txtLocationNo.Focus();
                    }
                    else if (txtLocationNo.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        btnCancel.Focus();
                    }
                    else if (btnCancel.Focused)
                    {
                        btnMenu.Focus();
                    }
                    else if (btnMenu.Focused)
                    {
                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (txtBucketNo.Focused)
                    {
                        btnMenu.Focus();
                    }
                    else if (btnMenu.Focused)
                    {
                        btnCancel.Focus();
                    }
                    else if (btnCancel.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        txtLocationNo.SelectAll();
                        txtLocationNo.Focus();
                    }
                    else if (txtLocationNo.Focused)
                    {
                        btnNext.Focus();
                    }
                    else if (btnNext.Focused)
                    {
                        btnPrev.Focus();
                    }
                    else if (btnPrev.Focused)
                    {
                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnMenu_Click(btnMenu, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));


                    if (txtBucketNo.Focused)
                    {
                        txtBucketNo_KeyPress(txtBucketNo, eventArgs);
                    }
                    else if (btnPrev.Focused)
                    {
                        btnPrev_Click(btnPrev, eventArgs);
                    }
                    else if (btnNext.Focused)
                    {
                        btnNext_Click(btnNext, eventArgs);
                    }
                    else if (txtLocationNo.Focused)
                    {
                        txtLocationNo_KeyPress(txtLocationNo, eventArgs);
                    }                   
                    else if (btnSubmit.Focused)
                    {
                        btnSubmit_Click(btnSubmit, eventArgs);
                    }
                    else if (btnCancel.Focused)
                    {
                        btnCancel_Click(btnCancel, eventArgs);
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