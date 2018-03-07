using System;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.Helper.Smart;

namespace wms_rft.Menu
{
    public partial class LoginForm : Form
    {
        private MessageHelper msgHelper;
        public int result = 9;
        public string userId;

        public LoginForm()
        {
            InitializeComponent();

            Viberator.Viberator.testFn();


        }

        private void BagDeleteForm_Load(object sender, EventArgs e)
        {
            clearAll();
            msgHelper = new MessageHelper(lblMessage);
        }

        private void clearAll()
        {
            txtUserId.Text = string.Empty;
            txtPassword.Text = string.Empty;
            lblMessage.Text = string.Empty;
        }

        private void btnSubmit_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();


                if (string.IsNullOrEmpty(txtUserId.Text))
                {
                    msgHelper.showInfo("Please input user code");
                    return;
                }


                if (string.IsNullOrEmpty(txtPassword.Text))
                {
                    msgHelper.showInfo("Please input password");
                    return;
                }

                result = ServiceFactorySmart.getCurrentService().isAdminUser(txtUserId.Text.Trim(), txtPassword.Text.Trim());

                if (result != 0)
                {
                    msgHelper.showWarning("invalid user");
                    return;
                }

                userId = txtUserId.Text.Trim();

                Close();
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void btnExit_Click(object sender, EventArgs e)
        {
            Close();
        }


        private void txtUserId_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();

                    if (string.IsNullOrEmpty(txtUserId.Text))
                    {
                        msgHelper.showInfo("Please input user code");
                        return;
                    }

                    txtPassword.SelectAll();
                    txtPassword.Focus();
                }
                catch (Exception ex)
                {
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void txtPassword_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                try
                {
                    msgHelper.clear();

                    if (string.IsNullOrEmpty(txtPassword.Text))
                    {
                        msgHelper.showInfo("Please input password");
                        return;
                    }

                    btnSubmit_Click(null, null);
                }
                catch (Exception ex)
                {
                    msgHelper.showError(ex.Message);
                }
            }
        }

        private void LoginForm_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.F1))
            {
//                int i = 0;
            }
        }

        protected override void OnKeyDown(KeyEventArgs keyg)
        {
            switch (keyg.KeyData)
            {
                case Keys.Left:
                    label1.Text = "Left";
                    break;
                case Keys.Right:
                    label1.Text = "Right";
                    break;
                case Keys.Down:
                    label1.Text = "Down";
                    break;
                case Keys.Up:
                    label1.Text = "Up";
                    break;
                case Keys.Return:
                    label1.Text = "Return";
                    break;
                default:
                    break;
            }
        }

        private void LoginForm_KeyDown(object sender, KeyEventArgs e)
        {
            try
            {
                if (e.KeyCode == Keys.Down)
                {
                    if (txtUserId.Focused)
                    {
                        txtPassword.SelectAll();
                        txtPassword.Focus();
                    }
                    else if (txtPassword.Focused)
                    {
                        btnSubmit.Focus();
                    }                    
                    else if (btnSubmit.Focused)
                    {
                        btnExit.Focus();
                    }
                    else if (btnExit.Focused)
                    {
                        txtUserId.SelectAll();
                        txtUserId.Focus();
                    }
                }
                else if (e.KeyCode == Keys.Up)
                {
                    if (txtUserId.Focused)
                    {
                        btnExit.Focus();
                    }
                    else if (btnExit.Focused)
                    {
                        btnSubmit.Focus();
                    }
                    else if (btnSubmit.Focused)
                    {
                        txtPassword.SelectAll();
                        txtPassword.Focus();
                    }
                    else if (txtPassword.Focused)
                    {
                        txtUserId.SelectAll();
                        txtUserId.Focus();
                    }                    
                }
                else if (e.KeyValue == 64)//L Button
                {
                    btnExit_Click(null, null);
                }
                else if (e.KeyValue == 94)//R Button
                {
                    KeyPressEventArgs eventArgs = new KeyPressEventArgs(Convert.ToChar(Keys.Enter));

                    if (txtUserId.Focused)
                    {
                        txtUserId_KeyPress(txtUserId, eventArgs);
                    }
                    else if (txtPassword.Focused)
                    {
                        txtPassword_KeyPress(txtPassword, eventArgs);
                    }                   
                    else if (btnSubmit.Focused)
                    {
                        btnSubmit_Click(btnSubmit, eventArgs);
                    }
                    else if (btnExit.Focused)
                    {
                        btnExit_Click(btnSubmit, eventArgs);
                    }                
                }
            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }
    }
}