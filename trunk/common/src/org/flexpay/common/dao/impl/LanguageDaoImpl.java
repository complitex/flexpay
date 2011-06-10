package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.LanguageDao;
import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;

import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.uniqueResult;

public class LanguageDaoImpl implements LanguageDao {

	protected JpaTemplate jpaTemplate;

    @NotNull
	@SuppressWarnings ({"unchecked"})
    @Override
	public List<Language> listLanguages() {
		return jpaTemplate.findByNamedQuery("Language.listLanguages");
	}

    @Nullable
    @Override
    public Language getLanguageByIsoCode(@NotNull String langIsoCode) {
        return (Language) uniqueResult((List<?>) jpaTemplate.findByNamedQuery("Language.getLanguageByIsoCode", langIsoCode));
    }

    @Required
	public void setJpaTemplate(JpaTemplate jpaTemplate) {
		this.jpaTemplate = jpaTemplate;
	}
}
