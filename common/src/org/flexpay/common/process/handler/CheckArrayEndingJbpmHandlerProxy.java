package org.flexpay.common.process.handler;

public class CheckArrayEndingJbpmHandlerProxy {

	private String arrayName;
	private String arraySizeName;

	protected Object lookupBean(Class type) {
		/*
		Object bean = super.lookupBean(type);
		if (bean instanceof CheckArrayEndingActionHandler) {
			CheckArrayEndingActionHandler handler = (CheckArrayEndingActionHandler)bean;

			handler.setArrayName(arrayName);
			handler.setArraySizeName(arraySizeName);
		}

		return bean;
		*/
		return null;
	}

	public void setArrayName(String arrayName) {
		this.arrayName = arrayName;
	}

	public void setArraySizeName(String arraySizeName) {
		this.arraySizeName = arraySizeName;
	}
}
