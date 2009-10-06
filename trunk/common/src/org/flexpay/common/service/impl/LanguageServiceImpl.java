package org.flexpay.common.service.impl;

import org.flexpay.common.dao.LanguageDao;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.service.LanguageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class LanguageServiceImpl implements LanguageService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private LanguageDao languageDao;

	/**
	 * Get a list of system languages
	 *
	 * @return list of languages
	 */
	public List<Language> getLanguages() {
		List<Language> langs = languageDao.listLanguages();

		log.debug("Loaded languages: {}", langs);

		return langs;
	}

	/**
	 * Setter for property 'languageDao'.
	 *
	 * @param languageDao Value to set for property 'languageDao'.
	 */
	public void setLanguageDao(LanguageDao languageDao) {
		this.languageDao = languageDao;
	}
}