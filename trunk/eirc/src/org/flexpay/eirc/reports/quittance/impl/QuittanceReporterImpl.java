package org.flexpay.eirc.reports.quittance.impl;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.AddressService;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.dao.QuittanceReportDao;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.reports.quittance.QuittancePrintInfo;
import org.flexpay.eirc.reports.quittance.QuittancePrintInfoData;
import org.flexpay.eirc.reports.quittance.QuittanceReporter;
import org.flexpay.eirc.reports.quittance.QuittancesPrintStats;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional (readOnly = true)
public class QuittanceReporterImpl implements QuittanceReporter {

	private Logger log = LoggerFactory.getLogger(getClass());

	private QuittanceReportDao reportDao;
	private AddressService addressService;

	/**
	 * Get a list of Quittance printing infos divided by bulks
	 *
	 * @param stub	 ServiceOrganization stub
	 * @param dateFrom Period begin date
	 * @param dateTill Period end date
	 * @return List of Quittances
	 */
	@NotNull
	public QuittancePrintInfoData getPrintData(
			Stub<? extends ServiceOrganization> stub, Date dateFrom, Date dateTill)
			throws Exception {

		QuittancePrintInfoData result = new QuittancePrintInfoData();
		List<QuittancePrintInfo> infos = CollectionUtils.list();
		List<Object[]> datum = reportDao.listPrintInfos(stub.getId(), dateFrom, dateTill);

		Long accountId = -1L;
		int orderNumber = -1;
		long count = 0;
		QuittancesPrintStats stats = new QuittancesPrintStats();
		for (Object[] data : datum) {

			// check account number
			if (!accountId.equals(getAccountId(data))) {
				accountId = getAccountId(data);
				orderNumber = getOrderNumber(data);
			}
			// check for quittance order number, only quittances with max order number
			// are taken into account
			else if (orderNumber != getOrderNumber(data)) {
				if (orderNumber < getOrderNumber(data)) {
					throw new IllegalStateException("Invalid order number, was quittances sorted by orderNumber??");
				}

				// OK, just skip quittances with lower numbers
				continue;
			}

			// now build quittance into quittance info
			QuittancePrintInfo info = new QuittancePrintInfo();
			info.setQuittanceStub(new Stub<Quittance>(getQuittanceId(data)));
			initAddress(data, info);
			infos.add(info);
			stats.addAddress(info.getBuildingAddress());

			++count;
			if (count % 100 == 0) {
				log.info("Generated {} quittance infos", count);
			}
		}

		result.setInfos(infos);
		result.setStats(stats);
		return result;
	}

	private void initAddress(Object[] data, QuittancePrintInfo info) throws Exception {

		Stub<Apartment> stub = new Stub<Apartment>(getApartmentId(data));
		String address = addressService.getAddress(stub, null);
		info.setApartmentAddress(address);

		Stub<Building> buildingStub = new Stub<Building>(getBuildingId(data));
		String buildingAddress = addressService.getBuildingAddress(buildingStub, null);
		info.setBuildingAddress(buildingAddress);
	}

	private Long getQuittanceId(Object[] data) {
		return (Long) data[0];
	}

	private Long getAccountId(Object[] data) {
		return (Long) data[1];
	}

	private int getOrderNumber(Object[] data) {
		return (Integer) data[2];
	}

	private Long getApartmentId(Object[] data) {
		return (Long) data[3];
	}

	private Long getBuildingId(Object[] data) {
		return (Long) data[4];
	}

	@Required
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	@Required
	public void setReportDao(QuittanceReportDao reportDao) {
		this.reportDao = reportDao;
	}
}
