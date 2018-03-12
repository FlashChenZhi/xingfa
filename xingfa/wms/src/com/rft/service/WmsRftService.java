package com.rft.service;

import com.master.service.PutInStorageService;
import com.master.vo.SkuVo2;
import com.rft.ReturnObj;
import com.rft.domain.FileInfo_RFT;
import com.util.common.BaseReturnObj;

import javax.jws.WebMethod;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Jack
 * Date: 13-4-10
 * Time: 下午3:26
 * To change this template use File | Settings | File Templates.
 */
public class WmsRftService {

    @WebMethod
    public ReturnObj<FileInfo_RFT> getFile(String fileName, long offset) throws Exception {
        ReturnObj<FileInfo_RFT> retObj = new ReturnObj<FileInfo_RFT>();
        BufferedInputStream bufferedInputStream = null;
        try {
            File file = new File("d:\\rft\\" + fileName);

            int buffLength = 10240;
            long remainLength = file.length() - offset;
            buffLength = (int) Math.min((long) buffLength, remainLength);
            byte[] binData = new byte[buffLength];
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            bufferedInputStream.skip(offset);
            bufferedInputStream.read(binData);
            bufferedInputStream.close();
            bufferedInputStream = null;

            FileInfo_RFT fileInfoRft = new FileInfo_RFT();
            fileInfoRft.setBinData(binData);
            fileInfoRft.setFileLength(file.length());

            retObj.setOk(true);
            retObj.setData(fileInfoRft);

        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return retObj;
    }

    @WebMethod
    public ReturnObj<Object> putaway(String palletNo, String stationNo, String skuCode, String lotNo,int qty) {
        ReturnObj<Object> returnObj = new ReturnObj<>();
        BaseReturnObj robj = new PutInStorageService().addTask(palletNo, stationNo, skuCode, lotNo,qty);
        returnObj.setOk(robj.isSuccess());
        returnObj.setErrorMessage(robj.getMsg());
        return returnObj;
    }

    public ReturnObj<List<SkuVo2>> getSkuList(){
        ReturnObj<List<SkuVo2>> returnObj = new ReturnObj<>();
        com.util.common.ReturnObj<List<SkuVo2>> robj = new PutInStorageService().getCommodityCode();
        returnObj.setOk(robj.isSuccess());
        returnObj.setErrorMessage(robj.getMsg());
        returnObj.setData(robj.getRes());
        return returnObj;
    }
}
