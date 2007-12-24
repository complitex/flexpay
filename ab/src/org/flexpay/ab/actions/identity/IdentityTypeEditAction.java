package org.flexpay.ab.actions.identity;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.common.exception.FlexPayException;

import com.opensymphony.xwork2.Preparable;

public class IdentityTypeEditAction extends CommonAction implements Preparable
{
	private IdentityTypeService identityTypeService;
	private IdentityType identityType;
	private Map<String, IdentityTypeTranslation> translationMap;
	
	
	public void prepare() throws FlexPayException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String id = request.getParameter("id");
		identityType = identityTypeService.read(Long.valueOf(id));
		
		
		
		translationMap = new HashMap<String, IdentityTypeTranslation>();
		for(IdentityTypeTranslation translation : identityType.getTranslations())
		{
			translationMap.put(translation.getId().toString(), translation);
		}
	}
	
	
	public String execute() throws Exception {
		if (isSubmitted()) {
			try {
				identityTypeService.update(identityType, identityType.getTranslations());
			} catch (RuntimeException e) {
				// TODO
			}
			
			return "afterSubmit";
		}
		
        return "form";
	}
	
	/**
	 * Setter for property 'identityTypeService'.
	 * 
	 * @param identityTypeService
	 *            Value to set for property 'identityTypeService'.
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
