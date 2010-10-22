package org.flexpay.common.service.impl;

import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.service.CertificateService;
import org.flexpay.common.service.UserPreferencesService;
import org.flexpay.common.util.config.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.InputStream;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public class CertificateServiceImpl implements CertificateService {

	private static final Logger log = LoggerFactory.getLogger(CertificateServiceImpl.class);

	private UserPreferencesService userPreferencesService;

	@Override
	public void addCertificate(String alias, String description, InputStream inputStream) {

		UserPreferences preferences = getUserPreferences(alias);

		userPreferencesService.editCertificate(preferences, description, inputStream);
	}

	@Override
	public void replaceCertificate(String alias, String description, InputStream inputStream) {

		UserPreferences preferences = getUserPreferences(alias);

		userPreferencesService.editCertificate(preferences, description, inputStream);
	}

	@Override
	public void editCertificateDescription(String alias, String description) {
		UserPreferences preferences = getUserPreferences(alias);

		userPreferencesService.editCertificate(preferences, description, null);
	}

	@Override
	public java.security.cert.Certificate getCertificate(String alias) {
		UserPreferences preferences = getUserPreferences(alias);

		return userPreferencesService.getCertificate(preferences);
	}

	private UserPreferences getUserPreferences(String alias) {
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

	@Required
	public void setUserPreferencesService(UserPreferencesService userPreferencesService) {
		this.userPreferencesService = userPreferencesService;
	}
}
