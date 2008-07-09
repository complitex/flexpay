package org.flexpay.eirc.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetName;
import org.flexpay.ab.persistence.StreetNameTranslation;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.eirc.dao.QuittanceDao;
import org.flexpay.eirc.dao.QuittanceDetailsDao;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class QuittanceServiceImpl implements QuittanceService {

	private Logger log = Logger.getLogger(QuittanceServiceImpl.class);

	private QuittanceDetailsDao quittanceDetailsDao;
	private QuittanceDao quittanceDao;
	private ServiceOrganisationService serviceOrganisationService;
	private EircAccountService eircAccountService;

	/**
	 * Create or update a QuittanceDetails record
	 * 
	 * @param details
	 *            QuittanceDetails to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *             if validation failure occurs
	 */
	@Transactional(readOnly = false)
	public void save(QuittanceDetails details) throws FlexPayExceptionContainer {
		if (details.isNew()) {
			details.setId(null);
			quittanceDetailsDao.create(details);
		} else {
			quittanceDetailsDao.update(details);
		}
	}

	public void setQuittanceDetailsDao(QuittanceDetailsDao quittanceDetailsDao) {
		this.quittanceDetailsDao = quittanceDetailsDao;
	}

	@Transactional(readOnly = false)
	public void generateForServiceOrganisation(Long serviceOrganisationId,
			Date dateFrom, Date dateTill) {
		ServiceOrganisation serviceOrganisation = serviceOrganisationService
				.read(serviceOrganisationId);
		Set<ServedBuilding> buildingSet = serviceOrganisation.getBuildings();
		for (Building building : buildingSet) {
			generateForBuilding(serviceOrganisation, building, dateFrom,
					dateTill);
		}

	}

	@Transactional(readOnly = false)
	private void generateForBuilding(ServiceOrganisation serviceOrganisation,
			Building building, Date dateFrom, Date dateTill) {
		Set<Apartment> apartmentSet = building.getApartments();
		for (Apartment apartment : apartmentSet) {
			generateForApartment(serviceOrganisation, apartment, dateFrom,
					dateTill);
		}
	}

	@Transactional(readOnly = false)
	private void generateForApartment(ServiceOrganisation serviceOrganisation,
			Apartment apartment, Date dateFrom, Date dateTill) {
		List<EircAccount> eircAccountList = eircAccountService
				.findByApartment(apartment.getId());
		for (EircAccount eircAccount : eircAccountList) {
			generateForEircAccount(eircAccount, serviceOrganisation, dateFrom,
					dateTill);
		}
	}

	@Transactional(readOnly = false)
	private void generateForEircAccount(EircAccount eircAccount,
			ServiceOrganisation serviceOrganisation, Date dateFrom,
			Date dateTill) {
		try {

			List<QuittanceDetails> quittanceDetailsList = quittanceDetailsDao
					.findByEircAccountAndDateTill(eircAccount.getId(),
							dateFrom, dateTill);
			if (quittanceDetailsList.isEmpty()) {
				return;
			}
			Quittance quittance = new Quittance();
			quittance.setQuittanceDetails(quittanceDetailsList);
			Date creationDate = new Date();
			quittance.setCreationDate(creationDate);
			quittance.setDateFrom(dateFrom);
			quittance.setDateTill(dateTill);
			quittance.setServiceOrganisation(serviceOrganisation);
			quittance.setEircAccount(eircAccount);
			quittance.setOrderNumber(calculateQuittanceOrderNumber(eircAccount,
					dateTill));

			quittanceDao.create(quittance);
		} catch (Throwable t) {
			log.error("Could't generate quittance for EircAccount id="
					+ eircAccount.getId(), t);
		}
	}

	private Integer calculateQuittanceOrderNumber(EircAccount eircAccount,
			Date dateTill) {
		Page<Quittance> pager = new Page<Quittance>();
		pager.setPageSize(1);
		quittanceDao.findObjectsByEircAccountAndDateTill(pager, eircAccount
				.getId(), dateTill);
		return pager.getTotalNumberOfElements();
	}

	public List<Object> getQuittanceListWithDelimiters(
			Long serviceOrganisationId, Date dateFrom, Date dateTill)
			throws FlexPayException {
		List<Quittance> quittanceList = quittanceDao
				.findByServiceOrganisationAndDate(serviceOrganisationId,
						dateTill);

		List<Object> result = new ArrayList<Object>();
		Quittance lastQuittance = null;
		for (Quittance quittance : quittanceList) {
			if (lastQuittance == null || quittance.getEircAccount().getApartment().getBuilding().getId().longValue() != 
					                     lastQuittance.getEircAccount().getApartment().getBuilding().getId().longValue()) {
				result.add(getAddressStr(quittance, false));
				result.add(quittance);
				lastQuittance = quittance;
				continue;
			}
			
			if(quittance.getEircAccount().getId().longValue() != lastQuittance.getEircAccount().getId().longValue()) {
				result.add(quittance);
			}
			
			lastQuittance = quittance;
		}

		return result;
	}

	public String getAddressStr(Quittance quittance,
			boolean withApartmentNumber) throws FlexPayException {
		quittance = quittanceDao.read(quittance.getId());
		Set<Buildings> buildingsSet = quittance.getEircAccount().getApartment()
				.getBuilding().getBuildingses();
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
						+ quittance.getEircAccount().getApartment().getNumber()
						: "");
	}
	
	public String getPayer(Quittance quittance) {
		quittance = quittanceDao.read(quittance.getId());
		PersonIdentity personIdentity = quittance.getEircAccount().getPerson().getPassportIdentity();
		if(personIdentity == null) {
			quittance.getEircAccount().getPerson().getForeignPassportIdentity();
		}
		if(personIdentity == null) {
			quittance.getEircAccount().getPerson().getDefaultIdentity();
		}
		
		if (personIdentity != null) {
			return personIdentity.getFirstName() + " "
					+ personIdentity.getMiddleName() + " "
					+ personIdentity.getLastName();
		} else {
			return null;
		}
	}
	
	public QuittanceDetails calculateTotalQuittanceDetails(Quittance quittance, ServiceType serviceType) {
		quittance = quittanceDao.read(quittance.getId());
		return quittance.calculateTotalQuittanceDetails(serviceType);
	}
	
	

	/**
	 * @param serviceOrganisationService
	 *            the serviceOrganisationService to set
	 */
	public void setServiceOrganisationService(
			ServiceOrganisationService serviceOrganisationService) {
		this.serviceOrganisationService = serviceOrganisationService;
	}

	/**
	 * @param eircAccountService
	 *            the eircAccountService to set
	 */
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

	/**
	 * @param quittanceDao
	 *            the quittanceDao to set
	 */
	public void setQuittanceDao(QuittanceDao quittanceDao) {
		this.quittanceDao = quittanceDao;
	}
}
