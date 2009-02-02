package org.flexpay.common.actions;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.util.config.UserPreferences;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.jetbrains.annotations.NonNls;

import javax.servlet.http.HttpServletRequest;

public abstract class FPActionWithPagerSupport<T> extends FPActionSupport implements ServletRequestAware {

	@NonNls
	private HttpServletRequest request;

	private Page<T> pager = new Page<T>();
	private boolean pageSizeChanged = false;

	public final String execute() throws Exception {
		Integer pageSize = getPageSize();
		if (isPageSizeChanged()) {
			if (pageSize == null || pageSize != pager.getPageSize()) {
				setPageSize(pager.getPageSize());
			}
		} else {
			if (pageSize != null) {
				pager.setPageSize(pageSize);
			}
		}

		return super.execute();
	}

	public boolean isPageSizeChanged() {
		return pageSizeChanged;
	}

	public void setPageSizeChanged(boolean pageSizeChanged) {
		this.pageSizeChanged = pageSizeChanged;
	}

	public void setPageSize(Integer pageSize) {
		getUserPreferences().setPageSize(pageSize);
		UserPreferences.setPreferences(request, getUserPreferences());
	}

	public Integer getPageSize() {
		return getUserPreferences().getPageSize();
	}

	public Page<T> getPager() {
		return pager;
	}

	public void setPager(Page<T> pager) {
		this.pager = pager;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}
