package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.LanguageDao;
import org.flexpay.common.persistence.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.List;

import static org.springframework.dao.support.DataAccessUtils.uniqueResult;

public class LanguageDaoImpl implements LanguageDao {

	protected HibernateTemplate hibernateTemplate;

    @NotNull
	@SuppressWarnings ({"unchecked"})
    @Override
	public List<Language> listLanguages() {
		return hibernateTemplate.findByNamedQuery("Language.listLanguages");
	}

    @Nullable
    @Override
    public Language getLanguageByIsoCode(@NotNull String langIsoCode) {
        return (Language) uniqueResult((List<?>) hibernateTemplate.findByNamedQuery("Language.getLanguageByIsoCode", langIsoCode));
    }

    @Required
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
