package org.flexpay.ab.actions.district;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class DistrictEditAction extends FPActionSupport {

	private District district = new District();
	private Long townFilter;
	private BeginDateFilter beginDateFilter = new BeginDateFilter();

	private Map<Long, String> names = treeMap();

	private String crumbCreateKey;
	private DistrictService districtService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		district = district.isNew() ? district : districtService.readFull(stub(district));

		if (isSubmit()) {
			if (!doValidate()) {
				return INPUT;
			}
			updateDistrict();

			return REDIRECT_SUCCESS;
		}

		initData();

		return INPUT;

	}

	private boolean doValidate() {

		if (district == null) {
			addActionError(getText("common.object_not_selected"));
			return false;
		}

		if (townFilter == null || townFilter <= 0) {
			log.warn("Incorrect town id in filter ({})", townFilter);
			addActionError(getText("error.ab.district.no_town"));
		}

		if (!beginDateFilter.needFilter()) {
			addActionError(getText("ab.error.district.no_begin_date"));
		}

		return !hasActionErrors();
	}

	/*
	* Creates new district if it is a new one (haven't been yet persisted) or updates persistent one
	*/
	private void updateDistrict() throws FlexPayExceptionContainer {

		DistrictName districtName = getDistrictName();
		district.setNameForDate(districtName, beginDateFilter.getDate());

		if (district.isNew()) {
			district.setParent(new Town(townFilter));
			districtService.create(district);
		} else {
			districtService.update(district);
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
		beginDateFilter.setDate(temporal != null ? temporal.getBegin() : DateUtil.now());

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
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

}
