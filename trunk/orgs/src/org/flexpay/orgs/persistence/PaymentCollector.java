package org.flexpay.orgs.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class PaymentCollector extends OrganizationInstance<PaymentCollectorDescription, PaymentCollector> {

	private String email;
//    private org.jbpm.graph.exe.ProcessInstance tradingDayProcessInstance;
    private Long tradingDayProcessInstanceId;

	private Set<PaymentPoint> paymentPoints = Collections.emptySet();

	public PaymentCollector() {
	}

	public PaymentCollector(@NotNull Long id) {
		super(id);
	}

	public PaymentCollector(@NotNull Stub<PaymentCollector> stub) {
		super(stub.getId());
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

/*
    public ProcessInstance getTradingDayProcessInstance() {
        return tradingDayProcessInstance;
    }

    public void setTradingDayProcessInstance(ProcessInstance tradingDayProcessInstance) {
        this.tradingDayProcessInstance = tradingDayProcessInstance;
    }
*/

    public Long getTradingDayProcessInstanceId() {
        return tradingDayProcessInstanceId;
    }

    public void setTradingDayProcessInstanceId(Long tradingDayProcessInstanceId) {
        this.tradingDayProcessInstanceId = tradingDayProcessInstanceId;
    }

    public Set<PaymentPoint> getPaymentPoints() {
        return paymentPoints;
    }

    public void setPaymentPoints(Set<PaymentPoint> paymentPoints) {
        this.paymentPoints = paymentPoints;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("id", getId()).
                append("status", getStatus()).
                append("email", email).
                append("tradingDayProcessInstanceId", tradingDayProcessInstanceId).
                toString();
    }
}
