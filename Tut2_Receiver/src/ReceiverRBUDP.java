
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverRBUDP {
    public static String filename ;
    public static int filesize;
    static int port = 7999;
    public static int packetSize = 1024;
    public static DatagramSocket socket = null;
    public static DatagramPacket packet;
    static public byte[] fileAsBytes;
    static public ArrayList<DatagramPacket> packetlist = new ArrayList<DatagramPacket>();
    
    
    public ReceiverRBUDP() {
        receiveFile();
    }
    
    public static void receiveFile(){
        
        setName_Size();
        String cwd = System.getProperty("user.dir");
        File file = new File(cwd+"/"+filename);
        fileAsBytes = new byte[filesize];
        
        try {
            socket = new DatagramSocket(port);
            RBUDP_Thread t = new RBUDP_Thread();
            System.out.println("line 37 Receiver_RBUDP");

            t.start();
            
            //System.out.println(t.getName());
            //byte[] buffer = new byte[10000];
            //packet = new DatagramPacket(buffer, buffer.length);
            //socket.receive(packet);
            //String received = new String(packet.getData());
            
        } catch (Exception e) {
            System.err.println("could not create datagram socket : " + e);
        }
    }
    
    public static void setName_Size(){
        try {
            filename = Receiver.in.readUTF();
            filesize = Receiver.in.readInt();
            System.out.println("filename = " + filename + "    FileSize = " + filesize);

        } catch (IOException ex) {
            Logger.getLogger(ReceiverRBUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }
}
