import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
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
    int PACKET_SIZE = 1024;
    
    public SenderRBUDP() {
        send();
    }
    
    public void send() {
        try {
            filesize = (int) Sender.file.length();
            numpackets = (filesize/PACKET_SIZE)+1;
            
            //sendBuffer = "Hello, World!".getBytes();
            fileAsBytes = new byte[filesize];
            fis = new FileInputStream(Sender.file);
            fis.read(fileAsBytes);
            
            address = InetAddress.getByName(Sender.ipAddress);
            socket = new DatagramSocket();
            //packet = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
            //socket.send(packet);
            //System.out.println("packet sent succesfully");
//            while(true){
//                socket.send(packet);
//            }
            Sender.out.writeUTF(Sender.file.getName());
            Sender.out.writeInt((int) Sender.file.length());

            breakUpFile();
            
            
            Thread.sleep(1000);
            sendPackets();
            
        } catch (Exception e) {
            //System.err.println("could not make RBUDP socket : " + e);
            Logger.getLogger(SenderRBUDP.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
    
    public void breakUpFile () {
        sendBuffer = new byte[PACKET_SIZE];
        
        System.out.println("numpackets : "+numpackets);
        
        for (int i = 0; i < numpackets-1; i++) {
            //System.out.println("ITERATION : " + i+" ");
            sendBuffer = Arrays.copyOfRange(fileAsBytes, i*PACKET_SIZE, (i+1)*PACKET_SIZE);
            //System.out.println("a");
            byte[] tempBuffer = new byte[PACKET_SIZE + 4];
            //System.out.println("b");
            //BigInteger I = BigInteger.valueOf(i);
            //System.out.println("c : "+ I.toByteArray());
            //System.out.println("cc : "+(byte)2);
            byte[] sequence = toBytes(i);
            System.arraycopy(sequence, 0, tempBuffer, 0, 4);
            //System.out.println("d");
            System.arraycopy(sendBuffer, 0, tempBuffer, 4, sendBuffer.length);
            //System.out.println("e");
            
            DatagramPacket tempPacket = new DatagramPacket(tempBuffer, tempBuffer.length, address, port);
            //DatagramPacket tempPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
            //System.out.println("f");
            packetList.add(tempPacket);
            //System.out.println("g");
        }
        
        //System.out.println("bjkhbjbhjbh");
        
        sendBuffer = Arrays.copyOfRange(fileAsBytes, (numpackets-1)*PACKET_SIZE, filesize);
        
        //System.out.println("herro");
        
        byte[] tempBuffer = new byte[PACKET_SIZE + 4];
        //BigInteger I = BigInteger.valueOf(numpackets-1);
        byte[] sequence = toBytes(numpackets-1);//I.toByteArray();
        System.arraycopy(sequence, 0, tempBuffer, 0, 4);
        System.arraycopy(sendBuffer, 0, tempBuffer, 4, sendBuffer.length);
        
        DatagramPacket tempPacket = new DatagramPacket(tempBuffer, tempBuffer.length, address, port);
        //DatagramPacket tempPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
        packetList.add(tempPacket);
        
    }
    
    byte[] toBytes(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);

        return result;
    }
    
    public void sendPackets(){ //make this recursive w/ arguments being packets to send
        
        for (DatagramPacket dp : packetList) {
            try {
                socket.send(dp);
                byte[] seq = new byte[4];
                System.arraycopy(dp.getData(), 0, seq, 0, 4);
                int seqInt = convertByteToInt(seq);
                System.err.println("AT : "+seqInt);
            } catch (IOException ex) {
                Logger.getLogger(SenderRBUDP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Sender.out.writeUTF("STOP");
        
        try {
            //byte[] temp1 = toBytes(-3);
            byte[] bb = {-1, -1, -1, -1};
            //int temp2 = convertByteToInt(temp1);
            //System.out.println("HHHHHHH : "+temp1[0]+" | "+temp1[1]+" | "+temp1[2]+" | "+temp1[3]);
            //System.out.println("HHHHHHH : "+temp2);

            //Thread.sleep(2000);
            //Sender.out.writeUTF("STOP");
            byte[] tempBuffer = new byte[PACKET_SIZE+4];
            System.arraycopy(bb, 0, tempBuffer, 0, 4);
            DatagramPacket tempDP = new DatagramPacket(tempBuffer, tempBuffer.length, address, port);
            socket.send(tempDP);
            socket.send(tempDP);
            socket.send(tempDP);
            socket.send(tempDP);
            socket.send(tempDP);
        } catch (Exception e) {
            System.err.println("could not say stop : "+e);
        }
        
        System.out.println("sending finished");
    }
    
    public int convertByteToInt(byte[] b) {
//        int value = 0;
//        for (int i = 0; i < b.length; i++) {
//            value = (value << 8) | b[i];
//        }
//        return value;
        return ByteBuffer.wrap(b).getInt();
    }
    
}
