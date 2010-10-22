package org.flexpay.common.service;

import org.flexpay.common.persistence.Certificate;

import java.io.InputStream;
import java.util.List;

public interface CertificateService {

	void addCertificate(String alias, String description, InputStream inputStream);

	void replaceCertificate(String alias, String description, InputStream inputStream);

	void editCertificateDescription(String alias, String description);

	java.security.cert.Certificate getCertificate(String alias);

	List<Certificate> listAllCertificates();
	
	void delete(Certificate certificate);
}
