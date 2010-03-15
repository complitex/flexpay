package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.UserPreferencesDao;
import org.flexpay.common.dao.impl.ldap.DnBuilder;
import org.flexpay.common.dao.impl.ldap.LdapConstants;
import org.flexpay.common.dao.impl.ldap.UserPreferencesContextMapper;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.util.config.UserPreferencesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ldap.control.SortControlDirContextProcessor;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.simple.AbstractParameterizedContextMapper;
import org.springframework.ldap.core.simple.SimpleLdapTemplate;
import org.springframework.ldap.core.support.AggregateDirContextProcessor;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
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
	private DnBuilder userNameBuilder;
	private DnBuilder userGroupBuilder;
	private UserPreferencesContextMapper mapper;
	private UserPreferencesFactory userPreferencesFactory;

	private final class PersonContextMapper extends AbstractParameterizedContextMapper<UserPreferences> {

		@Override
		protected UserPreferences doMapFromContext(DirContextOperations ctx) {

			UserPreferences person = userPreferencesFactory.newInstance();
			person.setObjectClasses(CollectionUtils.set(ctx.getStringAttributes("objectclass")));
			person.attributes(attributeIds(ctx));
			log.debug("\nCollected classes: {}\nAttributes: {}", person.getObjectClasses(), person.attributes());
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
	@Override
	public void saveAllPreferences(UserPreferences person) {
		DirContextOperations ctx = ldapTemplate.lookupContext(buildDn(person));
		mapToContextUserEditedPreferences(person, ctx);
		mapToContextAdminEditedPreferences(person, ctx);
		ldapTemplate.modifyAttributes(ctx);
	}

	@Override
	public void saveUserEditedPreferences(UserPreferences person) {
		DirContextOperations ctx = ldapTemplate.lookupContext(buildDn(person));
		mapToContextUserEditedPreferences(person, ctx);
		ldapTemplate.modifyAttributes(ctx);
	}

	@Override
	public void saveAdminEditedPreferences(UserPreferences person) {
		DirContextOperations ctx = ldapTemplate.lookupContext(buildDn(person));
		mapToContextAdminEditedPreferences(person, ctx);
		ldapTemplate.modifyAttributes(ctx);
	}

	@Override
	public void updateUserPassword(UserPreferences person, String password) {
		DirContextOperations ctx = ldapTemplate.lookupContext(buildDn(person));
		mapToContextPasswordPreferences(person, password, ctx);
		ldapTemplate.modifyAttributes(ctx);
	}

	@Override
	public void delete(UserPreferences preferences) {
		ldapTemplate.unbind(preferences.getUsername());
	}

	@Override
	public List<UserPreferences> listAllUser() {
		return ldapTemplate.search("", userGroupBuilder.getNameFilter(LdapConstants.OBJECT_CLASS).encode(), new PersonContextMapper());
	}

	@Override
	public UserPreferences findByUserName(String userName) {

		List<UserPreferences> persons = ldapTemplate.search("",
				userNameBuilder.getNameFilter(userName).encode(), new PersonContextMapper());
		if (persons.isEmpty()) {
			return null;
		}
		if (persons.size() > 1) {
			throw new IllegalStateException("Found several users by user name " + userName);
		}
		return persons.get(0);
	}

	private Name buildDn(UserPreferences person) {
		return userNameBuilder.buildDn(person);
	}

	private void mapToContextUserEditedPreferences(UserPreferences preferences, DirContextOperations ctx) {
		mapper.doMapToContextUserEdited(ctx, preferences);
	}

	private void mapToContextAdminEditedPreferences(UserPreferences preferences, DirContextOperations ctx) {
		mapper.doMapToContextAdminEdited(ctx, preferences);
	}

	private void mapToContextPasswordPreferences(UserPreferences preferences, String password, DirContextOperations ctx) {
		mapper.doMapToContextPassword(ctx, preferences, password);
	}

	@Required
	public void setLdapTemplate(SimpleLdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	@Required
	public void setUserNameBuilder(DnBuilder userNameBuilder) {
		this.userNameBuilder = userNameBuilder;
	}

	@Required
	public void setUserGroupBuilder(DnBuilder userGroupBuilder) {
		this.userGroupBuilder = userGroupBuilder;
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
