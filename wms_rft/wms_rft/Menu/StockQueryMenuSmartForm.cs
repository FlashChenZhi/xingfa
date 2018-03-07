using System;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.StockIn;
using wms_rft.StockInquiry;

namespace wms_rft.Menu
{
    public partial class StockQueryMenuSmartForm : Form
    {
        public StockQueryMenuSmartForm()
        {
            InitializeComponent();
        }

        private void btnReturn_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void btnPalletInquiry_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new PalletInquiryForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnBucketInquiry_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new BucketInquirySmartForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnBagInquiry_Click(object sender, EventArgs e)
        {
            try
            {
                Form form = new BagInquiryForm();
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
                Form form = new BucketNoReadSmartForm();
                form.ShowDialog();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnPreM2Inquiry_Click(object sender, EventArgs e)
        {
            try {
                Form form = new PreM2InquiryForm();
                form.ShowDialog();
            } catch (Exception ex) {
                MessageBox.Show(ex.Message);
            }
        }

        private void StockQueryMenuForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (btnPalletInquiry.Focused)
                    {
                        btnBucketInquiry.Focus();
                    }
                    else if (btnBucketInquiry.Focused)
                    {
                        btnBagInquiry.Focus();
                    }
                    else if (btnBagInquiry.Focused)
                    {
                        btnBcrStatusQuery.Focus();
                    }
                    else if (btnBcrStatusQuery.Focused){
                        btnPreM2Inquiry.Focus();
                    } else if (btnPreM2Inquiry.Focused) {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused)
                    {
                        btnPalletInquiry.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (btnPalletInquiry.Focused)
                    {
                        btnReturn.Focus();
                    }
                    else if (btnReturn.Focused){
                        btnPreM2Inquiry.Focus();
                    }
                    else if (btnPreM2Inquiry.Focused){
                        btnBcrStatusQuery.Focus();
                    }
                    else if (btnBcrStatusQuery.Focused)
                    {
                        btnBagInquiry.Focus();
                    }
                    else if (btnBagInquiry.Focused)
                    {
                        btnBucketInquiry.Focus();
                    }
                    else if (btnBucketInquiry.Focused)
                    {
                        btnPalletInquiry.Focus();
                    }
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnReturn_Click(null, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (btnPalletInquiry.Focused)
                    {
                        btnPalletInquiry_Click(btnPalletInquiry, eventArgs);
                    }
                    else if (btnBucketInquiry.Focused)
                    {
                        btnBucketInquiry_Click(btnBucketInquiry, eventArgs);
                    }
                    else if (btnBagInquiry.Focused)
                    {
                        btnBagInquiry_Click(btnBagInquiry, eventArgs);
                    }
                    else if (btnBcrStatusQuery.Focused)
                    {
                        btnBcrStatusQuery_Click(btnBcrStatusQuery, eventArgs);
                    } else if (btnPreM2Inquiry.Focused) {
                        btnPreM2Inquiry_Click(btnBcrStatusQuery, eventArgs);
                    }
                    else if (btnReturn.Focused)
                    {
                        btnReturn_Click(btnReturn, eventArgs);
                    }
                }
                else if (e.KeyCode == Keys.D1)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnPalletInquiry_Click(btnPalletInquiry, eventArgs);
                }
                else if (e.KeyCode == Keys.D2)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBucketInquiry_Click(btnBucketInquiry, eventArgs);
                }
                else if (e.KeyCode == Keys.D3)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBagInquiry_Click(btnBagInquiry, eventArgs);
                }
                else if (e.KeyCode == Keys.D4)
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnBcrStatusQuery_Click(btnBcrStatusQuery, eventArgs);
                } else if (e.KeyCode == Keys.D5) {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));
                    btnPreM2Inquiry_Click(btnPreM2Inquiry, eventArgs);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void StockQueryMenuSmartForm_Load(object sender, EventArgs e)
        {
            Text = CommonHelper.formatTitle(Text, Const.SystemCode.SMART);
        }

        
       

    }
}