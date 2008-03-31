package org.flexpay.ab.service.imp;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.ab.dao.StreetDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.TranslationUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class StreetProcessor extends AbstractProcessor<Street> {

	private StreetDao streetDao;

	public StreetProcessor() {
		super(Street.class);
	}

	/**
	 * Create new DomainObject from HistoryRecord
	 */
	protected Street doCreateObject() throws Exception {

		Street street = new Street();
		street.setParent(ApplicationConfig.getInstance().getDefaultTown());

		return street;
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected Street readObject(Street stub) {
		return streetDao.readFull(stub.getId());
	}

	private void setName(Street street, String name, Date updateDate) throws Exception {
		StreetName streetName = new StreetName();

		StreetNameTranslation translation = new StreetNameTranslation();
		translation.setLang(ApplicationConfig.getInstance().getDefaultLanguage());
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
			nameTemporal.setBegin(ApplicationConfig.getInstance().getPastInfinite());
			timeLine = new TimeLine<StreetName, StreetNameTemporal>(nameTemporal);
		}

		street.setNamesTimeLine(timeLine);
	}

	private void setStreetTypeId(Street street, HistoryRecord record, DataSourceDescription sd, CorrectionsService cs) {
		StreetType stub = cs.findCorrection(record.getCurrentValue(), StreetType.class, sd);
		if (stub == null) {
			throw new RuntimeException(String.format("No correction for street type #%s DataSourceDescription %d, " +
					"cannot set up reference for street", record.getCurrentValue(), sd.getId()));
		}

		StreetType persistentType = street.getTypeForDate(record.getRecordDate());
		if (persistentType != null && stub.getId().equals(persistentType.getId())) {
			// nothing to do
			return;
		}

		StreetTypeTemporal temporal = new StreetTypeTemporal();
		temporal.setBegin(record.getRecordDate());
		temporal.setValue(stub);
		temporal.setObject(street);

		TimeLine<StreetType, StreetTypeTemporal> timeLine = street.getTypesTimeLine();
		timeLine = DateIntervalUtil.addInterval(timeLine, temporal);
		street.setTypesTimeLine(timeLine);
	}

	/**
	 * Update DomainObject from HistoryRecord
	 *
	 * @param record HistoryRecord
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 */
	public void setProperty(DomainObject object, HistoryRecord record, DataSourceDescription sd, CorrectionsService cs)
			throws Exception {

		Street street = (Street) object;
		switch (record.getFieldType()) {
			case StreetName:
				StreetName streetName = street.getCurrentName();
				if (streetName != null) {
					String name = TranslationUtil.getTranslation(streetName.getTranslations()).getName();

					if (name.equals(record.getCurrentValue())) {
						log.info("History street name is the same as in DB: " + name);
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
	 * Save DomainObject
	 *
	 * @param object Object to save
	 */
	protected void doSaveObject(Street object) {
		if (object.getId() == null) {
			streetDao.create(object);
		} else {
			streetDao.update(object);
		}
	}

	public void setStreetDao(StreetDao streetDao) {
		this.streetDao = streetDao;
	}
}
