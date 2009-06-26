package org.flexpay.common.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

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

	void update(@NotNull T transientObject);

	void delete(@NotNull T persistentObject);

}
