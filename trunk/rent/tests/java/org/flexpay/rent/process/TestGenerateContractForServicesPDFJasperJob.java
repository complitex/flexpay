package org.flexpay.rent.process;

import org.flexpay.common.process.job.Job;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.rent.reports.contract.ContractForServicesForm;
import org.flexpay.rent.test.RentSpringBeanAwareTestCase;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.math.BigDecimal;

public class TestGenerateContractForServicesPDFJasperJob extends RentSpringBeanAwareTestCase {

	@Autowired
	private GenerateContractForServicesPDFJasperJob job;

	@Test
	public void testGenerateContractForServices() throws Throwable {

		ContractForServicesForm form = new ContractForServicesForm();
		form.setContractDate(new Date());
		form.setContractNumber("1235");
		form.setExecutor("EXECUTOR");
		form.setHeadFIO("FAMILIYA IMYA OTCHESTVO");
		form.setHeadPosition("GLAVNIY SHEF");
		form.setRegistrationDocument("REGISTER");
		form.setRenter("RENTER BUDET");
		form.setAddress("ADRES");
		form.setBeginDate(new Date(form.getContractDate().getTime() - 3*24*60*60*1000));
		form.setEndDate(new Date(form.getContractDate().getTime() - 2*24*60*60*1000));
		form.setBusinessType("TIP RABOTY");
		form.setDocument("DOCUMENT");
		form.setDocuments("DOCUMENT1, DOCUMENT2, DOCUMENT3");
		form.setServiceProviders("S_PROVIDER1, S_PROVIDER2, S_PROVIDER3");
		form.setServices("SERVICE1, SERVICE2, SERVICE3, SERVICE$");
		form.setTotalSquare(new BigDecimal(77.13));

		Map<Serializable, Serializable> contextVariables = CollectionUtils.map();
		contextVariables.put(GenerateContractForServicesPDFJasperJob.PARAM_DATA_FORM, form);

		assertSame("Invalid result", Job.RESULT_NEXT, job.execute(contextVariables));

		assertNotNull("Output file was not specified", contextVariables.get(GenerateContractForServicesPDFJasperJob.RESULT_FILE_ID));

	}

}
