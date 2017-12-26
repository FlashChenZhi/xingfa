package com.wms.service;

import com.wms.domain.Station;
import com.wms.vo.ComboBoxVo;
import com.util.common.HttpMessage;
import com.util.common.StringUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */
@Service
public class CommonService {

    public HttpMessage searchStation(String type) {
        HttpMessage message = new HttpMessage();
        try {
            Transaction.begin();
            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Station.class);
            if(StringUtils.isNotEmpty(type))
                criteria.add(Restrictions.eq(Station.__MODEL,type));
            List<Station> stations = criteria.list();
            List<ComboBoxVo> vos = new ArrayList<>();
            ComboBoxVo boxVo;
            for(Station station : stations){
                boxVo = new ComboBoxVo(station.getStationNo(),station.getStationNo());
                vos.add(boxVo);
            }
            Transaction.commit();
            message.setSuccess(true);
            message.setMsg(vos);
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
            message.setSuccess(false);
        }
        return message;
    }
}
