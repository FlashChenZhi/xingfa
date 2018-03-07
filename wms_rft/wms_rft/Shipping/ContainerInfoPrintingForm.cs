using System;
using System.Collections.Generic;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.Shipping
{
    public partial class ContainerInfoPrintingForm : Form
    {
        private string userId;
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;
        private List<containerInfoDB2RFT> containerInfoDB2RftList;
        private int currentPageNo = 0;

        public ContainerInfoPrintingForm(string userId)
        {
            InitializeComponent();
            this.userId = userId;
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void BucketToBagSettingForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);

            clearAll();
            msgHelper = new MessageHelper(lblMessage);

            try {
                initializeBarcodeScanner();
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }

        private void clearAll()
        {
            txtBucketNoOrBagNo.Text = string.Empty;
            lblBagNo.Text = string.Empty;
            lblPageNo.Text = string.Empty;
            lblItemCategory.Text = string.Empty;
            lblPrNo.Text = string.Empty;
            lblItemCode.Text = string.Empty;
            lblItemName1.Text = string.Empty;
            lblItemName2.Text = string.Empty;
            lblItemName3.Text = string.Empty;
            lblColorCode.Text = string.Empty;
            lblQty.Text = string.Empty;
            lblWeight.Text = string.Empty;

            lblMessage.Text = string.Empty;

            containerInfoDB2RftList = new List<containerInfoDB2RFT>();
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
            txtBucketNoOrBagNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
            txtBucketNoOrBagNo.SelectAll();
            txtBucketNoOrBagNo.Focus();
            txtBucketNoOrBagNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
        }

        private void BucketToBagSettingForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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

        private void txtBucketNoOrBagNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter)) {
                try {
                    msgHelper.clear();

                    string containerNo = txtBucketNoOrBagNo.Text.Trim();

                    clearAll();
                    txtBucketNoOrBagNo.Text = containerNo;

                    if (!CommonHelper.isBucketNo(containerNo) && !CommonHelper.isBagNo(containerNo)) {
                        msgHelper.showWarning("invalid bucket no or bag no");

                        txtBucketNoOrBagNo.SelectAll();
                        txtBucketNoOrBagNo.Focus();
                        return;
                    }

                    containerInfoDB2RftList.Clear();

                    containerInfoDB2RFT[] containerInfos;

                    if (CommonHelper.isBucketNo(containerNo)) {
                        containerInfos = ServiceFactorySmart.getCurrentService().getContainerInfoByBucketNo_DB2(containerNo);

                        if (containerInfos == null || containerInfos.Length == 0) {
                            msgHelper.showWarning("bucket info not found");

                            txtBucketNoOrBagNo.SelectAll();
                            txtBucketNoOrBagNo.Focus();
                            return;
                        }
                    } else {
                        containerInfos = ServiceFactorySmart.getCurrentService().getContainerInfoByBagNo_DB2(containerNo);
                        if (containerInfos == null || containerInfos.Length == 0) {
                            msgHelper.showWarning("bag info not found");

                            txtBucketNoOrBagNo.SelectAll();
                            txtBucketNoOrBagNo.Focus();
                            return;
                        }
                    }

                    containerInfoDB2RftList.AddRange(containerInfos);

                    currentPageNo = 1;

                    showPage();

                    txtBucketNoOrBagNo.SelectAll();
                    txtBucketNoOrBagNo.Focus();
                } catch (Exception ex) {
                    string bucketNo = txtBucketNoOrBagNo.Text.Trim();

                    clearAll();

                    txtBucketNoOrBagNo.Text = bucketNo;
                    txtBucketNoOrBagNo.SelectAll();
                    txtBucketNoOrBagNo.Focus();
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void showPage()
        {

            if (containerInfoDB2RftList == null || containerInfoDB2RftList.Count == 0) {
                currentPageNo = 0;
            } else {
                currentPageNo = Math.Max(currentPageNo, 1);
            }

            if (currentPageNo == 0) {
                clearAll();
                return;
            }

            if (containerInfoDB2RftList != null && containerInfoDB2RftList.Count > 0) {
                currentPageNo = Math.Min(currentPageNo, containerInfoDB2RftList.Count);

                containerInfoDB2RFT containerInfoDb2Rft = containerInfoDB2RftList[currentPageNo - 1];

                lblBagNo.Text = containerInfoDb2Rft.bagNo;
                lblItemCategory.Text = CommonHelper.itemCategoryToName(containerInfoDb2Rft.itemCategory);
                lblPrNo.Text = containerInfoDb2Rft.prNo;
                lblItemCode.Text = containerInfoDb2Rft.itemCode;
                lblItemName1.Text = containerInfoDb2Rft.itemName1;
                lblItemName2.Text = containerInfoDb2Rft.itemName2;
                lblItemName3.Text = containerInfoDb2Rft.itemName3;
                lblColorCode.Text = containerInfoDb2Rft.colorCode;
                lblQty.Text = containerInfoDb2Rft.qty.ToString("0");
                lblWeight.Text = containerInfoDb2Rft.weight.ToString("0.####");

                lblPageNo.Text = currentPageNo + "/" + containerInfoDB2RftList.Count;
            }
        }

        private void BucketToBagSettingForm_KeyDown(object sender, KeyEventArgs e)
        {
            try {
                if (e.KeyCode == Keys.Down) {
                    if (txtBucketNoOrBagNo.Focused) {
                        btnPrev.Focus();
                    } else if (btnPrev.Focused) {
                        btnNext.Focus();
                    } else if (btnNext.Focused) {
                        btnPrint.Focus();
                    } else if (btnPrint.Focused) {
                        btnMenu.Focus();
                    } else if (btnMenu.Focused) {
                        txtBucketNoOrBagNo.SelectAll();
                        txtBucketNoOrBagNo.Focus();
                    }
                } else if (e.KeyCode == Keys.Up) {
                    if (txtBucketNoOrBagNo.Focused) {
                        btnMenu.Focus();
                    } else if (btnMenu.Focused) {
                        btnPrint.Focus();
                    } else if (btnPrint.Focused) {
                        btnNext.Focus();
                    } else if (btnNext.Focused) {
                        btnPrev.Focus();
                    } else if (btnPrev.Focused) {
                        txtBucketNoOrBagNo.SelectAll();
                        txtBucketNoOrBagNo.Focus();
                    }
                } else if (e.KeyValue == 64)//L Button
                {
                    btnMenu_Click(btnMenu, null);
                } else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (txtBucketNoOrBagNo.Focused) {
                        txtBucketNoOrBagNo_KeyPress(txtBucketNoOrBagNo, eventArgs);
                    } else if (btnPrint.Focused) {
                        btnPrint_Click(btnPrint, eventArgs);
                    } else if (btnMenu.Focused) {
                        btnMenu_Click(btnMenu, eventArgs);
                    }
                }
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnPrint_Click(object sender, EventArgs e)
        {
            try {
                msgHelper.clear();

                string containerNo = txtBucketNoOrBagNo.Text.Trim();

                if (!CommonHelper.isBucketNo(containerNo) && !CommonHelper.isBagNo(containerNo)) {
                    msgHelper.showWarning("invalid bucket no or bag no");

                    txtBucketNoOrBagNo.SelectAll();
                    txtBucketNoOrBagNo.Focus();
                    return;
                }

                containerInfoDB2RFT[] containerInfos;

                if (CommonHelper.isBucketNo(containerNo)) {
                    containerInfos = ServiceFactorySmart.getCurrentService().getContainerInfoByBucketNo_DB2(containerNo);

                    if (containerInfos == null || containerInfos.Length == 0) {
                        msgHelper.showWarning("bucket info not found");

                        txtBucketNoOrBagNo.SelectAll();
                        txtBucketNoOrBagNo.Focus();
                        return;
                    }
                } else {
                    containerInfos = ServiceFactorySmart.getCurrentService().getContainerInfoByBagNo_DB2(containerNo);
                    if (containerInfos == null || containerInfos.Length == 0) {
                        msgHelper.showWarning("bag info not found");

                        txtBucketNoOrBagNo.SelectAll();
                        txtBucketNoOrBagNo.Focus();
                        return;
                    }
                }

                ServiceFactorySmart.getCurrentService().printLogisticContainerTicket(containerNo, userId);

                clearAll();
                msgHelper.showInfo("submit ok");

                txtBucketNoOrBagNo.SelectAll();
                txtBucketNoOrBagNo.Focus();

            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnPrev_Click(object sender, EventArgs e)
        {
            try {
                msgHelper.clear();

                if (containerInfoDB2RftList == null || containerInfoDB2RftList.Count == 0) {
                    return;
                }

                currentPageNo--;
                showPage();
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnNext_Click(object sender, EventArgs e)
        {
            try {
                msgHelper.clear();

                if (containerInfoDB2RftList == null || containerInfoDB2RftList.Count == 0) {
                    return;
                }

                currentPageNo++;
                showPage();
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }
    }
}