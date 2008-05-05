package org.flexpay.eirc.dao.imp;

import org.flexpay.eirc.dao.ServiceDaoExt;
import org.flexpay.eirc.persistence.AccountRecordType;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.ServiceType;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class ServiceDaoExtImpl extends HibernateDaoSupport implements ServiceDaoExt {

	private ServiceDaoJDBC serviceDaoJDBC;

	@SuppressWarnings({"unchecked"})
	public List<ServiceType> getServiceTypes() {
		return getHibernateTemplate().find("select distinct t from ServiceType t left join fetch t.typeNames n left join fetch n.lang where t.status=0");
	}

	/**
	 * Find Service type by its code
	 *
	 * @param code service type code
	 * @return ServiceType instance
	 */
	public ServiceType findByCode(int code) {
		try {
			getHibernateTemplate().setMaxResults(1);
			List objects = getHibernateTemplate().find("from ServiceType where code=? and status=0", code);
			return objects.isEmpty() ? null : (ServiceType) objects.get(0);
		} finally {
			getHibernateTemplate().setMaxResults(0);
		}
	}

	public ServiceProvider findByNumber(Long number) {
		try {
			getHibernateTemplate().setMaxResults(1);
			List objects = getHibernateTemplate().find("from ServiceProvider where providerNumber=?", number);
			return objects.isEmpty() ? null : (ServiceProvider) objects.get(0);
		} finally {
			getHibernateTemplate().setMaxResults(0);
		}
	}

	/**
	 * Find Service by provider and type ids
	 *
	 * @param providerId ServiceProvider id
	 * @param typeId	 ServiceType id
	 * @return Service instance
	 */
	public Service findService(Long providerId, Long typeId) {
		return serviceDaoJDBC.findService(providerId, typeId);
	}

	/**
	 * Find record type by id
	 *
	 * @param typeId Record type enum id
	 * @return record type
	 */
	public AccountRecordType findRecordType(int typeId) {
		try {
			getHibernateTemplate().setMaxResults(1);
			List objects = getHibernateTemplate().find(
					"from AccountRecordType where typeId=?", typeId);
			return objects.isEmpty() ? null : (AccountRecordType) objects.get(0);
		} finally {
			getHibernateTemplate().setMaxResults(0);
		}
	}

	public void setServiceDaoJDBC(ServiceDaoJDBC serviceDaoJDBC) {
		this.serviceDaoJDBC = serviceDaoJDBC;
	}
}
