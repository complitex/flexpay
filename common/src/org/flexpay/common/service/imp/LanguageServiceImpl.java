package org.flexpay.common.service.imp;

import org.flexpay.common.persistence.Language;
import org.flexpay.common.dao.LanguageDao;
import org.flexpay.common.service.LanguageService;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;

import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class LanguageServiceImpl implements LanguageService {

	private Logger log = Logger.getLogger(getClass());

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