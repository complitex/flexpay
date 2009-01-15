package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Language;

/**
 *
 */
public class HistoryRecord extends DomainObject {

	private String oldValue;
	private String newValue;
	private String userName;
	private long fieldType;
	private int operationType;
	private int operationOrder;
	private Language language;

}
