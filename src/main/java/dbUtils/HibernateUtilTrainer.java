package dbUtils;

import dbModels.Trainer;

import javax.persistence.EntityManager;
import java.util.List;

public class HibernateUtilTrainer {

    private static EntityManager em = HibernateUtil.getEm();

    public static List<Trainer> getAll() {
        List trainerList = em.createQuery("FROM Trainer").getResultList();
        return trainerList;
    }

    public static Trainer getById(Long id) {
        Trainer trainer = em.find(Trainer.class, id);
        return trainer;
    }

    private static Trainer getByName(Trainer trainer) {
        List tList = em.createQuery("FROM Trainer WHERE name LIKE :name AND surname LIKE :surname")
                .setParameter("name", trainer.getName()).setParameter("surname", trainer.getSurname())
                .getResultList();
        if(tList.size() != 0) {
            return (Trainer) tList.get(0);
        }
        return null;
    }

    public static boolean addTrainer(Trainer trainer) {
        em.getTransaction().begin();
        if(getByName(trainer) != null) {
            em.getTransaction().commit();
            return false;
        }
        em.persist(trainer);
        em.getTransaction().commit();
        return true;
    }

    public static void removeTrainer(Trainer trainer) {
        em.getTransaction().begin();
        //em.remove(em.contains(trainer) ? trainer : em.merge(trainer));
        em.remove(trainer);
        em.getTransaction().commit();
    }

    public static void updateTrainer(Trainer trainer) {
        em.getTransaction().begin();
        Trainer tmp = em.find(Trainer.class, trainer.getLicenceNr());
        tmp.setName(trainer.getName());
        tmp.setSurname(trainer.getSurname());
        em.merge(tmp);
        em.getTransaction().commit();
    }
}
