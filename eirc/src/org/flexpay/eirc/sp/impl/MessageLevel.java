package org.flexpay.eirc.sp.impl;

public class MessageLevel {
    private final static int ERROR_INT = 40000;
    private final static int WARN_INT  = 30000;
    private final static int INFO_INT  = 20000;

    public static final MessageLevel ERROR = new MessageLevel(ERROR_INT);
    public static final MessageLevel WARN = new MessageLevel(WARN_INT);
    public static final MessageLevel INFO = new MessageLevel(WARN_INT);

    private int level;

    private MessageLevel() {}

    public MessageLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageLevel that = (MessageLevel) o;

        return level == that.level;
    }

    @Override
    public int hashCode() {
        return level;
    }

    @Override
    public String toString() {
        return "level=" + String.valueOf(level);
    }
}
