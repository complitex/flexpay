package org.flexpay.common.service.impl;

import org.flexpay.common.dao.CertificateDao;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.CertificateService;
import org.flexpay.common.util.KeyStoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.List;

public class CertificateServiceImpl implements CertificateService {

	private static final Logger log = LoggerFactory.getLogger(CertificateServiceImpl.class);

	private CertificateDao certificateDao;

	@Override
	public void addCertificate(String alias, String description, InputStream inputStream) throws FlexPayException {

		addCertificateToKeyStore(alias, inputStream);
		certificateDao.create(new Certificate(alias, description));
	}

	@Override
	public void replaceCertificate(String alias, InputStream inputStream) throws FlexPayException {

		if (!aliasExists(alias)) {
			log.warn("Error replacing certificate: no certificate with alias {} found", alias);
			return;
		}


		java.security.cert.Certificate javaCertificate = null;
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			javaCertificate = certificateFactory.generateCertificate(inputStream);
		} catch (CertificateException e) {
			log.warn("Error replacing certificate: unable to load certificate from given input stream");
			return;
		}

		if (javaCertificate != null) {
			try {
				KeyStore keyStore = KeyStoreUtil.loadKeyStore();
				keyStore.deleteEntry(alias);
				keyStore.setCertificateEntry(alias, javaCertificate);
				KeyStoreUtil.saveKeyStore();
			} catch (KeyStoreException e) {
				throw new FlexPayException("Error replacing certificate in keystore", e);
			}
		}
	}

	@Override
	public List<Certificate> listAllCertificates() {

		return certificateDao.findCertificates();
	}

	@Override
	public void delete(Certificate certificate) throws FlexPayException {

		removeCertificateFromKeyStore(certificate);
		certificateDao.delete(certificate);
	}

	@Override
	public void update(Certificate certificate) {

		certificateDao.update(certificate);
	}

	@Override
	public Certificate readFull(Stub<Certificate> certificateStub) {

		return certificateDao.readFull(certificateStub.getId());
	}

	@Override
	public boolean aliasExists(String alias) {

		List<Certificate> result = certificateDao.findCertificateByAlias(alias);
		return result.size() > 0;
	}

	private void addCertificateToKeyStore(String alias, InputStream inputStream) throws FlexPayException {

		try {
			KeyStore keyStore = KeyStoreUtil.loadKeyStore();
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			keyStore.setCertificateEntry(alias, certificateFactory.generateCertificate(inputStream));
			KeyStoreUtil.saveKeyStore();
		} catch (Exception e) {
			throw new FlexPayException("Error importing certificate to keystore", e);
		}
	}

	private void removeCertificateFromKeyStore(Certificate certificate) throws FlexPayException {

		try {
			KeyStore keyStore = KeyStoreUtil.loadKeyStore();
			keyStore.deleteEntry(certificate.getAlias());
			KeyStoreUtil.saveKeyStore();
		} catch (Exception e) {
			throw new FlexPayException("Error deleting certificate to keystore", e);
		}
	}

	@Required
	public void setCertificateDao(CertificateDao certificateDao) {
		this.certificateDao = certificateDao;
	}
}
