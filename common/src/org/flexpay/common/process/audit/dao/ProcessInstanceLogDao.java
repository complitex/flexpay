package org.flexpay.common.process.audit.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.process.persistence.ProcessInstance;
import org.flexpay.common.process.sorter.ProcessSorter;
import org.jbpm.process.audit.ProcessInstanceLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

public interface ProcessInstanceLogDao {

	/**
	 * Returns list of all processes in database
	 *
	 * @param sorter process sorter
	 * @param pager pager
	 * @param startFrom lower bound for start date (if null will not be used)
	 * @param endBefore upper bound for end date (if null will not be used)
	 * @param state allowed state (if null will not be used)
	 * @param name allowed name (if null will not be used)
	 * @return list of all processes in database
	 */
	List<ProcessInstanceLog> findProcessInstances(ProcessSorter sorter, Page pager, Date startFrom, Date endBefore,
											   ProcessInstance.STATE state, String name);

	@NotNull
	List<ProcessInstanceLog> findProcessInstances();

	@NotNull
	List<ProcessInstanceLog> findProcessInstances(@NotNull String processId);

	@Nullable
	ProcessInstanceLog findProcessInstance(long processInstanceId);

	@Nullable
	ProcessInstanceLog findNotEndedProcessInstance(long processInstaceId);

	@NotNull
	ProcessInstanceLog create(@NotNull ProcessInstanceLog processInstance);

	@NotNull
	ProcessInstanceLog update(@NotNull ProcessInstanceLog processInstance);

    void deleteFinishedProcessInstances(@NotNull DateRange range, @Nullable String processId);

}
