package dbUtils;

import dbModels.Competition;
import dbModels.Competitor;
import dbModels.Contest;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class HibernateUtilContest {

    protected static EntityManager em = HibernateUtil.getEm();

    public static List<Contest> getAll() {
        List contestList = em.createQuery("FROM Contest").getResultList();
        return contestList;
    }

    public static Contest getById(Long id) {
        Contest contest = em.find(Contest.class, id);
        return contest;
    }

    public static Contest getByNameAndCityAndDate(String name, String city, Date date) {
        return (Contest) em.createQuery("FROM Contest WHERE name=:name and city=:city and date=:date")
                .setParameter("name", name).setParameter("city", city).setParameter("date", date)
                .getResultList().get(0);
    }

    public static List<Competitor> getAllCompetitors(Contest contest) {
        List competitorList =
                em.createQuery("select comp from Contest c join c.competitors comp where c.contest_id=:id")
                .setParameter("id", contest.getContest_id()).getResultList();
        return competitorList;
    }

    public static List<Competition> getAllCompetitions(Contest contest) {
        List competitionList =
                em.createQuery("select comp from Contest c join c.competitions comp where c.contest_id=:id")
                .setParameter("id", contest.getContest_id()).getResultList();
        return competitionList;
    }

    public static void addContest(Contest contest) {
        em.getTransaction().begin();
        em.persist(contest);
        em.getTransaction().commit();
    }

    public static void deleteContest(Contest contest) {
        em.getTransaction().begin();
        em.remove(em.contains(contest) ? contest : em.merge(contest));
        em.getTransaction().commit();
    }

    public static void addOrRemoveCompetitor(Contest contest, Competitor competitor) {
        em.getTransaction().begin();
        em.merge(contest);
        em.merge(competitor);
        em.getTransaction().commit();
    }

    public static void addOrRemoveAllCompetitors(Contest contest) {
        em.getTransaction().begin();
        em.merge(contest);
        em.getTransaction().commit();
    }

    public static void addOrRemoveCompetition(Contest contest, Competition competition) {
        em.getTransaction().begin();
        em.merge(contest);
        em.merge(competition);
        em.getTransaction().commit();
    }

    public static void addOrRemoveAllCompetitions(Contest contest) {
        em.getTransaction().begin();
        em.merge(contest);
        em.getTransaction().commit();
    }
}
