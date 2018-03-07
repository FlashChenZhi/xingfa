using System;
using System.Collections.Generic;
using System.Windows.Forms;
using Barcode;
using wms_rft.Common;
using wms_rft.Helper;
using wms_rft.Helper.Et;
using wms_rft.WmsRftEt;

namespace wms_rft.StockOut
{
    public partial class Pallet1FStockOutEtForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        private string jobId;
        private List<Label> labelBucketNos = new List<Label>();
        private palletJobInfoRFT palletJobInfoRft;
        private List<String> priority = new List<string>();
        private List<String> pass = new List<string>();
        private string jobCollectionNo;

        public Pallet1FStockOutEtForm(string jobId, string jobCollectionNo)
        {
            InitializeComponent();
            this.jobId = jobId;
            this.jobCollectionNo = jobCollectionNo;

            labelBucketNos.Add(lblBucketNo1);
            labelBucketNos.Add(lblBucketNo2);
            labelBucketNos.Add(lblBucketNo3);
            labelBucketNos.Add(lblBucketNo4);
            labelBucketNos.Add(lblBucketNo5);
            labelBucketNos.Add(lblBucketNo6);
            labelBucketNos.Add(lblBucketNo7);
            labelBucketNos.Add(lblBucketNo8);
            labelBucketNos.Add(lblBucketNo9);
            labelBucketNos.Add(lblBucketNo10);
            labelBucketNos.Add(lblBucketNo11);
            labelBucketNos.Add(lblBucketNo12);
            labelBucketNos.Add(lblBucketNo13);
            labelBucketNos.Add(lblBucketNo14);
            labelBucketNos.Add(lblBucketNo15);
            labelBucketNos.Add(lblBucketNo16);
            labelBucketNos.Add(lblBucketNo17);
            labelBucketNos.Add(lblBucketNo18);
            labelBucketNos.Add(lblBucketNo19);
            labelBucketNos.Add(lblBucketNo20);
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void Pallet1FStockOutForm_Load(object sender, EventArgs e)
        {
            try
            {
                Text = CommonHelper.formatTitle(Text, Const.SystemCode.ET);

                clearAll();
                msgHelper = new MessageHelper(lblMessage);

                initializeBarcodeScanner();

                palletJobInfoRft = ServiceFactoryEt.getCurrentService().getPalletJobInfoById(jobId);

                showPage();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void showPage()
        {
            lblLocationNo.Text = CommonHelper.locationFormatter(palletJobInfoRft.locationNo);
            lblAreaName.Text = palletJobInfoRft.areaName;
            string[] bucketNos = palletJobInfoRft.bucketNos;
            for (int i = 0; i < labelBucketNos.Count; i++)
            {
                labelBucketNos[i].Text = i < bucketNos.Length ? bucketNos[i] : string.Empty;
            }
            lblItemCode.Text = palletJobInfoRft.itemCode;
            lblItemName1.Text = palletJobInfoRft.itemName1;
            lblItemName2.Text = palletJobInfoRft.itemName2;
            lblItemName3.Text = palletJobInfoRft.itemName3;
            lblColorCode.Text = palletJobInfoRft.colorCode;
            lblQty.Text = palletJobInfoRft.qty.ToString("0");
            lblWeight.Text = palletJobInfoRft.weight.ToString("0.####");
//            lblResultQty.Text = palletJobInfoRft.resultQty.ToString("0");
//            lblPlanQty.Text = palletJobInfoRft.planQty.ToString("0");
            lblResultQty.Text = "0";
            lblPlanQty.Text = lblQty.Text;
            lblNumOfBucket.Text = bucketNos.Length.ToString("0");
        }

        private void clearAll()
        {
            lblLocationNo.Text = string.Empty;
            lblAreaName.Text = string.Empty;
            foreach (Label labelBucketNo in labelBucketNos)
            {
                labelBucketNo.Text = string.Empty;
            }
            lblItemCode.Text = string.Empty;
            lblItemName1.Text = string.Empty;
            lblItemName2.Text = string.Empty;
            lblItemName3.Text = string.Empty;
            lblColorCode.Text = string.Empty;
            lblQty.Text = string.Empty;
            lblWeight.Text = string.Empty;
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

        private void Pallet1FStockOutForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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

                bucketInfoRFT bucketInfoRft = ServiceFactoryEt.getCurrentService().getBucketInfoByBucketNoForBucketInquiry(bucketNo);
                stockOutResultRFT stockOutResultRft = ServiceFactoryEt.getCurrentService().donePalletJob(jobId, bucketNo);

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

                if (palletJobInfoRft != null)
                {
//                    string locationNo = palletJobInfoRft.locationNo.PadRight(13, '0');
                    string locationNo = bucketInfoRft != null
                                            ? bucketInfoRft.locationNo.PadRight(13, '0')
                                            : string.Empty;
//                    string palletId = string.IsNullOrEmpty(palletJobInfoRft.palletId) ? string.Empty : palletJobInfoRft.palletId;
                    string palletId = bucketInfoRft != null
                                            ? bucketInfoRft.palletId
                                            : string.Empty;
                    emptyInfoRFT emptyInfoRft = ServiceFactoryEt.getCurrentService().getEmptyInfo(locationNo, palletId, string.Empty, string.Empty);
                    if (emptyInfoRft.emptyLocation
                        || emptyInfoRft.emptyPallet
                        || emptyInfoRft.emptyBucket
                        || emptyInfoRft.emptyBag)
                    {
                        Form form = new EmptyConfirmEtForm2(emptyInfoRft);
                        form.ShowDialog();
                    }
                }

                palletJobInfoRft =
                        ServiceFactoryEt.getCurrentService()
                                      .getNextPalletJobInfoByCollectionJobNo(jobCollectionNo, priority.ToArray(),
                                                                             pass.ToArray());

                if (palletJobInfoRft == null)
                {
                    Form form = new CompleteConfirmForm();
                    form.ShowDialog();
                    Close();
                }
                else
                {
                    jobId = palletJobInfoRft.jobId;

                    pass.Clear();
                    if (palletJobInfoRft.pass != null && palletJobInfoRft.pass.Length != 0)
                    {
                        pass.AddRange(palletJobInfoRft.pass);
                    }

                    priority.Clear();
                    if (palletJobInfoRft.priority != null && palletJobInfoRft.priority.Length != 0)
                    {
                        priority.AddRange(palletJobInfoRft.priority);
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

                if (palletJobInfoRft == null)
                {
                    return;
                }

                priority.Remove(palletJobInfoRft.jobId);
                pass.Remove(palletJobInfoRft.jobId);
                pass.Add(palletJobInfoRft.jobId);


                palletJobInfoRft = ServiceFactoryEt.getCurrentService().getNextPalletJobInfoByCollectionJobNo(jobCollectionNo, priority.ToArray(), pass.ToArray());

                if (palletJobInfoRft == null)
                {
                    Form form = new CompleteConfirmForm();
                    form.ShowDialog();
                    Close();
                }
                else
                {
                    jobId = palletJobInfoRft.jobId;

                    pass.Clear();
                    if (palletJobInfoRft.pass != null && palletJobInfoRft.pass.Length != 0)
                    {
                        pass.AddRange(palletJobInfoRft.pass);
                    }

                    priority.Clear();
                    if (palletJobInfoRft.priority != null && palletJobInfoRft.priority.Length != 0)
                    {
                        priority.AddRange(palletJobInfoRft.priority);
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

        private void Pallet1FStockOutForm_KeyDown(object sender, KeyEventArgs e)
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