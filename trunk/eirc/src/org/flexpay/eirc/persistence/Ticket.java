package org.flexpay.eirc.persistence;

import java.util.Date;
import java.util.Set;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.persistence.DomainObject;

public class Ticket extends DomainObject {
	private Date creationDate;
	private ServiceOrganization serviceOrganization;
	private Person person;
	private Integer ticketNumber;
	private Date dateFrom;
	private Date dateTill;
	private Apartment apartment;
	private Set<TicketServiceAmount> ticketServiceAmounts;

	public Ticket() {
	}

	public Ticket(Long id) {
		super(id);
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
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
	 * @param serviceOrganization
	 *            the serviceOrganization to set
	 */
	public void setServiceOrganization(ServiceOrganization serviceOrganization) {
		this.serviceOrganization = serviceOrganization;
	}

	/**
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person
	 *            the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return the ticketNumber
	 */
	public Integer getTicketNumber() {
		return ticketNumber;
	}

	/**
	 * @param ticketNumber
	 *            the ticketNumber to set
	 */
	public void setTicketNumber(Integer ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	/**
	 * @return the dateFrom
	 */
	public Date getDateFrom() {
		return dateFrom;
	}

	/**
	 * @param dateFrom
	 *            the dateFrom to set
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
	 * @param dateTill
	 *            the dateTill to set
	 */
	public void setDateTill(Date dateTill) {
		this.dateTill = dateTill;
	}

	/**
	 * @return the apartment
	 */
	public Apartment getApartment() {
		return apartment;
	}

	/**
	 * @param apartment
	 *            the apartment to set
	 */
	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}

	/**
	 * @return the ticketServiceAmounts
	 */
	public Set<TicketServiceAmount> getTicketServiceAmounts() {
		return ticketServiceAmounts;
	}

	/**
	 * @param ticketServiceAmounts the ticketServiceAmounts to set
	 */
	public void setTicketServiceAmounts(
			Set<TicketServiceAmount> ticketServiceAmounts) {
		this.ticketServiceAmounts = ticketServiceAmounts;
	}

}
