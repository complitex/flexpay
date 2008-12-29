package org.flexpay.common.actions;

import org.flexpay.common.persistence.*;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

/**
 * Utilities class
 */
public class ActionUtil {

	/**
	 * Create mapping of object translations from language ids to translations
	 *
	 * @param temporaryName Temporary name to get mapping from
	 * @param <TV>          Temporary value type
	 * @param <T>           Translation type

	 */
	@NotNull
	public static <TV extends TemporaryValue, T extends Translation>
	Map<Long, String> getLangIdsToTranslations(@NotNull TemporaryName<TV, T> temporaryName) {

		return getLangIdsToTranslations(temporaryName.getTranslations());
	}

	/**
	 * Create mapping of object translations from language ids to translations
	 *
	 * @param temporaryType Temporary type to get mapping from
	 * @param <TV>          Temporary value type
	 * @param <T>           Translation type
	 * @return map of language ids to translations
	 */
	@NotNull
	public static <TV extends TemporaryValue, T extends Translation>
	Map<Long, String> getLangIdsToTranslations(@NotNull TemporaryType<TV, T> temporaryType) {

		return getLangIdsToTranslations(temporaryType.getTranslations());
	}

	@NotNull
	private static <T extends Translation>
	Map<Long, String> getLangIdsToTranslations(@NotNull Collection<T> translations) {

		Map<Long, String> result = CollectionUtils.map();
		for (T name : translations) {
			result.put(name.getLang().getId(), name.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (result.containsKey(lang.getId())) {
				continue;
			}
			result.put(lang.getId(), "");
		}
		return result;
	}
}
