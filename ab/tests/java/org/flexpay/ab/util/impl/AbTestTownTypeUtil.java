package org.flexpay.ab.util.impl;

import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.ab.util.TestTownTypeUtil;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.service.LanguageService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Set;

import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.util.CollectionUtils.list;

public class AbTestTownTypeUtil implements TestTownTypeUtil {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier ("townTypeService")
	private TownTypeService townTypeService;

    @Autowired
    @Qualifier ("languageService")
	private LanguageService languageService;

    @Nullable
    @Override
    public TownType create(@NotNull String name, @NotNull String shortName) {
        Language lang = languageService.getLanguage("ru");
        if (lang == null) {
            log.error("Language did not find");
            return null;
        }

        TownTypeTranslation translation = new TownTypeTranslation(name, lang);
        translation.setShortName(shortName);

        TownType newType = new TownType();
        newType.setTranslation(translation);
        try {
            return townTypeService.create(newType);
        } catch (FlexPayExceptionContainer e) {
            log.error("Town type did not create", e);
        }
        return null;
    }

    @Nullable
    @Override
    public TownTypeTranslation addTranslation(@NotNull TownType townType, @NotNull String name, @NotNull String shortName, @NotNull String langIsoCode) {
        Language lang = languageService.getLanguage(langIsoCode);
        if (lang == null) {
            log.error("Language {} did not find", langIsoCode);
            return null;
        }

        TownTypeTranslation translation = new TownTypeTranslation(name, lang);
        translation.setShortName(shortName);
        translation.setTranslatable(townType);

        Set<TownTypeTranslation> translations = townType.getTranslations();
        translations.add(translation);

        townType.setTranslations(translations);

        try {
            townTypeService.update(townType);
            return translation;
        } catch (FlexPayExceptionContainer e) {
            log.error("Town type did not update", e);
        }
        return null;
    }

    @Override
    public void delete(@NotNull TownType townType) {
        townTypeService.disable(collectionIds(list(townType)));
    }

}
