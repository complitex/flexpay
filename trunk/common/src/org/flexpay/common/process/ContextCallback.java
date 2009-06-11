package org.flexpay.common.process;

import org.jetbrains.annotations.NotNull;
import org.jbpm.JbpmContext;
import org.jbpm.taskmgmt.exe.TaskInstance;

public interface ContextCallback<T> {

	T doInContext(@NotNull JbpmContext context);
}
