package org.flexpay.ab.actions.measureunit;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.MeasureUnitName;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class MeasureUnitEditAction extends FPActionSupport {

	private MeasureUnit measureUnit = new MeasureUnit();
	private Map<Long, String> names = CollectionUtils.treeMap();

	private String crumbCreateKey;
	private MeasureUnitService measureUnitService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		measureUnit = measureUnit.isNew() ? measureUnit : measureUnitService.readFull(stub(measureUnit));

		if (measureUnit == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		if (isNotSubmit()) {
			initNames();
			return INPUT;
		}

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			if (lang.isDefault() && StringUtils.isEmpty(value)) {
				addActionError(getText("ab.error.identity_type.full_name_is_required"));
				return INPUT;
			}
			measureUnit.setName(new MeasureUnitName(value, lang));
		}

		if (measureUnit.isNew()) {
			measureUnitService.create(measureUnit);
		} else {
			measureUnitService.update(measureUnit);
		}

		return REDIRECT_SUCCESS;
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
		if (measureUnit.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	private void initNames() {
		for (MeasureUnitName name : measureUnit.getUnitNames()) {
			names.put(name.getLang().getId(), name.getName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (!names.containsKey(lang.getId())) {
				names.put(lang.getId(), "");
			}
		}
	}

	public MeasureUnit getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(MeasureUnit measureUnit) {
		this.measureUnit = measureUnit;
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
	public void setMeasureUnitService(MeasureUnitService measureUnitService) {
		this.measureUnitService = measureUnitService;
	}

}
