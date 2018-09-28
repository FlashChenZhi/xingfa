import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.InStorageStrategy;
import com.wms.domain.Location;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;

import java.util.List;

/**
 * Created by Administrator on 2016/12/19.
 */
public class Test {
    public static void main(String[] args) {
        Transaction.begin();
        Query query =HibernateUtil.getCurrentSession().createQuery("select 1 from InStorageStrategy iss where iss.bayLevel=:bayLevel");
        query.setParameter("bayLevel", "40_1");
        List<InStorageStrategy> inStorageStrategyList =query.list();
        System.out.println(inStorageStrategyList);
         Transaction.commit();

    }

    public static void oneToEight(String area, int level) {
        //level
        for (int bay = 27; bay <= 27; bay++) {
            //bay
            for (int bank = 13; bank <= 22; bank++) {
                //bank
                String locationNo = area + org.apache.commons.lang.StringUtils.leftPad(bank + "", 2, '0')
                        + org.apache.commons.lang.StringUtils.leftPad(bay + "", 3, '0')
                        + org.apache.commons.lang.StringUtils.leftPad(level + "", 3, '0');

                Location location = new  Location();
                location.setLocationNo(locationNo);
                location.setEmpty(true);
                location.setReserved(false);
                location.setAsrsFlag(true);
                location.setRetrievalRestricted(false);
                location.setPosition(area);
                location.setBank(bank);
                location.setBay(bay);
                location.setLevel(level);
                location.setSeq(1);
                location.setAisle(1);
                location.setReserved(false);
                location.setCyclecounting(false);
                location.setCapacity(1);
                location.setAbnormal(false);
                location.setPutawayRestricted(false);
                location.setSystem(true);
                HibernateUtil.getCurrentSession().save(location);

            }
        }
    }


    //1到8排
    public static void oneToEight(String area) {
        for (int level = 1; level <= 4; level++) {
            //level
            for (int bay = 1; bay <= 27; bay++) {
                //bay
                for (int bank = 1; bank <= 11; bank++) {
                    //bank
                    String locationNo = area + org.apache.commons.lang.StringUtils.leftPad(bank + "", 2, '0')
                            + org.apache.commons.lang.StringUtils.leftPad(bay + "", 3, '0')
                            + org.apache.commons.lang.StringUtils.leftPad(level + "", 3, '0');

                    Location location = new Location();
                    location.setLocationNo(locationNo);
                    location.setBank(bank);
                    location.setBay(bay);
                    location.setLevel(level);
                    location.setSeq(1);
                    HibernateUtil.getCurrentSession().save(location);
                }
            }
        }
    }

    public void test(){

    }

}
