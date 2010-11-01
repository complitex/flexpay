package org.flexpay.payments.util;

import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;

import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.PairUtil.pair;

public abstract class PaymentCollectorTradingDayConstants {

	// process variable names
	public final static String CAN_UPDATE_OR_CREATE_OPERATION = "CAN_UPDATE_OR_CREATE_OPERATION";
	public final static String PROCESS_STATUS = "PROCESS_STATUS";
	public final static String AUTO_MODE = "AUTO_MODE";

    public static final Map<String, String> PROCESSES_DEFINITION_NAME =
			map(pair(PaymentPoint.class.getName(), "PaymentPointTradingDay"),
				 pair(Cashbox.class.getName(), "CashboxTradingDay"),
				 pair(PaymentCollector.class.getName(), "PaymentCollectorTradingDay"));

	// trading day statuses
	public static enum Status {

		OPEN("Открыт"),
    	PROCESSED("Обрабатывается"),
    	ERROR("Допущена ошибка, обратитесь к администратору"),
    	WAIT_APPROVE("Ожидает подтверждения"),
    	CLOSED("Закрыт");

		private String statusName;

		private Status(String s) {
			statusName = s;
		}

		public String getStatusName() {
			return statusName;
		}

		@Override
		public String toString() {
			return statusName;
		}
	}

	// trading transitions
	public enum Transition {

        OPEN("Открыть"),
        CLOSE("Закрыть"),
        CLOSE_ALL_CASHBOXES("Закрыть все кассы"),
        MARK_CLOSE_DAY("Пометить на закрытие"),
        UNMARK_CLOSE_DAY("Отменить пометку"),
		CONFIRM_CLOSING_DAY("Подтвердить закрытие");

		private String transitionName;

		private Transition(String s) {
			transitionName = s;
		}

		public String getTransitionName() {
			return transitionName;
		}

		@Override
		public String toString() {
			return transitionName;
		}
	}
}
