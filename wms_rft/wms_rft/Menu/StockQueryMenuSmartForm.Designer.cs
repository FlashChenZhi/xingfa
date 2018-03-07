namespace wms_rft.Menu
{
    partial class StockQueryMenuSmartForm
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
            this.btnReturn = new System.Windows.Forms.Button();
            this.btnBucketInquiry = new System.Windows.Forms.Button();
            this.btnBcrStatusQuery = new System.Windows.Forms.Button();
            this.btnBagInquiry = new System.Windows.Forms.Button();
            this.btnPalletInquiry = new System.Windows.Forms.Button();
            this.btnPreM2Inquiry = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // btnReturn
            // 
            this.btnReturn.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnReturn.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnReturn.ForeColor = System.Drawing.Color.Black;
            this.btnReturn.Location = new System.Drawing.Point(85, 246);
            this.btnReturn.Name = "btnReturn";
            this.btnReturn.Size = new System.Drawing.Size(72, 22);
            this.btnReturn.TabIndex = 4;
            this.btnReturn.Text = "返回";
            this.btnReturn.Click += new System.EventHandler(this.btnReturn_Click);
            // 
            // btnBucketInquiry
            // 
            this.btnBucketInquiry.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnBucketInquiry.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnBucketInquiry.ForeColor = System.Drawing.Color.Black;
            this.btnBucketInquiry.Location = new System.Drawing.Point(3, 41);
            this.btnBucketInquiry.Name = "btnBucketInquiry";
            this.btnBucketInquiry.Size = new System.Drawing.Size(236, 32);
            this.btnBucketInquiry.TabIndex = 1;
            this.btnBucketInquiry.Text = "2.料箱No在库查询";
            this.btnBucketInquiry.Click += new System.EventHandler(this.btnBucketInquiry_Click);
            // 
            // btnBcrStatusQuery
            // 
            this.btnBcrStatusQuery.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnBcrStatusQuery.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnBcrStatusQuery.ForeColor = System.Drawing.Color.Black;
            this.btnBcrStatusQuery.Location = new System.Drawing.Point(3, 119);
            this.btnBcrStatusQuery.Name = "btnBcrStatusQuery";
            this.btnBcrStatusQuery.Size = new System.Drawing.Size(236, 32);
            this.btnBcrStatusQuery.TabIndex = 3;
            this.btnBcrStatusQuery.Text = "4.料箱No read";
            this.btnBcrStatusQuery.Click += new System.EventHandler(this.btnBcrStatusQuery_Click);
            // 
            // btnBagInquiry
            // 
            this.btnBagInquiry.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnBagInquiry.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnBagInquiry.ForeColor = System.Drawing.Color.Black;
            this.btnBagInquiry.Location = new System.Drawing.Point(3, 79);
            this.btnBagInquiry.Name = "btnBagInquiry";
            this.btnBagInquiry.Size = new System.Drawing.Size(236, 32);
            this.btnBagInquiry.TabIndex = 2;
            this.btnBagInquiry.Text = "3.塑料No在库查询";
            this.btnBagInquiry.Click += new System.EventHandler(this.btnBagInquiry_Click);
            // 
            // btnPalletInquiry
            // 
            this.btnPalletInquiry.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnPalletInquiry.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnPalletInquiry.ForeColor = System.Drawing.Color.Black;
            this.btnPalletInquiry.Location = new System.Drawing.Point(3, 3);
            this.btnPalletInquiry.Name = "btnPalletInquiry";
            this.btnPalletInquiry.Size = new System.Drawing.Size(236, 32);
            this.btnPalletInquiry.TabIndex = 0;
            this.btnPalletInquiry.Text = "1.托盘No在库查询";
            this.btnPalletInquiry.Click += new System.EventHandler(this.btnPalletInquiry_Click);
            // 
            // btnPreM2Inquiry
            // 
            this.btnPreM2Inquiry.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnPreM2Inquiry.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnPreM2Inquiry.ForeColor = System.Drawing.Color.Black;
            this.btnPreM2Inquiry.Location = new System.Drawing.Point(3, 157);
            this.btnPreM2Inquiry.Name = "btnPreM2Inquiry";
            this.btnPreM2Inquiry.Size = new System.Drawing.Size(236, 32);
            this.btnPreM2Inquiry.TabIndex = 5;
            this.btnPreM2Inquiry.Text = "5.M2关联查询";
            this.btnPreM2Inquiry.Click += new System.EventHandler(this.btnPreM2Inquiry_Click);
            // 
            // StockQueryMenuSmartForm
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.btnPreM2Inquiry);
            this.Controls.Add(this.btnBcrStatusQuery);
            this.Controls.Add(this.btnBagInquiry);
            this.Controls.Add(this.btnBucketInquiry);
            this.Controls.Add(this.btnReturn);
            this.Controls.Add(this.btnPalletInquiry);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "StockQueryMenuSmartForm";
            this.Text = "查询业务 Menu";
            this.Load += new System.EventHandler(this.StockQueryMenuSmartForm_Load);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.StockQueryMenuForm_KeyDown);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnReturn;
        private System.Windows.Forms.Button btnBucketInquiry;
        private System.Windows.Forms.Button btnBcrStatusQuery;
        private System.Windows.Forms.Button btnBagInquiry;
        private System.Windows.Forms.Button btnPalletInquiry;
        private System.Windows.Forms.Button btnPreM2Inquiry;
    }
}

