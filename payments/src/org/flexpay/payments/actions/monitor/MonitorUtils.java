package org.flexpay.payments.actions.monitor;

import org.flexpay.payments.persistence.OperationType;
import org.flexpay.payments.service.statistics.OperationTypeStatistics;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public class MonitorUtils {

    public static final SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

    public static Long getPaymentsCount(List<OperationTypeStatistics> typeStatisticses) {
		Long count = 0L;
		for (OperationTypeStatistics stats : typeStatisticses) {
			if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
				count += stats.getCount();
			}
		}
		return count;
	}

    public static BigDecimal getPaymentsSum(List<OperationTypeStatistics> typeStatisticses) {
        BigDecimal sum = new BigDecimal("0.00");
        for (OperationTypeStatistics stats : typeStatisticses) {
            if (OperationType.isPaymentCode(stats.getOperationTypeCode())) {
                sum = sum.add(stats.getSum());
            }
        }
        return sum;
    }

}
