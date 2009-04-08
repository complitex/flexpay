package org.flexpay.payments.actions.search;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.ab.persistence.filters.*;
import org.flexpay.ab.service.ApartmentService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.collections.ArrayStack;

public class SearchByAddressAction extends FPActionSupport {

	@NotNull
	protected String doExecute() throws Exception {


		return SUCCESS;
	}

	@NotNull
	protected String getErrorResult() {

		return SUCCESS;
	}	
}
