package org.flexpay.rent.process;

import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.rent.reports.contract.ContractForServicesAppendix1Form;
import org.flexpay.rent.reports.contract.ContractForServicesForm;
import org.flexpay.rent.test.RentSpringBeanAwareTestCase;

import static org.flexpay.common.util.CollectionUtils.map;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class TestGenerateContractForServicesJasperJob extends RentSpringBeanAwareTestCase {

	@Autowired
	private GenerateContractForServicesJasperJob job;

	@Test
	public void testGenerateContractForServicesPDF() throws Throwable {

		Map<Serializable, Serializable> contextVariables = map();
		contextVariables.put(GenerateContractForServicesJasperJob.PARAM_DATA_FORM, createTestForm());
		contextVariables.put(GenerateContractForServicesJasperJob.PARAM_DATA_FORM1, createTestForm1());
		contextVariables.put(GenerateContractForServicesJasperJob.PARAM_GENERATE_FORMAT, ReportUtil.FORMAT_PDF);

		assertSame("Invalid result for PDF generation", Job.RESULT_NEXT, job.execute(contextVariables));
		assertNotNull("PDF output file was not specified", contextVariables.get(GenerateContractForServicesJasperJob.RESULT_FILE_ID));

	}

	@Test
	public void testGenerateContractForServicesHTML() throws Throwable {

		Map<Serializable, Serializable> contextVariables = map();
		contextVariables.put(GenerateContractForServicesJasperJob.PARAM_DATA_FORM, createTestForm());
		contextVariables.put(GenerateContractForServicesJasperJob.PARAM_DATA_FORM1, createTestForm1());
		contextVariables.put(GenerateContractForServicesJasperJob.PARAM_GENERATE_FORMAT, ReportUtil.FORMAT_HTML);

		assertSame("Invalid result for HTML genaration", Job.RESULT_NEXT, job.execute(contextVariables));
		assertNotNull("HTML output file was not specified", contextVariables.get(GenerateContractForServicesJasperJob.RESULT_FILE_ID));

	}

	@Test
	public void testGenerateContractForServicesCSV() throws Throwable {

		Map<Serializable, Serializable> contextVariables = map();
		contextVariables.put(GenerateContractForServicesJasperJob.PARAM_DATA_FORM, createTestForm());
		contextVariables.put(GenerateContractForServicesJasperJob.PARAM_DATA_FORM1, createTestForm1());
		contextVariables.put(GenerateContractForServicesJasperJob.PARAM_GENERATE_FORMAT, ReportUtil.FORMAT_CSV);

		assertSame("Invalid result for CSV genaration", Job.RESULT_NEXT, job.execute(contextVariables));
		assertNotNull("CSV output file was not specified", contextVariables.get(GenerateContractForServicesJasperJob.RESULT_FILE_ID));

	}

	private ContractForServicesForm createTestForm() {
		ContractForServicesForm form = new ContractForServicesForm();
		form.setContractDate(new Date());
		form.setContractNumber("1235");
		form.setExecutor("EXECUTOR");
		form.setExecutorChiefFIO("FAMILIYA IMYA OTCHESTVO Руководитя ИСПОЛНИТЕЛЯ");
		form.setExecutorChiefPosition("GLAVNIY SHEF ИСПОЛНИТЕЛЯ");
		form.setExecutorAddress("EXECUTOR ADDRESS");
		form.setExecutorBankDetails("EXECUTOR BANK DETAILS");
		form.setRegistrationDocument("REGISTER");
		form.setRenter("RENTER BUDET");
		form.setRenterChiefFIO("FAMILIYA IMYA OTCHESTVO Руководитя АРЕНДАТОРА");
		form.setRenterJuridicalAddress("RENTER JURIDICAL ADDRESS");
		form.setRenterBankDetails("RENTER BANK DETAILS");
		form.setAddress("ADRES");
		form.setBeginDate(new Date(form.getContractDate().getTime() - 3*24*60*60*1000));
		form.setEndDate(new Date(form.getContractDate().getTime() - 2*24*60*60*1000));
		form.setBusinessType("TIP RABOTY");
		form.setDocument("DOCUMENT");
		form.setDocuments("DOCUMENT1, DOCUMENT2, DOCUMENT3");
		form.setServiceProviders("S_PROVIDER1, S_PROVIDER2, S_PROVIDER3");
		form.setServices("SERVICE1, SERVICE2, SERVICE3, SERVICE$");
		form.setTotalSquare(new BigDecimal("77.13"));

		form.setCentralHeatingSP("ЦЕНТРАЛЬНОЕ ОТОПЛЕНИЕ");
		form.setCentralHeatingSPAccount("СЧЁТ ЦО");
		form.setCentralHeatingHeatedSquare(new BigDecimal("34.56"));
		form.setCentralHeatingDesignLoad(452);
		form.setHotWaterSP("ГОРЯЧАЯ ВОДА");
		form.setHotWaterSPAccount("СЧЁТ ГВ");
		form.setHotWaterPercent(6);
		form.setHotWaterDesignLoad(97);
		form.setColdWaterSP("ХОЛОДНАЯ ВОДА");
		form.setColdWaterSPAccount("СЧЁТ ХОЛОДНОЙ ВОДЫ");
		form.setColdWaterPercent(13);
		form.setColdWaterSize(78);
		form.setWaterHeaterSP("НАГРЕВ ВОДЫ");
		form.setWaterHeaterSPAccount("СЧЁТ НАГРЕВА ВОДЫ");
		form.setWaterHeaterPercent(57);
		form.setWaterHeaterSize(23);

		return form;
	}

	private ContractForServicesAppendix1Form createTestForm1() {
		ContractForServicesAppendix1Form form = new ContractForServicesAppendix1Form();

		form.setContractDate(new Date());
		form.setContractNumber("1235");
		form.setAddress("АДРЕС ТУТ БУДЕТ");
		form.setBeginDate(new Date(form.getContractDate().getTime() - 3*24*60*60*1000));
		form.setRenter("RENTER BUDET");
		form.setCentralHeatingSP("ЦЕНТРАЛЬНОЕ ОТОПЛЕНИЕ");
		form.setCentralHeatingSPAccount("СЧЁТ ЦО");
		form.setCentralHeatingHeatedSquare(new BigDecimal("34.56"));
		form.setCentralHeatingDesignLoad(452);
		form.setHotWaterSP("ГОРЯЧАЯ ВОДА");
		form.setHotWaterSPAccount("СЧЁТ ГВ");
		form.setHotWaterPercent(6);
		form.setHotWaterDesignLoad(97);
		form.setColdWaterSP("ХОЛОДНАЯ ВОДА");
		form.setColdWaterSPAccount("СЧЁТ ХОЛОДНОЙ ВОДЫ");
		form.setColdWaterPercent(13);
		form.setColdWaterSize(78);
		form.setWaterHeaterSP("НАГРЕВ ВОДЫ");
		form.setWaterHeaterSPAccount("СЧЁТ НАГРЕВА ВОДЫ");
		form.setWaterHeaterPercent(57);
		form.setWaterHeaterSize(23);

		return form;
	}

}
