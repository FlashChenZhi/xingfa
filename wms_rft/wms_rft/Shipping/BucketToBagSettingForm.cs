using System;
using System.Collections.Generic;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.Shipping
{
    public partial class BucketToBagSettingForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;
        private List<containerInfoDB2RFT> containerInfoDB2RftList;
        private int currentPageNo = 0;

        public BucketToBagSettingForm()
        {
            InitializeComponent();
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
            txtBagNo.Text = string.Empty;
           
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
            if (txtBucketNo.Focused)
            {
                txtBucketNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();
                txtBucketNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }else if (txtBagNo.Focused)
            {
                txtBagNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtBagNo.SelectAll();
                txtBagNo.Focus();
                txtBagNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
        }

        private void BucketToBagSettingForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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

                    containerInfoDB2RftList.Clear();

                    containerInfoDB2RFT[] bucketInfos = ServiceFactorySmart.getCurrentService().getContainerInfoByBucketNo_DB2(bucketNo);

                    if (bucketInfos == null || bucketInfos.Length == 0)
                    {
                        msgHelper.showWarning("bucket info not found");

                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                        return;
                    }

                    containerInfoDB2RftList.AddRange(bucketInfos);
//                    if (containerInfoDB2RftList.Length > 1)
//                    {
//                        msgHelper.showWarning("multiple records found");
//
//                        txtBucketNo.SelectAll();
//                        txtBucketNo.Focus();
//                        return;
//                    }
                    currentPageNo = 1;

                    showPage();

                    txtBagNo.SelectAll();
                    txtBagNo.Focus();
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

        private void txtBagNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();
                    btnSubmmit_Click(btnSubmmit, null);
                    
                }
                catch (Exception ex)
                {
                    string bagNo = txtBagNo.Text.Trim();

                    clearAll();

                    txtBagNo.Text = bagNo;
                    txtBagNo.SelectAll();
                    txtBagNo.Focus();
                    msgHelper.showError(ex.Message);
                }
            }
        } 

        private void showPage()
        {

            if (containerInfoDB2RftList == null || containerInfoDB2RftList.Count == 0)
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

            if (containerInfoDB2RftList != null && containerInfoDB2RftList.Count > 0)
            {
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
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBucketNo.Focused)
                    {
                        txtBagNo.Focus();
                    }
                    else if (txtBagNo.Focused)
                    {
                        btnSubmmit.Focus();
                    }
                    else if (btnSubmmit.Focused)
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
                        btnSubmmit.Focus();
                    }
                    else if (btnSubmmit.Focused)
                    {
                        txtBagNo.Focus();
                    }
                    else if (txtBagNo.Focused)
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
                    else if (txtBagNo.Focused)
                    {
                        txtBagNo_KeyPress(txtBagNo, eventArgs);
                    }
                    else if (btnSubmmit.Focused)
                    {
                        btnSubmmit_Click(btnSubmmit, eventArgs);
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

        private void btnSubmmit_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                string bucketNo = txtBucketNo.Text.Trim();
                string bagNo = txtBagNo.Text.Trim();

                if (!CommonHelper.isBucketNo(bucketNo))
                {
                    msgHelper.showWarning("invalid bucket no");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }

                if (!CommonHelper.isBagNo(bagNo))
                {
                    msgHelper.showWarning("invalid bag no");

                    txtBagNo.SelectAll();
                    txtBagNo.Focus();
                    return;
                }

                containerInfoDB2RFT[] bucketInfoDB2RftList = ServiceFactorySmart.getCurrentService().getContainerInfoByBucketNo_DB2(bucketNo);

                if (bucketInfoDB2RftList == null)
                {
                    msgHelper.showWarning("bucket info not found");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }

//                if (bucketInfoDB2RftList.Length > 1)
//                {
//                    msgHelper.showWarning("multiple bucket records found");
//
//                    txtBucketNo.SelectAll();
//                    txtBucketNo.Focus();
//                    return;
//                }
                bool bagNoExists = false;
                foreach (containerInfoDB2RFT containerInfoDb2Rft in bucketInfoDB2RftList)
                {
                    if (!string.IsNullOrEmpty(containerInfoDb2Rft.bagNo))
                    {
                        bagNoExists = true;
                        break;
                    }
                }

                if (bagNoExists)
                {
                    msgHelper.showWarning("bag(s) already in the bucket");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();

                    return;
                }

                containerInfoDB2RFT[] bagInfoDB2RftList = ServiceFactorySmart.getCurrentService().getContainerInfoByBagNo_DB2(bagNo);

                if (bagInfoDB2RftList != null && bagInfoDB2RftList.Length > 0)
                {
                    msgHelper.showWarning("bag no already in use");

                    txtBagNo.SelectAll();
                    txtBagNo.Focus();
                    return;
                }

                ServiceFactorySmart.getCurrentService().updateBagNoByBucketNo_DB2(bagNo, bucketNo);

                clearAll();
                msgHelper.showInfo("submit ok");

                txtBucketNo.SelectAll();
                txtBucketNo.Focus();

            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnPrev_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (containerInfoDB2RftList == null || containerInfoDB2RftList.Count == 0)
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

                if (containerInfoDB2RftList == null || containerInfoDB2RftList.Count == 0)
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
    }
}