using System;
using System.Collections;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.Other
{
    public partial class BucketMergeForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        private ArrayList flatAreaList;
        private bucketInfoRFT fromBucketInfoRft;
        private bucketInfoRFT toBucketInfoRft;

        public BucketMergeForm()
        {
            InitializeComponent();
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void BucketMergeForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);

            clearAll();
            msgHelper = new MessageHelper(lblMessage);

            flatAreaList = new ArrayList();
            flatAreaList.Add("B");
            flatAreaList.Add("T");
            flatAreaList.Add("N");
            flatAreaList.Add("O");
            flatAreaList.Add("Q");
            flatAreaList.Add("C");
            flatAreaList.Add("D");
            flatAreaList.Add("E");
            flatAreaList.Add("K");
            flatAreaList.Add("F");
            flatAreaList.Add("J");
            flatAreaList.Add("L");

            try {
                initializeBarcodeScanner();
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }

        private void clearAll()
        {
            clearFromBucketSection();

            clearToBucketSection();

            lblTotalQty.Text = string.Empty;
            lblTotalWeight.Text = string.Empty;

            lblMessage.Text = string.Empty;
        }

        private void clearFromBucketSection()
        {
            txtFromBucketNo.Text = string.Empty;
            lblFromLocationNo.Text = string.Empty;
            lblPrNo.Text = string.Empty;
            lblItemCode.Text = string.Empty;
            lblItemName1.Text = string.Empty;
            lblItemName2.Text = string.Empty;
            lblItemName3.Text = string.Empty;
            lblColorCode.Text = string.Empty;
            lblFromQty.Text = string.Empty;
            lblFromWeight.Text = string.Empty;
            lblDayOfStorage.Text = string.Empty;

            lblMessage.Text = string.Empty;

            fromBucketInfoRft = null;

            updateTotalSection();
        }

        private void clearToBucketSection()
        {
            txtToBucketNo.Text = string.Empty;
            lblToLocationNo.Text = string.Empty;
            lblToQty.Text = string.Empty;
            lblToWeight.Text = string.Empty;

            lblMessage.Text = string.Empty;

            toBucketInfoRft = null;

            updateTotalSection();
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
            if (txtFromBucketNo.Focused) {
                txtFromBucketNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtFromBucketNo.SelectAll();
                txtFromBucketNo.Focus();
                txtFromBucketNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            } else if (txtToBucketNo.Focused) {
                txtToBucketNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtToBucketNo.SelectAll();
                txtToBucketNo.Focus();
                txtToBucketNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
        }

        private void BucketMergeForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            try {
                disposeBarcodeScanner();
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }

        private void disposeBarcodeScanner()
        {
            if (barcodeScanner != null) {
                barcodeScanner.Dispose();
            }
        }

        private void txtFromBucketNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter)) {
                try {
                    msgHelper.clear();

                    string bucketNo = txtFromBucketNo.Text.Trim();

                    clearFromBucketSection();
                    txtFromBucketNo.Text = bucketNo;

                    if (!CommonHelper.isBucketNo(bucketNo)) {
                        msgHelper.showWarning("invalid from bucket no");

                        updateTotalSection();

                        txtFromBucketNo.SelectAll();
                        txtFromBucketNo.Focus();

                        return;
                    }

                    fromBucketInfoRft = ServiceFactorySmart.getCurrentService().getBucketInfoByBucketNoForBucketInquiry(bucketNo);

                    if (fromBucketInfoRft != null){
                        string fromLocationNo = fromBucketInfoRft.locationNo;
                        if (string.IsNullOrEmpty(fromLocationNo) ||  !flatAreaList.Contains(fromLocationNo.Substring(0, 1))){

                            clearFromBucketSection();
                            txtFromBucketNo.Text = bucketNo;
                            lblFromLocationNo.Text = CommonHelper.locationFormatter(fromLocationNo);
                            msgHelper.showWarning("invalid from location no");

                            updateTotalSection();

                            txtFromBucketNo.SelectAll();
                            txtFromBucketNo.Focus();

                            return;
                        }

                    }

                    showFromBucketInfo();
                    updateTotalSection();
                } catch (Exception ex) {
                    string bucketNo = txtFromBucketNo.Text.Trim();

                    clearFromBucketSection();

                    txtFromBucketNo.Text = bucketNo;
                    txtFromBucketNo.SelectAll();
                    txtFromBucketNo.Focus();
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void txtToBucketNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter)) {
                try {
                    msgHelper.clear();

                    string bucketNo = txtToBucketNo.Text.Trim();

                    clearToBucketSection();
                    txtToBucketNo.Text = bucketNo;

                    if (!CommonHelper.isBucketNo(bucketNo)) {
                        msgHelper.showWarning("invalid to bucket no");

                        updateTotalSection();

                        txtToBucketNo.SelectAll();
                        txtToBucketNo.Focus();

                        return;
                    }

                    toBucketInfoRft = ServiceFactorySmart.getCurrentService().getBucketInfoByBucketNoForBucketInquiry(bucketNo);

                    if (toBucketInfoRft != null) {
                        string toLocationNo = toBucketInfoRft.locationNo;
                        if (string.IsNullOrEmpty(toLocationNo) || !flatAreaList.Contains(toLocationNo.Substring(0, 1))) {

                            clearToBucketSection();
                            txtToBucketNo.Text = bucketNo;
                            lblToLocationNo.Text = CommonHelper.locationFormatter(toLocationNo);
                            msgHelper.showWarning("invalid to location no");

                            updateTotalSection();

                            txtToBucketNo.SelectAll();
                            txtToBucketNo.Focus();


                            return;
                        }

                    }

                    showToBucketInfo();
                    updateTotalSection();
                } catch (Exception ex) {
                    string bucketNo = txtToBucketNo.Text.Trim();

                    clearToBucketSection();

                    txtToBucketNo.Text = bucketNo;
                    txtToBucketNo.SelectAll();
                    txtToBucketNo.Focus();
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void showFromBucketInfo()
        {
            if (fromBucketInfoRft == null || fromBucketInfoRft.stockRfts == null || fromBucketInfoRft.stockRfts.Length == 0) {
                clearFromBucketSection();
            } else {
                stockRFT stockRft = fromBucketInfoRft.stockRfts[0];

                lblFromLocationNo.Text = CommonHelper.locationFormatter(fromBucketInfoRft.locationNo);
                lblPrNo.Text = stockRft.ticketNo;
                lblItemCode.Text = stockRft.itemCode;
                lblItemName1.Text = stockRft.itemName1;
                lblItemName2.Text = stockRft.itemName2;
                lblItemName3.Text = stockRft.itemName3;
                lblColorCode.Text = stockRft.colorCode;
                lblFromQty.Text = stockRft.qty.ToString("0");
                lblFromWeight.Text = (stockRft.qty * stockRft.unitWeight).ToString("0.####");
                lblDayOfStorage.Text = fromBucketInfoRft.dayOfStorage;
            }
        }

        private void showToBucketInfo()
        {
            if (toBucketInfoRft == null || toBucketInfoRft.stockRfts == null || toBucketInfoRft.stockRfts.Length == 0) {
                clearToBucketSection();
            } else {
                stockRFT stockRft = toBucketInfoRft.stockRfts[0];

                lblToLocationNo.Text = CommonHelper.locationFormatter(toBucketInfoRft.locationNo);
                lblToQty.Text = stockRft.qty.ToString("0");
                lblToWeight.Text = (stockRft.qty * stockRft.unitWeight).ToString("0.####");
            }
        }

        private void updateTotalSection()
        {
            decimal totalQty = 0;
            decimal totalWeight = 0;

            if (fromBucketInfoRft != null && fromBucketInfoRft.stockRfts != null && fromBucketInfoRft.stockRfts.Length != 0) {
                stockRFT stockRft = fromBucketInfoRft.stockRfts[0];
                if (stockRft != null) {
                    totalQty += stockRft.qty;
                    totalWeight += stockRft.qty * stockRft.unitWeight;
                }
            }

            if (toBucketInfoRft != null && toBucketInfoRft.stockRfts != null && toBucketInfoRft.stockRfts.Length != 0) {
                stockRFT stockRft = toBucketInfoRft.stockRfts[0];
                if (stockRft != null) {
                    totalQty += stockRft.qty;
                    totalWeight += stockRft.qty * stockRft.unitWeight;
                }
            }

            lblTotalQty.Text = totalQty.ToString("0");
            lblTotalWeight.Text = totalWeight.ToString("0.####");

        }

        private void BucketMergeForm_KeyDown(object sender, KeyEventArgs e)
        {
            try {
                if (e.KeyCode == Keys.Down) {
                    if (txtFromBucketNo.Focused) {
                        txtToBucketNo.Focus();
                    } else if (txtToBucketNo.Focused) {
                        btnSubmit.Focus();
                    } else if (btnSubmit.Focused) {
                        btnCancel.Focus();
                    } else if (btnCancel.Focused) {
                        btnMenu.Focus();
                    } else if (btnMenu.Focused) {
                        txtFromBucketNo.SelectAll();
                        txtFromBucketNo.Focus();
                    }
                } else if (e.KeyCode == Keys.Up) {
                    if (txtFromBucketNo.Focused) {
                        btnMenu.Focus();
                    } else if (btnMenu.Focused) {
                        btnCancel.Focus();
                    } else if (btnCancel.Focused) {
                        btnSubmit.Focus();
                    } else if (btnSubmit.Focused) {
                        txtToBucketNo.Focus();
                    } else if (txtToBucketNo.Focused) {
                        txtFromBucketNo.SelectAll();
                        txtFromBucketNo.Focus();
                    }
                } else if (e.KeyValue == 64)//L Button
                {
                    btnMenu_Click(btnMenu, null);
                } else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (txtFromBucketNo.Focused) {
                        txtFromBucketNo_KeyPress(txtFromBucketNo, eventArgs);
                    } else if (txtToBucketNo.Focused) {
                        txtToBucketNo_KeyPress(txtToBucketNo, eventArgs);
                    } else if (btnSubmit.Focused) {
                        btnSubmit_Click(btnSubmit, eventArgs);
                    } else if (btnCancel.Focused) {
                        btnCancel_Click(btnCancel, eventArgs);
                    } else if (btnMenu.Focused) {
                        btnMenu_Click(btnMenu, eventArgs);
                    }
                }
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            try {
                clearAll();
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnSubmit_Click(object sender, EventArgs e)
        {
            try {
                msgHelper.clear();

                string fromBucketNo = txtFromBucketNo.Text.Trim();

                if (!CommonHelper.isBucketNo(fromBucketNo)) {
                    msgHelper.showWarning("invalid from bucket no");

                    txtFromBucketNo.SelectAll();
                    txtFromBucketNo.Focus();

                    return;
                }

                string toBucketNo = txtToBucketNo.Text.Trim();

                if (!CommonHelper.isBucketNo(toBucketNo)) {
                    msgHelper.showWarning("invalid to bucket no");

                    txtToBucketNo.SelectAll();
                    txtToBucketNo.Focus();

                    return;
                }

                ServiceFactorySmart.getCurrentService().flatStockMerge(fromBucketNo, toBucketNo, string.Empty);

                clearAll();
                msgHelper.showInfo("submit ok");

                txtFromBucketNo.SelectAll();
                txtFromBucketNo.Focus();
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }
    }
}