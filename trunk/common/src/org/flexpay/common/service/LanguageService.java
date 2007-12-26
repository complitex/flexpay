package org.flexpay.common.service;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.dao.LanguageDao;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;

import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class LanguageService {

	private static Logger log = Logger.getLogger(LanguageService.class);

	private LanguageDao languageDao;

	/**
	 * Get a list of system languages
	 *
	 * @return list of languages
	 */
	public List<Language> getLanguages() {
		List<Language> langs = languageDao.listLanguages();

		if (log.isDebugEnabled()) {
			log.debug("Loaded languages: " + langs);
		}

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
