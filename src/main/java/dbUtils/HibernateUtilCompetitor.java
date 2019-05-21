package dbUtils;

import dbModels.Competitor;
import dbModels.Record;

import javax.persistence.EntityManager;
import java.util.List;

public class HibernateUtilCompetitor {

    protected static EntityManager em = HibernateUtil.getEm();

    public static List<Competitor> getAll() {
        List competitorList = em.createQuery("FROM Competitor").getResultList();
        return competitorList;
    }

    public static Competitor getById(Long id) {
        Competitor competitor = em.find(Competitor.class, id);
        return competitor;
    }

    public static List<Record> getAllRecords(Competitor competitor) {
        List recordList =
                em.createQuery("SELECT r FROM Competitor c JOIN Record r ON c.pesel=r.competitor.pesel WHERE c.pesel=:pesel")
                .setParameter("pesel", competitor.getPesel()).getResultList();
        return recordList;
    }

    public static void addCompetitor(Competitor competitor) {
        em.getTransaction().begin();
        em.persist(competitor);
        em.getTransaction().commit();
    }

    public static void deleteCompetitor(Competitor competitor) {
        em.getTransaction().begin();
        em.remove(em.contains(competitor) ? competitor : em.merge(competitor));
        em.getTransaction().commit();
    }
}
