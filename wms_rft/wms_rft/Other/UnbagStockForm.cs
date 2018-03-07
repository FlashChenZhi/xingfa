using System;
using System.Collections.Generic;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.Other
{
    public partial class UnbagStockForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        private stockRFT stockRft;
        private List<Label> labelBagNos = new List<Label>();

        public UnbagStockForm()
        {
            InitializeComponent();

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

        private void UnbagStockForm_Load(object sender, EventArgs e)
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
            txtBagNo.Text = string.Empty;
            lblFromLocationNo.Text = string.Empty;
            lblFromAreaName.Text = string.Empty;
            lblFromBucketNo.Text = string.Empty;
            lblPrNo.Text = string.Empty;
            lblItemCode.Text = string.Empty;
            lblItemName1.Text = string.Empty;
            lblItemName2.Text = string.Empty;
            lblItemName3.Text = string.Empty;
            lblColorCode.Text = string.Empty;
            lblQty.Text = string.Empty;
            lblWeight.Text = string.Empty;
            lblDayOfStorage.Text = string.Empty;
            txtBucketNo.Text = string.Empty;
            lblToLocationNo.Text = string.Empty;
            lblToAreaName.Text = string.Empty;
            foreach (Label labelBagNo in labelBagNos)
            {
                labelBagNo.Text = string.Empty;
            }
            lblMessage.Text = string.Empty;

            stockRft = null;
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
            if (txtBagNo.Focused)
            {
                txtBagNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtBagNo.SelectAll();
                txtBagNo.Focus();
                txtBagNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
            else if (txtBucketNo.Focused)
            {
                txtBucketNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();
                txtBucketNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
        }

        private void UnbagStockForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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

        private void txtBagNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();

                    string bagNo = txtBagNo.Text.Trim();
//                    if (bagNo.Length != txtBagNo.MaxLength)
                    if(!CommonHelper.isBagNo(bagNo))
                    {
                        msgHelper.showWarning("invalid bag no");

                        txtBagNo.SelectAll();
                        txtBagNo.Focus();
                        return;
                    }

                    stockRft = ServiceFactorySmart.getCurrentService().getStockInfoByBagNoForBagMove(bagNo);

                    showPage();

                    txtBucketNo.Text = stockRft.bucketNo;

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();                   
                }
                catch (Exception ex)
                {
                    msgHelper.showError(ex.Message);
                }
            }

        }

        private void showPage()
        {
            if (stockRft == null)
            {
                return;
            }

            lblFromLocationNo.Text = stockRft.locationNo;
            lblFromAreaName.Text = stockRft.areaName;
            lblFromBucketNo.Text = stockRft.bucketNo;
            lblPrNo.Text = stockRft.ticketNo;
            lblItemCode.Text = stockRft.itemCode;
            lblItemName1.Text = stockRft.itemName1;
            lblItemName2.Text = stockRft.itemName2;
            lblItemName3.Text = stockRft.itemName3;
            lblColorCode.Text = stockRft.colorCode;
            lblQty.Text = stockRft.qty.ToString("0");
            lblWeight.Text = (stockRft.qty * stockRft.unitWeight).ToString("0.####");
            lblDayOfStorage.Text = stockRft.dayOfStorage;

        }

        private void txtBucketNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();

                    string bucketNo = txtBucketNo.Text.Trim();
//                    if (bucketNo.Length != txtBucketNo.MaxLength)
                    if(!CommonHelper.isBucketNo(bucketNo))
                    {
                        msgHelper.showWarning("invalid bucket no");

                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                        return;
                    }

                    bucketInfoRFT bucketInfoRft = ServiceFactorySmart.getCurrentService().getBucketInfoByBucketNoForUnbagStock(bucketNo);

                    if (bucketInfoRft != null)
                    {
                        lblToLocationNo.Text = bucketInfoRft.locationNo;
                        lblToAreaName.Text = bucketInfoRft.areaName;

                        if (bucketInfoRft.stockRfts != null && bucketInfoRft.stockRfts.Length != 0)
                        {
                            for (int i = 0; i < labelBagNos.Count; i++)
                            {
                                labelBagNos[i].Text = i < bucketInfoRft.stockRfts.Length
                                                          ? bucketInfoRft.stockRfts[i].bagNo
                                                          : string.Empty;
                            }
                        }
                    }

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

                string bagNo = txtBagNo.Text.Trim();
//                if (bagNo.Length != txtBagNo.MaxLength)
                if(!CommonHelper.isBagNo(bagNo))
                {
                    msgHelper.showWarning("invalid bag no");

                    txtBagNo.SelectAll();
                    txtBagNo.Focus();
                    return;
                }

                string bucketNo = txtBucketNo.Text.Trim();
                if(!CommonHelper.isBucketNo(bucketNo))
//                if (bucketNo.Length != txtBucketNo.MaxLength)
                {
                    msgHelper.showWarning("invalid bucket no");

                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }
                
                ServiceFactorySmart.getCurrentService().unbagStock(bagNo, bucketNo);

                clearAll();
                msgHelper.showInfo("submit ok");
             
                txtBagNo.SelectAll();
                txtBagNo.Focus();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void UnbagStockForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtBagNo.Focused)
                    {
                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();                        
                    }
                    else if(txtBucketNo.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        btnMenu.Focus();
                    }                    
                    else if (btnMenu.Focused)
                    {
                        txtBagNo.SelectAll();
                        txtBagNo.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (txtBagNo.Focused)
                    {
                        btnMenu.Focus();
                    }
                    else if (btnMenu.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();    
                    }
                    else if (txtBucketNo.Focused)
                    {
                        txtBagNo.SelectAll();
                        txtBagNo.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnMenu_Click(btnMenu, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (txtBagNo.Focused)
                    {
                        txtBagNo_KeyPress(txtBagNo, eventArgs);
                    }
                    else if (txtBucketNo.Focused)
                    {
                        txtBucketNo_KeyPress(txtBucketNo, eventArgs);
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