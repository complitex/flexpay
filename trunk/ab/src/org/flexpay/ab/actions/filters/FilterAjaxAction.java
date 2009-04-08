package org.flexpay.ab.actions.filters;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.flexpay.ab.actions.buildings.BuildingsActionsBase;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class FilterAjaxAction extends BuildingsActionsBase implements ServletRequestAware {

	protected String[] parents;
	protected Long filterValueLong;
	protected String filterValue;
	protected String filterString;
	protected String q;
	protected List<FilterObject> foundObjects;
	protected Boolean preRequest;
	protected Boolean saveFilterValue;
	@NonNls
	protected HttpServletRequest request;

	public String execute() throws Exception {

		try {
			filterValueLong = Long.parseLong(filterValue);
		} catch (Exception e) {
			log.debug("Incorrect filter value ({})", filterValue);
		}

		if (saveFilterValue != null && saveFilterValue
				&& (preRequest == null || !preRequest)
				&& filterValueLong != null) {
			saveFilterValue();
		}

		if (preRequest != null && preRequest) {
			readFilterString();
			return SUCCESS;
		}

		return super.execute();
	}

	protected abstract void readFilterString();

	protected abstract void saveFilterValue();

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

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setParents(String[] parents) {
		this.parents = parents;
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
