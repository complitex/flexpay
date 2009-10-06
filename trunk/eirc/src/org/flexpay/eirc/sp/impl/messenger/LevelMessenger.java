package org.flexpay.eirc.sp.impl.messenger;

import org.flexpay.eirc.sp.impl.MessageLevel;
import org.flexpay.eirc.sp.impl.Messenger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelMessenger implements Messenger {

    private Logger innerLogger = LoggerFactory.getLogger(LevelMessenger.class);
    private Logger log;
    private MessageLevel defaultLevel;

    public LevelMessenger(@NotNull Logger log, @NotNull MessageLevel defaultLevel) {
        this.defaultLevel = defaultLevel;
        this.log = log;
    }

	@Override
    public void addMessage(@NotNull String message) {
        addMessage(message, defaultLevel);
    }

	@Override
    public void addMessage(@NotNull String message, @NotNull MessageLevel level) {
        if (level.equals(MessageLevel.ERROR)) {
            log.error(message);
        } else if (level.equals(MessageLevel.WARN)) {
            log.warn(message);
        } else if (level.equals(MessageLevel.INFO)) {
            log.info(message);
        } else {
            innerLogger.warn("MessageLevel {} was not defined. Using {}", new Object[]{level, MessageLevel.WARN});
            log.warn(message);
        }
    }

	@Override
    public void addMessage(@NotNull String message, Object o) {
        addMessage(message, o, defaultLevel);
    }

	@Override
    public void addMessage(@NotNull String message, Object o, @NotNull MessageLevel level) {
        if (level.equals(MessageLevel.ERROR)) {
            log.error(message, o);
        } else if (level.equals(MessageLevel.WARN)) {
            log.warn(message, o);
        } else if (level.equals(MessageLevel.INFO)) {
            log.info(message, o);
        } else {
            innerLogger.warn("MessageLevel {} was not defined. Using {}", new Object[]{level, MessageLevel.WARN});
            log.warn(message, o);
        }
    }

	@Override
    public void addMessage(@NotNull String message, Object[] o) {
        addMessage(message, o, defaultLevel);
    }

	@Override
    public void addMessage(@NotNull String message, Object[] o, @NotNull MessageLevel level) {
        if (level.equals(MessageLevel.ERROR)) {
            log.error(message, o);
        } else if (level.equals(MessageLevel.WARN)) {
            log.warn(message, o);
        } else if (level.equals(MessageLevel.INFO)) {
            log.info(message, o);
        } else {
            innerLogger.warn("MessageLevel {} was not defined. Using {}", new Object[]{level, MessageLevel.WARN});
            log.warn(message, o);
        }
    }

}
