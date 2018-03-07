using System;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.StockOut;

namespace wms_rft.Menu
{
    public partial class StockOutMenuEtForm : Form
    {
        public StockOutMenuEtForm()
        {
            InitializeComponent();
        }

        private void btnReturn_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void btnJobInquiry_Click(object sender, EventArgs e)
        {
            try {
                Form form = new JobDataInquiryEtForm();
                form.ShowDialog();
            } catch (Exception ex) {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnAgvStockInOut_Click(object sender, EventArgs e)
        {
            try {
                Form form = new AgvStockInOutEtForm();
                form.ShowDialog();
            } catch (Exception ex) {
                MessageBox.Show(ex.Message);
            }
        }

        private void StockOutMenuForm_KeyDown(object sender, KeyEventArgs e)
        {
            try {
                if (e.KeyCode == Keys.Down) {
                    if (btnJobInquiry.Focused) {
                        btnAgvStockInOut.Focus();
                    } else if (btnAgvStockInOut.Focused) {
                        btnReturn.Focus();
                    } else if (btnReturn.Focused) {
                        btnJobInquiry.Focus();
                    }
                } else if (e.KeyCode == Keys.Up) {
                    if (btnJobInquiry.Focused) {
                        btnReturn.Focus();
                    } else if (btnReturn.Focused) {
                        btnAgvStockInOut.Focus();
                    } else if (btnAgvStockInOut.Focused) {
                        btnJobInquiry.Focus();
                    }
                } else if (e.KeyValue == 64)//L Button
                {
                    btnReturn_Click(null, null);
                } else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnJobInquiry.Focused) {
                        btnJobInquiry_Click(btnJobInquiry, eventArgs);
                    } else if (btnAgvStockInOut.Focused) {
                        btnAgvStockInOut_Click(btnReturn, eventArgs);
                    } else if (btnReturn.Focused) {
                        btnReturn_Click(btnReturn, eventArgs);
                    }
                } else if (e.KeyCode == Keys.D1) {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnJobInquiry_Click(btnJobInquiry, eventArgs);
                } else if (e.KeyCode == Keys.D2) {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnAgvStockInOut_Click(btnJobInquiry, eventArgs);
                }
            } catch (Exception ex) {
                MessageBox.Show(ex.Message);
            }
        }

        private void StockOutMenuEtForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.ET);
        }


    }
}