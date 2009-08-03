package org.flexpay.common.service.imp;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.dao.registry.RegistryDao;
import org.flexpay.common.dao.registry.RegistryFileDaoExt;
import org.flexpay.common.dao.registry.RegistryRecordDao;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryFileService;
import org.flexpay.common.service.fetch.ReadHints;
import org.flexpay.common.service.fetch.ReadHintsHolder;
import org.flexpay.common.service.imp.fetch.ProcessingReadHintsHandlerFactory;
import org.flexpay.common.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional (readOnly = true)
public class RegistryFileServiceImpl implements RegistryFileService {

	private RegistryDao registryDao;
	private RegistryFileDaoExt registryFileDaoExt;
	private RegistryRecordDao registryRecordDao;

	private List<ProcessingReadHintsHandlerFactory> readHintsHandlerFactories = CollectionUtils.list();

	/**
	 * Get registries for file
	 *
	 * @param spFile ServiceProvider obtained file
	 * @return List of registries in a file
	 */
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
	 * Check if RegistryFile was loaded
	 *
	 * @param stub File stub
	 * @return <code>true</code> if file already loaded, or <code>false</code> otherwise
	 */
	public boolean isLoaded(@NotNull Stub<FPFile> stub) {
		return registryFileDaoExt.isLoaded(stub.getId());
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

	/**
	 * Add hints read handler factory
	 *
	 * @param factory ProcessingReadHintsHandlerFactory
	 */
	public void setReadHintsHandlerFactory(ProcessingReadHintsHandlerFactory factory) {
		readHintsHandlerFactories.add(factory);
	}
}
