package org.flexpay.barcode;

import javax.swing.*;
import java.awt.*;
import java.util.TooManyListenersException;

public class BarcodeReaderApplet extends JApplet implements BarcodeEventListner {

    private BarcodeReader barcodeReader;
    private StringBuffer buffer = new StringBuffer();
    private Thread barcodeReaderThread = null;
    private boolean newInfoRecived = false;

    public BarcodeReaderApplet(){
        barcodeReader = new BarcodeReader();
    }


    public synchronized void barcodeEvent(String event) {
        System.out.println(" Event recived " +event);
        buffer.append(event);
        newInfoRecived = true;
    }

    public synchronized void barcodeEvent(StringBuffer event) {
        System.out.println(" Event recived " +event);
        buffer.append(event);
        newInfoRecived = true;
    }

    public synchronized StringBuffer getStringBuffer(){
        System.out.println("Buffer returned");
        StringBuffer resultBuffer = new StringBuffer();
        resultBuffer.append(buffer);
        buffer = new StringBuffer();
        newInfoRecived = false;
        return resultBuffer;
    }

    public void start(){
        try {
            barcodeReader.addEventListener(this);
            barcodeReaderThread = new Thread(barcodeReader);
            barcodeReaderThread.start();
        } catch (TooManyListenersException e) {
            System.out.println("Can't start barcode reader.");
            e.printStackTrace();
        } catch (RuntimeException e){
            System.out.println("Can't initialize barcode reader.");
            e.printStackTrace();
        }
    }

    public void init(){

    }

    public void destroy(){
        barcodeReader.stop();
        try {
            barcodeReaderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        barcodeReaderThread = null;
        barcodeReader = null;

    }

    public void paint(Graphics g){
        //todo paint flexpay icon as applet
        //Image image = getImage(getCodeBase(), "imgDir/a.gif");
    }

    public boolean isNewInfoRecived() {
        return newInfoRecived;
    }
}
