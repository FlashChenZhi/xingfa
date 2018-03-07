using System;
using System.Windows.Forms;
using wms_rft.Et;
using wms_rft.Helper;

namespace wms_rft.Menu
{
    public partial class EtMenuForm : Form
    {
        public EtMenuForm()
        {
            InitializeComponent();
        }

        private void btnReturn_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void btnEtRecovery_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new EtRecoveryForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnEtBucketErrorMaintenence_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new EtBucketErrorMaintenenceForm();
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
                    if (btnEtRecovery.Focused)
                    {                        
                        btnEtBucketErrorMaintenence.Focus();
                    }
                    else if (btnEtBucketErrorMaintenence.Focused)                    
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnEtRecovery.Focus();
                    }                  
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (btnEtRecovery.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)                    
                    {                        
                        btnEtBucketErrorMaintenence.Focus();
                    }
                    else if (btnEtBucketErrorMaintenence.Focused)
                    {                        
                        btnEtRecovery.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnReturn_Click(null, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnEtRecovery.Focused)
                    {
                        btnEtRecovery_Click(btnEtRecovery, eventArgs);
                    }
                    else if (btnEtBucketErrorMaintenence.Focused)
                    {
                        btnEtBucketErrorMaintenence_Click(btnEtBucketErrorMaintenence, eventArgs);
                    }
                    else if (btnReturn.Focused)
                    {
                        btnReturn_Click(btnReturn, eventArgs);
                    }                   
                }
                else if (e.KeyCode == Keys.D1)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnEtRecovery_Click(btnEtRecovery, eventArgs);
                }
                else if (e.KeyCode == Keys.D2)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnEtBucketErrorMaintenence_Click(btnEtBucketErrorMaintenence, eventArgs);
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