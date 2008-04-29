package org.flexpay.ab.actions.nametimedependent;

import org.apache.commons.collections.ArrayStack;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.NameDateInterval;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TemporaryValue;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.filter.NameFilter;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class ListAction<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> extends ActionBase<TV, DI, NTD, T> implements SessionAware {

	private static Logger log = Logger.getLogger(ListAction.class);

	private static final String ATTRIBUTE_ACTION_ERRORS =
			ListAction.class.getName() + ".ACTION_ERRORS";

	private Map session;
	protected Page pager = new Page();
	protected List objectNames;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings ({"unchecked"})
	public String execute() throws Exception {

		long start = System.currentTimeMillis();
		try {
			ArrayStack filterArrayStack = getFilters();
			for(Object filter : filterArrayStack) {
				((PrimaryKeyFilter) filter).initFilter(session);
			}
			ArrayStack filters = parentService.initFilters(filterArrayStack, userPreferences.getLocale());
			setFilters(filters);

			initObjects(filters);
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
			log.info("Listing " + (System.currentTimeMillis() - start) + " ms");
		}
		return SUCCESS;
	}
	
	/*private void initFilterId(PrimaryKeyFilter filter) {
		Long selectedId = filter.getSelectedId();
		Long defaultId = filter.getDefaultId();
		String filterName = filter.getClass().getName();
		Long inSessionId = (Long) session.get(filterName);
		if(selectedId == null) {
			if(inSessionId == null) {
				filter.setSelectedId(defaultId);
			} else {
				filter.setSelectedId(inSessionId);
			}
		} else {
			session.put(filterName, selectedId);
		}
	}*/

	protected void initObjects(ArrayStack filters) throws FlexPayException {
		objectNames = nameTimeDependentService.findNames(filters, pager);
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

	/**
	 * Getter for property 'objectNames'.
	 *
	 * @return Value for property 'objectNames'.
	 */
	public List getObjectNames() {
		return objectNames;
	}
}
