package org.flexpay.common.actions.interceptor;

import org.flexpay.common.actions.breadcrumbs.Crumb;

public interface BreadCrumbAware {

	void setCrumb(Crumb crumb);

}
