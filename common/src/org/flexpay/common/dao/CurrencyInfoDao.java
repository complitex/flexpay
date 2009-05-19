package org.flexpay.common.dao;

import org.flexpay.common.persistence.CurrencyInfo;

import java.util.List;

public interface CurrencyInfoDao extends GenericDao<CurrencyInfo, Long> {

	List<CurrencyInfo> listCurrencies();
}
