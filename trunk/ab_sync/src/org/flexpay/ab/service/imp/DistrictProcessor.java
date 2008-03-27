package org.flexpay.ab.service.imp;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.ab.dao.DistrictDao;
import org.flexpay.ab.dao.TownDao;
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

public class DistrictProcessor extends AbstractProcessor<District> {

	private DistrictDao districtDao;
	private TownDao townDao;

	public DistrictProcessor() {
		super(District.class);
	}

	/**
	 * Create new DomainObject
	 */
	protected District doCreateObject() throws Exception {

		District district = new District();
		district.setParent(ApplicationConfig.getInstance().getDefaultTown());

		return district;
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected District readObject(District stub) {
		return districtDao.readFull(stub.getId());
	}

	private void setName(District district, String name, Date updateDate) throws Exception {
		DistrictName districtName = new DistrictName();

		DistrictNameTranslation translation = new DistrictNameTranslation();
		translation.setLang(ApplicationConfig.getInstance().getDefaultLanguage());
		translation.setName(name);
		translation.setTranslatable(districtName);
		Set<DistrictNameTranslation> translations = new HashSet<DistrictNameTranslation>();
		translations.add(translation);

		districtName.setTranslations(translations);
		districtName.setObject(district);

		DistrictNameTemporal nameTemporal = new DistrictNameTemporal();
		nameTemporal.setBegin(DateUtils.truncate(updateDate, Calendar.DAY_OF_MONTH));
		nameTemporal.setObject(district);
		nameTemporal.setValue(districtName);

		TimeLine<DistrictName, DistrictNameTemporal> timeLine = district.getNamesTimeLine();
		timeLine = DateIntervalUtil.addInterval(timeLine, nameTemporal);

		district.setNamesTimeLine(timeLine);
	}

	private void setTownId(District district, HistoryRecord record, DataSourceDescription sd, CorrectionsService cs)
			throws Exception {

		Town stub = cs.findCorrection(record.getCurrentValue(), Town.class, sd);
		if (stub == null) {
			throw new RuntimeException(String.format("No correction for town #%s DataSourceDescription %d, " +
					"cannot set up reference for district", record.getCurrentValue(), sd.getId()));
		}

		if (townDao.read(stub.getId()) == null) {
			throw new RuntimeException(String.format("Correction for town #%s DataSourceDescription %d is invalid, " +
					"no town with id %d, cannot set up reference for district",
					record.getCurrentValue(), sd.getId(), stub.getId()));
		}

		district.setParent(stub);
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

		District district = (District) object;
		if (PROP_NAME.equals(record.getFieldName())) {
			DistrictName districtName = district.getCurrentName();
			String name = TranslationUtil.getTranslation(districtName.getTranslations()).getName();

			if (name.equals(record.getCurrentValue())) {
				log.info("History district name is the same as in DB: " + name);
				return;
			}

			setName(district, name, record.getRecordDate());
			districtDao.update(district);
		} else if (PROP_TOWN_ID.equals(record.getFieldName())) {
			setTownId(district, record, sd, cs);
		}
	}

	/**
	 * Save DomainObject
	 *
	 * @param object Object to save
	 */
	protected void doSaveObject(District object) {
		if (object.getId() == null) {
			districtDao.create(object);
		} else {
			districtDao.update(object);
		}
	}

	public void setDistrictDao(DistrictDao districtDao) {
		this.districtDao = districtDao;
	}

	public void setTownDao(TownDao townDao) {
		this.townDao = townDao;
	}
}