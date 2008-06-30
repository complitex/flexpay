package org.flexpay.ab.service.importexport.imp;

import org.flexpay.common.persistence.DomainObject;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class AllObjectsDao {

	private HibernateTemplate hibernateTemplate;

	private Session session = null;
	private Transaction tx = null;
	private boolean rollbackOnly = false;

	private long counter = 0;

	public void saveOrUpdate(DomainObject domainObject) {
		// remove object from hibernate session managed by hibernateTemplate
		hibernateTemplate.evict(domainObject);

//		if (domainObject.isNew()) {
//			session.save(domainObject);
//		} else {
//			session.saveOrUpdate(domainObject);
//		}
		hibernateTemplate.saveOrUpdate(domainObject);

		++counter;
		if (counter == 15) {
			hibernateTemplate.flush();
//			session.flush();
			counter = 0;
		}
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void openSession() {
//		session = hibernateTemplate.getSessionFactory().openSession();
//		tx = session.beginTransaction();
	}

	public void setRollbackOnly() {
		rollbackOnly = true;
	}

	public void closeSession() {
//		if (tx != null) {
//			if (rollbackOnly) {
//				tx.rollback();
//			} else {
//				tx.commit();
//			}
//			tx = null;
//		}
//		if (session != null) {
//			session.flush();
//			session.close();
//			session = null;
//		}

		hibernateTemplate.flush();
		hibernateTemplate.clear();
	}
}
