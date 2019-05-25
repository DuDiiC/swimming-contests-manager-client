package dbUtils;

import dbModels.Competition;
import dbModels.Competitor;
import dbModels.Contest;

import javax.persistence.EntityManager;
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

    public static Contest getByName(Contest contest) {
        List cList = em.createQuery("FROM Contest WHERE name=:name")
                .setParameter("name", contest.getName()).getResultList();
        if(cList.size() != 0) {
            return (Contest) cList.get(0);
        } else {
            return null;
        }
    }

    public static List<Competitor> getAllCompetitors(Contest contest) {
        List competitorList =
                em.createQuery("select comp from Contest c join c.competitors comp where c.id=:id")
                .setParameter("id", contest.getId()).getResultList();
        return competitorList;
    }

    public static List<Competition> getAllCompetitions(Contest contest) {
        List competitionList =
                em.createQuery("select comp from Contest c join c.competitions comp where c.id=:id")
                .setParameter("id", contest.getId()).getResultList();
        return competitionList;
    }

    /**
     * Add one contest to database
     * @param contest record to add
     * @return true if add contest, else if contest was in database
     */
    public static boolean addContest(Contest contest) {
        em.getTransaction().begin();
//        List<Contest> cList = getAll();
//        for(Contest c : cList) {
//            if(c.getName().equals(contest.getName())) {
//                em.getTransaction().commit();
//                return false;
//            }
//        }
        if(getByName(contest) != null) {
            em.getTransaction().commit();
            return false;
        }
        em.persist(contest);
        em.getTransaction().commit();
        return true;
    }

    public static void removeContest(Contest contest) {
        em.getTransaction().begin();
        em.remove(em.contains(contest) ? contest : em.merge(contest));
        em.getTransaction().commit();
    }

    public static void addOrRemoveCompetitor(Contest contest, Competitor competitor) {
        em.getTransaction().begin();
        em.merge(contest);
//        em.merge(competitor);
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
//        em.merge(competition);
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
