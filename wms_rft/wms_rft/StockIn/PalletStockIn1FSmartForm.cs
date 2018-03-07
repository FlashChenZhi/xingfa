using System;
using System.Collections.Generic;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.StockIn
{
    public partial class PalletStockIn1FSmartForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        private palletInfoRFT palletInfoRft;
        private List<Label> labelBucketNos = new List<Label>();

        public PalletStockIn1FSmartForm()
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

        private void PalletStockIn1FForm_Load(object sender, EventArgs e)
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
            foreach (Label labelBucketNo in labelBucketNos)
            {
                labelBucketNo.Text = string.Empty;
            }
            lblMixedLoad.Text = string.Empty;
            lblNumOfBucket.Text = string.Empty;
            txtLocationNo.Text = string.Empty;
            lblAreaName.Text = string.Empty;
            lblRecommendLocationNo.Text = string.Empty;
            lblMessage.Text = string.Empty;

            palletInfoRft = null;
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

        private void PalletStockIn1FForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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
                        msgHelper.showWarning("invalid pallet no");

                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                        return;
                    }

                    palletInfoRft = ServiceFactorySmart.getCurrentService().getPalletInfoByBucketNoForPalletStockIn1F(bucketNo);

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
                return;
            }

            lblMixedLoad.Text = palletInfoRft.mixedLoad.ToString("0");
            lblNumOfBucket.Text = palletInfoRft.numOfBucket.ToString("0");
            lblRecommendLocationNo.Text = string.Format("{0}({1})", CommonHelper.locationFormatter(palletInfoRft.recommendLocationNo),
                                                        palletInfoRft.recommendAreaName);

            string[] bucketNos = palletInfoRft.bucketNos;

            for (int i = 0; i < labelBucketNos.Count; i++)
            {
                labelBucketNos[i].Text = i < bucketNos.Length ? bucketNos[i] : string.Empty;
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

                    locationRFT locationRft = ServiceFactorySmart.getCurrentService().getLocationInfoByLocationNoForPalletStockIn1F(locationNo);

                    lblAreaName.Text = locationRft.areaName;

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
                    msgHelper.showWarning("invalid pallet no");

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

                ServiceFactorySmart.getCurrentService().doPalletStockIn1F(bucketNo, locationNo);

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

        private void PalletStockIn1FForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBucketNo.Focused)
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
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        txtLocationNo.SelectAll();
                        txtLocationNo.Focus();
                    }
                    else if (txtLocationNo.Focused)
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
                    else if (txtLocationNo.Focused)
                    {
                        txtLocationNo_KeyPress(txtLocationNo, eventArgs);
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
