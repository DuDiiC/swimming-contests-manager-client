package dbUtils;

import dbModels.Record;

import javax.persistence.EntityManager;

public class HibernateUtilRecord {

    private static EntityManager em = HibernateUtil.getEm();

    public static void addRecord(Record record) {
        em.getTransaction().begin();
        em.persist(record);
        em.getTransaction().commit();
    }

    public static void removeRecord(Record record) {
        em.getTransaction().begin();
        em.remove(em.contains(record) ? record : em.merge(record));
        em.getTransaction().commit();
    }
}
