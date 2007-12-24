package org.flexpay.ab.actions.identity;

import java.util.List;

import org.flexpay.ab.actions.CommonAction;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.ab.service.IdentityTypeService;

public class IdentityTypeListAction extends CommonAction
{
	private IdentityTypeService identityTypeService;
	private List<IdentityTypeTranslation> translationList;
	
	public String execute() throws Exception {
		translationList = identityTypeService.getTranslations(userPreferences.getLocale());
		
		return "list";
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
	
	public List<IdentityTypeTranslation> getTranslationList() {
		return translationList;
	}

}
