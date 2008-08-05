package org.flexpay.eirc.actions;

import org.flexpay.eirc.dao.RegistryDao;
import org.flexpay.eirc.persistence.SpFile;
import org.flexpay.eirc.persistence.SpRegistry;
import org.flexpay.common.process.ProcessManager;
import static org.flexpay.common.util.CollectionUtils.ar;
import org.jetbrains.annotations.NonNls;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.NotTransactional;

public class TestSpFileAction extends TestSpFileCreateAction {

	@Autowired
	protected SpFileAction fileAction;
	@Autowired
	protected RegistryDao registryDao;
	@Autowired
	protected ProcessManager processManager;

	@Test
	@NotTransactional
	public void testUploadFile() throws Throwable {
		SpFile newFile = uploadFile("org/flexpay/eirc/actions/sp/ree.txt");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	@Test
	@NotTransactional
	public void testUploadZipFile() throws Throwable {
		SpFile newFile = uploadFile("org/flexpay/eirc/actions/sp/ree.zip");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	@Test
	@NotTransactional
	public void testUploadGZipFile() throws Throwable {
		SpFile newFile = uploadFile("org/flexpay/eirc/actions/sp/ree.txt.gz");

		// do clean up
		deleteRecords(newFile);
		deleteFile(newFile);
	}

	protected void deleteRecords(SpFile file) {
		for (SpRegistry registry : fileService.getRegistries(file)) {
//			registryDao.deleteQuittances(registry.getId());
//			registryDao.deleteRecordContainers(registry.getId());
			deleteQuittances(registry.getId());
			deleteContainers(registry.getId());
			registryDao.deleteRegistryContainers(registry.getId());
			registryDao.deleteRecords(registry.getId());
			registryDao.delete(registry);
		}
	}

	private void deleteQuittances(Long registryId) {
		String sql = "delete q " +
					 "from eirc_registries_tbl r " +
					 "left join eirc_registry_records_tbl rr on r.id=rr.registry_id " +
					 "left join eirc_quittance_details_tbl q on rr.id=q.registry_record_id " +
					 "where r.id=?";
		jdbcTemplate.update(sql, ar(registryId));
	}

	private void deleteContainers(Long registryId) {
		String sql = "delete c " +
					 "from eirc_registries_tbl r " +
					 "left join eirc_registry_records_tbl rr on r.id=rr.registry_id " +
					 "left join eirc_registry_record_containers_tbl c on rr.id=c.record_id " +
					 "where r.id=?";
		jdbcTemplate.update(sql, ar(registryId));
	}

	protected SpFile uploadFile(@NonNls String fileName) throws Throwable {
		SpFile newFile = createSpFile(fileName);

		fileAction.setSpFileId(newFile.getId());
		fileAction.setAction("loadToDb");

		try {
			assertEquals("Invalid Struts action result", "success", fileAction.execute());
		} catch (Exception e) {
			deleteRecords(newFile);
			deleteFile(newFile);
			throw e;
		}
		return newFile;
	}
}
