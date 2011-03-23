package org.flexpay.tc.service.impl;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.FPFileService;
import org.flexpay.tc.dao.TariffCalculationRulesFileDao;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;
import org.flexpay.tc.service.TariffCalculationRulesFileService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class TariffCalculationRulesFileServiceImpl implements TariffCalculationRulesFileService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private FPFileService fpFileService;
	private TariffCalculationRulesFileDao tariffCalculationRulesFileDao;

	@Transactional (readOnly = false)
	@Override
	public void create(@NotNull TariffCalculationRulesFile tariffCalculationRulesFile) {
		tariffCalculationRulesFile.setId(null);
		tariffCalculationRulesFileDao.create(tariffCalculationRulesFile);
	}

	@Transactional (readOnly = false)
	@Override
	public void update(@NotNull TariffCalculationRulesFile tariffCalculationRulesFile) {
		tariffCalculationRulesFileDao.update(tariffCalculationRulesFile);
	}

	public TariffCalculationRulesFile read(@NotNull Stub<TariffCalculationRulesFile> stub) {
		return tariffCalculationRulesFileDao.readFull(stub.getId());
	}

	@Transactional (readOnly = false)
	@Override
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
	@Override
	public void delete(TariffCalculationRulesFile file) {
		tariffCalculationRulesFileDao.delete(file);
	}

	@SuppressWarnings ({"ResultOfMethodCallIgnored"})
	@Transactional (readOnly = false)
	@Override
	public void delete(@NotNull Stub<TariffCalculationRulesFile> fileStub) {
		TariffCalculationRulesFile rulesFile = tariffCalculationRulesFileDao.read(fileStub.getId());

		if (rulesFile == null) {
			log.debug("Can't find tariff calculation rules file with id {}", fileStub.getId());
			return;
		}

		fpFileService.deleteFromFileSystem(rulesFile.getFile());
		tariffCalculationRulesFileDao.delete(rulesFile);
	}

	@Transactional (readOnly = false)
	@Override
	public void disableByIds(@NotNull Collection<Long> objectIds) throws FlexPayExceptionContainer {
		for (Long id : objectIds) {
			TariffCalculationRulesFile rulesFile = tariffCalculationRulesFileDao.read(id);
			if (rulesFile != null) {
				rulesFile.disable();
				tariffCalculationRulesFileDao.update(rulesFile);

				log.debug("Disabled: {}", rulesFile);
			}
		}
	}

	@Override
	public List<TariffCalculationRulesFile> listTariffCalculationRulesFiles(Page<TariffCalculationRulesFile> pager) {
		return tariffCalculationRulesFileDao.findTariffCalculationRulesFiles(pager);
	}

	@Required
	public void setTariffCalculationRulesFileDao(TariffCalculationRulesFileDao tariffCalculationRulesFileDao) {
		this.tariffCalculationRulesFileDao = tariffCalculationRulesFileDao;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

}
