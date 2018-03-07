using System;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.Shipping;
using wms_rft.StockInquiry;

namespace wms_rft.Menu
{
    public partial class MainMenuLogisticForm : Form
    {
        public MainMenuLogisticForm()
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
                Form form = new BucketToBagSettingForm();
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
                Form form = new BagToBucketBindingForm();
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
                    if (btnBucketToBagSetting.Focused)
                    {
                        btnBagToBucketBinding.Focus();
                    }
                    else if (btnBagToBucketBinding.Focused)
                    {                        
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnOff.Focus();
                    }
                    else if (btnOff.Focused)
                    {
                        btnBucketToBagSetting.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (btnBucketToBagSetting.Focused)
                    {
                        btnOff.Focus();
                    }
                    else if (btnOff.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {                        
                        btnBagToBucketBinding.Focus();
                    }
                    else if (btnBagToBucketBinding.Focused)
                    {
                        btnBucketToBagSetting.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnReturn_Click(null, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnBucketToBagSetting.Focused)
                    {
                        btnStockRegistMenu_Click(btnBucketToBagSetting, eventArgs);
                    }
                    else if (btnBagToBucketBinding.Focused)
                    {
                        btnStockInMenu_Click(btnBagToBucketBinding, eventArgs);
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
                    btnStockRegistMenu_Click(btnBucketToBagSetting, eventArgs);                    
                }
                else if (e.KeyCode == Keys.D2)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnStockInMenu_Click(btnBagToBucketBinding, eventArgs);
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