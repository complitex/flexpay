package org.flexpay.common.dao.impl;

import org.apache.commons.lang.NotImplementedException;
import org.flexpay.common.dao.UserPreferencesDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.KeyStoreUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class UserPreferencesDaoImpl extends HibernateDaoSupport implements UserPreferencesDao {

	private static final Logger log = LoggerFactory.getLogger(UserPreferencesDaoImpl.class);

	@Override
	public void saveAllPreferences(UserPreferences preferences) {

		if (isEmpty(preferences.getFullName())) {
			preferences.setFullName(preferences.getUsername());
		}
		if (isEmpty(preferences.getLastName())) {
			preferences.setLastName(preferences.getUsername());
		}

		if (preferences.isNew()) {
			preferences.setId(null);
			getHibernateTemplate().save(preferences);
		} else {
			getHibernateTemplate().update(preferences);
		}
	}

	@Override
	public void saveAdminEditedPreferences(UserPreferences preferences) {
		saveAllPreferences(preferences);
	}

	@Override
	public void saveUserEditedPreferences(UserPreferences preferences) {
		saveAllPreferences(preferences);
	}

	@Override
	public void updateUserPassword(UserPreferences preferences, String password) {
		throw new NotImplementedException();
	}

	@Override
	public boolean checkUserPassword(UserPreferences preferences, String password) {
		throw new NotImplementedException();
	}

	@Override
	public void changeUserRole(UserPreferences preferences) {
		throw new NotImplementedException();
	}

	@Override
	public boolean delete(String uid) {
		getHibernateTemplate().findByNamedQuery("UserPreferences.deleteByName", uid);
		return true;
	}

	@Override
	public UserPreferences findByUserName(String uid) {
		return (UserPreferences) DataAccessUtils.uniqueResult(
				getHibernateTemplate().findByNamedQuery("UserPreferences.findByName", uid));
	}

	/**
	 * Not implemented
	 */
	@Override
	public List<String> getGrantedAuthorities(UserPreferences preferences) {
		throw new NotImplementedException();
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public List<UserPreferences> listAllUser() {
		return getHibernateTemplate().loadAll(UserPreferences.class);
	}

	@Override
	public Certificate editCertificate(UserPreferences preferences, String description, Boolean blocked, InputStream inputStreamCertificate) {
		try {
			X509Certificate certificate = (X509Certificate) addCertificateToKeyStore(preferences.getUsername(), inputStreamCertificate);
			if (certificate != null) {
				if (isCertificateExist(preferences)) {
					org.flexpay.common.persistence.Certificate userCertificate = preferences.getCertificate();
					userCertificate.setDescription(description);
					userCertificate.setBlocked(blocked);
					userCertificate.setBeginDate(certificate.getNotBefore());
					userCertificate.setEndDate(certificate.getNotAfter());
					userCertificate.setUserPreferences(preferences);

					getHibernateTemplate().update(preferences);
				}
			}
		} catch (FlexPayException e) {
			log.error("Add certificate to key store", e);
		}
		return null;
	}

    @Nullable
	@Override
	public Certificate getCertificate(UserPreferences preferences) {
		try {
			KeyStore keyStore = KeyStoreUtil.loadKeyStore();
			return keyStore.getCertificate(preferences.getUsername());
		} catch (Exception e) {
			log.error("Can not get certificate from key store");
		}
		return null;
	}

	@Override
	public void deleteCertificate(UserPreferences preferences) {
		if (isCertificateExist(preferences)) {
			preferences.setCertificate(null);
			getHibernateTemplate().delete(preferences);
		}
		try {
			deleteOldCertificateInKeyStore(preferences.getUsername(), KeyStoreUtil.loadKeyStore());
		} catch (FlexPayException e) {
			log.warn("Certificate can not delete from key store", e);
		}
	}

	@Override
	public boolean isCertificateExist(UserPreferences preferences) {
		return preferences.getCertificate() != null;
	}

	/**
	 * Not implemented
	 */
	@Override
	public boolean createNewUser(UserPreferences preferences, String password) {
		throw new NotImplementedException();
	}

	private Certificate addCertificateToKeyStore(String alias, InputStream inputStream) throws FlexPayException {

		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			inputStream.read();
			Certificate javaCertificate = certificateFactory.generateCertificate(inputStream);

			KeyStore keyStore = KeyStoreUtil.loadKeyStore();

			if (isCertificateExist(alias, keyStore)) {
				deleteOldCertificateInKeyStore(alias, keyStore);
			}

			keyStore.setCertificateEntry(alias, javaCertificate);
			KeyStoreUtil.saveKeyStore();

			return javaCertificate;
		} catch (Exception e) {
			throw new FlexPayException("Error importing certificate to keystore", e);
		}
	}

	private void deleteOldCertificateInKeyStore(String alias, KeyStore keyStore) throws FlexPayException {
		try {
			keyStore.deleteEntry(alias);
			KeyStoreUtil.saveKeyStore();
		} catch (KeyStoreException e) {
			throw new FlexPayException("Error replacing certificate in keystore", e);
		}
	}

	private boolean isCertificateExist(String alias, KeyStore keyStore) throws FlexPayException {
		try {
			return keyStore.isCertificateEntry(alias);
		} catch (KeyStoreException e) {
			throw new FlexPayException("Error replacing certificate in keystore", e);
		}
	}

}
