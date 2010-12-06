package org.flexpay.common.actions.filter;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.filter.FilterObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

/**
 * Abstract class for address filter-classes
 *
 * Parameters:
 *
 * q - this is a query string for search objects
 * 	   (ex.: "iva" - may found {"Ivanova", "Migivana"} -
 * 	   but don't forget, this is just example!)
 * parents - values of parents filters or some values, which can use for search
 * filterValue - value of filter hidden input (hidden-value). Uses for preRequest actions
 * filterValueLong - parsed filterValue to Long value
 * filterString - string value (for text filter input, also may call view-value). 
 * 				  Uses for preRequest actions
 * saveFilterValues - if true, that this is a request for save values
 * 					  for userPreferences - call saveFilterValues() method
 * preRequest - true - if this is a pre-request action. That we want to read
 * 				string value (view-value) for filter by its value (hidden-value) -
 * 				call readFilterString() method.
 * 				false or null - for else
 * PREREQUEST_RESPONSE - this is a name of result in struts config,
 * 						 which returned after pre-request action finished
 */
public abstract class FilterAjaxAction extends FPActionSupport {

	public final static String PREREQUEST_RESPONSE = "prerequestResponse";

	protected String[] parents;
	protected String q;
	protected String filterValue;
	protected Long filterValueLong;
	protected String filterString;
	protected Boolean preRequest;
	protected Boolean saveFilterValue;
	protected List<FilterObject> foundObjects = list();

	@Override
	public String execute() throws Exception {

		try {
			filterValueLong = Long.parseLong(filterValue);
		} catch (Exception e) {
			// do nothing
		}

		if (preRequest != null && preRequest) {
			readFilterString();
			if (!hasActionErrors()) {
				saveFilterValue();
			}
			return PREREQUEST_RESPONSE;
		} else if (saveFilterValue != null && saveFilterValue) {
			saveFilterValue();
			return SUCCESS;
		}

		return super.execute();
	}

	/**
	 * This method call if we know hidden-value
	 * (or some else values) and want to get view-value
	 *
	 * @throws FlexPayException FlexPayException
	 */
	protected abstract void readFilterString() throws FlexPayException;

	/**
	 * All filters values saving to session object
	 * userPreferences. This method call when we want
	 * to set new values to this object
	 */
	protected abstract void saveFilterValue();
	
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

	public void setParents(String[] parents) {
		this.parents = parents;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	public String getFilterString() {
		return filterString;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public void setSaveFilterValue(Boolean saveFilterValue) {
		this.saveFilterValue = saveFilterValue;
	}

	public Boolean getPreRequest() {
		return preRequest;
	}

	public void setPreRequest(Boolean preRequest) {
		this.preRequest = preRequest;
	}

	public List<FilterObject> getFoundObjects() {
		return foundObjects;
	}

}
