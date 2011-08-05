package org.flexpay.common.persistence.history.impl;

import org.flexpay.common.dao.ExternalHistoryPackDao;
import org.flexpay.common.dao.HistoryUnPackDataDao;
import org.flexpay.common.persistence.history.ExternalHistoryPack;
import org.flexpay.common.persistence.history.HistoryUnPackData;
import org.flexpay.common.persistence.history.HistoryUnpackManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class HistoryUnpackManagerImpl implements HistoryUnpackManager {

	private Logger log = LoggerFactory.getLogger(getClass());

	private ExternalHistoryPackDao historyPackDao;
	private HistoryUnPackDataDao unPackDataDao;

	/**
	 * Notify manager a new history records pack available
	 *
	 * @param pack History records pack
	 * @return Persisted pack back
	 */
	@NotNull
	@Transactional (readOnly = false)
    @Override
	public ExternalHistoryPack create(@NotNull ExternalHistoryPack pack) {
		historyPackDao.create(pack);

		log.debug("Created history pack: {}", pack);

		return pack;
	}

	/**
	 * update history pack details
	 *
	 * @param pack History records pack
	 * @return Updated pack back
	 */
	@NotNull
	@Override
	@Transactional (readOnly = false)
	public ExternalHistoryPack update(@NotNull ExternalHistoryPack pack) {
		historyPackDao.update(pack);

		log.debug("Updated history pack: {}", pack);

		return pack;
	}

	/**
	 * Get next history packs that is to be unpacked
	 *
	 * @return History packs
	 */
	@NotNull
    @Override
	public List<ExternalHistoryPack> getNextPacks() {

		List<ExternalHistoryPack> result = historyPackDao.findLatestPacks();
		log.debug("Next packs: {}", result);

		return result;
	}

	/**
	 * Notify manager that the pack was unpacked
	 *
	 * @param pack History records pack
	 * @return Unpack data for external source pack was created for
	 */
	@NotNull
	@Transactional (readOnly = false)
    @Override
	public HistoryUnPackData setLastUnpacked(@NotNull ExternalHistoryPack pack) {

		List<HistoryUnPackData> lastPacked = unPackDataDao.findLastUnpackedData(pack.getSourceInstanceId());
		HistoryUnPackData data = lastPacked.isEmpty() ? null : lastPacked.get(0);
		if (data == null) {
			data = new HistoryUnPackData();
			data.setSourceInstanceId(pack.getSourceInstanceId());
		}
		data.setLastUnPackedPacket(pack);

		if (data.isNew()) {
			unPackDataDao.create(data);
		} else {
			unPackDataDao.update(data);
		}

		log.debug("Updated unpack data: {}", data);

		return data;
	}

    @Override
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        historyPackDao.setJpaTemplate(jpaTemplate);
        unPackDataDao.setJpaTemplate(jpaTemplate);
    }

	@Required
	public void setHistoryPackDao(ExternalHistoryPackDao historyPackDao) {
		this.historyPackDao = historyPackDao;
	}

	@Required
	public void setUnPackDataDao(HistoryUnPackDataDao unPackDataDao) {
		this.unPackDataDao = unPackDataDao;
	}

}
