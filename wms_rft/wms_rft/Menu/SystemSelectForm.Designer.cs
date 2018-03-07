namespace wms_rft.Menu
{
    partial class SystemSelectForm
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
            this.btnSelectSmart = new System.Windows.Forms.Button();
            this.btnReturn = new System.Windows.Forms.Button();
            this.btnSelectEtPicking = new System.Windows.Forms.Button();
            this.btnOff = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.btnSelectLogistic = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // btnSelectSmart
            // 
            this.btnSelectSmart.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnSelectSmart.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnSelectSmart.ForeColor = System.Drawing.Color.Black;
            this.btnSelectSmart.Location = new System.Drawing.Point(3, 3);
            this.btnSelectSmart.Name = "btnSelectSmart";
            this.btnSelectSmart.Size = new System.Drawing.Size(236, 32);
            this.btnSelectSmart.TabIndex = 0;
            this.btnSelectSmart.Text = "1.Smart";
            this.btnSelectSmart.Click += new System.EventHandler(this.btnSelectSmart_Click);
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
            this.btnReturn.Text = "退出";
            this.btnReturn.Click += new System.EventHandler(this.btnReturn_Click);
            // 
            // btnSelectEtPicking
            // 
            this.btnSelectEtPicking.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnSelectEtPicking.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnSelectEtPicking.ForeColor = System.Drawing.Color.Black;
            this.btnSelectEtPicking.Location = new System.Drawing.Point(3, 41);
            this.btnSelectEtPicking.Name = "btnSelectEtPicking";
            this.btnSelectEtPicking.Size = new System.Drawing.Size(236, 32);
            this.btnSelectEtPicking.TabIndex = 1;
            this.btnSelectEtPicking.Text = "2.ET Picking";
            this.btnSelectEtPicking.Click += new System.EventHandler(this.btnSelectEtPicking_Click);
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
            this.btnOff.Click += new System.EventHandler(this.btnOff_Click);
            // 
            // label1
            // 
            this.label1.Font = new System.Drawing.Font("Tahoma", 8F, System.Drawing.FontStyle.Regular);
            this.label1.Location = new System.Drawing.Point(3, 250);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(76, 15);
            this.label1.Text = "v1.180129.01";
            // 
            // btnSelectLogistic
            // 
            this.btnSelectLogistic.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnSelectLogistic.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnSelectLogistic.ForeColor = System.Drawing.Color.Black;
            this.btnSelectLogistic.Location = new System.Drawing.Point(3, 79);
            this.btnSelectLogistic.Name = "btnSelectLogistic";
            this.btnSelectLogistic.Size = new System.Drawing.Size(236, 32);
            this.btnSelectLogistic.TabIndex = 8;
            this.btnSelectLogistic.Text = "3.Logistic";
            this.btnSelectLogistic.Click += new System.EventHandler(this.btnSelectLogistic_Click);
            // 
            // SystemSelectForm
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.btnSelectLogistic);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.btnOff);
            this.Controls.Add(this.btnSelectEtPicking);
            this.Controls.Add(this.btnReturn);
            this.Controls.Add(this.btnSelectSmart);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "SystemSelectForm";
            this.Text = "系统选择";
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.MainMenuForm_KeyDown);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnSelectSmart;
        private System.Windows.Forms.Button btnReturn;
        private System.Windows.Forms.Button btnSelectEtPicking;
        private System.Windows.Forms.Button btnOff;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button btnSelectLogistic;
    }
}

