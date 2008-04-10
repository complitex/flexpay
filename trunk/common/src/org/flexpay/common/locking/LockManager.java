package org.flexpay.common.locking;

import org.apache.log4j.Logger;
import org.hibernate.*;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class LockManager {

	private static Logger log = Logger.getLogger(LockManager.class);

	private static LockManager instance = new LockManager();
	private HibernateTemplate hibernateTemplate;
	private HashMap<String, StatelessSession> lockedSessions = new HashMap<String, StatelessSession>();

	public static LockManager getInstance() {
		return instance;
	}

	protected LockManager() {
	}

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
				session.connection().createStatement().executeUpdate(
						"insert into common_semaphores_tbl (semaphoreID) values ('" + semaphoreID + "')");
			} catch (SQLException e) {
				log.error("LockManager: lock: Create semaphore " + semaphoreID + " exception!", e);
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
		SQLQuery sqlQuery = session.createSQLQuery(
				"select semaphoreID from common_semaphores_tbl where semaphoreID=:semaphoreID for update");
		sqlQuery.addScalar("semaphoreID", Hibernate.STRING).setString("semaphoreID", semaphoreID);
		List list;
		try {
			list = sqlQuery.list();
		} catch (HibernateException e) {
			log.error("Lock Manager: acquireLock failed!", e);
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
//            if (session.getTransaction().isActive()){
                session.getTransaction().commit();
			    session.close();
//            }else{
//
//            }
        }
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		instance.hibernateTemplate = hibernateTemplate;
	}
}
