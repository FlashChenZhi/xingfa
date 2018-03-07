using System;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.Other;

namespace wms_rft.Menu
{
    public partial class OtherMenuSmartForm : Form
    {
        public OtherMenuSmartForm()
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
                Form form = new PalletMoveSmartForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnBucketMove_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new BucketMoveForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnBagMove_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new BagMoveForm();
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
                Form form = new BucketDeleteSmartForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnBagDelete_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new BagDeleteForm();
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
                Form form = new BucketOrBagChangeSmartForm();
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
                        btnBucketMove.Focus();
                    }
                    else if (btnBucketMove.Focused)
                    {
                        btnBagMove.Focus();
                    }
                    else if (btnBagMove.Focused)
                    {
                        btnBucketDelete.Focus();
                    }
                    else if (btnBucketDelete.Focused)
                    {
                        btnBagDelete.Focus();
                    }
                    else if (btnBagDelete.Focused)
                    {
                        btnBucketOrBagChange.Focus();
                    }
                    else if (btnBucketOrBagChange.Focused)
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
                        btnBucketOrBagChange.Focus();
                    }
                    else if (btnBucketOrBagChange.Focused)
                    {
                        btnBagDelete.Focus();
                    }
                    else if (btnBagDelete.Focused)
                    {
                        btnBucketDelete.Focus();
                    }
                    else if (btnBucketDelete.Focused)
                    {
                        btnBagMove.Focus();
                    }
                    else if (btnBagMove.Focused)
                    {
                        btnBucketMove.Focus();
                    }
                    else if (btnBucketMove.Focused)
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
                    else if (btnBucketMove.Focused)
                    {
                        btnBucketMove_Click(btnBucketMove, eventArgs);
                    }
                    else if (btnBagMove.Focused)
                    {
                        btnBagMove_Click(btnBagMove, eventArgs);
                    }
                    else if (btnBucketDelete.Focused)
                    {
                        btnBucketDelete_Click(btnBucketDelete, eventArgs);
                    }
                    else if (btnBagDelete.Focused)
                    {
                        btnBagDelete_Click(btnBagDelete, eventArgs);
                    }
                    else if (btnBucketOrBagChange.Focused)
                    {
                        btnBucketOrBagChange_Click(btnBucketOrBagChange, eventArgs);
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
                    btnBucketMove_Click(btnBucketMove, eventArgs);
                }
                else if (e.KeyCode == Keys.D3)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBagMove_Click(btnBagMove, eventArgs);
                }
                else if (e.KeyCode == Keys.D4)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBucketDelete_Click(btnBucketDelete, eventArgs);
                }
                else if (e.KeyCode == Keys.D5)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBagDelete_Click(btnBagDelete, eventArgs);
                }
                else if (e.KeyCode == Keys.D6)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBucketOrBagChange_Click(btnBucketOrBagChange, eventArgs);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void OtherMenuSmartForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);
        }
    }
}