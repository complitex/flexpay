package org.flexpay.ab.actions.filters;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.flexpay.ab.actions.buildings.BuildingsActionsBase;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class FilterAjaxAction extends BuildingsActionsBase implements ServletRequestAware {

	protected String[] parents;
	protected String filterValue;
	protected String q;
	protected List<FilterObject> foundObjects;
	@NonNls
	protected HttpServletRequest request;

	protected abstract boolean saveFilterValue();

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

	public void setQ(String q) {
		this.q = q;
	}

	public List<FilterObject> getFoundObjects() {
		return foundObjects;
	}

}
