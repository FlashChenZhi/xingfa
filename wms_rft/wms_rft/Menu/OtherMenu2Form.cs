using System;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.Other;
using wms_rft.StockRegist;

namespace wms_rft.Menu
{
    public partial class OtherMenu2Form : Form
    {
        public OtherMenu2Form()
        {
            InitializeComponent();
        }

        private void btnReturn_Click(object sender, EventArgs e)
        {
            Close();
        }    

        private void btnBagStock_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new BagStockForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnUnbagStock_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new UnbagStockForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnPreM2_Click(object sender, EventArgs e)
        {
            try {
                Form form = new M2RegistForm();
                form.ShowDialog();
            } catch (Exception ex) {
                MessageBox.Show(ex.Message);
            }
        }


        private void OtherMenu2Form_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (btnBagStock.Focused)
                    {
                        btnUnbagStock.Focus();
                    }
                    else if (btnUnbagStock.Focused)
                    {
                        btnPreM2.Focus();
                    } else if (btnPreM2.Focused) {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnBagStock.Focus();
                    }                    
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (btnBagStock.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnPreM2.Focus();
                    } else if (btnPreM2.Focused) {
                        btnUnbagStock.Focus();
                    }
                    else if (btnUnbagStock.Focused)
                    {
                        btnBagStock.Focus();
                    }                    
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnReturn_Click(null, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnBagStock.Focused)
                    {
                        btnBagStock_Click(btnBagStock, eventArgs);
                    }
                    else if (btnUnbagStock.Focused)
                    {
                        btnUnbagStock_Click(btnUnbagStock, eventArgs);
                    } else if (btnPreM2.Focused) {
                        btnPreM2_Click(btnPreM2, eventArgs);
                    }  
                    else if (btnReturn.Focused)
                    {
                        btnReturn_Click(btnReturn, eventArgs);
                    }                    
                }
                else if (e.KeyCode == Keys.D1)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBagStock_Click(btnBagStock, eventArgs);
                }
                else if (e.KeyCode == Keys.D2)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnUnbagStock_Click(btnUnbagStock, eventArgs);
                } else if (e.KeyCode == Keys.D3) {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnPreM2_Click(btnPreM2, eventArgs);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void OtherMenu2Form_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);
        }

       
       
    }
}