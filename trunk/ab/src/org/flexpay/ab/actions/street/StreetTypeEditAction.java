package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.Language;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.treeMap;
import org.flexpay.common.util.config.ApplicationConfig;
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

		streetType = streetType.isNew() ? streetType : streetTypeService.readFull(stub(streetType));

		if (streetType == null) {
			addActionError(getText("common.object_not_selected"));
			return REDIRECT_SUCCESS;
		}

		if (isNotSubmit()) {
			initNames();
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

	private void initNames() {
		for (StreetTypeTranslation name : streetType.getTranslations()) {
			names.put(name.getLang().getId(), name.getName());
			shortNames.put(name.getLang().getId(), name.getShortName());
		}

		for (Language lang : ApplicationConfig.getLanguages()) {
			if (names.containsKey(lang.getId())) {
				continue;
			}
			names.put(lang.getId(), "");
			shortNames.put(lang.getId(), "");
		}
	}

	@Override
	protected void setBreadCrumbs() {
		if (streetType.isNew()) {
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
