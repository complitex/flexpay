package org.flexpay.sz.service.imp;

import org.apache.log4j.Logger;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.sz.dao.CharacteristicDao;
import org.flexpay.sz.dbf.LivePropertyDBFInfo;
import org.flexpay.sz.dbf.SzDbfReader;
import org.flexpay.sz.persistence.Characteristic;
import org.flexpay.sz.service.CharacteristicService;
import org.springframework.transaction.annotation.Transactional;

import com.linuxense.javadbf.DBFException;

@Transactional(readOnly = true, rollbackFor = Exception.class)
public class CharacteristicServiceImpl implements CharacteristicService {
	private static Logger log = Logger.getLogger(CharacteristicServiceImpl.class);

	private CharacteristicDao livePropertyDao;

	/**
	 * Create LiveProperty
	 * 
	 * @param liveProperty
	 *            LiveProperty object
	 * @return created LiveProperty object
	 */
	@Transactional(readOnly = false)
	public Characteristic create(Characteristic liveProperty)
			throws FlexPayException {
		livePropertyDao.create(liveProperty);

		if (log.isDebugEnabled()) {
			log.debug("Created LiveProperty: " + liveProperty);
		}

		return liveProperty;
	}

	/**
	 * Create collection of LiveProperty
	 * 
	 * @param iterator
	 *            LiveProperty iterator object
	 * @throws DBFException
	 */
	@Transactional(readOnly = false)
	public void create(SzDbfReader<Characteristic, LivePropertyDBFInfo> reader)
			throws FlexPayException {
		Characteristic liveProperty = null;
		try {
			while ((liveProperty = reader.read()) != null) {
				livePropertyDao.create(liveProperty);
				if (log.isDebugEnabled()) {
					log.debug("Created LiveProperty: " + liveProperty);
				}
			}
		} catch (DBFException e) {
			throw new FlexPayException("error occured while read dbf file", e);
		}
	}

	public void setLivePropertyDao(CharacteristicDao livePropertyDao) {
		this.livePropertyDao = livePropertyDao;
	}

}
