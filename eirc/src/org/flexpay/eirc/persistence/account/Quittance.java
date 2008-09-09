package org.flexpay.eirc.persistence.account;

import org.flexpay.common.persistence.DomainObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.persistence.ServiceProvider;
import org.flexpay.eirc.persistence.ServiceType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.*;

public class Quittance extends DomainObject {

	private Date creationDate;
	private ServiceOrganisation serviceOrganisation;
	private Integer orderNumber;
	private Date dateFrom;
	private Date dateTill;
	private EircAccount eircAccount;
	private Set<QuittanceDetailsQuittance> quittanceDetailsQuittances = Collections.emptySet();


	/**
	 * Get total details by service type, contains summs for all details of specified service type
	 *
	 * @param serviceType ServiceType
	 * @return QuittanceDetails with total summs
	 */
	@Nullable
	public QuittanceDetails calculateTotals(@NotNull ServiceType serviceType) {

		List<QuittanceDetails> detailses = CollectionUtils.list();
		for (QuittanceDetails quittanceDetails : getQuittanceDetails()) {
			if (quittanceDetails.getConsumer().getService().getServiceType().getCode() == serviceType.getCode()) {
				detailses.add(quittanceDetails);
			}
		}

		return calculateTotalQuittanceDetail(detailses);
	}

	@Nullable
	private QuittanceDetails calculateTotalQuittanceDetail(List<QuittanceDetails> list) {
		if (list.isEmpty()) {
			return null;
		}

		Map<ServiceProvider, List<QuittanceDetails>> map = groupByServiceProvider(list);
		Map<ServiceProvider, QuittanceDetails> totals = CollectionUtils.map();
		for (Map.Entry<ServiceProvider, List<QuittanceDetails>> entry : map.entrySet()) {
			ServiceProvider serviceProvider = entry.getKey();
			List<QuittanceDetails> detailses = entry.getValue();
			// sort by dateTill
			Collections.sort(detailses, new Comparator<QuittanceDetails>() {
				public int compare(QuittanceDetails o1, QuittanceDetails o2) {
					return o1.getMonth().compareTo(o2.getMonth());
				}
			});

			if (detailses.size() == 1) {
				totals.put(serviceProvider, detailses.get(0));
			} else {
				QuittanceDetails first = detailses.get(0);
				QuittanceDetails last = detailses.get(list.size() - 1);
				last.setIncomingBalance(first.getOutgoingBalance());
				for (int i = 0; i < list.size() - 1; i++) {
					last.add(list.get(i));
				}
				totals.put(serviceProvider, last);
			}
		}

		// summ balances
		Iterator<QuittanceDetails> it = totals.values().iterator();
		QuittanceDetails result = it.next();
		while (it.hasNext()) {
			QuittanceDetails details = it.next();
			result.setIncomingBalance(sum(result.getIncomingBalance(), details.getIncomingBalance()));
			result.setOutgoingBalance(sum(result.getOutgoingBalance(), details.getOutgoingBalance()));
			result.add(details);
		}

		return result;
	}

	private BigDecimal sum(BigDecimal bal1, BigDecimal bal2) {
		if (bal1 == null || bal1.compareTo(BigDecimal.ZERO) < 0) {
			bal1 = BigDecimal.ZERO;
		}
		if (bal2 == null || bal2.compareTo(BigDecimal.ZERO) < 0) {
			bal2 = BigDecimal.ZERO;
		}

		return bal1.add(bal2);
	}

	private Map<ServiceProvider, List<QuittanceDetails>> groupByServiceProvider(List<QuittanceDetails> list) {

		Map<ServiceProvider, List<QuittanceDetails>> result = CollectionUtils.map();
		for (QuittanceDetails quittanceDetails : list) {
			ServiceProvider serviceProvider = quittanceDetails.getConsumer().getService().getServiceProvider();
			List<QuittanceDetails> quittanceDetailsList = result.get(serviceProvider);
			if (quittanceDetailsList == null) {
				result.put(serviceProvider, new ArrayList<QuittanceDetails>());
			}
			result.get(serviceProvider).add(quittanceDetails);
		}

		return result;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the serviceOrganisation
	 */
	public ServiceOrganisation getServiceOrganisation() {
		return serviceOrganisation;
	}

	/**
	 * @param serviceOrganisation the serviceOrganisation to set
	 */
	public void setServiceOrganisation(ServiceOrganisation serviceOrganisation) {
		this.serviceOrganisation = serviceOrganisation;
	}

	/**
	 * @return the dateFrom
	 */
	public Date getDateFrom() {
		return dateFrom;
	}

	/**
	 * @param dateFrom the dateFrom to set
	 */
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	/**
	 * @return the dateTill
	 */
	public Date getDateTill() {
		return dateTill;
	}

	/**
	 * @param dateTill the dateTill to set
	 */
	public void setDateTill(Date dateTill) {
		this.dateTill = dateTill;
	}

	/**
	 * @return the quittanceDetails
	 */
	public Set<QuittanceDetails> getQuittanceDetails() {
		Set<QuittanceDetails> quittanceDetails = CollectionUtils.set();
		for (QuittanceDetailsQuittance el : quittanceDetailsQuittances) {
			quittanceDetails.add(el.getQuittanceDetails());
		}

		return quittanceDetails;
	}

	public void addQuittanceDetails(QuittanceDetails quittanceDetails) {

		QuittanceDetailsQuittance quittanceDetailsQuittance = new QuittanceDetailsQuittance();
		quittanceDetailsQuittance.setQuittance(this);
		quittanceDetailsQuittance.setQuittanceDetails(quittanceDetails);

		if (quittanceDetailsQuittances == Collections.EMPTY_SET) {
			quittanceDetailsQuittances = CollectionUtils.set();
		}
		quittanceDetailsQuittances.add(quittanceDetailsQuittance);
	}

	/**
	 * @param detailses the quittanceDetails to set
	 */
	public void addQuittanceDetails(@NotNull Collection<QuittanceDetails> detailses) {

		for (QuittanceDetails quittanceDetails : detailses) {
			addQuittanceDetails(quittanceDetails);
		}
	}

	/**
	 * @return the quittanceDetailsQuittances
	 */
	public Set<QuittanceDetailsQuittance> getQuittanceDetailsQuittances() {
		return quittanceDetailsQuittances;
	}

	/**
	 * @param quittanceDetailsQuittances the quittanceDetailsQuittances to set
	 */
	public void setQuittanceDetailsQuittances(
			Set<QuittanceDetailsQuittance> quittanceDetailsQuittances) {
		this.quittanceDetailsQuittances = quittanceDetailsQuittances;
	}

	/**
	 * @return the orderNumber
	 */
	public Integer getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the eircAccount
	 */
	public EircAccount getEircAccount() {
		return eircAccount;
	}

	/**
	 * @param eircAccount the eircAccount to set
	 */
	public void setEircAccount(EircAccount eircAccount) {
		this.eircAccount = eircAccount;
	}

	/**
	 * Get account number of associated EIRC account
	 * 
	 * @return account number
	 */
	public String getAccountNumber() {
		return getEircAccount().getAccountNumber();
	}

	@NotNull
	public Long getEircAccountId() {
		return new Stub<EircAccount>(eircAccount).getId();
	}
}
