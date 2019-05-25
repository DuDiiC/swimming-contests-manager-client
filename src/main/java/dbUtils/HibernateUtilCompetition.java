package dbUtils;

import dbModels.Competition;
import dbModels.Record;

import javax.persistence.EntityManager;
import java.util.List;

public class HibernateUtilCompetition {

    private static EntityManager em = HibernateUtil.getEm();

    public static List<Competition> getAll() {
        List competitionList = em.createQuery("FROM Competition").getResultList();
        return competitionList;
    }

    public static Competition getById(Long id) {
        Competition competition = em.find(Competition.class, id);
        return competition;
    }

    public static Competition getByStyleAndDistanceAndGender(String style, Integer distance, String gender) {
        return (Competition) em.createQuery("FROM Competition where style=:style and distance=:distance and gender=:gender")
                .setParameter("style", style).setParameter("distance", distance).setParameter("gender", gender)
                .getResultList().get(0);
    }

    public static List<Record> getAllRecords(Competition competition) {
        List recordList =
                em.createQuery("SELECT r FROM Competition c JOIN Record r on c.competitionId=r.competition.competitionId where c.competitionId=:id")
                .setParameter("id", competition.getCompetitionId()).getResultList();
        return recordList;
    }

    public static void addCompetition(Competition competition) {
        em.getTransaction().begin();
        em.persist(competition);
        em.getTransaction().commit();
    }

    public static void removeCompetition(Competition competition) {
        em.getTransaction().begin();
        em.remove(em.contains(competition) ? competition : em.merge(competition));
        em.getTransaction().commit();
    }
}
