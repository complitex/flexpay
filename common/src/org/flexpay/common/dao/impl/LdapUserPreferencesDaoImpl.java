package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.UserPreferencesDao;
import org.flexpay.common.dao.impl.ldap.DnBuilder;
import org.flexpay.common.dao.impl.ldap.UserPreferencesContextMapper;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.util.config.UserPreferencesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.simple.AbstractParameterizedContextMapper;
import org.springframework.ldap.core.simple.SimpleLdapTemplate;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.util.List;
import java.util.Set;

/**
 * Spring LDAP implementation of PersonDao. This implementation uses many Spring LDAP features, such as the {@link
 * DirContextAdapter}, {@link AbstractParameterizedContextMapper}, and {@link SimpleLdapTemplate}.
 *
 * @author Mattias Hellborg Arthursson
 * @author Ulrik Sandberg
 */
public class LdapUserPreferencesDaoImpl implements UserPreferencesDao {

	private Logger log = LoggerFactory.getLogger(getClass());

	private SimpleLdapTemplate ldapTemplate;
	private DnBuilder dnBuilder;
	private UserPreferencesContextMapper mapper;
	private UserPreferencesFactory userPreferencesFactory;

	private final class PersonContextMapper extends AbstractParameterizedContextMapper<UserPreferences> {

		@Override
		protected UserPreferences doMapFromContext(DirContextOperations ctx) {

			UserPreferences person = userPreferencesFactory.newInstance();
			person.setObjectClasses(CollectionUtils.set(ctx.getStringAttributes("objectclass")));
			person.attributes(attributeIds(ctx));
			if (mapper.supports(person)) {
				mapper.doMapFromContext(ctx, person);
			}

			return person;
		}

		private Set<String> attributeIds(DirContextOperations ctx) {

			try {
				Set<String> result = CollectionUtils.set();
				NamingEnumeration<String> enumeration = ctx.getAttributes().getIDs();
				while (enumeration.hasMore()) {
					result.add(enumeration.next());
				}
				enumeration.close();

				return result;
			} catch (NamingException e) {
				log.error("Failed getting attribute ids", e);
				return CollectionUtils.set();
			}
		}
	}

	/*
	 * @see PersonDao#update(Person)
	 */
	public void save(UserPreferences person) {
		DirContextOperations ctx = ldapTemplate.lookupContext(buildDn(person));
		mapToContext(person, ctx);
		ldapTemplate.modifyAttributes(ctx);
	}

	public UserPreferences findByUserName(String userName) {

		List<UserPreferences> persons = ldapTemplate.search("",
				dnBuilder.getUserNameFilter(userName).encode(), new PersonContextMapper());
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
