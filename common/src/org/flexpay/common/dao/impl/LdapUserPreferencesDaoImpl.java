package org.flexpay.common.dao.impl;

import org.flexpay.common.dao.UserPreferencesDao;
import org.flexpay.common.dao.impl.ldap.*;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.util.config.UserPreferencesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.simple.AbstractParameterizedContextMapper;
import org.springframework.ldap.core.simple.SimpleLdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
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

	private UserPreferencesDnBuilder userNameBuilder;
	private UserPreferencesDnBuilder userGroupBuilder;
	private UserPreferencesDnBuilder accessPermissionsBuilder;

	private DnBuilder peopleBuilder;
	private DnBuilder groupsBuilder;

	private UserPreferencesContextMapper mapper;
	private UserPreferencesFactory userPreferencesFactory;

	private String url;
	private String base;

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

	private final class AccessPermissionsContextMapper extends AbstractParameterizedContextMapper<String> {

		@Override
		protected String doMapFromContext(DirContextOperations ctx) {
			log.debug("Access permission dn: {}", ctx.getDn());
			return "cn=" + ctx.getStringAttribute("cn") +",ou=groups,dc=opensso,dc=java,dc=net";
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
	public boolean checkUserPassword(UserPreferences person, String password) {
		//AndFilter filter = new AndFilter();
		//filter.and(userGroupBuilder.getNameFilter(LdapConstants.OBJECT_CLASS)).and(userNameBuilder.getNameFilter(person.getUsername()));

		// Construction du DN
		DistinguishedName dn = new DistinguishedName(base);
		dn.append(new DistinguishedName(buildDn(person)));

		// Connexion manuelle
		LdapContextSource ctxSource = new LdapContextSource();
		ctxSource.setUrl(url);
		ctxSource.setUserDn(dn.encode());
		ctxSource.setPassword(password);
		ctxSource.setPooled(false);
		try {
			ctxSource.afterPropertiesSet();
			DirContext context = ctxSource.getReadWriteContext();
			context.close();
			return true;
		} catch(Exception e) {
			log.debug("Missing create ldap connection: urls={}, userDn={}, {}", new Object[]{url, dn.encode(), e});
			return false;
		}
		//return ldapTemplate.getLdapOperations().authenticate(DistinguishedName.EMPTY_PATH, filter.encode(), password);
	}

	@Override
	public void changeUserRole(UserPreferences person) {
		DirContextOperations ctx = ldapTemplate.lookupContext(buildDn(person));
		List<String> accessPermissions;
		if (person.getUserRole() != null) {
			accessPermissions = ldapTemplate.search(groupsBuilder.buildDn(),
					accessPermissionsBuilder.getNameFilter(person.getUserRole().getExternalId()).encode(), new AccessPermissionsContextMapper());
			log.debug("Found permissions: {}", accessPermissions);
		} else {
			accessPermissions = CollectionUtils.list();
			log.debug("Remove permissions");
		}

		mapToContextAccessPermissionsPreferences(person, ctx, accessPermissions);
		ldapTemplate.modifyAttributes(ctx);
	}

	@Override
	public void delete(UserPreferences preferences) {
		ldapTemplate.unbind(preferences.getUsername());
	}

	@Override
	public List<UserPreferences> listAllUser() {
		return ldapTemplate.search(peopleBuilder.buildDn(), userGroupBuilder.getNameFilter(LdapConstants.OBJECT_CLASS).encode(), new PersonContextMapper());
	}

	@Override
	public UserPreferences findByUserName(String userName) {

		List<UserPreferences> persons = ldapTemplate.search(peopleBuilder.buildDn(),
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
		DistinguishedName dn = new DistinguishedName();
		dn.append(new DistinguishedName(peopleBuilder.buildDn())).
				append(new DistinguishedName(userNameBuilder.buildDn(person)));
		return dn;
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

	private void mapToContextAccessPermissionsPreferences(UserPreferences preferences, DirContextOperations ctx, List<String> permissions) {
		mapper.doMapToContextAccessPermissions(ctx, preferences, permissions);
	}

	@Required
	public void setLdapTemplate(SimpleLdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	@Required
	public void setUserNameBuilder(UserPreferencesDnBuilder userNameBuilder) {
		this.userNameBuilder = userNameBuilder;
	}

	@Required
	public void setUserGroupBuilder(UserPreferencesDnBuilder userGroupBuilder) {
		this.userGroupBuilder = userGroupBuilder;
	}

	public void setAccessPermissionsBuilder(UserPreferencesDnBuilder accessPermissionsBuilder) {
		this.accessPermissionsBuilder = accessPermissionsBuilder;
	}

	public void setPeopleBuilder(DnBuilder peopleBuilder) {
		this.peopleBuilder = peopleBuilder;
	}

	public void setGroupsBuilder(DnBuilder groupsBuilder) {
		this.groupsBuilder = groupsBuilder;
	}

	@Required
	public void setMapper(UserPreferencesContextMapper mapper) {
		this.mapper = mapper;
	}

	@Required
	public void setUserPreferencesFactory(UserPreferencesFactory userPreferencesFactory) {
		this.userPreferencesFactory = userPreferencesFactory;
	}

	@Required
	public void setUrl(String url) {
		this.url = url;
	}

	@Required
	public void setBase(String base) {
		this.base = base;
	}
}
