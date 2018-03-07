using System;
using System.Collections.Generic;
using System.Windows.Forms;
using Barcode;
using wms_rft.Common;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;


namespace wms_rft.Other
{
    public partial class PalletMoveSmartForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        private palletInfoRFT palletInfoRft;
        private int currentPageNo = 0;
        private List<Label> labelBucketNos = new List<Label>();

        public PalletMoveSmartForm()
        {
            InitializeComponent();

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
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void PalletMoveForm_Load(object sender, EventArgs e)
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
            lblFromLocationNo.Text = string.Empty;
            lblFromAreaName.Text = string.Empty;
            lblFromPalletNo.Text = string.Empty;
            lblPageNo.Text = string.Empty;
            foreach (Label labelBucketNo in labelBucketNos)
            {
                labelBucketNo.Text = string.Empty;
            }
            lblMixedLoad.Text = string.Empty;
            lblNumOfBucket.Text = string.Empty;
            lblDayOfStorage.Text = string.Empty;
            txtLocationNo.Text = string.Empty;
            lblToAreaName.Text = string.Empty;
            lblMessage.Text = string.Empty;

            palletInfoRft = null;
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
            }
            else if (txtLocationNo.Focused)
            {
                txtLocationNo.Text = data;
                txtLocationNo.SelectAll();
                txtLocationNo.Focus();
                txtLocationNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
        }

        private void PalletMoveForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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
                    if (bucketNo.Length != txtBucketNo.MaxLength)
                    {
                        msgHelper.showWarning("invalid bucket no");

                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                        return;
                    }

                    palletInfoRft = ServiceFactorySmart.getCurrentService().getPalletInfoByBucketNoForPalletMove(bucketNo);

                    currentPageNo = 1;

                    showPage();

                    txtLocationNo.SelectAll();
                    txtLocationNo.Focus();
                }
                catch (Exception ex)
                {
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void showPage()
        {
            if (palletInfoRft == null || palletInfoRft.bucketNos == null || palletInfoRft.bucketNos.Length == 0)
            {
                currentPageNo = 0;
            }
            else
            {
                currentPageNo = Math.Max(currentPageNo, 1);
            }

            lblFromLocationNo.Text = CommonHelper.locationFormatter(palletInfoRft.locationNo);
            lblFromAreaName.Text = palletInfoRft.areaName;
            lblFromPalletNo.Text = palletInfoRft.palletNo;
            lblMixedLoad.Text = palletInfoRft.mixedLoad.ToString("0");
            lblNumOfBucket.Text = palletInfoRft.numOfBucket.ToString("0");
            lblDayOfStorage.Text = palletInfoRft.dayOfStorage;

            string[] bucketNos = palletInfoRft.bucketNos;

            if (currentPageNo == 0)
            {
                clearAll();
                return;
            }

            int totalPage = bucketNos.Length / labelBucketNos.Count + 1;
            currentPageNo = Math.Min(currentPageNo, totalPage);

            for (int i = 0; i < labelBucketNos.Count; i++)
            {
                int index = (currentPageNo - 1) * labelBucketNos.Count + i;
                labelBucketNos[i].Text = index < bucketNos.Length ? bucketNos[index] : string.Empty;
            }

            lblPageNo.Text = currentPageNo.ToString() + "/" + totalPage;
        }

        private void btnPrev_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (palletInfoRft == null || palletInfoRft.bucketNos == null || palletInfoRft.bucketNos.Length == 0)
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

                if (palletInfoRft == null || palletInfoRft.bucketNos == null || palletInfoRft.bucketNos.Length == 0)
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

        private void txtLocationNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();

                    string locationNo = txtLocationNo.Text.Trim().Replace("-", string.Empty);
                    if (locationNo.Length != txtLocationNo.MaxLength && locationNo.Length != txtLocationNo.MaxLength - 6)
                    {
                        msgHelper.showWarning("invalid location no");

                        txtLocationNo.SelectAll();
                        txtLocationNo.Focus();
                        return;
                    }

                    locationRFT locationRft = ServiceFactorySmart.getCurrentService().getLocationInfoByLocationNoForPalletMove(locationNo);

                    lblToAreaName.Text = locationRft.areaName;

                    btnSubmit_Click(null, null);
                }
                catch (Exception ex)
                {
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void btnSubmit_Click(object sender, EventArgs e)
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

                string locationNo = txtLocationNo.Text.Trim().Replace("-", string.Empty);
                if (locationNo.Length != txtLocationNo.MaxLength && locationNo.Length != txtLocationNo.MaxLength - 6)
                {
                    msgHelper.showWarning("invalid location no");

                    txtLocationNo.SelectAll();
                    txtLocationNo.Focus();
                    return;
                }


                bucketInfoRFT bucketInfoRft = ServiceFactorySmart.getCurrentService().getBucketInfoByBucketNoForBucketInquiry(bucketNo);
                ServiceFactorySmart.getCurrentService().doPalletMove(bucketNo, locationNo);
                //                emptyInfoRFT emptyInfoRft = ServiceFactorySmart.getCurrentService().doPalletMove(bucketNo, locationNo);
                //                if (emptyInfoRft.empty)
                //                {
                //                    Form form = new EmptyConfirmForm(emptyInfoRft);
                //                    form.ShowDialog();
                //                }

                clearAll();
                msgHelper.showInfo("submit ok");

                emptyInfoRFT emptyInfoRft = ServiceFactorySmart.getCurrentService().getEmptyInfo(bucketInfoRft.locationNo.PadRight(13, '0'), string.Empty, string.Empty, string.Empty);
                if (emptyInfoRft.emptyLocation
                    || emptyInfoRft.emptyPallet
                    || emptyInfoRft.emptyBucket
                    || emptyInfoRft.emptyBag)
                {
                    Form form = new EmptyConfirmSmartForm2(emptyInfoRft);
                    form.ShowDialog();
                }

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
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void PalletMoveForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBucketNo.Focused)
                    {
                        btnPrev.Focus();
                    }
                    else if (btnPrev.Focused)
                    {
                        btnNext.Focus();
                    }
                    else if (btnNext.Focused)
                    {
                        txtLocationNo.SelectAll();
                        txtLocationNo.Focus();
                    }
                    else if (txtLocationNo.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
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
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        txtLocationNo.SelectAll();
                        txtLocationNo.Focus();
                    }
                    else if (txtLocationNo.Focused)
                    {
                        btnNext.Focus();
                    }
                    else if (btnNext.Focused)
                    {
                        btnPrev.Focus();
                    }
                    else if (btnPrev.Focused)
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
                    else if (btnPrev.Focused)
                    {
                        btnPrev_Click(btnPrev, eventArgs);
                    }
                    else if (btnNext.Focused)
                    {
                        btnNext_Click(btnNext, eventArgs);
                    }
                    else if (txtLocationNo.Focused)
                    {
                        txtLocationNo_KeyPress(txtLocationNo, eventArgs);
                    }
                    else if (btnSubmit.Focused)
                    {
                        btnSubmit_Click(btnSubmit, eventArgs);
                    }
                    else if (btnCancel.Focused)
                    {
                        btnCancel_Click(btnCancel, eventArgs);
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