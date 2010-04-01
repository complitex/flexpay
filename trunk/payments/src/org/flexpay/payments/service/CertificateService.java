package org.flexpay.payments.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.payments.persistence.Certificate;

import java.io.InputStream;
import java.util.List;

public interface CertificateService {

	public void addCertificate(String alias, String description, InputStream inputStream) throws FlexPayException;

	public List<Certificate> listAllCertificates();
	
	public void delete(Certificate certificate) throws FlexPayException;

	public void update(Certificate certificate);

	public boolean aliasExists(String alias);

	public Certificate readFull(Stub<Certificate> certificateStub);
}
