package org.flexpay.common.dao.finder.impl;

import org.springframework.aop.support.DefaultIntroductionAdvisor;

public class FinderIntroductionAdvisor extends DefaultIntroductionAdvisor {

	public FinderIntroductionAdvisor() {
		super(new FinderIntroductionInterceptor());
	}

}
