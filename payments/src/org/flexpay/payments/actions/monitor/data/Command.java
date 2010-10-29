package org.flexpay.payments.actions.monitor.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import static org.flexpay.payments.util.PaymentCollectorTradingDayConstants.Transitions;

public class Command {

    private Transitions name;
    private Short value;

    public Command() {
    }

    public Command(Transitions name, Short value) {
        this.name = name;
        this.value = value;
    }

    public Transitions getName() {
        return name;
    }

    public void setName(Transitions name) {
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
