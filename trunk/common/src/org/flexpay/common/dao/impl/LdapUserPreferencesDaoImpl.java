package org.flexpay.common.dao.impl;

import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.simple.AbstractParameterizedContextMapper;
import org.springframework.ldap.core.simple.SimpleLdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.beans.factory.annotation.Required;
import org.flexpay.common.dao.UserPreferencesDao;
import org.flexpay.common.dao.impl.ldap.DnBuilder;
import org.flexpay.common.dao.impl.ldap.UserPreferencesContextMapper;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.util.config.UserPreferencesFactory;
import org.flexpay.common.util.CollectionUtils;

/**
 * Spring LDAP implementation of PersonDao. This implementation uses many Spring
 * LDAP features, such as the {@link DirContextAdapter},
 * {@link AbstractParameterizedContextMapper}, and {@link SimpleLdapTemplate}.
 * 
 * @author Mattias Hellborg Arthursson
 * @author Ulrik Sandberg
 */
public class LdapUserPreferencesDaoImpl implements UserPreferencesDao {

	private SimpleLdapTemplate ldapTemplate;
	private DnBuilder dnBuilder;
	private UserPreferencesContextMapper mapper;
	private UserPreferencesFactory userPreferencesFactory;

	private final class PersonContextMapper extends AbstractParameterizedContextMapper<UserPreferences> {

		@Override
		protected UserPreferences doMapFromContext(DirContextOperations ctx) {

			UserPreferences person = userPreferencesFactory.newInstance();
			person.setObjectClasses(CollectionUtils.set(ctx.getStringAttributes("objectclass")));
			if (mapper.supports(person)) {
				mapper.doMapFromContext(ctx, person);
			}

			return person;
		}
	}

	/*
	 * @see PersonDao#update(Person)
	 */
	public void update(UserPreferences person) {
		DirContextOperations ctx = ldapTemplate.lookupContext(buildDn(person));
		mapToContext(person, ctx);
		ldapTemplate.modifyAttributes(ctx);
	}

	public UserPreferences findByUserName(String userName) {

		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "flexpayPerson"));
		filter.and(dnBuilder.getUserNameFilter(userName));
		List<UserPreferences> persons = ldapTemplate.search("", filter.encode(), new PersonContextMapper());
		if (persons.isEmpty()) {
			return null;
		}
		if (persons.size() > 1) {
			throw new IllegalStateException("Found several users by user name " + userName);
		}
		return persons.get(0);
	}

	private Name buildDn(UserPreferences person) {
		return dnBuilder.buildDn(person);
	}

	private void mapToContext(UserPreferences preferences, DirContextOperations ctx) {
		mapper.doMapToContext(ctx, preferences);
	}

	@Required
	public void setLdapTemplate(SimpleLdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	@Required
	public void setDnBuilder(DnBuilder dnBuilder) {
		this.dnBuilder = dnBuilder;
	}

	@Required
	public void setMapper(UserPreferencesContextMapper mapper) {
		this.mapper = mapper;
	}

	@Required
	public void setUserPreferencesFactory(UserPreferencesFactory userPreferencesFactory) {
		this.userPreferencesFactory = userPreferencesFactory;
	}
}
