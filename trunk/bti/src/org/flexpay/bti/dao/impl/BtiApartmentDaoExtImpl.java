package org.flexpay.bti.dao.impl;

import org.flexpay.bti.dao.BtiApartmentDaoExt;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.uniqueResult;

public class BtiApartmentDaoExtImpl extends HibernateDaoSupport implements BtiApartmentDaoExt {

    @Override
    public BtiApartment readApartmentWithAttributes(Long apartmentId) {
		return (BtiApartment) uniqueResult((List<?>) getHibernateTemplate().
                findByNamedQuery("ApartmentAttribute.findWithAttributes", apartmentId));
	}
}
