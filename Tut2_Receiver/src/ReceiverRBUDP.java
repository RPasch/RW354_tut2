
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 18214304
 */
public class ReceiverRBUDP {

    /**
     *
     */
    public static String filename;

    /**
     *
     */
    public static int filesize;
    static int port = 7999;

    /**
     *
     */
    public static int packetSize = 64000;

    /**
     *
     */
    public static DatagramSocket socket = null;

    /**
     *
     */
    public static DatagramPacket packet;

    /**
     *
     */
    static public byte[] fileAsBytes;

    /**
     *
     */
    static public ArrayList<DatagramPacket> packetlist = new ArrayList<DatagramPacket>();

    /**
     *
     */
    public static boolean stopSending = true;

    /**
     *
     */
    public static int missingSize = 1;

    /**
     *
     */
    public static int numberOfPackets = 0;

    /**
     *
     */
    public ReceiverRBUDP() {
        receiveFile();
    }

    /**
     *
     */
    public static void receiveFile() {

        setName_Size();
        ReceiverGUI.updateTextArea("Receiving " + filename);

        String cwd = System.getProperty("user.dir");
        File file = new File(cwd + "/" + filename);
        fileAsBytes = new byte[filesize];

        try {
            socket = new DatagramSocket(port);
            RBUDP_Thread t = new RBUDP_Thread();

            t.start();

            Receiver.gui.updateProgressBar();
        } catch (Exception e) {
            System.err.println("could not create datagram socket : " + e);
        }
    }

    /**
     *
     */
    public static void setName_Size() {
        try {
            filename = Receiver.in.readUTF();
            filesize = Receiver.in.readInt();
        } catch (IOException ex) {
            Logger.getLogger(ReceiverRBUDP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
