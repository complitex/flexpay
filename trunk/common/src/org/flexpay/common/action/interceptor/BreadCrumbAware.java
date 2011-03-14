package org.flexpay.common.action.interceptor;

import org.flexpay.common.action.breadcrumbs.Crumb;

public interface BreadCrumbAware {

	void setCrumb(Crumb crumb);

}
