package org.flexpay.common.dao.impl.ldap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.persistence.UserRole;
import org.flexpay.common.service.UserRoleService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.common.util.config.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ldap.core.DirContextOperations;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Set;

public class CommonUserPreferencesContextMapper implements UserPreferencesContextMapper {

	private Logger log = LoggerFactory.getLogger(getClass());

	private UserRoleService userRoleService;

    @Override
	public UserPreferences doMapFromContext(DirContextOperations ctx, UserPreferences preferences) {

		preferences.setFullName(ctx.getStringAttribute("cn"));
		preferences.setLastName(ctx.getStringAttribute("sn"));
		preferences.setUsername(ctx.getStringAttribute("uid"));
		preferences.setFirstName(ctx.getStringAttribute("givenName"));

		String externalUserRoleName = ctx.getStringAttribute("flexpayUserRole");

		if (StringUtils.isNotEmpty(externalUserRoleName)) {
			UserRole userRole = userRoleService.findByExternalId(externalUserRoleName);
			if (userRole == null) {
				log.warn("Can`t find user role by external name: {}", externalUserRoleName);
				preferences.setUserRole(null);
			} else {
				preferences.setUserRole(userRole);
			}
		}

		if (preferences.getObjectClasses().contains(LdapConstants.FLEXPAY_PERSON_OBJECT_CLASS)) {
			preferences.setLanguageCode(ctx.getStringAttribute("flexpayPreferedLocale"));
			preferences.setPageSize(getFilterValue("flexpayPreferedPagerSize", 20, ctx));
		}

		Attribute userCertificateAttribute = ctx.getAttributes().get("usercertificate");
		if (userCertificateAttribute != null && StringUtils.isNotEmpty(ctx.getStringAttribute("flexpayCertificateBeginDate"))) {
			try {
				Certificate certificate = new Certificate();
				certificate.setBeginDate(DateUtil.parseDate(ctx.getStringAttribute("flexpayCertificateBeginDate")));
				certificate.setEndDate(DateUtil.parseDate(ctx.getStringAttribute("flexpayCertificateEndDate")));
				certificate.setDescription(ctx.getStringAttribute("flexpayCertificateDescription"));
				preferences.setCertificate(certificate);
			} catch (ParseException e) {
				log.warn("Can not set certificate in user preferences", e);
			}
		}


		try {
			log.debug("Attributes of '{}'", preferences.getUsername());
			Attributes attributes = ctx.getAttributes();
			if (attributes != null && attributes.size() > 0) {
				NamingEnumeration<? extends Attribute> iterAttributes = attributes.getAll();
				while (iterAttributes.hasMore()) {
					Attribute attribute = iterAttributes.next();
					log.debug("attribute '{}' ({}, {})", new Object[]{attribute.getID(), attribute.get().getClass(), attribute.get()});
				}
				iterAttributes.close();
			}
		} catch (NamingException e) {
			log.warn("Get attribute usercertificate", e);
		}

		return preferences;
	}

	private Integer getFilterValue(String attributeName, Integer defaultValue, DirContextOperations ctx) {
		String filterValue = ctx.getStringAttribute(attributeName);
		try {
			return StringUtils.isNotBlank(filterValue) ? Integer.parseInt(filterValue) : defaultValue;
		} catch (NumberFormatException ex) {
			log.warn("Unexpected int value: {}", filterValue);
			return defaultValue;
		}
	}

	/**
	 * Do mapping of preferences properties to context attributes
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 */
	@Override
	public void doMapToContextAdminEdited(DirContextOperations ctx, UserPreferences preferences) {
		setSingleAttribute(ctx, preferences, "cn", preferences.getFullName());
		setSingleAttribute(ctx, preferences, "sn", preferences.getLastName());
		setSingleAttribute(ctx, preferences, "givenName", preferences.getFirstName());
		setSingleAttribute(ctx, preferences, "uid", preferences.getUsername());
	}

	/**
	 * Do mapping of preferences properties to context attributes
	 *
	 * @param ctx		 Context
	 * @param preferences UserPreferences
	 */
	@Override
	public void doMapToContextUserEdited(DirContextOperations ctx, UserPreferences preferences) {
		if (!preferences.getObjectClasses().contains(LdapConstants.FLEXPAY_PERSON_OBJECT_CLASS)) {
			ctx.addAttributeValue("objectclass", LdapConstants.FLEXPAY_PERSON_OBJECT_CLASS);
			preferences.getObjectClasses().add(LdapConstants.FLEXPAY_PERSON_OBJECT_CLASS);
		}

		setSingleAttribute(ctx, preferences, "flexpayPreferedLocale", preferences.getLanguageCode());
		setSingleAttribute(ctx, preferences, "flexpayPreferedPagerSize", String.valueOf(preferences.getPageSize()));
	}

	@Override
	public void doMapToContextPassword(DirContextOperations ctx, UserPreferences preferences, String password) {
		setSingleAttribute(ctx, preferences, "userPassword", password);
	}

	@Override
	public void doMapToContextCertificate(DirContextOperations ctx, UserPreferences preferences, InputStream inputStream, boolean delete) {
		log.debug("doMapToContextCertificate: preference ({}), stream ({}), delete ({})", new Object[]{preferences, inputStream, delete});
		if (delete) {
			setSingleAttribute(ctx, preferences, "flexpayCertificateBeginDate", "");
			setSingleAttribute(ctx, preferences, "flexpayCertificateEndDate", "");
			setSingleAttribute(ctx, preferences, "flexpayCertificateDescription", "");
			setSingleAttribute(ctx, preferences, "usercertificate", new byte[0]);
			return;
		}
		if (inputStream == null) {
			setSingleAttribute(ctx, preferences, "flexpayCertificateDescription", preferences.getCertificate().getDescription());
			return;
		}
		try {
			byte[] certificateData = new byte[ApplicationConfig.getMaxCertificateSize()];
			int countReadData;
			if ((countReadData = inputStream.read(certificateData)) == ApplicationConfig.getMaxCertificateSize()) {
				log.warn("Read max certificate size ({}) from stream", ApplicationConfig.getMaxCertificateSize());
			} else if (countReadData > 0) {
				certificateData = ArrayUtils.subarray(certificateData, 0, countReadData - 1);
			} else {
				log.warn("Read empty certificate data");
				certificateData = null;
			}

			if (certificateData == null) {
				log.debug("Empty certificate data");
				return;
			}

			setSingleAttribute(ctx, preferences, "flexpayCertificateBeginDate", DateUtil.format(preferences.getCertificate().getBeginDate()));
			setSingleAttribute(ctx, preferences, "flexpayCertificateEndDate", DateUtil.format(preferences.getCertificate().getEndDate()));
			setSingleAttribute(ctx, preferences, "flexpayCertificateDescription", preferences.getCertificate().getDescription());

			log.debug("Try set usercertificate: {}", certificateData);
			setSingleAttribute(ctx, preferences, "usercertificate", certificateData);
		} catch (IOException e) {
			log.error("Read line", e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				log.error("Close stream", e);
			}
		}
	}

	@Override
	public void doMapToContextAccessPermissions(DirContextOperations ctx, UserPreferences preferences, List<String> permissions) {
		/*
		String[] allCurrentValues = ctx.getStringAttributes("memberof");
		for (String currentValue : allCurrentValues) {
			ctx.removeAttributeValue("memberof", currentValue);
		}
		*/
		ctx.setAttributeValues("memberof", permissions.toArray());
		if (preferences.getUserRole() != null) {
			setSingleAttribute(ctx, preferences, "flexpayUserRole", preferences.getUserRole().getExternalId());
		} else {
			setSingleAttribute(ctx, preferences, "flexpayUserRole", "");
		}
	}

	@Override
	public void doMapToContextNewUser(DirContextOperations ctx, UserPreferences preferences) {
		Set<String> objectClasses = CollectionUtils.set();
		for (String objectClass : LdapConstants.REQUIRED_OBJECT_CLASSES) {
			ctx.addAttributeValue("objectclass", objectClass);
			objectClasses.add(objectClass);
		}
		preferences.setObjectClasses(objectClasses);
		ctx.addAttributeValue("inetUserStatus", "Active");
		ctx.addAttributeValue("uid", preferences.getUsername());
	}

	private void setSingleAttribute(DirContextOperations ctx, UserPreferences preferences, String name, Object value) {
		if (preferences.attributes().contains(name)) {
			ctx.setAttributeValue(name, value);
		} else {
			ctx.addAttributeValue(name, value);
		}
	}

	/**
	 * Check if this mapper supports supplied object
	 *
	 * @param preferences UserPreferences
	 * @return <code>true</code> if mapper able to map any properties to or from context, or <code>false</code> otherwise
	 */
	@Override
	public boolean supports(UserPreferences preferences) {
		return true;
	}

	@Required
	public void setUserRoleService(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}
}
