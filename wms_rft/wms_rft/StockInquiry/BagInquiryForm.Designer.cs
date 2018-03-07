namespace wms_rft.StockInquiry
{
    partial class BagInquiryForm
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
            this.btnMenu = new System.Windows.Forms.Button();
            this.panel1 = new System.Windows.Forms.Panel();
            this.lblFromBucketNo = new System.Windows.Forms.Label();
            this.label12 = new System.Windows.Forms.Label();
            this.lblColorCode = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.lblDayOfStorage = new System.Windows.Forms.Label();
            this.label8 = new System.Windows.Forms.Label();
            this.label13 = new System.Windows.Forms.Label();
            this.lblPrNo = new System.Windows.Forms.Label();
            this.lblWeight = new System.Windows.Forms.Label();
            this.label10 = new System.Windows.Forms.Label();
            this.lblQty = new System.Windows.Forms.Label();
            this.label7 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.lblItemCode = new System.Windows.Forms.Label();
            this.lblItemName1 = new System.Windows.Forms.Label();
            this.lblItemName2 = new System.Windows.Forms.Label();
            this.lblItemName3 = new System.Windows.Forms.Label();
            this.panel2 = new System.Windows.Forms.Panel();
            this.lblMessage = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.txtBagNo = new System.Windows.Forms.TextBox();
            this.lblFromAreaName = new System.Windows.Forms.Label();
            this.lblFromLocationNo = new System.Windows.Forms.Label();
            this.panel1.SuspendLayout();
            this.panel2.SuspendLayout();
            this.SuspendLayout();
            // 
            // btnMenu
            // 
            this.btnMenu.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnMenu.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnMenu.ForeColor = System.Drawing.Color.Black;
            this.btnMenu.Location = new System.Drawing.Point(85, 99);
            this.btnMenu.Name = "btnMenu";
            this.btnMenu.Size = new System.Drawing.Size(72, 22);
            this.btnMenu.TabIndex = 0;
            this.btnMenu.Text = "返回";
            this.btnMenu.Click += new System.EventHandler(this.btnMenu_Click);
            // 
            // panel1
            // 
            this.panel1.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(184)))), ((int)(((byte)(183)))), ((int)(((byte)(215)))));
            this.panel1.Controls.Add(this.lblFromBucketNo);
            this.panel1.Controls.Add(this.label12);
            this.panel1.Controls.Add(this.lblColorCode);
            this.panel1.Controls.Add(this.label2);
            this.panel1.Controls.Add(this.lblDayOfStorage);
            this.panel1.Controls.Add(this.label8);
            this.panel1.Controls.Add(this.label13);
            this.panel1.Controls.Add(this.lblPrNo);
            this.panel1.Controls.Add(this.lblWeight);
            this.panel1.Controls.Add(this.label10);
            this.panel1.Controls.Add(this.lblQty);
            this.panel1.Controls.Add(this.label7);
            this.panel1.Controls.Add(this.label5);
            this.panel1.Controls.Add(this.lblItemCode);
            this.panel1.Controls.Add(this.lblItemName1);
            this.panel1.Controls.Add(this.lblItemName2);
            this.panel1.Controls.Add(this.lblItemName3);
            this.panel1.Location = new System.Drawing.Point(0, 26);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(242, 120);
            // 
            // lblFromBucketNo
            // 
            this.lblFromBucketNo.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblFromBucketNo.Location = new System.Drawing.Point(68, 2);
            this.lblFromBucketNo.Name = "lblFromBucketNo";
            this.lblFromBucketNo.Size = new System.Drawing.Size(54, 15);
            this.lblFromBucketNo.Text = "X999999";
            // 
            // label12
            // 
            this.label12.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label12.Location = new System.Drawing.Point(3, 2);
            this.label12.Name = "label12";
            this.label12.Size = new System.Drawing.Size(61, 15);
            this.label12.Text = "料箱号";
            // 
            // lblColorCode
            // 
            this.lblColorCode.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblColorCode.Location = new System.Drawing.Point(68, 74);
            this.lblColorCode.Name = "lblColorCode";
            this.lblColorCode.Size = new System.Drawing.Size(61, 15);
            this.lblColorCode.Text = "XXXXXXX";
            // 
            // label2
            // 
            this.label2.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label2.Location = new System.Drawing.Point(3, 74);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(48, 15);
            this.label2.Text = "颜色";
            // 
            // lblDayOfStorage
            // 
            this.lblDayOfStorage.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(184)))), ((int)(((byte)(183)))), ((int)(((byte)(215)))));
            this.lblDayOfStorage.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblDayOfStorage.Location = new System.Drawing.Point(62, 104);
            this.lblDayOfStorage.Name = "lblDayOfStorage";
            this.lblDayOfStorage.Size = new System.Drawing.Size(147, 15);
            this.lblDayOfStorage.Text = "YYYY/MM/DD HH:MM";
            // 
            // label8
            // 
            this.label8.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label8.Location = new System.Drawing.Point(3, 16);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(59, 15);
            this.label8.Text = "传票Pr No.";
            // 
            // label13
            // 
            this.label13.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(184)))), ((int)(((byte)(183)))), ((int)(((byte)(215)))));
            this.label13.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label13.Location = new System.Drawing.Point(3, 102);
            this.label13.Name = "label13";
            this.label13.Size = new System.Drawing.Size(83, 15);
            this.label13.Text = "入库时间";
            // 
            // lblPrNo
            // 
            this.lblPrNo.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblPrNo.Location = new System.Drawing.Point(68, 16);
            this.lblPrNo.Name = "lblPrNo";
            this.lblPrNo.Size = new System.Drawing.Size(100, 15);
            this.lblPrNo.Text = "XXXXXXXXXX";
            // 
            // lblWeight
            // 
            this.lblWeight.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblWeight.Location = new System.Drawing.Point(183, 88);
            this.lblWeight.Name = "lblWeight";
            this.lblWeight.Size = new System.Drawing.Size(46, 15);
            this.lblWeight.Text = "Z9.9999";
            // 
            // label10
            // 
            this.label10.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label10.Location = new System.Drawing.Point(132, 88);
            this.label10.Name = "label10";
            this.label10.Size = new System.Drawing.Size(48, 15);
            this.label10.Text = "重量";
            // 
            // lblQty
            // 
            this.lblQty.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblQty.Location = new System.Drawing.Point(68, 88);
            this.lblQty.Name = "lblQty";
            this.lblQty.Size = new System.Drawing.Size(46, 15);
            this.lblQty.Text = "ZZZZZ9";
            // 
            // label7
            // 
            this.label7.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label7.Location = new System.Drawing.Point(3, 28);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(48, 15);
            this.label7.Text = "Item";
            // 
            // label5
            // 
            this.label5.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label5.Location = new System.Drawing.Point(3, 88);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(48, 15);
            this.label5.Text = "数量";
            // 
            // lblItemCode
            // 
            this.lblItemCode.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblItemCode.Location = new System.Drawing.Point(68, 28);
            this.lblItemCode.Name = "lblItemCode";
            this.lblItemCode.Size = new System.Drawing.Size(60, 15);
            this.lblItemCode.Text = "XXXXXXX";
            // 
            // lblItemName1
            // 
            this.lblItemName1.Font = new System.Drawing.Font("Tahoma", 7F, System.Drawing.FontStyle.Regular);
            this.lblItemName1.Location = new System.Drawing.Point(68, 41);
            this.lblItemName1.Name = "lblItemName1";
            this.lblItemName1.Size = new System.Drawing.Size(176, 14);
            this.lblItemName1.Text = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
            // 
            // lblItemName2
            // 
            this.lblItemName2.Font = new System.Drawing.Font("Tahoma", 7F, System.Drawing.FontStyle.Regular);
            this.lblItemName2.Location = new System.Drawing.Point(68, 53);
            this.lblItemName2.Name = "lblItemName2";
            this.lblItemName2.Size = new System.Drawing.Size(176, 14);
            this.lblItemName2.Text = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
            // 
            // lblItemName3
            // 
            this.lblItemName3.Font = new System.Drawing.Font("Tahoma", 7F, System.Drawing.FontStyle.Regular);
            this.lblItemName3.Location = new System.Drawing.Point(68, 65);
            this.lblItemName3.Name = "lblItemName3";
            this.lblItemName3.Size = new System.Drawing.Size(176, 14);
            this.lblItemName3.Text = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
            // 
            // panel2
            // 
            this.panel2.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(184)))), ((int)(((byte)(183)))), ((int)(((byte)(215)))));
            this.panel2.Controls.Add(this.lblMessage);
            this.panel2.Controls.Add(this.btnMenu);
            this.panel2.Location = new System.Drawing.Point(0, 148);
            this.panel2.Name = "panel2";
            this.panel2.Size = new System.Drawing.Size(242, 123);
            // 
            // lblMessage
            // 
            this.lblMessage.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblMessage.Location = new System.Drawing.Point(3, 82);
            this.lblMessage.Name = "lblMessage";
            this.lblMessage.Size = new System.Drawing.Size(236, 15);
            this.lblMessage.Text = "Message Area";
            // 
            // label1
            // 
            this.label1.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.label1.Location = new System.Drawing.Point(3, 4);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(86, 20);
            this.label1.Text = "塑料袋#:";
            // 
            // txtBagNo
            // 
            this.txtBagNo.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.txtBagNo.Location = new System.Drawing.Point(62, 3);
            this.txtBagNo.MaxLength = 7;
            this.txtBagNo.Name = "txtBagNo";
            this.txtBagNo.Size = new System.Drawing.Size(80, 21);
            this.txtBagNo.TabIndex = 0;
            this.txtBagNo.Text = "X999999";
            this.txtBagNo.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.txtBagNo_KeyPress);
            // 
            // lblFromAreaName
            // 
            this.lblFromAreaName.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblFromAreaName.Location = new System.Drawing.Point(148, 11);
            this.lblFromAreaName.Name = "lblFromAreaName";
            this.lblFromAreaName.Size = new System.Drawing.Size(93, 15);
            this.lblFromAreaName.Text = "XXXXXXX...XXXXX";
            // 
            // lblFromLocationNo
            // 
            this.lblFromLocationNo.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.lblFromLocationNo.Location = new System.Drawing.Point(148, 0);
            this.lblFromLocationNo.Name = "lblFromLocationNo";
            this.lblFromLocationNo.Size = new System.Drawing.Size(78, 15);
            this.lblFromLocationNo.Text = "X999-999-999";
            // 
            // BagInquiryForm
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.lblFromAreaName);
            this.Controls.Add(this.lblFromLocationNo);
            this.Controls.Add(this.txtBagNo);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.panel2);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "BagInquiryForm";
            this.Text = "塑料袋No在库查询";
            this.Load += new System.EventHandler(this.BagMoveForm_Load);
            this.Closing += new System.ComponentModel.CancelEventHandler(this.BagMoveForm_Closing);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.BagInquiryForm_KeyDown);
            this.panel1.ResumeLayout(false);
            this.panel2.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnMenu;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Panel panel2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox txtBagNo;
        private System.Windows.Forms.Label lblMessage;
        private System.Windows.Forms.Label lblWeight;
        private System.Windows.Forms.Label label10;
        private System.Windows.Forms.Label lblQty;
        private System.Windows.Forms.Label lblColorCode;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label lblItemCode;
        private System.Windows.Forms.Label lblItemName1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label lblItemName2;
        private System.Windows.Forms.Label lblItemName3;
        private System.Windows.Forms.Label label8;
        private System.Windows.Forms.Label lblPrNo;
        private System.Windows.Forms.Label lblFromAreaName;
        private System.Windows.Forms.Label lblFromLocationNo;
        private System.Windows.Forms.Label lblDayOfStorage;
        private System.Windows.Forms.Label label13;
        private System.Windows.Forms.Label lblFromBucketNo;
        private System.Windows.Forms.Label label12;
    }
}

