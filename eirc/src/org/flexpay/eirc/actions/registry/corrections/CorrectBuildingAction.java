package org.flexpay.eirc.actions.registry.corrections;

import org.flexpay.ab.actions.buildings.ListBuildings;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.eirc.dao.importexport.RawConsumersDataSource;
import org.flexpay.eirc.persistence.ServiceType;
import org.flexpay.eirc.persistence.ServiceTypeNameTranslation;
import org.flexpay.eirc.persistence.SpRegistryRecord;
import org.flexpay.eirc.service.ServiceTypeService;
import org.flexpay.eirc.service.SpRegistryRecordService;
import org.flexpay.eirc.service.importexport.RawConsumerData;

public class CorrectBuildingAction extends ListBuildings {

	private String setupType;
	private Buildings object = new Buildings();
	private SpRegistryRecord record = new SpRegistryRecord();

	private RawConsumersDataSource consumersDataSource;
	private CorrectionsService correctionsService;
	private SpRegistryRecordService recordService;
	private ServiceTypeService serviceTypeService;

	public String execute() throws Exception {

		record = recordService.read(record.getId());

		if ("building".equals(setupType)) {

			DataSourceDescription sd = recordService.getDataSourceDescription(record);
			if (sd == null) {
				addActionError(getText("error.eirc.data_source_not_found"));
				return super.execute();
			}

			RawConsumerData data = consumersDataSource.getById(String.valueOf(record.getId()));

			// add correction for buildings
			DataCorrection correction = correctionsService.getStub(data.getBuildingId(), object, sd);
			correctionsService.save(correction);

			record = recordService.removeError(record);
			return "complete";
		}
		return super.execute();
	}

	public String getServiceTypeName(ServiceType typeStub) throws FlexPayException {
		ServiceType type = serviceTypeService.getServiceType(typeStub);
		ServiceTypeNameTranslation name = getTranslation(type.getTypeNames());
		return name == null ? "Unknown" : name.getName();
	}

	public String getSetupType() {
		return setupType;
	}

	public void setSetupType(String setupType) {
		this.setupType = setupType;
	}

	public Buildings getObject() {
		return object;
	}

	public void setObject(Buildings object) {
		this.object = object;
	}

	public SpRegistryRecord getRecord() {
		return record;
	}

	public void setRecord(SpRegistryRecord record) {
		this.record = record;
	}

	public void setConsumersDataSource(RawConsumersDataSource consumersDataSource) {
		this.consumersDataSource = consumersDataSource;
	}

	public void setCorrectionsService(CorrectionsService correctionsService) {
		this.correctionsService = correctionsService;
	}

	public void setRecordService(SpRegistryRecordService recordService) {
		this.recordService = recordService;
	}

	public void setServiceTypeService(ServiceTypeService serviceTypeService) {
		this.serviceTypeService = serviceTypeService;
	}
}