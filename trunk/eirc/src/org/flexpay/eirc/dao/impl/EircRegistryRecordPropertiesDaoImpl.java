package org.flexpay.eirc.dao.impl;

import org.flexpay.eirc.dao.EircRegistryRecordPropertiesDao;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.bti.persistence.apartment.BtiApartment;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

public class EircRegistryRecordPropertiesDaoImpl extends HibernateDaoSupport implements EircRegistryRecordPropertiesDao {

	protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Find record properties with full consumers info fetched
	 *
	 * @param registryId Registry key
	 * @param lowerBound Registry records min key to fetch
	 * @param upperBound Registry records max key to fetch
	 * @return list of properties
	 */
	@SuppressWarnings ({"unchecked"})
	@Override
	public List<EircRegistryRecordProperties> findWithConsumers(Long registryId, Long lowerBound, Long upperBound) {
		Object[] params = {registryId, lowerBound, upperBound};
		return (List<EircRegistryRecordProperties>) getHibernateTemplate()
				.findByNamedQuery("EircRegistryRecordProperties.findWithConsumers", params);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings ({"unchecked"})
	@Override
	public List<EircRegistryRecordProperties> findWithConsumers(Collection<Long> recordIds) {
		return (List<EircRegistryRecordProperties>) getHibernateTemplate()
				.findByNamedQueryAndNamedParam("EircRegistryRecordProperties.findWithConsumersAndEircAccount", "ids", recordIds);
	}

	/**
	 * Find record properties with apartments info fetched
	 *
	 * @param registryId Registry key
	 * @param lowerBound Registry records min key to fetch
	 * @param upperBound Registry records max key to fetch
	 * @return list of properties
	 */
	@SuppressWarnings ({"unchecked"})
	@Override
	public List<EircRegistryRecordProperties> findWithApartmentAttributes(Long registryId, Long lowerBound, Long upperBound) {
		Object[] params = {registryId, lowerBound, upperBound};
		List<Object[]> objects = (List<Object[]>) getHibernateTemplate()
				.findByNamedQuery("EircRegistryRecordProperties.findWithApartmentAttributes", params);
		List<EircRegistryRecordProperties> result = CollectionUtils.list();
		for (Object[] pair : objects) {
			EircRegistryRecordProperties props = (EircRegistryRecordProperties) pair[0];
			BtiApartment apartment = (BtiApartment) pair[1];
			props.setApartment(apartment);
			result.add(props);
		}

		return result;
	}
}
