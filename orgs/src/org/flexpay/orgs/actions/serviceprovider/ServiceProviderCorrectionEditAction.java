package org.flexpay.orgs.actions.serviceprovider;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.persistence.DataCorrection;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.DataSourceDescriptionService;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.orgs.service.importexport.impl.ClassToTypeRegistryOrgs;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;

public class ServiceProviderCorrectionEditAction extends FPActionSupport {

    private DataCorrection dataCorrection = new DataCorrection();
    private ServiceProvider provider = new ServiceProvider();
    private List<DataSourceDescription> dataSources = list();

    private String crumbCreateKey;
    private CorrectionsService correctionsService;
    private DataSourceDescriptionService dataSourceDescriptionService;
    private ServiceProviderService providerService;
    private ClassToTypeRegistryOrgs classToTypeRegistryOrgs;

    @NotNull
    @Override
    public String doExecute() throws Exception {

        if (dataCorrection == null) {
            log.warn("Data correction parameter is null");
            addActionError(getText("orgs.error.service_provider.incorrect_data_correction"));
            return REDIRECT_SUCCESS;
        }

        if (provider == null || provider.isNew()) {
            log.warn("Incorrect service provider id");
            addActionError(getText("orgs.error.service_provider.incorrect_service_provider_id"));
            return REDIRECT_SUCCESS;
        }

        Stub<ServiceProvider> stub = stub(provider);
        provider = providerService.read(stub);
        if (provider == null) {
            log.warn("Can't get service provider with id {} from DB", stub.getId());
            addActionError(getText("orgs.error.service_provider.cant_get_provider"));
            return REDIRECT_SUCCESS;
        } else if (provider.isNotActive()) {
            log.warn("Service provider with id {} is disabled", stub.getId());
            addActionError(getText("orgs.error.service_provider.cant_get_provider"));
            return REDIRECT_SUCCESS;
        }

        initData();

        if (isSubmit()) {
            if (!doValidate()) {
                return INPUT;
            }

            correctionsService.save(dataCorrection);

            addActionMessage(getText("orgs.service_provider.saved"));

            return REDIRECT_SUCCESS;
        }

        return INPUT;
    }

    private boolean doValidate() {

        Stub<DataSourceDescription> stub = stub(dataCorrection.getDataSourceDescription());
        DataSourceDescription dataSourceDescription = dataSourceDescriptionService.read(stub);
        if (dataSourceDescription == null) {
            log.warn("Can't get data source description with id {} from DB");
            addActionError(getText("orgs.error.service_provider.cant_get_data_source"));
        }

        if (isEmpty(dataCorrection.getExternalId())) {
            log.warn("Incorrect external id value {}", dataCorrection.getExternalId());
            addActionError(getText("orgs.error.service_provider.incorrect_external_id"));
        }

        if (dataCorrection.getInternalObjectId() == null || dataCorrection.getInternalObjectId() <= 0) {
            log.warn("Incorrect internal id value {}", dataCorrection.getInternalObjectId());
            addActionError(getText("orgs.error.service_provider.incorrect_internal_id"));
        }

        return !hasActionErrors();
    }

    private void initData() {

        int type = classToTypeRegistryOrgs.getType(ServiceProvider.class);
        dataSources = dataSourceDescriptionService.find();
        dataCorrection.setObjectType(type);
        dataCorrection.setInternalObjectId(provider.getId());

        if (dataCorrection.isNotNew()) {
            dataCorrection = correctionsService.read(stub(dataCorrection));
        }

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
        return INPUT;
    }

    @Override
    protected void setBreadCrumbs() {
        if (provider.isNew()) {
            crumbNameKey = crumbCreateKey;
        }
        super.setBreadCrumbs();
    }

    public void setDataCorrection(DataCorrection dataCorrection) {
        this.dataCorrection = dataCorrection;
    }

    public DataCorrection getDataCorrection() {
        return dataCorrection;
    }

    public void setProvider(ServiceProvider provider) {
        this.provider = provider;
    }

    public ServiceProvider getProvider() {
        return provider;
    }

    public List<DataSourceDescription> getDataSources() {
        return dataSources;
    }

    public void setCrumbCreateKey(String crumbCreateKey) {
        this.crumbCreateKey = crumbCreateKey;
    }

    @Required
    public void setClassToTypeRegistryOrgs(ClassToTypeRegistryOrgs classToTypeRegistryOrgs) {
        this.classToTypeRegistryOrgs = classToTypeRegistryOrgs;
    }

    @Required
    public void setProviderService(ServiceProviderService providerService) {
        this.providerService = providerService;
    }

    @Required
    public void setCorrectionsService(CorrectionsService correctionsService) {
        this.correctionsService = correctionsService;
    }

    @Required
    public void setDataSourceDescriptionService(DataSourceDescriptionService dataSourceDescriptionService) {
        this.dataSourceDescriptionService = dataSourceDescriptionService;
    }
}
