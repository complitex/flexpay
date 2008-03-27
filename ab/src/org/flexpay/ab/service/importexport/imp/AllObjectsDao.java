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
		session.saveOrUpdate(domainObject);
//		hibernateTemplate.save(domainObject);
		++counter;
		if (counter == 15) {
//			hibernateTemplate.flush();
//			hibernateTemplate.clear();
			session.flush();
			session.clear();
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
