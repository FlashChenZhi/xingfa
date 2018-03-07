namespace wms_rft.Menu
{
    partial class OtherMenuEtForm
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
            this.btnPalletMove = new System.Windows.Forms.Button();
            this.btnReturn = new System.Windows.Forms.Button();
            this.btnBucketDelete = new System.Windows.Forms.Button();
            this.btnBucketOrBagChange = new System.Windows.Forms.Button();
            this.btnPalletBucketBinding = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // btnPalletMove
            // 
            this.btnPalletMove.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnPalletMove.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnPalletMove.ForeColor = System.Drawing.Color.Black;
            this.btnPalletMove.Location = new System.Drawing.Point(3, 3);
            this.btnPalletMove.Name = "btnPalletMove";
            this.btnPalletMove.Size = new System.Drawing.Size(236, 32);
            this.btnPalletMove.TabIndex = 0;
            this.btnPalletMove.Text = "1.托盘移动作业";
            this.btnPalletMove.Click += new System.EventHandler(this.btnPalletMove_Click);
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
            // btnBucketDelete
            // 
            this.btnBucketDelete.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnBucketDelete.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnBucketDelete.ForeColor = System.Drawing.Color.Black;
            this.btnBucketDelete.Location = new System.Drawing.Point(3, 41);
            this.btnBucketDelete.Name = "btnBucketDelete";
            this.btnBucketDelete.Size = new System.Drawing.Size(236, 32);
            this.btnBucketDelete.TabIndex = 3;
            this.btnBucketDelete.Text = "2.空料箱设定";
            this.btnBucketDelete.Click += new System.EventHandler(this.btnBucketDelete_Click);
            // 
            // btnBucketOrBagChange
            // 
            this.btnBucketOrBagChange.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnBucketOrBagChange.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnBucketOrBagChange.ForeColor = System.Drawing.Color.Black;
            this.btnBucketOrBagChange.Location = new System.Drawing.Point(3, 79);
            this.btnBucketOrBagChange.Name = "btnBucketOrBagChange";
            this.btnBucketOrBagChange.Size = new System.Drawing.Size(236, 32);
            this.btnBucketOrBagChange.TabIndex = 5;
            this.btnBucketOrBagChange.Text = "3.料箱/塑料袋更换作业";
            this.btnBucketOrBagChange.Click += new System.EventHandler(this.btnBucketOrBagChange_Click);
            // 
            // btnPalletBucketBinding
            // 
            this.btnPalletBucketBinding.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnPalletBucketBinding.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnPalletBucketBinding.ForeColor = System.Drawing.Color.Black;
            this.btnPalletBucketBinding.Location = new System.Drawing.Point(3, 117);
            this.btnPalletBucketBinding.Name = "btnPalletBucketBinding";
            this.btnPalletBucketBinding.Size = new System.Drawing.Size(236, 32);
            this.btnPalletBucketBinding.TabIndex = 7;
            this.btnPalletBucketBinding.Text = "4.料箱-托盘关联作业";
            this.btnPalletBucketBinding.Click += new System.EventHandler(this.btnPalletBucketBinding_Click);
            // 
            // OtherMenuEtForm
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.btnPalletBucketBinding);
            this.Controls.Add(this.btnBucketOrBagChange);
            this.Controls.Add(this.btnBucketDelete);
            this.Controls.Add(this.btnReturn);
            this.Controls.Add(this.btnPalletMove);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "OtherMenuEtForm";
            this.Text = "其他业务 Menu";
            this.Load += new System.EventHandler(this.OtherMenuEtForm_Load);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.OtherMenuForm_KeyDown);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnPalletMove;
        private System.Windows.Forms.Button btnReturn;
        private System.Windows.Forms.Button btnBucketDelete;
        private System.Windows.Forms.Button btnBucketOrBagChange;
        private System.Windows.Forms.Button btnPalletBucketBinding;
    }
}

