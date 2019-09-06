package dbUtils;

import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import java.util.Properties;

/**
 * Class to create and receive access to the oracle database using Hibernate Java framework.
 */
public class HibernateUtil {

    /**
     * Path to the hibernate configuration file.
     */
    private static final String HIBERNATE_CFG_XML = "META-INF/hibernate.cfg.xml";

    /**
     * {@link EntityManager} object for using hibernate module in whole application.
     */
    private static EntityManager em/* = createEM()*/;

    /**
     * Method to create connection between client application and Oracle database.
     * @param user user's login to sign in to the database.
     * @param passwd user's password to sign in to the database.
     * @return new {@link EntityManager} object after received remote access to the database.
     * @throws PersistenceException throws when user gives wrong data for sign in or if application can't create
     * connection with database server.
     */
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

