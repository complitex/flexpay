package org.flexpay.ab.actions.district;

import org.flexpay.ab.persistence.*;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.filter.BeginDateFilter;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class DistrictEditSimpleAction extends FPActionSupport {

	private District district = new District();
	private TownFilter townFilter = new TownFilter();
	private BeginDateFilter beginDateFilter = new BeginDateFilter(DateUtil.now());

	private TownService townService;
	private DistrictService districtService;

	private Map<Long, String> names = CollectionUtils.treeMap();

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	protected String doExecute() throws Exception {

		if (district.getId() == null) {
			addActionError(getText("error.no_id"));
			return REDIRECT_SUCCESS;
		}

		District dstrct = district.isNew() ? district : districtService.readFull(stub(district));
		if (dstrct == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		if (!isSubmit()) {
			district = dstrct;
			initData();
			return INPUT;
		}

		// no town selected in filter
		if (!townFilter.needFilter()) {
			log.debug("!!!!!!!!!!!!!! no town filter value selected");
			addActionError(getText("error.ab.district.no_town"));
			return INPUT;
		}

		DistrictName districtName = new DistrictName();
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			DistrictNameTranslation translation = new DistrictNameTranslation();
			translation.setLang(lang);
			translation.setName(value);
			districtName.setTranslation(translation);
		}
		log.debug("District name to set: {}", districtName);
		dstrct.setNameForDate(districtName, beginDateFilter.getDate());

		if (dstrct.isNew()) {
			dstrct.setParent(townService.readFull(townFilter.getSelectedStub()));
			districtService.create(dstrct);
		} else {
			log.debug("Updated names: {}", dstrct.getNameTemporals());
			districtService.update(dstrct);
		}

		addActionError(getText("ab.district.saved"));
		return REDIRECT_SUCCESS;
	}

	private void initData() {

		// init begin date filter
		DistrictNameTemporal temporal = district.getCurrentNameTemporal();
		if (temporal != null) {
			beginDateFilter.setDate(temporal.getBegin());
		}

		// init town filter if object is not new
		if (district.isNotNew()) {
			townFilter.setSelectedId(district.getTownStub().getId());
			townFilter.setReadOnly(true);
		}
		townFilter.setAllowEmpty(false);

		// init translations
		DistrictName districtName = temporal != null ? temporal.getValue() : null;
		if (districtName != null) {
			for (DistrictNameTranslation name : districtName.getTranslations()) {
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
	protected String getErrorResult() {
		return INPUT;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public TownFilter getTownFilter() {
		return townFilter;
	}

	public void setTownFilter(TownFilter townFilter) {
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

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}
}
