/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//RxTx
package rx.tx;

/**
 *
 * @author Hasith
 */

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;


public class RxTx implements SerialPortEventListener {
        
        PrintWriter writer;
        JLabel test;
 	SerialPort serialPort;
        CommPortIdentifier portId;
        /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = new String[4];
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results code page independent
	*/
	private BufferedReader input;
        private BufferedReader file_input;
	/** The output stream to the port */
	private OutputStream output;
        private OutputStream file_output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public void initialize(JLabel ststus, String comport) {
            
            PORT_NAMES[0] = "/dev/tty.usbserial-A9007UX1";
            PORT_NAMES[0] = "/dev/ttyACM0";
            PORT_NAMES[0] = "/dev/ttyUSB0";
            PORT_NAMES[0] = comport;
            
          
                // the next line is for Raspberry Pi and 
                // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
                //System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

		this.portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
                        ststus.setText("Could not find COM port.");
			return;
		} else {
                
                ststus.setText("Successfully connected to COM port.");
                }
                
                
                
                
                
                
                
	}
        
        
        
        
public void start (JLabel myj){

    try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
                        
		} catch (Exception e) {
			System.err.println(e.toString());
		}
    
            test=myj;
    
            Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
                
	t.start();
    
    
    
	}



	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close(JLabel status) {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
                        status.setText("COM port connection closed.");
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
            
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = this.input.readLine();
                                System.out.println(inputLine);
        
                                this.writer.println(inputLine);
        
                                this.test.setText(inputLine);
                      
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.

                

                
                

}
        
        
        
public void create_file(String f_name, String year, String status)
    throws FileNotFoundException
  {
    try
    {
      this.writer = new PrintWriter(f_name + "_" + year + "_" + status + ".csv", "UTF-8");
      this.writer.println("GSR_Value");
    }
    catch (UnsupportedEncodingException ex)
    {
      Logger.getLogger(RxTx.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public void close_file()
  {
    this.writer.close();
  }

      /*  
        
	public static void main(String[] args) throws Exception {
            
		RxTx main = new RxTx();
		main.initialize();
                
		Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 1000 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
			}
		};
                
		t.start();
                
		System.out.println("Started");
	} */
}