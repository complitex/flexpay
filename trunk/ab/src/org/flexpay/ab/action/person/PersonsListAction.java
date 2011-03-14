package org.flexpay.ab.action.person;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.filters.PersonSearchFilter;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class PersonsListAction extends FPActionWithPagerSupport<Person> {

	private List<Person> persons = list();
	private PersonSearchFilter personSearchFilter = new PersonSearchFilter();

	private PersonService personService;

	@NotNull
	@Override
	protected String doExecute() throws Exception {

		if (personSearchFilter == null) {
			log.warn("PersonSearchFilter parameter is null");
			personSearchFilter = new PersonSearchFilter();
		}

		String searchStr = personSearchFilter.getSearchString() == null ? "" : personSearchFilter.getSearchString();

		persons = personService.findByFIO("%" + searchStr + "%", getPager());

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
		return personService.readFull(new Stub<Person>(id));
	}

	@Required
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

}
