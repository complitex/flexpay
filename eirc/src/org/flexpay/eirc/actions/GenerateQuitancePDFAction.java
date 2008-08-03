package org.flexpay.eirc.actions;

import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.process.ProcessManager;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.eirc.persistence.ServiceOrganisation;
import org.flexpay.eirc.service.ServiceOrganisationService;

import java.io.Serializable;
import java.util.*;

public class GenerateQuitancePDFAction extends FPActionSupport {

    private ServiceOrganisationService serviceOrganisationService;
    private Integer year;
    private Integer month;
    private Long serviceOrganisationId;
    private List<ServiceOrganisation> serviceOrganizationList;

    private String resultFile;

    private ProcessManager processManager;

    public String doExecute() throws Exception {

        if (isSubmit()) {

            Calendar cal = new GregorianCalendar(year, month, 1);
            Date dateFrom = cal.getTime();
            cal.add(Calendar.MONTH, 1);
            Date dateTill = cal.getTime();

            Map<Serializable, Serializable> contextVariables = CollectionUtils.map();

            contextVariables.put("serviceOrganisationId", serviceOrganisationId);
            contextVariables.put("dateFrom", dateFrom);
            contextVariables.put("dateTill", dateTill);

            processManager.createProcess("GenerateQuitancePDF", contextVariables);

        }

        initDefaultDate();

        serviceOrganizationList = serviceOrganisationService.listServiceOrganisations();

        return SUCCESS;
    }

    /**
     * Get default error execution result
     * <p/>
     * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
     *
     * @return {@link #ERROR} by default
     */
    @Override
    protected String getErrorResult() {
        return SUCCESS;
    }

    private void initDefaultDate() {
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
    }


    /**
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @return the month
     */
    public Integer getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(Integer month) {
        this.month = month;
    }

    /**
     * @param serviceOrganisationId the serviceOrganisationId to set
     */
    public void setServiceOrganisationId(Long serviceOrganisationId) {
        this.serviceOrganisationId = serviceOrganisationId;
    }

    /**
     * @return the serviceOrganizationList
     */
    public List<ServiceOrganisation> getServiceOrganizationList() {
        return serviceOrganizationList;
    }

    /**
     * @param serviceOrganisationService the serviceOrganisationService to set
     */
    public void setServiceOrganisationService(
            ServiceOrganisationService serviceOrganisationService) {
        this.serviceOrganisationService = serviceOrganisationService;
    }

    /**
     * @return the resultFile
     */
    public String getResultFile() {
        return resultFile;
    }

    /**
     * @param processManager process manager setter
     */
    public void setProcessManager(ProcessManager processManager) {
        this.processManager = processManager;
    }
}
