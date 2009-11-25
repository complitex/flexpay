package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.StreetTypeFilter;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

/**
 * Street simple editor
 */
public class StreetEditAction extends FPActionSupport {

	private Street street = new Street();
	private Long countryFilter;
	private Long regionFilter;
	private Long townFilter;
	private StreetTypeFilter streetTypeFilter = new StreetTypeFilter();
	private BeginDateFilter beginDateFilter = new BeginDateFilter();
	private Map<Long, String> names = treeMap();

	private String crumbCreateKey;
	private StreetTypeService streetTypeService;
	private StreetService streetService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (street == null || street.getId() == null) {
			log.warn("Incorrect street id");
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_ERROR;
		}

		if (street.isNotNew()) {
			Stub<Street> stub = stub(street);
			street = streetService.readWithHierarchy(stub);

			if (street == null) {
				log.warn("Can't get street with id {} from DB", stub.getId());
				addActionError(getText("common.object_not_selected"));
				return REDIRECT_ERROR;
			} else if (street.isNotActive()) {
				log.warn("Street with id {} is disabled", stub.getId());
				addActionError(getText("common.object_not_selected"));
				return REDIRECT_ERROR;
			}

		}

		if (names == null) {
			log.debug("Names parameter is null");
			names = treeMap();
		}

		if (beginDateFilter == null) {
			log.debug("BeginDateFilter parameter is null");
			beginDateFilter = new BeginDateFilter();
		}

		initFilters();

		if (isSubmit()) {
			if (!doValidate()) {
				return INPUT;
			}
			updateStreet();

			addActionMessage(getText("ab.street.saved"));

			return REDIRECT_SUCCESS;
		}

		initData();

		if (street.isNotNew()) {
			townFilter = street.getTown().getId();
			regionFilter = street.getRegion().getId();
			countryFilter = street.getCountry().getId();
		}

		return INPUT;

	}

	/**
	 * Creates new street if it is a new one
	 * (haven't been yet persisted) or updates persistent one
	 *
	 * @throws FlexPayExceptionContainer if some errors
	 */
	private void updateStreet() throws FlexPayExceptionContainer {

		StreetName streetName = new StreetName();
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			streetName.setTranslation(new StreetNameTranslation(value, lang));
		}

		street.setNameForDate(streetName, beginDateFilter.getDate());
		street.setTypeForDate(new StreetType(streetTypeFilter.getSelectedStub()), beginDateFilter.getDate());

		if (street.isNew()) {
			street.setParent(new Town(townFilter));
			streetService.create(street);
		} else {
			streetService.update(street);
		}

	}

	private boolean doValidate() {

		if (townFilter == null || townFilter <= 0) {
			log.warn("Incorrect town id in filter ({})", townFilter);
			addActionError(getText("ab.error.street.no_town"));
		}

		if (!streetTypeFilter.needFilter()) {
			log.warn("Incorrect street type id in filter ({})", streetTypeFilter);
			addActionError(getText("ab.error.street.no_type"));
		}

		if (!beginDateFilter.needFilter()) {
			log.warn("Incorrect begin date in filter ({})", beginDateFilter);
			addActionError(getText("ab.error.street.no_begin_date"));
		}

		return !hasActionErrors();
	}


	private void initFilters() throws Exception {
		streetTypeFilter = streetTypeService.initFilter(streetTypeFilter, getUserPreferences().getLocale());
	}

	private void initData() {

		StreetNameTemporal temporal = street.getCurrentNameTemporal();
		beginDateFilter.setDate(temporal != null ? temporal.getBegin() : DateUtil.now());

		StreetType type = street.getCurrentType();
		if (type != null) {
			streetTypeFilter.setSelectedId(type.getId());
		}

		StreetName streetName = temporal != null ? temporal.getValue() : null;
		if (streetName != null) {
			for (StreetNameTranslation name : streetName.getTranslations()) {
				names.put(name.getLang().getId(), name.getName());
			}
		}

		for (Language lang : getLanguages()) {
			if (!names.containsKey(lang.getId())) {
				names.put(lang.getId(), "");
			}
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

	@Override
	protected void setBreadCrumbs() {
		if (street != null && street.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public Long getCountryFilter() {
		return countryFilter;
	}

	public Long getRegionFilter() {
		return regionFilter;
	}

	public Long getTownFilter() {
		return townFilter;
	}

	public void setTownFilter(Long townFilter) {
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

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
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