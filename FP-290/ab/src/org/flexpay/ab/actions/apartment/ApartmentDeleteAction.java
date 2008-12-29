package org.flexpay.ab.actions.apartment;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.flexpay.ab.service.ApartmentService;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ApartmentDeleteAction extends FPActionSupport {

	private ApartmentService apartmentService;
	private Set<Long> objectIds = set();

	@NotNull
	public String doExecute() throws Exception {
		apartmentService.disable(objectIds);

		return REDIRECT_SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return REDIRECT_SUCCESS;
	}

	public Set<Long> getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}

	public void setApartmentService(ApartmentService apartmentService) {
		this.apartmentService = apartmentService;
	}
}