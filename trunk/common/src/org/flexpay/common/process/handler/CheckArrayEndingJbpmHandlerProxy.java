package org.flexpay.common.process.handler;

import org.springmodules.workflow.jbpm31.JbpmHandlerProxy;

public class CheckArrayEndingJbpmHandlerProxy extends JbpmHandlerProxy {

	private String arrayName;
	private String arraySizeName;

	@Override
	protected Object lookupBean(Class type) {
		Object bean = super.lookupBean(type);
		if (bean instanceof CheckArrayEndingActionHandler) {
			CheckArrayEndingActionHandler handler = (CheckArrayEndingActionHandler)bean;

			handler.setArrayName(arrayName);
			handler.setArraySizeName(arraySizeName);
		}

		return bean;
	}

	public void setArrayName(String arrayName) {
		this.arrayName = arrayName;
	}

	public void setArraySizeName(String arraySizeName) {
		this.arraySizeName = arraySizeName;
	}
}
