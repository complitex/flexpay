package org.flexpay.eirc.dao;

import org.flexpay.eirc.persistence.EircRegistryRecordProperties;

import java.util.Collection;
import java.util.List;

public interface EircRegistryRecordPropertiesDao {

	/**
	 * Find record properties with full consumers info fetched
	 *
	 * @param registryId Registry key
	 * @param lowerBound Registry records min key to fetch
	 * @param upperBound Registry records max key to fetch
	 * @return list of properties
	 */
	List<EircRegistryRecordProperties> findWithConsumers(Long registryId, Long lowerBound, Long upperBound);

	/**
	 * Find record properties with full consumers info fetched
	 *
	 * @param recordIds Collection record ids
	 * @return list of properties
	 */
	List<EircRegistryRecordProperties> findWithConsumers(Collection<Long> recordIds);

	/**
	 * Find record properties with apartments info fetched
	 *
	 * @param registryId Registry key
	 * @param lowerBound Registry records min key to fetch
	 * @param upperBound Registry records max key to fetch
	 * @return list of properties
	 */
	List<EircRegistryRecordProperties> findWithApartmentAttributes(Long registryId, Long lowerBound, Long upperBound);
}
