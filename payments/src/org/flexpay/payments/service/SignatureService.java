package org.flexpay.payments.service;

import org.jetbrains.annotations.NotNull;

import java.security.Signature;

public interface SignatureService {
	/**
	 * Read protected signature  from resource
	 *
	 * @param key Resource
	 * @return  Private signature
	 * @throws Exception  If signature did not load
	 */
	@NotNull
	Signature readPrivateSignature(String key) throws Exception;

	/**
	 * Read public signature  from resource   
	 *
	 * @param key Resource
	 * @return  Public signature
	 * @throws Exception  If signature did not load
	 */
	@NotNull
	Signature readPublicSignature(String key) throws Exception;
}
