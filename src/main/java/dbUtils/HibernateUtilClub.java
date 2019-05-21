package dbUtils;

import dbModels.Club;
import dbModels.Competitor;
import dbModels.Trainer;

import javax.persistence.EntityManager;
import java.util.List;

public class HibernateUtilClub {

    protected static EntityManager em = HibernateUtil.getEm();

    public static List<Club> getAll() {
        List clubList = em.createQuery("FROM Club").getResultList();
        return clubList;
    }

    public static Club getById(Long id) {
        Club club = em.find(Club.class, id);
        return club;
    }

    public static Club getByNameAndCity(String name, String city) {
        Club club =
                (Club) em.createQuery("FROM Club WHERE name like :name and city like :city")
                .setParameter("name", name).setParameter("city", city).getResultList().get(0);
        return club;
    }

    public static List<Trainer> getAllTrainers(Club club) {
        List trainerList =
                em.createQuery("SELECT t FROM Club c JOIN Trainer t ON c.clubId=t.club.clubId where c.clubId=:id")
                .setParameter("id", club.getClubId()).getResultList();
        return trainerList;
    }

    public static List<Competitor> getAllCompetitors(Club club) {
        List competitorList =
                em.createQuery("SELECT comp from Club c JOIN Competitor comp ON c.clubId=comp.club.clubId where c.clubId=:id")
                .setParameter("id", club.getClubId()).getResultList();
        return competitorList;
    }

    public static void addClub(Club club) {
        em.getTransaction().begin();
        em.persist(club);
        em.getTransaction().commit();
    }

    public static void deleteClub(Club club) {
        em.getTransaction().begin();
        em.remove(em.contains(club) ? club : em.merge(club));
        em.getTransaction().commit();
    }
}
