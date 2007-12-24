package org.flexpay.ab.actions.identity;

import org.flexpay.ab.actions.AbstractCreateAction;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.IdentityTypeTranslation;
import org.flexpay.ab.service.IdentityTypeService;
import org.flexpay.ab.service.MultilangEntityService;

public class IdentityTypeCreateAction extends
		AbstractCreateAction<IdentityType, IdentityTypeTranslation> {

	private IdentityTypeService identityTypeService;

	protected IdentityTypeTranslation createTranslation() {
		return new IdentityTypeTranslation();
	}

	protected MultilangEntityService<IdentityType, IdentityTypeTranslation> getEntityService() {
		return identityTypeService;
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
}
