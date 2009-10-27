package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class AddressAttributeTypesListAction extends FPActionSupport {

	private List<AddressAttributeType> types = list();

	private AddressAttributeTypeService addressAttributeTypeService;

	@NotNull
	@Override
	public String doExecute() {

		types = addressAttributeTypeService.getAttributeTypes();

		return SUCCESS;
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
		return SUCCESS;
	}

	public List<AddressAttributeType> getTypes() {
		return types;
	}

	@Required
	public void setBuildingAttributeTypeService(AddressAttributeTypeService addressAttributeTypeService) {
		this.addressAttributeTypeService = addressAttributeTypeService;
	}

}
