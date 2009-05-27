package org.flexpay.ab.service.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.ApartmentDao;
import org.flexpay.ab.dao.BuildingsDao;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.ApartmentNumber;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.HistoryRecord;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.ab.service.ObjectsFactory;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashSet;
import java.util.Set;

public class ApartmentProcessor extends AbstractProcessor<Apartment> {

	private ApartmentDao apartmentDao;
	private BuildingsDao buildingsDao;
	private ApartmentService apartmentService;
	private ObjectsFactory factory;

	public ApartmentProcessor() {
		super(Apartment.class);
	}

	/**
	 * Create new DomainObject
	 *
	 * @return DomainObject
	 * @throws Exception if failure occurs
	 */
	@NotNull
	protected Apartment doCreateObject() throws Exception {
		return factory.newApartment();
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	@Nullable
	protected Apartment readObject(@NotNull Stub<Apartment> stub) {
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
	public void setProperty(@NotNull DomainObject object, @NotNull HistoryRecord record, DataSourceDescription sd, CorrectionsService cs)
			throws Exception {
		@NotNull Apartment apartment = (Apartment) object;
		switch (record.getFieldType()) {
			case Apartment:
				setNumber(apartment, record);
				break;
			case BuildingId:
				setBuildingId(apartment, record, sd, cs);
				break;
			default:
				log.debug("Unknown apartment property type: {}", record.getFieldType());
		}
	}

	private void setBuildingId(@NotNull Apartment apartment, @NotNull HistoryRecord record, DataSourceDescription sd, CorrectionsService cs)
			throws Exception {

		Stub<BuildingAddress> stub = cs.findCorrection(record.getCurrentValue(), BuildingAddress.class, sd);
		if (stub == null) {
			log.error("No correction for buildingAddress #{} DataSourceDescription {}, cannot set up building reference for apartment",
					record.getCurrentValue(), sd.getId());
			return;
		}

		BuildingAddress buildingAddress = buildingsDao.read(stub.getId());
		if (buildingAddress == null) {
			log.error("Correction for buildingAddress #{} DataSourceDescription {} is invalid, no buildingAddress with id {}, cannot set up building reference for apartment",
					new Object[] {record.getCurrentValue(), sd.getId(), stub.getId()});
			return;
		}
		apartment.setBuilding(buildingAddress.getBuilding());
	}

	private void setNumber(@NotNull Apartment apartment, @NotNull HistoryRecord record) {

		String newNumber = record.getCurrentValue();
		String numberStr = apartment.getNumberForDate(record.getRecordDate());
		if (numberStr != null && numberStr.equals(newNumber)) {
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

		if (newNumber == null || StringUtils.isBlank(newNumber)) {
			return;
		}

		// Create a new apartment number and setup its properties
		ApartmentNumber number = new ApartmentNumber();
		number.setBegin(record.getRecordDate());
		number.setEnd(ApplicationConfig.getFutureInfinite());
		number.setValue(newNumber);
		number.setApartment(apartment);

		// Add number to apartment numbers set
		apartment.getApartmentNumbers().add(number);
	}

	/**
	 * Save DomainObject
	 *
	 * @param object	 Object to save
	 * @param externalId External object identifier
	 */
	public void doSaveObject(Apartment object, String externalId) {
		if (object.hasNoBuilding()) {
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
	protected Stub<Apartment> findPersistentObject(Apartment object, DataSourceDescription sd, CorrectionsService cs) {
		if (object.getApartmentNumbers().isEmpty()) {
			return null;
		}

		log.debug("Checking if apartment exists: {} (number: {})", object, object.getNumber());

		if (object.hasNoBuilding()) {
			log.warn("Do not have a building reference");
			return null;
		}

		Stub<Apartment> stub = apartmentService.findApartmentStub(object.getBuilding(), object.getNumber());
		if (stub != null) {
			log.debug("Found apartment stub: {}", stub.getId());
		}

		return stub;
	}

	@Required
	public void setApartmentDao(ApartmentDao apartmentDao) {
		this.apartmentDao = apartmentDao;
	}

	@Required
	public void setBuildingsDao(BuildingsDao buildingsDao) {
		this.buildingsDao = buildingsDao;
	}

	@Required
	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}

	@Required
	public void setFactory(ObjectsFactory factory) {
		this.factory = factory;
	}
}
