package org.flexpay.jar;// derived from SUN's examples in the javax.comm package
import java.io.*;
import java.util.*;
//import javax.comm.*; // for SUN's serial/parallel port libraries
import gnu.io.*; // for rxtxSerial library

public class JarTest implements Runnable, SerialPortEventListener {

    static CommPortIdentifier portId;
   static CommPortIdentifier saveportId;
   static Enumeration        portList;
   InputStream           inputStream;
   SerialPort           serialPort;
   Thread           readThread;

   static String        messageString = "Hello, world!";
   static OutputStream      outputStream;
   static boolean        outputBufferEmptyFlag = false;

   public static void main(String[] args) {
      boolean           portFound = false;
      String           defaultPort;

      // determine the name of the serial port on several operating systems
      String osname = System.getProperty("os.name","").toLowerCase();
      if ( osname.startsWith("windows") ) {
         // windows
         defaultPort = "COM1";
      } else if (osname.startsWith("linux")) {
         // linux
        defaultPort = "/dev/ttyS0";
      } else if ( osname.startsWith("mac") ) {
         // mac
         defaultPort = "????";
      } else {
         System.out.println("Sorry, your operating system is not supported");
         return;
      }

      if (args.length > 0) {
         defaultPort = args[0];
      }

      System.out.println("Set default port to "+defaultPort);

		// parse ports and if the default port is found, initialized the reader
      portList = CommPortIdentifier.getPortIdentifiers();
      while (portList.hasMoreElements()) {
         portId = (CommPortIdentifier) portList.nextElement();
         if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            if (portId.getName().equals(defaultPort)) {
               System.out.println("Found port: "+defaultPort);
               portFound = true;
               // init reader thread
               JarTest reader = new JarTest();
            }
         }

      }
      if (!portFound) {
         System.out.println("port " + defaultPort + " not found.");
      }

   }

   public void initwritetoport() {
      // initwritetoport() assumes that the port has already been opened and
      //    initialized by "public nulltest()"

      try {
         // get the outputstream
         outputStream = serialPort.getOutputStream();
      } catch (IOException e) {}

      try {
         // activate the OUTPUT_BUFFER_EMPTY notifier
         serialPort.notifyOnOutputEmpty(true);
      } catch (Exception e) {
         System.out.println("Error setting event notification");
         System.out.println(e.toString());
         System.exit(-1);
      }

   }

   public void writetoport() {
      System.out.println("Writing \""+messageString+"\" to "+serialPort.getName());
      try {
         // write string to serial port
         outputStream.write(messageString.getBytes());
      } catch (IOException e) {}
   }

   public JarTest() {
      // initalize serial port
      try {
         serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
      } catch (PortInUseException e) {}

      try {
         inputStream = serialPort.getInputStream();
      } catch (IOException e) {}

      try {
         serialPort.addEventListener(this);
      } catch (TooManyListenersException e) {}

      // activate the DATA_AVAILABLE notifier
      serialPort.notifyOnDataAvailable(true);

      try {
         // set port parameters
         serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
                     SerialPort.STOPBITS_1,
                     SerialPort.PARITY_NONE);
      } catch (UnsupportedCommOperationException e) {}

      // start the read thread
      readThread = new Thread(this);
      readThread.start();

   }

   public void run() {
      // first thing in the thread, we initialize the write operation
      initwritetoport();
      try {
          //noinspection InfiniteLoopStatement
          while (true) {
            // write string to port, the serialEvent will read it
//            writetoport();
            Thread.sleep(1000);
         }
      } catch (InterruptedException e) {}
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
            String result  = new String(readBuffer);
            System.out.println("Read: "+result);
         } catch (IOException e) {}

         break;
      }
   }

}
