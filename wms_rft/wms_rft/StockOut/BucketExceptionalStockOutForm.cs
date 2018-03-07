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
    public partial class BucketExceptionalStockOutForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        private string jobId;
        private List<Label> labelBagNos = new List<Label>();
        private jobDetailInfoRFT jobDetailInfoRft;
        private List<String> priority = new List<string>();
        private List<String> pass = new List<string>();
        private string jobCollectionNo;

        public BucketExceptionalStockOutForm(string jobId, string jobCollectionNo)
        {
            InitializeComponent();
            this.jobId = jobId;
            this.jobCollectionNo = jobCollectionNo;

            labelBagNos.Add(lblBagNo1);
            labelBagNos.Add(lblBagNo2);
            labelBagNos.Add(lblBagNo3);
            labelBagNos.Add(lblBagNo4);
            labelBagNos.Add(lblBagNo5);
            labelBagNos.Add(lblBagNo6);
            labelBagNos.Add(lblBagNo7);
            labelBagNos.Add(lblBagNo8);
            labelBagNos.Add(lblBagNo9);
            labelBagNos.Add(lblBagNo10);
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void BucketExceptionalStockOutForm_Load(object sender, EventArgs e)
        {
            try
            {
                Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);

                clearAll();
                msgHelper = new MessageHelper(lblMessage);

                initializeBarcodeScanner();

                jobDetailInfoRft = ServiceFactorySmart.getCurrentService().getJobInfoById(jobId);

                showPage();
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
            lblBucketNo.Text = jobDetailInfoRft.bucketNo;
            string[] bagNos = jobDetailInfoRft.bagNos;
            if (bagNos != null)
            {
                for (int i = 0; i < labelBagNos.Count; i++)
                {
                    labelBagNos[i].Text = i < bagNos.Length ? bagNos[i] : string.Empty;
                }
                lblNumOfBucket.Text = bagNos.Length.ToString("0");
            }
            lblResultQty.Text = "0";
            lblPlanQty.Text = "0";
        }

        private void clearAll()
        {
            lblLocationNo.Text = string.Empty;
            lblAreaName.Text = string.Empty;
            lblBucketNo.Text = string.Empty;
            foreach (Label labelBagNo in labelBagNos)
            {
                labelBagNo.Text = string.Empty;
            }
            lblResultQty.Text = string.Empty;
            lblPlanQty.Text = string.Empty;
            lblNumOfBucket.Text = string.Empty;
            txtBucketNo.Text = string.Empty;
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
            txtBucketNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
            txtBucketNo.SelectAll();
            txtBucketNo.Focus();
            txtBucketNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
        }

        private void BucketExceptionalStockOutForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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
                msgHelper = new MessageHelper(lblMessage);

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
                    msgHelper.showWarning("invalid bucket");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }

                if (jobDetailInfoRft == null)
                {
                    msgHelper.showWarning("job not found");
                    return;
                }

                if (jobDetailInfoRft.bucketNo != bucketNo)
                {
                    msgHelper.showWarning("please choose assigned bucket");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }

                 stockOutResultRFT stockOutResultRft = ServiceFactorySmart.getCurrentService().doneJob(jobId, bucketNo, string.Empty, string.Empty);


                if (!stockOutResultRft.found)
                {
                    msgHelper.showError("job not found");
                }
                else
                {
                    if (stockOutResultRft.jobIdList != null && stockOutResultRft.jobIdList.Length > 0)
                    {
                        foreach (string priorityJobId in stockOutResultRft.jobIdList)
                        {
                            priority.Insert(0, priorityJobId);
                        }
                    }

                    clearAll();
                    msgHelper.showInfo("submit ok");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                }

                if (jobDetailInfoRft != null)
                {
                    string locationNo = jobDetailInfoRft.locationNo.PadRight(13, '0');
                    string palletId = string.IsNullOrEmpty(jobDetailInfoRft.palletId) ? string.Empty : jobDetailInfoRft.palletId;
                    bucketNo = string.IsNullOrEmpty(jobDetailInfoRft.bucketNo) ? string.Empty : jobDetailInfoRft.bucketNo;
                    string bagNo = string.IsNullOrEmpty(jobDetailInfoRft.bagNo) ? string.Empty : jobDetailInfoRft.bagNo;
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

                jobDetailInfoRft =
                        ServiceFactorySmart.getCurrentService()
                                      .getNextJobInfoByCollectionJobNo(jobCollectionNo, priority.ToArray(),
                                                                             pass.ToArray());

                if (jobDetailInfoRft == null)
                {
                    Form form = new CompleteConfirmForm();
                    form.ShowDialog();
                    Close();
                }
                else
                {
                    jobId = jobDetailInfoRft.jobId;

                    pass.Clear();
                    if (jobDetailInfoRft.pass != null && jobDetailInfoRft.pass.Length != 0)
                    {
                        pass.AddRange(jobDetailInfoRft.pass);
                    }

                    priority.Clear();
                    if (jobDetailInfoRft.priority != null && jobDetailInfoRft.priority.Length != 0)
                    {
                        priority.AddRange(jobDetailInfoRft.priority);
                    }
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
                msgHelper = new MessageHelper(lblMessage);

                if (jobDetailInfoRft == null)
                {
                    return;
                }

                priority.Remove(jobDetailInfoRft.jobId);
                pass.Remove(jobDetailInfoRft.jobId);
                pass.Add(jobDetailInfoRft.jobId);


                jobDetailInfoRft = ServiceFactorySmart.getCurrentService().getNextJobInfoByCollectionJobNo(jobCollectionNo, priority.ToArray(), pass.ToArray());

                if (jobDetailInfoRft == null)
                {
                    Form form = new CompleteConfirmForm();
                    form.ShowDialog();
                    Close();
                }
                else
                {
                    jobId = jobDetailInfoRft.jobId;

                    pass.Clear();
                    if (jobDetailInfoRft.pass != null && jobDetailInfoRft.pass.Length != 0)
                    {
                        pass.AddRange(jobDetailInfoRft.pass);
                    }

                    priority.Clear();
                    if (jobDetailInfoRft.priority != null && jobDetailInfoRft.priority.Length != 0)
                    {
                        priority.AddRange(jobDetailInfoRft.priority);
                    }
                    showPage();
                }
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void txtBucketNo_KeyPress(object sender, KeyPressEventArgs e)
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

        private void BucketExceptionalStockOutForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBucketNo.Focused)
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
                        btnPass.Focus();
                    }
                    else if (btnPass.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
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
                    else if (txtBucketNo.Focused)
                    {
                        txtBucketNo_KeyPress(txtBucketNo, eventArgs);
                    }
                    else if (btnSubmit.Focused)
                    {
                        btnSubmit_Click(btnSubmit, eventArgs);
                    }
                    else if (btnPass.Focused)
                    {
                        btnPass_Click(btnPass, eventArgs);
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