package org.flexpay.payments.action.reports;

import org.flexpay.orgs.persistence.filters.ServiceProviderFilter;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.OperationStatus;
import org.flexpay.payments.reports.payments.AccPaymentsRegistriesReportRequest;
import org.flexpay.payments.reports.payments.AccPaymentsReportRequest;
import org.flexpay.payments.reports.payments.AccReportRequest;
import org.flexpay.payments.util.config.PaymentsUserPreferences;
import org.springframework.beans.factory.annotation.Required;

public class AccPaymentsRegistriesReportAction extends AccPaymentsReportAction {

    private static final String PREFIX = "AccPaymentsRegistries";

    private static final String ACC_PAYMENTS_REGISTRIES_ALL_SERVICE_PROVIDERS = PREFIX + "AllServiceProviders";
    private static final String ACC_PAYMENTS_REGISTRIES_SERVICE_PROVIDER = PREFIX + "ServiceProvider";

    private ServiceProviderFilter serviceProviderFilter = new ServiceProviderFilter();

    private ServiceProviderService serviceProviderService;

    @SuppressWarnings({"unchecked"})
    @Override
    protected void initFilters() {
        serviceProviderFilter = serviceProviderService.initServiceProvidersFilter(serviceProviderFilter);
        serviceProviderFilter.setNeedAutoChange(false);
    }

    @Override
    protected AccReportRequest buildReportRequest() {

        AccPaymentsRegistriesReportRequest request = new AccPaymentsRegistriesReportRequest();

        request.setBeginDate(beginTimeFilter.setTime(beginDateFilter.getDate()));
        request.setEndDate(endTimeFilter.setTime(endDateFilter.getDate()));
        request.setPaymentStatus(OperationStatus.REGISTERED);
        request.setLocale(getUserPreferences().getLocale());
        request.setPaymentCollectorId(((PaymentsUserPreferences) getUserPreferences()).getPaymentCollectorId());

        if (serviceProviderFilter != null && serviceProviderFilter.needFilter()) {
            request.setServiceProviderId(serviceProviderFilter.getSelectedId());
        }

        return request;

    }

    @Override
    protected void uploadAdditionalReportFiles(AccReportRequest request) throws Exception {
        String reportName = getReportName(request);
        uploadReport(reportName + SERVICE_PROVIDERS_SUFFIX);
        uploadReport(reportName + REGISTRIES_SUFFIX);
    }

    @Override
    protected String getReportName(AccReportRequest request) {

        Long serviceProvierId = ((AccPaymentsRegistriesReportRequest) request).getServiceProviderId();

        if (serviceProvierId != null && serviceProvierId > 0) {
            return ACC_PAYMENTS_REGISTRIES_SERVICE_PROVIDER;
        } else {
            return ACC_PAYMENTS_REGISTRIES_SERVICE_PROVIDER;
        }

    }

    public ServiceProviderFilter getServiceProviderFilter() {
        return serviceProviderFilter;
    }

    public void setServiceProviderFilter(ServiceProviderFilter serviceProviderFilter) {
        this.serviceProviderFilter = serviceProviderFilter;
    }

    @Required
    public void setServiceProviderService(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }
}
