package org.flexpay.ab.service.importexport.impl;

import org.flexpay.common.persistence.DomainObject;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllObjectsDao {

	private Logger log = LoggerFactory.getLogger(getClass());

	private HibernateTemplate hibernateTemplate;

	private long counter = 0;

	public void saveOrUpdate(DomainObject domainObject) {
		// remove object from hibernate session managed by hibernateTemplate
		hibernateTemplate.evict(domainObject);

		log.debug("Saving object: {}", domainObject);

		hibernateTemplate.saveOrUpdate(domainObject);

		++counter;
		if (counter == 15) {
			hibernateTemplate.flush();
			counter = 0;
		}
	}

	public void flushAndClear() {
		hibernateTemplate.flush();
		hibernateTemplate.clear();
		counter = 0;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
