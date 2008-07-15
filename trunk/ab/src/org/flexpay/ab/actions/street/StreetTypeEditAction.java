package org.flexpay.ab.actions.street;

import com.opensymphony.xwork2.Preparable;
import org.apache.struts2.ServletActionContext;
import org.flexpay.ab.persistence.StreetType;
import org.flexpay.ab.persistence.StreetTypeTranslation;
import org.flexpay.ab.service.StreetTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import static org.flexpay.common.util.CollectionUtils.map;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class StreetTypeEditAction extends FPActionSupport implements Preparable {
	private StreetTypeService streetTypeService;
	private StreetType streetType;
	private Map<String, StreetTypeTranslation> translationMap;


	public void prepare() throws FlexPayException {
		// todo remove request reference
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		streetType = streetTypeService.read(Long.valueOf(id));


		translationMap = map();
		for (StreetTypeTranslation translation : streetType.getTranslations()) {
			translationMap.put(translation.getId().toString(), translation);
		}
	}


	public String doExecute() throws Exception {
		if (isSubmit()) {
			streetTypeService.update(streetType, streetType.getTranslations());

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
	@Override
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	/**
	 * Setter for property 'streetTypeService'.
	 *
	 * @param streetTypeService Value to set for property 'streetTypeService'.
	 */
	public void setStreetTypeService(StreetTypeService streetTypeService) {
		this.streetTypeService = streetTypeService;
	}

	public StreetType getStreetType() {
		return streetType;
	}


	public Map getTranslationMap() {
		return translationMap;
	}

}
