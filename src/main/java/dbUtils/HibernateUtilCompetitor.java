package dbUtils;

import dbModels.Club;
import dbModels.Competitor;
import dbModels.Record;

import javax.persistence.EntityManager;
import java.util.List;

public class HibernateUtilCompetitor {

    private static EntityManager em = HibernateUtil.getEm();

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

    public static void removeCompetitor(Competitor competitor) {
        em.getTransaction().begin();
        em.remove(em.contains(competitor) ? competitor : em.merge(competitor));
        em.getTransaction().commit();
    }

    public static void removeAllCompetitorsFromClub(Club club) {
        List<Competitor> cList = getAll();
        for(Competitor c : cList) {
            if(club.getClubId() == c.getClub().getClubId()) {
                removeCompetitor(c);
            }
        }
    }

    public static void updateCompetitor(Competitor competitor) {
        em.getTransaction().begin();
        Competitor tmp = em.find(Competitor.class, competitor.getPesel());
        tmp.setName(competitor.getName());
        tmp.setSurname(competitor.getSurname());
        em.merge(tmp);
        em.getTransaction().commit();
    }
}
