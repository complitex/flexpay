package org.flexpay.common.service;

import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface LanguageService {

	/**
	 * Get a list of system languages
	 *
	 * @return list of languages
	 */
    @NotNull
	public List<Language> getLanguages();

    /**
	 * Get language  by ISO code
	 *
        * @param langIsoCode ISO code
	 * @return  language or <code>null</code>
	 */
	@Nullable
    public Language getLanguage(@NotNull String langIsoCode);
}
