package org.flexpay.ab.action.measureunit;

import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.MeasureUnitName;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.MeasureUnitService;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class MeasureUnitEditAction extends FPActionSupport {

	private MeasureUnit measureUnit = new MeasureUnit();
	private Map<Long, String> names = treeMap();

	private String crumbCreateKey;
	private MeasureUnitService measureUnitService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (measureUnit == null || measureUnit.getId() == null) {
			log.warn("Incorrect measure unit id");
			addActionError(getText("common.error.measure_unit.incorrect_measure_unit_id"));
			return REDIRECT_ERROR;
		}

		if (measureUnit.isNotNew()) {
			Stub<MeasureUnit> stub = stub(measureUnit);
			measureUnit = measureUnitService.readFull(stub);

			if (measureUnit == null) {
				log.warn("Can't get measure unit with id {} from DB", stub.getId());
				addActionError(getText("common.error.measure_unit.cant_get_measure_unit"));
				return REDIRECT_ERROR;
			} else if (measureUnit.isNotActive()) {
				log.warn("Measure unit with id {} is disabled", stub.getId());
				addActionError(getText("common.error.measure_unit.cant_get_measure_unit"));
				return REDIRECT_ERROR;
			}

		}

		correctNames();

		if (isNotSubmit()) {
			initData();
			return INPUT;
		}

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			measureUnit.setName(new MeasureUnitName(value, lang));
		}

		if (measureUnit.isNew()) {
			measureUnitService.create(measureUnit);
		} else {
			measureUnitService.update(measureUnit);
		}

		addActionMessage(getText("common.measure_unit.saved"));

		return REDIRECT_SUCCESS;
	}

	private void correctNames() {
		if (names == null) {
			log.warn("Names parameter is null");
			names = treeMap();
		}
		Map<Long, String> newNames = treeMap();
		for (Language lang : getLanguages()) {
			newNames.put(lang.getId(), names.containsKey(lang.getId()) ? names.get(lang.getId()) : "");
		}
		names = newNames;
	}

	private void initData() {

		for (MeasureUnitName name : measureUnit.getUnitNames()) {
			names.put(name.getLang().getId(), name.getName());
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
		if (measureUnit != null && measureUnit.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
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
