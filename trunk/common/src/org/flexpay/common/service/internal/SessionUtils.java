package org.flexpay.common.service.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 *
 */
@Transactional
public class SessionUtils {

	private Logger log = LoggerFactory.getLogger(getClass());

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

	/**
	 * Evict objects from session
	 *
	 * @param o Object to be evicted
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void evict(Object o) {

		log.debug("Evicting");

		hibernateTemplate.evict(o);
	}

	/**
	 * Evict all objects from a collection
	 *
	 * @param c Collection that elements should be evicted
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void evict(Collection<?> c) {

		log.debug("Evicting collection");

		for (Object o : c) {
			hibernateTemplate.evict(o);
		}
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
