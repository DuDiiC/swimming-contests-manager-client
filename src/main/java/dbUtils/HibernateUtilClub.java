package dbUtils;

import dbModels.Club;
import dbModels.Competitor;
import dbModels.Trainer;

import javax.persistence.EntityManager;
import java.util.List;

public class HibernateUtilClub {

    private static EntityManager em = HibernateUtil.getEm();

    public static List<Club> getAll() {
        List clubList = em.createQuery("FROM Club").getResultList();
        return clubList;
    }

    public static Club getById(Long id) {
        Club club = em.find(Club.class, id);
        return club;
    }

    private static Club getByNameAndCity(Club club) {
        List cList =
                em.createQuery("FROM Club WHERE name like :name and city like :city")
                .setParameter("name", club.getName()).setParameter("city", club.getCity())
                .getResultList();
        if(cList.size() != 0) {
            return (Club) cList.get(0);
        }
        return null;
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

    public static boolean addClub(Club club) {
        em.getTransaction().begin();
        if(getByNameAndCity(club) != null) {
            em.getTransaction().commit();
            return false;
        }
        em.persist(club);
        em.getTransaction().commit();
        return true;
    }

    public static void removeClub(Club club) {
        em.getTransaction().begin();
        em.remove(em.contains(club) ? club : em.merge(club));
        em.getTransaction().commit();
    }

    public static void updateClub(Club club) {
        em.getTransaction().begin();
        Club tmp = em.find(Club.class, club.getClubId());
        tmp.setName(club.getName());
        tmp.setCity(club.getCity());
        em.merge(tmp);
        em.getTransaction().commit();
    }
}
