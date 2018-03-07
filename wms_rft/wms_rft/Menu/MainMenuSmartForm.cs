using System;
using System.Windows.Forms;
using wms_rft.Helper;

namespace wms_rft.Menu
{
    public partial class MainMenuSmartForm : Form
    {
        public MainMenuSmartForm()
        {
            InitializeComponent();
        }

        private void btnReturn_Click(object sender, EventArgs e)
        {
            Close();
        }
       
        private void btnStockRegistMenu_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new StockRegistMenuForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnStockInMenu_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new StockInMenuSmartForm();
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
                Form form = new StockOutMenuSmartForm();
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
                Form form = new OtherMenuSmartForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnOtherMenu2_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new OtherMenu2Form();
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
                Form form = new StockQueryMenuSmartForm();
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
                    if (btnStockRegistMenu.Focused)
                    {
                        btnStockInMenu.Focus();
                    }
                    else if (btnStockInMenu.Focused)
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
                        btnOtherMenu2.Focus();
                    }
                    else if (btnOtherMenu2.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnOff.Focus();
                    }
                    else if (btnOff.Focused)
                    {
                        btnStockRegistMenu.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (btnStockRegistMenu.Focused)
                    {
                        btnOff.Focus();
                    }
                    else if (btnOff.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnOtherMenu2.Focus();
                    }
                    else if (btnOtherMenu2.Focused)
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
                    else if (btnStockInMenu.Focused)
                    {
                        btnStockRegistMenu.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnReturn_Click(null, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnStockRegistMenu.Focused)
                    {
                        btnStockRegistMenu_Click(btnStockRegistMenu, eventArgs);
                    }
                    else if (btnStockInMenu.Focused)
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
                    else if (btnOtherMenu2.Focused)
                    {
                        btnOtherMenu2_Click(btnOtherMenu2, eventArgs);
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
                    btnStockRegistMenu_Click(btnStockRegistMenu, eventArgs);                    
                }
                else if (e.KeyCode == Keys.D2)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnStockInMenu_Click(btnStockInMenu, eventArgs);
                }
                else if (e.KeyCode == Keys.D3)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnStockOutMenu_Click(btnStockOutMenu, eventArgs);
                }
                else if (e.KeyCode == Keys.D4)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnStockQueryMenu_Click(btnStockQueryMenu, eventArgs);
                }
                else if (e.KeyCode == Keys.D5)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnOtherMenu_Click(btnOtherMenu, eventArgs);
                }
                else if (e.KeyCode == Keys.D6)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnOtherMenu_Click(btnOtherMenu2, eventArgs);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void MainMenuSmartForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);
        }
    }
}