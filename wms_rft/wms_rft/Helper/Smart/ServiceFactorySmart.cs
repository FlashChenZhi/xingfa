using wms_rft.WmsRftSmart;

namespace wms_rft.Helper.Smart
{
    public class ServiceFactorySmart
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
