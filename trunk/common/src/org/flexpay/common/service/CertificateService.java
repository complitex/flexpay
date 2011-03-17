package org.flexpay.common.service;

import org.flexpay.common.exception.CertificateBlockedException;
import org.flexpay.common.exception.CertificateExpiredException;
import org.flexpay.common.exception.CertificateNotFoundException;
import org.flexpay.common.exception.InvalidVerifySignatureException;
import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.util.config.UserPreferences;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.InputStream;
import java.util.List;

public interface CertificateService {

	void addCertificate(String alias, String description, Boolean blocked, InputStream inputStream) throws UsernameNotFoundException;

	void replaceCertificate(String alias, String description, Boolean blocked, InputStream inputStream) throws UsernameNotFoundException;

	void editCertificate(String alias, String description, Boolean blocked) throws UsernameNotFoundException;

	java.security.cert.Certificate getCertificate(String alias) throws UsernameNotFoundException;

	List<Certificate> listAllCertificates();
	
	void delete(Certificate certificate);

	UserPreferences authenticateUserByCertificate(String alias, byte[] signature, List<byte[]> fields) throws InvalidVerifySignatureException, UsernameNotFoundException, CertificateNotFoundException, CertificateBlockedException, CertificateExpiredException;
}
