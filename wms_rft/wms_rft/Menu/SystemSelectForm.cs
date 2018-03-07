using System;
using System.Windows.Forms;
using wms_rft.Helper;

namespace wms_rft.Menu
{
    public partial class SystemSelectForm : Form
    {
        public SystemSelectForm()
        {
            InitializeComponent();
        }

        private void btnReturn_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void btnSelectSmart_Click(object sender, EventArgs e)
        {
            try
            {
//                RegistryHelper.registWebserviceServer(Const.SystemCode.SMART);
                Form form = new MainMenuSmartForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnSelectEtPicking_Click(object sender, EventArgs e)
        {
            try
            {
//                RegistryHelper.registWebserviceServer(Const.SystemCode.ET);
                Form form = new MainMenuEtForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnSelectLogistic_Click(object sender, EventArgs e)
        {
            try
            {
                //                RegistryHelper.registWebserviceServer(Const.SystemCode.ET);
                Form form = new MainMenuLogisticForm();
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
                    if (btnSelectSmart.Focused)
                    {
                        btnSelectEtPicking.Focus();
                    }
                    else if (btnSelectEtPicking.Focused)
                    {
                        btnReturn.Focus();
                    }                    
                    else if (btnReturn.Focused)
                    {
                        btnOff.Focus();
                    }
                    else if (btnOff.Focused)
                    {
                        btnSelectSmart.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (btnSelectSmart.Focused)
                    {
                        btnOff.Focus();
                    }
                    else if (btnOff.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {                        
                        btnSelectEtPicking.Focus();
                    }
                    else if (btnSelectEtPicking.Focused)
                    {
                        btnSelectSmart.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnReturn_Click(null, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnSelectSmart.Focused)
                    {
                        btnSelectSmart_Click(btnSelectSmart, eventArgs);
                    }
                    else if (btnSelectEtPicking.Focused)
                    {
                        btnSelectEtPicking_Click(btnSelectEtPicking, eventArgs);
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
                    btnSelectSmart_Click(btnSelectSmart, eventArgs);                    
                }
                else if (e.KeyCode == Keys.D2)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnSelectEtPicking_Click(btnSelectEtPicking, eventArgs);
                }                
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        

        
    }
}