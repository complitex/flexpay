package org.flexpay.eirc.sp.impl.messager;

import org.flexpay.eirc.sp.impl.Messager;
import org.slf4j.Logger;

public class WarningMessager implements Messager {
    private Logger log;

    private WarningMessager() {
    }

    public WarningMessager(Logger log) {
        this.log = log;
    }

    public void addMessage(String message) {
        log.warn(message);
    }

    public void addMessage(String message, Object o) {
        log.warn(message, o);
    }

    public void addMessage(String message, Object[] o) {
        log.warn(message, o);
    }
}
