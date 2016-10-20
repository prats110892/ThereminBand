package sample.listeners;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import sample.Main;

/**
 * Class to accept values coming from arduino through serial port
 * Created by batman on 19/10/16.
 */
public class ArduinoSerialPortListener implements SerialPortDataListener {
    private static final String PORT_NAME = "/dev/cu.usbmodem1411";

    private SerialPort mCommunicationPortWithArduino;
    private Main mApplication;

    private String mIncompleteData = "";

    public ArduinoSerialPortListener(Main application) {
        mApplication = application;
    }

    public boolean initialize() {
        mCommunicationPortWithArduino = SerialPort.getCommPort(PORT_NAME);
        mCommunicationPortWithArduino.openPort();
        mCommunicationPortWithArduino.setBaudRate(9600);
        mCommunicationPortWithArduino.addDataListener(this);
        System.out.println("Port: " + mCommunicationPortWithArduino.toString());
        return true;
    }

    public void destroy() {
        mCommunicationPortWithArduino.removeDataListener();
        mCommunicationPortWithArduino.closePort();
    }

    private void handleNewInstruction(String instruction) {
        mApplication.notifySerialInstructionReceived(instruction);
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            while (mCommunicationPortWithArduino.bytesAvailable() > 0) {
                byte[] readBuffer = new byte[mCommunicationPortWithArduino.bytesAvailable()];
                mCommunicationPortWithArduino.readBytes(readBuffer, mCommunicationPortWithArduino.bytesAvailable());
                String readData = mIncompleteData + new String(readBuffer);
                int numRead = readData.length();

                System.out.println("Read Data: " + readData);

                while (numRead > 3) {
                    String instruction = readData.substring(0, 3);
                    handleNewInstruction(instruction);
                    readData = readData.replaceFirst(instruction, "");
                    numRead -= readData.length();
                }

                if (numRead == 3) {
                    handleNewInstruction(readData);
                    readData = "";
                    mIncompleteData = "";
                    numRead -= readData.length();
                }

                if (numRead < 3 && numRead > 0) {
                    mIncompleteData = readData;
                }
            }
        }
    }
}
