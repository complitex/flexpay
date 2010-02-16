package org.flexpay.payments.service.impl;

import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.service.SignatureService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class RSASignatureService implements SignatureService {
	private static final int BUFFER_SIZE = 1024;
	private static final String RSA_KEY_ALGORITHM = "RSA";
	private static final String SHA1_WITH_RSA_SIGNATURE_ALGORITHM = "SHA1withRSA";

	@NotNull
	@Override
	public Signature readPrivateSignature(String key) throws Exception {
		byte[] privKeyBytes = getDataFromFile(key);

		KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);

		// decode private key
		PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privKeyBytes);
		PrivateKey privKey = keyFactory.generatePrivate(privSpec);

		Signature instance = Signature.getInstance(SHA1_WITH_RSA_SIGNATURE_ALGORITHM);
		instance.initSign(privKey);

		return instance;
	}

	@NotNull
	@Override
	public Signature readPublicSignature(String key) throws Exception {
		byte[] pubKeyBytes = getDataFromFile(key);

		KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);

		// decode public key
		X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKeyBytes);
		PublicKey pubKey = keyFactory.generatePublic(pubSpec);

		Signature instance = Signature.getInstance(SHA1_WITH_RSA_SIGNATURE_ALGORITHM);
		instance.initVerify(pubKey);

		return instance;
	}

	@Nullable
	private byte[] getDataFromFile(String file) throws IOException {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(ApplicationConfig.getResourceAsStream(file));

			byte[] data = new byte[BUFFER_SIZE];
			int off = 0;
			int countRead;
			while ((countRead = dis.read(data, off, BUFFER_SIZE)) > 0) {
				off += countRead;
				data = Arrays.copyOf(data, off + BUFFER_SIZE);
			}
			return data;
		} finally {
			if (dis != null) {
				dis.close();
			}
		}
	}
}
