package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.StreetTypeFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class StreetEditSimpleAction extends FPActionSupport {

	private Street street = new Street();
	private String townFilter;
	private StreetTypeFilter streetTypeFilter = new StreetTypeFilter();
	private BeginDateFilter beginDateFilter = new BeginDateFilter(DateUtil.now());
	private Map<Long, String> names = CollectionUtils.treeMap();

	private TownService townService;
	private StreetTypeService streetTypeService;
	private StreetService streetService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (street.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		Street strt = street.isNew() ? street : streetService.readFull(stub(street));
		if (strt == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		streetTypeService.initFilter(streetTypeFilter, getLocale());

		if (!isSubmit()) {
			street = strt;
			initData();
			return INPUT;
		}

		Long townFilterLong;
		try {
			townFilterLong = Long.parseLong(townFilter);
		} catch (Exception e) {
			log.warn("Incorrect town id in filter ({})", townFilter);
			addActionError(getText("ab.error.street.no_town"));
			return INPUT;
		}

		// no town selected in filter
		if (!streetTypeFilter.needFilter()) {
			log.debug("!!!!!!!!!!!!!! no street type filter value selected");
			addActionError(getText("ab.error.street.no_type"));
			return INPUT;
		}

		StreetName districtName = new StreetName();
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			districtName.setTranslation(new StreetNameTranslation(value, lang));
		}
		log.debug("Street name to set: {}", districtName);
		strt.setNameForDate(districtName, beginDateFilter.getDate());

		strt.setTypeForDate(new StreetType(streetTypeFilter.getSelectedStub()), beginDateFilter.getDate());

		if (strt.isNew()) {
			strt.setParent(townService.readFull(new Stub<Town>(townFilterLong)));
			streetService.create(strt);
		} else {
			log.debug("Updated names: {}", strt.getNameTemporals());
			streetService.update(strt);
		}

		addActionError(getText("ab.street.saved"));
		return REDIRECT_SUCCESS;
	}

	private void initData() {

		// init begin date filter
		StreetNameTemporal temporal = street.getCurrentNameTemporal();
		if (temporal != null) {
			beginDateFilter.setDate(temporal.getBegin());
		}

		// init type filter
		StreetType type = street.getCurrentType();
		if (type != null) {
			streetTypeFilter.setSelectedId(type.getId());
		}

		// init translations
		StreetName districtName = temporal != null ? temporal.getValue() : null;
		if (districtName != null) {
			for (StreetNameTranslation name : districtName.getTranslations()) {
				names.put(name.getLang().getId(), name.getName());
			}
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (names.containsKey(lang.getId())) {
				continue;
			}
			names.put(lang.getId(), "");
		}
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return INPUT;
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public String getTownFilter() {
		return townFilter;
	}

	public void setTownFilter(String townFilter) {
		this.townFilter = townFilter;
	}

	public StreetTypeFilter getStreetTypeFilter() {
		return streetTypeFilter;
	}

	public void setStreetTypeFilter(StreetTypeFilter streetTypeFilter) {
		this.streetTypeFilter = streetTypeFilter;
	}

	public BeginDateFilter getBeginDateFilter() {
		return beginDateFilter;
	}

	public void setBeginDateFilter(BeginDateFilter beginDateFilter) {
		this.beginDateFilter = beginDateFilter;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

}
