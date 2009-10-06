package org.flexpay.eirc.sp.impl.messenger;

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

	@Override
    public void addMessage(@NotNull String message) {
        for (Messenger messenger : messengers) {
            messenger.addMessage(message);
        }
    }

	@Override
    public void addMessage(@NotNull String message, @NotNull MessageLevel level) {
        for (Messenger messenger : messengers) {
            messenger.addMessage(message, level);
        }
    }

	@Override
    public void addMessage(@NotNull String message, Object o) {
        for (Messenger messenger : messengers) {
            messenger.addMessage(message, o);
        }
    }

	@Override
    public void addMessage(@NotNull String message, Object o, @NotNull MessageLevel level) {
        for (Messenger messenger : messengers) {
            messenger.addMessage(message, o, level);
        }
    }

	@Override
    public void addMessage(@NotNull String message, Object[] o) {
        for (Messenger messenger : messengers) {
            messenger.addMessage(message, o);
        }
    }

	@Override
    public void addMessage(@NotNull String message, Object[] o, @NotNull MessageLevel level) {
        for (Messenger messenger : messengers) {
            messenger.addMessage(message, o, level);
        }
    }

}
