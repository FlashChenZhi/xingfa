package com.master.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.util.common.BaseReturnObj;
import com.util.common.LogMessage;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PutInStorageService {
    /**
     * 获取商品代码
     * @return
     * @throws IOException
     */
    public ReturnObj<List<Map<String,String>>> getCommodityCode() throws IOException{
        System.out.println("进入获取商品代码方法！");

        ReturnObj<List<Map<String,String>>> s = new ReturnObj<List<Map<String,String>>>();
        List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
        for (int i = 0; i <10 ; i++) {
            Map<String,String> map = new HashMap<String,String>();
            map.put("id",i + "");
            map.put("name","商品代码"+i);
            mapList.add(map);
        }
        s.setSuccess(true);
        s.setRes(mapList);
        return s;
    }
    /**
     * 设定任务
     * @param tuopanhao 托盘号
     * @param zhantai 站台
     * @param commodityCode 货品代码
     * @param num 数量
     * @return "0"设定成功，"1"设定失败
     * @throws IOException
     */
    @RequestMapping(value = "/addTask",method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj addTask(String tuopanhao, String zhantai, String commodityCode, int num) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Sku sku = Sku.getByCode(commodityCode);
            if (sku == null) {
                returnObj.setSuccess(false);
                returnObj.setMsg("商品不存在!");
                Transaction.rollback();
                return returnObj;
            }
            Container container = Container.getByBarcode(tuopanhao);
            if(container != null){
                returnObj.setSuccess(false);
                returnObj.setMsg("托盘号已存在!");
                Transaction.rollback();
                return returnObj;
            }
            Query query=HibernateUtil.getCurrentSession().createQuery("from InventoryView iv where iv.palletCode = :palletCode");
            InventoryView inventoryView= (InventoryView) query.uniqueResult();
            if( inventoryView.getPalletCode()!=null){
               returnObj.setSuccess(false);
               returnObj.setMsg("托盘号已存在");
               Transaction.rollback();
               return returnObj;
           }
            Job job=new Job();
            session.save(job);
            job.setFromStation(zhantai);
            job.setContainer(tuopanhao);
            job.setSendReport(false);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            job.setCreateDate(sdf.parse(sdf.format(new Date())));
            if(zhantai.equals("1101")){
                job.setToStation("ML01");
            }
            if(zhantai.equals("1301")){
                job.setToStation("ML02");
            }
            job.setType(AsrsJobType.PUTAWAY);
            job.setMcKey(Mckey.getNext());
            job.setStatus("1");

            JobDetail jobDetail = new JobDetail();
            session.save(jobDetail);
            jobDetail.setJob(job);
            jobDetail.setQty(new BigDecimal(num));

            inventoryView = new InventoryView();
            inventoryView.setPalletCode(tuopanhao);
            inventoryView.setQty(new BigDecimal(num));
            inventoryView.setSkuCode(commodityCode);

            inventoryView.setSkuName(sku.getSkuName());


            returnObj.setSuccess(true);
            Transaction.commit();
        }catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return returnObj;

    }

    }



