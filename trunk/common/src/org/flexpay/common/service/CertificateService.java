package org.flexpay.common.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Certificate;
import org.flexpay.common.persistence.Stub;

import java.io.InputStream;
import java.util.List;

public interface CertificateService {

	void addCertificate(String alias, String description, InputStream inputStream) throws FlexPayException;

	void replaceCertificate(String alias, InputStream inputStream) throws FlexPayException;

	List<Certificate> listAllCertificates();
	
	void delete(Certificate certificate) throws FlexPayException;

	void update(Certificate certificate);

	boolean aliasExists(String alias);

	Certificate readFull(Stub<Certificate> certificateStub);
}
