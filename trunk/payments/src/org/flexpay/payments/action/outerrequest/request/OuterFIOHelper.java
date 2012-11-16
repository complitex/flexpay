package org.flexpay.payments.action.outerrequest.request;

/**
 * @author Pavel Sknar
 *         Date: 16.11.12 9:52
 */
public abstract class OuterFIOHelper {

    public static String getOuterLastName(String data) {
        if (data == null) return "";
        return (data.length() > 2? data.substring(0, 2): data) + "**";
    }
}
