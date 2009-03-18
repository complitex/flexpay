package org.flexpay.tc.service.importexport;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.tc.service.TariffService;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.util.config.ApplicationConfig;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.persistence.Building;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.io.IOUtils;

import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;

public class TestOutputExportResults extends SpringBeanAwareTestCase {

	@Autowired
	private TariffService tariffService;
	@Autowired
	private BuildingService buildingService;
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
					 "where tr.calculationDate=?";
		List<TariffCalculationResult> results = (List<TariffCalculationResult>) hibernateTemplate.find(hql, calcDate);
		System.out.println("Found " + results.size() + " results for date: " + calcDate);

		File tmpFile = File.createTempFile("Output_" + df.format(calcDate) + "_", ".csv");
		OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpFile));
		Writer wr = new OutputStreamWriter(os, "cp1251");
		try {
			for (TariffCalculationResult result : results) {
				Building building = result.getBuilding();
				String address = addressService.getBuildingAddress(stub(building), ApplicationConfig.getDefaultLocale());
				StringBuilder sb = new StringBuilder()
					.append("\"").append(address).append("\"").append(delimeter)
					.append("\"").append(result.getTariff().getDefultTranslation()).append("\"").append(delimeter)
					.append(result.getValue()).append(delimeter)
					.append(result.getTariff().getSubServiceCode()).append(delimeter)
					.append(result.getTariffExportCode().getCode()).append("\n");
				wr.write(sb.toString());
			}
		}	finally {
			IOUtils.closeQuietly(wr);
			IOUtils.closeQuietly(os);
		}

		System.out.println("Results printed to file: " + tmpFile.getAbsolutePath());
	}
}
