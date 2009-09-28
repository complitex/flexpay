package org.flexpay.eirc.sp.impl;

public interface Messager {
    void addMessage(String message);
    void addMessage(String message, Object o);
    void addMessage(String message, Object[] o);
}
