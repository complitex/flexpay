package org.flexpay.eirc.process.quittance.report;

import org.flexpay.eirc.persistence.ServiceType;

import java.util.List;

public class ServiceTotals extends ServiceTotalsBase {

	private List<SubServiceTotals> subServicesTotals;

	public ServiceTotals(ServiceType serviceType) {
		super(serviceType);
	}

	public List<SubServiceTotals> getSubServicesTotals() {
		return subServicesTotals;
	}

	public void setSubServicesTotals(List<SubServiceTotals> subServicesTotals) {
		this.subServicesTotals = subServicesTotals;
	}
}
