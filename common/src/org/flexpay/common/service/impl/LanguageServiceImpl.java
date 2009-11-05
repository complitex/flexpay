package org.flexpay.common.service.impl;

import org.flexpay.common.dao.LanguageDao;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.service.LanguageService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class LanguageServiceImpl implements LanguageService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private LanguageDao languageDao;

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
	public List<Language> getLanguages() {
		List<Language> langs = languageDao.listLanguages();

		log.debug("Loaded languages: {}", langs);

		return langs;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public Language getLanguage(@NotNull String langIsoCode) {
        return languageDao.getLanguageByIsoCode(langIsoCode);
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