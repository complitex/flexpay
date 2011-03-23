package org.flexpay.eirc.process.quittance.report;

import org.flexpay.common.persistence.MeasureUnit;
import org.flexpay.common.util.TranslationUtil;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Group of quittance details that have the same service
 */
public class ServiceGroup {

	private Service service;
	private SortedSet<QuittanceDetails> detailses = new TreeSet<QuittanceDetails>(new DateTillComparator());

	public ServiceGroup(@NotNull Service service) {
		this.service = service;
	}

	public boolean canAdd(@NotNull QuittanceDetails qd) {
		return service.equals(qd.getConsumer().getService());
	}

	/**
	 * Add QuittanceDetails to a detailses for later processing.
	 * <p/>
	 * <code>qd</code> should have the same service type and service provider
	 *
	 * @param qd QuittanceDetails to add
	 * @return <code>true</code> if details can be added, or <code>false</code> otherwise
	 */
	public boolean add(@NotNull QuittanceDetails qd) {

		return canAdd(qd) && detailses.add(qd);
	}

	@NotNull
	public Service getService() {
		return service;
	}

	/**
	 * Get quittances of specified type and provider sorted by month
	 *
	 * @return Set of QuittanceDetails
	 */
	@NotNull
	public SortedSet<QuittanceDetails> getDetailses() {
		return detailses;
	}

	@NotNull
	private ServiceTotalsBase initGroup() throws Exception {
		ServiceType type = service.getServiceType();
		ServiceTotalsBase totals =
				service.isSubService() ?
				new SubServiceTotals(type) : new ServiceTotals(type);

		// init measure unit name
		MeasureUnit unit = service.getMeasureUnit();
		if (unit != null) {
			String unitName = TranslationUtil.getTranslation(unit.getUnitNames()).getName();
			totals.setExpenceUnitKey(unitName);
		}

		return totals;
	}

	@NotNull
	public ServiceTotalsBase addToGroup(@Nullable ServiceTotalsBase totals) throws Exception {

		if (totals == null) {
			totals = initGroup();
		}

		if (!service.getServiceType().equals(totals.getServiceType())) {
			throw new IllegalArgumentException(
					"Invalid service totals type: " +
					"expected #" + service.getServiceType().getId() +
					", but found #" + totals.getServiceType().getId());
		}

		boolean isFirst = true;
		for (Iterator<QuittanceDetails> it = detailses.iterator(); it.hasNext();) {

			QuittanceDetails qd = it.next();
			boolean isLast = !it.hasNext();

			if (isFirst) {
				totals.addIncomingDebt(qd.getIncomingBalance());
				totals.setTariff(qd.getRate());
			}
			if (isLast) {
				totals.addOutgoingDebt(qd.getOutgoingBalance());
			}

			totals.addAmount(qd.getAmount());
			totals.addExpence(qd.getExpence());
			totals.addCharges(qd.getAmount());
			totals.addPayed(qd.getPayment());
			totals.addPrivilege(qd.getBenifit());
			totals.addSubsidy(qd.getSubsidy());
			totals.addRecalculation(qd.getRecalculation());

			isFirst = false;
		}

		return totals;
	}

}
