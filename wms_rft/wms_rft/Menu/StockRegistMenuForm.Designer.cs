namespace wms_rft.Menu
{
    partial class StockRegistMenuForm
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
            this.btnTicketBucketBinding = new System.Windows.Forms.Button();
            this.btnReturn = new System.Windows.Forms.Button();
            this.btnM2Regist = new System.Windows.Forms.Button();
            this.btnPalletBucketBinding = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // btnTicketBucketBinding
            // 
            this.btnTicketBucketBinding.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnTicketBucketBinding.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnTicketBucketBinding.ForeColor = System.Drawing.Color.Black;
            this.btnTicketBucketBinding.Location = new System.Drawing.Point(3, 3);
            this.btnTicketBucketBinding.Name = "btnTicketBucketBinding";
            this.btnTicketBucketBinding.Size = new System.Drawing.Size(236, 32);
            this.btnTicketBucketBinding.TabIndex = 0;
            this.btnTicketBucketBinding.Text = "1.生产传票-料箱关联作业";
            this.btnTicketBucketBinding.Click += new System.EventHandler(this.btnTicketBucketBinding_Click);
            // 
            // btnReturn
            // 
            this.btnReturn.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnReturn.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnReturn.ForeColor = System.Drawing.Color.Black;
            this.btnReturn.Location = new System.Drawing.Point(85, 246);
            this.btnReturn.Name = "btnReturn";
            this.btnReturn.Size = new System.Drawing.Size(72, 22);
            this.btnReturn.TabIndex = 3;
            this.btnReturn.Text = "返回";
            this.btnReturn.Click += new System.EventHandler(this.btnReturn_Click);
            // 
            // btnM2Regist
            // 
            this.btnM2Regist.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnM2Regist.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnM2Regist.ForeColor = System.Drawing.Color.Black;
            this.btnM2Regist.Location = new System.Drawing.Point(3, 41);
            this.btnM2Regist.Name = "btnM2Regist";
            this.btnM2Regist.Size = new System.Drawing.Size(236, 32);
            this.btnM2Regist.TabIndex = 1;
            this.btnM2Regist.Text = "2.M2关联设定";
            this.btnM2Regist.Click += new System.EventHandler(this.btnM2Regist_Click);
            // 
            // btnPalletBucketBinding
            // 
            this.btnPalletBucketBinding.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnPalletBucketBinding.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnPalletBucketBinding.ForeColor = System.Drawing.Color.Black;
            this.btnPalletBucketBinding.Location = new System.Drawing.Point(3, 79);
            this.btnPalletBucketBinding.Name = "btnPalletBucketBinding";
            this.btnPalletBucketBinding.Size = new System.Drawing.Size(236, 32);
            this.btnPalletBucketBinding.TabIndex = 2;
            this.btnPalletBucketBinding.Text = "3.料箱-托盘关联作业";
            this.btnPalletBucketBinding.Click += new System.EventHandler(this.btnPalletBucketBinding_Click);
            // 
            // StockRegistMenuForm
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.btnPalletBucketBinding);
            this.Controls.Add(this.btnM2Regist);
            this.Controls.Add(this.btnReturn);
            this.Controls.Add(this.btnTicketBucketBinding);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "StockRegistMenuForm";
            this.Text = "完了登陆业务 Menu";
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.StockRegistMenuForm_KeyDown);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnTicketBucketBinding;
        private System.Windows.Forms.Button btnReturn;
        private System.Windows.Forms.Button btnM2Regist;
        private System.Windows.Forms.Button btnPalletBucketBinding;
    }
}

