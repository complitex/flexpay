package org.flexpay.ab.action.street;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import static org.flexpay.common.util.config.ApplicationConfig.getLanguages;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class StreetTypeEditAction extends FPActionSupport {

	private StreetType streetType = new StreetType();
	private Map<Long, String> names = treeMap();
	private Map<Long, String> shortNames = treeMap();

	private String crumbCreateKey;
	private StreetTypeService streetTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (streetType == null || streetType.getId() == null) {
			log.warn("Incorrect street type id");
			addActionError(getText("ab.error.street_type.incorrect_street_type_id"));
			return REDIRECT_ERROR;
		}

		if (streetType.isNotNew()) {
			Stub<StreetType> stub = stub(streetType);
			streetType = streetTypeService.readFull(stub);

			if (streetType == null) {
				log.warn("Can't get street type with id {} from DB", stub.getId());
				addActionError(getText("ab.error.street_type.cant_get_street_type"));
				return REDIRECT_ERROR;
			} else if (streetType.isNotActive()) {
				log.warn("Street type with id {} is disabled", stub.getId());
				addActionError(getText("ab.error.street_type.cant_get_street_type"));
				return REDIRECT_ERROR;
			}

		}

		correctNames();

		if (isNotSubmit()) {
			initData();
			return INPUT;
		}

		// init translations
		for (Map.Entry<Long, String> name : names.entrySet()) {
			String value = name.getValue();
			String shortName = shortNames.get(name.getKey());
			Language lang = getLang(name.getKey());
			streetType.setTranslation(new StreetTypeTranslation(value, shortName, lang));
		}

		if (streetType.isNew()) {
			streetTypeService.create(streetType);
		} else {
			streetTypeService.update(streetType);
		}

		addActionMessage(getText("ab.street_type.saved"));

		return REDIRECT_SUCCESS;
	}

	private void correctNames() {
		Map<Long, String> newNames = treeMap();
		Map<Long, String> newShortNames = treeMap();
		for (Language lang : getLanguages()) {
			newNames.put(lang.getId(), names != null && names.containsKey(lang.getId()) ? names.get(lang.getId()) : "");
			newShortNames.put(lang.getId(), shortNames != null && shortNames.containsKey(lang.getId()) ? shortNames.get(lang.getId()) : "");
		}
		names = newNames;
		shortNames = newShortNames;
	}

	private void initData() {
		for (StreetTypeTranslation name : streetType.getTranslations()) {
			names.put(name.getLang().getId(), name.getName());
			shortNames.put(name.getLang().getId(), name.getShortName());
		}

		for (Language lang : getLanguages()) {
			if (!names.containsKey(lang.getId())) {
				names.put(lang.getId(), "");
				shortNames.put(lang.getId(), "");
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
		if (streetType != null && streetType.isNew()) {
			crumbNameKey = crumbCreateKey;
		}
		super.setBreadCrumbs();
	}

	public StreetType getStreetType() {
		return streetType;
	}

	public void setStreetType(StreetType streetType) {
		this.streetType = streetType;
	}

	public Map<Long, String> getNames() {
		return names;
	}

	public void setNames(Map<Long, String> names) {
		this.names = names;
	}

	public Map<Long, String> getShortNames() {
		return shortNames;
	}

	public void setShortNames(Map<Long, String> shortNames) {
		this.shortNames = shortNames;
	}

	public void setCrumbCreateKey(String crumbCreateKey) {
		this.crumbCreateKey = crumbCreateKey;
	}

	@Required
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

}
