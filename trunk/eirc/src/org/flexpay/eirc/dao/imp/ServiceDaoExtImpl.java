package org.flexpay.eirc.dao.imp;

import org.flexpay.eirc.dao.ServiceDaoExt;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.AccountRecordType;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class ServiceDaoExtImpl extends HibernateDaoSupport implements ServiceDaoExt {

	/**
	 * Find Service type by its code
	 *
	 * @param code service type code
	 * @return ServiceType instance
	 */
	public ServiceType findByCode(int code) {
		getHibernateTemplate().setMaxResults(1);
		List objects = getHibernateTemplate().find("from ServiceType where code=? and status=0", code);
		return objects.isEmpty() ? null : (ServiceType) objects.get(0);
	}

	public ServiceProvider findByNumber(Long number) {
		getHibernateTemplate().setMaxResults(1);
		List objects = getHibernateTemplate().find("from ServiceProvider where providerNumber=?", number);
		return objects.isEmpty() ? null : (ServiceProvider) objects.get(0);
	}

	/**
	 * Find Service by provider and type ids
	 *
	 * @param providerId ServiceProvider id
	 * @param typeId	 ServiceType id
	 * @return Service instance
	 */
	public Service findService(Long providerId, Long typeId) {
		getHibernateTemplate().setMaxResults(1);
		Object[] params = {providerId, typeId};
		List objects = getHibernateTemplate().find(
				"from Service where serviceProvider.id=? and serviceType.id=?", params);
		return objects.isEmpty() ? null : (Service) objects.get(0);
	}

	/**
	 * Find record type by id
	 *
	 * @param typeId Record type enum id
	 * @return record type
	 */
	public AccountRecordType findRecordType(int typeId) {
		getHibernateTemplate().setMaxResults(1);
		List objects = getHibernateTemplate().find(
				"from PersonalAccountRecordType where typeId=?", typeId);
		return objects.isEmpty() ? null : (AccountRecordType) objects.get(0);
	}
}
