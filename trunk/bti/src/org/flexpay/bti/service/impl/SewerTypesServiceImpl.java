package org.flexpay.bti.service.impl;

import org.flexpay.bti.dao.SewerTypeDao;
import org.flexpay.bti.persistence.SewerType;
import org.flexpay.bti.service.SewerTypesService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class SewerTypesServiceImpl implements SewerTypesService {

	private SewerTypeDao sewerTypeDao;

	@Transactional (readOnly = false)
	public void save(@NotNull SewerType sewerType) {
		if (sewerType.isNew()) {
			sewerType.setId(null);
			sewerTypeDao.create(sewerType);
		} else {
			sewerTypeDao.update(sewerType);
		}
	}

	public SewerType getSewerType(@NotNull Stub<SewerType> stub) {
		return sewerTypeDao.readFull(stub.getId());
	}

	@Transactional (readOnly = false)
	public void disable(@NotNull Set<Long> objectIds) {
		for (Long id : objectIds) {
			SewerType sewerType = sewerTypeDao.read(id);
			if (sewerType != null) {
				sewerType.disable();
				sewerTypeDao.update(sewerType);
			}
		}
	}

	public List<SewerType> listSewerTypes(Page<SewerType> pager) {
		return sewerTypeDao.findSewerTypes(pager);
	}

	@Required
	public void setSewerTypeDao(SewerTypeDao sewerTypeDao) {
		this.sewerTypeDao = sewerTypeDao;
	}

}
