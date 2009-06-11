package org.flexpay.ab.actions.nametimedependent;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.NameDateInterval;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TemporaryValue;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.service.NameTimeDependentService;
import org.flexpay.common.service.ParentService;
import org.springframework.beans.factory.annotation.Required;

public abstract class ActionBase<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> extends FPActionWithPagerSupport {

	protected NameTimeDependentService<TV, DI, NTD, T> nameTimeDependentService;
	protected ParentService<?> parentService;

	protected abstract ArrayStack getFilters();

	protected abstract void setFilters(ArrayStack filters);

	@Required
	public void setNameTimeDependentService(NameTimeDependentService<TV, DI, NTD, T> nameTimeDependentService) {
		this.nameTimeDependentService = nameTimeDependentService;
	}

	@Required
	public void setParentService(ParentService<?> parentService) {
		this.parentService = parentService;
	}

}
