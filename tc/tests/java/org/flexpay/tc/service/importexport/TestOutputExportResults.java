package org.flexpay.tc.service.importexport;

import org.apache.commons.io.IOUtils;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.tc.persistence.TariffExportCode;
import org.flexpay.tc.persistence.TariffExportLogRecord;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.util.config.ApplicationConfig;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestOutputExportResults extends SpringBeanAwareTestCase {

	private String delimeter = ",";

	private Long dataSourceDescriptionId = 1L;

	private static final String hql = "select distinct logrecord " +
									  "from TariffExportLogRecord logrecord" +
									  " left join fetch logrecord.tariff tariff " +
									  " left join fetch tariff.translations ttr " +
									  "	left join fetch logrecord.tariffExportCode exportCode " +
									  "	left join fetch logrecord.building building " +
									  " where logrecord.tariffBeginDate=? and building.id=? " +
									  " and exportCode.code <> " + TariffExportCode.EXPORTED +
									  " order by building.id, exportCode.code";

	private static final String hqlGetResult = "select distinct tcresult " +
											   "from TariffCalculationResult tcresult " +
											   "where tcresult.lastTariffExportLogRecord.id=?";

	// required services
	@Autowired
	private BuildingService buildingService;

	@Autowired
	private DistrictService districtService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private TariffCalculationResultService tariffCalculationResultService;

	@Autowired
	@Qualifier ("correctionsServiceAb")
	private CorrectionsService correctionsService;

	@Autowired
	@Qualifier ("typeRegistryAb")
	private ClassToTypeRegistry classToTypeRegistry;

	@Test
	public void testGenerateTariffs() throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd");
		Date tariffBeginDate = df.parse("2009_01_01");
		Date calcDate = df.parse("2009_01_30");

		File tmpFile = null;
		OutputStream os = null;
		Writer wr = null;
		try {
			// creating temporary file and writer
			tmpFile = File.createTempFile("Output_" + df.format(tariffBeginDate) + "_", ".csv");
			os = new BufferedOutputStream(new FileOutputStream(tmpFile));
			wr = new OutputStreamWriter(os, "cp1251");

			// loading message source
			ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
			ms.setDefaultEncoding("UTF-8");
			ms.setBasename("org/flexpay/tc/i18n/messages");

			List<Long> buildingIds = tariffCalculationResultService.getAddressIds(calcDate);

			for (Long buildingId : buildingIds) {
				List<TariffExportLogRecord> records = (List<TariffExportLogRecord>) hibernateTemplate.find(hql, new Object[]{tariffBeginDate, buildingId});
				log.info("Found {} log records for date: {} and buiding #{}", new Object[]{records.size(), tariffBeginDate, buildingId});

				List<TariffExportLogRecord> filteredRecords = filterRecordsByExportDate(records);
				log.info("{} records filtered", records.size() - filteredRecords.size());

				for (TariffExportLogRecord record : filteredRecords) {
					if (record.getTariffExportCode().getCode() != TariffExportCode.EXPORTED) {

						List<TariffCalculationResult> results = (List<TariffCalculationResult>) hibernateTemplate.find(hqlGetResult, new Object[] { record.getId() });
						if (results.size() > 1) {
							log.error("Unexpected number of results for log record: {}", results.size());
							throw new RuntimeException("Unexpected number of results for log record: " + results.size());
						}

						write(ms, wr, record, results.get(0));
					}
				}
			}
		} finally {
			IOUtils.closeQuietly(wr);
			IOUtils.closeQuietly(os);
		}

		System.out.println("Results printed to file: " + tmpFile.getAbsolutePath());
	}

	private List<TariffExportLogRecord> filterRecordsByExportDate(List<TariffExportLogRecord> records) {

		List<TariffExportLogRecord> filtered = CollectionUtils.list();

		for (TariffExportLogRecord record : records) {
			TariffExportLogRecord latestExportDateResult = getLatestExportDateRecord(records,
					record.getTariffBeginDate(), record.getBuilding().getId(), record.getTariff().getId());

			if (!filtered.contains(latestExportDateResult)) {
				filtered.add(latestExportDateResult);
			}
		}

		return filtered;
	}

	private TariffExportLogRecord getLatestExportDateRecord(List<TariffExportLogRecord> records, Date tariffBeginDate, Long buildingId, Long tariffId) {

		TariffExportLogRecord latestExportDateRecord = null;
		Date latestExportDate = null;

		for (TariffExportLogRecord record : records) {
			Date logRecordExportDate = record.getExportdate();

			// if we have export result with the same tariff begin date and building id
			if (tariffBeginDate.equals(record.getTariffBeginDate())
				&& buildingId.equals(record.getBuilding().getId())
				&& tariffId.equals(record.getTariff().getId())) {
				if (latestExportDate == null) {
					latestExportDateRecord = record;
					latestExportDate = logRecordExportDate;
				} else {
					if (latestExportDate.before(logRecordExportDate)) {
						latestExportDateRecord = record;
						latestExportDate = logRecordExportDate;
					}
				}
			}
		}

		return latestExportDateRecord;
	}

	private void write(ReloadableResourceBundleMessageSource ms, Writer wr, TariffExportLogRecord record, TariffCalculationResult result) throws Exception {
		// формат записи в файл
		// 1) Адрес дома (улица, номер дома и так далее) 2) код дома в ЦН 3) название тарифа 4) значение тарифа 5) код тарифа 6) tariff export code

		Building building = record.getBuilding();
		String address = addressService.getBuildingAddress(stub(building), ApplicationConfig.getDefaultLocale());
		building = buildingService.read(stub(building));
		District district = districtService.readFull(stub(building.getDistrict()));
		String i18nName = record.getTariffExportCode().getI18nName();
		Object[] params = {};

		String externalId = getExternalId(stub(building));
		externalId = (externalId == null) ? "" : externalId;
		String cnId = externalId.equals("1") ? "" : externalId; // exclude cnId 1

		String value = (result != null) ? result.getValue().toString() : "0";

		StringBuilder sb = new StringBuilder()
				.append("\"").append(district.getCurrentName().getDefaultNameTranslation()).append("\"").append(delimeter)
				.append("\"").append(address).append("\"").append(delimeter)
				.append(record.getBuilding().getId()).append(delimeter)
				.append(cnId).append(delimeter)
				.append("\"").append(record.getTariff().getDefultTranslation()).append("\"").append(delimeter)
				.append(value).append(delimeter)
				.append(record.getTariff().getSubServiceCode()).append(delimeter)
				.append(record.getTariffExportCode().getCode()).append(delimeter)
				.append("\"").append(ms.getMessage(i18nName, params, ApplicationConfig.getDefaultLocale())).append("\"")
				.append("\n");

		wr.write(sb.toString());
	}

	private String getExternalId(@NotNull Stub<Building> buildingStub) throws FlexPayException {

		Stub<DataSourceDescription> dataSourceDescriptionStub = new Stub<DataSourceDescription>(dataSourceDescriptionId);
		List<BuildingAddress> buildingAddressList = buildingService.getBuildingBuildings(buildingStub);

		for (BuildingAddress buildingAddress : buildingAddressList) {
			String externalId = correctionsService.getExternalId(buildingAddress.getId(), classToTypeRegistry.getType(BuildingAddress.class), dataSourceDescriptionStub);

			if (externalId != null) {
				return externalId;
			}
		}

		return null;
	}
}
