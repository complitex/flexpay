package org.flexpay.bti.dao.impl;

import org.flexpay.bti.dao.BtiApartmentDaoExt;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BtiApartmentDaoExtImpl extends HibernateDaoSupport implements BtiApartmentDaoExt {

	public BtiApartment readApartmentWithAttributes(Long apartmentId) {

		return (BtiApartment) DataAccessUtils.uniqueResult(getHibernateTemplate()
				.findByNamedQuery("ApartmentAttribute.findWithAttributes", apartmentId));
	}
}
