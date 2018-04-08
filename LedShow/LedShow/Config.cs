using System.Configuration;

namespace LedShow
{
    public static class Config
    {
        private static Configuration config = ConfigurationManager.OpenExeConfiguration(ConfigurationUserLevel.None);
        public static void Set(string name, string value)
        {
            config.AppSettings.Settings[name].Value = value;
            config.Save();
        }

        public static string Get(string name)
        {
            return config.AppSettings.Settings[name].Value;
        }

        public static string ErrorLogFile = "ErrorLog.txt";
        public static int Id = 1;

    }
}
