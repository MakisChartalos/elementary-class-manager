package gr.aueb.cf.elementaryclassmanager.service.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class for managing JPA EntityManager instances and the EntityManagerFactory.
 * Provides methods for starting and managing transactions, as well as handling the lifecycle
 * of EntityManager and EntityManagerFactory instances.
 */
public class JPAHelper {
    private static EntityManagerFactory emf;
    private static ThreadLocal<EntityManager> threadLocal = new ThreadLocal<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private JPAHelper() {

    }

    /**
     * Retrieves the EntityManagerFactory instance.
     * If the factory is not initialized or is closed, a new instance is created.
     *
     * @return the EntityManagerFactory instance
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if ((emf == null) || (!emf.isOpen())) {
            emf = Persistence.createEntityManagerFactory("schoolPU");
        }
        return emf;
    }

    /**
     * Retrieves the EntityManager instance for the current thread.
     * If no EntityManager is associated with the current thread or it is closed, a new one is created.
     *
     * @return the EntityManager for the current thread
     */
    public static EntityManager getEntityManager() {
        EntityManager em = threadLocal.get();
        if ((em == null) || (!em.isOpen())) {
            em = getEntityManagerFactory().createEntityManager();
            threadLocal.set(em);
        }

        return em;
    }

    /**
     * Closes the EntityManager associated with the current thread, if it is open.
     */
    public static void closeEntityManager() {
        getEntityManager().close();
    }

    /**
     * Begins a new transaction on the current thread's EntityManager.
     */
    public static void beginTransaction() {
        getEntityManager().getTransaction().begin();
    }

    /**
     * Commits the current transaction on the current thread's EntityManager.
     */
    public static void commitTransaction() {
        getEntityManager().getTransaction().commit();
    }

    /**
     * Rolls back the current transaction on the current thread's EntityManager.
     */
    public static void rollbackTransaction() {
        getEntityManager().getTransaction().rollback();
    }

    /**
     * Closes the EntityManagerFactory, releasing all resources associated with it.
     */
    public static void closeEntityManagerFactory() {
        emf.close();
    }
}
