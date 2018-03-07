using System;
using System.IO;
using System.Reflection;
using Microsoft.Win32;
using wms_updater.WmsRft;

namespace wms_updater
{
    class Program
    {
        static void Main()
        {
            byte[] ipaddr = new byte[]{192,168,10,13};
            RegistryKey key = Registry.LocalMachine.CreateSubKey("Comm").CreateSubKey("Tcpip").CreateSubKey("Hosts").CreateSubKey("webservice-server");
            key.SetValue("ipaddr", ipaddr, RegistryValueKind.Binary);

//            Object aaa = Registry.GetValue("HKEY_LOCAL_MACHINE\\Comm\\Tcpip\\Hosts\\webservice-server", "ipaddr", null);

            String appDirPath = Path.GetDirectoryName(Assembly.GetExecutingAssembly().GetName().CodeBase).Replace(@"file:\", string.Empty);
            const string tempDirName = "temp";
            string tempDirPath = Path.Combine(appDirPath, tempDirName);
            const string fileListFileName = "filelist.txt";
            string fileListFilePath = Path.Combine(tempDirPath, fileListFileName);

            StreamReader sr = null;

            try
            {
                string[] tempFileNames;
                if (Directory.Exists(tempDirPath))
                {
                    tempFileNames = Directory.GetFiles(tempDirPath);
                    foreach (string tempFileName in tempFileNames)
                    {
                        File.Delete(tempFileName);
                    }
                }
                else
                {
                    Directory.CreateDirectory(tempDirPath);
                }

                if (!downloadFile(fileListFileName, tempDirPath))
                {
                    Console.WriteLine("failed to download file '{0}'", fileListFileName);
                    Console.ReadLine();
                    return;
                }

                sr = new StreamReader(File.OpenRead(fileListFilePath));
                while (!sr.EndOfStream)
                {
                    if (!downloadFile(sr.ReadLine(), tempDirPath))
                    {
                        Console.WriteLine("failed to download file '{0}'", fileListFileName);
                        Console.ReadLine();
                        return;
                    }
                    ;
                }

                sr.Close();
                sr = null;

                try
                {
                    File.Delete(fileListFilePath);
                }
                catch
                {
                    //ignore                    
                }

                tempFileNames = Directory.GetFiles(tempDirPath);
                foreach (string tempFileName in tempFileNames)
                {
                    string fileName = Path.GetFileName(tempFileName);
                    string filePath = Path.Combine(appDirPath, fileName);
                    if (File.Exists(filePath))
                    {
                        File.Delete(filePath);
                    }
                }

                foreach (string tempFileName in tempFileNames)
                {
                    string fileName = Path.GetFileName(tempFileName);
                    string filePath = Path.Combine(appDirPath, fileName);
                    File.Move(tempFileName, filePath);
                }

                try
                {
                    Directory.Delete(tempDirPath);
                }
                catch
                {

                    //ingnore
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                Console.ReadLine();
            }
            finally
            {
                if (sr != null)
                {
                    sr.Close();
                }
            }

        }

        static bool downloadFile(string fileName, string toDirPath)
        {
            WmsRftService service = new WmsRftService(); ;
            FileStream fs = null;
            try
            {
                int rcvLength = 0;
                long fileLength;
                byte[] binData = null;
                do
                {
                    fileInfoRFT fileInfoRft = service.getFile(fileName, rcvLength);
                    fileLength = fileInfoRft.fileLength;

                    if (binData == null)
                    {
                        binData = new byte[fileLength];
                    }

                    Array.Copy(fileInfoRft.binData, 0, binData, rcvLength, fileInfoRft.binData.Length);                    

                    rcvLength += fileInfoRft.binData.Length;
                } while (rcvLength < fileLength);

                if (binData != null)
                {
                    fs = new FileStream(Path.Combine(toDirPath, fileName), FileMode.Create);
                    fs.Write(binData, 0, binData.Length);
                    fs.Close();
                    fs = null;
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return false;
            }
            finally
            {
                if (fs != null)
                {
                    fs.Close();
                }
            }

            return true;
        }
    }
}
