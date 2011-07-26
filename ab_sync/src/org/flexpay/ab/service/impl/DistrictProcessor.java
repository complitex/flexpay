package org.flexpay.ab.service.impl;

import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.persistence.DistrictNameTranslation;
import org.flexpay.ab.persistence.HistoryRec;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;
import java.util.List;

import static org.flexpay.ab.util.config.ApplicationConfig.getDefaultTown;
import static org.flexpay.common.persistence.Stub.stub;

public class DistrictProcessor extends AbstractProcessor<District> {

	private DistrictService districtService;

	public DistrictProcessor() {
		super(District.class);
	}

	/**
	 * Create new DomainObject
	 */
	@NotNull
    @Override
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
    @Override
	protected District readObject(@NotNull Stub<District> stub) {
		return districtService.readFull(stub);
	}

	private void setName(District district, String name, Date updateDate) throws Exception {

		DistrictName districtName = new DistrictName();
		districtName.setTranslation(new DistrictNameTranslation(name));
		districtName.setObject(district);

		district.setNameForDate(districtName, updateDate);
	}

	/**
	 * Update DomainObject from HistoryRecord
	 *
	 * @param record HistoryRecord
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 */
    @Override
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
    @Override
	protected Stub<District> findPersistentObject(District object, Stub<DataSourceDescription> sd, CorrectionsService cs) {
		DistrictName name = object.getCurrentName();
		if (name == null || name.getTranslations().isEmpty()) {
			log.debug("No current name found");
			return null;
		}
		String nameStr = name.getDefaultNameTranslation();
		if (nameStr == null) {
			log.debug("No default name translation");
			return null;
		}

		List<District> districts = districtService.findByTownAndName(object.getTownStub(), nameStr);

		log.debug("Looked up for {}, found {}", nameStr, districts.size());
		if (districts.isEmpty()) {
			return null;
		}

		if (districts.size() > 1) {
			log.warn("Found several similar districts: {}", districts);
		}
		return stub(districts.get(0));
	}

	/**
	 * Save DomainObject
	 *
	 * @param object	 Object to save
	 * @param externalId External object identifier
	 */
    @Override
	protected void doSaveObject(District object, String externalId) throws FlexPayExceptionContainer {
		if (log.isDebugEnabled()) {
			for (DistrictName name : object.getNamesTimeLine().getValues()) {
				log.debug("Name object: {}", name.getObject());
			}
		}
		if (object.getId() == null) {
			districtService.create(object);
		} else {
			districtService.update(object);
		}
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}
}
