package org.flexpay.bti.dao.impl;

import org.flexpay.bti.dao.BtiApartmentDaoExt;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.uniqueResult;

public class BtiApartmentDaoExtImpl extends JpaDaoSupport implements BtiApartmentDaoExt {

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public BtiApartment readApartmentWithAttributes(Long apartmentId) {
		return (BtiApartment) uniqueResult((List<?>) getJpaTemplate().
                findByNamedQuery("ApartmentAttribute.findWithAttributes", apartmentId));
	}
}
