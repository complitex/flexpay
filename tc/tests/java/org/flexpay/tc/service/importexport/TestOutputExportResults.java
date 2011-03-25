package org.flexpay.tc.service.importexport;

import org.apache.commons.io.IOUtils;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.persistence.TariffExportCode;
import org.flexpay.tc.persistence.TariffExportLogRecord;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLocale;
import static org.junit.Assert.assertNotNull;

public class TestOutputExportResults extends SpringBeanAwareTestCase {

	private final String delimeter = ",";

	private Long dataSourceDescriptionId = 1L;

	private static final String hql = "select distinct logrecord " +
									  "from TariffExportLogRecord logrecord " +
									  " left join fetch logrecord.tariff tariff " +
									  " left join fetch tariff.translations ttr " +
									  "	left join fetch logrecord.tariffExportCode exportCode " +
									  "	left join fetch logrecord.building building " +
									  " where logrecord.tariffBeginDate=? and building.id=? " +
									  " order by logrecord.exportdate desc";

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
				@SuppressWarnings ({"unchecked"})
				List<TariffExportLogRecord> records = (List<TariffExportLogRecord>) hibernateTemplate.find(
						hql, tariffBeginDate, buildingId);
				log.info("Found {} log records for date: {} and building #{}",
						new Object[]{records.size(), tariffBeginDate, buildingId});

				List<Long> exportedTariffIds = CollectionUtils.list();
				for (TariffExportLogRecord record : records) {
					// filtering records with code EXPORTED
					if (record.getTariffExportCode().getCode() == TariffExportCode.EXPORTED) {
						continue;
					}

					// chacking if we have record with such tariff
					Long tariffId = record.getTariff().getId();
					if (exportedTariffIds.contains(tariffId)) {
						continue;
					}

					@SuppressWarnings ({"unchecked"})
					List<TariffCalculationResult> results = (List<TariffCalculationResult>) hibernateTemplate.find(
							hqlGetResult, new Object[]{record.getId()});
					if (results.size() > 1) {
						log.error("Unexpected number of results for log record: {}", results.size());
						throw new RuntimeException("Unexpected number of results for log record: " + results.size());
					}

					TariffCalculationResult result = null;
					if (results.size() == 1) {
						result = results.get(0);
					}

					write(ms, wr, record, result);

					exportedTariffIds.add(tariffId);
				}
			}
		} finally {
			IOUtils.closeQuietly(wr);
			IOUtils.closeQuietly(os);
		}

		System.out.println("Results printed to file: " + tmpFile.getAbsolutePath());
	}

	private void write(ReloadableResourceBundleMessageSource ms, Writer wr,
					   TariffExportLogRecord record, TariffCalculationResult result) throws Exception {
		// формат записи в файл
		// 1) Адрес дома (улица, номер дома и так далее) 2) код дома в ЦН
		// 3) название тарифа 4) значение тарифа 5) код тарифа 6) tariff export code
		Building building = record.getBuilding();
		String address = addressService.getBuildingAddress(stub(building), getDefaultLocale());
		building = buildingService.readFull(stub(building));
		District district = districtService.readFull(stub(building.getDistrict()));
		String i18nName = record.getTariffExportCode().getI18nName();
		Object[] params = {};

		String externalId = getExternalId(stub(building));
		externalId = (externalId == null) ? "" : externalId;
		String cnId = externalId.equals("1") ? "" : externalId; // exclude cnId 1

		String value = (result != null) ? result.getValue().toString() : "0";

		assertNotNull("District is null", district);
		DistrictName name = district.getCurrentName();
		assertNotNull("Current name is null", name);
		StringBuilder sb = new StringBuilder()
				.append("\"").append(name.getDefaultNameTranslation()).append("\"").append(delimeter)
				.append("\"").append(address).append("\"").append(delimeter)
				.append(record.getBuilding().getId()).append(delimeter)
				.append(cnId).append(delimeter)
				.append("\"").append(record.getTariff().getDefultTranslation()).append("\"").append(delimeter)
				.append(value).append(delimeter)
				.append(record.getTariff().getSubServiceCode()).append(delimeter)
				.append(record.getTariffExportCode().getCode()).append(delimeter)
				.append("\"").append(ms.getMessage(i18nName, params, getDefaultLocale())).append("\"")
				.append("\n");

		wr.write(sb.toString());
	}

	private String getExternalId(@NotNull Stub<Building> buildingStub) throws FlexPayException {

		Stub<DataSourceDescription> dataSourceDescriptionStub = new Stub<DataSourceDescription>(dataSourceDescriptionId);
		List<BuildingAddress> buildingAddressList = buildingService.findAddresesByBuilding(buildingStub);

		for (BuildingAddress buildingAddress : buildingAddressList) {
			String externalId = correctionsService.getExternalId(buildingAddress.getId(),
					classToTypeRegistry.getType(BuildingAddress.class), dataSourceDescriptionStub);

			if (externalId != null) {
				return externalId;
			}
		}

		return null;
	}
}
