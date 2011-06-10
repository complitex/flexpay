package org.flexpay.common.locking;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;

public class LockManager {

	private Logger log = LoggerFactory.getLogger(getClass());

	private JpaTemplate jpaTemplate;
	private Map<String, EntityManager> lockedSessions = map();

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
	public synchronized boolean lock(String semaphoreID) {
		/*
		EntityManager entityManager = lockedSessions.get(semaphoreID);
		if (entityManager != null) {
			log.debug("Already locked");
			return false;
		}

		log.debug("Creating new stateless session");

		entityManager = jpaTemplate.getEntityManager();


		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		List<?> list = acquireLock(entityManager, semaphoreID);
		if (list == null) {
			log.debug("Semaphore already locked: {}", semaphoreID);
			entityManager.getTransaction().commit();
			entityManager.close();
			return false;
		}

		if (list.isEmpty()) {
			// create semaphore
			Query semQuery = entityManager.createNativeQuery("insert into common_semaphores_tbl (semaphoreID) values (:semaphoreID)");
			semQuery.setParameter("semaphoreID", semaphoreID);
			semQuery.executeUpdate();
			transaction.commit();

			// create new transaction
			entityManager.getTransaction();
			transaction.begin();
			list = acquireLock(entityManager, semaphoreID);
		}

		boolean isLocked = list.size() == 1;
		if (isLocked) {
			lockedSessions.put(semaphoreID, entityManager);
		} else {
			entityManager.getTransaction().commit();
			entityManager.close();
		}

		return isLocked;
		*/
		return true;
	}

	private List acquireLock(EntityManager entityManager, String semaphoreID) {

		Query sqlQuery = entityManager.createNativeQuery(
				"select semaphoreID from common_semaphores_tbl where semaphoreID=:semaphoreID");
		sqlQuery.setParameter("semaphoreID", semaphoreID);
		List<?> list;
		try {
			list = sqlQuery.getResultList();
		} catch (HibernateException e) {
			log.error("AcquireLock failed!");
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

		/*
		log.debug("Releasing lock: {}", semaphoreID);

		EntityManager entityManager = lockedSessions.remove(semaphoreID);
		if (entityManager != null) {
			entityManager.getTransaction().commit();
			entityManager.close();
		}
		*/
	}

	/**
	 * Release all locked sessions
	 */
	public synchronized void releaseAll() {

		/*
		log.debug("Releasing all locks");

		Collection<EntityManager> entityManagers = lockedSessions.values();
		for (EntityManager entityManager : entityManagers) {
			entityManager.getTransaction().commit();
			entityManager.close();
		}

		lockedSessions.clear();
		*/
	}

    @Required
	public void setJpaTemplate(JpaTemplate jpaTemplate) {
		this.jpaTemplate = jpaTemplate;
	}

}
