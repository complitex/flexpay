package org.flexpay.common.service.internal;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.apache.log4j.Logger;

import java.util.Collection;

/**
 *
 */
@Transactional
public class SessionUtils {

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

	@Transactional(propagation = Propagation.REQUIRED)
	public void evict(Object o) {

		log.debug("Evicting");

		hibernateTemplate.evict(o);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void evict(Collection c) {

		log.debug("Evicting collection");

		for (Object o : c) {
			hibernateTemplate.evict(o);
		}
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
