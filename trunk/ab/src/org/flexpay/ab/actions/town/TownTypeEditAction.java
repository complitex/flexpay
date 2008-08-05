package org.flexpay.ab.actions.town;

import com.opensymphony.xwork2.Preparable;
import org.apache.struts2.ServletActionContext;
import org.flexpay.ab.persistence.TownType;
import org.flexpay.ab.persistence.TownTypeTranslation;
import org.flexpay.ab.service.TownTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.util.CollectionUtils.map;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class TownTypeEditAction extends FPActionSupport implements Preparable {
	private TownTypeService townTypeService;
	private TownType townType;
	private Map<String, TownTypeTranslation> translationMap;

	public void prepare() throws FlexPayException {
		// todo remove reference to request
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		townType = townTypeService.read(Long.valueOf(id));

		translationMap = map();
		for (TownTypeTranslation translation : townType.getTranslations()) {
			translationMap.put(translation.getId().toString(), translation);
		}
	}

	@NotNull
	public String doExecute() throws Exception {

		if (isSubmit()) {
			townTypeService.update(townType, townType.getTranslations());
			return REDIRECT_SUCCESS;
		}

		return INPUT;
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
		return REDIRECT_SUCCESS;
	}

	/**
	 * Setter for property 'townTypeService'.
	 *
	 * @param townTypeService Value to set for property 'townTypeService'.
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
