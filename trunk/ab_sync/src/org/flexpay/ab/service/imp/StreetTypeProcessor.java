package org.flexpay.ab.service.imp;

import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.persistence.HistoryRecord;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.TranslationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Dummy implementation, does nothing usefull
 */
public class StreetTypeProcessor extends AbstractProcessor<StreetType> {

	private StreetTypeDao streetTypeDao;
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
	protected StreetType doCreateObject() throws Exception {
		return new StreetType();
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected StreetType readObject(@NotNull Stub<StreetType> stub) {
		return streetTypeDao.readFull(stub.getId());
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
		if (log.isDebugEnabled()) {
			log.debug("About to set property: " + record.getFieldType());
		}

		StreetType streetType = (StreetType) object;
		switch (record.getFieldType()) {
			case StreetType:
				Translation typeTranslation = TranslationUtil.getTranslation(streetType.getTranslations());
				if (typeTranslation != null) {
					String name = typeTranslation.getName();

					if (name.equals(record.getCurrentValue())) {
						if (log.isDebugEnabled()) {
							log.debug("History street type name is the same as in DB: " + name);
						}
						return;
					}
				}

				if (log.isDebugEnabled()) {
					log.debug("Setting street type name, object is new: " + (object.getId() == null));
				}

				setName(streetType, record.getCurrentValue());
				break;
			default:
				if (log.isDebugEnabled()) {
					log.debug("Unknown street type field: " + record.getFieldType());
				}
		}
	}

	private void setName(StreetType streetType, String name) throws Exception {
		StreetTypeTranslation translation = new StreetTypeTranslation();
		translation.setLang(ApplicationConfig.getDefaultLanguage());
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
	protected Stub<StreetType> findPersistentObject(StreetType object, DataSourceDescription sd, CorrectionsService cs) {

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

			StreetType type = streetTypeService.findTypeByName(translation.getName());
			return type != null ? stub(type) : null;

		} catch (FlexPayException e) {
			log.info("Cannot find persistent street type by example: ", e);
			return null;
		}
	}

	/**
	 * Save DomainObject
	 *
	 * @param object Object to save
	 * @param externalId External object identifier
	 */
	protected void doSaveObject(StreetType object, String externalId) throws Exception {

		streetTypeService.save(object);
	}

	public void setStreetTypeDao(StreetTypeDao streetTypeDao) {
		this.streetTypeDao = streetTypeDao;
	}

	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}
}
