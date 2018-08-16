import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderRBUDP {
    private final int port = 7999;
    private InetAddress address;
    private DatagramSocket socket = null;
    private DatagramPacket packet;
    private byte[] sendBuffer = new byte[10000];
    
    public SenderRBUDP() {
        send();
    }
    
    public void send() {
        try {
            sendBuffer = "Hello, World!".getBytes();
            address = InetAddress.getByName(Sender.ipAddress);
            socket = new DatagramSocket();
            packet = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
            socket.send(packet);
            System.out.println("packet sent succesfully");
            while(true){
                socket.send(packet);
            }
            
        } catch (Exception e) {
            System.err.println("could not make RBUDP socket" + e);
        }
        
    }
    
    
}
