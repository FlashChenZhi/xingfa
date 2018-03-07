using System;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.Other;
using wms_rft.StockRegist;

namespace wms_rft.Menu
{
    public partial class OtherMenuEtForm : Form
    {
        public OtherMenuEtForm()
        {
            InitializeComponent();
        }

        private void btnReturn_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void btnPalletMove_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new PalletMoveEtForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnBucketDelete_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new BucketDeleteEtForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnBucketOrBagChange_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new BucketOrBagChangeEtForm();
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
                Form form = new BucketPalletBindingEtForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void OtherMenuForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (btnPalletMove.Focused)
                    {                        
                        btnBucketDelete.Focus();
                    }
                    else if (btnBucketDelete.Focused)
                    {                        
                        btnBucketOrBagChange.Focus();
                    }
                    else if (btnBucketOrBagChange.Focused)
                    {
                        btnPalletBucketBinding.Focus();
                    }
                    else if (btnPalletBucketBinding.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnPalletMove.Focus();
                    }                  
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (btnPalletMove.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnPalletBucketBinding.Focus();
                    }
                    else if (btnPalletBucketBinding.Focused)
                    {
                        btnBucketOrBagChange.Focus();
                    }
                    else if (btnBucketOrBagChange.Focused)
                    {                        
                        btnBucketDelete.Focus();
                    }
                    else if (btnBucketDelete.Focused)
                    {                        
                        btnPalletMove.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnReturn_Click(null, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnPalletMove.Focused)
                    {
                        btnPalletMove_Click(btnPalletMove, eventArgs);
                    }
                    else if (btnBucketDelete.Focused)
                    {
                        btnBucketDelete_Click(btnBucketDelete, eventArgs);
                    }
                    else if (btnBucketOrBagChange.Focused)
                    {
                        btnBucketOrBagChange_Click(btnBucketOrBagChange, eventArgs);
                    }
                    else if (btnPalletBucketBinding.Focused)
                    {
                        btnPalletBucketBinding_Click(btnBucketOrBagChange, eventArgs);
                    }
                    else if (btnReturn.Focused)
                    {
                        btnReturn_Click(btnReturn, eventArgs);
                    }                   
                }
                else if (e.KeyCode == Keys.D1)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnPalletMove_Click(btnPalletMove, eventArgs);
                }
                else if (e.KeyCode == Keys.D2)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBucketDelete_Click(btnBucketDelete, eventArgs);
                }
                else if (e.KeyCode == Keys.D3)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBucketOrBagChange_Click(btnBucketOrBagChange, eventArgs);
                }
                else if (e.KeyCode == Keys.D4)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnPalletBucketBinding_Click(btnBucketOrBagChange, eventArgs);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void OtherMenuEtForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.ET);
        }
    }
}