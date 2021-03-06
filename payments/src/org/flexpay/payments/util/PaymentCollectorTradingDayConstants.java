package org.flexpay.payments.util;

import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.hibernate.envers.tools.Pair;

import java.util.Map;

import static org.flexpay.common.util.CollectionUtils.map;

@SuppressWarnings({"unchecked"})
public abstract class PaymentCollectorTradingDayConstants {

	// process variable names
	public final static String CAN_UPDATE_OR_CREATE_OPERATION = "CAN_UPDATE_OR_CREATE_OPERATION";
	public final static String PROCESS_STATUS = "PROCESS_STATUS";
	public final static String AUTO_MODE = "AUTO_MODE";

    public static final Map<String, String> PROCESSES_DEFINITION_NAME =
			map(Pair.make(PaymentPoint.class.getName(), "PaymentPointTradingDay"),
				Pair.make(Cashbox.class.getName(), "CashboxTradingDay"),
				Pair.make(PaymentCollector.class.getName(), "PaymentCollectorTradingDay"));

	public static Status getStatusByName(String statusName) {
		return statusCollection.get(statusName);
	}

	private static Map<String, Status> statusCollection = map(
			new Pair<String, Status>(Status.CLOSED.getStatusName(), Status.CLOSED),
			new Pair<String, Status>(Status.ERROR.getStatusName(), Status.ERROR),
			new Pair<String, Status>(Status.OPEN.getStatusName(), Status.OPEN),
			new Pair<String, Status>(Status.PROCESSED.getStatusName(), Status.PROCESSED),
			new Pair<String, Status>(Status.WAIT_APPROVE.getStatusName(), Status.WAIT_APPROVE));

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
