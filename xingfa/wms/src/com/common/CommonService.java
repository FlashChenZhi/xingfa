package com.common;


import com.util.common.ComboBoxVo;
import com.util.common.LogMessage;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CommonService {


    /**
     * 获取商品列表
     *
     * @return
     */

    public ReturnObj<List<ComboBoxVo>> getSkuCodes() {
        ReturnObj<List<ComboBoxVo>> returnObj = new ReturnObj<List<ComboBoxVo>>();
        try {
            Session session = HibernateUtil.getCurrentSession();

            Transaction.begin();

            Criteria c = session.createCriteria(SkuView.class);
            c.addOrder(Order.asc(SkuView.COL_CODE));
            List<SkuView> list = c.list();

            List<ComboBoxVo> datas = new ArrayList<ComboBoxVo>();
            for (SkuView sku : list) {
                datas.add(new ComboBoxVo(String.valueOf(sku.getSkuCode()), String.format("%s:%s", sku.getSkuCode(), sku.getSkuName())));
            }

            returnObj.setSuccess(true);
            returnObj.setRes(datas);

            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            ex.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

        }
        return returnObj;
    }





    /**
     * 获取角色
     *
     * @return
     */
    public ReturnObj<List<ComboBoxVo>> getRoleIds() {
        ReturnObj<List<ComboBoxVo>> returnObj = new ReturnObj<List<ComboBoxVo>>();
        try {
            Session session = HibernateUtil.getCurrentSession();

            Transaction.begin();

            Criteria c = session.createCriteria(Role.class);

            c.addOrder(Order.asc(Role.COL_ID));
            List<Role> list = c.list();

            List<ComboBoxVo> datas = new ArrayList<ComboBoxVo>();
            for (Role role : list) {
                datas.add(new ComboBoxVo(String.valueOf(role.getId()), role.getName()));
            }

            returnObj.setSuccess(true);
            returnObj.setRes(datas);

            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            ex.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

        }
        return returnObj;
    }

    /**
     * 获取一级菜单
     *
     * @return
     */
    public ReturnObj<List<ComboBoxVo>> getFirstMenuIds() {
        ReturnObj<List<ComboBoxVo>> returnObj = new ReturnObj<List<ComboBoxVo>>();
        try {
            Session session = HibernateUtil.getCurrentSession();

            Transaction.begin();

            Criteria c = session.createCriteria(Menu.class);
            c.add(Restrictions.eq(Menu.COL_TYPE, Menu.TYPE_FIRST_MENU));
            c.addOrder(Order.asc(Menu.COL_ID));
            List<Menu> list = c.list();

            List<ComboBoxVo> datas = new ArrayList<ComboBoxVo>();
            for (Menu menu : list) {
                datas.add(new ComboBoxVo(String.valueOf(menu.getId()), String.format("%s:%s", menu.getDistStr(), menu.getName())));
            }

            returnObj.setSuccess(true);
            returnObj.setRes(datas);

            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            ex.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

        }
        return returnObj;
    }

    public ReturnObj<List<ComboBoxVo>> getUser() {

        ReturnObj<List<ComboBoxVo>> returnObj = new ReturnObj<List<ComboBoxVo>>();
        try {
            Session session = HibernateUtil.getCurrentSession();

            Transaction.begin();

            Criteria c = session.createCriteria(User.class);
            c.addOrder(Order.asc(User.COL_CODE));
            List<User> list = c.list();

            List<ComboBoxVo> datas = new ArrayList<ComboBoxVo>();
            for (User user : list) {
                datas.add(new ComboBoxVo(user.getCode(), user.getName()));
            }

            returnObj.setSuccess(true);
            returnObj.setRes(datas);

            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            ex.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

        }
        return returnObj;
    }



}
