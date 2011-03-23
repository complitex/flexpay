package org.flexpay.payments.export;

import org.apache.commons.io.IOUtils;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.test.PaymentsSpringBeanAwareTestCase;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.junit.Assert.assertTrue;

public class TestSignature extends PaymentsSpringBeanAwareTestCase {

	@Test
	public void testSignature() throws Exception {

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
		IOUtils.copy(ApplicationConfig.getResourceAsStream("WEB-INF/payments/configs/keys/secret.key"), os);

		// decode private key
		PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(os.toByteArray());
		PrivateKey privKey = keyFactory.generatePrivate(privSpec);

		Signature instance = Signature.getInstance("SHA1withRSA");
		instance.initSign(privKey);

		byte[] bytes = "TEST-ME".getBytes();
		instance.update(bytes);
		byte[] sign = instance.sign();

		// decode public key
		os = new ByteArrayOutputStream(1024);
		IOUtils.copy(ApplicationConfig.getResourceAsStream("WEB-INF/payments/configs/keys/public.key"), os);
		X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(os.toByteArray());
		PublicKey pubKey = keyFactory.generatePublic(pubSpec);

		Signature instance2 = Signature.getInstance("SHA1withRSA");
		instance2.initVerify(pubKey);
		instance2.update(bytes);
		assertTrue("Signature validation failed", instance2.verify(sign));
	}
}
