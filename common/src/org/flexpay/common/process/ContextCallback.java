package org.flexpay.common.process;

import org.jbpm.JbpmContext;
import org.jetbrains.annotations.NotNull;

public interface ContextCallback<T> {

	T doInContext(@NotNull JbpmContext context);
}
