using System;
using System.Collections.Generic;
using System.Windows.Forms;
using Barcode;
using wms_rft.Common;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.StockOut
{
    public partial class ItemStockOutSmartForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        private jobKeyRFT jobKeyRft;
        private jobDetailInfoRFT jobDetailInfoRft;
        private List<String> priority = new List<string>();
        private List<String> pass = new List<string>();
        private string jobCollectionNo;

        public ItemStockOutSmartForm(jobKeyRFT jobKeyRft, string jobCollectionNo)
        {
            InitializeComponent();
            this.jobKeyRft = jobKeyRft;
            this.jobCollectionNo = jobCollectionNo;
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void ItemStockOutForm_Load(object sender, EventArgs e)
        {
            try
            {
                Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);

                clearAll();
                msgHelper = new MessageHelper(lblMessage);

                initializeBarcodeScanner();

                jobDetailInfoRft = ServiceFactorySmart.getCurrentService().getJobInfoByKey(jobKeyRft);
                if (jobDetailInfoRft == null)
                {
                    jobDetailInfoRft = ServiceFactorySmart.getCurrentService().getNextJobInfoByCollectionJobNo2(jobCollectionNo, jobKeyRft);
                }

                if (jobDetailInfoRft == null)
                {
                    Form form = new CompleteConfirmForm();
                    form.ShowDialog();
                    Close();
                }
                else
                {
                    jobKeyRft = new jobKeyRFT();
                    jobKeyRft.key = jobDetailInfoRft.key;
                    jobKeyRft.bucketNo = jobDetailInfoRft.bucketNo;
                    showPage();
                }

                //                showPage();

                txtBucketNoOrBagNo.Focus();
                txtBucketNoOrBagNo.SelectAll();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void showPage()
        {
            lblLocationNo.Text = CommonHelper.locationFormatter(jobDetailInfoRft.locationNo);
            lblAreaName.Text = jobDetailInfoRft.areaName;
            lblPalletNo.Text = jobDetailInfoRft.palletNo;
            lblBucketNo.Text = string.Format("{0} {1}", jobDetailInfoRft.bucketNo, jobDetailInfoRft.allBucket ? "ALL" : string.Empty);
            lblBagNo.Text = string.Format("{0} {1}", jobDetailInfoRft.bagNo, jobDetailInfoRft.allBag ? "ALL" : string.Empty);
            lblPrNo.Text = jobDetailInfoRft.prNo;
            lblItemCode.Text = jobDetailInfoRft.itemCode;
            lblItemName1.Text = jobDetailInfoRft.itemName1;
            lblItemName2.Text = jobDetailInfoRft.itemName2;
            lblItemName3.Text = jobDetailInfoRft.itemName3;
            lblColorCode.Text = jobDetailInfoRft.colorCode;
            lblQty.Text = string.Format("{0} {1}", jobDetailInfoRft.qty.ToString("0"), jobDetailInfoRft.hasOtherWork ? "*" : string.Empty);
            lblWeight.Text = jobDetailInfoRft.weight.ToString("0.####");
        }

        private void clearAll()
        {
            lblLocationNo.Text = string.Empty;
            lblAreaName.Text = string.Empty;
            lblPalletNo.Text = string.Empty;
            lblBucketNo.Text = string.Empty;
            lblBagNo.Text = string.Empty;
            lblPrNo.Text = string.Empty;
            lblItemCode.Text = string.Empty;
            lblItemName1.Text = string.Empty;
            lblItemName2.Text = string.Empty;
            lblItemName3.Text = string.Empty;
            lblColorCode.Text = string.Empty;
            lblQty.Text = string.Empty;
            lblWeight.Text = string.Empty;
            txtBucketNoOrBagNo.Text = string.Empty;
            txtDestBucketNo.Text = string.Empty;
            txtDestBagNo.Text = string.Empty;
            lblMessage.Text = string.Empty;
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
            if (txtBucketNoOrBagNo.Focused)
            {
                txtBucketNoOrBagNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtBucketNoOrBagNo.SelectAll();
                txtBucketNoOrBagNo.Focus();                
                txtBucketNoOrBagNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
            else if (txtDestBagNo.Focused)
            {
                txtDestBagNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtDestBagNo.SelectAll();
                txtDestBagNo.Focus();
                txtDestBagNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
            else if (txtDestBucketNo.Focused)
            {
                txtDestBucketNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtDestBucketNo.SelectAll();
                txtDestBucketNo.Focus();
                txtDestBucketNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
        }

        private void ItemStockOutForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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

        private void btnSubmit_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                string temp = txtBucketNoOrBagNo.Text.Trim();
                if (temp.Length != txtBucketNoOrBagNo.MaxLength)
                {
                    msgHelper.showWarning("invalid bucket no or bag no");

                    txtBucketNoOrBagNo.SelectAll();
                    txtBucketNoOrBagNo.Focus();
                    return;
                }

                if (!CommonHelper.isBucketNo(temp) && !CommonHelper.isBagNo(temp))
                {
                    msgHelper.showWarning("invalid bucket no or bag no");

                    txtBucketNoOrBagNo.SelectAll();
                    txtBucketNoOrBagNo.Focus();
                    return;
                }

                string destBucketNo = txtDestBucketNo.Text.Trim();
                if (destBucketNo.Length != txtDestBucketNo.MaxLength)
                {
                    msgHelper.showWarning("invalid dest bucket no");

                    txtDestBucketNo.SelectAll();
                    txtDestBucketNo.Focus();
                    return;
                }

                if (!CommonHelper.isBucketNo(destBucketNo))
                {
                    msgHelper.showWarning("invalid dest bucket no");

                    txtDestBucketNo.SelectAll();
                    txtDestBucketNo.Focus();
                    return;
                }

                string destBagNo = string.Empty;
                if (!string.IsNullOrEmpty(txtDestBagNo.Text))
                {
                    destBagNo = txtDestBagNo.Text.Trim();
                    if (destBagNo.Length != txtDestBagNo.MaxLength)
                    {
                        msgHelper.showWarning("invalid dest bag no");

                        txtDestBagNo.SelectAll();
                        txtDestBagNo.Focus();
                        return;
                    }

                    if (!CommonHelper.isBagNo(destBagNo))
                    {
                        msgHelper.showWarning("invalid dest bag no");

                        txtDestBagNo.SelectAll();
                        txtDestBagNo.Focus();
                        return;
                    }
                }

                stockOutResultRFT stockOutResultRft;

                stockRFT stockRft = null;
                bucketInfoRFT bucketInfoRft = null;
                if (CommonHelper.isBucketNo(temp))
                {
                    bucketInfoRft = ServiceFactorySmart.getCurrentService().getBucketInfoByBucketNoForBucketInquiry(temp);
                    stockOutResultRft = ServiceFactorySmart.getCurrentService().doneJob2(jobKeyRft, temp, destBucketNo, destBagNo);
                }
                else
                {
                    stockRft = ServiceFactorySmart.getCurrentService().getStockInfoByBagNoForBagInquiry(temp);
                    stockOutResultRft = ServiceFactorySmart.getCurrentService().doneBagJob2(jobKeyRft, temp, destBucketNo, destBagNo);
                }

                if (!stockOutResultRft.found)
                {
                    msgHelper.showError("job not found");
                }
                else
                {
                    //                    if (stockOutResultRft.jobIdList != null && stockOutResultRft.jobIdList.Length > 0)
                    //                    {
                    //                        foreach (string priorityJobId in stockOutResultRft.jobIdList)
                    //                        {
                    //                            priority.Insert(0, priorityJobId);
                    //                        }
                    //                    }

                    jobKeyRft = stockOutResultRft.nextKey;

                    clearAll();
                    msgHelper.showInfo("submit ok");

                    txtBucketNoOrBagNo.SelectAll();
                    txtBucketNoOrBagNo.Focus();
                }

                if (jobDetailInfoRft != null)
                {
                    //                    string locationNo = jobDetailInfoRft.locationNo.PadRight(13, '0');
                    string locationNo = stockRft != null
                                            ? stockRft.locationNo.PadRight(13, '0')
                                            : bucketInfoRft != null ? bucketInfoRft.locationNo.PadRight(13, '0') : string.Empty;
                    //                    string palletId = string.IsNullOrEmpty(jobDetailInfoRft.palletId) ? string.Empty : jobDetailInfoRft.palletId;
                    string palletId = stockRft != null
                                            ? stockRft.palletId
                                            : bucketInfoRft != null ? bucketInfoRft.palletId : string.Empty;
                    //                    string bucketNo = string.IsNullOrEmpty(jobDetailInfoRft.bucketNo) ? string.Empty : jobDetailInfoRft.bucketNo;
                    string bucketNo = CommonHelper.isBucketNo(temp) ? temp : stockRft == null ? string.Empty : stockRft.bucketNo;
                    //                    string bagNo = string.IsNullOrEmpty(jobDetailInfoRft.bagNo) ? string.Empty : jobDetailInfoRft.bagNo;
                    string bagNo = CommonHelper.isBucketNo(temp) ? string.Empty : temp;
                    emptyInfoRFT emptyInfoRft = ServiceFactorySmart.getCurrentService().getEmptyInfo(locationNo, palletId, bucketNo, bagNo);
                    if (emptyInfoRft.emptyLocation
                        || emptyInfoRft.emptyPallet
                        || emptyInfoRft.emptyBucket
                        || emptyInfoRft.emptyBag)
                    {
                        Form form = new EmptyConfirmSmartForm2(emptyInfoRft);
                        form.ShowDialog();
                    }
                }

                //                jobDetailInfoRft =
                //                        ServiceFactorySmart.getCurrentService()
                //                                      .getNextJobInfoByCollectionJobNo(jobCollectionNo, priority.ToArray(),
                //                                                                             pass.ToArray());

                //                if (jobDetailInfoRft == null)
                //                {
                //                    Form form = new CompleteConfirmForm();
                //                    form.ShowDialog();
                //                    Close();
                //                }
                //                else
                //                {
                //                    jobKeyRft = jobDetailInfoRft.jobId;
                //
                //                    pass.Clear();
                //                    if (jobDetailInfoRft.pass != null && jobDetailInfoRft.pass.Length != 0)
                //                    {
                //                        pass.AddRange(jobDetailInfoRft.pass);
                //                    }
                //
                //                    priority.Clear();
                //                    if (jobDetailInfoRft.priority != null && jobDetailInfoRft.priority.Length != 0)
                //                    {
                //                        priority.AddRange(jobDetailInfoRft.priority);
                //                    }
                //                    showPage();
                //                }

                if (jobKeyRft == null || string.IsNullOrEmpty(jobKeyRft.bucketNo))
                {
                    Form form = new CompleteConfirmForm();
                    form.ShowDialog();
                    Close();
                }
                else
                {
                    jobDetailInfoRft = ServiceFactorySmart.getCurrentService().getJobInfoByKey(jobKeyRft);
                    showPage();
                }

            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnPass_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();



                //                if (jobDetailInfoRft == null)
                //                {
                //                    return;
                //                }

                //                priority.Remove(jobDetailInfoRft.jobId);
                //                pass.Remove(jobDetailInfoRft.jobId);
                //                pass.Add(jobDetailInfoRft.jobId);
                //
                //
                //                jobDetailInfoRft = ServiceFactorySmart.getCurrentService().getNextJobInfoByCollectionJobNo(jobCollectionNo, priority.ToArray(), pass.ToArray());
                //
                //                if (jobDetailInfoRft == null)
                //                {
                //                    Form form = new CompleteConfirmForm();
                //                    form.ShowDialog();
                //                    Close();
                //                }
                //                else
                //                {
                //                    jobKeyRft = jobDetailInfoRft.jobId;
                //
                //                    pass.Clear();
                //                    if (jobDetailInfoRft.pass != null && jobDetailInfoRft.pass.Length != 0)
                //                    {
                //                        pass.AddRange(jobDetailInfoRft.pass);
                //                    }
                //
                //                    priority.Clear();
                //                    if (jobDetailInfoRft.priority != null && jobDetailInfoRft.priority.Length != 0)
                //                    {
                //                        priority.AddRange(jobDetailInfoRft.priority);
                //                    }
                //                    showPage();
                //                }

                jobDetailInfoRft = ServiceFactorySmart.getCurrentService().getNextJobInfoByCollectionJobNo2(jobCollectionNo, jobKeyRft);
                if (jobDetailInfoRft == null)
                {
                    Form form = new CompleteConfirmForm();
                    form.ShowDialog();
                    Close();
                }
                else
                {
                    jobKeyRft = new jobKeyRFT();
                    jobKeyRft.key = jobDetailInfoRft.key;
                    jobKeyRft.bucketNo = jobDetailInfoRft.bucketNo;
                    showPage();
                }
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
                if (MessageBox.Show("确认取消?", "确认", MessageBoxButtons.YesNo, MessageBoxIcon.Question, MessageBoxDefaultButton.Button2) == DialogResult.No)
                {
                    return;
                }
                ServiceFactorySmart.getCurrentService().jobCancel(jobKeyRft);
                clearAll();
                msgHelper.showInfo("cancel ok");
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }

        }

        private void txtBucketNoOrBagNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();

                    string temp = txtBucketNoOrBagNo.Text.Trim();

                    if (!string.IsNullOrEmpty(temp) && temp.Length != txtBucketNoOrBagNo.MaxLength)
                    {
                        msgHelper.showWarning("invalid bucket no or bag no");

                        txtBucketNoOrBagNo.SelectAll();
                        txtBucketNoOrBagNo.Focus();
                        return;
                    }

                    txtDestBucketNo.SelectAll();
                    txtDestBucketNo.Focus();
                }
                catch (Exception ex)
                {
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void txtDestBucketNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();

                    btnSubmit_Click(null, null);
                }
                catch (Exception ex)
                {
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void txtDestBagNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();

                    btnSubmit_Click(null, null);
                }
                catch (Exception ex)
                {
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void ItemStockOutForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBucketNoOrBagNo.Focused)
                    {
                        txtDestBucketNo.SelectAll();
                        txtDestBucketNo.Focus();
                    }
                    else if (txtDestBucketNo.Focused)
                    {
                        txtDestBagNo.SelectAll();
                        txtDestBagNo.Focus();
                    }
                    else if (txtDestBagNo.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        btnPass.Focus();
                    }
                    else if (btnPass.Focused)
                    {
                        btnMenu.Focus();
                    }
                    else if (btnMenu.Focused)
                    {
                        btnCancel.Focus();
                    }
                    else if (btnCancel.Focused)
                    {
                        txtBucketNoOrBagNo.SelectAll();
                        txtBucketNoOrBagNo.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (txtBucketNoOrBagNo.Focused)
                    {
                        btnCancel.Focus();
                    }
                    else if (btnCancel.Focused)
                    {
                        btnMenu.Focus();
                    }
                    else if (btnMenu.Focused)
                    {
                        btnPass.Focus();
                    }
                    else if (btnPass.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        txtDestBagNo.SelectAll();
                        txtDestBagNo.Focus();
                    }
                    else if (txtDestBagNo.Focused)
                    {
                        txtDestBucketNo.SelectAll();
                        txtDestBucketNo.Focus();
                    }
                    else if (txtDestBucketNo.Focused)
                    {
                        txtBucketNoOrBagNo.SelectAll();
                        txtBucketNoOrBagNo.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnMenu_Click(null,null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs =new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (txtBucketNoOrBagNo.Focused)
                    {
                        txtBucketNoOrBagNo_KeyPress(txtBucketNoOrBagNo, eventArgs);
                    }
                    else if (txtDestBucketNo.Focused)
                    {
                        txtDestBucketNo_KeyPress(txtDestBucketNo, eventArgs);
                    }
                    else if (txtDestBagNo.Focused)
                    {
                        txtDestBagNo_KeyPress(txtDestBagNo, eventArgs);
                    }
                    else if (btnSubmit.Focused)
                    {
                        btnSubmit_Click(btnSubmit,eventArgs);
                    }
                    else if (btnPass.Focused)
                    {
                        btnPass_Click(btnPass, eventArgs);
                    }
                    else if (btnMenu.Focused)
                    {
                        btnMenu_Click(btnMenu, eventArgs);
                    }
                    else if (btnCancel.Focused)
                    {
                        btnCancel_Click(btnCancel, eventArgs);
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