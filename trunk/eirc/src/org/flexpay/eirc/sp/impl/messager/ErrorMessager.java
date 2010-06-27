package org.flexpay.eirc.sp.impl.messager;

import org.flexpay.eirc.sp.impl.Messager;
import org.slf4j.Logger;

public class ErrorMessager implements Messager {
    private Logger log;

    private ErrorMessager() {
    }

    public ErrorMessager(Logger log) {
        this.log = log;
    }

    public void addMessage(String message) {
        log.error(message);
    }

    public void addMessage(String message, Object o) {
        log.error(message, o);
    }

    public void addMessage(String message, Object[] o) {
        log.error(message, o);
    }
}
