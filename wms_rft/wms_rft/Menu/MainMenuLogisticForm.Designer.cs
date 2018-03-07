namespace wms_rft.Menu
{
    partial class MainMenuLogisticForm
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
            this.btnBucketToBagSetting = new System.Windows.Forms.Button();
            this.btnReturn = new System.Windows.Forms.Button();
            this.btnBagToBucketBinding = new System.Windows.Forms.Button();
            this.btnOff = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // btnBucketToBagSetting
            // 
            this.btnBucketToBagSetting.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnBucketToBagSetting.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnBucketToBagSetting.ForeColor = System.Drawing.Color.Black;
            this.btnBucketToBagSetting.Location = new System.Drawing.Point(3, 3);
            this.btnBucketToBagSetting.Name = "btnBucketToBagSetting";
            this.btnBucketToBagSetting.Size = new System.Drawing.Size(236, 32);
            this.btnBucketToBagSetting.TabIndex = 0;
            this.btnBucketToBagSetting.Text = "1.发货料箱塑料袋更换";
            this.btnBucketToBagSetting.Click += new System.EventHandler(this.btnStockRegistMenu_Click);
            // 
            // btnReturn
            // 
            this.btnReturn.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnReturn.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnReturn.ForeColor = System.Drawing.Color.Black;
            this.btnReturn.Location = new System.Drawing.Point(85, 246);
            this.btnReturn.Name = "btnReturn";
            this.btnReturn.Size = new System.Drawing.Size(72, 22);
            this.btnReturn.TabIndex = 6;
            this.btnReturn.Text = "返回";
            this.btnReturn.Click += new System.EventHandler(this.btnReturn_Click);
            // 
            // btnBagToBucketBinding
            // 
            this.btnBagToBucketBinding.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnBagToBucketBinding.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnBagToBucketBinding.ForeColor = System.Drawing.Color.Black;
            this.btnBagToBucketBinding.Location = new System.Drawing.Point(3, 41);
            this.btnBagToBucketBinding.Name = "btnBagToBucketBinding";
            this.btnBagToBucketBinding.Size = new System.Drawing.Size(236, 32);
            this.btnBagToBucketBinding.TabIndex = 1;
            this.btnBagToBucketBinding.Text = "2.发货塑料袋关联设定";
            this.btnBagToBucketBinding.Click += new System.EventHandler(this.btnStockInMenu_Click);
            // 
            // btnOff
            // 
            this.btnOff.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnOff.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnOff.ForeColor = System.Drawing.Color.Black;
            this.btnOff.Location = new System.Drawing.Point(163, 246);
            this.btnOff.Name = "btnOff";
            this.btnOff.Size = new System.Drawing.Size(72, 22);
            this.btnOff.TabIndex = 7;
            this.btnOff.Text = "关机";
            this.btnOff.Visible = false;
            this.btnOff.Click += new System.EventHandler(this.btnOff_Click);
            // 
            // MainMenuLogisticForm
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.btnOff);
            this.Controls.Add(this.btnBagToBucketBinding);
            this.Controls.Add(this.btnReturn);
            this.Controls.Add(this.btnBucketToBagSetting);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "MainMenuLogisticForm";
            this.Text = "主菜单";
            this.Load += new System.EventHandler(this.MainMenuSmartForm_Load);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.MainMenuForm_KeyDown);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnBucketToBagSetting;
        private System.Windows.Forms.Button btnReturn;
        private System.Windows.Forms.Button btnBagToBucketBinding;
        private System.Windows.Forms.Button btnOff;
    }
}

