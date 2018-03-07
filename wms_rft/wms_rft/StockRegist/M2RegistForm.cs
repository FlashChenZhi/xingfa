using System;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Smart;
using wms_rft.WmsRftSmart;

namespace wms_rft.StockRegist
{
    public partial class M2RegistForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        public M2RegistForm()
        {
            InitializeComponent();
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void M2RegistForm_Load(object sender, EventArgs e)
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
            txtPrTicket.Text = string.Empty;
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
            //            txtBarcode.Text = data;
            if (txtPrTicket.Focused)
            {
                txtPrTicket.Text = data;
                txtPrTicket.SelectAll();
                txtPrTicket.Focus();
                txtPrTicket_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
            else if (txtBucketNo.Focused)
            {
                txtBucketNo.Text = CommonHelper.substringBucketNoOrBagNo(data);
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();
                txtBucketNo_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
            }
        }

        private void M2RegistForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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

        private void txtPrTicket_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();
                    if (doTicketNo(txtPrTicket.Text.Trim()))
                    {

                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                    }
                    else
                    {
                        txtPrTicket.SelectAll();
                        txtPrTicket.Focus();
                    }
                }
                catch (Exception ex)
                {
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
                && (prefix.Length + ticketNo.Length) != txtPrTicket.MaxLength)
            {
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

            return true;
        }

        private void btnSubmit_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                if (!doTicketNo(txtPrTicket.Text.Trim()))
                {
                    return;
                }

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
                    msgHelper.showWarning("invalid bucket no");
                    txtBucketNo.SelectAll();
                    txtBucketNo.Focus();
                    return;
                }

                ServiceFactorySmart.getCurrentService().doM2RegistPhase1(txtPrTicket.Text.Trim(), bucketNo);

                Form form = new WavesCommunicatingForm(txtPrTicket.Text.Trim());
                form.ShowDialog();

                destinationInstructionRFT instruction = ((WavesCommunicatingForm)form).instruction;

                if (instruction == null)
                {
                    clearAll();
                    msgHelper.showError("no instruction");

                    txtPrTicket.SelectAll();
                    txtPrTicket.Focus();
                    return;
                }

                ServiceFactorySmart.getCurrentService().doM2RegistPhase2(txtPrTicket.Text.Trim(), bucketNo, instruction.id);

                form = new LabelPrintForm(txtPrTicket.Text.Trim(), bucketNo);
                form.ShowDialog();

                clearAll();
                msgHelper.showInfo("submit ok");

                txtPrTicket.SelectAll();
                txtPrTicket.Focus();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
                txtBucketNo.SelectAll();
                txtBucketNo.Focus();
            }
        }

        private void txtBucketNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    btnSubmit_Click(null, null);
                }
                catch (Exception ex)
                {
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void M2RegistForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtPrTicket.Focused)
                    {
                        txtBucketNo.SelectAll();
                        txtBucketNo.Focus();
                    }
                    else if (txtBucketNo.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        btnMenu.Focus();
                    }
                    else if (btnMenu.Focused)
                    {
                        txtPrTicket.SelectAll();
                        txtPrTicket.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (txtPrTicket.Focused)
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
                        txtPrTicket.SelectAll();
                        txtPrTicket.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnMenu_Click(btnMenu, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (txtPrTicket.Focused)
                    {
                        txtPrTicket_KeyPress(txtPrTicket, eventArgs);
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