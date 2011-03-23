package org.flexpay.eirc.process.quittance.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import org.apache.commons.beanutils.PropertyUtils;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.process.ProcessLogger;
import org.flexpay.eirc.persistence.ConsumerInfo;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.process.QuittanceNumberService;
import org.flexpay.eirc.process.quittance.report.util.QuittanceInfoGenerator;
import org.flexpay.eirc.reports.quittance.QuittancePrintInfo;
import org.flexpay.eirc.reports.quittance.QuittancePrintInfoData;
import org.flexpay.eirc.reports.quittance.QuittancesPrintStats;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.orgs.persistence.ServiceOrganization;
import org.flexpay.orgs.service.ServiceOrganizationService;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.ServiceTypeService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;

public class JRQuittanceDataSource implements JRRewindableDataSource {

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Field mapping that produces the current bean.
	 * <p/>
	 * If the field name/description matches this constant (the case is important), the data source will return the current
	 * bean as the field value.
	 */
	public static final String CURRENT_BEAN_MAPPING = "_THIS";

	private SPService spService;
	private QuittanceService quittanceService;
	private QuittanceNumberService quittanceNumberService;
	private ServiceTypeService serviceTypeService;
	private ServiceOrganizationService serviceOrganizationService;

	private List<QuittancePrintInfo> data = null;
	private Iterator<QuittancePrintInfo> iterator = null;
	private QuittancePrintInfo currentInfo = null;
	private long processCounter = 0;

	public void setPrintData(QuittancePrintInfoData data, int nBatches) throws Exception {

		log = ProcessLogger.getLogger(getClass());

		log.info("Building batches");

		if (nBatches > 1) {
			data.setInfos(buildBatches(data.getInfos(), data.getStats(), nBatches));
		}

		this.data = data.getInfos();
		iterator = this.data.iterator();

		log.info("OK. Ready to print them");
	}

	private QuittancePrintInfo prepareInfo(@NotNull QuittancePrintInfo stub) throws JRException {

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
			QuittancePrintInfo info = stub.clone();
			QuittanceInfoGenerator.buildInfo(q, info);
			initHabitants(q, info);
			initPersonFIO(q, info);
			initServiceOrganization(q, info);
			initDates(q, info);
			initQuittanceNumber(q, info);

			if (info.getOutgoingBalance().compareTo(BigDecimal.ZERO) <= 0) {
				log.warn("Quittance #{} not positive balance.", q.getId());
			}

			return info;
		} catch (Exception e) {
			throw new JRException("Failed preparing quittance info #" + quittanceStub.getId(), e);
		}
	}

	private List<QuittancePrintInfo> buildBatches(List<QuittancePrintInfo> infos, QuittancesPrintStats stats, int nBatches) {

		Map<String, Integer> addrStats = stats.getStats();
		int totalQuittances = stats.getCount() + addrStats.size();
		int nPages = totalQuittances % nBatches == 0 ?
					 totalQuittances / nBatches : (totalQuittances / nBatches + 1);

		QuittancePrintInfo[] result = new QuittancePrintInfo[nPages * nBatches];
		String previousAddress = "";
		int n = 0;
		for (QuittancePrintInfo info : infos) {
			if (!previousAddress.equals(info.getBuildingAddress())) {
				QuittancePrintInfo addressStub = new QuittancePrintInfo();
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
	 * Go through infos and set their batch address, only marker address infos has already set up batch address
	 *
	 * @param infos QuittanceInfo array to init
	 */
	private void setupBatchAddresses(QuittancePrintInfo[] infos) {
		String prevAddr = "";
		for (int n = 0; n < infos.length; ++n) {
			QuittancePrintInfo info = infos[n];
			if (info == null) {
				info = new QuittancePrintInfo();
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

	private void initQuittanceNumber(Quittance q, QuittancePrintInfo info) {

		String quittanceNumber = quittanceNumberService.getNumber(q);
		info.setQuittanceNumber(quittanceNumber);
	}

	private void initDates(Quittance q, QuittancePrintInfo info) {

		info.setPeriodBeginDate(q.getDateFrom());
		info.setPeriodEndDate(q.getDateTill());
		info.setOperationDate(q.getCreationDate());
	}

	private void initServiceOrganization(Quittance q, QuittancePrintInfo info) throws Exception {

		ServedBuilding building = (ServedBuilding) q.getEircAccount().getApartment().getBuilding();
		assert building != null;
		ServiceOrganization org = serviceOrganizationService.read(building.getServiceOrganizationStub());

		assert org != null;
		info.setServiceOrganizationName(org.getName());

		// kvartplata
		ServiceType defaultService = serviceTypeService.getServiceType(1);
		for (QuittanceDetails details : q.getQuittanceDetails()) {
			Service srv = spService.readFull(details.getConsumer().getServiceStub());
			assert srv != null;
			if (srv.getServiceType().equals(defaultService)) {
				String accountNumber = details.getConsumer().getExternalAccountNumber();
				info.setServiceOrganizationAccount(accountNumber);
			}
		}
	}

	private void initPersonFIO(Quittance q, QuittancePrintInfo info) {

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
			qd.getConsumer().setService(spService.readFull(serviceStub));
		}
	}

	private void initHabitants(Quittance q, QuittancePrintInfo info) {

		Apartment apartment = q.getEircAccount().getApartment();
		info.setHabitantNumber(apartment.getPersonRegistrations().size());
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
				if (processCounter % 100 == 0) {
					log.info("Prepared info #{}", processCounter);
				}
			}

//			if (processCounter >= 500) {
//				return false;
//			}
		}

		return hasNext;
	}

	/**
	 * Gets the field value for the current position.
	 *
	 * @return an object containing the field value. The object type must be the field object type.
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
			catch (IllegalAccessException e) {
				throw new JRException("Error retrieving field value from bean : " + propertyName, e);
			}
			catch (InvocationTargetException e) {
				throw new JRException("Error retrieving field value from bean : " + propertyName, e);
			}
			catch (NoSuchMethodException e) {
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

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

	@Required
	public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
		this.serviceOrganizationService = serviceOrganizationService;
	}

	@Required
	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}

	@Required
	public void setQuittanceService(QuittanceService quittanceService) {
		this.quittanceService = quittanceService;
	}

	@Required
	public void setQuittanceNumberService(QuittanceNumberService quittanceNumberService) {
		this.quittanceNumberService = quittanceNumberService;
	}

}
