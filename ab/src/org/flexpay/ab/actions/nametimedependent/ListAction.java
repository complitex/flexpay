package org.flexpay.ab.actions.nametimedependent;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.NameDateInterval;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TemporaryValue;
import org.flexpay.common.persistence.Translation;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class ListAction<
		TV extends TemporaryValue<TV>,
		DI extends NameDateInterval<TV, DI>,
		NTD extends NameTimeDependentChild<TV, DI>,
		T extends Translation> extends ActionBase<TV, DI, NTD, T> {

	protected List<?> objectNames;

	@NotNull
	public String doExecute() throws Exception {

		long start = System.currentTimeMillis();

		ArrayStack filterArrayStack = getFilters();
		for (Object filter : filterArrayStack) {
			((PrimaryKeyFilter<?>) filter).initFilter(session);
		}
		ArrayStack filters = parentService.initFilters(filterArrayStack, getUserPreferences().getLocale());
		setFilters(filters);

		initObjects(filters);

		log.info("Listing {} ms", (System.currentTimeMillis() - start));

		return SUCCESS;
	}

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

	protected void initObjects(ArrayStack filters) throws FlexPayException {
		objectNames = nameTimeDependentService.findNames(filters, getPager());
	}

	public List<?> getObjectNames() {
		return objectNames;
	}

}
