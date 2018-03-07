using System;
using System.Windows.Forms;
using wms_rft.StockRegist;

namespace wms_rft.Menu
{
    public partial class StockRegistMenuForm : Form
    {
        public StockRegistMenuForm()
        {
            InitializeComponent();
        }

        private void btnReturn_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void btnTicketBucketBinding_Click(object sender, EventArgs e)
        {
//            Hide();
            try
            {
                Form form = new TicketBucketBindingForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
//                Show();
            }
        }

        private void btnM2Regist_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new PreM2Form();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnPalletBucketBinding_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new BucketPalletBindingSmartForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void StockRegistMenuForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (btnTicketBucketBinding.Focused)
                    {
                        btnM2Regist.Focus();
                    }
                    else if (btnM2Regist.Focused)
                    {
                        btnPalletBucketBinding.Focus();
                    }            
                    else if (btnPalletBucketBinding.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnTicketBucketBinding.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (btnTicketBucketBinding.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnPalletBucketBinding.Focus();
                    }
                    else if (btnPalletBucketBinding.Focused)
                    {
                        btnM2Regist.Focus();
                    }
                    else if (btnM2Regist.Focused)
                    {
                        btnTicketBucketBinding.Focus();
                    }                 
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnReturn_Click(null, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnTicketBucketBinding.Focused)
                    {
                        btnTicketBucketBinding_Click(btnTicketBucketBinding, eventArgs);
                    }
                    else if (btnM2Regist.Focused)
                    {
                        btnM2Regist_Click(btnM2Regist, eventArgs);
                    }
                    else if (btnPalletBucketBinding.Focused)
                    {
                        btnPalletBucketBinding_Click(btnPalletBucketBinding, eventArgs);
                    }                
                    else if (btnReturn.Focused)
                    {
                        btnReturn_Click(btnReturn, eventArgs);
                    }
                }
                else if (e.KeyCode == Keys.D1)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnTicketBucketBinding_Click(btnTicketBucketBinding, eventArgs);
                }
                else if (e.KeyCode == Keys.D2)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnM2Regist_Click(btnM2Regist, eventArgs);
                }
                else if (e.KeyCode == Keys.D3)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnPalletBucketBinding_Click(btnPalletBucketBinding, eventArgs);
                }            
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
    }
}