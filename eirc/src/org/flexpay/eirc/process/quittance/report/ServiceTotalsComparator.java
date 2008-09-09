package org.flexpay.eirc.process.quittance.report;

import java.util.Comparator;
import java.io.Serializable;

public class ServiceTotalsComparator<T extends ServiceTotalsBase>
		implements Comparator<T>, Serializable {

	public int compare(T o1, T o2) {
		return o1.getServiceType().getCode() - o2.getServiceType().getCode();
	}
}
