package org.flexpay.ab.service.imp;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StreetProcessor extends AbstractProcessor<Street> {

	private StreetDao streetDao;
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
		street.setParent(ApplicationConfig.getDefaultTown());

		return street;
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected Street readObject(@NotNull Stub<Street> stub) {
		return streetDao.readFull(stub.getId());
	}

	private void setName(Street street, String name, Date updateDate) throws Exception {
		StreetName streetName = new StreetName();

		StreetNameTranslation translation = new StreetNameTranslation();
		translation.setLang(ApplicationConfig.getDefaultLanguage());
		translation.setName(name);
		translation.setTranslatable(streetName);
		Set<StreetNameTranslation> translations = new HashSet<StreetNameTranslation>();
		translations.add(translation);

		streetName.setTranslations(translations);
		streetName.setObject(street);

		StreetNameTemporal nameTemporal = new StreetNameTemporal();
		nameTemporal.setBegin(DateUtils.truncate(updateDate, Calendar.DAY_OF_MONTH));
		nameTemporal.setObject(street);
		nameTemporal.setValue(streetName);

		TimeLine<StreetName, StreetNameTemporal> timeLine = street.getNamesTimeLine();
		if (timeLine != null) {
			timeLine = DateIntervalUtil.addInterval(timeLine, nameTemporal);
		} else {
			nameTemporal.setBegin(ApplicationConfig.getPastInfinite());
			timeLine = new TimeLine<StreetName, StreetNameTemporal>(nameTemporal);
		}

		street.setNamesTimeLine(timeLine);
	}

	private void setStreetTypeId(Street street, HistoryRecord record, DataSourceDescription sd, CorrectionsService cs) {
		Stub<StreetType> stub = cs.findCorrection(record.getCurrentValue(), StreetType.class, sd);
		if (stub == null) {
			log.error("No correction for street type #{} DataSourceDescription {}, " +
					  "cannot set up reference for street", record.getCurrentValue(), sd.getId());
			return;
		}

		StreetType persistentType = street.getTypeForDate(record.getRecordDate());
		if (persistentType != null && stub.getId().equals(persistentType.getId())) {
			// nothing to do
			return;
		}

		StreetTypeTemporal temporal = new StreetTypeTemporal();
		temporal.setBegin(record.getRecordDate());
		temporal.setValue(new StreetType(stub));
		temporal.setObject(street);

		TimeLine<StreetType, StreetTypeTemporal> timeLine = street.getTypesTimeLine();
		if (timeLine == null) {
			temporal.setBegin(ApplicationConfig.getPastInfinite());
			timeLine = new TimeLine<StreetType, StreetTypeTemporal>(temporal);
		} else {
			timeLine = DateIntervalUtil.addInterval(timeLine, temporal);
		}
		street.setTypesTimeLine(timeLine);
	}

	/**
	 * Update DomainObject from HistoryRecord
	 *
	 * @param record HistoryRecord
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 */
	public void setProperty(@NotNull DomainObject object, @NotNull HistoryRecord record, DataSourceDescription sd, CorrectionsService cs)
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
	protected Stub<Street> findPersistentObject(Street object, DataSourceDescription sd, CorrectionsService cs) {
		StreetName name = object.getCurrentName();
		StreetType type = object.getCurrentType();
		if (name == null || name.getTranslations().isEmpty() || type == null) {
			log.debug("No current name or type found");
			return null;
		}
		String nameStr = name.getTranslations().iterator().next().getName();

		List<Street> streets = streetService.findByName(nameStr, new TownFilter(object.getParent().getId()));

		log.debug("Looked up for {}, found ", nameStr, streets.size());
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
	protected void doSaveObject(Street object, String externalId) {
		if (object.getId() == null) {
			streetDao.create(object);
		} else {
			streetDao.update(object);
		}
	}

	public void setStreetDao(StreetDao streetDao) {
		this.streetDao = streetDao;
	}

	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}
}
