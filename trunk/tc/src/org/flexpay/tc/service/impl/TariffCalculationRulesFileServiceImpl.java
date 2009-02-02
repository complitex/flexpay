package org.flexpay.tc.service.impl;

import org.flexpay.tc.service.TariffCalculationRulesFileService;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.dao.TariffCalculationRulesFileDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.util.FPFileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.List;
import java.io.File;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class TariffCalculationRulesFileServiceImpl implements TariffCalculationRulesFileService {

	@NonNls
	private Logger log = LoggerFactory.getLogger(getClass());

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

	@Transactional (readOnly = false)
	public void delete(TariffCalculationRulesFile file) {
		tariffCalculationRulesFileDao.delete(file);
	}

	@Transactional (readOnly = false)
	public void delete(@NotNull Long fileId) {
		TariffCalculationRulesFile rulesFile = tariffCalculationRulesFileDao.read(fileId);

		if (rulesFile == null) {
			log.debug("Can't find tariff calculation rules file with id {}", fileId);
			return;
		}

		File fileOnSystem = FPFileUtil.getFileOnServer(rulesFile.getFile());
		if (fileOnSystem != null) {
			fileOnSystem.delete();
		}
		tariffCalculationRulesFileDao.delete(rulesFile);
	}

	public List<TariffCalculationRulesFile> listTariffCalculationRulesFiles(Page<TariffCalculationRulesFile> pager) {
		return tariffCalculationRulesFileDao.findTariffCalculationRulesFiles(pager);
	}

	@Required
	public void setTariffCalculationRulesFileDao(TariffCalculationRulesFileDao tariffCalculationRulesFileDao) {
		this.tariffCalculationRulesFileDao = tariffCalculationRulesFileDao;
	}

}
