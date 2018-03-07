package com.rft;


import com.rft.domain.FileInfo_RFT;
import com.rft.service.WmsRftService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceException;

/**
 * Created with IntelliJ IDEA.
 * User: Jack
 * Date: 13-3-8
 * Time:  H7:46
 * To change this template use File | Settings | File Templates.
 */
@WebService
public class WmsRft {
    private Logger logger = LogManager.getLogger("rftLog");

//    @WebMethod
//    public String sayHelloWorldFrom(String from) {
//        String result = "Hello, world, from " + from;
//        System.out.println(result);
//        return result;
//    }


    @WebMethod
    public FileInfo_RFT getFile(String fileName, long offset) {
        ReturnObj<FileInfo_RFT> retObj = null;

        WmsRftService service = new WmsRftService();
        try {
            retObj = service.getFile(fileName, offset);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);

            retObj = new ReturnObj<FileInfo_RFT>();
            retObj.setOk(false);
            retObj.setErrorMessage(ex.getMessage());
        }

        if (!retObj.isOk()) {
            throw new WebServiceException(retObj.getErrorMessage());
        }

        return retObj.getData();
    }

//    @WebMethod
//    public void getSkuList(){
//
//    }
}