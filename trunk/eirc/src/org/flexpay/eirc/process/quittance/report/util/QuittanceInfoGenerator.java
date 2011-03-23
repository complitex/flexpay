package org.flexpay.eirc.process.quittance.report.util;

import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.process.quittance.report.ServiceGroup;
import org.flexpay.eirc.process.quittance.report.ServiceTotals;
import org.flexpay.eirc.process.quittance.report.ServiceTotalsBase;
import org.flexpay.eirc.process.quittance.report.SubServiceTotals;
import org.flexpay.eirc.reports.quittance.QuittancePrintInfo;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Helper class used to form QuittanceInfos
 */
public class QuittanceInfoGenerator {

	/**
	 * Build quittance info
	 *
	 * @param q Quittance
	 * @param info QuittanceInfo to build
	 * @throws Exception if failure occurs
	 */
	public static void buildInfo(Quittance q, QuittancePrintInfo info) throws Exception {

		Map<Service, ServiceGroup> groups = makeServiceGroups(q);

		Map<Service, ServiceTotals> servicesTotals = CollectionUtils.map();
		Map<ServiceType, ServiceTotalsBase> serviceTypesTotals = CollectionUtils.map();

		// now setup services totals
		buildServicesTotals(groups, servicesTotals, serviceTypesTotals);

		// now setup subservices totals and add them to services totals
		buildSubservicesTotals(groups, servicesTotals, serviceTypesTotals);

		// build from parent services totals map and put them to quittance info
		Map<ServiceType, ServiceTotals> totalsMap = CollectionUtils.map();
		for (Map.Entry<Service, ServiceTotals> entry : servicesTotals.entrySet()) {
			totalsMap.put(entry.getKey().getServiceType(), entry.getValue());
		}

		info.setServicesTotals(totalsMap);
	}

	/**
	 * Build quittance info
	 *
	 * @param q Quittance
	 * @return QuittanceInfo
	 * @throws Exception if failure occurs
	 */
	public static QuittancePrintInfo buildInfo(Quittance q) throws Exception {

		QuittancePrintInfo qi = new QuittancePrintInfo();
		buildInfo(q, qi);
		return qi;
	}

	/**
	 * Build subservices totals, fills only <code>serviceTypesTotals</code>
	 *
	 * @param groups			 Service groups to get subservices from
	 * @param servicesTotals	 Calculated parent services totals
	 * @param serviceTypesTotals Calculated totals by service types
	 * @throws Exception if failure occurs
	 */
	public static void buildSubservicesTotals(Map<Service, ServiceGroup> groups,
											  Map<Service, ServiceTotals> servicesTotals,
											  Map<ServiceType, ServiceTotalsBase> serviceTypesTotals)
			throws Exception {

		for (Service service : groups.keySet()) {
			if (service.isSubService()) {
				// check if group of specified type was already added
				ServiceTotalsBase totals = serviceTypesTotals.get(service.getServiceType());

				// calculate totals based on (possibly) calculated totals
				totals = groups.get(service).addToGroup(totals);
				serviceTypesTotals.put(service.getServiceType(), totals);

				// add calculated totals to parent service subtotals
				Service parentService = service.getParentService();
				ServiceTotals serviceTotals = servicesTotals.get(parentService);
				if (serviceTotals == null) {
					throw new IllegalStateException("No parent service in quittance: " + service +
					", totals: " + totals);
				}
				serviceTotals.setSubServiceTotals((SubServiceTotals) totals);
			}
		}
	}

	/**
	 * Build services totals, does skip subservices when fetching services from
	 * <code>groups</code>
	 *
	 * @param groups			 Service groups to get services from
	 * @param servicesTotals	 Map to store services totals in
	 * @param serviceTypesTotals Map to store totals by service types in
	 * @throws Exception if failure occurs
	 */
	public static void buildServicesTotals(Map<Service, ServiceGroup> groups,
										   Map<Service, ServiceTotals> servicesTotals,
										   Map<ServiceType, ServiceTotalsBase> serviceTypesTotals)
			throws Exception {

		for (Service service : groups.keySet()) {
			if (!service.isSubService()) {
				// check if group of specified type was already added
				ServiceTotalsBase totals = serviceTypesTotals.get(service.getServiceType());

				// calculate totals based on (possibly) calculated totals
				totals = groups.get(service).addToGroup(totals);
				servicesTotals.put(service, (ServiceTotals) totals);
				serviceTypesTotals.put(service.getServiceType(), totals);
			}
		}
	}

	/**
	 * Get a collection of service groups (usually contains single QuittanceDetails but may
	 * contain multiple if there are several quittance details in current quittance in
	 * period)
	 *
	 * @param q Quittance to extract groups from
	 * @return Service to groups map
	 */
	@NotNull
	public static Map<Service, ServiceGroup> makeServiceGroups(Quittance q) {
		Map<Service, ServiceGroup> serviceGroups = CollectionUtils.map();
		for (QuittanceDetails qd : q.getQuittanceDetails()) {
			Service service = qd.getConsumer().getService();
			if (!serviceGroups.containsKey(service)) {
				ServiceGroup group = new ServiceGroup(service);
				serviceGroups.put(service, group);
			}

			serviceGroups.get(service).add(qd);
		}

		return serviceGroups;
	}

}
