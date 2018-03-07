using System;
using System.Windows.Forms;
using Barcode;
using wms_rft.Helper;
using wms_rft.Helper.Et;
using wms_rft.WmsRftEt;

namespace wms_rft.StockOut
{
    public partial class AgvStockInOutEtForm : Form
    {
        private BarcodeScanner barcodeScanner;
        delegate void setBarcodeDelegate(string data, string type);
        private MessageHelper msgHelper;

        public AgvStockInOutEtForm()
        {
            InitializeComponent();
        }

        private void btnMenu_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void AgvStockInOutEtForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.ET);

            txtCoatingMachineCode.Text = string.Empty;
            lblCoatingMachineShortName.Text = string.Empty;

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
            //            txtCoatingMachineCode.Text = string.Empty;
            //            lblCoatingMachineShortName.Text = string.Empty;

            rdoSupply.Checked = true;
            rdoCoatingColor.Checked = true;
            lblRetrievalAvailableQty.Text = string.Empty;
            txtSupplySettingQty.Text = string.Empty;
            lblRetrievingBucketCount.Text = string.Empty;
            lblCollectingBucketCount.Text = string.Empty;
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
            txtCoatingMachineCode.Text = data;
            txtCoatingMachineCode.SelectAll();
            txtCoatingMachineCode.Focus();
            txtCoatingMachineCode_KeyPress(null, new KeyPressEventArgs(Convert.ToChar(Keys.Enter)));
        }

        private void AgvStockInOutEtForm_Closing(object sender, System.ComponentModel.CancelEventArgs e)
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

        private void btnSubmit_Click(object sender, EventArgs e)
        {
            try {
                disposeBarcodeScanner();

                msgHelper.clear();

                string coatingMachineCode = txtCoatingMachineCode.Text.Trim();
                if (string.IsNullOrEmpty(coatingMachineCode)) {
                    msgHelper.showWarning("please input machine code");
                    txtCoatingMachineCode.SelectAll();
                    txtCoatingMachineCode.Focus();
                    return;
                }

                if (rdoSupply.Checked) {

                    if (string.IsNullOrEmpty(txtSupplySettingQty.Text)) {
                        msgHelper.showWarning("please input qty");
                        txtSupplySettingQty.SelectAll();
                        txtSupplySettingQty.Focus();
                        return;
                    }

                    int settingQty;
                    try {
                        settingQty = Int32.Parse(txtSupplySettingQty.Text.Trim());
                    } catch (Exception) {
                        msgHelper.showWarning("invalid qty");
                        txtSupplySettingQty.SelectAll();
                        txtSupplySettingQty.Focus();
                        return;
                    }
                    ServiceFactoryEt.getCurrentService().agvRetrievalSetting(coatingMachineCode, rdoCoatingColor.Checked ? 1 : 2, settingQty);
                } else {
                    ServiceFactoryEt.getCurrentService().agvCollectSetting(coatingMachineCode, rdoCollect1.Checked ? 1 : 2);
                }

                clearAll();
                msgHelper.showInfo("submit ok");

                txtCoatingMachineCode.SelectAll();
                txtCoatingMachineCode.Focus();
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            } finally {
                initializeBarcodeScanner();
            }
        }

        private void txtCoatingMachineCode_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter)) {
                try {
                    msgHelper.clear();

                    string coatingMachineCode = txtCoatingMachineCode.Text.Trim();
                    if (!string.IsNullOrEmpty(coatingMachineCode)) {
                        agvInfoRFT agvInfo = ServiceFactoryEt.getCurrentService().getAgvInfoByCoatingMachineCode(coatingMachineCode);
                        lblCoatingMachineShortName.Text = agvInfo.coatingMachineName;
                        lblRetrievalAvailableQty.Text = agvInfo.retrievalAvailableQty.ToString("0");
                        lblRetrievingBucketCount.Text = agvInfo.retrievingBucketCount.ToString("0");
                        lblCollectingBucketCount.Text = agvInfo.collectingBucketCount.ToString("0");
                    }
                } catch (Exception ex) {
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void AgvStockInOutEtForm_KeyDown(object sender, KeyEventArgs e)
        {
            try {
                if (e.KeyCode == Keys.Down) {
                    if (txtCoatingMachineCode.Focused) {
                        rdoSupply.Focus();
                    } else if (rdoSupply.Focused) {
                        rdoCollect1.Focus();
                    } else if (rdoCollect1.Focused) {
                        rdoCollect2.Focus();
                    } else if (rdoCollect2.Focused) {
                        rdoCoatingColor.Focus();
                    } else if (rdoCoatingColor.Focused) {
                        rdoRetrievalDate.Focus();
                    } else if (rdoRetrievalDate.Focused) {
                        txtSupplySettingQty.SelectAll();
                        txtSupplySettingQty.Focus();
                    } else if (txtSupplySettingQty.Focused) {
                        btnSubmit.Focus();
                    } else if (btnSubmit.Focused) {
                        btnMenu.Focus();
                    } else if (btnMenu.Focused) {
                        txtCoatingMachineCode.SelectAll();
                        txtCoatingMachineCode.Focus();
                    }
                } else if (e.KeyCode == Keys.Up) {
                    if (txtCoatingMachineCode.Focused) {
                        btnMenu.Focus();
                    } else if (btnMenu.Focused) {
                        btnSubmit.Focus();
                    } else if (btnSubmit.Focused) {
                        txtSupplySettingQty.SelectAll();
                        txtSupplySettingQty.Focus();
                    } else if (txtSupplySettingQty.Focused) {
                        rdoRetrievalDate.Focus();
                    } else if (rdoRetrievalDate.Focused) {
                        rdoCoatingColor.Focus();
                    } else if (rdoCoatingColor.Focused) {
                        rdoCollect2.Focus();
                    } else if (rdoCollect2.Focused) {
                        rdoCollect1.Focus();
                    } else if (rdoCollect1.Focused) {
                        rdoSupply.Focus();
                    } else if (rdoSupply.Focused) {
                        txtCoatingMachineCode.SelectAll();
                        txtCoatingMachineCode.Focus();
                    }
                } else if (e.KeyValue == 64)//L Button
                {
                    btnMenu_Click(btnMenu, null);
                } else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (txtCoatingMachineCode.Focused) {
                        txtCoatingMachineCode_KeyPress(txtCoatingMachineCode, eventArgs);
                    } else if (btnSubmit.Focused) {
                        btnSubmit_Click(btnSubmit, eventArgs);
                    } else if (btnMenu.Focused) {
                        btnMenu_Click(btnMenu, eventArgs);
                    }
                }
            } catch (Exception ex) {
                msgHelper.showError(ex.Message);
            }
        }
    }
}