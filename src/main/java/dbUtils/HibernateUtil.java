package dbUtils;

import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import java.util.Properties;

public class HibernateUtil {

    private static final String HIBERNATE_CFG_XML = "META-INF/hibernate.cfg.xml";

    private static EntityManager em/* = createEM()*/;

    public static EntityManager createEM(String user, String passwd) throws PersistenceException {

        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_XML);
        configuration.getProperties().setProperty("connection.username", user);
        configuration.getProperties().setProperty("connection.password", passwd);
        Properties props = new Properties();
        props.setProperty("javax.persistence.jdbc.user", user);
        props.setProperty("javax.persistence.jdbc.password", passwd);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence1", props);
        return emf.createEntityManager();
    }

    public static EntityManager getEm() {
        return em;
    }

    public static void setEm(EntityManager em) {
        HibernateUtil.em = em;
    }
}

