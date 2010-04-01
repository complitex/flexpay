package org.flexpay.payments.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.payments.persistence.Certificate;

import java.util.List;

public interface CertificateDao extends GenericDao<Certificate, Long> {

	public List<Certificate> findCertificates();

	public List<Certificate> findCertificateByAlias(String alias);
}
