package org.flexpay.eirc.process.registry.helper;

import org.flexpay.common.exception.FlexPayException;

import java.util.Map;

public interface ParseRegistryFacade {

	String parse(Map<String, Object> parameters) throws FlexPayException;

}
