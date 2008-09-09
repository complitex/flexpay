package org.flexpay.eirc.process.quittance.report;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.ServiceType;

import java.util.*;

public class ServiceTotals extends ServiceTotalsBase {

	private Map<ServiceType, SubServiceTotals> subServicesTotals = CollectionUtils.map();

	public ServiceTotals(ServiceType serviceType) {
		super(serviceType);
	}

	public Map<ServiceType, SubServiceTotals> getSubServicesTotalsMap() {
		return subServicesTotals;
	}

	public List<SubServiceTotals> getSubServicesTotalsList() {
		SortedSet<SubServiceTotals> set = new TreeSet<SubServiceTotals>(new ServiceTotalsComparator<SubServiceTotals>());
		set.addAll(subServicesTotals.values());
		return new ArrayList<SubServiceTotals>(set);
	}

	public void setSubServiceTotals(SubServiceTotals totals) {
		subServicesTotals.put(totals.getServiceType(), totals);
	}
}
