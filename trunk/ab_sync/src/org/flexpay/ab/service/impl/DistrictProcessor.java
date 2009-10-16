package org.flexpay.ab.service.impl;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.ab.dao.DistrictDao;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.util.config.ApplicationConfig;
import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultTown;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.TranslationUtil;
import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DistrictProcessor extends AbstractProcessor<District> {

	private DistrictDao districtDao;
	private DistrictService districtService;

	public DistrictProcessor() {
		super(District.class);
	}

	/**
	 * Create new DomainObject
	 */
	@NotNull
	protected District doCreateObject() throws Exception {

		District district = new District();
		district.setParent(getDefaultTown());

		return district;
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected District readObject(@NotNull Stub<District> stub) {
		return districtDao.readFull(stub.getId());
	}

	private void setName(District district, String name, Date updateDate) throws Exception {
		DistrictName districtName = new DistrictName();

		DistrictNameTranslation translation = new DistrictNameTranslation();
		translation.setLang(getDefaultLanguage());
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
		if (timeLine != null) {
			timeLine = DateIntervalUtil.addInterval(timeLine, nameTemporal);
		} else {
			nameTemporal.setBegin(getPastInfinite());
			timeLine = new TimeLine<DistrictName, DistrictNameTemporal>(nameTemporal);
		}

		district.setNamesTimeLine(timeLine);
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

		District district = (District) object;
		switch (record.getFieldType()) {
			case District:
				DistrictName districtName = district.getCurrentName();
				if (districtName != null) {
					String name = TranslationUtil.getTranslation(districtName.getTranslations()).getName();

					if (name.equals(record.getCurrentValue())) {
						log.debug("History district name is the same as in DB: {}", name);
						return;
					}
				}

				setName(district, record.getCurrentValue(), record.getRecordDate());
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
	protected Stub<District> findPersistentObject(District object, Stub<DataSourceDescription> sd, CorrectionsService cs) {
		DistrictName name = object.getCurrentName();
		if (name == null || name.getTranslations().isEmpty()) {
			return null;
		}
		String nameStr = name.getTranslations().iterator().next().getName();
		List<District> districts = districtService.findByName(nameStr.toLowerCase(), new TownFilter(object.getParent().getId()));
		if (districts.isEmpty()) {
			return null;
		}
		if (districts.size() > 1) {
			log.error("More than 1 district found by name: {}", districts);
		}
		return stub(districts.get(0));
	}

	/**
	 * Save DomainObject
	 *
	 * @param object	 Object to save
	 * @param externalId External object identifier
	 */
	protected void doSaveObject(District object, String externalId) {
		if (object.getId() == null) {
			districtDao.create(object);
		} else {
			districtDao.update(object);
		}
	}

	public void setDistrictDao(DistrictDao districtDao) {
		this.districtDao = districtDao;
	}

	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}
}
