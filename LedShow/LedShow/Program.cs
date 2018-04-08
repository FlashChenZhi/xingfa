using System;
using System.Collections.Generic;
using System.IO;
using System.Threading;
using System.Windows.Forms;

namespace LedShow
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main(string[] args)
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);

//            IList<string> ledInfo = new List<string>();
//            StreamReader sr = null;
//            try
//            {
//                sr = new StreamReader("led.properties");
//                while(!sr.EndOfStream)
//                {
//                    ledInfo.Add(sr.ReadLine());                   
//                }
//
//            }
//            catch (Exception ex)
//            {
//                Console.WriteLine(ex.Message);
//                return;
//            }
//            finally
//            {
//                if (sr != null)
//                {
//                    sr.Close();
//                }
//            }
//
//            for (int i = 0; i < ledInfo.Count; i++ )
//            {
//                string[] tokens = ledInfo[i].Split(' ');
//                if (tokens.Length < 2)
//                {
//                    continue;
//                }              
//            }

            if (args.Length > 1)
            {
                Application.Run(new LedFrm(args[0], args[1]));
            }
            else if (args.Length == 1)
            {
                Application.Run(new LedFrm(args[0]));
            }
            else
            {
                Application.Run(new LedFrm("1207"));
            }
            //Application.Run(new TestFrm());
        }
    }
}