package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTemporal;
import org.flexpay.ab.persistence.filters.StreetTypeFilter;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.config.ApplicationConfig;

import java.text.ParseException;

public class StreetEditTypeAction extends FPActionSupport {

	private Street object = new Street();
	private StreetTypeTemporal temporal = new StreetTypeTemporal();
	private StreetTypeFilter streetTypeFilter = new StreetTypeFilter();

	private StreetService streetService;
	private StreetTypeService streetTypeService;

	public String doExecute() throws Exception {
		object = streetService.read(object.getId());
		temporal.setObject(object);

		// for get request set initial data from existing temporal
		if (!isPost()) {
			if (temporal.getId() > 0) {
				for (StreetTypeTemporal typeTemporal : object.getTypeTemporals()) {
					if (typeTemporal.getId().equals(temporal.getId())) {
						temporal = typeTemporal;

						log.info("Found type temporal: " + typeTemporal.getValue());
						break;
					}
				}
			} else {
				temporal.setBegin(DateIntervalUtil.now());
			}
		} else {
			// for new temporals remove id before save
			if (temporal.getId() <= 0) {
				temporal.setId(null);
			}

			temporal.setValue(new StreetType(streetTypeFilter.getSelectedId()));
		}

		// do save 
		if (isPost()) {
			object.addTypeTemporal(temporal);

			if (log.isDebugEnabled()) {
				log.debug("Types: " + object.getTypeTemporals());
				log.debug("Temporal: " + temporal);
			}

			streetService.saveTypes(object);

			return SUCCESS;
		}

		streetTypeService.initFilter(streetTypeFilter, userPreferences.getLocale());
		if (temporal.getValue() != null) {
			streetTypeFilter.setSelectedId(temporal.getValue().getId());
		}

		return INPUT;
	}

	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	public Street getObject() {
		return object;
	}

	public void setObject(Street object) {
		this.object = object;
	}

	public StreetTypeTemporal getTemporal() {
		return temporal;
	}

	public void setTemporal(StreetTypeTemporal temporal) {
		this.temporal = temporal;
	}

	public StreetTypeFilter getStreetTypeFilter() {
		return streetTypeFilter;
	}

	public void setStreetTypeFilter(StreetTypeFilter streetTypeFilter) {
		this.streetTypeFilter = streetTypeFilter;
	}

	public String getDate() {
		if (temporal.getBegin() == null) {
			return "";
		}
		String dt = DateIntervalUtil.format(temporal.getBegin());
		return "-".equals(dt) ? "" : dt;
	}

	public void setDate(String dt) {
		try {
			temporal.setBegin(DateIntervalUtil.parse(dt));
		} catch (ParseException e) {
			temporal.setBegin(ApplicationConfig.getPastInfinite());
		}
	}
}
