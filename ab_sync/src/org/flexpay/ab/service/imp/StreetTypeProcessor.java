package org.flexpay.ab.service.imp;

import org.flexpay.ab.dao.StreetTypeDao;
import org.flexpay.ab.persistence.HistoryRecord;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.util.TranslationUtil;

import java.util.Set;
import java.util.HashSet;

/**
 * Dummy implementation, does nothing usefull
 */
public class StreetTypeProcessor extends AbstractProcessor<StreetType> {

	private StreetTypeDao streetTypeDao;

	public StreetTypeProcessor() {
		super(StreetType.class);
	}

	/**
	 * Create new DomainObject
	 *
	 * @return DomainObject
	 * @throws Exception if failure occurs
	 */
	protected StreetType doCreateObject() throws Exception {
		return new StreetType();
	}

	/**
	 * Read full object info
	 *
	 * @param stub Object id container
	 * @return DomainObject instance
	 */
	protected StreetType readObject(StreetType stub) {
		return streetTypeDao.read(stub.getId());
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
	public void setProperty(DomainObject object, HistoryRecord record, DataSourceDescription sd, CorrectionsService cs)
			throws Exception {
		StreetType streetType = (StreetType) object;
		switch (record.getFieldType()) {
			case StreetType:
				String name = TranslationUtil.getTranslation(streetType.getTranslations()).getName();

				if (name.equals(record.getCurrentValue())) {
					log.info("History street type name is the same as in DB: " + name);
					return;
				}

				setName(streetType, record.getCurrentValue());
				break;
			default:
				log.info("Unknown street type field: " + record.getFieldType());
		}
	}

	private void setName(StreetType streetType, String name) throws Exception {
		StreetTypeTranslation translation = new StreetTypeTranslation();
		translation.setLang(ApplicationConfig.getInstance().getDefaultLanguage());
		translation.setName(name);
		translation.setTranslatable(streetType);

		Set<StreetTypeTranslation> translations = new HashSet<StreetTypeTranslation>();
		translations.add(translation);
		streetType.setTranslations(translations);
	}

	/**
	 * Save DomainObject
	 *
	 * @param object Object to save
	 */
	protected void doSaveObject(StreetType object) {
		if (object.getId() != null) {
			streetTypeDao.update(object);
		} else {
			streetTypeDao.create(object);
		}
	}
}