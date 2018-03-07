namespace wms_rft.Menu
{
    partial class StockQueryMenuEtForm
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
            this.btnBucketInquiry.Location = new System.Drawing.Point(3, 3);
            this.btnBucketInquiry.Name = "btnBucketInquiry";
            this.btnBucketInquiry.Size = new System.Drawing.Size(236, 32);
            this.btnBucketInquiry.TabIndex = 1;
            this.btnBucketInquiry.Text = "1.料箱No在库查询";
            this.btnBucketInquiry.Click += new System.EventHandler(this.btnBucketInquiry_Click);
            // 
            // btnBcrStatusQuery
            // 
            this.btnBcrStatusQuery.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnBcrStatusQuery.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnBcrStatusQuery.ForeColor = System.Drawing.Color.Black;
            this.btnBcrStatusQuery.Location = new System.Drawing.Point(3, 41);
            this.btnBcrStatusQuery.Name = "btnBcrStatusQuery";
            this.btnBcrStatusQuery.Size = new System.Drawing.Size(236, 32);
            this.btnBcrStatusQuery.TabIndex = 3;
            this.btnBcrStatusQuery.Text = "2.料箱No read";
            this.btnBcrStatusQuery.Click += new System.EventHandler(this.btnBcrStatusQuery_Click);
            // 
            // StockQueryMenuEtForm
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.btnBcrStatusQuery);
            this.Controls.Add(this.btnBucketInquiry);
            this.Controls.Add(this.btnReturn);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "StockQueryMenuEtForm";
            this.Text = "查询业务 Menu";
            this.Load += new System.EventHandler(this.StockQueryMenuEtForm_Load);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.StockQueryMenuForm_KeyDown);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnReturn;
        private System.Windows.Forms.Button btnBucketInquiry;
        private System.Windows.Forms.Button btnBcrStatusQuery;
    }
}

