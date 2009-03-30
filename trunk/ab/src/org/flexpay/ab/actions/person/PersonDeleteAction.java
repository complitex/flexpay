package org.flexpay.ab.actions.person;

import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PersonDeleteAction extends FPActionSupport {

	private PersonService personService;
	private Set<Long> objectIds = set();

	@NotNull
	public String doExecute() throws Exception {
		personService.disable(objectIds);
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

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }
}