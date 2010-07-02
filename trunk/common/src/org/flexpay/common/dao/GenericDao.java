package org.flexpay.common.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * The basic GenericDao interface with CRUD methods Finders are added with interface inheritance <br /> and AOP
 * introductions for concrete implementations
 * <p/>
 * Extended interfaces may declare methods starting with find... list... iterate... or scroll...<br /> They will execute
 * a preconfigured query that is looked up based on the rest of the method name
 * <p/>
 * Extended interfaces could include Collection, Page and FetchRange parameters
 */
public interface GenericDao<T, PK extends Serializable> {

	/**
	 * Create a new persistent object
	 *
	 * @param newInstance Object to save in DB
	 * @return New object primary key
	 */
	@NotNull
	PK create(@NotNull T newInstance);

	/**
	 * Read persistent object
	 *
	 * @param id Object identifier
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	T read(@NotNull PK id);

	/**
	 * Read full persistent object info
	 *
	 * @param id Object identifier
	 * @return Object if found, or <code>null</code> otherwise
	 */
	@Nullable
	T readFull(@NotNull PK id);

	/**
	 * Read full persistent objects info
	 *
	 * @param ids Object identifiers
	 * @param preserveOrder wether to preserve order of elements
	 * @return Objects found
	 */
	@NotNull
	List<T> readFullCollection(@NotNull Collection<PK> ids, boolean preserveOrder);

	void update(@NotNull T transientObject);

	T merge(@NotNull T object);

	void delete(@NotNull T persistentObject);

}
