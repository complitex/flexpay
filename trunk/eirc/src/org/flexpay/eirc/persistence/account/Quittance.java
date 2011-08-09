package org.flexpay.eirc.persistence.account;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.payments.persistence.Service;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.flexpay.common.util.CollectionUtils.set;

public class Quittance extends DomainObject {

	private Date creationDate;
	private ServiceOrganization serviceOrganization;
	private Integer orderNumber;
	private Date dateFrom;
	private Date dateTill;
	private EircAccount eircAccount;
	private Set<QuittanceDetailsQuittance> quittanceDetailsQuittances = set();

    public Quittance() {
    }

    public Quittance(@NotNull Long id) {
        super(id);
    }

    public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public ServiceOrganization getServiceOrganization() {
		return serviceOrganization;
	}

	public void setServiceOrganization(ServiceOrganization serviceOrganization) {
		this.serviceOrganization = serviceOrganization;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTill() {
		return dateTill;
	}

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

	public Set<QuittanceDetailsQuittance> getQuittanceDetailsQuittances() {
		return quittanceDetailsQuittances;
	}

	public void setQuittanceDetailsQuittances(Set<QuittanceDetailsQuittance> quittanceDetailsQuittances) {
		this.quittanceDetailsQuittances = quittanceDetailsQuittances;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public EircAccount getEircAccount() {
		return eircAccount;
	}

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
