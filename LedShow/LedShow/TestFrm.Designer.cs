namespace LedShow
{
    partial class TestFrm
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
            this.button1 = new System.Windows.Forms.Button();
            this.button2 = new System.Windows.Forms.Button();
            this.button3 = new System.Windows.Forms.Button();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.button4 = new System.Windows.Forms.Button();
            this.button5 = new System.Windows.Forms.Button();
            this.speedBox = new System.Windows.Forms.NumericUpDown();
            this.label20 = new System.Windows.Forms.Label();
            this.timeBox = new System.Windows.Forms.NumericUpDown();
            this.label2 = new System.Windows.Forms.Label();
            this.methodBox = new System.Windows.Forms.NumericUpDown();
            this.label3 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.speedBox)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.timeBox)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.methodBox)).BeginInit();
            this.SuspendLayout();
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(35, 95);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(108, 48);
            this.button1.TabIndex = 0;
            this.button1.Text = "打开电源";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // button2
            // 
            this.button2.Location = new System.Drawing.Point(191, 95);
            this.button2.Name = "button2";
            this.button2.Size = new System.Drawing.Size(108, 48);
            this.button2.TabIndex = 0;
            this.button2.Text = "关闭电源";
            this.button2.UseVisualStyleBackColor = true;
            this.button2.Click += new System.EventHandler(this.button2_Click);
            // 
            // button3
            // 
            this.button3.Location = new System.Drawing.Point(35, 30);
            this.button3.Name = "button3";
            this.button3.Size = new System.Drawing.Size(108, 48);
            this.button3.TabIndex = 0;
            this.button3.Text = "打开端口";
            this.button3.UseVisualStyleBackColor = true;
            this.button3.Click += new System.EventHandler(this.button3_Click);
            // 
            // textBox1
            // 
            this.textBox1.Location = new System.Drawing.Point(35, 230);
            this.textBox1.Multiline = true;
            this.textBox1.Name = "textBox1";
            this.textBox1.Size = new System.Drawing.Size(250, 54);
            this.textBox1.TabIndex = 1;
            this.textBox1.Text = "那天是週末，春日的黃昏有新榨橙汁的顏色與氣息。老早說好了要和朋友們去逛夜市，母親卻在下班的時候打來了電話，聲音裡是小女孩一般的歡欣雀躍：「明天我們單位組織去春遊";
            // 
            // button4
            // 
            this.button4.Location = new System.Drawing.Point(35, 299);
            this.button4.Name = "button4";
            this.button4.Size = new System.Drawing.Size(108, 48);
            this.button4.TabIndex = 0;
            this.button4.Text = "发送";
            this.button4.UseVisualStyleBackColor = true;
            this.button4.Click += new System.EventHandler(this.button4_Click);
            // 
            // button5
            // 
            this.button5.Location = new System.Drawing.Point(191, 30);
            this.button5.Name = "button5";
            this.button5.Size = new System.Drawing.Size(108, 48);
            this.button5.TabIndex = 0;
            this.button5.Text = "查询状态";
            this.button5.UseVisualStyleBackColor = true;
            this.button5.Click += new System.EventHandler(this.button5_Click);
            // 
            // speedBox
            // 
            this.speedBox.Location = new System.Drawing.Point(92, 203);
            this.speedBox.Name = "speedBox";
            this.speedBox.Size = new System.Drawing.Size(51, 21);
            this.speedBox.TabIndex = 2;
            this.speedBox.Value = new decimal(new int[] {
            1,
            0,
            0,
            0});
            // 
            // label20
            // 
            this.label20.AutoSize = true;
            this.label20.Location = new System.Drawing.Point(33, 205);
            this.label20.Name = "label20";
            this.label20.Size = new System.Drawing.Size(29, 12);
            this.label20.TabIndex = 3;
            this.label20.Text = "速度";
            // 
            // timeBox
            // 
            this.timeBox.Location = new System.Drawing.Point(92, 176);
            this.timeBox.Name = "timeBox";
            this.timeBox.Size = new System.Drawing.Size(51, 21);
            this.timeBox.TabIndex = 2;
            this.timeBox.Value = new decimal(new int[] {
            2,
            0,
            0,
            0});
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(33, 178);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(53, 12);
            this.label2.TabIndex = 3;
            this.label2.Text = "停留时间";
            // 
            // methodBox
            // 
            this.methodBox.Location = new System.Drawing.Point(206, 176);
            this.methodBox.Name = "methodBox";
            this.methodBox.Size = new System.Drawing.Size(55, 21);
            this.methodBox.TabIndex = 2;
            this.methodBox.Value = new decimal(new int[] {
            17,
            0,
            0,
            0});
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(171, 178);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(29, 12);
            this.label3.TabIndex = 3;
            this.label3.Text = "方式";
            // 
            // TestFrm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(334, 389);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label20);
            this.Controls.Add(this.timeBox);
            this.Controls.Add(this.methodBox);
            this.Controls.Add(this.speedBox);
            this.Controls.Add(this.textBox1);
            this.Controls.Add(this.button2);
            this.Controls.Add(this.button4);
            this.Controls.Add(this.button5);
            this.Controls.Add(this.button3);
            this.Controls.Add(this.button1);
            this.Name = "TestFrm";
            this.Text = "TestFrm";
            this.Load += new System.EventHandler(this.TestFrm_Load);
            ((System.ComponentModel.ISupportInitialize)(this.speedBox)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.timeBox)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.methodBox)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button button2;
        private System.Windows.Forms.Button button3;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.Button button4;
        private System.Windows.Forms.Button button5;
        private System.Windows.Forms.NumericUpDown speedBox;
        private System.Windows.Forms.Label label20;
        private System.Windows.Forms.NumericUpDown timeBox;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.NumericUpDown methodBox;
        private System.Windows.Forms.Label label3;
    }
}