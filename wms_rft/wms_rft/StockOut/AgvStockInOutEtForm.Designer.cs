namespace wms_rft.StockOut
{
    partial class AgvStockInOutEtForm
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
            this.btnSubmit = new System.Windows.Forms.Button();
            this.btnMenu = new System.Windows.Forms.Button();
            this.panel1 = new System.Windows.Forms.Panel();
            this.lblCollectingBucketCount = new System.Windows.Forms.Label();
            this.label8 = new System.Windows.Forms.Label();
            this.lblRetrievingBucketCount = new System.Windows.Forms.Label();
            this.txtSupplySettingQty = new System.Windows.Forms.TextBox();
            this.label4 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.panel4 = new System.Windows.Forms.Panel();
            this.rdoRetrievalDate = new System.Windows.Forms.RadioButton();
            this.rdoCoatingColor = new System.Windows.Forms.RadioButton();
            this.panel3 = new System.Windows.Forms.Panel();
            this.rdoSupply = new System.Windows.Forms.RadioButton();
            this.rdoCollect2 = new System.Windows.Forms.RadioButton();
            this.rdoCollect1 = new System.Windows.Forms.RadioButton();
            this.lblRetrievalAvailableQty = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.lblCoatingMachineShortName = new System.Windows.Forms.Label();
            this.label7 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.panel2 = new System.Windows.Forms.Panel();
            this.lblMessage = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.txtCoatingMachineCode = new System.Windows.Forms.TextBox();
            this.panel1.SuspendLayout();
            this.panel4.SuspendLayout();
            this.panel3.SuspendLayout();
            this.panel2.SuspendLayout();
            this.SuspendLayout();
            // 
            // btnSubmit
            // 
            this.btnSubmit.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.btnSubmit.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnSubmit.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnSubmit.ForeColor = System.Drawing.Color.Black;
            this.btnSubmit.Location = new System.Drawing.Point(28, 26);
            this.btnSubmit.Name = "btnSubmit";
            this.btnSubmit.Size = new System.Drawing.Size(72, 22);
            this.btnSubmit.TabIndex = 0;
            this.btnSubmit.Text = "提交";
            this.btnSubmit.Click += new System.EventHandler(this.btnSubmit_Click);
            // 
            // btnMenu
            // 
            this.btnMenu.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.btnMenu.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnMenu.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnMenu.ForeColor = System.Drawing.Color.Black;
            this.btnMenu.Location = new System.Drawing.Point(143, 26);
            this.btnMenu.Name = "btnMenu";
            this.btnMenu.Size = new System.Drawing.Size(72, 22);
            this.btnMenu.TabIndex = 1;
            this.btnMenu.Text = "返回";
            this.btnMenu.Click += new System.EventHandler(this.btnMenu_Click);
            // 
            // panel1
            // 
            this.panel1.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(184)))), ((int)(((byte)(183)))), ((int)(((byte)(215)))));
            this.panel1.Controls.Add(this.lblCollectingBucketCount);
            this.panel1.Controls.Add(this.label8);
            this.panel1.Controls.Add(this.lblRetrievingBucketCount);
            this.panel1.Controls.Add(this.txtSupplySettingQty);
            this.panel1.Controls.Add(this.label4);
            this.panel1.Controls.Add(this.label2);
            this.panel1.Controls.Add(this.panel4);
            this.panel1.Controls.Add(this.panel3);
            this.panel1.Controls.Add(this.lblRetrievalAvailableQty);
            this.panel1.Controls.Add(this.label3);
            this.panel1.Controls.Add(this.lblCoatingMachineShortName);
            this.panel1.Controls.Add(this.label7);
            this.panel1.Controls.Add(this.label5);
            this.panel1.Location = new System.Drawing.Point(0, 24);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(242, 188);
            // 
            // lblCollectingBucketCount
            // 
            this.lblCollectingBucketCount.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblCollectingBucketCount.Location = new System.Drawing.Point(115, 150);
            this.lblCollectingBucketCount.Name = "lblCollectingBucketCount";
            this.lblCollectingBucketCount.Size = new System.Drawing.Size(107, 15);
            this.lblCollectingBucketCount.Text = "Z9";
            // 
            // label8
            // 
            this.label8.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label8.Location = new System.Drawing.Point(3, 150);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(106, 15);
            this.label8.Text = "回收要求山数";
            // 
            // lblRetrievingBucketCount
            // 
            this.lblRetrievingBucketCount.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblRetrievingBucketCount.Location = new System.Drawing.Point(115, 129);
            this.lblRetrievingBucketCount.Name = "lblRetrievingBucketCount";
            this.lblRetrievingBucketCount.Size = new System.Drawing.Size(107, 15);
            this.lblRetrievingBucketCount.Text = "Z9";
            // 
            // txtSupplySettingQty
            // 
            this.txtSupplySettingQty.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.txtSupplySettingQty.Location = new System.Drawing.Point(77, 102);
            this.txtSupplySettingQty.MaxLength = 2;
            this.txtSupplySettingQty.Name = "txtSupplySettingQty";
            this.txtSupplySettingQty.Size = new System.Drawing.Size(33, 19);
            this.txtSupplySettingQty.TabIndex = 0;
            this.txtSupplySettingQty.Text = "Z9";
            // 
            // label4
            // 
            this.label4.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label4.Location = new System.Drawing.Point(3, 106);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(68, 15);
            this.label4.Text = "要求设定数";
            // 
            // label2
            // 
            this.label2.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label2.Location = new System.Drawing.Point(3, 59);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(54, 15);
            this.label2.Text = "出库优先";
            // 
            // panel4
            // 
            this.panel4.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(184)))), ((int)(((byte)(183)))), ((int)(((byte)(215)))));
            this.panel4.Controls.Add(this.rdoRetrievalDate);
            this.panel4.Controls.Add(this.rdoCoatingColor);
            this.panel4.Location = new System.Drawing.Point(63, 51);
            this.panel4.Name = "panel4";
            this.panel4.Size = new System.Drawing.Size(176, 25);
            // 
            // rdoRetrievalDate
            // 
            this.rdoRetrievalDate.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.rdoRetrievalDate.Location = new System.Drawing.Point(88, 3);
            this.rdoRetrievalDate.Name = "rdoRetrievalDate";
            this.rdoRetrievalDate.Size = new System.Drawing.Size(80, 20);
            this.rdoRetrievalDate.TabIndex = 1;
            this.rdoRetrievalDate.TabStop = false;
            this.rdoRetrievalDate.Text = "纳期";
            // 
            // rdoCoatingColor
            // 
            this.rdoCoatingColor.Checked = true;
            this.rdoCoatingColor.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.rdoCoatingColor.Location = new System.Drawing.Point(8, 3);
            this.rdoCoatingColor.Name = "rdoCoatingColor";
            this.rdoCoatingColor.Size = new System.Drawing.Size(80, 20);
            this.rdoCoatingColor.TabIndex = 0;
            this.rdoCoatingColor.Text = "涂装色";
            // 
            // panel3
            // 
            this.panel3.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(184)))), ((int)(((byte)(183)))), ((int)(((byte)(215)))));
            this.panel3.Controls.Add(this.rdoSupply);
            this.panel3.Controls.Add(this.rdoCollect2);
            this.panel3.Controls.Add(this.rdoCollect1);
            this.panel3.Location = new System.Drawing.Point(3, 26);
            this.panel3.Name = "panel3";
            this.panel3.Size = new System.Drawing.Size(236, 25);
            // 
            // rdoSupply
            // 
            this.rdoSupply.Checked = true;
            this.rdoSupply.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.rdoSupply.Location = new System.Drawing.Point(8, 3);
            this.rdoSupply.Name = "rdoSupply";
            this.rdoSupply.Size = new System.Drawing.Size(60, 20);
            this.rdoSupply.TabIndex = 0;
            this.rdoSupply.Text = "供给";
            // 
            // rdoCollect2
            // 
            this.rdoCollect2.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.rdoCollect2.Location = new System.Drawing.Point(148, 3);
            this.rdoCollect2.Name = "rdoCollect2";
            this.rdoCollect2.Size = new System.Drawing.Size(80, 20);
            this.rdoCollect2.TabIndex = 2;
            this.rdoCollect2.TabStop = false;
            this.rdoCollect2.Text = "回收(2山)";
            // 
            // rdoCollect1
            // 
            this.rdoCollect1.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.rdoCollect1.Location = new System.Drawing.Point(68, 3);
            this.rdoCollect1.Name = "rdoCollect1";
            this.rdoCollect1.Size = new System.Drawing.Size(80, 20);
            this.rdoCollect1.TabIndex = 1;
            this.rdoCollect1.TabStop = false;
            this.rdoCollect1.Text = "回收(1山)";
            // 
            // lblRetrievalAvailableQty
            // 
            this.lblRetrievalAvailableQty.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblRetrievalAvailableQty.Location = new System.Drawing.Point(77, 82);
            this.lblRetrievalAvailableQty.Name = "lblRetrievalAvailableQty";
            this.lblRetrievalAvailableQty.Size = new System.Drawing.Size(107, 15);
            this.lblRetrievalAvailableQty.Text = "Z9";
            // 
            // label3
            // 
            this.label3.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label3.Location = new System.Drawing.Point(3, 83);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(68, 15);
            this.label3.Text = "要求可能数";
            // 
            // lblCoatingMachineShortName
            // 
            this.lblCoatingMachineShortName.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblCoatingMachineShortName.Location = new System.Drawing.Point(77, 9);
            this.lblCoatingMachineShortName.Name = "lblCoatingMachineShortName";
            this.lblCoatingMachineShortName.Size = new System.Drawing.Size(107, 15);
            this.lblCoatingMachineShortName.Text = "XXXXXXXXXXXXXXXXX";
            // 
            // label7
            // 
            this.label7.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label7.Location = new System.Drawing.Point(3, 9);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(68, 15);
            this.label7.Text = "涂装机略称";
            // 
            // label5
            // 
            this.label5.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label5.Location = new System.Drawing.Point(3, 129);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(106, 15);
            this.label5.Text = "出库搬送中料箱数";
            // 
            // panel2
            // 
            this.panel2.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(184)))), ((int)(((byte)(183)))), ((int)(((byte)(215)))));
            this.panel2.Controls.Add(this.lblMessage);
            this.panel2.Controls.Add(this.btnSubmit);
            this.panel2.Controls.Add(this.btnMenu);
            this.panel2.Location = new System.Drawing.Point(0, 214);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(242, 57);
            // 
            // lblMessage
            // 
            this.lblMessage.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
            this.lblMessage.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblMessage.Location = new System.Drawing.Point(3, 5);
            this.lblMessage.Name = "lblMessage";
            this.lblMessage.Size = new System.Drawing.Size(236, 15);
            this.lblMessage.Text = "Message Area";
            // 
            // label1
            // 
            this.label1.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.label1.Location = new System.Drawing.Point(3, 2);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(86, 20);
            this.label1.Text = "涂装机Code:";
            // 
            // txtCoatingMachineCode
            // 
            this.txtCoatingMachineCode.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.txtCoatingMachineCode.Location = new System.Drawing.Point(94, 1);
            this.txtCoatingMachineCode.MaxLength = 7;
            this.txtCoatingMachineCode.Name = "txtCoatingMachineCode";
            this.txtCoatingMachineCode.Size = new System.Drawing.Size(58, 21);
            this.txtCoatingMachineCode.TabIndex = 0;
            this.txtCoatingMachineCode.Text = "XXXXXX";
            this.txtCoatingMachineCode.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.txtCoatingMachineCode_KeyPress);
            // 
            // AgvStockInOutEtForm
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.txtCoatingMachineCode);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.panel2);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "AgvStockInOutEtForm";
            this.Text = "AGV料箱供给/回收";
            this.Load += new System.EventHandler(this.AgvStockInOutEtForm_Load);
            this.Closing += new System.ComponentModel.CancelEventHandler(this.AgvStockInOutEtForm_Closing);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.AgvStockInOutEtForm_KeyDown);
            this.panel1.ResumeLayout(false);
            this.panel4.ResumeLayout(false);
            this.panel3.ResumeLayout(false);
            this.panel2.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnSubmit;
        private System.Windows.Forms.Button btnMenu;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox txtCoatingMachineCode;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label lblMessage;
        private System.Windows.Forms.Label lblCoatingMachineShortName;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.Label lblRetrievalAvailableQty;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.RadioButton rdoCollect2;
        private System.Windows.Forms.RadioButton rdoCollect1;
        private System.Windows.Forms.RadioButton rdoSupply;
        private System.Windows.Forms.Panel panel4;
        private System.Windows.Forms.RadioButton rdoRetrievalDate;
        private System.Windows.Forms.RadioButton rdoCoatingColor;
        private System.Windows.Forms.Panel panel3;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.TextBox txtSupplySettingQty;
        private System.Windows.Forms.Label lblRetrievingBucketCount;
        private System.Windows.Forms.Label lblCollectingBucketCount;
        private System.Windows.Forms.Label label8;
    }
}

