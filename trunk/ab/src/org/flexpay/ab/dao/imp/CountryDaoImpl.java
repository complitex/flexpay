package org.flexpay.ab.dao.imp;

import org.flexpay.ab.dao.CountryDao;
import org.flexpay.ab.persistence.Country;
import org.flexpay.common.dao.impl.GenericDaoHibernateImpl;

import java.util.List;

@SuppressWarnings ({"unchecked"})
public class CountryDaoImpl extends GenericDaoHibernateImpl<Country, Long> implements CountryDao {

	public CountryDaoImpl(Class<Country> type) {
		super(type);
	}

	public List<Country> getCountries() {
		return hibernateTemplate.loadAll(Country.class);
	}


}
