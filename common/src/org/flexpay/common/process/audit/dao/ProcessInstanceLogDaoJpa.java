package org.flexpay.common.process.audit.dao;

import org.jbpm.process.audit.ProcessInstanceLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.List;

public class ProcessInstanceLogDaoJpa extends JpaDaoSupport implements ProcessInstanceLogDao {

	private static final Logger log = LoggerFactory.getLogger(ProcessInstanceLogDaoJpa.class);

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public List<ProcessInstanceLog> findProcessInstances() {
		return getJpaTemplate().find("FROM ProcessInstanceLog");
	}

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public List<ProcessInstanceLog> findProcessInstances(@NotNull String processId) {
		return getJpaTemplate().find("FROM ProcessInstanceLog p WHERE p.processId = ?", processId);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public ProcessInstanceLog findProcessInstance(final long processInstanceId) {
		log.debug("Execute findProcessInstance");
		/*
		List<ProcessInstanceLog> result =
				getJpaTemplate().find("FROM ProcessInstanceLog p WHERE p.processInstanceId = ?", processInstanceId);

		return result != null && result.size() != 0? result.get(result.size() - 1 ): null;
              */
		return getJpaTemplate().execute(new JpaCallback<ProcessInstanceLog>() {
			@Override
			public ProcessInstanceLog doInJpa(EntityManager entityManager) throws PersistenceException {
				List resultList = entityManager
						.createQuery("FROM ProcessInstanceLog WHERE processInstanceId = :processInstance")
						.setParameter("processInstance", processInstanceId)
						.getResultList();
				ProcessInstanceLog result = resultList != null && resultList.size() != 0 ?
						(ProcessInstanceLog) resultList.get(resultList.size() - 1 ) : null;
				if (result != null) {
					Date endDate = (Date) entityManager
							.createQuery("SELECT p.end FROM ProcessInstanceLog p WHERE p.processInstanceId = :processInstance")
							.setParameter("processInstance", processInstanceId)
							.getSingleResult();
					result.setEnd(endDate);
					log.debug("End date: {}", endDate);
				}
				return result;
				//find(ProcessInstanceLog.class, processInstanceId);
			}
		});
	}

	@Nullable
	@Override
	public ProcessInstanceLog findNotEndedProcessInstance(long processInstaceId) {
		@SuppressWarnings("unchecked")
		List<ProcessInstanceLog> result = getJpaTemplate().
				find("FROM ProcessInstanceLog p WHERE p.processInstanceId = ? and p.end is null", processInstaceId);

		return result != null && result.size() != 0? result.get(result.size() - 1): null;
	}

	@NotNull
	@Override
	public ProcessInstanceLog create(@NotNull final ProcessInstanceLog processInstance) {
		getJpaTemplate().persist(processInstance);
		return processInstance;
	}

	@NotNull
	@Override
	public ProcessInstanceLog update(@NotNull final ProcessInstanceLog processInstance) {
		return getJpaTemplate().execute(new JpaCallback<ProcessInstanceLog>() {
			@Override
			public ProcessInstanceLog doInJpa(EntityManager entityManager) throws PersistenceException {
				return entityManager.merge(processInstance);
			}
		});
		//return getJpaTemplate().merge(processInstance);
	}
}
