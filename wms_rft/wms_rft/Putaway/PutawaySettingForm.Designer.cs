namespace wms_rft.Putaway
{
    partial class PutawaySettingForm
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
            this.txt_PalletNo = new System.Windows.Forms.TextBox();
            this.lbl_PalletNo = new System.Windows.Forms.Label();
            this.lbl_SkuCode = new System.Windows.Forms.Label();
            this.pul_SkuCode = new System.Windows.Forms.ComboBox();
            this.lbl_Qty = new System.Windows.Forms.Label();
            this.txt_Qty = new System.Windows.Forms.NumericUpDown();
            this.btn_Setting = new System.Windows.Forms.Button();
            this.btn_Clear = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // txt_PalletNo
            // 
            this.txt_PalletNo.Location = new System.Drawing.Point(66, 15);
            this.txt_PalletNo.Name = "txt_PalletNo";
            this.txt_PalletNo.Size = new System.Drawing.Size(163, 23);
            this.txt_PalletNo.TabIndex = 0;
            // 
            // lbl_PalletNo
            // 
            this.lbl_PalletNo.Location = new System.Drawing.Point(3, 18);
            this.lbl_PalletNo.Name = "lbl_PalletNo";
            this.lbl_PalletNo.Size = new System.Drawing.Size(57, 20);
            this.lbl_PalletNo.Text = "托盘号";
            // 
            // lbl_SkuCode
            // 
            this.lbl_SkuCode.Location = new System.Drawing.Point(3, 53);
            this.lbl_SkuCode.Name = "lbl_SkuCode";
            this.lbl_SkuCode.Size = new System.Drawing.Size(74, 20);
            this.lbl_SkuCode.Text = "商品代码";
            // 
            // pul_SkuCode
            // 
            this.pul_SkuCode.Location = new System.Drawing.Point(84, 49);
            this.pul_SkuCode.Name = "pul_SkuCode";
            this.pul_SkuCode.Size = new System.Drawing.Size(145, 23);
            this.pul_SkuCode.TabIndex = 3;
            // 
            // lbl_Qty
            // 
            this.lbl_Qty.Location = new System.Drawing.Point(3, 91);
            this.lbl_Qty.Name = "lbl_Qty";
            this.lbl_Qty.Size = new System.Drawing.Size(57, 20);
            this.lbl_Qty.Text = "数量";
            // 
            // txt_Qty
            // 
            this.txt_Qty.Location = new System.Drawing.Point(66, 87);
            this.txt_Qty.Maximum = new decimal(new int[] {
            99999999,
            0,
            0,
            0});
            this.txt_Qty.Minimum = new decimal(new int[] {
            1,
            0,
            0,
            0});
            this.txt_Qty.Name = "txt_Qty";
            this.txt_Qty.Size = new System.Drawing.Size(163, 24);
            this.txt_Qty.TabIndex = 5;
            this.txt_Qty.Value = new decimal(new int[] {
            1,
            0,
            0,
            0});
            // 
            // btn_Setting
            // 
            this.btn_Setting.Location = new System.Drawing.Point(79, 160);
            this.btn_Setting.Name = "btn_Setting";
            this.btn_Setting.Size = new System.Drawing.Size(72, 20);
            this.btn_Setting.TabIndex = 6;
            this.btn_Setting.Text = "设定";
            // 
            // btn_Clear
            // 
            this.btn_Clear.Location = new System.Drawing.Point(157, 160);
            this.btn_Clear.Name = "btn_Clear";
            this.btn_Clear.Size = new System.Drawing.Size(72, 20);
            this.btn_Clear.TabIndex = 7;
            this.btn_Clear.Text = "清除";
            // 
            // PutawaySettingForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(96F, 96F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
            this.AutoScroll = true;
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.Controls.Add(this.btn_Clear);
            this.Controls.Add(this.btn_Setting);
            this.Controls.Add(this.txt_Qty);
            this.Controls.Add(this.lbl_Qty);
            this.Controls.Add(this.pul_SkuCode);
            this.Controls.Add(this.lbl_SkuCode);
            this.Controls.Add(this.lbl_PalletNo);
            this.Controls.Add(this.txt_PalletNo);
            this.Name = "PutawaySettingForm";
            this.Text = "入库";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TextBox txt_PalletNo;
        private System.Windows.Forms.Label lbl_PalletNo;
        private System.Windows.Forms.Label lbl_SkuCode;
        private System.Windows.Forms.ComboBox pul_SkuCode;
        private System.Windows.Forms.Label lbl_Qty;
        private System.Windows.Forms.NumericUpDown txt_Qty;
        private System.Windows.Forms.Button btn_Setting;
        private System.Windows.Forms.Button btn_Clear;
    }
}