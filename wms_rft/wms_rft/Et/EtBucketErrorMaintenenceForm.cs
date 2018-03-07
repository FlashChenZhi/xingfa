using System;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Et;
using wms_rft.WmsRftEt;

namespace wms_rft.Et
{
    public partial class EtBucketErrorMaintenenceForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        private bucketInfoRFT bucketInfoRft;

        public EtBucketErrorMaintenenceForm()
        {
            InitializeComponent();
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void EtBucketErrorMaintenenceForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.ET);

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
            lblPrNo.Text = string.Empty;
            lblItemCode.Text = string.Empty;
            lblItemName1.Text = string.Empty;
            lblItemName2.Text = string.Empty;
            lblItemName3.Text = string.Empty;
            lblColorCode.Text = string.Empty;
            lblPlanQty.Text = string.Empty;
            lblPlanWeight.Text = string.Empty;
            lblPickQty.Text = string.Empty;
            lblPickWeight.Text = string.Empty;
            lblDayOfPicking.Text = string.Empty;
            lblReason.Text = string.Empty;
            lblMessage.Text = string.Empty;

            bucketInfoRft = null;
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

        private void EtBucketErrorMaintenenceForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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

                    clearAll();
                    txtBucketNo.Text = bucketNo;

                    if (!CommonHelper.isBucketNo(bucketNo))
                    {
                        msgHelper.showWarning("invalid bucket no");

                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                        return;
                    }
                    
                    bucketInfoRft = ServiceFactoryEt.getCurrentService().getBucketInfoByBucketNoForEtError(bucketNo);                    

                    showPage();
                }
                catch (Exception ex)
                {
                    string bucketNo = txtBucketNo.Text.Trim();

                    clearAll();

                    txtBucketNo.Text = bucketNo;
                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void showPage()
        {
            if (bucketInfoRft == null)
            {
                return;
            }

            lblFromLocationNo.Text = CommonHelper.locationFormatter(bucketInfoRft.locationNo);
            lblFromAreaName.Text = bucketInfoRft.areaName;
            if (bucketInfoRft.stockRfts != null && bucketInfoRft.stockRfts.Length > 0)
            {
                lblPrNo.Text = bucketInfoRft.stockRfts[0].ticketNo;
                lblItemCode.Text = bucketInfoRft.stockRfts[0].itemCode;
                lblItemName1.Text = bucketInfoRft.stockRfts[0].itemName1;
                lblItemName2.Text = bucketInfoRft.stockRfts[0].itemName2;
                lblItemName3.Text = bucketInfoRft.stockRfts[0].itemName3;
                lblColorCode.Text = bucketInfoRft.stockRfts[0].colorCode;
            }
            lblPlanQty.Text = bucketInfoRft.planQty.ToString("0");
            lblPlanWeight.Text = bucketInfoRft.planWeight.ToString("0.####");
            lblPickQty.Text = bucketInfoRft.pickQty.ToString("0");
            lblPickWeight.Text = bucketInfoRft.pickWeight.ToString("0.####");
            lblDayOfPicking.Text = bucketInfoRft.dayOfPicking;
        }

        private void btnComplete_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();
                string bucketNo = txtBucketNo.Text.Trim();

                if (!CommonHelper.isBucketNo(bucketNo))
                {
                    msgHelper.showWarning("invalid bucket no");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }

                ServiceFactoryEt.getCurrentService().doneBucketForEtError(bucketNo);

                clearAll();
                msgHelper.showInfo("submit ok");
             
            }
            catch (Exception ex)
            {
                string bucketNo = txtBucketNo.Text.Trim();

                clearAll();

                txtBucketNo.Text = bucketNo;
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();
                msgHelper.showError(ex.Message);
            }
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();
                string bucketNo = txtBucketNo.Text.Trim();

                if (!CommonHelper.isBucketNo(bucketNo))
                {
                    msgHelper.showWarning("invalid bucket no");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }

                ServiceFactoryEt.getCurrentService().cancelBucketForEtError(bucketNo);

                clearAll();
                msgHelper.showInfo("submit ok");

            }
            catch (Exception ex)
            {
                string bucketNo = txtBucketNo.Text.Trim();

                clearAll();

                txtBucketNo.Text = bucketNo;
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();
                msgHelper.showError(ex.Message);
            }
        }

        private void btnDelete_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();
                string bucketNo = txtBucketNo.Text.Trim();

                if (!CommonHelper.isBucketNo(bucketNo))
                {
                    msgHelper.showWarning("invalid bucket no");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }

                ServiceFactoryEt.getCurrentService().deleteBucketForEtError(bucketNo);

                clearAll();
                msgHelper.showInfo("submit ok");

            }
            catch (Exception ex)
            {
                string bucketNo = txtBucketNo.Text.Trim();

                clearAll();

                txtBucketNo.Text = bucketNo;
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();
                msgHelper.showError(ex.Message);
            }
        }  

        private void BucketInquiryForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBucketNo.Focused)
                    {
                        btnComplete.Focus();
                    }
                    else if (btnComplete.Focused)
                    {
                        btnCancel.Focus();
                    }
                    else if (btnCancel.Focused)
                    {
                        btnDelete.Focus();
                    }
                    else if (btnDelete.Focused)
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
                        btnDelete.Focus();
                    }
                    else if (btnDelete.Focused)
                    {
                        btnCancel.Focus();
                    }
                    else if (btnCancel.Focused)
                    {
                        btnComplete.Focus();
                    }
                    else if (btnComplete.Focused)
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
                    else if (btnComplete.Focused)
                    {
                        btnComplete_Click(btnComplete, eventArgs);
                    }
                    else if (btnCancel.Focused)
                    {
                        btnCancel_Click(btnCancel, eventArgs);
                    }
                    else if (btnDelete.Focused)
                    {
                        btnDelete_Click(btnDelete, eventArgs);
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