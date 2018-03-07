using System;
using System.Windows.Forms;
using wms_rft.Helper;

namespace wms_rft.Menu
{
    public partial class MainMenuEtForm : Form
    {
        public MainMenuEtForm()
        {
            InitializeComponent();
        }

        private void btnReturn_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void btnStockInMenu_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new StockInMenuEtForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnStockOutMenu_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new StockOutMenuEtForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnOtherMenu_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new OtherMenuEtForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnEtMenu_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new EtMenuForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnStockQueryMenu_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new StockQueryMenuEtForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnOff_Click(object sender, EventArgs e)
        {
            try
            {
                Viberator.Viberator.stop();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void MainMenuForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (btnStockInMenu.Focused)
                    {
                        btnStockOutMenu.Focus();
                    }
                    else if (btnStockOutMenu.Focused)
                    {
                        btnStockQueryMenu.Focus();
                    }
                    else if (btnStockQueryMenu.Focused)
                    {
                        btnOtherMenu.Focus();
                    }
                    else if (btnOtherMenu.Focused)
                    {
                        btnEtMenu.Focus();
                    }
                    else if (btnEtMenu.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnOff.Focus();
                    }
                    else if (btnOff.Focused)
                    {
                        btnStockInMenu.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (btnStockInMenu.Focused)
                    {
                        btnOff.Focus();
                    }
                    else if (btnOff.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnEtMenu.Focus();
                    }
                    else if (btnEtMenu.Focused)
                    {
                        btnOtherMenu.Focus();
                    }
                    else if (btnOtherMenu.Focused)
                    {
                        btnStockQueryMenu.Focus();
                    }
                    else if (btnStockQueryMenu.Focused)
                    {
                        btnStockOutMenu.Focus();
                    }
                    else if (btnStockOutMenu.Focused)
                    {
                        btnStockInMenu.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnReturn_Click(null, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnStockInMenu.Focused)
                    {
                        btnStockInMenu_Click(btnStockInMenu, eventArgs);
                    }
                    else if (btnStockOutMenu.Focused)
                    {
                        btnStockOutMenu_Click(btnStockOutMenu, eventArgs);
                    }
                    else if (btnStockQueryMenu.Focused)
                    {
                        btnStockQueryMenu_Click(btnStockQueryMenu, eventArgs);
                    }
                    else if (btnOtherMenu.Focused)
                    {
                        btnOtherMenu_Click(btnOtherMenu, eventArgs);
                    }
                    else if (btnEtMenu.Focused)
                    {
                        btnEtMenu_Click(btnOtherMenu, eventArgs);
                    }
                    else if (btnReturn.Focused)
                    {
                        btnReturn_Click(btnReturn, eventArgs);
                    }
                    else if (btnOff.Focused)
                    {
                        btnOff_Click(btnOff, eventArgs);
                    }
                }                
                else if (e.KeyCode == Keys.D1)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnStockInMenu_Click(btnStockInMenu, eventArgs);
                }
                else if (e.KeyCode == Keys.D2)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnStockOutMenu_Click(btnStockOutMenu, eventArgs);
                }
                else if (e.KeyCode == Keys.D3)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnStockQueryMenu_Click(btnStockQueryMenu, eventArgs);
                }
                else if (e.KeyCode == Keys.D4)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnOtherMenu_Click(btnOtherMenu, eventArgs);
                }
                else if (e.KeyCode == Keys.D5)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnEtMenu_Click(btnOtherMenu, eventArgs);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void MainMenuEtForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.ET);
        }
    }
}