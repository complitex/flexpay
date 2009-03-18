package org.flexpay.tc.service.importexport;

import org.apache.commons.io.IOUtils;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.DistrictService;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.util.config.ApplicationConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestOutputExportResults extends SpringBeanAwareTestCase {

	@Autowired
	private BuildingService buildingService;
	@Autowired
	private DistrictService districtService;
	@Autowired
	private AddressService addressService;

	private String delimeter = ",";

	@Test
	public void testGenerateTariffs() throws Exception {
		//  1) Адрес дома (улица, номер дома и так далее) 2) название тарифа 3) значение тарифа 4) код тарифа  5) tariff export code
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd");
		Date calcDate = df.parse("2009_01_30");

		String hql = "select distinct tr " +
					 "from TariffCalculationResult tr " +
					 "	inner join fetch tr.tariff t " +
					 "	left join fetch t.translations ttr " +
					 "	inner join fetch tr.tariffExportCode c " +
					 "where tr.calculationDate=? " +
					 "order by tr.building.id, c.code ";
		List<TariffCalculationResult> results = (List<TariffCalculationResult>) hibernateTemplate.find(hql, calcDate);
		System.out.println("Found " + results.size() + " results for date: " + calcDate);

		File tmpFile = File.createTempFile("Output_" + df.format(calcDate) + "_", ".csv");
		OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpFile));
		Writer wr = new OutputStreamWriter(os, "cp1251");
		try {
			for (TariffCalculationResult result : results) {
				Building building = result.getBuilding();
				String address = addressService.getBuildingAddress(stub(building), ApplicationConfig.getDefaultLocale());
				building = buildingService.read(stub(building));
				District district = districtService.readFull(stub(building.getDistrict()));
				StringBuilder sb = new StringBuilder()
						.append("\"").append(district.getCurrentName().getDefaultNameTranslation()).append("\"").append(delimeter)
						.append("\"").append(address).append("\"").append(delimeter)
						.append("\"").append(result.getTariff().getDefultTranslation()).append("\"").append(delimeter)
						.append(result.getValue()).append(delimeter)
						.append(result.getTariff().getSubServiceCode()).append(delimeter)
						.append(result.getTariffExportCode().getCode()).append(delimeter)
						.append(result.getTariffExportCode().getI18nName()).append(delimeter)
						.append("\n");
				wr.write(sb.toString());
			}
		} finally {
			IOUtils.closeQuietly(wr);
			IOUtils.closeQuietly(os);
		}

		System.out.println("Results printed to file: " + tmpFile.getAbsolutePath());
	}
}
