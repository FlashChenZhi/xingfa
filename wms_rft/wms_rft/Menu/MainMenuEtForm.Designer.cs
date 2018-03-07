namespace wms_rft.Menu
{
    partial class MainMenuEtForm
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
            this.btnStockInMenu = new System.Windows.Forms.Button();
            this.btnStockOutMenu = new System.Windows.Forms.Button();
            this.btnStockQueryMenu = new System.Windows.Forms.Button();
            this.btnOtherMenu = new System.Windows.Forms.Button();
            this.btnOff = new System.Windows.Forms.Button();
            this.btnEtMenu = new System.Windows.Forms.Button();
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
            this.btnReturn.TabIndex = 5;
            this.btnReturn.Text = "返回";
            this.btnReturn.Click += new System.EventHandler(this.btnReturn_Click);
            // 
            // btnStockInMenu
            // 
            this.btnStockInMenu.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnStockInMenu.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnStockInMenu.ForeColor = System.Drawing.Color.Black;
            this.btnStockInMenu.Location = new System.Drawing.Point(3, 3);
            this.btnStockInMenu.Name = "btnStockInMenu";
            this.btnStockInMenu.Size = new System.Drawing.Size(236, 32);
            this.btnStockInMenu.TabIndex = 0;
            this.btnStockInMenu.Text = "1.入库业务菜单";
            this.btnStockInMenu.Click += new System.EventHandler(this.btnStockInMenu_Click);
            // 
            // btnStockOutMenu
            // 
            this.btnStockOutMenu.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnStockOutMenu.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnStockOutMenu.ForeColor = System.Drawing.Color.Black;
            this.btnStockOutMenu.Location = new System.Drawing.Point(3, 41);
            this.btnStockOutMenu.Name = "btnStockOutMenu";
            this.btnStockOutMenu.Size = new System.Drawing.Size(236, 32);
            this.btnStockOutMenu.TabIndex = 1;
            this.btnStockOutMenu.Text = "2.出库业务菜单";
            this.btnStockOutMenu.Click += new System.EventHandler(this.btnStockOutMenu_Click);
            // 
            // btnStockQueryMenu
            // 
            this.btnStockQueryMenu.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnStockQueryMenu.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnStockQueryMenu.ForeColor = System.Drawing.Color.Black;
            this.btnStockQueryMenu.Location = new System.Drawing.Point(3, 79);
            this.btnStockQueryMenu.Name = "btnStockQueryMenu";
            this.btnStockQueryMenu.Size = new System.Drawing.Size(236, 32);
            this.btnStockQueryMenu.TabIndex = 2;
            this.btnStockQueryMenu.Text = "3.查询业务菜单";
            this.btnStockQueryMenu.Click += new System.EventHandler(this.btnStockQueryMenu_Click);
            // 
            // btnOtherMenu
            // 
            this.btnOtherMenu.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnOtherMenu.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnOtherMenu.ForeColor = System.Drawing.Color.Black;
            this.btnOtherMenu.Location = new System.Drawing.Point(3, 117);
            this.btnOtherMenu.Name = "btnOtherMenu";
            this.btnOtherMenu.Size = new System.Drawing.Size(236, 32);
            this.btnOtherMenu.TabIndex = 3;
            this.btnOtherMenu.Text = "4.其他业务菜单";
            this.btnOtherMenu.Click += new System.EventHandler(this.btnOtherMenu_Click);
            // 
            // btnOff
            // 
            this.btnOff.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnOff.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnOff.ForeColor = System.Drawing.Color.Black;
            this.btnOff.Location = new System.Drawing.Point(163, 246);
            this.btnOff.Name = "btnOff";
            this.btnOff.Size = new System.Drawing.Size(72, 22);
            this.btnOff.TabIndex = 6;
            this.btnOff.Text = "关机";
            this.btnOff.Visible = false;
            this.btnOff.Click += new System.EventHandler(this.btnOff_Click);
            // 
            // btnEtMenu
            // 
            this.btnEtMenu.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(252)))), ((int)(((byte)(136)))), ((int)(((byte)(0)))));
            this.btnEtMenu.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Bold);
            this.btnEtMenu.ForeColor = System.Drawing.Color.Black;
            this.btnEtMenu.Location = new System.Drawing.Point(3, 155);
            this.btnEtMenu.Name = "btnEtMenu";
            this.btnEtMenu.Size = new System.Drawing.Size(236, 32);
            this.btnEtMenu.TabIndex = 4;
            this.btnEtMenu.Text = "5.自动拣选装置异常恢复菜单";
            this.btnEtMenu.Click += new System.EventHandler(this.btnEtMenu_Click);
            // 
            // MainMenuEtForm
            // 
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Inherit;
            this.AutoScroll = true;
            this.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(218)))), ((int)(((byte)(217)))), ((int)(((byte)(238)))));
            this.ClientSize = new System.Drawing.Size(242, 271);
            this.ControlBox = false;
            this.Controls.Add(this.btnEtMenu);
            this.Controls.Add(this.btnOff);
            this.Controls.Add(this.btnStockQueryMenu);
            this.Controls.Add(this.btnOtherMenu);
            this.Controls.Add(this.btnStockOutMenu);
            this.Controls.Add(this.btnStockInMenu);
            this.Controls.Add(this.btnReturn);
            this.KeyPreview = true;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "MainMenuEtForm";
            this.Text = "主菜单";
            this.Load += new System.EventHandler(this.MainMenuEtForm_Load);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.MainMenuForm_KeyDown);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button btnReturn;
        private System.Windows.Forms.Button btnStockInMenu;
        private System.Windows.Forms.Button btnStockOutMenu;
        private System.Windows.Forms.Button btnStockQueryMenu;
        private System.Windows.Forms.Button btnOtherMenu;
        private System.Windows.Forms.Button btnOff;
        private System.Windows.Forms.Button btnEtMenu;
    }
}

