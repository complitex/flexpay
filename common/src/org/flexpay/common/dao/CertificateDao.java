package org.flexpay.common.dao;

import org.flexpay.common.persistence.Certificate;

import java.util.List;

public interface CertificateDao extends GenericDao<Certificate, Long> {

	public List<Certificate> findCertificates();

	public List<Certificate> findCertificateByAlias(String alias);
}
