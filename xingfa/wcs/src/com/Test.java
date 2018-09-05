package com;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.communication.MessageProxy;
import com.asrs.domain.Location;
import com.asrs.domain.TransportOrderLog;
import com.asrs.message.Message03;
import com.asrs.message.Message40;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.utils.MsgSender;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
        try {
            try {
                Transaction.begin();
                int i = 1/0;
                Transaction.commit();
            }catch (Exception e){

                System.out.println("nei catch");

            }
        }catch (Exception e){
            Transaction.rollback();
            System.out.println("wai catch");
            e.printStackTrace();
        }


    }
}
