package org.flexpay.common.service;

import org.flexpay.common.persistence.Language;

import java.util.List;

public interface LanguageService {

	/**
	 * Get a list of system languages
	 *
	 * @return list of languages
	 */
	public List<Language> getLanguages();
}
