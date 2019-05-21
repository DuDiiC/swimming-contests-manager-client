package dbUtils;

import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {

    private static final String HIBERNATE_CFG_XML = "META-INF/hibernate.cfg.xml";

    public static EntityManager em = createEM();

    public static EntityManager createEM() {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_XML);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence1");
        return emf.createEntityManager();
    }

    public static EntityManager getEm() {
        return em;
    }
}

