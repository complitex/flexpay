package org.flexpay.eirc.service.imp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.persistence.Street;
import org.flexpay.eirc.dao.TicketDao;
import org.flexpay.eirc.persistence.AccountRecord;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.Ticket;
import org.flexpay.eirc.persistence.TicketServiceAmount;
import org.flexpay.eirc.service.AccountRecordService;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.flexpay.eirc.service.TicketService;
import org.flexpay.eirc.service.TicketServiceAmountService;
import org.springframework.transaction.annotation.Transactional;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class TicketServiceImpl implements TicketService {
	private static Logger log = Logger.getLogger(TicketServiceImpl.class);
	
	private TicketDao ticketDao;
	private ServiceOrganisationService serviceOrganisationService;
	private AccountRecordService accountRecordService;
	private TicketServiceAmountService ticketServiceAmountService;
	
	
	@Transactional(readOnly = false)
	public void generateForServiceOrganisation(Long serviceOrganisationId, Date dateFrom, Date dateTill) {
		ServiceOrganisation serviceOrganisation = serviceOrganisationService.read(serviceOrganisationId);
		Set<ServedBuilding> buildingSet = serviceOrganisation.getBuildings();
		for(Building building : buildingSet) {
			generateForBuilding(serviceOrganisation, building, dateFrom, dateTill);
		}
		
	}
	
	@Transactional(readOnly = false)
	private void generateForBuilding(ServiceOrganisation serviceOrganisation, Building building, Date dateFrom, Date dateTill) {
		Object[] streetAndBuildingsNumber = getTargetBuildingNumberAndStreet(building);
		Street street = null;
		String buildingsNumber = null;
		if(streetAndBuildingsNumber != null) {
			street = (Street) streetAndBuildingsNumber[0];
			buildingsNumber = (String) streetAndBuildingsNumber[1];
		} else {
			return;
		}
		
		Set<Apartment> apartmentSet = building.getApartments();
		for(Apartment apartment : apartmentSet) {
			generateForApartment(serviceOrganisation, apartment, dateFrom, dateTill);
		}
	}
	
	@Transactional(readOnly = false)
	private void generateForApartment(ServiceOrganisation serviceOrganisation, Apartment apartment, Date dateFrom, Date dateTill) {
		Set<Person> personSet = apartment.getPersons();
		for(Person person : personSet) {
			generateForPerson(serviceOrganisation, person, apartment, dateFrom, dateTill);
		}
	}
	
	@Transactional(readOnly = false)
	private void generateForPerson(ServiceOrganisation serviceOrganisation, Person person, Apartment apartment, Date dateFrom, Date dateTill) {
		Ticket ticket = new Ticket();
		ticket.setCreationDate(new Date());
		ticket.setServiceOrganisation(serviceOrganisation);
		ticket.setPerson(person);
		ticket.setTicketNumber(getTicketNumber());
		ticket.setDateFrom(dateFrom);
		ticket.setDateTill(dateTill);
		ticket.setApartment(apartment);
		//Set<TicketServiceAmount> ticketServiceAmountSet = new HashSet<TicketServiceAmount>();
		//ticket.setTicketServiceAmounts(ticketServiceAmountSet);
		
		List<AccountRecord> accountRecordList = accountRecordService.findForTicket(person.getId(), apartment.getId());
		List<AccountRecord> tempList = null;
		ServiceType currentServiceType = null;
		for(AccountRecord accountRecord : accountRecordList) {
			if(currentServiceType == null || currentServiceType.getId() != accountRecord.getConsumer().getService().getServiceType().getId()) {
				if(tempList != null && !tempList.isEmpty()) {
					ticketServiceAmountService.create(getTicketServiceAmount(ticket, currentServiceType, tempList, dateFrom, dateTill));
				}
				currentServiceType = accountRecord.getConsumer().getService().getServiceType();
				tempList = new ArrayList<AccountRecord>();
			}
			tempList.add(accountRecord);
		}
		if(tempList != null && !tempList.isEmpty()) {
			ticketServiceAmountService.create(getTicketServiceAmount(ticket, currentServiceType, tempList, dateFrom, dateTill));
		}
		create(ticket);
	}
	
	private Integer getTicketNumber() {
		// TODO realize logic
		return 1;
	}
	
	private TicketServiceAmount getTicketServiceAmount(Ticket ticket, ServiceType serviceType, List<AccountRecord> accountRecordList, Date dateFrom, Date dateTill) {
		TicketServiceAmount ticketServiceAmount = new TicketServiceAmount();
		ticketServiceAmount.setTicket(ticket);
		ticketServiceAmount.setServiceType(serviceType);
		
		BigDecimal dateFromAmount = BigDecimal.ZERO;
		BigDecimal dateTillAmount = BigDecimal.ZERO;
		for(AccountRecord accountRecord : accountRecordList) {
			if(!accountRecord.getOperationDate().after(dateFrom)) {
				dateFromAmount = dateFromAmount.add(accountRecord.getAmount());
			} else if(!accountRecord.getOperationDate().after(dateTill)) {
				dateTillAmount = dateTillAmount.add(accountRecord.getAmount());
			} else {
				break;
			}
		}
		dateTillAmount = dateTillAmount.add(dateFromAmount);
		
		ticketServiceAmount.setDateFromAmount(dateFromAmount);
		ticketServiceAmount.setDateTillAmount(dateTillAmount);
		
		return ticketServiceAmount;
	}
	
	
	private Object[] getTargetBuildingNumberAndStreet(Building building) {
		// TODO what Buildings we need to use ?
		Set<Buildings> buildingsSet = building.getBuildingses();
		for(Buildings buildings : buildingsSet) {
			String buildingsNumber = buildings.getNumber();
			Street street = buildings.getStreet();
			if(buildingsNumber != null && street != null) {
				return new Object[] {street, buildingsNumber};
			}
		}
		return null;
	}
	
	private PersonIdentity getTargetPersonIdentity(Person person) {
		// TODO what PersonIdentity we need to use ?
		Set<PersonIdentity> personIdentitySet = person.getPersonIdentities();
		return personIdentitySet.isEmpty() ? null : personIdentitySet.iterator().next();
	}

	/**
	 * @param serviceOrganisationService the serviceOrganisationService to set
	 */
	public void setServiceOrganisationService(
			ServiceOrganisationService serviceOrganisationService) {
		this.serviceOrganisationService = serviceOrganisationService;
	}

	/**
	 * @param accountRecordService the accountRecordService to set
	 */
	public void setAccountRecordService(AccountRecordService accountRecordService) {
		this.accountRecordService = accountRecordService;
	}
	
	/**
	 * Create new Ticket
	 *
	 * @param ticket Ticket
	 * @return persisted Ticket
	 */
	public Ticket create(Ticket ticket) {
		Long id = ticketDao.create(ticket);
		ticket.setId(id);
		return ticket;
	}

	/**
	 * @param ticketDao the ticketDao to set
	 */
	public void setTicketDao(TicketDao ticketDao) {
		this.ticketDao = ticketDao;
	}

	/**
	 * @param ticketServiceAmountService the ticketServiceAmountService to set
	 */
	public void setTicketServiceAmountService(
			TicketServiceAmountService ticketServiceAmountService) {
		this.ticketServiceAmountService = ticketServiceAmountService;
	}

}
