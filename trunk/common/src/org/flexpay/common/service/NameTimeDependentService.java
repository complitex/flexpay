package org.flexpay.common.service;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.*;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.apache.commons.collections.ArrayStack;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface NameTimeDependentService<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> {

	/**
	 * Create new NameTimeDependent with a single name
	 *
	 * @param nameTranslations name translations
	 * @param filters		   parent filters
	 * @param from			 Date from which the name is valid
	 * @return persisted Region object
	 * @throws FlexPayExceptionContainer if operation fails
	 */
	NTD create(List<T> nameTranslations, ArrayStack filters, Date from)
			throws FlexPayExceptionContainer;

	/**
	 * Read object by its unique id
	 *
	 * @param id key
	 * @return object, or <code>null</code> if object not found
	 */
	public NTD read(Long id);

	/**
	 * Read object temporal name by its unique id
	 *
	 * @param id key
	 * @return object temporal name , or <code>null</code> if not found
	 */
	public DI readTemporalName(Long id);

	/**
	 * Get temporal names
	 *
	 * @param filters parent filters
	 * @param pager  Objects list pager
	 * @return List of names
	 * @throws FlexPayException if failure occurs
	 */
	List<TV> findNames(ArrayStack filters, Page pager) throws FlexPayException;

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @return List of Regions
	 */
	List<NTD> find(ArrayStack filters);

	/**
	 * Disable objects
	 *
	 * @param objects Objects to disable
	 * @throws FlexPayExceptionContainer if failure occurs
	 */
	void disable(Collection<NTD> objects) throws FlexPayExceptionContainer;

	/**
	 * Get name translations for temporal
	 *
	 * @param temporalId Temporal id
	 * @return Mapping from language ids to translations
	 */
	Map<Long, T> getTranslations(Long temporalId);

	/**
	 * Update object name translations
	 *
	 * @param obj			  Object to update
	 * @param temporalId	   Temporal id to apply changes for
	 * @param nameTranslations New translations
	 * @param date			 Date from which the name is valid
	 * @return updated object instance
	 * @throws FlexPayExceptionContainer exceptions container
	 */
	NTD updateNameTranslations(NTD obj, Long temporalId, List<T> nameTranslations, Date date)
			throws FlexPayExceptionContainer;

	/**
	 * Create empty name translation
	 *
	 * @return name translation
	 */
	T getEmptyNameTranslation();
}
