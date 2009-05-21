package org.flexpay.payments.dao;

import org.springframework.security.annotation.Secured;
import org.flexpay.payments.service.Roles;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.orgs.persistence.Organization;

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

public interface OperationDaoExt {

	/**
	 * Returns list of operations which contains documents suitable to search criterias
	 * @param organizationId creator organization id
	 * @param serviceTypeId document service type id
	 * @param begin lower bound for document creation date
	 * @param end upper bound for document creation date
	 * @param minimalSumm minimal summ of document
	 * @param maximalSumm maximal summ of document
	 * @param pager pager (used for operations!)
	 * @return list of operations which contains documents suitable to search criterias
	 */
	@Secured (Roles.OPERATION_READ)
	List<Operation> searchDocuments(Organization organization, Long serviceTypeId, Date begin, Date end, BigDecimal minimalSumm, BigDecimal maximalSumm, Page<Operation> pager);

	/**
	 * Returns list of operations suitable to search criterias
	 * @param organizationId creator organization id
	 * @param begin lower bound for operation creation date
	 * @param end upper bound for operation creation date
	 * @param minimalSumm minimal operation summ
	 * @param maximalSumm maximal operation summ
	 * @param pager pager
	 * @return list of operations suitable to search criterias
	 */
	@Secured(Roles.OPERATION_READ)
	List<Operation> searchOperations(Organization organization, Date begin, Date end, BigDecimal minimalSumm, BigDecimal maximalSumm, Page<Operation> pager);
}
