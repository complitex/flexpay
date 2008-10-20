package org.flexpay.eirc.actions.registry.corrections;

import org.flexpay.ab.actions.person.ListPersons;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.persistence.RegistryRecord;
import org.flexpay.eirc.service.RegistryRecordService;
import org.flexpay.eirc.service.importexport.RawConsumerData;
import org.jetbrains.annotations.NotNull;

public class CorrectPersonAction extends ListPersons {

	private String setupType;
	private Person object = new Person();
	private RegistryRecord record = new RegistryRecord();

	private RawConsumersDataSource consumersDataSource;
	private CorrectionsService correctionsService;
	private RegistryRecordService recordService;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in
	 * a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	protected String doExecute() throws Exception {
		record = recordService.read(record.getId());

		if ("person".equals(setupType)) {

			DataSourceDescription sd = recordService.getDataSourceDescription(record);
			if (sd == null) {
				addActionError(getText("error.eirc.data_source_not_found"));
				return super.doExecute();
			}

			RawConsumerData data = consumersDataSource.getById(String.valueOf(record.getId()));

			// add correction for apartment
			DataCorrection correction = correctionsService.getStub(data.getPersonFIOId(), object, sd);
			correctionsService.save(correction);

			record = recordService.removeError(record);
			return "complete";
		}
		return super.doExecute();
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in
	 * a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	protected String getErrorResult() {
		return "person".equals(setupType) ? "complete" : super.getErrorResult();
	}

	public String getSetupType() {
		return setupType;
	}

	public void setSetupType(String setupType) {
		this.setupType = setupType;
	}

	public Person getObject() {
		return object;
	}

	public void setObject(Person object) {
		this.object = object;
	}

	public RegistryRecord getRecord() {
		return record;
	}

	public void setRecord(RegistryRecord record) {
		this.record = record;
	}

	public void setConsumersDataSource(RawConsumersDataSource consumersDataSource) {
		this.consumersDataSource = consumersDataSource;
	}

	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	public void setRecordService(RegistryRecordService recordService) {
		this.recordService = recordService;
	}
}
