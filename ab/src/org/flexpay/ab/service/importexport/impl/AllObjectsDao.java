package org.flexpay.ab.service.importexport.impl;

import org.flexpay.common.persistence.DomainObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

public class AllObjectsDao {

	private Logger log = LoggerFactory.getLogger(getClass());

	private JpaTemplate jpaTemplate;

	private long counter = 0;

	public void saveOrUpdate(DomainObject domainObject) {
		// remove object from hibernate session managed by hibernateTemplate
		//jpaTemplate.evict(domainObject);

		log.debug("Saving object: {}", domainObject);

		if (domainObject.isNew()) {
			jpaTemplate.persist(domainObject);
		} else {
			jpaTemplate.merge(domainObject);
		}

		++counter;
		if (counter == 15) {
			jpaTemplate.flush();
			counter = 0;
		}
	}

	public void flushAndClear() {
		jpaTemplate.execute(new JpaCallback<Object>() {
            @Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				em.flush();
				em.clear();
				return null;
			}

		}, true);
	}

	@Required
	public void setJpaTemplate(JpaTemplate jpaTemplate) {
		this.jpaTemplate = jpaTemplate;
	}

}
