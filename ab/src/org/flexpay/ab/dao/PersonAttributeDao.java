package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.PersonAttribute;
import org.flexpay.common.dao.GenericDao;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PersonAttributeDao extends GenericDao<Long, PersonAttribute> {

	@NotNull
	List<PersonAttribute> listAttributes(@NotNull Long personId);
}
