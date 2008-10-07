package org.flexpay.common.locking;

import org.apache.log4j.Logger;
import org.flexpay.common.util.CollectionUtils;
import org.hibernate.*;
import org.jetbrains.annotations.NonNls;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class LockManager {

	@NonNls
	private Logger log = Logger.getLogger(getClass());

	private HibernateTemplate hibernateTemplate;
	private Map<String, StatelessSession> lockedSessions = CollectionUtils.map();

	/**
	 * Private constructor
	 */
	private LockManager() {
	}

	/**
	 * Lock semaphore.
	 *
	 * @param semaphoreID String for semaphore ID
	 * @return true if semaphore lock obtained, false if semaphore already locked
	 */
	public synchronized boolean lock(@NonNls String semaphoreID) {

		StatelessSession session = lockedSessions.get(semaphoreID);
		if (session != null) {
			log.debug("Already locked");
			return false;
		}

		log.debug("Creating new stateless session");
		session = hibernateTemplate.getSessionFactory().openStatelessSession();

		Transaction transaction = session.getTransaction();
		transaction.begin();
		List list = acquireLock(session, semaphoreID);
		if (list == null) {
			if (log.isDebugEnabled()) {
				log.debug("Semaphore already locked: " + semaphoreID);
			}
			session.getTransaction().commit();
			session.close();
			return false;
		}

		if (list.size() == 0) {
			// create semaphore
			@NonNls SQLQuery semQuery = session.createSQLQuery("insert into common_semaphores_tbl (semaphoreID) values (:semaphoreID)");
			semQuery.setString("semaphoreID", semaphoreID);
			semQuery.executeUpdate();
			transaction.commit();

			// create new transaction
			transaction = session.getTransaction();
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

	private List acquireLock(@NonNls StatelessSession session, String semaphoreID) {

		@NonNls SQLQuery sqlQuery = session.createSQLQuery(
				"select semaphoreID from common_semaphores_tbl where semaphoreID=:semaphoreID for update");
		sqlQuery.addScalar("semaphoreID", Hibernate.STRING).setString("semaphoreID", semaphoreID);
		List list;
		try {
			list = sqlQuery.list();
		} catch (HibernateException e) {
			log.error("AcquireLock failed!", e);
			return null;
		}
		return list;
	}

	/**
	 * Release locked semaphore.
	 *
	 * @param semaphoreID Semaphore for release
	 */
	public synchronized void releaseLock(@NonNls String semaphoreID) {

		if (log.isDebugEnabled()) {
			log.debug("Releasing lock: " + semaphoreID);
		}

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

		log.debug("Releasing all locks");

		Collection<StatelessSession> sessions = lockedSessions.values();
		for (StatelessSession session : sessions) {
			session.getTransaction().commit();
			session.close();
		}

		lockedSessions.clear();
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
