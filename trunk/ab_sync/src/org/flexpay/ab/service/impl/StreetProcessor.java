package org.flexpay.ab.service.impl;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.service.StreetTypeService;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultTown;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

public class StreetProcessor extends AbstractProcessor<Street> {

	private StreetTypeService streetTypeService;
	private StreetService streetService;

	public StreetProcessor() {
		super(Street.class);
	}

	/**
	 * Create new DomainObject from HistoryRecord
	 */
	@NotNull
	protected Street doCreateObject() throws Exception {

		Street street = new Street();
		street.setParent(getDefaultTown());

		return street;
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected Street readObject(@NotNull Stub<Street> stub) {
		return streetService.readFull(stub);
	}

	private void setName(Street street, String name, Date updateDate) throws Exception {

		StreetName streetName = new StreetName();
		streetName.setObject(street);
		streetName.setTranslation(new StreetNameTranslation(name));

		street.setNameForDate(streetName, updateDate);
	}

	private void setStreetTypeId(Street street, HistoryRec record, Stub<DataSourceDescription> sd, CorrectionsService cs) {
		Stub<StreetType> stub = cs.findCorrection(record.getCurrentValue(), StreetType.class, sd);
		if (stub == null) {
			log.error("No correction for street type #{} DataSourceDescription {}, cannot set up reference for street",
					record.getCurrentValue(), sd.getId());
			return;
		}

		StreetType persistentType = street.getTypeForDate(record.getRecordDate());
		if (persistentType != null && stub.sameId(persistentType)) {
			// nothing to do
			return;
		}

		street.setTypeForDate(streetTypeService.read(stub), record.getRecordDate());
	}

	/**
	 * Update DomainObject from HistoryRecord
	 *
	 * @param record HistoryRecord
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 */
	public void setProperty(@NotNull DomainObject object, @NotNull HistoryRec record, Stub<DataSourceDescription> sd, CorrectionsService cs)
			throws Exception {

		Street street = (Street) object;
		switch (record.getFieldType()) {
			case StreetName:
				StreetName streetName = street.getCurrentName();
				if (streetName != null) {
					String name = TranslationUtil.getTranslation(streetName.getTranslations()).getName();

					if (name.equals(record.getCurrentValue())) {
						log.debug("History street name is the same as in DB: {}", name);
						return;
					}
				}

				setName(street, record.getCurrentValue(), record.getRecordDate());
				break;
			case StreetTypeId:
				setStreetTypeId(street, record, sd, cs);
				break;
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
	protected Stub<Street> findPersistentObject(Street object, Stub<DataSourceDescription> sd, CorrectionsService cs) {
		StreetName name = object.getCurrentName();
		StreetType type = object.getCurrentType();
		if (name == null || name.getTranslations().isEmpty() || type == null) {
			log.debug("No current name or type found");
			return null;
		}
		String nameStr = name.getTranslations().iterator().next().getName();

		List<Street> streets = streetService.findByName(nameStr, new TownFilter(object.getParent().getId()));

		log.debug("Looked up for {}, found {}", nameStr, streets.size());
		if (streets.isEmpty()) {
			return null;
		}

		streets = filterStreetsByType(streets, type);

		if (streets.isEmpty()) {
			log.debug("All candidates filtered by type");
			return null;
		}

		if (streets.size() > 1) {
			log.warn("Found several similar streets: {}", streets);
		}

		return stub(streets.get(0));
	}

	@NotNull
	private List<Street> filterStreetsByType(@NotNull List<Street> streets, @NotNull StreetType type) {
		List<Street> result = CollectionUtils.list();
		for (Street street : streets) {
			StreetType currentType = street.getCurrentType();
			if (currentType == null) {
				log.warn("No type for street: {}", street);
				continue;
			}

			if (currentType.equals(type)) {
				result.add(street);
			}
		}

		return result;
	}

	/**
	 * Save DomainObject
	 *
	 * @param object	 Object to save
	 * @param externalId External object identifier
	 */
	protected void doSaveObject(Street object, String externalId) throws FlexPayExceptionContainer {
		if (object.getId() == null) {
			streetService.create(object);
		} else {
			streetService.update(object);
		}
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}
}
