package org.flexpay.ab.service.importexport;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.dao.DistrictDao;
import org.flexpay.ab.dao.importexport.DistrictJdbcDataSource;
import org.flexpay.ab.dao.importexport.StreetJdbcDataSource;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.*;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.service.importexport.ImportOperationTypeHolder;
import org.flexpay.common.util.config.ApplicationConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional (readOnly = true, rollbackFor = Exception.class)
public class ImportService {

	private static Logger log = Logger.getLogger(ImportService.class);

	private RawDistrictDataConverter districtDataConverter;
	private RawStreetDataConverter streetDataConverter;

	private DistrictJdbcDataSource districtDataSource;
	private StreetJdbcDataSource streetDataSource;

	private DistrictDao districtDao;

	private CorrectionsService correctionsService;
	private DistrictService districtService;

	@Transactional (readOnly = false)
	public void importDistricts(Town town, DataSourceDescription sourceDescription) {
		ArrayStack filters = new ArrayStack();
		filters.push(new TownFilter(town.getId()));
		List<District> townDistricts = districtService.find(filters);
		districtDataSource.initialize();
		while (districtDataSource.hasNext()) {
			ImportOperationTypeHolder typeHolder = new ImportOperationTypeHolder();
			RawDistrictData data = districtDataSource.next(typeHolder);
			try {
				District district = districtDataConverter.fromRawData(data, sourceDescription, correctionsService);
				district.setParent(town);

				// Find object by correction
				District persistentObj = (District) correctionsService.findCorrection(
						data.getExternalSourceId(), District.class, sourceDescription);

				// Find object by its name
				District nameMatchObj = findObject(townDistricts, district);

				// no corrections found
				if (persistentObj == null) {
					// this is a new object
					if (nameMatchObj == null) {
						districtDao.create(district);
					} else {
						// already existing object
						district = nameMatchObj;
					}
					DataCorrection correction = correctionsService.getStub(
							data.getExternalSourceId(), district, sourceDescription);
					correctionsService.save(correction);
				} else {
					if (nameMatchObj == null) {
						log.warn("Invalid correction found, no object found");
						districtDao.create(district);
						DataCorrection correction = correctionsService.getStub(
								data.getExternalSourceId(), district, sourceDescription);
						correctionsService.save(correction);
					} else {
						// correction found but objects do not match
						if (!nameMatchObj.getId().equals(persistentObj.getId())) {
							log.warn("Found by name object does not match correction");
							// TODO decide what to do here
						} else {
							if (log.isDebugEnabled()) {
								log.debug("Object " + data.getName() + " found");
							}
							System.out.println("Object " + data.getName() + " found");
						}
					}
				}
			} catch (Exception e) {
				log.error("Failed getting district with id: " + data.getExternalSourceId(), e);
				System.out.println("Failed getting district with id: " + data.getExternalSourceId());
				e.printStackTrace(System.out);
			}
		}
	}

	public <NTD extends NameTimeDependentChild> NTD findObject(List<NTD> ntds, NTD newObj)
			throws FlexPayException {

		TemporaryName newName = (TemporaryName) newObj.getCurrentName();
		Translation newTranslation = getDefaultLangTranslation(newName.getTranslations());
		String newObjName = newTranslation.getName().toLowerCase();

		int minDistance = newObjName.length();
		int nCandidates = 0;
		NTD candidate = null;
		for (NTD oldObj : ntds) {
			TemporaryName oldName = (TemporaryName) oldObj.getCurrentName();
			Translation defaultTranslation = getDefaultLangTranslation(oldName.getTranslations());
			String oldObjName = defaultTranslation.getName().toLowerCase();
			if (oldObjName.equals(newObjName)) {
				return oldObj;
			}

			int diff = StringUtils.getLevenshteinDistance(newObjName, oldObjName);
			// allow only two letter difference in handwriting of the name
			if (diff < 3 && diff <= minDistance) {
				if (minDistance > diff) {
					minDistance = diff;
					nCandidates = 0;
				}
				++nCandidates;
				candidate = oldObj;
			}
		}

		if (nCandidates > 1) {
			log.warn("Found more than one candidate, return the last one: " + newObjName);
		}
		return candidate;
	}

	public Translation getDefaultLangTranslation(Collection<? extends Translation> translations)
			throws FlexPayException {

		Long defaultLangId = ApplicationConfig.getInstance().getDefaultLanguage().getId();
		for (Translation translation : translations) {
			if (translation.getLang().getId().equals(defaultLangId)) {
				return translation;
			}
		}

		throw new IllegalArgumentException("No default lang translation found");
	}

	/**
	 * Setter for property 'districtDataConverter'.
	 *
	 * @param districtDataConverter Value to set for property 'districtDataConverter'.
	 */
	public void setDistrictDataConverter(RawDistrictDataConverter districtDataConverter) {
		this.districtDataConverter = districtDataConverter;
	}

	/**
	 * Setter for property 'streetDataConverter'.
	 *
	 * @param streetDataConverter Value to set for property 'streetDataConverter'.
	 */
	public void setStreetDataConverter(RawStreetDataConverter streetDataConverter) {
		this.streetDataConverter = streetDataConverter;
	}

	/**
	 * Setter for property 'districtDataSource'.
	 *
	 * @param districtDataSource Value to set for property 'districtDataSource'.
	 */
	public void setDistrictDataSource(DistrictJdbcDataSource districtDataSource) {
		this.districtDataSource = districtDataSource;
	}

	/**
	 * Setter for property 'streetDataSource'.
	 *
	 * @param streetDataSource Value to set for property 'streetDataSource'.
	 */
	public void setStreetDataSource(StreetJdbcDataSource streetDataSource) {
		this.streetDataSource = streetDataSource;
	}

	/**
	 * Setter for property 'districtDao'.
	 *
	 * @param districtDao Value to set for property 'districtDao'.
	 */
	public void setDistrictDao(DistrictDao districtDao) {
		this.districtDao = districtDao;
	}

	/**
	 * Setter for property 'correctionsService'.
	 *
	 * @param correctionsService Value to set for property 'correctionsService'.
	 */
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	/**
	 * Setter for property 'districtService'.
	 *
	 * @param districtService Value to set for property 'districtService'.
	 */
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}
}
