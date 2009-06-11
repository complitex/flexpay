package org.flexpay.common.persistence.history.builder;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

/**
 * Callback interface for {@link HistoryBuilderHelper#patchTranslation(org.flexpay.common.persistence.DomainObject,
 * org.flexpay.common.persistence.history.HistoryRecord, TranslationPatcher)}
 *
 * @param <T> Object translation type
 * @param <DO> Object type
 */
public interface TranslationPatcher<T extends Translation, DO extends DomainObject> {

	/**
	 * Extract needed translation from object
	 *
	 * @param obj	  Object to get translation for
	 * @param language Language to get translation in
	 * @return Translation
	 */
	T getNotNullTranslation(DO obj, @NotNull Language language);

	/**
	 * Set translation to object
	 *
	 * @param obj Object
	 * @param tr Translation
	 * @param name Translation value
	 */
	void setTranslation(DO obj, T tr, String name);
}
