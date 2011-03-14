package org.flexpay.ab.action.street;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;


public class StreetAction extends FPActionWithPagerSupport<Street> {

	private Long streetFilter;

	private StreetService streetService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (streetFilter == null || streetFilter <= 0) {
			log.warn("Incorrect street id in filter ({})", streetFilter);
			return SUCCESS;
		}

        if (streetService.readFull(new Stub<Street>(streetFilter)) == null) {
            log.warn("Street do not exist with id={}", streetFilter);
            streetFilter = 0L;
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
		return SUCCESS;
	}

    public Long getStreetFilter() {
        return streetFilter;
    }

    public void setStreetFilter(Long streetFilter) {
        this.streetFilter = streetFilter;
    }

    @Required
    public void setStreetService(StreetService streetService) {
        this.streetService = streetService;
    }
}

