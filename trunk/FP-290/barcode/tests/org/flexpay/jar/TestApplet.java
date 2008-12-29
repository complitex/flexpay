package org.flexpay.jar;

import gnu.io.*;

import javax.swing.*;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


public class TestApplet extends JApplet implements Runnable, SerialPortEventListener {

    private static CommPortIdentifier portId;
    private static CommPortIdentifier saveportId;
    private static Enumeration portList;
    private InputStream inputStream;
    private SerialPort serialPort;
    private Thread readThread;

    private static String messageString = "Hello, world!";
    private static OutputStream outputStream;
    private static boolean outputBufferEmptyFlag = false;

    public void init() {
        boolean portFound = false;
        String defaultPort;

        // determine the name of the serial port on several operating systems
        String osname = System.getProperty("os.name", "").toLowerCase();
        if (osname.startsWith("windows")) {
            // windows
            defaultPort = "COM1";
        } else if (osname.startsWith("linux")) {
            // linux
            defaultPort = "/dev/ttyS0";
        } else if (osname.startsWith("mac")) {
            // mac
            defaultPort = "????";
        } else {
            System.out.println("Sorry, your operating system is not supported");
            return;
        }

//        if (args.length > 0) {
//           defaultPort = args[0];
//        }

        System.out.println("Set default port to " + defaultPort);
        // parse ports and if the default port is found, initialized the reader
        portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(defaultPort)) {
                    System.out.println("Found port: " + defaultPort);
                    portFound = true;
                    // init reader thread
//                 JarTest reader = new JarTest();
                    try {
                        serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
                    } catch (PortInUseException e) {
                    }

                    try {
                        inputStream = serialPort.getInputStream();
                    } catch (IOException e) {
                    }

                    try {
                        serialPort.addEventListener(this);
                    } catch (TooManyListenersException e) {
                    }

                    // activate the DATA_AVAILABLE notifier
                    serialPort.notifyOnDataAvailable(true);

                    try {
                        // set port parameters
                        serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException e) {
                    }

                    // start the read thread
                    readThread = new Thread(this);
                    readThread.start();
                }
            }
        }
        if (!portFound) {
            System.out.println("port " + defaultPort + " not found.");
        }
    }

    public void start() {

    }


    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
        }

    }

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
                // we get here if data has been received
                byte[] readBuffer = new byte[20];
                try {
                    // read data
                    while (inputStream.available() > 0) {
                        int numBytes = inputStream.read(readBuffer);
                    }
                    // print data
                    String result = new String(readBuffer);
                    System.out.println("Read: " + result);
                } catch (IOException e) {
                }
                break;
        }
    }
}
