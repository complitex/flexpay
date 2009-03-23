package org.flexpay.ab.dao;

import org.flexpay.ab.persistence.CountryNameTranslation;
import org.flexpay.common.dao.GenericDao;
import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CountryNameTranslationDao extends GenericDao<CountryNameTranslation, Long> {

    /**
     * Returns list of countries with name <code>name</code> in language <code>language</code>
     * @param name country full name
     * @param language search language
     * @return list of countries with name <code>name</code> in language <code>language</code>
     */
    public List<CountryNameTranslation> findByName(@NotNull String name, @NotNull Language language);

    /**
     * Returns list of countries with short name <code>shortName</code> in language <code>language</code>
     * @param shortName country short name
     * @param language search language
     * @return list of countries with short name <code>shortName</code> in language <code>language</code>
     */
    public List<CountryNameTranslation> findByShortName(@NotNull String shortName, @NotNull Language language);
}
