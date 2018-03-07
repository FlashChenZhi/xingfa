namespace wms_rft.Menu
{
    partial class StockOutMenuEtForm
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
            this.btnJobInquiry = new System.Windows.Forms.Button();
            this.btnReturn = new System.Windows.Forms.Button();
            this.btnAgvStockInOut = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // btnJobInquiry
            // 
            this.btnJobInquiry.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnJobInquiry.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnJobInquiry.ForeColor = System.Drawing.Color.Black;
            this.btnJobInquiry.Location = new System.Drawing.Point(3, 3);
            this.btnJobInquiry.Name = "btnJobInquiry";
            this.btnJobInquiry.Size = new System.Drawing.Size(236, 32);
            this.btnJobInquiry.TabIndex = 0;
            this.btnJobInquiry.Text = "1.作业数据取得";
            this.btnJobInquiry.Click += new System.EventHandler(this.btnJobInquiry_Click);
            // 
            // btnReturn
            // 
            this.btnReturn.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnReturn.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnReturn.ForeColor = System.Drawing.Color.Black;
            this.btnReturn.Location = new System.Drawing.Point(85, 246);
            this.btnReturn.Name = "btnReturn";
            this.btnReturn.Size = new System.Drawing.Size(72, 22);
            this.btnReturn.TabIndex = 1;
            this.btnReturn.Text = "返回";
            this.btnReturn.Click += new System.EventHandler(this.btnReturn_Click);
            // 
            // btnAgvStockInOut
            // 
            this.btnAgvStockInOut.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnAgvStockInOut.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnAgvStockInOut.ForeColor = System.Drawing.Color.Black;
            this.btnAgvStockInOut.Location = new System.Drawing.Point(3, 41);
            this.btnAgvStockInOut.Name = "btnAgvStockInOut";
            this.btnAgvStockInOut.Size = new System.Drawing.Size(236, 32);
            this.btnAgvStockInOut.TabIndex = 2;
            this.btnAgvStockInOut.Text = "2.AGV料箱供给/回收";
            this.btnAgvStockInOut.Click += new System.EventHandler(this.btnAgvStockInOut_Click);
            // 
            // StockOutMenuEtForm
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.btnAgvStockInOut);
            this.Controls.Add(this.btnReturn);
            this.Controls.Add(this.btnJobInquiry);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "StockOutMenuEtForm";
            this.Text = "出库业务 Menu";
            this.Load += new System.EventHandler(this.StockOutMenuEtForm_Load);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.StockOutMenuForm_KeyDown);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnJobInquiry;
        private System.Windows.Forms.Button btnReturn;
        private System.Windows.Forms.Button btnAgvStockInOut;
    }
}

