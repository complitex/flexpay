package org.flexpay.eirc.service.importexport;

import org.apache.commons.lang.time.StopWatch;
import org.flexpay.ab.persistence.*;
import org.flexpay.ab.service.importexport.ImportServiceImpl;
import org.flexpay.common.persistence.*;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.TransitionNotAllowed;
import org.flexpay.common.service.importexport.RawDataSource;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.EircRegistryRecordProperties;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.Service;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;

@Transactional (readOnly = true)
public class EircImportServiceTxImpl extends ImportServiceImpl implements EircImportServiceTx {

	private ConsumerService consumerService;
	private ImportUtil importUtil;
	private RegistryRecordWorkflowManager recordWorkflowManager;

	private StopWatch readRawDataBatchWatch = new StopWatch();
	private StopWatch setPersonWatch = new StopWatch();
	private StopWatch findServiceWatch = new StopWatch();
	private StopWatch findApartmentWatch = new StopWatch();
	private StopWatch findConsumerWatch = new StopWatch();
	private StopWatch stackWatch = new StopWatch();

	@Override
	public boolean processBatch(long[] counters, boolean inited,
								Stub<DataSourceDescription> sd, RawDataSource<RawConsumerData> dataSource,
								Map<String, List<Street>> nameObjsMap) {

		log.debug("Fetching for next batch");

		// get next butch
		List<RawConsumerData> datum = readRawDataBatch(inited, dataSource);
		if (datum.isEmpty()) {
			flushStack();
			printWatch();
			return false;
		}

		log.debug("Fetched next batch");

		for (RawConsumerData data : datum) {
			++counters[0];

			if (data == null) {
				log.info("Empty data read");
				++counters[1];
				continue;
			}

			try {
				log.debug("Starting record processing: {}", data.getRegistryRecord());
				recordWorkflowManager.startProcessing(data.getRegistryRecord());
			} catch (TransitionNotAllowed e) {
				log.info("Skipping record, processing not allowed: {}", data.getExternalSourceId());
				++counters[1];
				continue;
			}

			RegistryRecord record = data.getRegistryRecord();

			EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
			if (props.getConsumer() != null) {
				log.info("Record already has consumer set up");
				++counters[1];
				addToStack(record);
				continue;
			}

			if (props.getApartment() != null &&
				props.getPerson() != null &&
				props.getService() != null) {
				log.info("Record already have apartment, person and service set");
				++counters[1];
				addToStack(record);
				continue;
			}

			try {
				// set service if not found
				EircRegistryProperties regProps = (EircRegistryProperties) record.getRegistry().getProperties();
				Service service = props.getService();
				Stub<ServiceProvider> spStub = regProps.getServiceProviderStub();
				if (service == null) {
					findServiceWatch.resume();
					service = consumerService.findService(spStub, data.getServiceCode());
					findServiceWatch.suspend();
					if (service == null) {
						log.warn("Unknown service code: {}", data.getServiceCode());
						ImportError error = addImportError(sd, data.getExternalSourceId(), Service.class, dataSource);
						error.setErrorId("error.eirc.import.service_not_found");
						setConsumerError(record, error);

						addToStack(record);
						continue;
					}

					props.setService(service);
				}

				// try to find consumer (correction lost or service code came in a different format?)
				findConsumerWatch.resume();
				Consumer consumer = consumerService.findConsumer(regProps.getServiceProviderStub(), data.getAccountNumber(), String.valueOf(service.getId()));
				findConsumerWatch.suspend();

				if (consumer != null) {
					log.info("Found existing consumer: #{}", data.getExternalSourceId());
					// todo do we need to fetch this?
					++counters[1];
					props.setConsumer(consumer);
					addToStack(record);
					continue;
				}

				if (data.isPersonalInfoEmpty()) {
					log.info("Cannot find consumer by short info: {}", data.getShortConsumerId());
					ImportError error = addImportError(sd, data.getExternalSourceId(), Consumer.class, dataSource);
					error.setErrorId("error.eirc.import.consumer_not_found");
					setConsumerError(record, error);
					addToStack(record);
					continue;
				}


				// set apartment
				if (props.getApartment() == null) {
					// Find apartment
					findApartmentWatch.resume();
					Apartment apartment = findApartment(nameObjsMap, sd, data, dataSource, record);
					findApartmentWatch.suspend();
					if (apartment == null) {
						// addImportError(sd, data.getExternalSourceId(), Apartment.class, dataSource);
						addToStack(record);
						continue;
					}

					props.setApartment(apartment);
					log.info("Found apartment: {}", data.getApartmentId());
				}

				// set person
				setPersonWatch.resume();
				if (props.getPerson() == null) {
					Person person = findPerson(sd, data, record, dataSource);
					if (person != null) {
						props.setPerson(person);
						log.info("Found responsible person: {}", data.getPersonFIO());
					} else {
						log.info("No person found");
					}
				}
				setPersonWatch.suspend();

				addToStack(record);
			} catch (Exception e) {
				log.error("Failed getting consumer: " + data.toString(), e);
				throw new RuntimeException(e);
			}
		}

		log.debug("Imported {} records so far", counters[0]);

		return true;
	}

	private List<RawConsumerData> readRawDataBatch(boolean inited, RawDataSource<RawConsumerData> dataSource) {
		log.debug("Reading batch");

		if (!inited) {
			initWatch();

			dataSource.initialize();
			log.debug("Inited");
		}

		readRawDataBatchWatch.resume();

		List<RawConsumerData> result = dataSource.nextPage();
		log.debug("Listing records for update");
		readRawDataBatchWatch.suspend();

		return result;
	}

	@Override
	protected void flushStack() {
		stackWatch.resume();
		super.flushStack();
		stackWatch.suspend();
	}

	private void setConsumerError(RegistryRecord record, ImportError error) throws Exception {
		recordWorkflowManager.setNextErrorStatus(record, error);
	}

	@Nullable
	private Person findPerson(Stub<DataSourceDescription> sd, @NotNull RawConsumerData data, RegistryRecord record,
							  RawDataSource<RawConsumerData> dataSource) throws Exception {

		// first try to find person via set correction
		Stub<Person> personById = correctionsService.findCorrection(
				data.getPersonFIOId(), Person.class, sd);
		if (personById != null) {
			log.info("Found person correction");
			return new Person(personById);
		}

		EircRegistryRecordProperties props = (EircRegistryRecordProperties) record.getProperties();
		ImportError error = addImportError(sd, data.getExternalSourceId(), Person.class, dataSource);
		Person person = importUtil.findPersonByFIO(props.getApartmentStub(),
				data.getFirstName(), data.getMiddleName(), data.getLastName(), error);
		if (person != null) {
			return person;
		}

		if (error.getErrorId() != null) {
//			setConsumerError(data, error);
		}

		return null;
	}

	private Apartment findApartment(Map<String, List<Street>> nameObjsMap,
									Stub<DataSourceDescription> sd, RawConsumerData data,
									RawDataSource<RawConsumerData> dataSource, RegistryRecord record) throws Exception {

		// try to find building and later apartment in it
		Stub<BuildingAddress> buildingsById = correctionsService.findCorrection(
				data.getBuildingId(), BuildingAddress.class, sd);
		if (buildingsById != null) {
			log.info("Found buildingAddress correction: {}", data.getBuildingId());
			BuildingAddress buildingAddress = buildingService.readFullAddress(buildingsById);
			return findApartment(data, buildingAddress, sd, dataSource, record);
		}

		// try to find street by correction
		Stub<Street> streetById = correctionsService.findCorrection(
				data.getStreetId(), Street.class, sd);
		if (streetById != null) {
			log.info("Found street correction: {}", data.getStreetId());
			return findApartment(data, new Street(streetById), sd, dataSource, record);
		}

		Street streetByName = findStreet(nameObjsMap, data, sd, dataSource, record);
		if (streetByName == null) {
			log.warn("Failed getting street for account #{}", data.getExternalSourceId());
			return null;
		}

		DataCorrection corr = correctionsService.getStub(data.getStreetId(), streetByName, sd);
		log.info("Adding street correction: {}", data.getStreetId());
		addToStack(corr);

		return findApartment(data, streetByName, sd, dataSource, record);
	}

	private Street findStreet(Map<String, List<Street>> nameObjsMap,
							  RawConsumerData data,
							  Stub<DataSourceDescription> sd, RawDataSource<RawConsumerData> dataSource, RegistryRecord record) throws Exception {

		Stub<StreetType> stub = findStreetType(data, sd);
		if (stub == null) {
			log.warn("No street type was found: {}, for street {}", data.getAddressStreetType(), data.getAddressStreet());
			ImportError error = addImportError(sd, data.getExternalSourceId(), StreetType.class, dataSource);
			error.setErrorId("error.eirc.import.street_type_not_found");
			setConsumerError(record, error);
			return null;
		}

		List<Street> streets = nameObjsMap.get(data.getAddressStreet().toLowerCase());
		// no candidates
		if (streets == null) {
			log.info("No candidates for street: {}", data.getAddressStreet());
			ImportError error = addImportError(sd, data.getExternalSourceId(), Street.class, dataSource);
			error.setErrorId("error.eirc.import.street_not_found");
			setConsumerError(record, error);
			return null;
		}

		log.debug("Street candidates: {}", streets);

		List<Street> filteredStreets = filterStreetsbyType(streets, stub);
		if (filteredStreets.isEmpty()) {
			log.warn("Cannot find street by type: {}, {}", data.getAddressStreetType(), data.getAddressStreet());
			ImportError error = addImportError(sd, data.getExternalSourceId(), Street.class, dataSource);
			error.setErrorId("error.eirc.import.street_type_invalid");
			setConsumerError(record, error);
			return null;
		}

		if (filteredStreets.size() == 1) {
			log.info("Street filtered by type: {}", data.getAddressStreet());
			return filteredStreets.get(0);
		}

		log.warn("Cannot find street candidate, even by type: {}, {}",
				data.getAddressStreetType(), data.getAddressStreet());
		ImportError error = addImportError(sd, data.getExternalSourceId(), Street.class, dataSource);
		error.setErrorId("error.eirc.import.street_too_many_variants");
		setConsumerError(record, error);
		return null;
	}

	private List<Street> filterStreetsbyType(List<Street> streets, Stub<StreetType> stub) {
		List<Street> filteredStreets = list();
		for (Street street : streets) {
			StreetType current = street.getCurrentType();
			if (current == null) {
				log.warn("No type for street {}", street);
				continue;
			}
			if (stub(current).equals(stub)) {
				filteredStreets.add(street);
			}
		}

		return filteredStreets;
	}

	@Nullable
	private Stub<StreetType> findStreetType(RawConsumerData data, Stub<DataSourceDescription> sd) throws Exception {

		Stub<StreetType> stub = streetTypeService.findTypeByName(data.getAddressStreetType());
		if (stub != null) {
			return stub;
		}

		return correctionsService.findCorrection(data.getAddressStreetType(), StreetType.class, sd);
	}

	private Apartment findApartment(RawConsumerData data, Street street,
									Stub<DataSourceDescription> sd, RawDataSource<RawConsumerData> dataSource,
									RegistryRecord record)
			throws Exception {

		BuildingAddress buildingAddress = buildingService.findAddresses(stub(street), buildingService.attributes(data.getAddressHouse(), data.getAddressBulk()));
		if (buildingAddress == null) {
			log.warn("Failed getting building for consumer, Street({}, {}), Building({}, {}) ",
					new Object[]{street.getId(), data.getAddressStreet(), data.getAddressHouse(), data.getAddressBulk()});
			ImportError error = addImportError(sd, data.getExternalSourceId(), BuildingAddress.class, dataSource);
			error.setErrorId("error.eirc.import.building_not_found");
			setConsumerError(record, error);
			return null;
		}

		DataCorrection corr = correctionsService.getStub(data.getBuildingId(), buildingAddress, sd);
		log.info("Adding buildingAddress correction: {}", data.getBuildingId());
		addToStack(corr);

		return findApartment(data, buildingAddress, sd, dataSource, record);
	}

	private Apartment findApartment(RawConsumerData data, BuildingAddress buildingAddress,
									Stub<DataSourceDescription> sd, RawDataSource<RawConsumerData> dataSource,
									RegistryRecord record)
			throws Exception {
		Building building = buildingAddress.getBuilding();

		Stub<Apartment> stub = apartmentService.findApartmentStub(building, data.getAddressApartment());
		if (stub == null) {
			ImportError error = addImportError(sd, data.getExternalSourceId(), Apartment.class, dataSource);
			error.setErrorId("error.eirc.import.apartment_not_found");
			setConsumerError(record, error);
			return null;
		}

		return new Apartment(stub);
	}

	private void initWatch() {
		initWatch(findApartmentWatch);

		initWatch(setPersonWatch);

		initWatch(findServiceWatch);

		initWatch(readRawDataBatchWatch);

		initWatch(findConsumerWatch);

		initWatch(stackWatch);
	}

	private void initWatch(StopWatch watch) {
		try {
			if (watch.getStartTime() > 0) {
				watch.reset();
			}
		} catch (IllegalStateException ex) {
			// nothing do
		}
		watch.start();
		watch.suspend();
	}

	private void printWatch() {
		readRawDataBatchWatch.stop();
		log.debug("Read raw data batch: {}", readRawDataBatchWatch);

		setPersonWatch.stop();
		log.debug("Set person: {}", setPersonWatch);

		findServiceWatch.stop();
		log.debug("Find service: {}", findServiceWatch);

		findApartmentWatch.stop();
		log.debug("Find apartment: {}", findApartmentWatch);

		findConsumerWatch.stop();
		log.debug("Find consumer: {}", findConsumerWatch);

		stackWatch.stop();
		log.debug("Stack: {}", stackWatch);
	}

	@Required
	public void setConsumerService(ConsumerService consumerService) {
		this.consumerService = consumerService;
	}

	@Required
	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}

	@Required
	public void setImportUtil(ImportUtil importUtil) {
		this.importUtil = importUtil;
	}

}
