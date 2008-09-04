package org.flexpay.eirc.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.ab.persistence.*;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.eirc.dao.QuittanceDao;
import org.flexpay.eirc.dao.QuittanceDaoExt;
import org.flexpay.eirc.dao.QuittanceDetailsDao;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.service.QuittanceService;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class QuittanceServiceImpl implements QuittanceService {

	private Logger log = Logger.getLogger(getClass());

	private QuittanceDetailsDao quittanceDetailsDao;
	private QuittanceDao quittanceDao;

	private QuittanceDaoExt quittanceDaoExt;

	/**
	 * Create or update a QuittanceDetails record
	 *
	 * @param details QuittanceDetails to save
	 * @throws org.flexpay.common.exception.FlexPayExceptionContainer
	 *          if validation failure occurs
	 */
	@Transactional (readOnly = false)
	public void save(QuittanceDetails details) throws FlexPayExceptionContainer {
		if (details.isNew()) {
			details.setId(null);
			quittanceDetailsDao.create(details);
		} else {
			quittanceDetailsDao.update(details);
		}
	}

	@Transactional (readOnly = false)
	public void generateForServiceOrganisation(Date dateFrom, Date dateTill) {

		long time = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("Starting quittances generation at " + new Date());
		}

		quittanceDaoExt.createQuittances(dateFrom, dateTill);

		if (log.isInfoEnabled()) {
			log.info("Quittances generation finished, time took: " + (System.currentTimeMillis() - time) + "ms");
		}
	}

	public List<Object> getQuittanceListWithDelimiters(@NotNull Stub<ServiceOrganisation> stub, Date dateFrom, Date dateTill)
			throws FlexPayException {

		List<Quittance> quittanceList = quittanceDao
				.findByServiceOrganisationAndDate(stub.getId(), dateFrom, dateTill);

		List<Object> result = new ArrayList<Object>();
		Quittance lastQuittance = null;
		for (Quittance quittance : quittanceList) {
			if (lastQuittance == null || buildingsDiffer(quittance, lastQuittance)) {
				result.add(getAddressStr(quittance, false));
				result.add(quittance);
				lastQuittance = quittance;
				continue;
			}

			if (!quittance.getEircAccount().equals(lastQuittance.getEircAccount())) {
				result.add(quittance);
			}

			lastQuittance = quittance;
		}

		return result;
	}

	private boolean buildingsDiffer(Quittance q1, Quittance q2) {
		return !q1.getEircAccount().getApartment().getBuilding()
					.equals(q2.getEircAccount().getApartment().getBuilding());
	}

	public String getAddressStr(Quittance quittance, boolean withApartmentNumber) throws FlexPayException {

		quittance = quittanceDao.read(quittance.getId());
		Set<Buildings> buildingses = quittance.getEircAccount().getApartment().getBuilding().getBuildingses();
		if (buildingses.isEmpty()) {
			return null;
		}

		Buildings buildings = buildingses.iterator().next();
		Street street = buildings.getStreet();
		StreetName streetName = street.getCurrentName();
		StreetNameTranslation streetNameTranslation = TranslationUtil
				.getTranslation(streetName.getTranslations());
		StreetType streetType = street.getCurrentType();
		StreetTypeTranslation streetTypeTranslation = TranslationUtil
				.getTranslation(streetType.getTranslations());

		return streetTypeTranslation.getName() + " " + streetNameTranslation.getName()
			   + ", д." + buildings.getNumber()
			   + (withApartmentNumber ? ", кв." + quittance.getEircAccount().getApartment().getNumber() : "");
	}

	public String getPayer(Quittance quittance) {
		quittance = quittanceDao.read(quittance.getId());
		PersonIdentity personIdentity = quittance.getEircAccount().getPerson().getPassportIdentity();
		if (personIdentity == null) {
			quittance.getEircAccount().getPerson().getForeignPassportIdentity();
		}
		if (personIdentity == null) {
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
		return quittance.calculateTotals(serviceType);
	}

	/**
	 * @param quittanceDao the quittanceDao to set
	 */
	public void setQuittanceDao(QuittanceDao quittanceDao) {
		this.quittanceDao = quittanceDao;
	}

	public void setQuittanceDetailsDao(QuittanceDetailsDao quittanceDetailsDao) {
		this.quittanceDetailsDao = quittanceDetailsDao;
	}

	public void setQuittanceDaoExt(QuittanceDaoExt quittanceDaoExt) {
		this.quittanceDaoExt = quittanceDaoExt;
	}
}
