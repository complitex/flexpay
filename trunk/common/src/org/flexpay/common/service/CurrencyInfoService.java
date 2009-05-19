package org.flexpay.common.service;

import org.flexpay.common.persistence.CurrencyInfo;

import java.util.List;

public interface CurrencyInfoService {

	/**
	 * Find currency by its ISO code
	 *
	 * @param code Currency ISO code
	 * @return CurrencyInfo object
	 * @throws IllegalArgumentException if currency info cannot be found
	 */
	CurrencyInfo getCurrencyByCode(String code) throws IllegalArgumentException;

	/**
	 * Find default currency
	 *
	 * @return CurrencyInfo object
	 * @throws IllegalArgumentException if currency info cannot be found
	 * @see org.flexpay.common.util.config.ApplicationConfig#getDefaultCurrencyCode()
	 */
	CurrencyInfo getDefaultCurrency() throws IllegalArgumentException;

	/**
	 * List all currency infos
	 *
	 * @return Currency infos
	 */
	List<CurrencyInfo> listCurrencies();
}
