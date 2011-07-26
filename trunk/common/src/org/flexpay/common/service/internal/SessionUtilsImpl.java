package org.flexpay.common.service.internal;

import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 *
 */
@Transactional
public class SessionUtilsImpl implements SessionUtils {

	private Logger log = LoggerFactory.getLogger(getClass());

	private JpaTemplate jpaTemplate;

	@Transactional (propagation = Propagation.REQUIRED)
	public void flush() {

		log.debug("Flushing");

		jpaTemplate.flush();
	}

	@Transactional (propagation = Propagation.REQUIRED)
	public void clear() {

		log.debug("Clearing");

		jpaTemplate.getEntityManager().clear();
	}

	/**
	 * Evict objects from session
	 *
	 * @param o Object to be evicted
	 */
	public void evict(Object o) {

		if (o != null) {
			log.debug("Evicting does not support {}", o.getClass());
		}
	}

	/**
	 * Evict all objects from a collection
	 *
	 * @param c Collection that elements should be evicted
	 */
	public void evict(Collection<?> c) {

		log.debug("Evicting collection does not support");
	}

	/**
	 * Check if object is a proxy and get real domain object
	 *
	 * @param obj Object to unproxy
	 * @param <T> Object type
	 * @return Object back
	 */
	@SuppressWarnings ({"unchecked"})
	@Override
	@Transactional (propagation = Propagation.REQUIRED)
	public <T> T unproxy(T obj) {
		if (obj instanceof HibernateProxy) {
			obj = (T)((HibernateProxy) obj).getHibernateLazyInitializer().getImplementation();
		}

		return obj;
	}

    @Required
	public void setJpaTemplate(JpaTemplate jpaTemplate) {
		this.jpaTemplate = jpaTemplate;
	}
}
