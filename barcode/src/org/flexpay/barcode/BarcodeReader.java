package org.flexpay.barcode;

import gnu.io.*;

import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

public class BarcodeReader implements Runnable, SerialPortEventListener {

    /**
     * Port input stream
     */
    private InputStream inputStream;

    /**
     * Is reader thread rinnung
     */
    private boolean isRunning;

    /**
     * BarcodeEventListner, only one :)
     */
    private BarcodeEventListner barcodeEventListner = null;

    /**
     * Main BarcodeReader thread
     */
    public void run() {
        try {
            initBarcodeReader();
            isRunning = true;
            while (isRunning){
                Thread.sleep(1000);
            }
        } catch (TooManyListenersException e) {
            System.out.println("BarcodeReader: too many listeners. Can't init barcode reader");
            e.printStackTrace();
        } catch (PortInUseException e) {
            System.out.println("BarcodeReader: port in use.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("BarcodeReader Thread was interrupted.");
            e.printStackTrace();
        }
    }

    /**
     * Return default port name depending on OS
     * @return port name
     */
    private String getDefaultPortName(){
        String osname = System.getProperty("os.name", "").toLowerCase();
        if (osname.startsWith("windows")) {
            // windows
            return "COM1";
        } else if (osname.startsWith("linux")) {
            // linux
            return "/dev/ttyS0";
        } else if (osname.startsWith("mac")) {
            // mac
            return "????";
        } else {
            System.out.println("Sorry, your operating system is not supported");
            return null;
        }

    }

    /**
     * initialize barcode reader
     * @throws TooManyListenersException when too many listenrs on port
     * @throws PortInUseException when port in use
     */
    public void initBarcodeReader() throws TooManyListenersException, PortInUseException {
        initBarcodeReader(getDefaultPortName());
    }

    /**
     * initialize barcode reader
     * @param portName desired port name
     * @throws TooManyListenersException when too many listenrs on port
     * @throws PortInUseException when port in use
     */
    public void initBarcodeReader(String portName) throws TooManyListenersException, PortInUseException {

        System.out.println("Initializing BarcodeReader.");
        boolean portFound = false;
//        String defaultPort = getDefaultPortName();

        System.out.println("Set default port to " + portName);

        // parse ports and if the default port is found, initialized the reader
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(portName)) {
                    System.out.println("Found port: " + portName);
                    portFound = true;
                    SerialPort serialPort = (SerialPort) portId.open("FlexPayBarcodeReader", 2000);

                    try {
                        inputStream = serialPort.getInputStream();
                    } catch (IOException e) {
                        System.out.println("Can't get input stream.");
                        e.printStackTrace();
                    }

                    serialPort.addEventListener(this);
                    // activate the DATA_AVAILABLE notifier
                    serialPort.notifyOnDataAvailable(true);
                    try {
                        // set port parameters
                        serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException e) {
                        System.out.println("Can't init serial port.");
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!portFound) {
            System.out.println("port " + portName + " not found.");
        }
        System.out.println("Initialization complite.");
    }

    /**
     * Recive serial port event
     * @param event serial port event
     */
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                try {
                    if (inputStream.available()>0){
                        System.out.println("BarcodeReader: Not empty Event recived");
                        byte buffer[] = new byte[inputStream.available()];
                        inputStream.read(buffer);
                        barcodeEventListner.barcodeEvent(new String(buffer));
                        System.out.println("BarcodeReader: Event string " + new String(buffer));
                    }
                } catch (IOException e) {
                    System.out.println("Can't read form input stream.");
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * Stop BarcodeRader thread
     */
    public void stop(){
        isRunning = false;
    }

    /**
     *
     * @return true if BarcodeReader thread is running
     */
    public boolean isRunning(){
        return isRunning;
    }

    /**
     * Add BarcodeReaderEventListner
     * @param barcodeEventListner BarcodeEventListner
     * @throws TooManyListenersException when too many barcodeEventListners
     */
    public void addEventListener(BarcodeEventListner barcodeEventListner) throws TooManyListenersException {
        if (this.barcodeEventListner != null){
            throw new TooManyListenersException("Too many BarcodeEventListners");
        }else{
            this.barcodeEventListner = barcodeEventListner;
        }
    }
}
