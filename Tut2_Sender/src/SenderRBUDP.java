import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderRBUDP {
    private final int port = 7999;
    private InetAddress address;
    private DatagramSocket socket = null;
    private DatagramPacket packet;
    private byte[] sendBuffer;
    ArrayList<DatagramPacket> packetList = new ArrayList<DatagramPacket>();
    private byte[] fileAsBytes;
    FileInputStream fis;
    int filesize = 0, numpackets;
    int PACKET_SIZE = 10000;
    
    public SenderRBUDP() {
        send();
    }
    
    public void send() {
        try {
            filesize = (int) Sender.file.length();
            numpackets = (filesize/PACKET_SIZE)+1;
            
            sendBuffer = "Hello, World!".getBytes();
            fileAsBytes = new byte[filesize];
            fis = new FileInputStream(Sender.file);
            fis.read(fileAsBytes);
            
            address = InetAddress.getByName(Sender.ipAddress);
            socket = new DatagramSocket();
            packet = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
            socket.send(packet);
            System.out.println("packet sent succesfully");
//            while(true){
//                socket.send(packet);
//            }
            breakUpFile();
            
        } catch (Exception e) {
            System.err.println("could not make RBUDP socket" + e);
        }
        
    }
    
    public void breakUpFile () {
        sendBuffer = new byte[PACKET_SIZE];
        
        for (int i = 0; i < numpackets-1; i++) {
            sendBuffer = Arrays.copyOfRange(fileAsBytes, i*PACKET_SIZE, (i+1)*PACKET_SIZE);
            
            byte[] tempBuffer = new byte[PACKET_SIZE + 4];
            BigInteger I = BigInteger.valueOf(i);
            System.arraycopy(I, 0, tempBuffer, 0, 4);
            System.arraycopy(sendBuffer, 0, tempBuffer, 4, sendBuffer.length);
            
            DatagramPacket tempPacket = new DatagramPacket(tempBuffer, tempBuffer.length, address, port);
            packetList.add(tempPacket);
        }
        
        sendBuffer = Arrays.copyOfRange(fileAsBytes, numpackets*PACKET_SIZE, filesize);
        
        byte[] tempBuffer = new byte[PACKET_SIZE + 4];
        BigInteger I = BigInteger.valueOf(numpackets-1);
        System.arraycopy(I, 0, tempBuffer, 0, 4);
        System.arraycopy(sendBuffer, 0, tempBuffer, 4, sendBuffer.length);
        
        DatagramPacket tempPacket = new DatagramPacket(tempBuffer, tempBuffer.length, address, port);
        packetList.add(tempPacket);
        
    }
    
    
}
