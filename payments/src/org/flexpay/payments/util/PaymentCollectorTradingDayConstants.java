package org.flexpay.payments.util;

import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;

import java.util.List;
import java.util.Map;

import static org.flexpay.common.service.Roles.PROCESS_DEFINITION_UPLOAD_NEW;
import static org.flexpay.common.service.Roles.PROCESS_DELETE;
import static org.flexpay.common.service.Roles.PROCESS_READ;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.map;
import static org.flexpay.common.util.PairUtil.pair;
import static org.flexpay.orgs.service.Roles.*;
import static org.flexpay.payments.service.Roles.*;
import static org.flexpay.payments.service.Roles.SERVICE_READ;

public abstract class PaymentCollectorTradingDayConstants {

	// process variable names
	public final static String CAN_UPDATE_OR_CREATE_OPERATION = "CAN_UPDATE_OR_CREATE_OPERATION";
	public final static String PROCESS_STATUS = "PROCESS_STATUS";
	public final static String AUTO_MODE = "AUTO_MODE";

    public static final Map<String, String> PROCESSES_DEFINITION_NAME =
			map(pair(PaymentPoint.class.getName(), "PaymentPointTradingDay"),
				 pair(Cashbox.class.getName(), "CashBoxTradingDay"),
				 pair(PaymentCollector.class.getName(), "PaymentCollectorTradingDay"));

	// trading day statuses
	public static enum Statuses {
		OPEN("Открыт"),
    	PROCESSED("Обрабатывается"),
    	ERROR("Допущена ошибка, обратитесь к администратору"),
    	WAIT_APPROVE("Ожидает подтверждения"),
    	CLOSED("Закрыт");

		private String statusName;

		private Statuses(String s) {
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
	public enum Transitions {
		CONFIRM_CLOSING_DAY("Подтвердить закрытие");

		private String transitionName;

		private Transitions(String s) {
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
