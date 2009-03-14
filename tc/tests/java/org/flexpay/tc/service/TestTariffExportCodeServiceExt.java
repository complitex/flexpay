package org.flexpay.tc.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.tc.persistence.TariffExportCode;
import org.flexpay.tc.dao.TariffExportCodeDaoExt;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class TestTariffExportCodeServiceExt extends SpringBeanAwareTestCase {

	@Autowired
	private TariffExportCodeDaoExt tariffExportCodeDAOExt;

	@Test
	public void testFindByCode(){
		TariffExportCode tariffExportCode = tariffExportCodeDAOExt.findByCode(TariffExportCode.EXPORTED);
		assertNotNull("Tariff Export Code not found", tariffExportCode);
		assertEquals("Tariff Export Code is not same", tariffExportCode.getCode(), TariffExportCode.EXPORTED);
		assertEquals("Tariff Export Code id not equals 1", tariffExportCode.getId(), Long.valueOf(1));
	}
}
