using System;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.StockIn;

namespace wms_rft.Menu
{
    public partial class StockInMenuSmartForm : Form
    {
        public StockInMenuSmartForm()
        {
            InitializeComponent();
        }

        private void btnReturn_Click(object sender, EventArgs e)
        {
            Close();
        }             

        private void btnPalletStockInBZ_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new PalletStockInBZForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnPalletStockIn1F_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new PalletStockIn1FSmartForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnBucketStockInBZ_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new BucketStockInBZForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnBagStockInBZ_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new BagStockInBZForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void StockInMenuForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (btnPalletStockInBZ.Focused)
                    {
                        btnPalletStockIn1F.Focus();
                    }
                    else if (btnPalletStockIn1F.Focused)
                    {
                        btnBucketStockInBZ.Focus();
                    }
                    else if (btnBucketStockInBZ.Focused)
                    {
                        btnBagStockInBZ.Focus();
                    }
                    else if (btnBagStockInBZ.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnPalletStockInBZ.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (btnPalletStockInBZ.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnBagStockInBZ.Focus();
                    }
                    else if (btnBagStockInBZ.Focused)
                    {
                        btnBucketStockInBZ.Focus();
                    }
                    else if (btnBucketStockInBZ.Focused)
                    {
                        btnPalletStockIn1F.Focus();
                    }
                    else if (btnPalletStockIn1F.Focused)
                    {
                        btnPalletStockInBZ.Focus();
                    }                  
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnReturn_Click(null, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnPalletStockInBZ.Focused)
                    {
                        btnPalletStockInBZ_Click(btnPalletStockInBZ, eventArgs);
                    }
                    else if (btnPalletStockIn1F.Focused)
                    {
                        btnPalletStockIn1F_Click(btnPalletStockIn1F, eventArgs);
                    }
                    else if (btnBucketStockInBZ.Focused)
                    {
                        btnBucketStockInBZ_Click(btnBucketStockInBZ, eventArgs);
                    }
                    else if (btnBagStockInBZ.Focused)
                    {
                        btnBagStockInBZ_Click(btnBagStockInBZ, eventArgs);
                    }                    
                    else if (btnReturn.Focused)
                    {
                        btnReturn_Click(btnReturn, eventArgs);
                    }
                }
                else if (e.KeyCode == Keys.D1)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnPalletStockInBZ_Click(btnPalletStockInBZ, eventArgs);
                }
                else if (e.KeyCode == Keys.D2)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnPalletStockIn1F_Click(btnPalletStockIn1F, eventArgs);
                }
                else if (e.KeyCode == Keys.D3)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBucketStockInBZ_Click(btnBucketStockInBZ, eventArgs);
                }
                else if (e.KeyCode == Keys.D4)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBagStockInBZ_Click(btnBagStockInBZ, eventArgs);
                }                
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void StockInMenuSmartForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);
        }
    }
}