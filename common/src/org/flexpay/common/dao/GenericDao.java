package org.flexpay.common.dao;

import java.io.Serializable;

/**
 * The basic GenericDao interface with CRUD methods Finders are added with interface
 * inheritance <br /> and AOP introductions for concrete implementations
 * <p/>
 * Extended interfaces may declare methods starting with find... list... iterate... or
 * scroll...<br /> They will execute a preconfigured query that is looked up based on the
 * rest of the method name
 */
public interface GenericDao<T, PK extends Serializable> {
	PK create(T newInstance);

	T read(PK id);

	T readFull(PK id);

	void update(T transientObject);

	void delete(T persistentObject);
}
