package org.flexpay.common.dao;

import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.process.sorter.ProcessSorter;
import org.jbpm.graph.exe.ProcessInstance;

import java.util.Date;
import java.util.List;

/**
 * Dao for accessing information about processes (extracts data from JBPM tables actually)
 */
public interface ProcessDao {

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
	List<Process> findProcesses(ProcessSorter sorter, Page<Process> pager, Date startFrom, Date endBefore,
								ProcessState state, String name);

	/**
	 * Returns list of all unique names of processes in database
	 * @return list of all unique names of processes in database
	 */
	List<String> findAllProcessNames();

	/**
	 * Converts JBPM process instance object into {@link org.flexpay.common.process.Process}
	 * @param processInstance instance to be converted
	 * @return process information
	 */
	Process getProcessInfoWithVariables(ProcessInstance processInstance);

	void deleteProcessInstances(DateRange range, String name);
}
