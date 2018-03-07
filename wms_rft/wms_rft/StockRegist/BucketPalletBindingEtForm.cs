using System;
using System.Collections.Generic;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Et;
using wms_rft.WmsRftEt;

namespace wms_rft.StockRegist
{
    public partial class BucketPalletBindingEtForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;
        private List<stockRFT> stocks = new List<stockRFT>();
        private bool isEnd;

        public BucketPalletBindingEtForm()
        {
            InitializeComponent();
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void BucketPalletBindingForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);

            clearAll();
            msgHelper = new MessageHelper(lblMessage);

            try
            {
                initializeBarcodeScanner();
                refresh();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }


        }

        private void refresh()
        {
            if (stocks == null)
            {
                return;
            }

            lblNumOfBucket_U.Text = stocks.Count.ToString("0");

            if (stocks.Count > 0)
            {
                lblPrNo.Text = stocks[stocks.Count - 1].ticketNo;
                lblItemCode.Text = stocks[stocks.Count - 1].itemCode;
                lblItemName1.Text = stocks[stocks.Count - 1].itemName1;
                lblItemName2.Text = stocks[stocks.Count - 1].itemName2;
                lblItemName3.Text = stocks[stocks.Count - 1].itemName3;
                lblColorCode.Text = stocks[stocks.Count - 1].colorCode;
                lblQty.Text = stocks[stocks.Count - 1].qty.ToString("0");
                lblWeight.Text = (stocks[stocks.Count - 1].unitWeight * stocks[stocks.Count - 1].qty).ToString("0.####");
            }

            if (stocks.Count > 1)
            {
                lblLastBucetNo.Text = stocks[stocks.Count - 2].bucketNo;
            }
        }

        private void clearAll()
        {
            txtBucketNo.Text = string.Empty;
            lblLastBucetNo.Text = string.Empty;
            lblPrNo.Text = string.Empty;
            lblNumOfBucket_U.Text = string.Empty;
            lblItemCode.Text = string.Empty;
            lblItemName1.Text = string.Empty;
            lblItemName2.Text = string.Empty;
            lblItemName3.Text = string.Empty;
            lblColorCode.Text = string.Empty;
            lblQty.Text = string.Empty;
            lblWeight.Text = string.Empty;
            txtPalletNoOrBucketNo.Text = string.Empty;
            lblMixedLoad.Text = string.Empty;
            lblNumOfBucket_L.Text = string.Empty;
            lblMessage.Text = string.Empty;

            isEnd = false;
            stocks.Clear();
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
            else if (txtPalletNoOrBucketNo.Focused)
            {
                txtPalletNoOrBucketNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtPalletNoOrBucketNo.SelectAll();
                txtPalletNoOrBucketNo.Focus();
                txtPalletNoOrBucketNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
        }

        private void BucketPalletBindingForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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
                    doBucketNo(txtBucketNo.Text.Trim());
                }
                catch (Exception ex)
                {
                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();

                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void doBucketNo(string bucketNo)
        {
            if (isEnd)
            {
                msgHelper.showWarning("already end");
                return;
            }

            if (bucketNo.Length != txtBucketNo.MaxLength)
            {
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();

                msgHelper.showWarning("invalid bucket no");

                return;
            }

            foreach (stockRFT s in stocks)
            {
                if (s.bucketNo == bucketNo)
                {
                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();

                    msgHelper.showWarning("bucket already added");

                    return;
                }
            }
            stockRFT stock = ServiceFactoryEt.getCurrentService().getOneStockByBucketNo(bucketNo);

            if (!stock.locationNo.StartsWith("W") && !stock.locationNo.StartsWith("R") && !stock.locationNo.StartsWith("I001"))
            {
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();

                msgHelper.showWarning("bucket not at correct location");

                return;
            }

            if (!string.IsNullOrEmpty(stock.bagNo))
            {
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();

                msgHelper.showWarning("there is a bag in the bucket");

                return;
            }

            if (stocks.Count == 0)
            {
                stocks.Add(stock);

                txtBucketNo.SelectAll();
                txtBucketNo.Focus();

                refresh();
                return;
            }

            if (stock.locationNo != stocks[0].locationNo)
            {
                msgHelper.showWarning("can not mix bucket at different location");

                txtBucketNo.SelectAll();
                txtBucketNo.Focus();

                return;
            }

            Dictionary<int, stockRFT> dict = new Dictionary<int, stockRFT>();
            dict.Add(getHashCodeByItemCodeAndColorCode(stock.itemCode, stock.colorCode), stock);

            foreach (stockRFT s in stocks)
            {
                int hashCode = getHashCodeByItemCodeAndColorCode(s.itemCode, s.colorCode);
                if (!dict.ContainsKey(hashCode))
                {
                    dict.Add(hashCode, s);
                }
            }

            if (dict.Keys.Count > 20)
            {
                msgHelper.showWarning("more than 4 mixed-load");

                txtBucketNo.SelectAll();
                txtBucketNo.Focus();
                return;
            }

            stocks.Add(stock);
            txtBucketNo.SelectAll();
            txtBucketNo.Focus();
            refresh();

        }

        private int getHashCodeByItemCodeAndColorCode(string itemCode, string colorCode)
        {
            int result = itemCode != null ? itemCode.GetHashCode() : 0;
            result = 31 * result + (colorCode != null ? colorCode.GetHashCode() : 0);
            return result;
        }

        private void btnEnd_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (stocks.Count == 0)
                {
                    msgHelper.showWarning("please input at least 1 bucket");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }
                isEnd = true;
                txtPalletNoOrBucketNo.SelectAll();
                txtPalletNoOrBucketNo.Focus();
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
                refresh();
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void txtPalletNoOrBucketNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();
                    if (CommonHelper.isBucketNo(txtPalletNoOrBucketNo.Text.Trim()))
                    {
                        doToBucketNo(txtPalletNoOrBucketNo.Text.Trim());
                    }
                    else
                    {
                        doToPalletNo(txtPalletNoOrBucketNo.Text.Trim());
                    }
                }
                catch (Exception ex)
                {
                    txtPalletNoOrBucketNo.SelectAll();
                    txtPalletNoOrBucketNo.Focus();

                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void doToPalletNo(string palletNo)
        {
            if (palletNo.Length != txtPalletNoOrBucketNo.MaxLength)
            {
                txtPalletNoOrBucketNo.SelectAll();
                txtPalletNoOrBucketNo.Focus();

                msgHelper.showWarning("invalid pallet no");

                return;
            }

            palletInfoRFT pallet = ServiceFactoryEt.getCurrentService().peekPalletInfoByPalletNo(palletNo);
            lblMixedLoad.Text = pallet.mixedLoad.ToString("0");
            lblNumOfBucket_L.Text = pallet.numOfBucket.ToString("0");
        }

        private void doToBucketNo(string bucketNo)
        {
            palletInfoRFT pallet = ServiceFactoryEt.getCurrentService().peekPalletInfoByBucketNo(bucketNo);
            lblMixedLoad.Text = pallet.mixedLoad.ToString("0");
            lblNumOfBucket_L.Text = pallet.numOfBucket.ToString("0");
        }

        private void btnSubmit_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (stocks.Count == 0)
                {
                    msgHelper.showWarning("please input at least 1 bucket");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }

                if (!isEnd)
                {
                    msgHelper.showWarning("please set end first");
                    return;
                }

                string[] bucketNos = new string[stocks.Count];
                for (int i = 0; i < stocks.Count; i++)
                {
                    bucketNos[i] = stocks[i].bucketNo;
                }

                string temp = txtPalletNoOrBucketNo.Text.Trim();
                if (CommonHelper.isBucketNo(temp))
                {
                    ServiceFactoryEt.getCurrentService().bindPalletAndBucketsByBucketNo(bucketNos, temp);
                }
                else
                {
                    if (temp.Length != txtPalletNoOrBucketNo.MaxLength)
                    {
                        txtPalletNoOrBucketNo.SelectAll();
                        txtPalletNoOrBucketNo.Focus();

                        msgHelper.showWarning("invalid pallet no or bucket no");

                        return;
                    }

                    ServiceFactoryEt.getCurrentService().bindPalletAndBucketsByPalletNo(bucketNos, temp);
                }
                
                clearAll();
                msgHelper.showInfo("submit ok");
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void BucketPalletBindingForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBucketNo.Focused)
                    {
                        btnEnd.Focus();
                    }
                    else if (btnEnd.Focused)
                    {
                        btnCancel.Focus();
                    }
                    else if (btnCancel.Focused)
                    {
                        txtPalletNoOrBucketNo.SelectAll();
                        txtPalletNoOrBucketNo.Focus();
                    }
                    else if (txtPalletNoOrBucketNo.Focused)
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
                        txtPalletNoOrBucketNo.SelectAll();
                        txtPalletNoOrBucketNo.Focus();
                    }
                    else if (txtPalletNoOrBucketNo.Focused)
                    {
                        btnCancel.Focus();
                    }
                    else if (btnCancel.Focused)
                    {
                        btnEnd.Focus();
                    }
                    else if (btnEnd.Focused)
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
                    else if (btnEnd.Focused)
                    {
                        btnEnd_Click(btnEnd, eventArgs);
                    }
                    else if (btnCancel.Focused)
                    {
                        btnCancel_Click(btnCancel, eventArgs);
                    }
                    else if (txtPalletNoOrBucketNo.Focused)
                    {
                        txtPalletNoOrBucketNo_KeyPress(txtPalletNoOrBucketNo, eventArgs);
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