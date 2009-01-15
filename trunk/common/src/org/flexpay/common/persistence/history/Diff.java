package org.flexpay.common.persistence.history;

import org.flexpay.common.persistence.DomainObjectWithStatus;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * Diff is a set of history records applyed to a single DomainObject
 */
public class Diff extends DomainObjectWithStatus {

	private Date operationTime;
	private Integer objectType;
	private Long objectId;
	private Set<HistoryRecord> historyRecords = Collections.emptySet();
}
