package org.flexpay.ab.service.history;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.*;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.history.Diff;
import org.flexpay.common.persistence.history.HistoryGenerator;
import org.flexpay.common.persistence.history.ProcessingStatus;
import org.flexpay.common.service.DiffService;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class TownHistoryGenerator implements HistoryGenerator<Town> {

	protected Logger log = LoggerFactory.getLogger(getClass());

	private TownTypeService townTypeService;
	private TownService townService;
	private DiffService diffService;

	private TownTypeHistoryGenerator townTypeHistoryGenerator;
	private TownHistoryBuilder historyBuilder;

	private StreetTypeService streetTypeService;
	private StreetTypeHistoryGenerator streetTypeHistoryGenerator;

	private DistrictService districtService;
	private DistrictHistoryGenerator districtHistoryGenerator;

	private StreetService streetService;
	private StreetHistoryGenerator streetHistoryGenerator;

	private AddressAttributeTypeService addressAttributeTypeService;
	private AddressAttributeTypeHistoryGenerator addressAttributeTypeHistoryGenerator;

	/**
	 * Do generation
	 *
	 * @param obj Object to generate history for
	 */
	public void generateFor(@NotNull Town obj) {

		log.debug("starting generating history for town {}", obj);

		log.debug("starting generating history for town types");
		// generate history for all town types
		for (TownType type : townTypeService.getEntities()) {
			townTypeHistoryGenerator.generateFor(type);
		}
		log.debug("ended generating history for town types");

		log.debug("starting generating history for street types");
		// generate history for all street types
		for (StreetType type : streetTypeService.getEntities()) {
			streetTypeHistoryGenerator.generateFor(type);
		}
		log.debug("ended generating history for street types");

		log.debug("starting generating history for address attribute types");
		// generate history for all address attribute types
		for (AddressAttributeType type : addressAttributeTypeService.getAttributeTypes()) {
			addressAttributeTypeHistoryGenerator.generateFor(type);
		}
		log.debug("ended generating history for address attribute types");

		// now create town history
		Town town = townService.readFull(stub(obj));
		if (town == null) {
			log.warn("Town not found {}", town);
			return;
		}

		if (!diffService.hasDiffs(town)) {
			log.debug("starting generating history for town {}", town);

			Diff diff = historyBuilder.diff(null, town);
			diff.setProcessingStatus(ProcessingStatus.STATUS_PROCESSED);
			diffService.create(diff);

			log.debug("Ended generating history for town {}", obj);
		} else {
			log.info("Town already has history, do nothing {}", town);
		}

		log.debug("starting generating history for districts");
		// generate history for all town districts
		ArrayStack filters = CollectionUtils.arrayStack(new TownFilter(town.getId()));
		for (District district : districtService.find(filters)) {
			districtHistoryGenerator.generateFor(district);
		}
		log.debug("ended generating history for districts");

		log.debug("starting generating history for streets");
		// generate history for all town streets
		for (Street street : streetService.find(filters)) {
			streetHistoryGenerator.generateFor(street);
		}
		log.debug("ended generating history for streets");
	}

	@Required
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

	@Required
	public void setDiffService(DiffService diffService) {
		this.diffService = diffService;
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	@Required
	public void setTownTypeHistoryGenerator(TownTypeHistoryGenerator townTypeHistoryGenerator) {
		this.townTypeHistoryGenerator = townTypeHistoryGenerator;
	}

	@Required
	public void setHistoryBuilder(TownHistoryBuilder historyBuilder) {
		this.historyBuilder = historyBuilder;
	}

	@Required
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	@Required
	public void setStreetTypeHistoryGenerator(StreetTypeHistoryGenerator streetTypeHistoryGenerator) {
		this.streetTypeHistoryGenerator = streetTypeHistoryGenerator;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	@Required
	public void setDistrictHistoryGenerator(DistrictHistoryGenerator districtHistoryGenerator) {
		this.districtHistoryGenerator = districtHistoryGenerator;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setStreetHistoryGenerator(StreetHistoryGenerator streetHistoryGenerator) {
		this.streetHistoryGenerator = streetHistoryGenerator;
	}

	@Required
	public void setAddressAttributeTypeService(AddressAttributeTypeService addressAttributeTypeService) {
		this.addressAttributeTypeService = addressAttributeTypeService;
	}

	@Required
	public void setAddressAttributeTypeHistoryGenerator(AddressAttributeTypeHistoryGenerator addressAttributeTypeHistoryGenerator) {
		this.addressAttributeTypeHistoryGenerator = addressAttributeTypeHistoryGenerator;
	}
}
