package org.flexpay.common.process.handler;

import org.flexpay.common.exception.FlexPayException;

import java.util.Map;

public class CheckArrayEndingActionHandler extends FlexPayActionHandler {

	private static final String RESULT_END = "end";

	private String arrayName;
	private String arraySizeName;

	@Override
	public String execute2(Map<String, Object> parameters) throws FlexPayException {

		Object[] array = (Object[])parameters.get(arrayName);
		Integer arraySize = (Integer)parameters.get(arraySizeName);

		if (array == null) {
			log.error("Array '{}' did not find in parameters", arrayName);
			return RESULT_ERROR;
		}

		if (arraySize == null) {
			log.error("Array size '{}' did not find in parameters", arraySize);
			return RESULT_ERROR;
		}

		if (array.length < arraySize) {
			log.error("Array index out Of bounds. Actual array size is {}, required is {}", array.length, arraySize);
			return RESULT_ERROR;
		}

		if (array.length > arraySize) {
			return RESULT_NEXT;
		}
		log.debug("Finished {}", getProcessId());

		return RESULT_END;
	}

	public void setArrayName(String arrayName) {
		this.arrayName = arrayName;
	}

	public void setArraySizeName(String arraySizeName) {
		this.arraySizeName = arraySizeName;
	}
}
