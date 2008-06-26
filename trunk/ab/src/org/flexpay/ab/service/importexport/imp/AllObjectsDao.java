package org.flexpay.ab.service.importexport.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.persistence.DomainObject;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AllObjectsDao {

	private Logger log = Logger.getLogger(getClass());
	private HibernateTemplate hibernateTemplate;

	private Session session = null;
	private Transaction tx = null;
	private boolean rollbackOnly = false;

	private long counter = 0;

	public void saveOrUpdate(DomainObject domainObject) {
		// remove object from hibernate session managed by hibernateTemplate
		hibernateTemplate.evict(domainObject);

		if (domainObject.isNew()) {
			session.save(domainObject);
		} else {
			session.saveOrUpdate(domainObject);
		}
//		hibernateTemplate.save(domainObject);
		++counter;
		if (counter == 15) {
//			hibernateTemplate.flush();
//			hibernateTemplate.clear();
			session.flush();
			counter = 0;
		}
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void openSession() {
		session = hibernateTemplate.getSessionFactory().openSession();
		tx = session.beginTransaction();
	}

	public void setRollbackOnly() {
		rollbackOnly = true;
	}

	public void closeSession() {
		if (tx != null) {
			if (rollbackOnly) {
				tx.rollback();
			} else {
				tx.commit();
			}
			tx = null;
		}
		if (session != null) {
			session.close();
			session = null;
		}
	}
}
