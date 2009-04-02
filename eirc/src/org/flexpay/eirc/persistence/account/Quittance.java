package org.flexpay.eirc.persistence.account;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Quittance extends DomainObject {

	private Date creationDate;
	private ServiceOrganization serviceOrganization;
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
	 * @return the serviceOrganization
	 */
	public ServiceOrganization getServiceOrganization() {
		return serviceOrganization;
	}

	/**
	 * @param serviceOrganization the serviceOrganization to set
	 */
	public void setServiceOrganization(ServiceOrganization serviceOrganization) {
		this.serviceOrganization = serviceOrganization;
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
	public List<QuittanceDetails> getQuittanceDetails() {
		List<QuittanceDetails> quittanceDetails = CollectionUtils.list();
		for (QuittanceDetailsQuittance el : quittanceDetailsQuittances) {
			quittanceDetails.add(el.getQuittanceDetails());
		}

		return quittanceDetails;
	}

	/**
	 * Reorder quittance details that subservices go after service
	 *
	 * @return quittance details
	 */
	public List<QuittanceDetails> getOrderedQuittanceDetails() {

		List<Service> parentServices = CollectionUtils.list();
		Map<Service, List<QuittanceDetails>> service2subservices = CollectionUtils.map();

		for (QuittanceDetailsQuittance el : quittanceDetailsQuittances) {

			Service service = getService(el);
			Service parentService = service.isSubService() ? service.getParentService() : service;

			List<QuittanceDetails> detailses = service2subservices.get(parentService);
			if (detailses == null) {
				detailses = CollectionUtils.list();
			}

			// subservice is added to the end of a list, and parent service itself to the begining
			if (service.isSubService()) {
				detailses.add(el.getQuittanceDetails());
			} else {
				detailses.add(0, el.getQuittanceDetails());
				parentServices.add(parentService);
			}
			service2subservices.put(parentService, detailses);
		}

		// now build result list
		List<QuittanceDetails> result = CollectionUtils.list();
		for (Service service : parentServices) {
			result.addAll(service2subservices.get(service));
		}

		return result;
	}

	private Service getService(QuittanceDetailsQuittance qdq) {
		return qdq.getQuittanceDetails().getConsumer().getService();
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

	@Nullable
	public QuittanceDetails getServiceDetails(Stub<Service> stub) {
		for (QuittanceDetails details : getQuittanceDetails()) {
			if (details.getConsumer().getServiceStub().equals(stub)) {
				return details;
			}
		}

		return null;
	}
}
