package org.flexpay.common.process;

import org.apache.commons.lang.StringUtils;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.Transition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.flexpay.common.util.CollectionUtils.set;

public abstract class TaskHelper {

    @Nullable
    public static TaskInstance getTaskInstance(@NotNull final ProcessManager processManager, final long processInstanceId, @NotNull final Logger log) {
        return processManager.execute(new ContextCallback<TaskInstance>() {
            @Override
            public TaskInstance doInContext(@NotNull JbpmContext context) {
                ProcessInstance processInstance = context.getProcessInstance(processInstanceId);
				if (processInstance == null){
					log.debug("Process instance with id = {} deleted", processInstanceId);
					return null;
				}
                Collection<?> tasks = processInstance.getTaskMgmtInstance().getTaskInstances();
                for (Object o : tasks) {
                    TaskInstance task = (TaskInstance) o;
                    if (log.isDebugEnabled()) {
                        log.debug("Task: name={}, actorId={}, end={}, start={}, create={}, ended={}, open={}",
                                new Object[]{task.getName(), task.getActorId(), task.getEnd(), task.getStart(), task.getCreate(), task.hasEnded(), task.isOpen()});
                    }
                    if (task.isSignalling()) {
                        return task;
                    }
                }
                return null;
            }
        });
    }

    @Nullable
    public static TaskInstance getTaskInstance(@NotNull final ProcessManager processManager, final long processInstanceId, @NotNull final String actor, @NotNull final Logger log) {
        return processManager.execute(new ContextCallback<TaskInstance>() {
            @Override
            public TaskInstance doInContext(@NotNull JbpmContext context) {
                ProcessInstance processInstance = context.getProcessInstance(processInstanceId);
				if (processInstance == null){
					log.debug("Process instance with id = {} deleted", processInstanceId);
					return null;
				}
                Collection<?> tasks = processInstance.getTaskMgmtInstance().getTaskInstances();
                for (Object o : tasks) {
                    TaskInstance task = (TaskInstance) o;
                    if (log.isDebugEnabled()) {
                        log.debug("Task: name={}, actorId={}, end={}, start={}, create={}, ended={}, open={}",
                                new Object[] {task.getName(), task.getActorId(), task.getEnd(), task.getStart(), task.getCreate(), task.hasEnded(), task.isOpen()});
                    }
                    if (task.isSignalling() && actor.equals(task.getActorId())) {
                        return task;
                    }
                }
                return null;
            }
        });
    }

	public static Set<?> getTransitions(@NotNull final ProcessManager processManager, @NotNull final String actorName, final long processInstanceId,
                                      @Nullable final String transitionName, @NotNull final Logger log) {
		return getTransitions(processManager, actorName, processInstanceId, transitionName, log, true);
	}

    public static Set<?> getTransitions(@NotNull final ProcessManager processManager, @NotNull final String actorName, final long processInstanceId,
                                      @Nullable final String transitionName, @NotNull final Logger log, boolean required) {

		final Set<Integer> status = set();
		final Integer LOCKED = 1;
		final Integer ENDED = 2;
		Set<?> result;
		do {
			log.debug("Call to transition: {}, actor: {}, process instance id: {}", new Object[]{transitionName, actorName, processInstanceId});
			result = processManager.execute(new ContextCallback<Set<?>>() {
				@Override
				public Set<?> doInContext(@NotNull JbpmContext context) {
					ProcessInstance processInstance = context.getProcessInstance(processInstanceId);
					if (processInstance == null) {
						log.debug("Process instance with id = {} deleted", processInstanceId);
						return Collections.emptySet();
					}
					if (processInstance.hasEnded()) {
						log.debug("Process ended with id = {}", processInstanceId);
						status.add(ENDED);
						return Collections.emptySet();
					}
					log.debug("Root token of process instance locked '{}' ({})", processInstance.getRootToken().isLocked(),
							processInstance.getRootToken().getLockOwner());

					if (processInstance.getRootToken().isLocked()) {
						status.add(LOCKED);
						return Collections.emptySet();
					}
					Collection<?> tasks = processInstance.getTaskMgmtInstance().getTaskInstances();
					if (tasks.isEmpty()) {
						return Collections.emptySet();
					}
					TaskInstance actorTask = null;

                    if (log.isDebugEnabled()) {
                        log.debug("Count tasks: {}", tasks.size());
                    }

					for (Object o : tasks) {
						TaskInstance task = (TaskInstance) o;
						if (log.isDebugEnabled()) {
							log.debug("Task: name={}, actorId={}, end={}, start={}, create={}, ended={}, open={}, signaling={}, canceled={}",
									new Object[] {task.getName(), task.getActorId(), task.getEnd(), task.getStart(), task.getCreate(), task.hasEnded(), task.isOpen(), task.isSignalling(), task.isCancelled()});
						}
						if (task.isSignalling()) {
							log.debug("Task: {}, {}", task.getName(), task.getActorId());
							if (actorName.equals(task.getActorId())) {
								actorTask = task;
								break;
							}
						}
					}
					if (actorTask == null) {
						return Collections.emptySet();
					}
					//Set<?> transitions = processInstance.getRootToken().getAvailableTransitions();
					Set<?> transitions = new HashSet(actorTask.getToken().getAvailableTransitions());
					if (log.isDebugEnabled()) {
						log.debug("Count transitions {}", transitions.size());
						for (Object o : transitions) {
							Transition transition = (Transition) o;
							log.debug("Transition name: {}", transition.getName());
						}
					}
					if (transitionName != null && transitionName.length() > 0) {
						Transition t = null;
						for (Object o : transitions) {
							Transition transition = (Transition) o;
							if (transitionName.equals(transition.getName())) {
								t = transition;
								break;
							}
						}
						if (t != null) {
							actorTask.getToken().signal(t);
							Set<Transition> signaledTransitions = set();
							signaledTransitions.add(t);
							return signaledTransitions;
						}
					}

					return transitions;
				}
			});
			if (status.contains(ENDED)) {
				break;
			} else if (status.contains(LOCKED)) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					log.warn("While root token locked", e);
				}
			} else if (required && StringUtils.isNotEmpty(transitionName) && (result == null || result.isEmpty())) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					log.warn("While signaled to transition", e);
				}
			}
		} while (status.contains(LOCKED) || (required && StringUtils.isNotEmpty(transitionName) && (result == null || result.isEmpty())));

		return result;
    }
}
