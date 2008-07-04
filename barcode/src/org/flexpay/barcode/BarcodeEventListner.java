package org.flexpay.barcode;

public interface BarcodeEventListner {
    public void barcodeEvent(String event);
    public void barcodeEvent(StringBuffer event);
}
