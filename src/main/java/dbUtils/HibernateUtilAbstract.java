package dbUtils;

import javax.persistence.EntityManager;

/**
 * Abstract class with {@link EntityManager} object using to connecting with the database.
 */
public abstract class HibernateUtilAbstract {

    /**
     * {@link EntityManager} object for communication with the database.
     */
    protected static EntityManager em = HibernateUtil.getEm();
}
