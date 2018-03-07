namespace wms_rft.Menu
{
    partial class StockInMenuEtForm
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
            this.btnPalletStockIn1F.Location = new System.Drawing.Point(3, 3);
            this.btnPalletStockIn1F.Name = "btnPalletStockIn1F";
            this.btnPalletStockIn1F.Size = new System.Drawing.Size(236, 32);
            this.btnPalletStockIn1F.TabIndex = 1;
            this.btnPalletStockIn1F.Text = "1.托盘入库作业(平库)";
            this.btnPalletStockIn1F.Click += new System.EventHandler(this.btnPalletStockIn1F_Click);
            // 
            // StockInMenuEtForm
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.btnPalletStockIn1F);
            this.Controls.Add(this.btnReturn);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "StockInMenuEtForm";
            this.Text = "入库业务 Menu";
            this.Load += new System.EventHandler(this.StockInMenuEtForm_Load);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.StockInMenuForm_KeyDown);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnReturn;
        private System.Windows.Forms.Button btnPalletStockIn1F;
    }
}

