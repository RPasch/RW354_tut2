
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverRBUDP {
    
    int port = 7999;
    DatagramSocket socket = null;
    DatagramPacket packet;
    
    public ReceiverRBUDP() {
        receiveFile();
    }
    
    public void receiveFile(){
        try {
            socket = new DatagramSocket(port);
            System.out.println("a");
            byte[] buffer = new byte[10000];
            packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("b");
            socket.receive(packet);
            System.out.println("c");
            String received = new String(packet.getData());
            System.out.println("d");
            System.out.println("received : " + received);
            
        } catch (Exception e) {
            System.err.println("could not create datagram socket : " + e);
        }
    }
    
}
