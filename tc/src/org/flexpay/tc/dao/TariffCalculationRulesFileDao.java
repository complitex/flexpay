package org.flexpay.tc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.tc.persistence.TariffCalculationRulesFile;

import java.util.List;

public interface TariffCalculationRulesFileDao extends GenericDao<TariffCalculationRulesFile, Long> {

	List<TariffCalculationRulesFile> findTariffCalculationRulesFiles(Page<TariffCalculationRulesFile> pager);

}
