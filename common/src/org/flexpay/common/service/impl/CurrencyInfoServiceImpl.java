package org.flexpay.common.service.impl;

import org.flexpay.common.dao.CurrencyInfoDao;
import org.flexpay.common.persistence.CurrencyInfo;
import org.flexpay.common.service.CurrencyInfoService;
import org.flexpay.common.util.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class CurrencyInfoServiceImpl implements CurrencyInfoService {

	private CurrencyInfoDao currencyInfoDao;

	/**
	 * Find currency by its ISO code
	 *
	 * @param code Currency ISO code
	 * @return CurrencyInfo object
	 * @throws IllegalArgumentException if currency info cannot be found
	 */
	public CurrencyInfo getCurrencyByCode(String code) throws IllegalArgumentException {
		for (CurrencyInfo info : listCurrencies()) {
			if (code.equals(info.getCurrencyIsoCode())) {
				return info;
			}
		}

		throw new IllegalArgumentException("Cannot find currency by code: " + code);
	}

	/**
	 * Find default currency
	 *
	 * @return CurrencyInfo object
	 * @throws IllegalArgumentException if currency info cannot be found
	 * @see org.flexpay.common.util.config.ApplicationConfig#getDefaultCurrencyCode()
	 */
	public CurrencyInfo getDefaultCurrency() throws IllegalArgumentException {
		return getCurrencyByCode(ApplicationConfig.getDefaultCurrencyCode());
	}

	/**
	 * List all currency infos
	 *
	 * @return Currency infos
	 */
	public List<CurrencyInfo> listCurrencies() {
		return currencyInfoDao.listCurrencies();
	}

	@Required
	public void setCurrencyInfoDao(CurrencyInfoDao currencyInfoDao) {
		this.currencyInfoDao = currencyInfoDao;
	}
}
