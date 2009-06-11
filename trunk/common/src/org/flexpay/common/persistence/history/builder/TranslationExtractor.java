package org.flexpay.common.persistence.history.builder;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Translation;
import org.jetbrains.annotations.NotNull;

/**
 * Callback interface for {@link HistoryBuilderHelper#buildTranslationDiff(org.flexpay.common.persistence.DomainObject,
 * org.flexpay.common.persistence.DomainObject, org.flexpay.common.persistence.history.Diff, TranslationExtractor)}
 *
 * @param <T> Object translation type
 * @param <DO> Object type
 */
public interface TranslationExtractor<T extends Translation, DO extends DomainObject> {

	/**
	 * Extract needed translation from object
	 *
	 * @param obj Object to get translation for
	 * @param language Language to get translation in
	 * @return Translation
	 */
	T getTranslation(DO obj, @NotNull Language language);

	/**
	 * Get translation field code
	 *
	 * @return field code
	 */
	int getTranslationField();
}

