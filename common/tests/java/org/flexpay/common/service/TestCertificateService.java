package org.flexpay.common.service;

import org.flexpay.common.exception.CertificateBlockedException;
import org.flexpay.common.exception.CertificateExpiredException;
import org.flexpay.common.exception.CertificateNotFoundException;
import org.flexpay.common.exception.InvalidVerifySignatureException;
import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.SecurityUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertFalse;

public class TestCertificateService extends SpringBeanAwareTestCase {

	@Autowired
	private CertificateService certificateService;

	@Test
	public void testAuthenticateUserByCertificate()
			throws CertificateExpiredException, CertificateNotFoundException, InvalidVerifySignatureException, CertificateBlockedException {
		assertNotSame("testlogin2", SecurityUtil.getUserName());
		assertFalse(SecurityUtil.isAuthenticationGranted("ROLE_AB_COUNTRY_READ"));
		certificateService.authenticateUserByCertificate("testlogin2", new byte[0], CollectionUtils.<byte[]>list());
		assertEquals("testlogin2", SecurityUtil.getUserName());
		assertTrue(SecurityUtil.isAuthenticationGranted("ROLE_AB_COUNTRY_READ"));
	}
}
