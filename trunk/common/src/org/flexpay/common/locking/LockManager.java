package org.flexpay.common.locking;

import org.hibernate.*;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.sql.SQLException;

import org.flexpay.common.logger.FPLogger;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class LockManager {
    private static LockManager instance = new LockManager();
    private HibernateTemplate hibernateTemplate;
    private HashMap<String,StatelessSession> lockedSessions = new HashMap<String, StatelessSession>();

    public static LockManager getInstance() {
        return instance;
    }

    protected LockManager() {}

    /**
     * Lock semaphore.
     *
     * @param semaphoreID String for semaphore ID
     * @return true if semaphore lock obtained, false if semaphore already locked
     */
    public synchronized boolean lock(String semaphoreID) {
        StatelessSession session = lockedSessions.get(semaphoreID);                     
        if (session != null) {
            return false;
        }
        session = hibernateTemplate.getSessionFactory().openStatelessSession();

        Transaction transaction = session.getTransaction();
        transaction.begin();
        List list = acquireLock(session, semaphoreID);
        if (list == null) {
            // semaphore already locked
            session.getTransaction().commit();
            session.close();
            return false;
        }
        if (list.size() == 0) {
            // create semaphore
            try {
                session.connection().createStatement().executeUpdate("insert into semaphore (semaphoreID) values ('" + semaphoreID + "')");
            } catch (SQLException e) {
                FPLogger.logMessage(FPLogger.ERROR, "LockManager: lock: Create semaphore " + semaphoreID + " exception!", e);
            }
            transaction.commit();
            transaction.begin();
            list = acquireLock(session, semaphoreID);
        }
        boolean isLocked = list.size() == 1;
        if (isLocked) {
            lockedSessions.put(semaphoreID, session);
        } else {
            session.getTransaction().commit();
            session.close();
        }
        return isLocked;

    }

    private List acquireLock(StatelessSession session, String semaphoreID) {
        SQLQuery sqlQuery = session.createSQLQuery("select semaphoreID from semaphore where semaphoreID=:semaphoreID for update nowait");
        sqlQuery.addScalar("semaphoreID", Hibernate.STRING).setString("semaphoreID", semaphoreID);
        List list;
        try {
            list = sqlQuery.list();
        } catch (HibernateException e) {
            FPLogger.logMessage(FPLogger.ERROR,"Lock Manager: acquireLock failed!",e);
            return null;
        }
        return list;
    }


    /**
     * Release locked semaphore.
     *
     * @param semaphoreID Semaphore for release 
     */
    public synchronized void releaseLock(String semaphoreID) {
        StatelessSession session = lockedSessions.remove(semaphoreID);
        if (session != null) {
            session.getTransaction().commit();
            session.close();
        }
    }

    /**
     * Release all locked sessions
     */
    public synchronized void releaseAll() {
        Collection<StatelessSession> sessions = lockedSessions.values();
        for (StatelessSession session : sessions) {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
}
