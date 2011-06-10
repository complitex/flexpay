package org.flexpay.common.process.audit.dao;

import org.jbpm.process.audit.NodeInstanceLog;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface NodeInstanceLogDao {

	@NotNull
	List<NodeInstanceLog> findNodeInstances(long processInstanceId);

	@NotNull
	List<NodeInstanceLog> findNodeInstances(long processInstanceId, String nodeId);

	@NotNull
	NodeInstanceLog create(@NotNull NodeInstanceLog node);

}
