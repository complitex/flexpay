package org.flexpay.eirc.process.quittance.report;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.ServiceType;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

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
		List<SubServiceTotals> result = CollectionUtils.list();
		result.addAll(set);
		return result;
	}

	public void setSubServiceTotals(SubServiceTotals totals) {
		subServicesTotals.put(totals.getServiceType(), totals);
	}
}
