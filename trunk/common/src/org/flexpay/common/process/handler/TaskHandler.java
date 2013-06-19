package org.flexpay.common.process.handler;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.process.audit.WorkItemCompleteLocker;
import org.flexpay.common.process.dao.WorkItemDao;
import org.flexpay.common.util.SecurityUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.flexpay.common.process.ProcessManager.PARAM_SECURITY_CONTEXT;

@Transactional(readOnly = true)
public abstract class TaskHandler implements WorkItemHandler {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	public final static String RESULT_NEXT = "next";
	public final static String RESULT_ERROR = "error";

	public final static String PROCESS_INSTANCE_ID = "processInstanceId";

	private WorkItemDao workItemDao;

    private Set<Boolean> completed = Collections.synchronizedSet(new HashSet<Boolean>());

    private static ExecutorService pool = Executors.newCachedThreadPool();

	//@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	@Override
	public void executeWorkItem(final WorkItem workItem, final WorkItemManager manager) {
		final Map<String, Object> parameters = workItem.getParameters();
		parameters.put(PROCESS_INSTANCE_ID, workItem.getProcessInstanceId());

		Authentication runAuthentication;
		if (parameters.containsKey(PARAM_SECURITY_CONTEXT)
						&& parameters.get(PARAM_SECURITY_CONTEXT) instanceof Authentication) {
			runAuthentication = (Authentication)parameters.get(PARAM_SECURITY_CONTEXT);
			log.debug("Authentication in parameters: {}", runAuthentication);
		} else {
			runAuthentication = SecurityUtil.getAuthentication();
			parameters.put(PARAM_SECURITY_CONTEXT, runAuthentication);
		}

		final Authentication auth = runAuthentication;

		pool.execute(new Runnable() {
            @Override
            public void run() {
                log.debug("Run execute work item: {} ({})", workItem.getId(), workItem.getName());

                log.debug("Work item authentication: {}", auth);

                SecurityContextHolder.getContext().setAuthentication(auth);

                String result = RESULT_ERROR;
                try {
                    result = execute(parameters);
                } catch (Throwable th) {
                    log.error("Failed execute task handler", th);
                }
                parameters.put("Result", result);
                try {
                    WorkItemCompleteLocker.lock();
                    workItemDao.completeWorkItem(workItem.getId(), parameters);
                } catch (RuntimeException ex) {
                    log.error("Exception in work item: {}", workItem.getName());
                    log.error("{}", ex);
                } finally {
                    WorkItemCompleteLocker.unlock();
                }
                log.debug("Completed work item: {} ({}), {}", new Object[]{workItem.getId(), workItem.getName(), parameters});
                completed.add(true);
            }
        });
		log.debug("Executed work item thread: {}", workItem.getId());
	}

    protected boolean isCompleted() {
        return !completed.isEmpty();
    }

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

	}

	public abstract String execute(Map<String, Object> parameters) throws FlexPayException;

	public static Long getProcessInstanceId(Map<String, Object> parameters) throws FlexPayException {
		return required(PROCESS_INSTANCE_ID, parameters);
	}

	@NotNull
	@SuppressWarnings ({"unchecked"})
	protected static <T> T required(String name, Map<String, Object> params)
			throws FlexPayException {

		return (T)required(name, params, null);
	}

	@NotNull
	@SuppressWarnings ({"unchecked"})
	protected static <T> T required(String name, Map<String, Object> params, String i18nErrorKey)
			throws FlexPayException {

		try {
			T result = (T) params.get(name);
			if (result == null) {
				throw new FlexPayException("No param " + name, i18nErrorKey);
			}
			return result;
		} catch (ClassCastException ex) {
			throw new FlexPayException(ex);
		}
	}

	@Nullable
	@SuppressWarnings ({"unchecked"})
	protected static <T> T optional(String name, Map<String, Object> params)
			throws FlexPayException {

		try {
			return (T) params.get(name);
		} catch (ClassCastException ex) {
			throw new FlexPayException(ex);
		}
	}

	@Required
	public void setWorkItemDao(WorkItemDao workItemDao) {
		this.workItemDao = workItemDao;
	}
}
