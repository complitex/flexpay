package org.flexpay.eirc.process.quittance.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.service.AddressService;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.Luhn;
import org.flexpay.eirc.persistence.*;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.process.quittance.report.util.QuittanceInfoGenerator;
import org.flexpay.eirc.service.SPService;
import org.flexpay.eirc.service.ServiceOrganisationService;
import org.flexpay.eirc.service.ServiceTypeService;
import org.flexpay.eirc.service.QuittanceService;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;
import java.math.BigDecimal;

public class JRQuittanceDataSource implements JRRewindableDataSource {

	/**
	 * Field mapping that produces the current bean.
	 * <p/>
	 * If the field name/description matches this constant (the case is important), the data
	 * source will return the current bean as the field value.
	 */
	public static final String CURRENT_BEAN_MAPPING = "_THIS";

	private AddressService addressService;
	private SPService spService;
	private QuittanceService quittanceService;
	private ServiceTypeService serviceTypeService;
	private ServiceOrganisationService serviceOrganisationService;

	private Collection<QuittanceInfo> data = null;
	private Iterator<QuittanceInfo> iterator = null;
	private QuittanceInfo currentInfo = null;
	private long processCounter = 0;
	private Logger log = Logger.getLogger(getClass());

	public void setQuittances(List<Quittance> quittances, int nBatches) throws Exception {

		log = ProcessLogger.getLogger(getClass());
		log.info("Starting quittance generation");
		List<QuittanceInfo> infos = CollectionUtils.list();

		@NotNull Long accountId = -1L;
		int orderNumber = -1;

		long count = 0;
		QuittancesStats stats = new QuittancesStats();
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
			QuittanceInfo info = new QuittanceInfo();
			info.setQuittanceStub(stub(q));
			initAddress(q, info);
			infos.add(info);
			stats.addAddress(info.getBuildingAddress());

//			infos.add(info.clone());
//			stats.addAddress(info.getBuildingAddress());

//			infos.add(info);
//			stats.addAddress(info.getBuildingAddress());

//			QuittanceInfo stub = info.clone();
//			stub.setApartmentAddress("Test address");
//			stub.setBuildingAddress("Test address");
//			infos.add(stub);
//			stats.addAddress(stub.getBuildingAddress());
//			infos.add(stub.clone());
//			stats.addAddress(stub.getBuildingAddress());

			++count;
			if (log.isInfoEnabled() && count % 100 == 0) {
				log.info("Generated " + count + " quittance infos");
			}

//			if (count >= 122) {
//				break;
//			}
		}

		if (log.isInfoEnabled()) {
			log.info("Total " + count + " quittances.");
			log.info("Building batches");
		}

		if (nBatches > 1) {
			infos = buildBatches(infos, stats, nBatches);
		}

		data = infos;
		iterator = data.iterator();

		log.info("OK. Ready to print them");
	}

	private QuittanceInfo prepareInfo(@NotNull QuittanceInfo stub) throws JRException {

		Stub<Quittance> quittanceStub = stub.getQuittanceStub();
		if (quittanceStub == null) {
			return stub;
		}

		Quittance q = quittanceService.readFull(quittanceStub);
		if (q == null) {
			throw new JRException("Expected quittance, but not found: " + quittanceStub);
		}
		initLazyProperties(q);

		try {
			QuittanceInfo info = stub.clone();
			QuittanceInfoGenerator.buildInfo(q, info);
			initHabitants(q, info);
			initPersonFIO(q, info);
			initServiceOrganisation(q, info);
			initDates(q, info);
			initQuittanceNumber(q, info);

			if (info.getOutgoingBalance().compareTo(BigDecimal.ZERO) <= 0) {
				log.warn("Quittance #" + q.getId() + " not positive balance.");
			}

			return info;
		} catch (Exception e) {
			throw new JRException("Failed preparing quittance info #" + quittanceStub.getId(), e);
		}
	}

	private List<QuittanceInfo> buildBatches(List<QuittanceInfo> infos, QuittancesStats stats, int nBatches) {

		Map<String, Integer> addrStats = stats.getStats();
		int totalQuittances = stats.getCount() + addrStats.size();
		int nPages = totalQuittances % nBatches == 0 ?
					 totalQuittances / nBatches : (totalQuittances / nBatches + 1);

		QuittanceInfo[] result = new QuittanceInfo[nPages * nBatches];
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
		for (int n = 0; n < infos.length; ++n) {
			QuittanceInfo info = infos[n];
			if (info == null) {
				info = new QuittanceInfo();
				info.setEmptyInfo(true);
				infos[n] = info;
			} else if (info.getBatchBuildingAddress() != null) {
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
				.append(new SimpleDateFormat("MMyyyy").format(q.getDateTill()))
				.append(String.format("%02d", q.getOrderNumber()));
		String controlDigit = Luhn.controlDigit(digits.toString());

		String quittanceNumber = new StringBuilder()
				.append(q.getEircAccount().getAccountNumber())
				.append("-").append(new SimpleDateFormat("MM/yyyy").format(q.getDateTill()))
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

	private void initHabitants(Quittance q, QuittanceInfo info) {

		Apartment apartment = q.getEircAccount().getApartment();
		info.setHabitantNumber(apartment.getPersonRegistrations().size());
	}

	// todo setup summs
//	private void initTotals(QuittanceInfo info) {
//
//	}

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
	 * Tries to position the cursor on the next element in the data source.
	 *
	 * @return true if there is a next record, false otherwise
	 * @throws net.sf.jasperreports.engine.JRException
	 *          if any error occurs while trying to move to the next element
	 */
	public boolean next() throws JRException {

		boolean hasNext = false;

		if (iterator != null) {
			hasNext = iterator.hasNext();

			if (hasNext) {
				currentInfo = prepareInfo(iterator.next());

				++processCounter;
				if (log.isInfoEnabled() && processCounter % 100 == 0) {
					log.info("Prepared info #" + processCounter);
				}
			}
		}

		return hasNext;
	}

	/**
	 * Gets the field value for the current position.
	 *
	 * @return an object containing the field value. The object type must be the field object
	 *         type.
	 */
	public Object getFieldValue(JRField jrField) throws JRException {
		return getBeanProperty(currentInfo, jrField.getName());
	}

	protected static Object getBeanProperty(Object bean, String propertyName) throws JRException {
		Object value = null;

		if (isCurrentBeanMapping(propertyName)) {
			value = bean;
		} else if (bean != null) {
			try {
				value = PropertyUtils.getProperty(bean, propertyName);
			}
			catch (java.lang.IllegalAccessException e) {
				throw new JRException("Error retrieving field value from bean : " + propertyName, e);
			}
			catch (java.lang.reflect.InvocationTargetException e) {
				throw new JRException("Error retrieving field value from bean : " + propertyName, e);
			}
			catch (java.lang.NoSuchMethodException e) {
				throw new JRException("Error retrieving field value from bean : " + propertyName, e);
			}
			catch (IllegalArgumentException e) {
				//FIXME replace with NestedNullException when upgrading to BeanUtils 1.7
				if (!e.getMessage().startsWith("Null property value for ")) {
					throw e;
				}
			}
		}

		return value;
	}

	protected static boolean isCurrentBeanMapping(String propertyName) {
		return CURRENT_BEAN_MAPPING.equals(propertyName);
	}

	/**
	 * Moves back to the first element in the data source.
	 */
	public void moveFirst() throws JRException {
		if (data != null) {
			iterator = data.iterator();
		}
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

	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}
}
