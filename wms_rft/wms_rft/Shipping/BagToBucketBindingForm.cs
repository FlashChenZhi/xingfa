using System;
using System.Collections.Generic;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.StockInquiry
{
    public partial class BagToBucketBindingForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        private List<containerInfoDB2RFT> allContainerInfoDb2Rfts;
        private List<containerInfoDB2RFT> bindingContainerInfoDb2Rfts;
        private int currentPageNo = 0;

        public BagToBucketBindingForm()
        {
            InitializeComponent();
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void BagToBucketBindingForm_Load(object sender, EventArgs e)
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
            txtBagNo.Text = string.Empty;
            lblItemCategory.Text = string.Empty;
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

            allContainerInfoDb2Rfts = new List<containerInfoDB2RFT>() ;
            bindingContainerInfoDb2Rfts = new List<containerInfoDB2RFT>();
          
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

        private void BagToBucketBindingForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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
                    
                    containerInfoDB2RFT[] bucketContainerInfoDb2Rfts = ServiceFactorySmart.getCurrentService().getContainerInfoByBucketNo_DB2(bucketNo);

                    if (bucketContainerInfoDb2Rfts != null && bucketContainerInfoDb2Rfts.Length > 0)
                    {
                        if (allContainerInfoDb2Rfts.Count > 0 &&
                            allContainerInfoDb2Rfts[0].itemCategory != bucketContainerInfoDb2Rfts[0].itemCategory)
                        {
                            msgHelper.showWarning("destination mismatch");

                            txtBagNo.SelectAll();
                            txtBagNo.Focus();
                            return;
                        }

                        allContainerInfoDb2Rfts.Clear();
                        bindingContainerInfoDb2Rfts.Clear();
                        allContainerInfoDb2Rfts.AddRange(bucketContainerInfoDb2Rfts);

                        currentPageNo = 1;

                        showPage();
                    }

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

                    string bagNo = txtBagNo.Text.Trim();

                    if (!CommonHelper.isBagNo(bagNo))
                    {
                        msgHelper.showWarning("invalid bag no");

                        txtBagNo.SelectAll();
                        txtBagNo.Focus();
                        return;
                    }

                    containerInfoDB2RFT[] bagContainerInfoDb2Rfts = ServiceFactorySmart.getCurrentService().getContainerInfoByBagNo_DB2(bagNo);

                    if (bagContainerInfoDb2Rfts != null && bagContainerInfoDb2Rfts.Length > 0)
                    {
//                        if (bagContainerInfoDb2Rfts.Length > 1)
//                        {
//                            msgHelper.showWarning("multiple bag records found");
//
//                            txtBagNo.SelectAll();
//                            txtBagNo.Focus();
//                            return;
//                        }

                        if (allContainerInfoDb2Rfts.Count > 0 && allContainerInfoDb2Rfts[0].itemCategory != bagContainerInfoDb2Rfts[0].itemCategory)
                        {
                            msgHelper.showWarning("destination mismatch");

                            txtBagNo.SelectAll();
                            txtBagNo.Focus();
                            return;
                        }

                        List<string> bagNos = new List<string>();
                        foreach (containerInfoDB2RFT allContainerInfoDb2Rft in allContainerInfoDb2Rfts)
                        {
                            if (!bagNos.Contains(allContainerInfoDb2Rft.bagNo))
                            {
                                bagNos.Add(allContainerInfoDb2Rft.bagNo);
                            }
                        }

                        bool exists = false;
                            
                        foreach (string itBagNo in bagNos)
                        {
                            if (itBagNo == bagContainerInfoDb2Rfts[0].bagNo)
                            {
                                exists = true;
                                break;
                            }
                        }
                            
                        if (!exists)
                        {
                            if (bagNos.Count == 3)
                            {
                                msgHelper.showWarning("more than 10 bags in the bucket");

                                txtBagNo.SelectAll();
                                txtBagNo.Focus();
                                return;
                            }

                            allContainerInfoDb2Rfts.AddRange(bagContainerInfoDb2Rfts);
                            bindingContainerInfoDb2Rfts.AddRange(bagContainerInfoDb2Rfts);
                        }

                        currentPageNo = 1;

                        txtBagNo.Text = string.Empty;
                        
                        showPage();
                    }

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

        private void showPage()
        {
            if (allContainerInfoDb2Rfts == null || allContainerInfoDb2Rfts.Count == 0)
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

            if (allContainerInfoDb2Rfts != null && allContainerInfoDb2Rfts.Count > 0)
            {
                currentPageNo = Math.Min(currentPageNo, allContainerInfoDb2Rfts.Count);

                containerInfoDB2RFT containerInfoDb2Rft = allContainerInfoDb2Rfts[currentPageNo - 1];

                lblItemCategory.Text = CommonHelper.itemCategoryToName(containerInfoDb2Rft.itemCategory);
                lblBagNo.Text = containerInfoDb2Rft.bagNo;
                lblPrNo.Text = containerInfoDb2Rft.prNo;
                lblItemCode.Text = containerInfoDb2Rft.itemCode;
                lblItemName1.Text = containerInfoDb2Rft.itemName1;
                lblItemName2.Text = containerInfoDb2Rft.itemName2;
                lblItemName3.Text = containerInfoDb2Rft.itemName3;
                lblColorCode.Text = containerInfoDb2Rft.colorCode;
                lblQty.Text = containerInfoDb2Rft.qty.ToString("0");
                lblWeight.Text = containerInfoDb2Rft.weight.ToString("0.####");

                lblPageNo.Text = currentPageNo + "/" + allContainerInfoDb2Rfts.Count;
            }
        }

        private void btnPrev_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (allContainerInfoDb2Rfts == null || allContainerInfoDb2Rfts.Count == 0)
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

                if (allContainerInfoDb2Rfts == null || allContainerInfoDb2Rfts.Count == 0)
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

        private void BagToBucketBindingForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBucketNo.Focused)
                    {
                        txtBagNo.SelectAll();
                        txtBagNo.Focus();
                    }
                    else if (txtBagNo.Focused)
                    {
                        btnPrev.Focus();
                    }
                    else if (btnPrev.Focused)
                    {
                        btnNext.Focus();
                    }
                    else if (btnNext.Focused)
                    {
                        btnSubmmit.Focus();
                    }
                    else if (btnSubmmit.Focused)
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
                        btnSubmmit.Focus();
                    }
                    else if (btnSubmmit.Focused)
                    {
                        btnNext.Focus();
                    }
                    else if (btnNext.Focused)
                    {
                        btnPrev.Focus();
                    }
                    else if (btnPrev.Focused)
                    {
                        txtBagNo.SelectAll();
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
                    else if (btnPrev.Focused)
                    {
                        btnPrev_Click(btnPrev, eventArgs);
                    }
                    else if (btnNext.Focused)
                    {
                        btnNext_Click(btnNext, eventArgs);
                    }
                    else if (btnSubmmit.Focused)
                    {
                        btnSubmmit_Click(btnNext, eventArgs);
                    }
                    else if (btnCancel.Focused)
                    {
                        btnCancel_Click(btnNext, eventArgs);
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
                if (bucketNo.Length != txtBucketNo.MaxLength)
                {
                    msgHelper.showWarning("invalid bucket no");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }

                if (bindingContainerInfoDb2Rfts == null || bindingContainerInfoDb2Rfts.Count == 0)
                {
                    msgHelper.showWarning("please input bag no");

                    txtBagNo.SelectAll();
                    txtBagNo.Focus();
                    return;
                }

//                string[] bagNos = new string[bindingContainerInfoDb2Rfts.Count];
//                for (int i = 0; i < bindingContainerInfoDb2Rfts.Count; i++)
//                {
//                    bagNos[i] = bindingContainerInfoDb2Rfts[i].bagNo;
//                }

                List<string> bagNos = new List<string>();
                foreach (containerInfoDB2RFT bindingContainerInfoDb2Rft in bindingContainerInfoDb2Rfts)
                {
                    if (!bagNos.Contains(bindingContainerInfoDb2Rft.bagNo))
                    {
                        bagNos.Add(bindingContainerInfoDb2Rft.bagNo);
                    }
                }

                ServiceFactorySmart.getCurrentService().updateBucketNoByBagNos_DB2(bucketNo, bagNos.ToArray());

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

        private void btnCancel_Click(object sender, EventArgs e)
        {
            try
            {
                clearAll();
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }
    }
}