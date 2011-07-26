package org.flexpay.mule.component;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.service.Security;
import org.flexpay.mule.Request;
import org.flexpay.mule.service.MuleService;
import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class MuleComponent implements Callable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private MuleService muleService;
    private JpaTemplate jpaTemplate;
    private PlatformTransactionManager transactionManager;

    @Override
    public Object onCall(MuleEventContext context) throws Exception {

        log.debug("----------------------------------------------------------");

        log.debug("Authenticating...");
        Security.authenticateMuler();

        log.debug("Setting jpaTemplate...");
        muleService.setJpaTemplate(jpaTemplate);

        log.debug("Processing request");

        Request request = (Request) context.getMessage().getPayload();
        log.debug("request = {}", request);

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setReadOnly(false);
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            if (request.isApartment()) {
                muleService.processApartment(request);
            } else if (request.isBuildingAddress()) {
                muleService.processBuildingAddress(request);
            } else if (request.isBuilding()) {
                muleService.processBuilding(request);
            } else if (request.isStreet()) {
                muleService.processStreet(request);
            } else if (request.isStreetType()) {
                muleService.processStreetType(request);
            } else if (request.isDistrict()) {
                muleService.processDistrict(request);
            } else if (request.isTownType()) {
                muleService.processTownType(request);
            } else if (request.isTown()) {
                muleService.processTown(request);
            } else if (request.isRegion()) {
                muleService.processRegion(request);
            } else if (request.isCountry()) {
                muleService.processCountry(request);
            }

            transactionManager.commit(status);

        } catch (FlexPayExceptionContainer e) {
            for (FlexPayException e1 : e.getExceptions()) {
                log.error("Error in proccesing request", e1);
            }
            transactionManager.rollback(status);
        } catch (FlexPayException e) {
            log.error("Error in proccesing request", e);
            transactionManager.rollback(status);
        } catch (Exception e) {
            log.error("Some errors", e);
            transactionManager.rollback(status);
        }

        return null;
    }

    @Required
    public void setMuleService(MuleService muleService) {
        this.muleService = muleService;
    }

    @Required
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        this.jpaTemplate = jpaTemplate;
    }

    @Required
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

}
