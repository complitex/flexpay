package org.flexpay.ab.actions.person;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.filters.PersonSearchFilter;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PersonsListAction extends FPActionWithPagerSupport<Person> {

	private ParentService parentService;
	private PersonService personService;

	private List<Person> persons = new ArrayList<Person>();


	private PersonSearchFilter personSearchFilter = new PersonSearchFilter();

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (!personSearchFilter.needFilter()) {
			ArrayStack filters = parentService == null ? null :
								 parentService.initFilters(getFilters(), userPreferences.getLocale());

			persons = personService.findPersons(filters, getPager());
		} else {
			persons = personService.findByFIO(getPager(), "%" + personSearchFilter.getSearchString() + "%");
		}

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in
	 * a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return SUCCESS;
	}

	private ArrayStack getFilters() {
		return new ArrayStack();
	}

	/**
	 * Setter for property 'parentService'.
	 *
	 * @param parentService Value to set for property 'parentService'.
	 */
	public void setParentService(ParentService parentService) {
		this.parentService = parentService;
	}

	/**
	 * Setter for property 'personService'.
	 *
	 * @param personService Value to set for property 'personService'.
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	/**
	 * Getter for property 'persons'.
	 *
	 * @return Value for property 'persons'.
	 */
	public List<Person> getPersons() {
		return persons;
	}

	public PersonSearchFilter getPersonSearchFilter() {
		return personSearchFilter;
	}

	public void setPersonSearchFilter(PersonSearchFilter personSearchFilter) {
		this.personSearchFilter = personSearchFilter;
	}

}
