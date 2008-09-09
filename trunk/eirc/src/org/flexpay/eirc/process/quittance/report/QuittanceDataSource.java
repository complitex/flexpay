package org.flexpay.eirc.process.quittance.report;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.Service;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class QuittanceDataSource implements JRDataSource {

	private JRBeanCollectionDataSource collectionDataSource;

	public QuittanceDataSource(List<Quittance> quittances) {
		Collection<QuittanceInfo> infos = initInfos(quittances);
		collectionDataSource = new JRBeanCollectionDataSource(infos);
	}

	private Collection<QuittanceInfo> initInfos(List<Quittance> quittances) {

		List<QuittanceInfo> infos = CollectionUtils.list();

		@NotNull Long accountId = -1L;
		int orderNumber = -1;

		for (Quittance q : quittances) {

			// check account number
			if (isNewAccount(accountId, q)) {
				accountId = q.getEircAccountId();
				orderNumber = q.getOrderNumber();
			}
			// check for quittance order number, only quittances with max order number
			// are taken into account
			else if (orderNumber != q.getOrderNumber()) {
				if (orderNumber < q.getOrderNumber()) {
					throw new IllegalStateException("Invalid order number, was quittances sorted by orderNumber??");
				}

				// OK, just skip quittances with lower numbers
				continue;
			}

			// now build quittance into quittance info
			infos.add(buildInfo(q));
		}

		return infos;
	}

	/**
	 * Check if next quittance has different account
	 *
	 * @param oldId Old quittance id
	 * @param q	 next Quittance
	 * @return <code>true</code> if quittance has different account
	 */
	private boolean isNewAccount(Long oldId, Quittance q) {
		return !oldId.equals(q.getEircAccountId());
	}

	/**
	 * Build quittance info
	 *
	 * @param q Quittance
	 * @return QuittanceInfo
	 */
	private QuittanceInfo buildInfo(Quittance q) {

		Map<Service, ServiceGroup> groups = makeServiceGroups(q);

		Map<Service, ServiceTotals> servicesTotals = CollectionUtils.map();
		Map<ServiceType, ServiceTotalsBase> serviceTypesTotals = CollectionUtils.map();

		// now setup servises totals
		buildServicesTotals(groups, servicesTotals, serviceTypesTotals);

		// now setup subservices totals and add them to services totals
		buildSubservicesTotals(groups, servicesTotals, serviceTypesTotals);

		// build from parent services totals map and put them to quittance info
		Map<ServiceType, ServiceTotals> totalsMap = CollectionUtils.map();
		for (Map.Entry<Service, ServiceTotals> entry : servicesTotals.entrySet()) {
			totalsMap.put(entry.getKey().getServiceType(), entry.getValue());
		}

		QuittanceInfo qi = new QuittanceInfo();
		qi.setServicesTotals(totalsMap);
		return qi;
	}

	private void buildSubservicesTotals(Map<Service, ServiceGroup> groups,
										Map<Service, ServiceTotals> servicesTotals,
										Map<ServiceType, ServiceTotalsBase> serviceTypesTotals) {
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
				if (parentService == null) {
					throw new IllegalStateException("No parent services in quittance: " + service);
				}
				serviceTotals.setSubServiceTotals((SubServiceTotals) totals);
			}
		}
	}

	private void buildServicesTotals(Map<Service, ServiceGroup> groups,
									 Map<Service, ServiceTotals> servicesTotals,
									 Map<ServiceType, ServiceTotalsBase> serviceTypesTotals) {
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
	private Map<Service, ServiceGroup> makeServiceGroups(Quittance q) {
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

	public boolean next() throws JRException {
		return collectionDataSource.next();
	}

	public Object getFieldValue(JRField jrField) throws JRException {
		return collectionDataSource.getFieldValue(jrField);
	}
}
