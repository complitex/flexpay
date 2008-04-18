package org.flexpay.ab.service.imp;

import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.dao.BuildingsDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.CorrectionsService;

import java.util.HashSet;
import java.util.Set;

public class ApartmentProcessor extends AbstractProcessor<Apartment> {

	private ApartmentDao apartmentDao;
	private BuildingsDao buildingsDao;
	private ApartmentService apartmentService;

	public ApartmentProcessor() {
		super(Apartment.class);
	}

	/**
	 * Create new DomainObject
	 *
	 * @return DomainObject
	 * @throws Exception if failure occurs
	 */
	protected Apartment doCreateObject() throws Exception {
		return new Apartment();
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected Apartment readObject(Apartment stub) {
		return apartmentDao.readFull(stub.getId());
	}

	/**
	 * Update DomainObject from HistoryRecord
	 *
	 * @param object DomainObject to set properties on
	 * @param record HistoryRecord
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 * @throws Exception if failure occurs
	 */
	public void setProperty(DomainObject object, HistoryRecord record, DataSourceDescription sd, CorrectionsService cs)
			throws Exception {
		Apartment apartment = (Apartment) object;
		switch (record.getFieldType()) {
			case Apartment:
				setNumber(apartment, record);
				break;
			case BuildingId:
				setBuildingId(apartment, record, sd, cs);
				break;
			default:
				log.debug("Unknown apartment property: " + record.getFieldType());
		}
	}

	private void setBuildingId(Apartment apartment, HistoryRecord record, DataSourceDescription sd, CorrectionsService cs)
			throws Exception {

		Buildings stub = cs.findCorrection(record.getCurrentValue(), Buildings.class, sd);
		if (stub == null) {
			log.error(String.format("No correction for buildings #%s DataSourceDescription %d, " +
					"cannot set up building reference for apartment", record.getCurrentValue(), sd.getId()));
			return;
		}

		Buildings buildings = buildingsDao.read(stub.getId());
		if (buildings == null) {
			log.error(String.format("Correction for buildings #%s DataSourceDescription %d is invalid, " +
					"no buildings with id %d, cannot set up building reference for apartment",
					record.getCurrentValue(), sd.getId(), stub.getId()));
			return;
		}
		apartment.setBuilding(buildings.getBuilding());
	}

	private void setNumber(Apartment apartment, HistoryRecord record) {

		String numberStr = apartment.getNumberForDate(record.getRecordDate());
		if (numberStr.equals(record.getCurrentValue())) {
			// nothing to do
			return;
		}

		// Check if apartment numbers is not empty (Collections.EMPTY_SET)
		if (apartment.getApartmentNumbers().isEmpty()) {
			Set<ApartmentNumber> numberSet = new HashSet<ApartmentNumber>();
			apartment.setApartmentNumbers(numberSet);
		}

		// set up previous numbers to end at the record's operation date
		for (ApartmentNumber number : apartment.getApartmentNumbers()) {
			if (number.getEnd().after(record.getRecordDate())) {
				number.setEnd(record.getRecordDate());
			}
		}

		// Create a new apartment number and setup its properties
		ApartmentNumber number = new ApartmentNumber();
		number.setBegin(record.getRecordDate());
		number.setEnd(ApplicationConfig.getInstance().getFutureInfinite());
		number.setValue(record.getCurrentValue());
		number.setApartment(apartment);

		// Add number to apartment numbers set
		apartment.getApartmentNumbers().add(number);
	}

	/**
	 * Save DomainObject
	 *
	 * @param object Object to save
	 */
	public void doSaveObject(Apartment object) {
		if (object.getBuilding() == null) {
			log.warn("Invalid sync data, no building specified");
			return;
		}
		if (object.getId() == null) {
			apartmentDao.create(object);
		} else {
			apartmentDao.update(object);
		}
	}

	/**
	 * Try to find persistent object by set properties
	 *
	 * @param object DomainObject
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 * @return Persistent object stub if exists, or <code>null</code> otherwise
	 */
	protected Apartment findPersistentObject(Apartment object, DataSourceDescription sd, CorrectionsService cs) {
		if (object.getBuilding() == null || object.getApartmentNumbers().isEmpty()) {
			return null;
		}

		if (log.isDebugEnabled()) {
			log.debug("Checking if apartment exists: " + object);
		}

		Apartment stub = apartmentService.findApartmentStub(object.getBuilding(), object.getNumber());
		if (stub != null && log.isDebugEnabled()) {
			log.debug("Found apartment stub: " + stub);
		}

		return stub;
	}

	public void setApartmentDao(ApartmentDao apartmentDao) {
		this.apartmentDao = apartmentDao;
	}

	public void setBuildingsDao(BuildingsDao buildingsDao) {
		this.buildingsDao = buildingsDao;
	}

	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}
}
