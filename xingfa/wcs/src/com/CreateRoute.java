package com;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.Route;
import com.asrs.domain.RouteDetail;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/6/8.
 */
public class CreateRoute {
    /**
     * 0010会变成00010，要特别处理;若要改动，需要把  “000”+  改成4位左补0.
     *
     * @param args
     */
    public static void main(String[] args) {

        Transaction.begin();
        Session session = HibernateUtil.getCurrentSession();
        Route route = new Route();
        session.save(route);
        route.setFromStation("ML01");
        route.setStatus("1");
        route.setToStation("ML02");
        route.setType("08");
        List<String> routes = new ArrayList<>();
        routes.add("ML01");
        routes.add("ML02");

        for(int i = 0;i < routes.size() - 1; i++){
            RouteDetail routeDetail = new RouteDetail();
            session.save(routeDetail);
            routeDetail.setCurrentBlockNo(routes.get(i));
            routeDetail.setNextBlockNo(routes.get(i+1));
            routeDetail.setRoute(route);
        }

        Transaction.commit();
    }

    /**
     * 入出库路径
     *
     * @param from
     * @param to
     */
    public static void createRoute(String from, String to) {
        Session session = HibernateUtil.getCurrentSession();
        Route route = new Route();
        route.setFromStation(from);
        route.setStatus("1");
        route.setToStation(to);
        if (from.startsWith("0")) {
            route.setType("01");
            int start = Integer.parseInt(from.substring(from.length() - 1));
            for (int i = 0; i < 3; i++) {
                RouteDetail detail = new RouteDetail();
                detail.setCurrentBlockNo("000" + start);
                detail.setNextBlockNo("000" + ++start);
                detail.setRoute(route);
                session.save(detail);
            }
            int end = Integer.parseInt(from.substring(from.length() - 1));
            if (end > 4) {
                RouteDetail detail = new RouteDetail();
                detail.setCurrentBlockNo("000" + start);
                detail.setNextBlockNo("MC0" + (end - 4));
                detail.setRoute(route);
                session.save(detail);
                RouteDetail detail1 = new RouteDetail();
                detail1.setCurrentBlockNo("MC0" + (end - 4));
                detail1.setNextBlockNo(to);
                detail1.setRoute(route);
                session.save(detail1);
            } else {
                RouteDetail detail = new RouteDetail();
                detail.setCurrentBlockNo("000" + start);
                detail.setNextBlockNo(to);
                detail.setRoute(route);
                session.save(detail);
            }

        } else {
            route.setType("03");
            int end = Integer.parseInt(to.substring(to.length() - 1));
            for (int i = 0; i < 3; i++) {
                RouteDetail detail = new RouteDetail();
                detail.setNextBlockNo("000" + end);
                detail.setCurrentBlockNo("000" + ++end);
                detail.setRoute(route);
                session.save(detail);
            }
            int start = Integer.parseInt(from.substring(from.length() - 1));
            if (start > 4) {
                RouteDetail detail = new RouteDetail();
                detail.setCurrentBlockNo(from);
                detail.setNextBlockNo("MC0" + (start - 4));
                detail.setRoute(route);
                session.save(detail);
                RouteDetail detail1 = new RouteDetail();
                detail1.setCurrentBlockNo("MC0" + (start - 4));
                detail1.setNextBlockNo("000" + end);
                detail1.setRoute(route);
                session.save(detail1);
            } else {
                RouteDetail detail = new RouteDetail();
                detail.setCurrentBlockNo(from);
                detail.setNextBlockNo("000" + end);
                detail.setRoute(route);
                session.save(detail);
            }
        }
        session.save(route);
    }

    /**
     * 创建换层路径
     */
    public static void createChangeLevel(int fromLevel, int toLevel) {
        Session session = HibernateUtil.getCurrentSession();
        Route route = new Route();
        route.setFromStation("MC0" + fromLevel);
        route.setToStation("MC0" + (toLevel + 4));
        route.setStatus("1");
        route.setType(AsrsJobType.CHANGELEVEL);
        session.save(route);
        RouteDetail detail = new RouteDetail();
        detail.setCurrentBlockNo("MC0" + fromLevel);
        detail.setNextBlockNo("MC0" + (fromLevel + 4));
        detail.setRoute(route);
        session.save(detail);
        RouteDetail detail1 = new RouteDetail();
        detail1.setCurrentBlockNo("MC0" + (fromLevel + 4));
        detail1.setNextBlockNo("0011");
        detail1.setRoute(route);
        session.save(detail1);
        RouteDetail detail2 = new RouteDetail();
        detail2.setCurrentBlockNo("0011");
        detail2.setNextBlockNo("MC0" + (toLevel + 4));
        detail2.setRoute(route);
        session.save(detail2);

        Route route1 = new Route();
        route1.setFromStation("MC0" + (fromLevel + 4));
        route1.setToStation("MC0" + (toLevel + 4));
        route1.setStatus("1");
        route1.setType(AsrsJobType.CHANGELEVEL);
        session.save(route1);
        RouteDetail detail3 = new RouteDetail();
        detail3.setCurrentBlockNo("MC0" + (fromLevel + 4));
        detail3.setNextBlockNo("0011");
        detail3.setRoute(route1);
        session.save(detail3);
        RouteDetail detail4 = new RouteDetail();
        detail4.setCurrentBlockNo("0011");
        detail4.setNextBlockNo("MC0" + (toLevel + 4));
        detail4.setRoute(route1);
        session.save(detail4);
    }

}
