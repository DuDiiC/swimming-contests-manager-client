package dbUtils;

import dbModels.Competition;
import dbModels.Competitor;
import dbModels.Record;
import fxUtils.DialogsUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class HibernateUtilRecord {

    private static EntityManager em = HibernateUtil.getEm();

    public static boolean addRecord(Record record) {
        List<Record> rList = HibernateUtilCompetitor.getAllRecords(record.getCompetitor());
        for(Record r : rList) {
            if(r.getCompetition() == record.getCompetition() && r.compareTo(record) < 0) {
                return false;
            }
        }
        em.getTransaction().begin();
        em.persist(record);
        em.getTransaction().commit();
        return true;
    }

    public static void removeRecord(Record record) {
        em.getTransaction().begin();
        em.remove(em.contains(record) ? record : em.merge(record));
        em.getTransaction().commit();
    }
}
