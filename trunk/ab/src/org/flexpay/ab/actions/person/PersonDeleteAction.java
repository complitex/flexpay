package org.flexpay.ab.actions.person;

import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.Set;

public class PersonDeleteAction extends FPActionSupport {

	private Set<Long> objectIds = set();

	private PersonService personService;

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

	@Required
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

}
