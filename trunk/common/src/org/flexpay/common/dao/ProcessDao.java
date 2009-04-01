package org.flexpay.common.dao;

import java.util.List;
import org.flexpay.common.process.Process;
import org.flexpay.common.process.sorter.ProcessSorter;
import org.jetbrains.annotations.NotNull;
import org.jbpm.graph.exe.ProcessInstance;

/**
 * Dao for accessing information about processes (extracts data from JBPM tables actually)
 */
public interface ProcessDao {

	/**
	 * Returns list of all processes in database
	 *
	 * @param sorter process sorter
	 * @return list of all processes in database
	 */
	List<Process> findAllProcesses(ProcessSorter sorter);

	/**
	 * Converts JBPM process instance object into {@link org.flexpay.common.process.Process}
	 * @param processInstance instance to be converted
	 * @return process information
	 */
	Process getProcessInfoWithVariables(ProcessInstance processInstance);
}
