using System.Text.RegularExpressions;

namespace wms_rft.Helper
{
    public class CommonHelper
    {
        public static string substringBucketNoOrBagNo(string data)
        {
            return string.IsNullOrEmpty(data) 
                ? string.Empty 
                : data.Length > 7 
                ? data.Substring(0, 7) 
                : data;            
        }

        public static bool isBucketNo(string barcode)
        {
//            return barcode.StartsWith("71") || barcode.StartsWith("72");

//            return Regex.IsMatch(barcode, "^7[12][0-9A-Z]{5}|^6[1][0-9A-Z]{5}");
            return Regex.IsMatch(barcode, "^(7[12]|6[1])[0-9A-Z]{5}");
        }

        public static bool isBagNo(string barcode)
        {
//            return barcode.StartsWith("91") || barcode.StartsWith("92");

            return Regex.IsMatch(barcode, "^9[12][0-9A-Z]{5}");
        }

        public static string locationFormatter(string locationNo)
        {
            if (locationNo == null)
            {
                return string.Empty;
            }

            if (string.IsNullOrEmpty(locationNo))
            {
                return locationNo;
            }

            if (locationNo.Length != 10)
            {
                return locationNo;
            }

            return string.Format("{0}-{1}-{2}", locationNo.Substring(0, 4), locationNo.Substring(4, 3),
                                 locationNo.Substring(7, 3));
        }

        public static string formatTitle(string title, string systemCode)
        {
            return string.Format("{0}({1})", title, systemCode);
        }

        public static string itemCategoryToName(string itemCategory)
        {
            if (itemCategory == "1")
            {
                return "MF";
            }

            if (itemCategory == "2")
            {
                return "CF細物";
            }

            if (itemCategory == "3")
            {
                return "CF太物";
            }

            if (itemCategory == "4")
            {
                return "VF";
            }

            return "未知分类";
        }
    }
}
