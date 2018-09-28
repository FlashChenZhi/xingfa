package com.master.service;

import com.util.common.ReturnObj;
import com.util.common.StringUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.WholeInStorageStrategy;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 10:46 2018/9/14
 * @Description:
 * @Modified By:
 */
@Service
public class WholeInStorageStrategyService {

    public ReturnObj<Map<String, Object>> getWholeInStorageStrategy(){
        ReturnObj<Map<String, Object>> s = new ReturnObj<>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            WholeInStorageStrategy wholeInStorageStrategy = WholeInStorageStrategy.getWholeInStorageStrategyById(1);
            Map<String, Object> map =new HashMap<>();
            if(wholeInStorageStrategy !=null){
                map.put("status", wholeInStorageStrategy.isStatus()?"1":"0");
            }else{
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("此全局策略不存在！");
                return s;
            }
            s.setSuccess(true);
            s.setRes(map);
            Transaction.commit();
        }catch (Exception e){
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return s;
    }


    /*
     * @author：ed_chen
     * @date：2018/8/10 11:08
     * @description：更新app连接状态
     * @param skuCode
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public ReturnObj<Map<String, Object>> updateWholeInStorageStrategy(String status){
        ReturnObj<Map<String, Object>> s = new ReturnObj<>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            WholeInStorageStrategy wholeInStorageStrategy = WholeInStorageStrategy.getWholeInStorageStrategyById(1);

            if(StringUtils.isNotEmpty(status)){
                if(wholeInStorageStrategy !=null){

                    wholeInStorageStrategy.setStatus("1".equals(status)?true:false);

                }else{
                    Transaction.rollback();
                    s.setSuccess(false);
                    s.setMsg("此连接不存在！");
                    return s;
                }
            }else{
                Transaction.rollback();
                s.setSuccess(false);
                s.setMsg("状态值为空！");
                return s;
            }
            Transaction.commit();
            s.setSuccess(true);
            s.setMsg("修改成功！");
        }catch (Exception e){
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return s;
    }
}