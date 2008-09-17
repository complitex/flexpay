package org.flexpay.eirc.service.imp;

import org.flexpay.ab.persistence.*;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.eirc.dao.TicketDao;
import org.flexpay.eirc.pdf.PdfTicketWriter.ServiceAmountInfo;
import org.flexpay.eirc.pdf.PdfTicketWriter.TicketInfo;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.flexpay.eirc.service.TicketService;
import org.flexpay.eirc.service.TicketServiceAmountService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class TicketServiceImpl implements TicketService {

	private TicketDao ticketDao;
	private ServiceOrganisationService serviceOrganisationService;
	private TicketServiceAmountService ticketServiceAmountService;

	@Transactional (readOnly = false)
	public void generateForServiceOrganisation(Stub<ServiceOrganisation> stub, Date dateFrom, Date dateTill) {
		ServiceOrganisation serviceOrganisation = serviceOrganisationService.read(stub);
		Set<ServedBuilding> buildingSet = serviceOrganisation.getBuildings();
		for (Building building : buildingSet) {
			generateForBuilding(serviceOrganisation, building, dateFrom,
					dateTill);
		}

	}

	@Transactional (readOnly = false)
	private void generateForBuilding(ServiceOrganisation serviceOrganisation,
									 Building building, Date dateFrom, Date dateTill) {
		Object[] streetAndBuildingsNumber = getTargetBuildingNumberAndStreet(building);

		// what's a fuck for???
		Street street = null;
		String buildingsNumber = null;
		if (streetAndBuildingsNumber != null) {
			street = (Street) streetAndBuildingsNumber[0];
			buildingsNumber = (String) streetAndBuildingsNumber[1];
		} else {
			return;
		}

		Set<Apartment> apartmentSet = building.getApartments();
		for (Apartment apartment : apartmentSet) {
			generateForApartment(serviceOrganisation, apartment, dateFrom,
					dateTill);
		}
	}

	@Transactional (readOnly = false)
	private void generateForApartment(ServiceOrganisation serviceOrganisation,
									  Apartment apartment, Date dateFrom, Date dateTill) {
		Set<Person> personSet = apartment.getPersons();
		for (Person person : personSet) {
			generateForPerson(serviceOrganisation, person, apartment, dateFrom,
					dateTill);
		}
	}

	@Transactional (readOnly = false)
	private void generateForPerson(ServiceOrganisation serviceOrganisation,
								   Person person, Apartment apartment, Date dateFrom, Date dateTill) {
		Ticket ticket = new Ticket();
		ticket.setCreationDate(new Date());
		ticket.setServiceOrganisation(serviceOrganisation);
		ticket.setPerson(person);
		ticket.setTicketNumber(getTicketNumber(person, dateTill));
		ticket.setDateFrom(dateFrom);
		ticket.setDateTill(dateTill);
		ticket.setApartment(apartment);
		create(ticket);

		List<Object[]> serviceAmountDateFromList = null;
//				accountRecordService
//				.findCalculateServiceAmount(person.getId(), apartment.getId(),
//						dateFrom);
		List<Object[]> serviceAmountDateTillList = null;
//		accountRecordService
//				.findCalculateServiceAmount(person.getId(), apartment.getId(),
//						dateTill);
		Map<Long, TicketServiceAmount> map = new HashMap<Long, TicketServiceAmount>(
				serviceAmountDateTillList.size());
		for (Object[] element : serviceAmountDateFromList) {
			TicketServiceAmount amount = new TicketServiceAmount();
			amount.setTicket(ticket);
			amount.setConsumer(new Consumer((Long) element[0]));
			amount.setDateFromAmount((BigDecimal) element[1]);
			map.put(amount.getConsumer().getId(), amount);
		}
		for (Object[] element : serviceAmountDateTillList) {
			TicketServiceAmount amount = map.get(element[0]);
			if (amount != null) {
				amount.setDateTillAmount((BigDecimal) element[1]);
			} else {
				amount = new TicketServiceAmount();
				amount.setTicket(ticket);
				amount.setConsumer(new Consumer((Long) element[0]));
				amount.setDateTillAmount((BigDecimal) element[1]);
				map.put(amount.getConsumer().getId(), amount);
			}
		}
		for (TicketServiceAmount ticketServiceAmount : map.values()) {
			ticketServiceAmountService.create(ticketServiceAmount);
		}
	}

	private Integer getTicketNumber(Person person, Date dateTill) {
		Page<Ticket> pager = new Page<Ticket>();
		pager.setPageSize(1);
		ticketDao.findObjectsByPersonAndTillDate(pager, person.getId(),
				dateTill);
		return pager.getTotalNumberOfElements();
	}

	private Object[] getTargetBuildingNumberAndStreet(Building building) {
		// TODO what Buildings we need to use ?
		Set<Buildings> buildingsSet = building.getBuildingses();
		for (Buildings buildings : buildingsSet) {
			String buildingsNumber = buildings.getNumber();
			Street street = buildings.getStreet();
			if (buildingsNumber != null && street != null) {
				return new Object[]{street, buildingsNumber};
			}
		}
		return null;
	}

	public TicketInfo getTicketInfo(Long ticketId) throws FlexPayException {
		Ticket ticket = ticketDao.read(ticketId);
		TicketInfo ticketInfo = new TicketInfo();
		ticketInfo.dateFrom = ticket.getDateFrom();
		ticketInfo.dateTill = ticket.getDateTill();
		ticketInfo.creationDate = ticket.getCreationDate();
		ticketInfo.ticketNumber = ticket.getId();
		PersonIdentity personIdentity = getTargetPersonIdentity(ticket
				.getPerson());
		if (personIdentity != null) {
			ticketInfo.payer = personIdentity.getFirstName() + " "
							   + personIdentity.getMiddleName() + " "
							   + personIdentity.getLastName();
		}
		String addressStr = getAddressStr(ticket, true);
		if (addressStr != null) {
			ticketInfo.address = getAddressStr(ticket, true);
		}
		Set<TicketServiceAmount> ticketSetviceAmountSet = ticket
				.getTicketServiceAmounts();
		Map<Integer, ServiceAmountInfo> serviceAmountInfoMap = new HashMap<Integer, ServiceAmountInfo>();
		ticketInfo.serviceAmountInfoMap = serviceAmountInfoMap;
		for (TicketServiceAmount ticketServiceAmount : ticketSetviceAmountSet) {
			ServiceType serviceType = ticketServiceAmount.getConsumer()
					.getService().getServiceType();
			Translation translation = TranslationUtil
					.getTranslation(serviceType.getTypeNames());

			ServiceAmountInfo serviceAmountInfo = serviceAmountInfoMap
					.get(serviceType.getCode());
			if (serviceAmountInfo == null) {
				serviceAmountInfo = new ServiceAmountInfo();
				if (translation != null) {
					serviceAmountInfo.name = translation.getName();
				}
				serviceAmountInfo.dateFromAmount = getAbsAmount(ticketServiceAmount
						.getDateFromAmount());
				serviceAmountInfo.dateTillAmount = getAbsAmount(ticketServiceAmount
						.getDateTillAmount());
				serviceAmountInfo.code = serviceType.getCode();
				serviceAmountInfoMap.put(serviceAmountInfo.code,
						serviceAmountInfo);
			} else {
				serviceAmountInfo.dateFromAmount = serviceAmountInfo.dateFromAmount
						.add(getAbsAmount(ticketServiceAmount
								.getDateFromAmount()));
				serviceAmountInfo.dateTillAmount = serviceAmountInfo.dateTillAmount
						.add(getAbsAmount(ticketServiceAmount
								.getDateTillAmount()));
			}
		}

		return ticketInfo;
	}

	private BigDecimal getAbsAmount(BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) >= 0) {
			return BigDecimal.ZERO;
		} else {
			return amount.abs();
		}
	}

	private PersonIdentity getTargetPersonIdentity(Person person) {
		Set<PersonIdentity> personIdentitySet = person.getPersonIdentities();
		if (personIdentitySet.isEmpty()) {
			return null;
		}
		PersonIdentity defaultPersonIdentity = null;
		for (PersonIdentity personIdentity : personIdentitySet) {
			if (personIdentity.getIdentityType().getTypeId() == IdentityType.TYPE_PASSPORT) {
				return personIdentity;
			} else if (personIdentity.getIdentityType().getTypeId() == IdentityType.TYPE_FOREIGN_PASSPORT) {
				defaultPersonIdentity = personIdentity;
			} else if (defaultPersonIdentity == null) {
				defaultPersonIdentity = personIdentity;
			}
		}

		return defaultPersonIdentity;
	}

	public List<Object> getTicketsWithDelimiters(Long serviceOrganisationId,
												 Date dateFrom, Date dateTill) throws FlexPayException {
		List<Ticket> ticketList = ticketDao.findByOrganisationAndInterval(
				serviceOrganisationId, dateFrom, dateTill);
		if (ticketList.isEmpty()) {
			return Collections.emptyList();
		}
		List<Object> result = new ArrayList<Object>();
		Iterator<Ticket> it = ticketList.iterator();
		Ticket lastTicket = it.next();
		result.add(getAddressStr(lastTicket, false));
		result.add(lastTicket);
		while (it.hasNext()) {
			Ticket ticket = it.next();
			if (lastTicket.getPerson().getId() == ticket.getPerson().getId()
				&& lastTicket.getDateTill().equals(ticket.getDateTill())
				&& lastTicket.getApartment().getId() == ticket
					.getApartment().getId()) {
				continue;
			}

			if (ticket.getApartment().getBuilding().getId() != lastTicket
					.getApartment().getBuilding().getId()) {
				result.add(getAddressStr(ticket, false));
			}
			result.add(ticket);
			lastTicket = ticket;
		}

		return result;
	}

	private String getAddressStr(Ticket ticket, boolean withApartmentNumber)
			throws FlexPayException {
		Set<Buildings> buildingsSet = ticket.getApartment().getBuilding()
				.getBuildingses();
		if (buildingsSet.isEmpty()) {
			return null;
		}
		Buildings buildings = buildingsSet.iterator().next();
		Street street = buildings.getStreet();
		StreetName streetName = street.getCurrentName();
		StreetNameTranslation streetNameTranslation = TranslationUtil
				.getTranslation(streetName.getTranslations());
		StreetType streetType = street.getCurrentType();
		StreetTypeTranslation streetTypeTranslation = TranslationUtil
				.getTranslation(streetType.getTranslations());

		return streetTypeTranslation.getName()
			   + " "
			   + streetNameTranslation.getName()
			   + ", д."
			   + buildings.getNumber()
			   + (withApartmentNumber ? ", кв."
										+ ticket.getApartment().getNumber() : "");
	}

	@Transactional (readOnly = false)
	public void payTicket(Long ticketId) {
		Ticket ticket = ticketDao.read(ticketId);
		Set<TicketServiceAmount> ticketServiceAmountSet = ticket
				.getTicketServiceAmounts();
		Date operationDate = new Date();
		for (TicketServiceAmount ticketServiceAmount : ticketServiceAmountSet) {
			BigDecimal amount = ticketServiceAmount.getDateTillAmount();
			if (amount == null || amount.compareTo(BigDecimal.ZERO) >= 0) {
				continue;
			}
//			AccountRecord record = new AccountRecord();
//			record.setAmount(amount.abs());
//			record.setConsumer(ticketServiceAmount.getConsumer());
//			record.setOperationDate(operationDate);
//			record.setRecordType(new AccountRecordType((long) AccountRecordType.TYPE_PAYMENT));

			// todo save record
//			accountRecordService.create(record);
		}
	}

	/**
	 * @param serviceOrganisationService the serviceOrganisationService to set
	 */
	public void setServiceOrganisationService(
			ServiceOrganisationService serviceOrganisationService) {
		this.serviceOrganisationService = serviceOrganisationService;
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
