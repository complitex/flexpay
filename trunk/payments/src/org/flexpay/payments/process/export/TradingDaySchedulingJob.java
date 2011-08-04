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

import java.util.*;

import static org.flexpay.common.service.Roles.*;
import static org.flexpay.common.util.CollectionUtils.*;
import static org.flexpay.orgs.service.Roles.*;
import static org.flexpay.payments.service.Roles.*;

public class TradingDaySchedulingJob extends QuartzJobBean {

    private Logger log = LoggerFactory.getLogger(getClass());

	/**
     * Authorities names for payments registry
     */
    private static final List<String> USER_TRADING_DAY_AUTHORITIES = list(
			PROCESS_START,
            PROCESS_READ,
			PROCESS_DEFINITION_UPLOAD_NEW,
			PROCESS_DEFINITION_READ,
			PROCESS_COMPLETE_HUMAN_TASK,

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

	private static final Map<Long, Collection<PaymentCollector>> canNotStartedPaymentCollectorsCollection = map();

	/**
	 * Main execution method
	 *
	 * @param context - job execution context
	 * @throws JobExecutionException when something goes wrong 
	 */
	@Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        log.debug("Starting trading day at {}", new Date());

        authenticateTradingDayGenerator();
		FetchRange fetch = new FetchRange();
		long threadId = Thread.currentThread().getId();
		Collection<PaymentCollector> canNotStartedPaymentCollectorsCollectionInThisThread = set();
		synchronized (canNotStartedPaymentCollectorsCollection) {
			do {
				List<PaymentCollector> paymentCollectors = paymentCollectorService.listInstances(fetch);
				for (Collection<PaymentCollector> clearPaymentCollectors : canNotStartedPaymentCollectorsCollection.values()) {
					paymentCollectors.removeAll(clearPaymentCollectors);
				}
				canNotStartedPaymentCollectorsCollectionInThisThread.addAll(startPaymentCollectors(paymentCollectors));
				fetch.nextPage();
			} while (fetch.hasMore());
			canNotStartedPaymentCollectorsCollection.put(threadId, canNotStartedPaymentCollectorsCollectionInThisThread);
		}

		while (!canNotStartedPaymentCollectorsCollectionInThisThread.isEmpty()) {
			log.debug("Timeout 60 sec. For repeat start trading day: {}", canNotStartedPaymentCollectorsCollectionInThisThread);
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				log.warn("Interrupted begin payment collector`s trading day", e);
				canNotStartedPaymentCollectorsCollection.remove(threadId);
				break;
			}
			synchronized (canNotStartedPaymentCollectorsCollection) {
				try {
					canNotStartedPaymentCollectorsCollectionInThisThread = startPaymentCollectors(canNotStartedPaymentCollectorsCollectionInThisThread);
					canNotStartedPaymentCollectorsCollection.put(threadId, canNotStartedPaymentCollectorsCollectionInThisThread);
				} catch (Throwable th) {
					canNotStartedPaymentCollectorsCollection.remove(threadId);
					throw new JobExecutionException(th);
				}
			}
		}

		canNotStartedPaymentCollectorsCollection.remove(threadId);
    }

	@SuppressWarnings ({"unchecked"})
	private Collection<PaymentCollector> startPaymentCollectors(Collection<PaymentCollector> paymentCollectors) throws JobExecutionException {
		Collection<PaymentCollector> newListCanNotStartedPaymentCollectors = set();
		for (PaymentCollector paymentCollector : paymentCollectors) {
			try {
				if (!tradingDayService.startTradingDay(paymentCollector)) {
					newListCanNotStartedPaymentCollectors.add(paymentCollector);
				}
			} catch (FlexPayException e) {
				throw new JobExecutionException(e);
			}
		}
		return newListCanNotStartedPaymentCollectors;
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
