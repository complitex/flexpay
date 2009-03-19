package org.flexpay.eirc.actions.quittance;

import org.flexpay.common.actions.FPActionSupport;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.eirc.service.QuittanceService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.eirc.persistence.account.Quittance;
import org.flexpay.eirc.persistence.account.QuittanceDetailsQuittance;
import org.flexpay.eirc.persistence.account.QuittanceDetails;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.ConsumerInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class QuittancePayAction extends FPActionSupport {

    // form data
    private Quittance quittance = new Quittance();
    private String quittanceNumber;

    // required services
    private QuittanceService quittanceService;
    
    @NotNull
    protected String doExecute() throws Exception {

        if (isSubmit()) {
            session.put(quittanceNumber , "true");
            return REDIRECT_SUCCESS;
        }

        quittance = quittanceService.readFull(stub(quittance));

        return INPUT;
    }

    @NotNull
    protected String getErrorResult() {
        return INPUT;
    }

    // rendering utility methods
    public String getAddress() {
        return quittance.getEircAccount().getConsumerInfo().getAddress();
    }

    public String getFIO() {
        return quittance.getEircAccount().getConsumerInfo().getFIO();
    }

    public String getServiceName(QuittanceDetails qd) {
        return getTranslation(qd.getConsumer().getService().getServiceType().getTypeNames()).getName();        
    }

    public String getServiceProviderName(QuittanceDetails qd) {
        return qd.getConsumer().getService().getServiceProvider().getName();
    }

    public String getPayable(QuittanceDetails qd) {        
        return qd.getOutgoingBalance().toString();
    }

    public String getTotalPayable() {
        BigDecimal total = new BigDecimal("0.00");

        for (QuittanceDetails qd : quittance.getQuittanceDetails()) {
            total = total.add(qd.getOutgoingBalance());
        }

        return total.toString();
    }

    // set/get form data
    public void setQuittance(Quittance quittance) {
        this.quittance = quittance;
    }

    public Quittance getQuittance() {
        return quittance;
    }

    public String getQuittanceNumber() {
        return quittanceNumber;
    }

    public void setQuittanceNumber(String quittanceNumber) {
        this.quittanceNumber = quittanceNumber;
    }

    // required services setters
    @Required
    public void setQuittanceService(QuittanceService quittanceService) {
        this.quittanceService = quittanceService;
    }

}
