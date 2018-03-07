using wms_rft.WmsRftEt;

namespace wms_rft.Helper.Et
{
    public class ServiceFactoryEt
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
