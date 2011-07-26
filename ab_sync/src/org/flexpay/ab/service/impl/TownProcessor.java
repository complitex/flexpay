package org.flexpay.ab.service.impl;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.TimeLine;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;

public class TownProcessor extends AbstractProcessor<Town> {

	private TownService townService;

	public TownProcessor() {
		super(Town.class);
	}

	/**
	 * Create new DomainObject
	 *
	 * @return DomainObject
	 * @throws Exception if failure occurs
	 */
	@NotNull
    @Override
	protected Town doCreateObject()
			throws Exception {

		Town town = new Town();
		town.setParent(ApplicationConfig.getDefaultRegion());

		return town;
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
    @Override
	protected Town readObject(@NotNull Stub<Town> stub) {
		return townService.readFull(stub);
	}

	private void setName(Town town, String name, Date updateDate) throws Exception {
		TownName townName = new TownName();

		TownNameTranslation translation = new TownNameTranslation(name);
		translation.setTranslatable(townName);
		Set<TownNameTranslation> translations = new HashSet<TownNameTranslation>();
		translations.add(translation);

		townName.setTranslations(translations);
		townName.setObject(town);

		TownNameTemporal nameTemporal = new TownNameTemporal();
		nameTemporal.setBegin(DateUtils.truncate(updateDate, Calendar.DAY_OF_MONTH));
		nameTemporal.setObject(town);
		nameTemporal.setValue(townName);

		TimeLine<TownName, TownNameTemporal> timeLine = town.getNamesTimeLine();
		if (timeLine != null) {
			timeLine = DateIntervalUtil.addInterval(timeLine, nameTemporal);
		} else {
			nameTemporal.setBegin(getPastInfinite());
			timeLine = new TimeLine<TownName, TownNameTemporal>(nameTemporal);
		}

		town.setNamesTimeLine(timeLine);
	}

	/**
	 * Update DomainObject from HistoryRecord
	 *
	 * @param record HistoryRecord
	 * @param sd	 DataSourceDescription
	 * @param cs	 CorrectionsService
	 * @throws Exception if failure occurs
	 */
    @Override
	public void setProperty(@NotNull DomainObject object, @NotNull HistoryRec record, Stub<DataSourceDescription> sd, CorrectionsService cs)
			throws Exception {
		Town town = (Town) object;
		switch (record.getFieldType()) {
			case Unknown:
				TownName townName = town.getCurrentName();
				if (townName != null) {
					String name = TranslationUtil.getTranslation(townName.getTranslations()).getName();

					if (name.equals(record.getCurrentValue())) {
						log.debug("History town name is the same as in DB: {}", name);
						return;
					}
				}

				setName(town, record.getCurrentValue(), record.getRecordDate());
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
	protected Stub<Town> findPersistentObject(Town object, Stub<DataSourceDescription> sd, CorrectionsService cs) {
		return null;
	}

	/**
	 * Save DomainObject
	 *
	 * @param object	 Object to save
	 * @param externalId External object identifier
	 */
    @Override
	public void doSaveObject(Town object, String externalId) throws FlexPayExceptionContainer {
		if (object.getId() == null) {
			townService.create(object);
		} else {
			townService.update(object);
		}
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}
}
