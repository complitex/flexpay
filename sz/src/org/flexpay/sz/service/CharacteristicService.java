package org.flexpay.sz.service;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.sz.dbf.LivePropertyDBFInfo;
import org.flexpay.sz.dbf.SzDbfReader;
import org.flexpay.sz.persistence.Characteristic;

public interface CharacteristicService {
	/**
	 * Create LiveProperty
	 * 
	 * @param liveProperty
	 *            LiveProperty
	 * @return created LiveProperty object
	 */
	Characteristic create(Characteristic liveProperty) throws FlexPayException;

	/**
	 * Create collection of LiveProperty
	 * 
	 * @param iterator
	 *            LiveProperty iterator object
	 */
	void create(SzDbfReader<Characteristic, LivePropertyDBFInfo> reader)
			throws FlexPayException;

}
