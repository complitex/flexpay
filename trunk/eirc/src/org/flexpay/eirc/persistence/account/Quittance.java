package org.flexpay.eirc.persistence.account;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

public class Quittance extends DomainObject {

	private Date creationDate;
	private ServiceOrganisation serviceOrganisation;
	private Integer orderNumber;
	private Date dateFrom;
	private Date dateTill;
	private EircAccount eircAccount;
	private Set<QuittanceDetailsQuittance> quittanceDetailsQuittances = Collections.emptySet();

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the serviceOrganisation
	 */
	public ServiceOrganisation getServiceOrganisation() {
		return serviceOrganisation;
	}

	/**
	 * @param serviceOrganisation the serviceOrganisation to set
	 */
	public void setServiceOrganisation(ServiceOrganisation serviceOrganisation) {
		this.serviceOrganisation = serviceOrganisation;
	}

	/**
	 * @return the dateFrom
	 */
	public Date getDateFrom() {
		return dateFrom;
	}

	/**
	 * @param dateFrom the dateFrom to set
	 */
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	/**
	 * @return the dateTill
	 */
	public Date getDateTill() {
		return dateTill;
	}

	/**
	 * @param dateTill the dateTill to set
	 */
	public void setDateTill(Date dateTill) {
		this.dateTill = dateTill;
	}

	/**
	 * @return the quittanceDetails
	 */
	public Set<QuittanceDetails> getQuittanceDetails() {
		Set<QuittanceDetails> quittanceDetails = CollectionUtils.set();
		for (QuittanceDetailsQuittance el : quittanceDetailsQuittances) {
			quittanceDetails.add(el.getQuittanceDetails());
		}

		return quittanceDetails;
	}

	/**
	 * @return the quittanceDetailsQuittances
	 */
	public Set<QuittanceDetailsQuittance> getQuittanceDetailsQuittances() {
		return quittanceDetailsQuittances;
	}

	/**
	 * @param quittanceDetailsQuittances the quittanceDetailsQuittances to set
	 */
	public void setQuittanceDetailsQuittances(
			Set<QuittanceDetailsQuittance> quittanceDetailsQuittances) {
		this.quittanceDetailsQuittances = quittanceDetailsQuittances;
	}

	/**
	 * @return the orderNumber
	 */
	public Integer getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the eircAccount
	 */
	public EircAccount getEircAccount() {
		return eircAccount;
	}

	/**
	 * @param eircAccount the eircAccount to set
	 */
	public void setEircAccount(EircAccount eircAccount) {
		this.eircAccount = eircAccount;
	}

	/**
	 * Get account number of associated EIRC account
	 *
	 * @return account number
	 */
	public String getAccountNumber() {
		return getEircAccount().getAccountNumber();
	}

	@NotNull
	public Long getEircAccountId() {
		return new Stub<EircAccount>(eircAccount).getId();
	}
}
