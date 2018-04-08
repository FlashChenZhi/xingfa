using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Windows.Forms;

namespace LedShow
{
    public class CommonFuncs
    {
        public static void WriteLog(string log, string file)
        {
            try
            {
                StreamWriter stream = new StreamWriter(file, true, System.Text.Encoding.Default);
                stream.WriteLine(DateTime.Now.ToString() + "  " + log);
                stream.Flush();
                stream.Close();
            }
            catch
            {
            }
        }

        public static void WriteLog(string log)
        {
            try
            {
                StreamWriter stream = new StreamWriter(DateTime.Now.ToString("yyyyMMdd") + ".log", true, System.Text.Encoding.Default);
                stream.WriteLine(DateTime.Now.ToString() + "  " + log);
                stream.Flush();
                stream.Close();
            }
            catch
            {
            }
        }

        public static bool ShowConfirmBox(string message, string title)
        {
            if (MessageBox.Show(message, title, MessageBoxButtons.YesNo, MessageBoxIcon.Question, MessageBoxDefaultButton.Button2) == DialogResult.No)
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        public static void ShowErrorBox(string message)
        {
            MessageBox.Show(message, "´íÎó", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }

        public static void ShowWarningBox(string message)
        {
            MessageBox.Show(message, "¾¯¸æ", MessageBoxButtons.OK, MessageBoxIcon.Warning);
        }

        public static void ShowSuccessBox(string message)
        {
            MessageBox.Show(message, "³É¹¦", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }

        public static void Run(string filename)
        {
            System.Diagnostics.Process proc = new System.Diagnostics.Process();
            proc.EnableRaisingEvents = false;
            proc.StartInfo.FileName = filename;
            proc.Start();
        }

        //public static  void InitCombo(ComboBox combo, ComboItem[] items)
        //{
        //    combo.Items.Clear();
        //    combo.Items.AddRange(items);
        //    //if (combo.Items.Count > 0)
        //    //{
        //    //    combo.SelectedIndex = 0;
        //    //}
        //}

        public static string GetUpper(decimal lower)
        {
            string UNIT = "·Ö½ÇÔªÊ°°ÛÇªÍòÊ°°ÛÇªÒÚÊ°°ÛÇªÍò"; 
            string UPPER = "ÁãÒ¼·¡ÈşËÁÎéÂ½Æâ°Æ¾Á"; 
            string stResult = "", stLower = "";

            stLower = lower.ToString("0.00").Replace(".", "");
            if (stLower.Length <= UNIT.Length)
            {
                for (int i = 0, j = stLower.Length - 1; i <= j; i++)
                {
                    stResult += UPPER[stLower[i] - 48].ToString() + UNIT[j - i].ToString();
                }
            }
            else
            {
                stResult = "#E";
            }

            return stResult;
        } 
    }
}
