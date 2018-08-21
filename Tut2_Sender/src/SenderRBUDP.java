
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
import java.util.Scanner;
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
    int PACKET_SIZE = 16384;

    public SenderRBUDP() {
        send();
    }

    public void send() {
        try {
//            filesize = (int) Sender.file.length();
            numpackets = (filesize / PACKET_SIZE) + 1;
            
            fileAsBytes = new byte[filesize];
//            fis = new FileInputStream(Sender.file);
            fis.read(fileAsBytes);

            address = InetAddress.getByName(Sender.ipAddress);
            socket = new DatagramSocket();
            
//            Sender.out.writeUTF(Sender.file.getName());
//            Sender.out.writeInt((int) Sender.file.length());

            breakUpFile();

            Thread.sleep(100);
            sendPackets();

        } catch (Exception e) {
            Logger.getLogger(SenderRBUDP.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public void breakUpFile() {
        sendBuffer = new byte[PACKET_SIZE];

        System.out.println("numpackets : " + numpackets);

        for (int i = 0; i < numpackets - 1; i++) {
            
            System.arraycopy(fileAsBytes, i * PACKET_SIZE, sendBuffer, 0, PACKET_SIZE);
            byte[] tempBuffer = new byte[PACKET_SIZE + 4];
            byte[] sequence = toBytes(i);
            System.arraycopy(sequence, 0, tempBuffer, 0, 4);
            System.arraycopy(sendBuffer, 0, tempBuffer, 4, sendBuffer.length);

            DatagramPacket tempPacket = new DatagramPacket(tempBuffer, tempBuffer.length, address, port);
            packetList.add(tempPacket);
        }

        sendBuffer = Arrays.copyOfRange(fileAsBytes, (numpackets - 1) * PACKET_SIZE, filesize);

        byte[] tempBuffer = new byte[PACKET_SIZE + 4];
        byte[] sequence = toBytes(numpackets - 1);//I.toByteArray();
        System.arraycopy(sequence, 0, tempBuffer, 0, 4);
        System.arraycopy(sendBuffer, 0, tempBuffer, 4, sendBuffer.length);

        DatagramPacket tempPacket = new DatagramPacket(tempBuffer, tempBuffer.length, address, port);
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

    public void sendPackets() {

        for (DatagramPacket dp : packetList) {
            try {
                socket.send(dp);
                byte[] seq = new byte[4];
                System.arraycopy(dp.getData(), 0, seq, 0, 4);
                int seqInt = convertByteToInt(seq);
                System.out.println("AT : " + seqInt);
            } catch (IOException ex) {
                Logger.getLogger(SenderRBUDP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            String receivedPacketNums = Sender.in.readUTF();
            System.out.println("THESE WERE RECEIVED : " + receivedPacketNums);

            removeFromPacketList(receivedPacketNums);
            if (packetList.size() > 0) {
                System.out.println("THIS IS PACKET SIZE : "+packetList.size());
                sendPackets();

            } else {
                System.out.println("ALL PACKETS RECEIVED");
                
            }

        } catch (Exception e) {
            System.err.println("could not get list of missing packets : " + e);
        }

    }

    public int convertByteToInt(byte[] b) {
        return ByteBuffer.wrap(b).getInt();
    }

    public void removeFromPacketList(String list) {
        Scanner sc = new Scanner(list);
        sc.useDelimiter(",");

        while (sc.hasNext()) {
            int item = Integer.parseInt(sc.next());

            for (int i = packetList.size() - 1; i >= 0; i--) {
                byte[] seqBytes = new byte[4];
                seqBytes[0] = packetList.get(i).getData()[0];
                seqBytes[1] = packetList.get(i).getData()[1];
                seqBytes[2] = packetList.get(i).getData()[2];
                seqBytes[3] = packetList.get(i).getData()[3];

                int seqInt = convertByteToInt(seqBytes);
                if (seqInt == item) {
                    packetList.remove(i);
                }
            }
        }
    }

}
