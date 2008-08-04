package org.flexpay.eirc.process.quittance;

import org.apache.log4j.Logger;
import org.flexpay.common.process.job.Job;
import org.flexpay.eirc.service.QuittanceService;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class GenerateQuittanceJob extends Job {

    private QuittanceService quittanceService;
    private Logger log = Logger.getLogger(getClass());

    public String execute(Map<Serializable, Serializable> contextVariables) {

        Long serviceOrganisationId = (Long) contextVariables.get("serviceOrganisationId");
        Date dateFrom = (Date) contextVariables.get("dateFrom");
        Date dateTill = (Date) contextVariables.get("dateTill");

        quittanceService.generateForServiceOrganisation(serviceOrganisationId, dateFrom, dateTill);

        return Job.RESULT_NEXT;
    }

    public void setQuittanceService(QuittanceService quittanceService) {
        this.quittanceService = quittanceService;
    }
}
