package org.flexpay.ab.actions.person;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.filters.PersonSearchFilter;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.service.ParentService;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class PersonsListAction extends FPActionWithPagerSupport<Person> {

	private List<Person> persons = new ArrayList<Person>();
	private PersonSearchFilter personSearchFilter = new PersonSearchFilter();

	private ParentService<?> parentService;
	private PersonService personService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (!personSearchFilter.needFilter()) {
			ArrayStack filters = parentService == null ? null :
								 parentService.initFilters(getFilters(), getUserPreferences().getLocale());

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
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	private ArrayStack getFilters() {
		return new ArrayStack();
	}

	public List<Person> getPersons() {
		return persons;
	}

	public PersonSearchFilter getPersonSearchFilter() {
		return personSearchFilter;
	}

	public void setPersonSearchFilter(PersonSearchFilter personSearchFilter) {
		this.personSearchFilter = personSearchFilter;
	}

	public Person getPerson(Long id) {
		return personService.read(new Stub<Person>(id));
	}

	public void setParentService(ParentService<?> parentService) {
		this.parentService = parentService;
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
