package org.flexpay.payments.actions.registry.corrections;

import org.flexpay.ab.action.person.PersonsListAction;
import org.flexpay.ab.persistence.Person;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.service.RegistryRecordService;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.actions.interceptor.CashboxAware;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

public class CorrectPersonAction extends PersonsListAction implements CashboxAware {

	protected String setupType;
	protected Person object = new Person();
	protected RegistryRecord record = new RegistryRecord();
	protected Long cashboxId;

	protected CorrectionsService correctionsService;
	protected RegistryRecordService recordService;
	protected OrganizationService organizationService;

	/**
	 * Perform action execution.
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return execution result code
	 * @throws Exception if failure occurs
	 */
	@NotNull
	@Override
	protected String doExecute() throws Exception {
		record = recordService.read(record.getId());

		if ("person".equals(setupType)) {

			EircRegistryProperties props = (EircRegistryProperties) record.getRegistry().getProperties();
			Organization organization = organizationService.readFull(props.getSenderStub());
			if (organization == null) {
				addActionError(getText("error.eirc.data_source_not_found"));
				return SUCCESS;
			}
			Stub<DataSourceDescription> sd = organization.sourceDescriptionStub();

			saveCorrection(sd);

			record = recordService.removeError(record);
			return "complete";
		}
		return super.doExecute();
	}

	protected void saveCorrection(Stub<DataSourceDescription> sd) {
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
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

	@Override
	public Long getCashboxId() {
		return cashboxId;
	}

	@Override
	public void setCashboxId(Long cashboxId) {
		this.cashboxId = cashboxId;
	}

	@Required
	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	@Required
	public void setRecordService(RegistryRecordService recordService) {
		this.recordService = recordService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
