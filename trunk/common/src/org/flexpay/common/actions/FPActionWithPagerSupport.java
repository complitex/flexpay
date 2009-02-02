package org.flexpay.common.actions;

import org.flexpay.common.dao.paging.Page;
import org.jetbrains.annotations.NonNls;

public abstract class FPActionWithPagerSupport<T> extends FPActionSupport {

	@NonNls
	private static final String PAGE_SIZE_SESSION_ATTRIBUTE = FPActionWithPagerSupport.class.getName() + ".PAGE_SIZE";

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

	@SuppressWarnings({"unchecked"})
	public void setPageSize(Integer pageSize) {
		session.put(PAGE_SIZE_SESSION_ATTRIBUTE, pageSize);
	}

	public Integer getPageSize() {
		return (Integer) session.get(PAGE_SIZE_SESSION_ATTRIBUTE);
	}

	public Page<T> getPager() {
		return pager;
	}

	public void setPager(Page<T> pager) {
		this.pager = pager;
	}

}
