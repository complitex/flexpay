package org.flexpay.common.process;

import org.jetbrains.annotations.NotNull;
import org.jbpm.JbpmContext;

public interface ContextCallback<T> {

	T doInContext(@NotNull JbpmContext context);
}
