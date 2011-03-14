package org.flexpay.ab.action.building;

import org.flexpay.ab.persistence.AddressAttributeType;
import org.flexpay.ab.service.AddressAttributeTypeService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class AddressAttributeTypeViewAction extends FPActionSupport {

	private AddressAttributeType attributeType = new AddressAttributeType();

	private AddressAttributeTypeService addressAttributeTypeService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (attributeType == null || attributeType.isNew()) {
			log.warn("Incorrect attribute type id");
			addActionError(getText("ab.error.building_attribute_type.incorrect_attribute_type_id"));
			return REDIRECT_ERROR;
		}

		Stub<AddressAttributeType> stub = stub(attributeType);
		attributeType = addressAttributeTypeService.readFull(stub);

		if (attributeType == null) {
			log.warn("Can't get attribute type with id {} from DB", stub.getId());
			addActionError(getText("ab.error.building_attribute_type.cant_get_attribute_type"));
			return REDIRECT_ERROR;
		} else if (attributeType.isNotActive()) {
			log.warn("Attribute type with id {} is disabled", stub.getId());
			addActionError(getText("ab.error.building_attribute_type.cant_get_attribute_type"));
			return REDIRECT_ERROR;
		}

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
		return REDIRECT_ERROR;
	}

	public AddressAttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AddressAttributeType attributeType) {
		this.attributeType = attributeType;
	}

	@Required
	public void setBuildingAttributeTypeService(AddressAttributeTypeService addressAttributeTypeService) {
		this.addressAttributeTypeService = addressAttributeTypeService;
	}

}
