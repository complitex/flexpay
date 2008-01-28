package org.flexpay.ab.actions.person;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.PersonService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.service.ParentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ListPersons extends FPActionSupport implements SessionAware {

	private static Logger log = Logger.getLogger(ListPersons.class);

	private static final String ATTRIBUTE_ACTION_ERRORS = ListPersons.class.getName() + ".ACTION_ERRORS";

	private ParentService parentService;
	private PersonService personService;

	private Map session;
	private List<Person> persons = new ArrayList<Person>();
	private Page pager = new Page();

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings ({"unchecked"})
	public String execute() throws Exception {

		long start = System.currentTimeMillis();
		try {
			ArrayStack filters = parentService == null ? null :
								 parentService.initFilters(getFilters(), userPreferences.getLocale());
			setFilters(filters);

			persons = personService.findPersons(filters, pager);
		} catch (FlexPayException e) {
			addActionError(e);
		}

		// Retrieve action errors from session if any
		if (log.isDebugEnabled()) {
			log.debug("Getting actionErrors: " + session.get(ATTRIBUTE_ACTION_ERRORS));
		}
		Collection errors = (Collection) session.remove(ATTRIBUTE_ACTION_ERRORS);
		if (errors != null && !errors.isEmpty()) {
			Collection actionErrors = getActionErrors();
			actionErrors.addAll(errors);
			setActionErrors(actionErrors);
		}

		if (log.isInfoEnabled()) {
			log.info("Listing persons" + (System.currentTimeMillis() - start) + " ms");
		}
		return SUCCESS;
	}

	public static void setActionErrors(Map<String, Object> session, Collection actionErrors) {
		if (log.isDebugEnabled()) {
			log.debug("Setting actionErrors: " + actionErrors);
		}
		session.put(ATTRIBUTE_ACTION_ERRORS, actionErrors);
	}

	/**
	 * Sets the Map of session attributes in the implementing class.
	 *
	 * @param session a Map of HTTP session attribute name/value pairs.
	 */
	public void setSession(Map session) {
		this.session = session;
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
		ArrayStack filters = new ArrayStack();
		filters.push(null);

		return filters;
	}

	private void setFilters(ArrayStack filters) {

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
}
