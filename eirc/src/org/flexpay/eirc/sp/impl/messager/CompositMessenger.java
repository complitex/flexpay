package org.flexpay.eirc.sp.impl.messager;

import org.flexpay.eirc.sp.impl.MessageLevel;
import org.flexpay.eirc.sp.impl.Messenger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CompositMessenger implements Messenger {
    List<Messenger> messengers;

    private CompositMessenger() {
    }

    public CompositMessenger(@NotNull List<Messenger> messengers) {
        this.messengers = messengers;
    }

    public void addMessage(@NotNull String message) {
        for (Messenger messenger : messengers) {
            messenger.addMessage(message);
        }
    }

    public void addMessage(@NotNull String message, @NotNull MessageLevel level) {
        for (Messenger messenger : messengers) {
            messenger.addMessage(message, level);
        }
    }

    public void addMessage(@NotNull String message, Object o) {
        for (Messenger messenger : messengers) {
            messenger.addMessage(message, o);
        }
    }

    public void addMessage(@NotNull String message, Object o, @NotNull MessageLevel level) {
        for (Messenger messenger : messengers) {
            messenger.addMessage(message, o, level);
        }
    }

    public void addMessage(@NotNull String message, Object[] o) {
        for (Messenger messenger : messengers) {
            messenger.addMessage(message, o);
        }
    }

    public void addMessage(@NotNull String message, Object[] o, @NotNull MessageLevel level) {
        for (Messenger messenger : messengers) {
            messenger.addMessage(message, o, level);
        }
    }
}
