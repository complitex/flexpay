package org.flexpay.ab.actions.district;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.service.TownService;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class DistrictEditSimpleAction extends FPActionSupport {

	private District district = new District();
	private Long townFilter;
	private BeginDateFilter beginDateFilter = new BeginDateFilter(DateUtil.now());

	private Map<Long, String> names = CollectionUtils.treeMap();

	private String crumbCreateKey;
	private TownService townService;
	private DistrictService districtService;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	@Override
	protected String doExecute() throws Exception {

		District dstrct = district.isNew() ? district : districtService.readFull(stub(district));

		if (isSubmit()) {
			if (!doValidate(dstrct)) {
				return INPUT;
			}
			updateDistrict(dstrct);

			return REDIRECT_SUCCESS;
		}

		district = dstrct;
		initData();

		return INPUT;

	}

	private boolean doValidate(District dstrct) {

		if (dstrct.getId() == null) {
			addActionError(getText("error.invalid_id"));
			return false;
		}

		if (dstrct == null) {
			addActionError(getText("common.object_not_selected"));
			return false;
		}

		if (townFilter == null || townFilter <= 0) {
			log.warn("Incorrect town id in filter ({})", townFilter);
			addActionError(getText("error.ab.district.no_town"));
		}

		if (!beginDateFilter.needFilter()) {
			addActionError(getText("ab.error.town.no_begin_date"));
		}

		return !hasActionErrors();
	}

	/*
	* Creates new district if it is a new one (haven't been yet persisted) or updates persistent one
	*/
	private void updateDistrict(District dstrct) throws FlexPayExceptionContainer {

		DistrictName districtName = getDistrictName();
		dstrct.setNameForDate(districtName, beginDateFilter.getDate());

		if (dstrct.isNew()) {
			dstrct.setParent(new Town(townFilter));
			districtService.create(dstrct);
		} else {
			districtService.update(dstrct);
		}

	}

	private DistrictName getDistrictName() {

		DistrictName districtName = new DistrictName();
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			districtName.setTranslation(new DistrictNameTranslation(value, lang));
		}

		return districtName;
	}

	private void initData() {

		// init begin date filter
		DistrictNameTemporal temporal = district.getCurrentNameTemporal();
		beginDateFilter.setDate(temporal != null ? temporal.getBegin() : ApplicationConfig.getPastInfinite());

		// init translations
		DistrictName districtName = temporal != null ? temporal.getValue() : null;
		if (districtName != null) {
			for (DistrictNameTranslation name : districtName.getTranslations()) {
				names.put(name.getLang().getId(), name.getName());
			}
		}

		for (Language lang : org.flexpay.common.util.config.ApplicationConfig.getLanguages()) {
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

	@Override
	protected void setBreadCrumbs() {
		if (district.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Long getTownFilter() {
		return townFilter;
	}

	public void setTownFilter(Long townFilter) {
		this.townFilter = townFilter;
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
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

}
