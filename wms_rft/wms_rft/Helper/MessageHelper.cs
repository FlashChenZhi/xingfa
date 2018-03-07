using System.Drawing;
using System.Windows.Forms;

namespace wms_rft.Helper
{
    public class MessageHelper
    {
        private Label lblMessage;

        public MessageHelper(Label lblMessage)
        {
            this.lblMessage = lblMessage;
        }

        public void showError(string msg)
        {
            lblMessage.ForeColor = Color.Red;
            lblMessage.Text = msg;

            Viberator.Viberator.viberate();
        }

        public void showWarning(string msg)
        {
            lblMessage.ForeColor = Color.Red;
            lblMessage.Text = msg;

            Viberator.Viberator.viberate();
        }

        public void showInfo(string msg)
        {
            lblMessage.ForeColor = Color.Blue;
            lblMessage.Text = msg;
        }

        public void clear()
        {
            lblMessage.ForeColor = Color.Black;
            lblMessage.Text = string.Empty;
        }
    }
}
