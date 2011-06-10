package org.flexpay.common.process.audit.dao;

import org.jbpm.process.audit.NodeInstanceLog;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import java.util.List;

public class NodeInstanceLogDaoJpa extends JpaDaoSupport implements NodeInstanceLogDao {

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public List<NodeInstanceLog> findNodeInstances(long processInstanceId) {
		return getJpaTemplate().find("FROM NodeInstanceLog n WHERE n.processInstanceId = ? ORDER BY date", processInstanceId);
	}

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public List<NodeInstanceLog> findNodeInstances(long processInstanceId, String nodeId) {
		return getJpaTemplate().find("FROM NodeInstanceLog n WHERE n.processInstanceId = ? AND n.nodeId = ? ORDER BY date",
				processInstanceId, nodeId);
	}

	@NotNull
	@Override
	public NodeInstanceLog create(@NotNull NodeInstanceLog node) {
		getJpaTemplate().persist(node);
		return node;
	}

}
