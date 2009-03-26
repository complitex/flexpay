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
import org.flexpay.common.util.DateUtil;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.persistence.TariffExportLogRecord;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.flexpay.tc.service.TariffService;
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

	private String[] subserviceCodes = {"010", "020", "030", "040", "050", "060", "070", "080", "090", "100", "110", "120", "130", "140", "150", "160"};

	private Long dataSourceDescriptionId = 1L;

	private static final String hql = "select distinct result " +
									  "from TariffCalculationResult result " +
									  "	inner join fetch result.tariff tariff " +
									  "	left join fetch tariff.translations ttr " +
									  "	inner join fetch result.lastTariffExportLogRecord logrecord " +
									  "	left join fetch logrecord.tariffExportCode exportCode " +
									  "	left join fetch result.building building " +
									  "where result.calculationDate=? and building.id=? " +
									  "order by result.building.id, exportCode.code ";

	// required services
	@Autowired
	private BuildingService buildingService;

	@Autowired
	private DistrictService districtService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private TariffService tariffService;

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
		Date tariffBeginDate = df.parse("2009_01_30");

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

			List<Long> buildingIds = tariffCalculationResultService.getAddressIds(tariffBeginDate);

			for (Long buildingId : buildingIds) {
				List<TariffCalculationResult> results = (List<TariffCalculationResult>) hibernateTemplate.find(hql, new Object[]{tariffBeginDate, buildingId});
				System.out.println("Found " + results.size() + " results for date: " + tariffBeginDate);

				List<TariffCalculationResult> filteredResults = filterResultsByExportDate(results);
				log.info("Several results with same building and tariff begin date found, {} filtered", results.size() - filteredResults.size());

				for (TariffCalculationResult result : filteredResults) {
					writeCalculationResult(ms, wr, result);
				}
			}
		} finally {
			IOUtils.closeQuietly(wr);
			IOUtils.closeQuietly(os);
		}

		System.out.println("Results printed to file: " + tmpFile.getAbsolutePath());
	}

	private List<TariffCalculationResult> filterResultsByExportDate(List<TariffCalculationResult> results) {

		List<TariffCalculationResult> filtered = CollectionUtils.list();

		for (TariffCalculationResult result : results) {
			TariffExportLogRecord logRecord = result.getLastTariffExportLogRecord();
			TariffCalculationResult latestExportDateResult = getLatestExportDateResult(results, logRecord.getTariffBeginDate(), logRecord.getBuilding().getId());

			if (!filtered.contains(latestExportDateResult)) {
				filtered.add(latestExportDateResult);
			}
		}

		return filtered;
	}

	private TariffCalculationResult getLatestExportDateResult(List<TariffCalculationResult> results, Date tariffBeginDate, Long buildingId) {

		TariffCalculationResult latestExportDateResult = null;
		Date latestExportDate = null;

		for (TariffCalculationResult result : results) {
			TariffExportLogRecord logRecord = result.getLastTariffExportLogRecord();
			Date logRecordExportDate = logRecord.getExportdate();

			// if we have export result with the same tariff begin date and building id
			if (tariffBeginDate.equals(logRecord.getTariffBeginDate()) && buildingId.equals(logRecord.getBuilding().getId())) {
				if (latestExportDate == null) {
					latestExportDateResult = result;
					latestExportDate = logRecordExportDate;
				} else {
					if (latestExportDate.before(logRecordExportDate)) {
						latestExportDateResult = result;
						latestExportDate = logRecordExportDate;
					}
				}
			}
		}

		return latestExportDateResult;
	}

	private void writeCalculationResult(ReloadableResourceBundleMessageSource ms, Writer wr, TariffCalculationResult result) throws Exception {
		// формат записи в файл
		// 1) Адрес дома (улица, номер дома и так далее) 2) код дома в ЦН 3) название тарифа 4) значение тарифа 5) код тарифа 6) tariff export code

		Building building = result.getBuilding();
		String address = addressService.getBuildingAddress(stub(building), ApplicationConfig.getDefaultLocale());
		building = buildingService.read(stub(building));
		District district = districtService.readFull(stub(building.getDistrict()));
		String i18nName = result.getLastTariffExportLogRecord().getTariffExportCode().getI18nName();
		Object[] params = {};

		String externalId = getExternalId(stub(building));
		externalId = (externalId == null) ? "" : externalId;
		String cnId = externalId.equals("1") ? "" : externalId; // exclude cnId 1

		StringBuilder sb = new StringBuilder()
				.append("\"").append(district.getCurrentName().getDefaultNameTranslation()).append("\"").append(delimeter)
				.append("\"").append(address).append("\"").append(delimeter)
				.append(cnId).append(delimeter)
				.append("\"").append(result.getTariff().getDefultTranslation()).append("\"").append(delimeter)
				.append(result.getValue()).append(delimeter)
				.append(result.getTariff().getSubServiceCode()).append(delimeter)
				.append(result.getLastTariffExportLogRecord().getTariffExportCode().getCode()).append(delimeter)
				.append("\"").append(ms.getMessage(i18nName, params, ApplicationConfig.getDefaultLocale())).append("\"")
				.append("\n");

		wr.write(sb.toString());
	}

	private TariffCalculationResult getTariffCalcResultBySubserviceCode(List<TariffCalculationResult> results, String subserviceCode) {
		for (TariffCalculationResult tcr : results) {
			if (subserviceCode.equals(tcr.getTariff().getSubServiceCode())) {
				results.remove(tcr);
				return tcr;
			}
		}
		return null;
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
