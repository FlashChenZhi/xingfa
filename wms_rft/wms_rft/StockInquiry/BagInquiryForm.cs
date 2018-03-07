using System;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.StockInquiry
{
    public partial class BagInquiryForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        private stockRFT stockRft;

        public BagInquiryForm()
        {
            InitializeComponent();
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void BagMoveForm_Load(object sender, EventArgs e)
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
            txtBagNo.Text = string.Empty;
            lblFromLocationNo.Text = string.Empty;
            lblFromAreaName.Text = string.Empty;
            lblFromBucketNo.Text = string.Empty;
            lblPrNo.Text = string.Empty;
            lblItemCode.Text = string.Empty;
            lblItemName1.Text = string.Empty;
            lblItemName2.Text = string.Empty;
            lblItemName3.Text = string.Empty;
            lblColorCode.Text = string.Empty;
            lblQty.Text = string.Empty;
            lblWeight.Text = string.Empty;
            lblDayOfStorage.Text = string.Empty;
            lblMessage.Text = string.Empty;

            stockRft = null;
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
            txtBagNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
            txtBagNo.SelectAll();
            txtBagNo.Focus();
            txtBagNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
        }

        private void BagMoveForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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

        private void txtBagNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();

                    string bagNo = txtBagNo.Text.Trim();
                    clearAll();
                    txtBagNo.Text = bagNo;

                    if (!CommonHelper.isBagNo(bagNo))
                    {
                        msgHelper.showWarning("invalid bag no");

                        txtBagNo.SelectAll();
                        txtBagNo.Focus();
                        return;
                    }                    

                    stockRft = ServiceFactorySmart.getCurrentService().getStockInfoByBagNoForBagInquiry(bagNo);

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
            if (stockRft == null)
            {
                return;
            }

            lblFromLocationNo.Text = CommonHelper.locationFormatter(stockRft.locationNo);
            lblFromAreaName.Text = stockRft.areaName;
            lblFromBucketNo.Text = stockRft.bucketNo;
            lblPrNo.Text = stockRft.ticketNo;
            lblItemCode.Text = stockRft.itemCode;
            lblItemName1.Text = stockRft.itemName1;
            lblItemName2.Text = stockRft.itemName2;
            lblItemName3.Text = stockRft.itemName3;
            lblColorCode.Text = stockRft.colorCode;
            lblQty.Text = stockRft.qty.ToString("0");
            lblWeight.Text = (stockRft.qty * stockRft.unitWeight).ToString("0.####");
            lblDayOfStorage.Text = stockRft.dayOfStorage;

        }

        private void BagInquiryForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBagNo.Focused)
                    {
                        btnMenu.Focus();
                    }                  
                    else if (btnMenu.Focused)
                    {
                        txtBagNo.SelectAll();
                        txtBagNo.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (txtBagNo.Focused)
                    {
                        btnMenu.Focus();
                    }
                    else if (btnMenu.Focused)
                    {
                        txtBagNo.SelectAll();
                        txtBagNo.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnMenu_Click(btnMenu, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (txtBagNo.Focused)
                    {
                        txtBagNo_KeyPress(txtBagNo, eventArgs);
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