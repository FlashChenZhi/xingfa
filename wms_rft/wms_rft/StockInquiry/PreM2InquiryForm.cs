using System;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.StockInquiry
{
    public partial class PreM2InquiryForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;
        private stockRFT[] stockRfts = null;
        private int currentPageNo = 0;

        public PreM2InquiryForm()
        {
            InitializeComponent();
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void PreM2InquiryForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);

            clearAll();
            msgHelper = new MessageHelper(lblMessage);

            try {
                initializeBarcodeScanner();
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }

        private void clearAll()
        {
            txtBucketNo.Text = string.Empty;
            txtPrTicket.Text = string.Empty;
            lblBucketNo.Text = string.Empty;
            lblItemCode.Text = string.Empty;
            lblItemName1.Text = string.Empty;
            lblItemName2.Text = string.Empty;
            lblItemName3.Text = string.Empty;
            lblColorCode.Text = string.Empty;
            lblDepotCode.Text = string.Empty;
            lblQty.Text = string.Empty;
            lblWeight.Text = string.Empty;
            lblLine1.Text = string.Empty;
            lblLine2.Text = string.Empty;
            lblPrNo.Text = string.Empty;
            lblSettingCount.Text = string.Empty;
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
            //            txtBarcode.Text = data;
            if (txtPrTicket.Focused) {
                txtPrTicket.Text = data;
                txtPrTicket.SelectAll();
                txtPrTicket.Focus();
                txtPrTicket_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            } else if (txtBucketNo.Focused) {
                txtBucketNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();
                txtBucketNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
        }

        private void PreM2InquiryForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            try {
                disposeBarcodeScanner();
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }

        private void disposeBarcodeScanner()
        {
            if (barcodeScanner != null) {
                barcodeScanner.Dispose();
            }
        }

        private void txtPrTicket_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter)) {
                try {
                    msgHelper.clear();

                    //                    if (doTicketNo(txtPrTicket.Text.Trim())) {
                    //
                    //                        txtBucketNo.SelectAll();
                    //                        txtBucketNo.Focus();
                    //                    } else {
                    //                        txtPrTicket.SelectAll();
                    //                        txtPrTicket.Focus();
                    //                    }

                    if (!doTicketNo(txtPrTicket.Text.Trim())) {
                        txtPrTicket.SelectAll();
                        txtPrTicket.Focus();
                    }
                    else{
                        btnInquiry.Focus();
                    }
                } catch (Exception ex) {
                    string temp = txtPrTicket.Text;
                    clearAll();
                    txtPrTicket.Text = temp;
                    txtPrTicket.SelectAll();
                    txtPrTicket.Focus();

                    msgHelper.showError(ex.Message);
                }
            }
        }

        private bool doTicketNo(string ticketNo)
        {
            string prefix = "PR";
            //                string ticketNo = txtPrTicket.Text.Trim();
            if (ticketNo.Length != txtPrTicket.MaxLength
                && (prefix.Length + ticketNo.Length) != txtPrTicket.MaxLength) {
                msgHelper.showWarning("invalid pr.ticket");
                return false;
            }

            ticketNo = ticketNo.Length == txtPrTicket.MaxLength ? ticketNo : prefix + ticketNo;
            txtPrTicket.Text = ticketNo;

            stockRFT stock = ServiceFactorySmart.getCurrentService().getUnregistStockByTicketNo(ticketNo);

            lblItemCode.Text = stock.itemCode;
            lblItemName1.Text = stock.itemName1;
            lblItemName2.Text = stock.itemName2;
            lblItemName3.Text = stock.itemName3;
            lblColorCode.Text = stock.colorCode;
            lblDepotCode.Text = stock.depotCode;
            lblQty.Text = stock.planQty.ToString("0");
            lblWeight.Text = stock.planWeight.ToString("0.####");
            lblLine1.Text = stock.line1;
            lblLine2.Text = stock.line2;
            lblPrNo.Text = ticketNo;


            return true;
        }

        //        private void btnSubmit_Click(object sender, EventArgs e)
        //        {
        //            try {
        //                msgHelper.clear();
        //
        //                string ticketNo = txtPrTicket.Text.Trim();
        //
        //                if (ticketNo != "9999999999"
        //                    && !doTicketNo(ticketNo)) {
        //                    return;
        //                }
        //
        //                string bucketNo = txtBucketNo.Text.Trim();
        //                if (bucketNo.Length != txtBucketNo.MaxLength) {
        //                    msgHelper.showWarning("invalid bucket no");
        //                    txtBucketNo.SelectAll();
        //                    txtBucketNo.Focus();
        //                    return;
        //                }
        //
        //                if (!CommonHelper.isBucketNo(bucketNo)) {
        //                    msgHelper.showWarning("invalid bucket no");
        //                    txtBucketNo.SelectAll();
        //                    txtBucketNo.Focus();
        //                    return;
        //                }
        //
        //                decimal planWeight;
        //                try {
        //                    planWeight = decimal.Parse(txtPlanWeight.Text);
        //                } catch (Exception) {
        //                    msgHelper.showWarning("invalid weight");
        //                    txtPlanWeight.SelectAll();
        //                    txtPlanWeight.Focus();
        //                    return;
        //                }
        //
        //                if (planWeight == 0) {
        //                    msgHelper.showWarning("invalid weight");
        //                    txtPlanWeight.SelectAll();
        //                    txtPlanWeight.Focus();
        //                    return;
        //                }
        //
        //                string[] tokens = txtPlanWeight.Text.Split('.');
        //
        //                if (tokens.Length > 0 && tokens[0].Length > 2) {
        //                    msgHelper.showWarning("invalid weight");
        //                    txtPlanWeight.SelectAll();
        //                    txtPlanWeight.Focus();
        //                    return;
        //                }
        //
        //                if (tokens.Length > 1 && tokens[1].Length > 4) {
        //                    msgHelper.showWarning("invalid weight");
        //                    txtPlanWeight.SelectAll();
        //                    txtPlanWeight.Focus();
        //                    return;
        //                }
        //
        //                ServiceFactorySmart.getCurrentService().doPreM2(ticketNo, bucketNo, planWeight, true);
        //
        //                clearAll();
        //                msgHelper.showInfo("submit ok");
        //
        //                txtBucketNo.SelectAll();
        //                txtBucketNo.Focus();
        //            } catch (Exception ex) {
        //                msgHelper.showError(ex.Message);
        //                txtBucketNo.SelectAll();
        //                txtBucketNo.Focus();
        //            }
        //        }

        private void txtBucketNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter)) {
                try {
                    //                    btnSubmit_Click(null, null);
                } catch (Exception ex) {
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void PreM2InquiryForm_KeyDown(object sender, KeyEventArgs e)
        {
            try {
                if (e.KeyCode == Keys.Down) {
                    if (txtBucketNo.Focused) {
                        txtPrTicket.SelectAll();
                        txtPrTicket.Focus();
                    } else if (txtPrTicket.Focused) {
                        btnInquiry.Focus();
                    } else if (btnInquiry.Focused) {
                        btnMenu.Focus();
                    } else if (btnMenu.Focused) {
                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                    }
                } else if (e.KeyCode == Keys.Up) {
                    if (txtBucketNo.Focused) {
                        btnMenu.Focus();
                    } else if (btnMenu.Focused) {
                        btnInquiry.Focus();
                    } else if (btnInquiry.Focused) {
                        txtPrTicket.SelectAll();
                        txtPrTicket.Focus();
                    } else if (txtPrTicket.Focused) {
                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                    }

                } else if (e.KeyValue == 64)//L Button
                {
                    btnMenu_Click(btnMenu, null);
                } else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (txtPrTicket.Focused) {
                        txtPrTicket_KeyPress(txtPrTicket, eventArgs);
                    } else if (txtBucketNo.Focused) {
                        txtBucketNo_KeyPress(txtBucketNo, eventArgs);
                    } else if (btnMenu.Focused) {
                        btnMenu_Click(btnMenu, eventArgs);
                    } else if (btnInquiry.Focused) {
                        btnInquiry_Click(btnInquiry, eventArgs);
                    }
                }
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnInquiry_Click(object sender, EventArgs e)
        {
            try {
                msgHelper.clear();

                string ticketNo = txtPrTicket.Text.Trim();
                string bucketNo = txtBucketNo.Text.Trim();

                stockRfts = ServiceFactorySmart.getCurrentService().getPreM2Stock(bucketNo, ticketNo);

                currentPageNo = 1;

                showPage();
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
                btnInquiry.Focus();
            }
        }

        private void showPage()
        {
            if (stockRfts == null || stockRfts.Length == 0) {
                currentPageNo = 0;
            } else {
                currentPageNo = Math.Max(currentPageNo, 1);
            }

            if (currentPageNo == 0) {
                clearAll();
                return;
            }

            currentPageNo = Math.Min(currentPageNo, stockRfts.Length);

            stockRFT stockRft = stockRfts[currentPageNo - 1];

            lblBucketNo.Text = stockRft.bucketNo;
            lblItemCode.Text = stockRft.itemCode;
            lblItemName1.Text = stockRft.itemName1;
            lblItemName2.Text = stockRft.itemName2;
            lblItemName3.Text = stockRft.itemName3;
            lblColorCode.Text = stockRft.colorCode;
            lblDepotCode.Text = stockRft.depotCode;
            lblQty.Text = stockRft.planQty.ToString("0");
            lblWeight.Text = stockRft.planWeight.ToString("0.####");
            lblLine1.Text = stockRft.line1;
            lblLine2.Text = stockRft.line2;
            lblPrNo.Text = stockRft.ticketNo;
            lblSettingCount.Text = stockRfts.Length.ToString("0");
        }

        private void btnPrev_Click(object sender, EventArgs e)
        {
            try {
                msgHelper.clear();

                if (stockRfts == null || stockRfts.Length == 0) {
                    return;
                }

                currentPageNo--;
                showPage();
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnNext_Click(object sender, EventArgs e)
        {
            try {
                msgHelper.clear();

                if (stockRfts == null || stockRfts.Length == 0) {
                    return;
                }

                currentPageNo++;
                showPage();
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }
    }
}