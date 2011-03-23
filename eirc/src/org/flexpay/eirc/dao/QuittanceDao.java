package org.flexpay.eirc.dao;

import org.flexpay.common.dao.GenericDao;
import org.flexpay.eirc.persistence.account.Quittance;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public interface QuittanceDao extends GenericDao<Quittance, Long> {

	/**
	 * Get quittance by account number, till date and order number
	 *
	 * @param accountNumber Eirc account number quittance was generated for
	 * @param month		 Month the quittance was generated for
	 * @param number		Order of quittance in specified month
     *
	 * @return List of quittances, possibly empty, usuals case is one quittance list
	 */
	@NotNull
	List<Quittance> findQuittanceByNumber(String accountNumber, Date month, Integer number);

    /**
     * Get quittance by account number, till date, order number and service type id
     *
     * @param accountNumber Eirc account number quittance was generated for
     * @param month		 Month the quittance was generated for
     * @param number		Order of quittance in specified month
     * @param serviceTypeId service type id
     *
     * @return List of quittances, possibly empty, usuals case is one quittance list
     */
    @NotNull
    List<Quittance> findQuittanceByNumberAndServiceType(String accountNumber, Date month, Integer number, Long serviceTypeId);

}
