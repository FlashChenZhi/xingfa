namespace wms_rft.Menu
{
    partial class OtherMenu2Form
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.btnBagStock = new System.Windows.Forms.Button();
            this.btnReturn = new System.Windows.Forms.Button();
            this.btnUnbagStock = new System.Windows.Forms.Button();
            this.btnPreM2 = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // btnBagStock
            // 
            this.btnBagStock.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnBagStock.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnBagStock.ForeColor = System.Drawing.Color.Black;
            this.btnBagStock.Location = new System.Drawing.Point(3, 3);
            this.btnBagStock.Name = "btnBagStock";
            this.btnBagStock.Size = new System.Drawing.Size(236, 32);
            this.btnBagStock.TabIndex = 0;
            this.btnBagStock.Text = "1.装袋作业";
            this.btnBagStock.Click += new System.EventHandler(this.btnBagStock_Click);
            // 
            // btnReturn
            // 
            this.btnReturn.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnReturn.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnReturn.ForeColor = System.Drawing.Color.Black;
            this.btnReturn.Location = new System.Drawing.Point(85, 246);
            this.btnReturn.Name = "btnReturn";
            this.btnReturn.Size = new System.Drawing.Size(72, 22);
            this.btnReturn.TabIndex = 2;
            this.btnReturn.Text = "返回";
            this.btnReturn.Click += new System.EventHandler(this.btnReturn_Click);
            // 
            // btnUnbagStock
            // 
            this.btnUnbagStock.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnUnbagStock.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnUnbagStock.ForeColor = System.Drawing.Color.Black;
            this.btnUnbagStock.Location = new System.Drawing.Point(3, 41);
            this.btnUnbagStock.Name = "btnUnbagStock";
            this.btnUnbagStock.Size = new System.Drawing.Size(236, 32);
            this.btnUnbagStock.TabIndex = 1;
            this.btnUnbagStock.Text = "2.拆袋作业";
            this.btnUnbagStock.Click += new System.EventHandler(this.btnUnbagStock_Click);
            // 
            // btnPreM2
            // 
            this.btnPreM2.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnPreM2.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnPreM2.ForeColor = System.Drawing.Color.Black;
            this.btnPreM2.Location = new System.Drawing.Point(3, 79);
            this.btnPreM2.Name = "btnPreM2";
            this.btnPreM2.Size = new System.Drawing.Size(236, 32);
            this.btnPreM2.TabIndex = 3;
            this.btnPreM2.Text = "3.M2完了登陆作业";
            this.btnPreM2.Click += new System.EventHandler(this.btnPreM2_Click);
            // 
            // OtherMenu2Form
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.btnPreM2);
            this.Controls.Add(this.btnUnbagStock);
            this.Controls.Add(this.btnReturn);
            this.Controls.Add(this.btnBagStock);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "OtherMenu2Form";
            this.Text = "其他业务 Menu";
            this.Load += new System.EventHandler(this.OtherMenu2Form_Load);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.OtherMenu2Form_KeyDown);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnBagStock;
        private System.Windows.Forms.Button btnReturn;
        private System.Windows.Forms.Button btnUnbagStock;
        private System.Windows.Forms.Button btnPreM2;
    }
}

