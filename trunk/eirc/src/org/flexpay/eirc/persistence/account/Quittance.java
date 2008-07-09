package org.flexpay.eirc.persistence.account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.flexpay.ab.persistence.PersonRegistration;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.ServiceType;

public class Quittance extends DomainObject {
	private Date creationDate;
	private ServiceOrganisation serviceOrganisation;
	private Integer orderNumber;
	private Date dateFrom;
	private Date dateTill;
	private EircAccount eircAccount;
	private Set<QuittanceDetailsQuittance> quittanceDetailsQuittances = Collections.emptySet();
	
	
	public QuittanceDetails calculateTotalQuittanceDetails(ServiceType serviceType) {
		List<QuittanceDetails> quittanceDetailsList = new ArrayList<QuittanceDetails>();
		for(QuittanceDetails quittanceDetails : getQuittanceDetails()) {
			if(quittanceDetails.getConsumer().getService().getServiceType().getCode() == serviceType.getCode()) {
				quittanceDetailsList.add(quittanceDetails);
			}
		}
		
		return calculateTotalQuittanceDetail(quittanceDetailsList);
	}
	
	private QuittanceDetails calculateTotalQuittanceDetail(List<QuittanceDetails> list) {
		if(list.isEmpty()) {
			return null;
		}
		
		// sort by dateTill
		Collections.sort(list, new Comparator () {
	        public int compare(Object o1, Object o2) {
	        	QuittanceDetails q1 = (QuittanceDetails)o1;
	        	QuittanceDetails q2 = (QuittanceDetails)o2;
	            return q1.getMonth().compareTo(q2.getMonth());
	        }
	    });
		
		QuittanceDetails last = list.get(list.size() - 1);
		for(int i = 0; i < list.size() - 1; i++) {
			last.add(list.get(i));
		}
		
		return list.get(list.size() - 1);
	}
	
	
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
		Set<QuittanceDetails> quittanceDetails = new HashSet<QuittanceDetails>(quittanceDetailsQuittances.size());
		for(QuittanceDetailsQuittance el : quittanceDetailsQuittances) {
			quittanceDetails.add(el.getQuittanceDetails());
		}
		
		return quittanceDetails;
	}
	
	public void addQuittanceDetails(QuittanceDetails quittanceDetails) {
		QuittanceDetailsQuittance quittanceDetailsQuittance = new QuittanceDetailsQuittance();
		quittanceDetailsQuittance.setQuittance(this);
		quittanceDetailsQuittance.setQuittanceDetails(quittanceDetails);
		if(quittanceDetailsQuittances.isEmpty()) {
			quittanceDetailsQuittances = new HashSet<QuittanceDetailsQuittance>();
		}
		quittanceDetailsQuittances.add(quittanceDetailsQuittance);
	}
	
	/**
	 * @param quittanceDetails the quittanceDetails to set
	 */
	public void setQuittanceDetails(Collection<QuittanceDetails> quittanceDetailsCol) {
		if(quittanceDetailsCol.isEmpty()) {
			return;
		}
		for(QuittanceDetails quittanceDetails : quittanceDetailsCol)
			addQuittanceDetails(quittanceDetails);
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
}
