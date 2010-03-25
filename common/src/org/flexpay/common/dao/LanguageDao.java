package org.flexpay.common.dao;

import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface LanguageDao {

    @NotNull
	List<Language> listLanguages();

    @Nullable
    Language getLanguageByIsoCode(@NotNull String langIsoCode);
}
