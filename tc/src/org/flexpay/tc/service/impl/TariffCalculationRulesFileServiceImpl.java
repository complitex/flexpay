package org.flexpay.tc.service.impl;

import org.flexpay.tc.service.TariffCalculationRulesFileService;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.dao.TariffCalculationRulesFileDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.List;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TariffCalculationRulesFileServiceImpl implements TariffCalculationRulesFileService {

	private TariffCalculationRulesFileDao tariffCalculationRulesFileDao;

	@Transactional (readOnly = false)
	public void save(@NotNull TariffCalculationRulesFile tariffCalculationRulesFile) {
		if (tariffCalculationRulesFile.isNew()) {
			tariffCalculationRulesFile.setId(null);
			tariffCalculationRulesFileDao.create(tariffCalculationRulesFile);
		} else {
			tariffCalculationRulesFileDao.update(tariffCalculationRulesFile);
		}
	}

	public TariffCalculationRulesFile read(@NotNull Stub<TariffCalculationRulesFile> stub) {
		return tariffCalculationRulesFileDao.readFull(stub.getId());
	}

	@Transactional (readOnly = false)
	public void disable(@NotNull Set<Long> objectIds) {
		for (Long id : objectIds) {
			TariffCalculationRulesFile tariffCalculationRulesFile = tariffCalculationRulesFileDao.read(id);
			if (tariffCalculationRulesFile != null) {
				tariffCalculationRulesFile.disable();
				tariffCalculationRulesFileDao.update(tariffCalculationRulesFile);
			}
		}
	}

	public List<TariffCalculationRulesFile> listTariffCalculationRulesFiles(Page<TariffCalculationRulesFile> pager) {
		return tariffCalculationRulesFileDao.findTariffCalculationRulesFiles(pager);
	}

	@Required
	public void setTariffCalculationRulesFileDao(TariffCalculationRulesFileDao tariffCalculationRulesFileDao) {
		this.tariffCalculationRulesFileDao = tariffCalculationRulesFileDao;
	}

}
