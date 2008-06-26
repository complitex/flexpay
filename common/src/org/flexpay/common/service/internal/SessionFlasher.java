package org.flexpay.common.service.internal;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.apache.log4j.Logger;

/**
 *
 */
@Transactional
public class SessionFlasher {

	private Logger log = Logger.getLogger(getClass());

	private HibernateTemplate hibernateTemplate;

	@Transactional(propagation = Propagation.REQUIRED)
	public void flush() {

		log.debug("Flushing");

		hibernateTemplate.flush();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void clear() {

		log.debug("Clearing");

		hibernateTemplate.clear();
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
