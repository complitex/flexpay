package org.flexpay.bti.service;

import org.flexpay.bti.persistence.SewerType;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.annotation.Secured;

import java.util.List;
import java.util.Set;

public interface SewerTypesService {

	@Secured({Roles.SEWER_TYPE_ADD, Roles.SEWER_TYPE_CHANGE})
	void save(@NotNull SewerType sewerType);

	@Secured(Roles.SEWER_TYPE_READ)
	SewerType getSewerType(@NotNull Stub<SewerType> stub);

	@Secured({Roles.SEWER_TYPE_DELETE})
	void disable(@NotNull Set<Long> objectIds);

	@Secured({Roles.SEWER_TYPE_READ})
	List<SewerType> listSewerTypes(Page<SewerType> pager);

}
