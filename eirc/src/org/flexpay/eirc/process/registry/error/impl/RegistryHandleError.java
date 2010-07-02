package org.flexpay.eirc.process.registry.error.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.ImportError;
import org.flexpay.common.persistence.registry.RegistryRecord;
import org.flexpay.common.persistence.registry.workflow.RegistryRecordWorkflowManager;
import org.flexpay.common.persistence.registry.workflow.RegistryWorkflowManager;
import org.flexpay.common.service.importexport.ClassToTypeRegistry;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.exchange.ProcessingContext;
import org.flexpay.eirc.process.registry.error.HandleError;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class RegistryHandleError implements HandleError {
	private Logger log = LoggerFactory.getLogger(getClass());

	private RegistryWorkflowManager registryWorkflowManager;
	private RegistryRecordWorkflowManager recordWorkflowManager;
	private ClassToTypeRegistry classToTypeRegistry;

	private OrganizationService organizationService;

	@SuppressWarnings ({"ThrowableResultOfMethodCallIgnored"})
    @Override
	public ImportError handleError(Throwable t, ProcessingContext context) throws Exception {
		String code = "eirc.error_code.unknown_error";
		if (t instanceof FlexPayExceptionContainer) {
			t = ((FlexPayExceptionContainer) t).getFirstException();
		}
		if (t instanceof FlexPayException) {
			FlexPayException ex = (FlexPayException) t;
			code = StringUtils.isNotEmpty(ex.getErrorKey()) ?
				   ex.getErrorKey() : "eirc.error_code.error_with_processing_container";
		}

		log.warn("Failed processing registry", t);

		ImportError error = new ImportError();
		error.setErrorId(code);
		EircRegistryProperties props = (EircRegistryProperties) context.getRegistry().getProperties();
		Organization sender = organizationService.readFull(props.getSenderStub());
		if (sender == null) {
			throw new IllegalStateException("Cannot find sender organization: #" + props.getSenderStub().getId());
		}
		DataSourceDescription sd = sender.getDataSourceDescription();
		error.setSourceDescription(sd);

		error.setDataSourceBean("consumersDataSource");

		error.setObjectType(classToTypeRegistry.getType(Consumer.class));
		if (context.getCurrentRecord() == null) {
			// mark registry having error if processing header
			error.setSourceObjectId(String.valueOf(context.getRegistry().getId()));
			registryWorkflowManager.setNextErrorStatus(context.getRegistry(), error);
		} else {
			error.setSourceObjectId(String.valueOf(context.getCurrentRecord().getId()));
			recordWorkflowManager.setNextErrorStatus(context.getCurrentRecord(), error);
		}
		return error;
	}

	@Required
	public void setRegistryWorkflowManager(RegistryWorkflowManager registryWorkflowManager) {
		this.registryWorkflowManager = registryWorkflowManager;
	}

	@Required
	public void setRecordWorkflowManager(RegistryRecordWorkflowManager recordWorkflowManager) {
		this.recordWorkflowManager = recordWorkflowManager;
	}

	@Required
	public void setClassToTypeRegistry(ClassToTypeRegistry classToTypeRegistry) {
		this.classToTypeRegistry = classToTypeRegistry;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}
}
