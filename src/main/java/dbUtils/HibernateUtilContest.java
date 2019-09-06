package dbUtils;

import dbModels.Competition;
import dbModels.Competitor;
import dbModels.Contest;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Class with database operations for {@link Contest} objects.
 */
public class HibernateUtilContest extends HibernateUtilAbstract {

    /**
     * @return {@link List} with all {@link Contest}s in database.
     */
    public static List<Contest> getAll() {
        List contestList = em.createQuery("FROM Contest").getResultList();
        return contestList;
    }

    /**
     * @param id {@link Contest} id value in the database.
     * @return {@link Contest} object with id which is given.
     */
    public static Contest getById(Long id) {
        Contest contest = em.find(Contest.class, id);
        return contest;
    }

    /**
     * @return {@link Contest} object by name which was given.
     */
    public static Contest getByName(Contest contest) {
        List cList = em.createQuery("FROM Contest WHERE name=:name")
                .setParameter("name", contest.getName()).getResultList();
        if(cList.size() != 0) {
            return (Contest) cList.get(0);
        }
        return null;
    }

    /**
     * @return {@link List} of all {@link Competitor}s registered on the {@link Contest} which was given.
     */
    public static List<Competitor> getAllCompetitors(Contest contest) {
        List competitorList =
                em.createQuery("select comp from Contest c join c.competitors comp where c.id=:id")
                .setParameter("id", contest.getId()).getResultList();
        return competitorList;
    }

    /**
     * @return {@link List} of all {@link Competition}s in the {@link Contest} which was given.
     */
    public static List<Competition> getAllCompetitions(Contest contest) {
        List competitionList =
                em.createQuery("select comp from Contest c join c.competitions comp where c.id=:id")
                .setParameter("id", contest.getId()).getResultList();
        return competitionList;
    }

    /**
     * @return true if contest will be added, false if contest was in database
     */
    public static boolean addContest(Contest contest) {
        em.getTransaction().begin();
        if(getByName(contest) != null) {
            em.getTransaction().commit();
            return false;
        }
        em.persist(contest);
        em.getTransaction().commit();
        return true;
    }

    /**
     * Removes given {@link Contest} object from the database.
     */
    public static void removeContest(Contest contest) {
        em.getTransaction().begin();
        em.remove(em.contains(contest) ? contest : em.merge(contest));
        em.getTransaction().commit();
    }


    public static void addCompetitor(Contest contest, Competitor competitor) {
        em.getTransaction().begin();
        em.merge(contest);
        em.getTransaction().commit();
    }

    public static void removeCompetitor(Contest contest, Competitor competitor) {
        em.getTransaction().begin();
        em.merge(contest);
        em.getTransaction().commit();
    }

    public static void addOrRemoveCompetitor(Contest contest, Competitor competitor) {
        em.getTransaction().begin();
        em.merge(contest);
        em.getTransaction().commit();
    }

    public static void addOrRemoveAllCompetitors(Contest contest) {
        em.getTransaction().begin();
        em.merge(contest);
        em.getTransaction().commit();
    }

    public static void addOrRemoveCompetition(Contest contest) {
        em.getTransaction().begin();
        em.merge(contest);
        em.getTransaction().commit();
    }

    public static void addOrRemoveAllCompetitions(Contest contest) {
        em.getTransaction().begin();
        em.merge(contest);
        em.getTransaction().commit();
    }

    public static void updateContest(Contest contest) {
        em.getTransaction().begin();
        Contest tmp = em.find(Contest.class, contest.getId());
        tmp.setName(contest.getName());
        tmp.setCity(contest.getCity());
        em.merge(tmp);
        em.getTransaction().commit();
    }
}
