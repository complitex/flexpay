package org.flexpay.ab.actions.identity;

import com.opensymphony.xwork2.Preparable;
import org.apache.struts2.ServletActionContext;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class IdentityTypeEditAction extends FPActionSupport implements Preparable {
	private IdentityTypeService identityTypeService;
	private IdentityType identityType;
	private Map<String, IdentityTypeTranslation> translationMap;

	public void prepare() throws FlexPayException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		identityType = identityTypeService.read(Long.valueOf(id));

		translationMap = new HashMap<String, IdentityTypeTranslation>();
		for (IdentityTypeTranslation translation : identityType.getTranslations()) {
			translationMap.put(translation.getId().toString(), translation);
		}
	}

	public String doExecute() throws Exception {
		if (isSubmit()) {
			identityTypeService.update(identityType, identityType.getTranslations());

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
	 * Setter for property 'identityTypeService'.
	 *
	 * @param identityTypeService Value to set for property 'identityTypeService'.
	 */
	public void setIdentityTypeService(IdentityTypeService identityTypeService) {
		this.identityTypeService = identityTypeService;
	}

	public IdentityType getIdentityType() {
		return identityType;
	}

	public Map<String, IdentityTypeTranslation> getTranslationMap() {
		return translationMap;
	}

}
