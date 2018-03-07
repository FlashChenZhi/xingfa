

using wms_rft.WmsRft;

namespace wms_rft.Helper
{
    public class ServiceFactory
    {
        private static WmsRftService service;

        public static WmsRftService getCurrentService()
        {
            if (service == null)
            {
                service = new WmsRftService();
            }

            return service;
        }
    }
}
