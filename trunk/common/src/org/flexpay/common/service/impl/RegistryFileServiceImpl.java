package org.flexpay.common.service.impl;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.registry.RegistryDao;
import org.flexpay.common.dao.registry.RegistryFileDaoExt;
import org.flexpay.common.dao.registry.RegistryRecordDao;
import org.flexpay.common.dao.registry.RegistryRecordDaoExt;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.service.fetch.ReadHints;
import org.flexpay.common.service.fetch.ReadHintsHolder;
import org.flexpay.common.service.impl.fetch.ProcessingReadHintsHandlerFactory;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional (readOnly = true)
public class RegistryFileServiceImpl implements RegistryFileService {

	private RegistryDao registryDao;
	private RegistryFileDaoExt registryFileDaoExt;
	private RegistryRecordDao registryRecordDao;
	private RegistryRecordDaoExt registryRecordDaoExt;

	private List<ProcessingReadHintsHandlerFactory> readHintsHandlerFactories = CollectionUtils.list();

	/**
	 * Get registries for file
	 *
	 * @param spFile ServiceProvider obtained file
	 * @return List of registries in a file
	 */
	@Override
	public List<Registry> getRegistries(FPFile spFile) {
		return registryDao.listRegistries(spFile.getId());
	}

	/**
	 * Get registry records for processing
	 *
	 * @param registry Registry header
	 * @param range	Fetch range
	 * @return list of records
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public List<RegistryRecord> getRecordsForProcessing(@NotNull Stub<Registry> registry, FetchRange range) {

		List<RegistryRecord> records = registryRecordDao.listRecordsForProcessing(registry.getId(), range);

		ReadHints hints = ReadHintsHolder.getHints();
		if (hints == null) {
			return records;
		}

		for (ProcessingReadHintsHandlerFactory factory : readHintsHandlerFactories) {
			if (factory.supports(hints)) {
				factory.getInstance(registry, range, records).read();
			}
		}

		return records;
	}

	/**
	 * @{inheritDoc}
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public List<RegistryRecord> getLoadedAndFixedRecords(@NotNull Stub<Registry> registry, FetchRange range) {

		List<RegistryRecord> records = registryRecordDao.listLoadedAndFixedRecords(registry.getId(), range);

		ReadHints hints = ReadHintsHolder.getHints();
		if (hints == null) {
			return records;
		}

		for (ProcessingReadHintsHandlerFactory factory : readHintsHandlerFactories) {
			if (factory.supports(hints)) {
				factory.getInstance(registry, range, records).read();
			}
		}

		return records;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public List<RegistryRecord> getRecordsForProcessing(@NotNull Set<Long> recordIds) {

		List<RegistryRecord> records = registryRecordDao.listRecordsForProcessingCollection(recordIds);

		ReadHints hints = ReadHintsHolder.getHints();
		if (hints == null) {
			return records;
		}

		for (ProcessingReadHintsHandlerFactory factory : readHintsHandlerFactories) {
			if (factory.supports(hints)) {
				factory.getInstance(records).read();
			}
		}

		return records;
	}

	public FetchRange getFetchRangeForProcessing(@NotNull Stub<Registry> registry, int pageSize, @NotNull Long lastProcessedRegistryRecord) {

		FetchRange range = new FetchRange(pageSize);
		Long[] stats = registryRecordDaoExt.getMinMaxIdsForProcessing(registry.getId(), lastProcessedRegistryRecord);

		range.setMinId(stats[0]);
		range.setMaxId(stats[1]);
		range.setCount(stats[2].intValue());
		range.setLowerBound(range.getMinId());
		range.setUpperBound(range.getLowerBound() != null ? range.getLowerBound() + range.getPageSize() - 1 : null);
		// validate stats query
		if (stats[0] != null && stats[1] != null && stats[2] != null) {
			if (range.getMinId() > range.getMaxId()) {
				throw new IllegalStateException("minId > maxId, did you specified");
			}
			if (range.getMaxId() - range.getMinId() + 1 < range.getCount()) {
				throw new IllegalStateException("maxId - minId < count, did you specified");
			}
		}

		return range;
	}

	/**
	 * Check if RegistryFile was loaded
	 *
	 * @param stub File stub
	 * @return <code>true</code> if file already loaded, or <code>false</code> otherwise
	 */
	@Override
	public boolean isLoaded(@NotNull Stub<FPFile> stub) {
		return registryFileDaoExt.isLoaded(stub.getId());
	}

	/**
	 * @{inheritDoc}
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	@Override
	public boolean hasLoadedAndFixedRecords(@NotNull Stub<Registry> registry) {
		return registryRecordDaoExt.hasLoadedAndFixedRecords(registry.getId());
	}

	/**
	 * Add hints read handler factory
	 *
	 * @param factory ProcessingReadHintsHandlerFactory
	 */
	public void setReadHintsHandlerFactory(ProcessingReadHintsHandlerFactory factory) {
		readHintsHandlerFactories.add(factory);
	}

	@Required
	public void setRegistryDao(RegistryDao registryDao) {
		this.registryDao = registryDao;
	}

	@Required
	public void setRegistryRecordDao(RegistryRecordDao registryRecordDao) {
		this.registryRecordDao = registryRecordDao;
	}

	@Required
	public void setRegistryFileDaoExt(RegistryFileDaoExt registryFileDaoExt) {
		this.registryFileDaoExt = registryFileDaoExt;
	}

	@Required
	public void setRegistryRecordDaoExt(RegistryRecordDaoExt registryRecordDaoExt) {
		this.registryRecordDaoExt = registryRecordDaoExt;
	}
}
