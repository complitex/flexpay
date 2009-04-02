package org.flexpay.common.dao;

import java.util.List;
import java.util.Date;

import org.flexpay.common.process.Process;
import org.flexpay.common.process.ProcessState;
import org.flexpay.common.process.sorter.ProcessSorter;
import org.flexpay.common.dao.paging.Page;
import org.jbpm.graph.exe.ProcessInstance;

/**
 * Dao for accessing information about processes (extracts data from JBPM tables actually)
 */
public interface ProcessDao {

	/**
	 * Returns list of all processes in database
	 *
	 * @param sorter process sorter
	 * @param pager pager
	 * @return list of all processes in database
	 */
	List<Process> findProcesses(ProcessSorter sorter, Page<Process> pager, Date startFrom, Date endBefore, ProcessState state);

	/**
	 * Converts JBPM process instance object into {@link org.flexpay.common.process.Process}
	 * @param processInstance instance to be converted
	 * @return process information
	 */
	Process getProcessInfoWithVariables(ProcessInstance processInstance);
}
