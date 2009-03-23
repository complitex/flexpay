package org.flexpay.tc.service.importexport;

import org.apache.commons.io.IOUtils;
import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.DistrictService;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.tc.persistence.TariffCalculationResult;
import org.flexpay.tc.persistence.TariffExportLogRecord;
import org.flexpay.tc.persistence.TariffExportCode;
import org.flexpay.tc.util.config.ApplicationConfig;
import org.flexpay.tc.service.TariffService;
import org.flexpay.tc.service.TariffCalculationResultService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

public class TestOutputExportResults extends SpringBeanAwareTestCase {

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

    private String delimeter = ",";

    private String[] subserviceCodes = {"010", "020", "030", "040", "050", "060", "070", "080", "090", "100", "110", "120", "130", "140", "150", "160"};

    @Test
    public void testGenerateTariffs() throws Exception {

        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd");
        Date calcDate = df.parse("2009_01_30");

        File tmpFile = null;
        OutputStream os = null;
        Writer wr = null;
        try {
            // creating temporary file and writer
            tmpFile = File.createTempFile("Output_" + df.format(calcDate) + "_", ".csv");
            os = new BufferedOutputStream(new FileOutputStream(tmpFile));
            wr = new OutputStreamWriter(os, "cp1251");

            // loading message source
            ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
            ms.setDefaultEncoding("UTF-8");
            ms.setBasename("org/flexpay/tc/i18n/messages");

            List<Long> buildingIds = tariffCalculationResultService.getAddressIds(calcDate);

            for (Long buildingId : buildingIds) {

                String hql = "select distinct result " +
                        "from TariffCalculationResult result " +
                        "	inner join fetch result.tariff tariff " +
                        "	left join fetch tariff.translations ttr " +
                        "	inner join fetch result.lastTariffExportLogRecord logrecord " +
                        "   left join fetch logrecord.tariffExportCode exportCode " +
                        "   left join fetch result.building building " +
                        "where result.calculationDate=? and building.id=? " +
                        "order by result.building.id, exportCode.code ";
                List<TariffCalculationResult> results = (List<TariffCalculationResult>) hibernateTemplate.find(hql, new Object[]{calcDate, buildingId});
                System.out.println("Found " + results.size() + " results for date: " + calcDate);

                Building defaultBuilding = buildingService.read(new Stub<Building>(buildingId));

                for (String subserviceCode : subserviceCodes) {

                    log.info("Subservice code is {}", subserviceCode);
                    TariffCalculationResult result = getTariffCalcResultBySubserviceCode(results, subserviceCode);

                    if (result == null) {
                        result = new TariffCalculationResult();
                        result.setBuilding(defaultBuilding);
                        result.setTariff(tariffService.getTariffByCode(subserviceCode));
                        result.setCalculationDate(calcDate);
                        result.setValue(BigDecimal.ZERO);

                        TariffExportCode exportCode = new TariffExportCode();
                        exportCode.setCode(TariffExportCode.TARIFF_NOT_FOUND_FOR_BUILDING);
                        TariffExportLogRecord logRecord = new TariffExportLogRecord();
                        logRecord.setTariffExportCode(exportCode);
                        result.setLastTariffExportLogRecord(logRecord);
                    }

                    writeCalculationResult(ms, wr, result);
                }
            }
        } finally {
            IOUtils.closeQuietly(wr);
            IOUtils.closeQuietly(os);
        }
        
        System.out.println("Results printed to file: " + tmpFile.getAbsolutePath());
    }

    private void writeCalculationResult(ReloadableResourceBundleMessageSource ms, Writer wr, TariffCalculationResult result) throws Exception {
        // формат записи в файл
        // 1) Адрес дома (улица, номер дома и так далее) 2) название тарифа 3) значение тарифа 4) код тарифа  5) tariff export code

        Building building = result.getBuilding();
        String address = addressService.getBuildingAddress(stub(building), ApplicationConfig.getDefaultLocale());
        building = buildingService.read(stub(building));
        District district = districtService.readFull(stub(building.getDistrict()));
        String i18nName = result.getLastTariffExportLogRecord().getTariffExportCode().getI18nName();
        Object[] params = {};

        StringBuilder sb = new StringBuilder()
                .append("\"").append(district.getCurrentName().getDefaultNameTranslation()).append("\"").append(delimeter)
                .append("\"").append(address).append("\"").append(delimeter)
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
}
