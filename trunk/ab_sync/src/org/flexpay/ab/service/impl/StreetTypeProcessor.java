package org.flexpay.ab.service.impl;

import org.flexpay.ab.persistence.HistoryRec;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashSet;
import java.util.Set;

import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;

/**
 * Dummy implementation, does nothing usefull
 */
public class StreetTypeProcessor extends AbstractProcessor<StreetType> {

	private StreetTypeService streetTypeService;

	public StreetTypeProcessor() {
		super(StreetType.class);
	}

	/**
	 * Create new DomainObject
	 *
	 * @return DomainObject
	 * @throws Exception if failure occurs
	 */
	@NotNull
    @Override
	protected StreetType doCreateObject() throws Exception {
		return new StreetType();
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
    @Override
	protected StreetType readObject(@NotNull Stub<StreetType> stub) {
		return streetTypeService.readFull(stub);
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
    @Override
	public void setProperty(@NotNull DomainObject object, @NotNull HistoryRec record, Stub<DataSourceDescription> sd, CorrectionsService cs)
			throws Exception {

		log.debug("About to set property: {}", record.getFieldType());

		StreetType streetType = (StreetType) object;
		switch (record.getFieldType()) {
			case StreetType:
				Translation typeTranslation = TranslationUtil.getTranslation(streetType.getTranslations());
				if (typeTranslation != null) {
					String name = typeTranslation.getName();

					if (name.equals(record.getCurrentValue())) {
						log.debug("History street type name is the same as in DB: {}", name);
						return;
					}
				}

				if (log.isDebugEnabled()) {
					log.debug("Setting street type name, object is new: {}", object.getId() == null);
				}

				setName(streetType, record.getCurrentValue());
				break;
			default:
				log.debug("Unknown street type field: {}", record.getFieldType());
		}
	}

	private void setName(StreetType streetType, String name) throws Exception {
		StreetTypeTranslation translation = new StreetTypeTranslation();
		translation.setLang(getDefaultLanguage());
		translation.setName(name);
		translation.setTranslatable(streetType);

		Set<StreetTypeTranslation> translations = new HashSet<StreetTypeTranslation>();
		translations.add(translation);
		streetType.setTranslations(translations);
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
	protected Stub<StreetType> findPersistentObject(StreetType object, Stub<DataSourceDescription> sd, CorrectionsService cs) {

		if (object.getTranslations().isEmpty()) {
			return null;
		}

		try {
			StreetTypeTranslation translation = object.getTranslations().iterator().next();
			if (translation == null) {
				return null;
			}

			// Try to find general correction by type name
			Stub<StreetType> correction = cs.findCorrection(translation.getName(), StreetType.class, sd);
			if (correction != null) {
				return correction;
			}

			return streetTypeService.findTypeByName(translation.getName());

		} catch (FlexPayException e) {
			log.info("Cannot find persistent street type by example", e);
			return null;
		}
	}

	/**
	 * Save DomainObject
	 *
	 * @param object	 Object to save
	 * @param externalId External object identifier
	 */
    @Override
	protected void doSaveObject(StreetType object, String externalId) throws Exception {

		if (object.isNew()) {
			streetTypeService.create(object);
		} else {
			streetTypeService.update(object);
		}
	}

	@Required
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}
}
