namespace wms_rft.Menu
{
    partial class EtMenuForm
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
            this.btnEtRecovery = new System.Windows.Forms.Button();
            this.btnReturn = new System.Windows.Forms.Button();
            this.btnEtBucketErrorMaintenence = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // btnEtRecovery
            // 
            this.btnEtRecovery.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnEtRecovery.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnEtRecovery.ForeColor = System.Drawing.Color.Black;
            this.btnEtRecovery.Location = new System.Drawing.Point(3, 3);
            this.btnEtRecovery.Name = "btnEtRecovery";
            this.btnEtRecovery.Size = new System.Drawing.Size(236, 32);
            this.btnEtRecovery.TabIndex = 0;
            this.btnEtRecovery.Text = "1.自动拣选装置异常恢复处理";
            this.btnEtRecovery.Click += new System.EventHandler(this.btnEtRecovery_Click);
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
            // btnEtBucketErrorMaintenence
            // 
            this.btnEtBucketErrorMaintenence.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnEtBucketErrorMaintenence.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnEtBucketErrorMaintenence.ForeColor = System.Drawing.Color.Black;
            this.btnEtBucketErrorMaintenence.Location = new System.Drawing.Point(3, 41);
            this.btnEtBucketErrorMaintenence.Name = "btnEtBucketErrorMaintenence";
            this.btnEtBucketErrorMaintenence.Size = new System.Drawing.Size(236, 32);
            this.btnEtBucketErrorMaintenence.TabIndex = 3;
            this.btnEtBucketErrorMaintenence.Text = "2.异常料箱处理";
            this.btnEtBucketErrorMaintenence.Click += new System.EventHandler(this.btnEtBucketErrorMaintenence_Click);
            // 
            // EtMenuForm
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.btnEtBucketErrorMaintenence);
            this.Controls.Add(this.btnReturn);
            this.Controls.Add(this.btnEtRecovery);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "EtMenuForm";
            this.Text = "自动拣选装置异常恢复 Menu";
            this.Load += new System.EventHandler(this.OtherMenuEtForm_Load);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.OtherMenuForm_KeyDown);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnEtRecovery;
        private System.Windows.Forms.Button btnReturn;
        private System.Windows.Forms.Button btnEtBucketErrorMaintenence;
    }
}

