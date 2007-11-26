package org.flexpay.common.dao;

import org.flexpay.common.persistence.Language;

import java.util.List;

public interface LanguageDao extends GenericDao<Language, Long> {

	List<Language> listLanguages();
}
