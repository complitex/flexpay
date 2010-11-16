package org.flexpay.common.dao.impl;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.iplanet.sso.SSOTokenManager;
import com.sun.identity.entitlement.ApplicationManager;
import com.sun.identity.entitlement.PrivilegeManager;
import com.sun.identity.policy.*;
import com.sun.identity.policy.interfaces.Subject;
import com.sun.identity.security.cert.AMCertStore;
import com.sun.identity.security.cert.AMLDAPCertStoreParameters;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.actions.security.opensso.PolicyUtils;
import org.flexpay.common.dao.UserPreferencesDao;
import org.flexpay.common.dao.impl.ldap.DnBuilder;
import org.flexpay.common.dao.impl.ldap.LdapConstants;
import org.flexpay.common.dao.impl.ldap.UserPreferencesContextMapper;
import org.flexpay.common.dao.impl.ldap.UserPreferencesDnBuilder;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.config.UserPreferences;
import org.flexpay.common.util.config.UserPreferencesFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.simple.AbstractParameterizedContextMapper;
import org.springframework.ldap.core.simple.SimpleLdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.OrFilter;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import static java.security.cert.CertificateFactory.getInstance;

/**
 * Spring LDAP implementation of PersonDao. This implementation uses many Spring LDAP features, such as the {@link
 * DirContextAdapter}, {@link AbstractParameterizedContextMapper}, and {@link SimpleLdapTemplate}.
 *
 * @author Mattias Hellborg Arthursson
 * @author Ulrik Sandberg
 */
public class LdapUserPreferencesDaoImpl implements UserPreferencesDao {
	private static final String POLICY_SUBJECT = "AMIdentitySubject";

	private Logger log = LoggerFactory.getLogger(getClass());

	private SimpleLdapTemplate ldapTemplate;
	private AMLDAPCertStoreParameters ldapCertStoreParameters;

	private UserPreferencesDnBuilder userNameBuilder;
	private UserPreferencesDnBuilder userGroupBuilder;
	private UserPreferencesDnBuilder accessPermissionsBuilder;

	private DnBuilder peopleBuilder;
	private DnBuilder groupsBuilder;

	private UserPreferencesContextMapper mapper;
	private UserPreferencesFactory userPreferencesFactory;

	private String url;
	private String base;
	private List<String> policyNames;

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
			return "cn=" + ctx.getStringAttribute("cn") +",ou=groups," + base;
		}
	}

	@Override
	public boolean createNewUser(UserPreferences person, String password) {

		if (!checkPolicy()) {
			return false;
		}

		Name dn = buildDn(person);
		DirContextOperations ctx = new DirContextAdapter(dn);
		mapToContextNewUser(person, ctx);
		mapToContextAdminEditedPreferences(person, ctx);
		mapToContextPasswordPreferences(person, password, ctx);
		ctx.getAttributes();
		if (person.getUserRole() != null) {
			List<String> accessPermissions = ldapTemplate.search(groupsBuilder.buildDn(null),
					accessPermissionsBuilder.getNameFilter(person.getUserRole().getExternalId()).encode(), new AccessPermissionsContextMapper());
			log.debug("Found permissions: {}", accessPermissions);
			mapToContextAccessPermissionsPreferences(person, ctx, accessPermissions);
		}
		ldapTemplate.bind(dn, ctx, null);
		try {
			addUserToPolicy(person);
		} catch (Exception e) {
			log.warn("Created user. Could not update policy {}", policyNames);
			log.warn("Exception: ", e);
		}
		return true;
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
			accessPermissions = ldapTemplate.search(groupsBuilder.buildDn(null),
					accessPermissionsBuilder.getNameFilter(person.getUserRole().getExternalId()).encode(), new AccessPermissionsContextMapper());
			log.debug("Found permissions: {}", accessPermissions);
		} else {
			accessPermissions = CollectionUtils.list();
			log.debug("Remove permissions");
		}

		mapToContextAccessPermissionsPreferences(person, ctx, accessPermissions);
		ldapTemplate.modifyAttributes(ctx);
	}

	@SuppressWarnings ({"unchecked"})
	@Override
	public boolean delete(@NotNull String userName) {
		SSOToken ssoToken = null;
		try {
			ssoToken = PolicyUtils.getToken();
			log.debug("TokenId: {}", ssoToken.getTokenID());
			PolicyManager pm = new PolicyManager(ssoToken);
			
			for (String policyName : policyNames) {
				try {
					Policy policy = pm.getPolicy(policyName);
					Set<String> subjectNames = policy.getSubjectNames();
					for (String subjectName : subjectNames) {
						Subject subject = policy.getSubject(subjectName);
						Set<Object> deletedSubjectValues = CollectionUtils.set();
						for (Object val : subject.getValues()) {
							if (StringUtils.equals(userName, getUserId((String)val))) {
								deletedSubjectValues.add(val);
								log.debug("Subject value '{}' deleted", val);
							} else {
								log.debug("Subject value '{}' skip", val);
							}
						}
						if (deletedSubjectValues.size() > 0) {
							Set<Object> values = subject.getValues();
							values.removeAll(deletedSubjectValues);
							subject.setValues(values);
							log.debug("Replace subject values: {}", subject.getValues());
							policy.replaceSubject(subjectName, subject, policy.isSubjectExclusive(subjectName));
							try {
								pm.replacePolicy(policy);
							} catch (NullPointerException e) {
								// TODO by FP-1166 Ignore null pointer exception
							}
						}
					}
				} catch (NameNotFoundException e) {
					log.warn("OpenSSO policy name not found {}: {}", policyName, e);
				} catch (InvalidNameException e) {
					log.warn("Invalid OpenSSO policy name {}", policyName);
				}
			}
		} catch (Exception e) {
			log.warn("Can not remove user from policy`s subject", e);
			return false;
		} finally {
			if (ssoToken != null) {
				try {
					SSOTokenManager.getInstance().destroyToken(ssoToken);
				} catch (SSOException e) {
					log.warn("Failed destroy token", e);
				}
			}
		}
		UserPreferences person = userPreferencesFactory.newInstance();
		person.setUsername(userName);
		ldapTemplate.unbind(buildDn(person));
		return true;
	}

	@Override
	public List<UserPreferences> listAllUser() {
		try {
			List<String> userNames = getUserNames();

			if (userNames.isEmpty()) {
				log.debug("Empty user list in policy {}", policyNames);
				return CollectionUtils.list();
			}

			log.debug("User names in policy {}: {}", policyNames, userNames);

			return ldapTemplate.search(peopleBuilder.buildDn(null), getUserFilter(userNames).encode(), new PersonContextMapper());
		} catch (SSOException e) {
			log.error("Failed init policy manager", e);
		} catch (PolicyException e) {
			log.error("Failed init policy manager", e);
		} catch (Exception e) {
			log.error("Can`t create SSOToken", e);
		}
		return CollectionUtils.list();
	}

	@Override
	public Certificate editCertificate(UserPreferences person, String description, Boolean blocked, InputStream inputStreamCertificate) {

		if (inputStreamCertificate == null && person.getCertificate() == null) {
			log.warn("Empty person`s certificate and input stream certificate is null");
			return null;
		}

		if (inputStreamCertificate == null && person.getCertificate() != null) {
			org.flexpay.common.persistence.Certificate certificate = person.getCertificate();
			certificate.setDescription(description);
			if (blocked != null) {
				certificate.setBlocked(blocked);
			}

			DirContextOperations ctx = ldapTemplate.lookupContext(buildDn(person));
			mapToContextCertificate(person, ctx, inputStreamCertificate, false);
			ldapTemplate.modifyAttributes(ctx);

			log.debug("Edited only description");
			return getCertificate(person);
		}
		
		byte[] certificateData = new byte[ApplicationConfig.getMaxCertificateSize()];
		int countReadData = 0;
		try {
			if ((countReadData = inputStreamCertificate.read(certificateData)) == ApplicationConfig.getMaxCertificateSize()) {
				log.warn("Read max certificate size ({}) from stream", ApplicationConfig.getMaxCertificateSize());
			}
			if (countReadData > 0) {
				certificateData = ArrayUtils.subarray(certificateData, 0, countReadData);
			} else {
				log.warn("Read empty certificate data");
				certificateData = null;
			}
		} catch (IOException e) {
			log.error("Read certificate");
			certificateData = null;
		}

		if (certificateData == null) {
			return null;
		}

		log.debug("Read certificate. Size={}", countReadData);

		inputStreamCertificate = new ByteArrayInputStream(certificateData);

		X509Certificate javaCertificate = null;
		try {
			CertificateFactory certificateFactory = getInstance("X.509");
			//inputStreamCertificate.read();
			javaCertificate = (X509Certificate) certificateFactory.generateCertificate(inputStreamCertificate);
		} catch (Exception e) {
			log.debug("Generate certificate", e);
		}

		if (javaCertificate == null) {
			return null;
		}

		org.flexpay.common.persistence.Certificate certificate =
					new org.flexpay.common.persistence.Certificate(person, description, javaCertificate.getNotBefore(), javaCertificate.getNotAfter());
		if (blocked != null) {
			certificate.setBlocked(blocked);
		}
		person.setCertificate(certificate);

		inputStreamCertificate = new ByteArrayInputStream(certificateData);

		DirContextOperations ctx = ldapTemplate.lookupContext(buildDn(person));
		mapToContextCertificate(person, ctx, inputStreamCertificate, false);
		ldapTemplate.modifyAttributes(ctx);

		log.debug("Success edited person`s certificate: {}", certificate);

		return javaCertificate;
	}

	@Override
	public Certificate getCertificate(UserPreferences preferences) {
		try {
			return AMCertStore.getCertificate(ldapCertStoreParameters, "uid", preferences.getUsername());
		} catch (Exception e) {
			log.error("Error load certificate from ldap", e);
		}
		return null;
	}

	@Override
	public void deleteCertificate(UserPreferences person) {
		DirContextOperations ctx = ldapTemplate.lookupContext(buildDn(person));
		mapToContextCertificate(person, ctx, null, true);
		ldapTemplate.modifyAttributes(ctx);
	}

	@Override
	public boolean isCertificateExist(UserPreferences preferences) {
		return preferences.getCertificate() != null;
	}

	@Override
	public UserPreferences findByUserName(String userName) {

		List<UserPreferences> persons = ldapTemplate.search(peopleBuilder.buildDn(null),
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
		dn.append(new DistinguishedName(peopleBuilder.buildDn(null))).
				append(new DistinguishedName(userNameBuilder.buildDn(person)));
		return dn;
	}

	private String getUserId(String userDn) {
		StringTokenizer stz = new StringTokenizer(userDn, ",");
		String id = null;
		boolean isUser = false;
		while (stz.hasMoreTokens()) {
			String nameValue = stz.nextToken().trim();
			int index = nameValue.indexOf("=");
			if (index == -1) {
				continue;
			}
			String name = nameValue.substring(0, index).trim();
			String value = nameValue.substring(index + 1).trim();
			if ("id".equals(name)) {
				id = value;
			} else if ("ou".equals(name) && "user".equals(value)) {
				isUser = true;
			}
		}
		return isUser? id: null;
	}

	private AndFilter getUserFilter(List<String> userNames) {
		AndFilter resultFilter = new AndFilter();
		resultFilter.and(userGroupBuilder.getNameFilter(LdapConstants.FLEXPAY_PERSON_OBJECT_CLASS));
		OrFilter uidFilter = new OrFilter();
		for (String name : userNames) {
			uidFilter.or(userNameBuilder.getNameFilter(name));
		}
		resultFilter.and(uidFilter);
		log.debug("List all user filter: {}", resultFilter);

		return resultFilter;
	}

	@SuppressWarnings ({"unchecked"})
	private List<String> getUserNames() throws Exception {
		SSOToken ssoToken = null;
		try {
			ssoToken = PolicyUtils.getToken();
			log.debug("TokenId: {}", ssoToken.getTokenID());
			PolicyManager pm = new PolicyManager(ssoToken);
			
			List<String> userNames = CollectionUtils.list();
			for (String policyName : policyNames) {
				try {
					Policy policy = pm.getPolicy(policyName);
					Set<String> subjectNames = policy.getSubjectNames();
					for (String subjectName : subjectNames) {
						for (Object val : policy.getSubject(subjectName).getValues()) {
							log.debug("Subject value {} ({})", val, val.getClass());
							String userName = getUserId((String)val);
							if (userName != null) {
								userNames.add(userName);
							}
						}
					}
				} catch (NameNotFoundException e) {
					log.warn("OpenSSO policy name not found {}: {}", policyName, e);
				} catch (InvalidNameException e) {
					log.warn("Invalid OpenSSO policy name {}", policyName);
				}
			}
			return userNames;
		} finally {
			if (ssoToken != null) {
				SSOTokenManager.getInstance().destroyToken(ssoToken);
			}
		}
	}

	private boolean checkPolicy() {
		SSOToken ssoToken = null;
		String currentPolicyName = null;
		try {
			ssoToken = PolicyUtils.getToken();
			log.debug("TokenId: {}", ssoToken.getTokenID());
			PolicyManager pm = new PolicyManager(ssoToken);
			for (String policyName : policyNames) {
				currentPolicyName = policyName;
				Policy policy = pm.getPolicy(policyName);
				if (policy == null) {
					log.error("Null policy '{}'", policyName);
					return false;
				}
			}
			return true;
		} catch (Throwable t) {
			log.error("Failed check policy '{}'", currentPolicyName);
			log.error("Exception: ", t);
		} finally {
			if (ssoToken != null) {
				try {
					SSOTokenManager.getInstance().destroyToken(ssoToken);
				} catch (SSOException e) {
					log.warn("Failed destroy token", e);
				}
			}
		}
		return false;
	}

	@SuppressWarnings ({"unchecked"})
	private void addUserToPolicy(UserPreferences person) throws Exception {
		SSOToken ssoToken = null;
		try {
			ssoToken = PolicyUtils.getToken();
			log.debug("TokenId: {}", ssoToken.getTokenID());
			PolicyManager pm = new PolicyManager(ssoToken);

			for (String policyName : policyNames) {
				log.debug("Edit policy {}", policyName);
				Policy policy = pm.getPolicy(policyName);
				Set<String> subjectNames = policy.getSubjectNames();
				Subject subject;
				String existSubjectName = null;
				if (subjectNames.size() > 0) {
					existSubjectName = subjectNames.iterator().next();
					subject = policy.getSubject(existSubjectName);
				} else {
					subject = pm.getSubjectTypeManager().getSubject(POLICY_SUBJECT);
				}
				log.debug("Exist subject name {}", existSubjectName);
				Set<String> values = subject.getValues();
				if (values == null) {
					values = CollectionUtils.set();
				}
				values.add("id=" + person.getUsername() + ",ou=user," + base);
				log.debug("Subject values: {}", values);
				subject.setValues(values);
				if (existSubjectName != null) {
					policy.replaceSubject(existSubjectName, subject, policy.isSubjectExclusive(existSubjectName));
				} else {
					policy.addSubject("users", subject, true);
				}
				try {
					pm.replacePolicy(policy);
				} catch (NullPointerException e) {
					// TODO by FP-1166 Ignore null pointer exception
				}
			}
		} finally {
			if (ssoToken != null) {
				SSOTokenManager.getInstance().destroyToken(ssoToken);
			}
		}
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

	private void mapToContextNewUser(UserPreferences preferences, DirContextOperations ctx) {
		mapper.doMapToContextNewUser(ctx, preferences);
	}

	private void mapToContextCertificate(UserPreferences preferences, DirContextOperations ctx, InputStream inputStream, boolean delete) {
		mapper.doMapToContextCertificate(ctx, preferences, inputStream, delete);
	}

	@Required
	public void setPolicyNames(String policyNames) {
		StringTokenizer stz = new StringTokenizer(policyNames, "|");
		this.policyNames = CollectionUtils.list();
		while(stz.hasMoreTokens()) {
			String token = stz.nextToken().trim();
			this.policyNames.add(token);
		}
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

	@Required
	public void setLdapCertStoreParameters(AMLDAPCertStoreParameters ldapCertStoreParameters) {
		this.ldapCertStoreParameters = ldapCertStoreParameters;
	}
}
