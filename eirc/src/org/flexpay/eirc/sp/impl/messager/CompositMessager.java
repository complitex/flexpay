package org.flexpay.eirc.sp.impl.messager;

import org.flexpay.eirc.sp.impl.Messager;
import org.flexpay.common.exception.FlexPayException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CompositMessager implements Messager {
    List<Messager> messagers;

    private CompositMessager() {
    }

    public CompositMessager(@NotNull List<Messager> messagers) {
        this.messagers = messagers;
    }

    public void addMessage(String message) {
        for (Messager messager : messagers) {
            messager.addMessage(message);
        }
    }

    public void addMessage(String message, Object o) {
        for (Messager messager : messagers) {
            messager.addMessage(message, o);
        }
    }

    public void addMessage(String message, Object[] o) {
        for (Messager messager : messagers) {
            messager.addMessage(message, o);
        }
    }
}
