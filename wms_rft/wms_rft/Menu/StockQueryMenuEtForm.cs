using System;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.StockIn;
using wms_rft.StockInquiry;


namespace wms_rft.Menu
{
    public partial class StockQueryMenuEtForm : Form
    {
        public StockQueryMenuEtForm()
        {
            InitializeComponent();
        }

        private void btnReturn_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void btnBucketInquiry_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new BucketInquiryEtForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnBcrStatusQuery_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new BucketNoReadEtForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void StockQueryMenuForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (btnBucketInquiry.Focused)
                    {                        
                        btnBcrStatusQuery.Focus();
                    }
                    else if (btnBcrStatusQuery.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnBucketInquiry.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (btnBucketInquiry.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnBcrStatusQuery.Focus();
                    }
                    else if (btnBcrStatusQuery.Focused)
                    {                        
                        btnBucketInquiry.Focus();
                    }                   
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnReturn_Click(null, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnBucketInquiry.Focused)
                    {
                        btnBucketInquiry_Click(btnBucketInquiry, eventArgs);
                    }                    
                    else if (btnBcrStatusQuery.Focused)
                    {
                        btnBcrStatusQuery_Click(btnBcrStatusQuery, eventArgs);
                    }
                    else if (btnReturn.Focused)
                    {
                        btnReturn_Click(btnReturn, eventArgs);
                    }
                }
                else if (e.KeyCode == Keys.D1)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBucketInquiry_Click(btnBucketInquiry, eventArgs);
                }               
                else if (e.KeyCode == Keys.D2)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBcrStatusQuery_Click(btnBcrStatusQuery, eventArgs);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void StockQueryMenuEtForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.ET);
        }
       

    }
}