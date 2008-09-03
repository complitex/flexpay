package org.flexpay.ab.actions.person;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.service.ParentService;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListPersons extends FPActionSupport {

	private ParentService parentService;
	private PersonService personService;

	private List<Person> persons = new ArrayList<Person>();
	private String searchString;
	private Page pager = new Page();

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (StringUtils.isEmpty(searchString)) {
			ArrayStack filters = parentService == null ? null :
								 parentService.initFilters(getFilters(), userPreferences.getLocale());

			persons = personService.findPersons(filters, pager);
		} else {
			persons = personService.findByFIO(pager, "%" + searchString + "%");
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
	protected String getErrorResult() {
		return SUCCESS;
	}

	/**
	 * Getter for property 'pager'.
	 *
	 * @return Value for property 'pager'.
	 */
	public Page getPager() {
		return pager;
	}

	/**
	 * Setter for property 'pager'.
	 *
	 * @param pager Value to set for property 'pager'.
	 */
	public void setPager(Page pager) {
		this.pager = pager;
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

	/**
	 * @return the searchString
	 */
	public String getSearchString() {
		return searchString;
	}

	/**
	 * @param searchString the searchString to set
	 */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}
}
