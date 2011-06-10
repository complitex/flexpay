package org.flexpay.common.process.audit.dao;

import org.jbpm.process.audit.VariableInstanceLog;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import java.util.List;

public class VariableInstanceLogDaoJpa extends JpaDaoSupport implements VariableInstanceLogDao {

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public List<VariableInstanceLog> findVariableInstances(long processInstanceId) {
		return getJpaTemplate().find("FROM VariableInstanceLog v WHERE v.processInstanceId = ? ORDER BY date", processInstanceId);
	}

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public List<VariableInstanceLog> findVariableInstances(long processInstanceId, String variableId) {
		return getJpaTemplate()
			.find("FROM VariableInstanceLog v WHERE v.processInstanceId = ? AND v.variableId = ? ORDER BY date", processInstanceId, variableId);
	}

	@NotNull
	@Override
	public VariableInstanceLog create(@NotNull VariableInstanceLog variableInstance) {
		getJpaTemplate().persist(variableInstance);
		return variableInstance;
	}

}
