package org.flexpay.eirc.process.quittance.report;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.AddressService;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.Luhn;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.process.quittance.report.util.QuittanceInfoGenerator;
import org.flexpay.eirc.service.SPService;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.flexpay.eirc.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

public class JRQuittanceDataSource implements JRDataSource {

	private AddressService addressService;
	private SPService spService;
	private ServiceTypeService serviceTypeService;
	private ServiceOrganisationService serviceOrganisationService;
	private JRBeanCollectionDataSource jrBeanCollectionDataSource;

	public void setQuittances(List<Quittance> quittances) throws Exception {

		List<QuittanceInfo> infos = CollectionUtils.list();

		@NotNull Long accountId = -1L;
		int orderNumber = -1;

		QuittancesStats stats = new QuittancesStats();
		for (Quittance q : quittances) {

			initLazyProperties(q);

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
			QuittanceInfo info = QuittanceInfoGenerator.buildInfo(q);
			initAddress(q, info);
			initPersonFIO(q, info);
			initServiceOrganisation(q, info);
			initDates(q, info);
			initQuittanceNumber(q, info);
			infos.add(info);
			stats.addAddress(info.getBuildingAddress());
		}

		infos = buildBatches(infos, stats, 2);

		jrBeanCollectionDataSource = new JRBeanCollectionDataSource(infos);
	}

	private List<QuittanceInfo> buildBatches(List<QuittanceInfo> infos, QuittancesStats stats, int nBatches) {

		Map<String, Integer> addrStats = stats.getStats();
		int totalQuittances = stats.getCount() + addrStats.size();
		int nPages = totalQuittances % nBatches == 0 ?
					 totalQuittances / nBatches : totalQuittances / nBatches + 1;

		QuittanceInfo[] result = new QuittanceInfo[totalQuittances];
		String previousAddress = "";
		int n = 0;
		for (QuittanceInfo info : infos) {
			if (!previousAddress.equals(info.getBuildingAddress())) {
				QuittanceInfo addressStub = new QuittanceInfo();
				// init batch address for later filling
				addressStub.setBatchBuildingAddress(info.getBuildingAddress());
				addressStub.setAddressStub(true);
				int pos = getPosition(n, nPages, nBatches);
				if (result[pos] != null) {
					throw new IllegalStateException("Invalid position calc algorithm");
				}
				result[pos] = addressStub;
				previousAddress = info.getBuildingAddress();
				++n;
			}

			int pos = getPosition(n, nPages, nBatches);
			if (result[pos] != null) {
				throw new IllegalStateException("Invalid position calc algorithm");
			}
			result[pos] = info;
			++n;
		}

		setupBatchAddresses(result);

		return Arrays.asList(result);
	}

	/**
	 * Go through infos and set their batch address, only marker address infos has already
	 * set up batch address
	 *
	 * @param infos QuittanceInfo array to init
	 */
	private void setupBatchAddresses(QuittanceInfo[] infos) {
		String prevAddr = "";
		for (QuittanceInfo info : infos) {
			if (info.getBatchBuildingAddress() != null) {
				prevAddr = info.getBatchBuildingAddress();
			}
			info.setBatchBuildingAddress(prevAddr);
		}
	}

	/**
	 * Find position of n-th element for the batches
	 *
	 * @param n		 N-th element to find position for
	 * @param nPages	Total number of pages
	 * @param batchSize Batch size (i.e. number of quittances per page)
	 * @return element position
	 */
	private int getPosition(int n, int nPages, int batchSize) {
		int nPage = n % nPages;
		int pagePos = n / nPages;
		return nPage * batchSize + pagePos;
	}

	private void initQuittanceNumber(Quittance q, QuittanceInfo info) {

		Calendar cal = new GregorianCalendar();
		cal.setTime(q.getDateTill());

		StringBuilder digits = new StringBuilder()
				.append(q.getEircAccount().getAccountNumber())
				.append(new SimpleDateFormat("mmyyyy").format(q.getDateTill()))
				.append(String.format("%02d", q.getOrderNumber()));
		String controlDigit = Luhn.controlDigit(digits.toString());

		String quittanceNumber = new StringBuilder()
				.append(q.getEircAccount().getAccountNumber())
				.append("-").append(new SimpleDateFormat("mm/yyyy").format(q.getDateTill()))
				.append("-").append(String.format("%02d", q.getOrderNumber()))
				.append(controlDigit)
				.toString();

		info.setQuittanceNumber(quittanceNumber);
	}

	private void initDates(Quittance q, QuittanceInfo info) {

		info.setPeriodBeginDate(q.getDateFrom());
		info.setPeriodEndDate(q.getDateTill());
		info.setOperationDate(q.getCreationDate());
	}

	private void initServiceOrganisation(Quittance q, QuittanceInfo info) throws Exception {
		ServedBuilding building = (ServedBuilding) q.getEircAccount().getApartment().getBuilding();
		ServiceOrganisation org = serviceOrganisationService.read(building.getServiceOrganisationStub());

		info.setServiceOrganisationName(org.getName());

		// kvarplata
		ServiceType defaultService = serviceTypeService.getServiceType(1);
		for (QuittanceDetails details : q.getQuittanceDetails()) {
			if (details.getConsumer().getService().getServiceType().equals(defaultService)) {
				String accountNumber = details.getConsumer().getExternalAccountNumber();
				info.setServiceOrganisationAccount(accountNumber);
			}
		}
	}

	private void initAddress(Quittance q, QuittanceInfo info) throws Exception {

		Stub<Apartment> stub = q.getEircAccount().getApartmentStub();
		String address = addressService.getAddress(stub, null);
		info.setApartmentAddress(address);

		Stub<Building> buildingStub = q.getEircAccount().getApartment().getBuildingStub();
		String buildingAddress = addressService.getBuildingAddress(buildingStub, null);
		info.setBuildingAddress(buildingAddress);
	}

	private void initPersonFIO(Quittance q, QuittanceInfo info) {

		Person person = q.getEircAccount().getPerson();
		if (person != null) {
			info.setPersonFIO(person.getFIO());
		} else {
			ConsumerInfo consumerInfo = q.getEircAccount().getConsumerInfo();
			info.setPersonFIO(consumerInfo.getFIO());
		}
	}

	private void initLazyProperties(Quittance q) {
		for (QuittanceDetails qd : q.getQuittanceDetails()) {
			Stub<Service> serviceStub = stub(qd.getConsumer().getService());
			qd.getConsumer().setService(spService.read(serviceStub));
		}
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


	public boolean next() throws JRException {
		// delegate all work to native implementation
		return jrBeanCollectionDataSource != null && jrBeanCollectionDataSource.next();
	}

	public Object getFieldValue(JRField jrField) throws JRException {
		// delegate all work to native implementation
		return jrBeanCollectionDataSource.getFieldValue(jrField);
	}

	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	public void setServiceOrganisationService(ServiceOrganisationService serviceOrganisationService) {
		this.serviceOrganisationService = serviceOrganisationService;
	}

	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}
}
