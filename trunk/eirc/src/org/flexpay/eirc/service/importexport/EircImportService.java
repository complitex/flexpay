package org.flexpay.eirc.service.importexport;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.NameTimeDependentChild;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.TemporaryName;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.util.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EircImportService {

	private Logger log = LoggerFactory.getLogger(getClass());

	private StreetService streetService;
	private EircImportServiceTx eircImportServiceTx;

	public void importConsumers(DataSourceDescription sd, RawDataSource<RawConsumerData> dataSource)
			throws FlexPayException {

		log.info("Starting importing consumers for data source: {}", sd.getId());

		Town defaultTown = ApplicationConfig.getDefaultTown();
		ArrayStack filters = new ArrayStack();
		filters.push(new TownFilter(defaultTown.getId()));
		List<Street> townStreets = streetService.find(filters);

		Map<String, List<Street>> nameObjsMap = initializeNamesToObjectsMap(townStreets);

		if (log.isInfoEnabled()) {
			log.info("Streets number: {}", nameObjsMap.keySet().size());
		}

		// records count + skipped data read
		long[] counters = {0, 0};

		boolean hasMoreData;
		boolean inited = false;
		do {
			log.debug("Start fetching for next batch");
			hasMoreData = eircImportServiceTx.processBatch(
					counters, inited, sd, dataSource, nameObjsMap);
			inited = true;
		} while (hasMoreData);

		log.debug("Imported {} records. Skipped: {}", counters[0], counters[1]);
	}

	/**
	 * Build mapping from object names to objects themself
	 *
	 * @param ntds List of objects
	 * @return mapping
	 * @throws org.flexpay.common.exception.FlexPayException
	 *          if language configuration is invalid
	 */
	@SuppressWarnings ({"unchecked"})
	protected <NTD extends NameTimeDependentChild> Map<String, List<NTD>> initializeNamesToObjectsMap(List<NTD> ntds)
			throws FlexPayException {

		Map<String, List<NTD>> stringNTDMap = new HashMap<String, List<NTD>>(ntds.size());
		for (NTD object : ntds) {
			TemporaryName tmpName = (TemporaryName) object.getCurrentName();
			if (tmpName == null) {
				log.error("No current name for object: {}", object);
				continue;
			}
			Translation defTranslation = getDefaultLangTranslation(tmpName.getTranslations());
			String name = defTranslation.getName().toLowerCase();
			List<NTD> val = stringNTDMap.containsKey(name) ?
							stringNTDMap.get(name) : new ArrayList<NTD>();
			val.add(object);
			stringNTDMap.put(name, val);
		}

		return stringNTDMap;
	}

	private Translation getDefaultLangTranslation(Collection<? extends Translation> translations)
			throws FlexPayException {

		Long defaultLangId = org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage().getId();
		for (Translation translation : translations) {
			if (stub(translation.getLang()).getId().equals(defaultLangId)) {
				return translation;
			}
		}

		throw new IllegalArgumentException("No default lang translation found");
	}

	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	public void setEircImportServiceTx(EircImportServiceTx eircImportServiceTx) {
		this.eircImportServiceTx = eircImportServiceTx;
	}
}
