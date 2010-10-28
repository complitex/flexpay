package org.flexpay.payments.process.export;

import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.util.SecurityUtil;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.payments.service.TradingDay;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

import static org.flexpay.common.service.Roles.*;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.orgs.service.Roles.*;
import static org.flexpay.payments.service.Roles.*;

public class TradingDaySchedulingJob extends QuartzJobBean {

    private Logger log = LoggerFactory.getLogger(getClass());

	/**
     * Authorities names for payments registry
     */
    private static final List<String> USER_TRADING_DAY_AUTHORITIES = list(
            PROCESS_READ,
			PROCESS_DELETE,
			PROCESS_DEFINITION_UPLOAD_NEW,

			PROCESS_DEFINITION_UPLOAD_NEW,
			PROCESS_DEFINITION_UPLOAD_NEW,

            PAYMENT_COLLECTOR_READ,
            PAYMENT_COLLECTOR_CHANGE,
            PAYMENT_POINT_READ,
			PAYMENT_POINT_CHANGE,
            ORGANIZATION_READ,
            CASHBOX_READ,
            CASHBOX_CHANGE,

			DOCUMENT_READ,
			DOCUMENT_CHANGE,
			OPERATION_READ,
			OPERATION_CHANGE,
			SERVICE_READ
    );

	private static final String USER_TRADING_DAY = "trading-day";

    private PaymentCollectorService paymentCollectorService;
	@SuppressWarnings ({"unchecked"})
	private TradingDay tradingDayService;

	/**
	 * Main execution method
	 *
	 * @param context - job execution context
	 * @throws JobExecutionException when something goes wrong 
	 */
	@SuppressWarnings ({"unchecked"})
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        log.debug("Starting trading day at {}", new Date());

        authenticateTradingDayGenerator();
		FetchRange fetch = new FetchRange();
		do {
			List<PaymentCollector> paymentCollectors = paymentCollectorService.listInstances(fetch);
			for (PaymentCollector paymentCollector : paymentCollectors) {
				try {
					tradingDayService.startTradingDay(paymentCollector);
				} catch (FlexPayException e) {
					throw new JobExecutionException(e);
				}
			}
            fetch.nextPage();
        } while (fetch.hasMore());
    }

	/**
	 * Do payments registry user authentication
	 */
	private void authenticateTradingDayGenerator() {
		SecurityUtil.authenticate(USER_TRADING_DAY, USER_TRADING_DAY_AUTHORITIES);
	}

    @Required
    public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
        this.paymentCollectorService = paymentCollectorService;
    }

	@SuppressWarnings ({"unchecked"})
	@Required
	public void setTradingDayService(TradingDay tradingDayService) {
		this.tradingDayService = tradingDayService;
	}
}
