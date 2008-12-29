package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.LanguageDao;
import org.flexpay.common.persistence.Language;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.util.List;

public class LanguageDaoImpl implements LanguageDao {

	protected HibernateTemplate hibernateTemplate;

	@SuppressWarnings ({"unchecked"})
	public List<Language> listLanguages() {
		return hibernateTemplate.findByNamedQuery("Language.listLanguages");
	}

	/**
	 * Setter for property 'hibernateTemplate'.
	 *
	 * @param hibernateTemplate Value to set for property 'hibernateTemplate'.
	 */
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
