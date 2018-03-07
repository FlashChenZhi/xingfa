using System;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.StockOut
{
    public partial class JobDataInquirySmartForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        private jobInfoRFT[] jobInfoRfts;
        private int currentPageNo = 0;

        public JobDataInquirySmartForm()
        {
            InitializeComponent();
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void JobDataInquiryForm_Load(object sender, EventArgs e)
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
            txtBarcode.Text = string.Empty;
            lblLocationNo.Text = string.Empty;
            lblPageNo.Text = string.Empty;
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
            lblResultQty.Text = string.Empty;
            lblPlanQty.Text = string.Empty;
            lblWeight.Text = string.Empty;
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
            txtBarcode.Text = data;
            txtBarcode.SelectAll();
            txtBarcode.Focus();
            txtBarcode_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
        }

        private void JobDataInquiryForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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
                disposeBarcodeScanner();

                msgHelper.clear();

                if (jobInfoRfts == null || currentPageNo > jobInfoRfts.Length)
                {
                    return;
                }

                jobInfoRFT jobInfoRft = jobInfoRfts[currentPageNo - 1];

                string prefix = jobInfoRft.locationNo.Substring(0, 1);
                if ((prefix == "C" || prefix == "D") && !jobInfoRft.exception)
                {
                    Form form = new Pallet1FStockOutSmartForm(jobInfoRft.jobId, jobInfoRft.jobCollectionNo);
                    form.Text = jobInfoRft.replenish ? "1F托盘补充作业" : "1F托盘移动作业";
                    form.ShowDialog();
                    clearAll();
                }
                else if ((prefix == "T" || prefix == "K") && jobInfoRft.replenish)
                {
                    Form form = new BZ2FStockOutForm(jobInfoRft.jobId, jobInfoRft.jobCollectionNo);
                    form.ShowDialog();
                    clearAll();
                }
                else if (!jobInfoRft.exception)
                {
                    jobKeyRFT jobKeyRft = new jobKeyRFT();
                    jobKeyRft.bucketNo = jobInfoRft.bucketNo;
                    jobKeyRft.key = jobInfoRft.key;
                    Form form = new ItemStockOutSmartForm(jobKeyRft, jobInfoRft.jobCollectionNo);
                    
                    form.ShowDialog();
                    clearAll();
                }
                else if (jobInfoRft.exception)
                {
                    if (!string.IsNullOrEmpty(jobInfoRft.bucketNo) && string.IsNullOrEmpty(jobInfoRft.bagNo))
                    {
                        Form form = new BucketExceptionalStockOutForm(jobInfoRft.jobId, jobInfoRft.jobCollectionNo);
                        form.ShowDialog();
                        clearAll();
                    }
                }

            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
            finally
            {
                initializeBarcodeScanner();
            }
            
        }

        private void txtBarcode_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();                    

                    string barcode = txtBarcode.Text.Trim();
                    if (string.IsNullOrEmpty(barcode))
                    {
                        txtBarcode.SelectAll();
                        txtBarcode.Focus();
                        return;
                    }

                    clearAll();
                    txtBarcode.Text = barcode;

                    jobInfoRfts = ServiceFactorySmart.getCurrentService().getJobInfoListByBarcode2(barcode);

                    currentPageNo = 1;

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
            if (jobInfoRfts == null || jobInfoRfts.Length == 0)
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

            currentPageNo = Math.Min(currentPageNo, jobInfoRfts.Length);

            jobInfoRFT info = jobInfoRfts[currentPageNo - 1];

            lblLocationNo.Text = CommonHelper.locationFormatter(info.locationNo);
            lblPalletNo.Text = info.palletNo;
            lblBucketNo.Text = info.bucketNo;
            lblBagNo.Text = info.bagNo;
            lblPrNo.Text = info.prNo;
            lblItemCode.Text = info.itemCode;
            lblItemName1.Text = info.itemName1;
            lblItemName2.Text = info.itemName2;
            lblItemName3.Text = info.itemName3;
            lblColorCode.Text = info.colorCode;
            lblQty.Text = info.qty.ToString();
            lblResultQty.Text = info.resultQty.ToString();
            lblPlanQty.Text = info.planQty.ToString();
            lblWeight.Text = info.weight.ToString("0.####");

            lblPageNo.Text = currentPageNo.ToString() + "/" + jobInfoRfts.Length;
        }

        private void btnNext_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (jobInfoRfts == null || jobInfoRfts.Length == 0)
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

        private void btnPrev_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (jobInfoRfts == null || jobInfoRfts.Length == 0)
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

        private void JobDataInquiryForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBarcode.Focused)
                    {
                        btnPrev.Focus();
                    }
                    else if (btnPrev.Focused)
                    {
                        btnNext.Focus();
                    }
                    else if (btnNext.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        btnMenu.Focus();
                    }                  
                    else if (btnMenu.Focused)
                    {
                        txtBarcode.SelectAll();
                        txtBarcode.Focus();
                    }                   
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (txtBarcode.Focused)
                    {
                        btnMenu.Focus();
                    }
                    else if (btnMenu.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        btnNext.Focus();
                    }
                    else if (btnNext.Focused)
                    {
                        btnPrev.Focus();
                    }
                    else if (btnPrev.Focused)
                    {
                        txtBarcode.SelectAll();
                        txtBarcode.Focus();
                    }                   
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnMenu_Click(btnMenu, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (txtBarcode.Focused)
                    {
                        txtBarcode_KeyPress(txtBarcode, eventArgs);
                    }
                    else if (btnPrev.Focused)
                    {
                        btnPrev_Click(btnPrev, eventArgs);
                    }
                    else if (btnNext.Focused)
                    {
                        btnNext_Click(btnNext, eventArgs);
                    }                   
                    else if (btnSubmit.Focused)
                    {
                        btnSubmit_Click(btnSubmit, eventArgs);
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