using System;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.StockIn;

namespace wms_rft.Menu
{
    public partial class StockInMenuEtForm : Form
    {
        public StockInMenuEtForm()
        {
            InitializeComponent();
        }

        private void btnReturn_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void btnPalletStockIn1F_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new PalletStockIn1FEtForm();
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
                    if (btnPalletStockIn1F.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnPalletStockIn1F.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (btnPalletStockIn1F.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnPalletStockIn1F.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnReturn_Click(null, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnPalletStockIn1F.Focused)
                    {
                        btnPalletStockIn1F_Click(btnPalletStockIn1F, eventArgs);
                    }
                    else if (btnReturn.Focused)
                    {
                        btnReturn_Click(btnReturn, eventArgs);
                    }
                }
                else if (e.KeyCode == Keys.D1)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnPalletStockIn1F_Click(btnPalletStockIn1F, eventArgs);
                }                
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void StockInMenuEtForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.ET);
        }
    }
}