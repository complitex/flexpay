package org.flexpay.rent.process;

import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.reporting.ReportUtil;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.rent.reports.contract.ContractForServicesAppendix1Form;
import org.flexpay.rent.test.RentSpringBeanAwareTestCase;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;
import java.math.BigDecimal;

public class TestGenerateContractForServicesAppendix1JasperJob extends RentSpringBeanAwareTestCase {

	@Autowired
	private GenerateContractForServicesAppendix1JasperJob job;

	@Test
	public void testGenerateContractForServicesAppendix1PDF() throws Throwable {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
		contextVariables.put(GenerateContractForServicesAppendix1JasperJob.PARAM_DATA_FORM, createTestForm());
		contextVariables.put(GenerateContractForServicesAppendix1JasperJob.PARAM_GENERATE_FORMAT, ReportUtil.FORMAT_PDF);

		assertSame("Invalid result for PDF generation", Job.RESULT_NEXT, job.execute(contextVariables));
		assertNotNull("PDF output file was not specified", contextVariables.get(GenerateContractForServicesAppendix1JasperJob.RESULT_FILE_ID));

	}

	@Test
	public void testGenerateContractForServicesAppendix1HTML() throws Throwable {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
		contextVariables.put(GenerateContractForServicesAppendix1JasperJob.PARAM_DATA_FORM, createTestForm());
		contextVariables.put(GenerateContractForServicesAppendix1JasperJob.PARAM_GENERATE_FORMAT, ReportUtil.FORMAT_HTML);

		assertSame("Invalid result for HTML genaration", Job.RESULT_NEXT, job.execute(contextVariables));
		assertNotNull("HTML output file was not specified", contextVariables.get(GenerateContractForServicesAppendix1JasperJob.RESULT_FILE_ID));

	}

	@Test
	public void testGenerateContractForServicesAppendix1CSV() throws Throwable {

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
		contextVariables.put(GenerateContractForServicesAppendix1JasperJob.PARAM_DATA_FORM, createTestForm());
		contextVariables.put(GenerateContractForServicesAppendix1JasperJob.PARAM_GENERATE_FORMAT, ReportUtil.FORMAT_CSV);

		assertSame("Invalid result for CSV genaration", Job.RESULT_NEXT, job.execute(contextVariables));
		assertNotNull("CSV output file was not specified", contextVariables.get(GenerateContractForServicesAppendix1JasperJob.RESULT_FILE_ID));

	}

	private ContractForServicesAppendix1Form createTestForm() {
		ContractForServicesAppendix1Form form = new ContractForServicesAppendix1Form();

		form.setContractDate(new Date());
		form.setContractNumber("1235");
		form.setAddress("АДРЕС ТУТ БУДЕТ");
		form.setBeginDate(new Date(form.getContractDate().getTime() - 3*24*60*60*1000));
		form.setRenter("RENTER BUDET");
		form.setCentralHeatingSP("ЦЕНТРАЛЬНОЕ ОТОПЛЕНИЕ");
		form.setCentralHeatingSPAccount("СЧЕТ ЦО");
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