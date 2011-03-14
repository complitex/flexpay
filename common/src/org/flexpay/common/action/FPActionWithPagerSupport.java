package org.flexpay.common.action;

import org.flexpay.common.dao.paging.Page;

public abstract class FPActionWithPagerSupport<T> extends FPActionSupport {

	private Page<T> pager = new Page<T>();
	private boolean pageSizeChanged = false;

	@Override
	public String execute() throws Exception {
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

}
