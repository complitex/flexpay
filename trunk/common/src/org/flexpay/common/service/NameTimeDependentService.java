package org.flexpay.common.service;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.*;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface NameTimeDependentService<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> extends JpaSetService {

	/**
	 * Read object by its unique id
	 *
	 * @param stub Object stub
	 * @return object, or <code>null</code> if object not found
	 */
	@Nullable
	public NTD readFull(@NotNull Stub<NTD> stub);

	/**
	 * Get temporal names
	 *
	 * @param filter Filter
	 * @return List of names
	 * @throws FlexPayException if failure occurs
	 */
	List<TV> findNames(PrimaryKeyFilter<?> filter) throws FlexPayException;

	/**
	 * Get a list of available objects
	 *
	 * @param filters Parent filters
	 * @return List of Objects
	 */
	List<NTD> find(ArrayStack filters);

}
