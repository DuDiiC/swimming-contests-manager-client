package dbUtils;

import dbModels.Trainer;

import javax.persistence.EntityManager;
import java.util.List;

public class HibernateUtilTrainer {

    protected static EntityManager em = HibernateUtil.getEm();

    public static List<Trainer> getAll() {
        List trainerList =
                em.createQuery("FROM Trainer").getResultList();
        return trainerList;
    }

    public static Trainer getById(Long id) {
        Trainer trainer = em.find(Trainer.class, id);
        return trainer;
    }

    public static void addTrainer(Trainer trainer) {
        em.getTransaction().begin();
        em.persist(trainer);
        em.getTransaction().commit();
    }

    public static void deleteTrainer(Trainer trainer) {
        em.getTransaction().begin();
        em.remove(em.contains(trainer) ? trainer : em.merge(trainer));
        em.getTransaction().commit();
    }
}
