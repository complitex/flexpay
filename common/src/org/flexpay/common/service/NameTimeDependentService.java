package org.flexpay.common.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.*;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface NameTimeDependentService<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> {

	/**
	 * Create new NameTimeDependent with a single name
	 *
	 * @param object		   New transient object
	 * @param nameTranslations name translations
	 * @param filters		  parent filters
	 * @param from			 Date from which the name is valid
	 * @return persisted object
	 * @throws FlexPayExceptionContainer if operation fails
	 */
	NTD create(NTD object, List<T> nameTranslations, ArrayStack filters, Date from)
			throws FlexPayExceptionContainer;

	/**
	 * Read object by its unique id
	 *
	 * @param stub Object stub
	 * @return object, or <code>null</code> if object not found
	 */
	@Nullable
	public NTD readFull(@NotNull Stub<NTD> stub);

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
	 * @param pager   Objects list pager
	 * @return List of names
	 * @throws FlexPayException if failure occurs
	 */
	List<TV> findNames(ArrayStack filters, Page pager) throws FlexPayException;

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @return List of Objects
	 */
	List<NTD> find(ArrayStack filters);

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @param pager Page
	 * @return List of Objects
	 */
	List<NTD> find(ArrayStack filters, Page pager);

	/**
	 * Disable objects
	 *
	 * @param objects Objects to disable
	 * @throws FlexPayExceptionContainer if failure occurs
	 */
	void disable(Collection<NTD> objects) throws FlexPayExceptionContainer;

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

	/**
	 * Find existing object by name
	 *
	 * @param name	 Object name to search
	 * @param filter Parent object filter
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@NotNull
	List<NTD> findByName(String name, PrimaryKeyFilter filter);
}
