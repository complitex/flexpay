package org.flexpay.bti.dao.impl;

import org.flexpay.ab.persistence.Town;
import org.flexpay.bti.dao.BtiApartmentDaoExt;
import org.flexpay.bti.persistence.apartment.ApartmentAttributeBase;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.flexpay.common.persistence.Stub;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collections;
import java.util.List;

public class BtiApartmentDaoExtImpl extends HibernateDaoSupport implements BtiApartmentDaoExt {

    public BtiApartment readApartmentWithAttributes(Long buildingId) {

        BtiApartment building = (BtiApartment) DataAccessUtils.uniqueResult(getHibernateTemplate()
                .findByNamedQuery("BtiApartment.find", buildingId));

        return setupAttributes(building);
    }

    public BtiApartment readApartmentWithAttributesByAddress(Long addressId) {
        BtiApartment building = (BtiApartment) DataAccessUtils.uniqueResult(getHibernateTemplate()
                .findByNamedQuery("BtiApartment.findByAddress", addressId));
        return setupAttributes(building);
    }

    private BtiApartment setupAttributes(BtiApartment apartment) {

        if (apartment == null) {
            return null;
        }

        List<?> simpleAttrs = getHibernateTemplate()
                .findByNamedQuery("ApartmentAttributeBase.readSimpleAttributes", apartment.getId());
        addAttributes(apartment, simpleAttrs);
        List<?> tmpAttrs = getHibernateTemplate()
                .findByNamedQuery("ApartmentAttributeBase.readTmpAttributes", apartment.getId());
        addAttributes(apartment, tmpAttrs);

        if (apartment.getAttributes().isEmpty()) {
            apartment.setAttributes(Collections.EMPTY_SET);
        }

        return apartment;
    }

    private void addAttributes(BtiApartment apartment, List<?> attributes) {

        if (attributes == null || attributes.isEmpty()) {
            return;
        }

        for (Object obj : attributes) {
            ApartmentAttributeBase attribute = (ApartmentAttributeBase) obj;
            apartment.addAttribute(attribute);
        }
    }

    /**
     * Find all BtiApartment in the town
     *
     * @param town town to search
     * @return BtiApartment list in town
     */
    public List<BtiApartment> findByTown(Stub<Town> town) {
        return (List<BtiApartment>) getHibernateTemplate().findByNamedQuery("BtiApartment.findByTown", town.getId());
	}

}
