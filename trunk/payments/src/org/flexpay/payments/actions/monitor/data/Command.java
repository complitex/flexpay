package org.flexpay.payments.actions.monitor.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.Transition;

public class Command {

    private Transition name;
    private Short value;

    public Command() {
    }

    public Command(Transition name, Short value) {
        this.name = name;
        this.value = value;
    }

    public Transition getName() {
        return name;
    }

    public void setName(Transition name) {
        this.name = name;
    }

    public Short getValue() {
        return value;
    }

    public void setValue(Short value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
                append("name", name).
                append("value", value).
                toString();
    }
}
