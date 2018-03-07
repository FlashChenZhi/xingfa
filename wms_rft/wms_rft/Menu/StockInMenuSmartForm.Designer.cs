namespace wms_rft.Menu
{
    partial class StockInMenuSmartForm
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
            this.btnPalletStockIn1F = new System.Windows.Forms.Button();
            this.btnBagStockInBZ = new System.Windows.Forms.Button();
            this.btnBucketStockInBZ = new System.Windows.Forms.Button();
            this.btnPalletStockInBZ = new System.Windows.Forms.Button();
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
            // btnPalletStockIn1F
            // 
            this.btnPalletStockIn1F.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnPalletStockIn1F.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnPalletStockIn1F.ForeColor = System.Drawing.Color.Black;
            this.btnPalletStockIn1F.Location = new System.Drawing.Point(3, 41);
            this.btnPalletStockIn1F.Name = "btnPalletStockIn1F";
            this.btnPalletStockIn1F.Size = new System.Drawing.Size(236, 32);
            this.btnPalletStockIn1F.TabIndex = 1;
            this.btnPalletStockIn1F.Text = "2.1F托盘入库作业(平库)";
            this.btnPalletStockIn1F.Click += new System.EventHandler(this.btnPalletStockIn1F_Click);
            // 
            // btnBagStockInBZ
            // 
            this.btnBagStockInBZ.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnBagStockInBZ.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnBagStockInBZ.ForeColor = System.Drawing.Color.Black;
            this.btnBagStockInBZ.Location = new System.Drawing.Point(3, 117);
            this.btnBagStockInBZ.Name = "btnBagStockInBZ";
            this.btnBagStockInBZ.Size = new System.Drawing.Size(236, 32);
            this.btnBagStockInBZ.TabIndex = 3;
            this.btnBagStockInBZ.Text = "4.塑料袋入库作业(BZ)";
            this.btnBagStockInBZ.Click += new System.EventHandler(this.btnBagStockInBZ_Click);
            // 
            // btnBucketStockInBZ
            // 
            this.btnBucketStockInBZ.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnBucketStockInBZ.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnBucketStockInBZ.ForeColor = System.Drawing.Color.Black;
            this.btnBucketStockInBZ.Location = new System.Drawing.Point(3, 79);
            this.btnBucketStockInBZ.Name = "btnBucketStockInBZ";
            this.btnBucketStockInBZ.Size = new System.Drawing.Size(236, 32);
            this.btnBucketStockInBZ.TabIndex = 2;
            this.btnBucketStockInBZ.Text = "3.料箱入库作业(BZ)";
            this.btnBucketStockInBZ.Click += new System.EventHandler(this.btnBucketStockInBZ_Click);
            // 
            // btnPalletStockInBZ
            // 
            this.btnPalletStockInBZ.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnPalletStockInBZ.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnPalletStockInBZ.ForeColor = System.Drawing.Color.Black;
            this.btnPalletStockInBZ.Location = new System.Drawing.Point(3, 3);
            this.btnPalletStockInBZ.Name = "btnPalletStockInBZ";
            this.btnPalletStockInBZ.Size = new System.Drawing.Size(236, 32);
            this.btnPalletStockInBZ.TabIndex = 0;
            this.btnPalletStockInBZ.Text = "1.托盘入库作业(BZ)";
            this.btnPalletStockInBZ.Click += new System.EventHandler(this.btnPalletStockInBZ_Click);
            // 
            // StockInMenuSmartForm
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.btnBagStockInBZ);
            this.Controls.Add(this.btnBucketStockInBZ);
            this.Controls.Add(this.btnPalletStockIn1F);
            this.Controls.Add(this.btnReturn);
            this.Controls.Add(this.btnPalletStockInBZ);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "StockInMenuSmartForm";
            this.Text = "入库业务 Menu";
            this.Load += new System.EventHandler(this.StockInMenuSmartForm_Load);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.StockInMenuForm_KeyDown);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnReturn;
        private System.Windows.Forms.Button btnPalletStockIn1F;
        private System.Windows.Forms.Button btnBagStockInBZ;
        private System.Windows.Forms.Button btnBucketStockInBZ;
        private System.Windows.Forms.Button btnPalletStockInBZ;
    }
}

