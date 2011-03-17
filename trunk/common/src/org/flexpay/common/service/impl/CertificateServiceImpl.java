package org.flexpay.common.service.impl;

import org.flexpay.common.exception.CertificateBlockedException;
import org.flexpay.common.exception.CertificateExpiredException;
import org.flexpay.common.exception.CertificateNotFoundException;
import org.flexpay.common.exception.InvalidVerifySignatureException;
import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.service.CertificateService;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.common.util.config.UserPreferences;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Signature;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class CertificateServiceImpl implements CertificateService {

	public static final String SIGNATURE_ALGORITHM = "SHA1withDSA";

	private static final Logger log = LoggerFactory.getLogger(CertificateServiceImpl.class);

	private UserPreferencesService userPreferencesService;

	@Override
	public void addCertificate(String alias, String description, Boolean blocked, InputStream inputStream) throws UsernameNotFoundException {

		UserPreferences preferences = getUserPreferences(alias);

		userPreferencesService.editCertificate(preferences, description, blocked, inputStream);
	}

	@Override
	public void replaceCertificate(String alias, String description, Boolean blocked, InputStream inputStream) throws UsernameNotFoundException {

		UserPreferences preferences = getUserPreferences(alias);

		userPreferencesService.editCertificate(preferences, description, blocked, inputStream);
	}

	@Override
	public void editCertificate(String alias, String description, Boolean blocked) throws UsernameNotFoundException {
		UserPreferences preferences = getUserPreferences(alias);

		userPreferencesService.editCertificate(preferences, description, blocked, null);
	}

	/**
	 * Get user certificate
	 *
	 * @param alias User`s alias
	 * @return Certificate or <null> if certificate did not load.
	 */
	@Override
	public java.security.cert.Certificate getCertificate(String alias) throws UsernameNotFoundException {
		UserPreferences preferences = getUserPreferences(alias);

		return userPreferencesService.getCertificate(preferences);
	}

	private UserPreferences getUserPreferences(String alias) throws UsernameNotFoundException {
		return userPreferencesService.loadUserByUsername(alias);
	}

	@Override
	public List<Certificate> listAllCertificates() {
		return list();
	}

	@Override
	public void delete(Certificate certificate) {

		userPreferencesService.deleteCertificate(certificate.getUserPreferences());
	}

	@Override
	public UserPreferences authenticateUserByCertificate(@NotNull String alias, @NotNull byte[] signature, @NotNull List<byte[]> fields)
			throws InvalidVerifySignatureException, UsernameNotFoundException, CertificateNotFoundException, CertificateBlockedException, CertificateExpiredException {

		UserPreferences preferences = userPreferencesService.loadUserByUsername(alias);

		if (preferences.getCertificate() == null) {
			throw new CertificateNotFoundException(alias, "common.user.certificate.not_found", alias);
		}

		Certificate aboutCertificate = preferences.getCertificate();

		if (aboutCertificate.isBlocked()) {
			throw new CertificateBlockedException(alias, "common.user.certificate.blocked", alias);
		}

		if (aboutCertificate.isExpired()) {
			throw new CertificateExpiredException(alias, "common.user.certificate.expired", alias);
		}

		java.security.cert.Certificate certificate = userPreferencesService.getCertificate(preferences);

		try {
			Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);
			sign.initVerify(certificate);

			for (byte[] field : fields) {
				sign.update(field);
			}

			if (!sign.verify(signature)) {
				throw new InvalidVerifySignatureException(alias, "common.user.certificate.invalid_signature", alias);
			}
		} catch (GeneralSecurityException e) {
			throw new InvalidVerifySignatureException(alias, e, "common.user.certificate.invalid_signature", alias);
		}

		SecurityUtil.authenticate(alias, userPreferencesService.getGrantedAuthorities(preferences));

        return preferences;
	}

	@Required
	public void setUserPreferencesService(UserPreferencesService userPreferencesService) {
		this.userPreferencesService = userPreferencesService;
	}
}
