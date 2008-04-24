package org.flexpay.ab.actions.town;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.exception.FlexPayException;

import com.opensymphony.xwork2.Preparable;

public class TownTypeEditAction extends CommonAction implements Preparable {
	private TownTypeService townTypeService;
	private TownType townType;
	private Map<String, TownTypeTranslation> translationMap;

	public void prepare() throws FlexPayException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		townType = townTypeService.read(Long.valueOf(id));

		translationMap = new HashMap<String, TownTypeTranslation>();
		for (TownTypeTranslation translation : townType.getTranslations()) {
			translationMap.put(translation.getId().toString(), translation);
		}
	}

	public String execute() throws Exception {
		if (isSubmitted()) {
			try {
				townTypeService.update(townType, townType.getTranslations());
			} catch (RuntimeException e) {
				// TODO
			}

			return "afterSubmit";
		}

		return "form";
	}

	/**
	 * Setter for property 'townTypeService'.
	 * 
	 * @param townTypeService
	 *            Value to set for property 'townTypeService'.
	 */
	public void setTownTypeService(TownTypeService townTypeService) {
		this.townTypeService = townTypeService;
	}

	public TownType getTownType() {
		return townType;
	}

	public Map getTranslationMap() {
		return translationMap;
	}

}
