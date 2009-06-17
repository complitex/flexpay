package org.flexpay.ab.actions.measureunit;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.persistence.MeasureUnitName;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.MeasureUnitService;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class MeasureUnitEditAction extends FPActionSupport {

	private MeasureUnit measureUnit = new MeasureUnit();
	private Map<Long, String> names = CollectionUtils.treeMap();

	private String crumbCreateKey;
	private MeasureUnitService measureUnitService;

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

		if (measureUnit.getId() == null) {
			log.debug("Id not specified");
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		MeasureUnit unit = measureUnit.isNew() ?
						   measureUnit : measureUnitService.readFull(Stub.stub(measureUnit));
		if (unit == null) {
			log.debug("Invalid id specified");
			addActionError(getText("error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

		log.debug("Unit names before: {}", unit.getUnitNames());

		if (isNotSubmit()) {
			measureUnit = unit;
			initNames();
			return INPUT;
		}

		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			Language lang = getLang(name.getKey());
			MeasureUnitName unitName = new MeasureUnitName();
			unitName.setLang(lang);
			unitName.setName(value);
			unit.setName(unitName);
		}

		log.debug("Unit names: {}", unit.getUnitNames());

		if (unit.isNew()) {
			measureUnitService.create(unit);
		} else {
			measureUnitService.update(unit);
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
			if (names.containsKey(lang.getId())) {
				continue;
			}
			names.put(lang.getId(), "");
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
