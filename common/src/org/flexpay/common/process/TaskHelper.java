package org.flexpay.common.process;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jbpm.JbpmContext;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.def.Transition;
import org.slf4j.Logger;

import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class TaskHelper {

    @Nullable
    public static TaskInstance getTaskInstance(@NotNull final ProcessManager processManager, @NotNull final long processInstanceId, @NotNull final Logger log) {
        return processManager.execute(new ContextCallback<TaskInstance>() {
            public TaskInstance doInContext(@NotNull JbpmContext context) {
                ProcessInstance processInstance = context.getProcessInstance(processInstanceId);
                Collection tasks = processInstance.getTaskMgmtInstance().getTaskInstances();
                for (Object o : tasks) {
                    TaskInstance task = (TaskInstance) o;
                    log.debug("Task: name={}, actorId={}, end={}, start={}, create={}, ended={}, open={}",
                            new Object[]{task.getName(), task.getActorId(), task.getEnd(), task.getStart(), task.getCreate(), task.hasEnded(), task.isOpen()});
                    if (task.isSignalling()) {
                        return task;
                    }
                }
                return null;
            }
        });
    }

    @Nullable
    public static TaskInstance getTaskInstance(@NotNull final ProcessManager processManager, @NotNull final long processInstanceId, @NotNull final String actor, @NotNull final Logger log) {
        return processManager.execute(new ContextCallback<TaskInstance>() {
            public TaskInstance doInContext(@NotNull JbpmContext context) {
                ProcessInstance processInstance = context.getProcessInstance(processInstanceId);
                Collection tasks = processInstance.getTaskMgmtInstance().getTaskInstances();
                for (Object o : tasks) {
                    TaskInstance task = (TaskInstance) o;
                    log.debug("Task: name={}, actorId={}, end={}, start={}, create={}, ended={}, open={}",
                            new Object[]{task.getName(), task.getActorId(), task.getEnd(), task.getStart(), task.getCreate(), task.hasEnded(), task.isOpen()});
                    if (task.isSignalling() && task.getActorId().equals(actor)) {
                        return task;
                    }
                }
                return null;
            }
        });
    }

    public static Set getTransitions(@NotNull final ProcessManager processManager, @NotNull final String actorName, @NotNull final long processInstanceId,
                                      @Nullable final String transitionName, @NotNull final Logger log) {
        return processManager.execute(new ContextCallback<Set>() {
            public Set doInContext(@NotNull JbpmContext context) {
                ProcessInstance processInstance = context.getProcessInstance(processInstanceId);
                Collection tasks = processInstance.getTaskMgmtInstance().getTaskInstances();
                if (tasks.isEmpty()) {
                    return Collections.emptySet();
                }
                TaskInstance actorTask = null;
                for (Object o : tasks) {
                    TaskInstance task = (TaskInstance) o;
                    log.debug("Task: name={}, actorId={}, end={}, start={}, create={}, ended={}, open={}, signaling={}, canceled={}",
                            new Object[]{task.getName(), task.getActorId(), task.getEnd(), task.getStart(), task.getCreate(), task.hasEnded(), task.isOpen(), task.isSignalling(), task.isCancelled()});
                    if (task.isSignalling()) {
                        log.debug("Task: {}, {}", new Object[]{task.getName(), task.getActorId()});
                        if (actorName.equals(task.getActorId())) {
                            actorTask = task;
                            break;
                        }
                    }
                }
                if (actorTask == null) {
                    return Collections.emptySet();
                }
                Set transitions = processInstance.getRootToken().getAvailableTransitions();
                log.debug("Count transitions {}", transitions.size());
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
                        processInstance.getRootToken().signal(t);
                        return Collections.emptySet();
                    }
                }

                return transitions;
            }
        });
    }
}