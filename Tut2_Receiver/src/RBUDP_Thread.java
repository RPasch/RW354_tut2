
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RBUDP_Thread extends Thread {

    /**
     *
     */
    public static byte[] completeFinalArray;
    public static ArrayList<Integer> missingElements = new ArrayList<>();

    public static int num_of_packets;
    public static boolean exitBool = true;

    public RBUDP_Thread() {

    }

    @Override
    public void run() {

        int count = 0;
        num_of_packets = (ReceiverRBUDP.filesize / ReceiverRBUDP.packetSize) + 1;
        ReceiverRBUDP.numberOfPackets = num_of_packets;
        String missing_packets = "";
        byte[] tempBytes = {};
        DatagramPacket temp = null;
        fillarrayList(num_of_packets);
        listenAndSend(tempBytes, temp);
        if (missingElements.isEmpty()) {
            System.out.println("ALL PACKETS WERE RECEIVED");
            orderBytes();
            createFile();
        }

    }

    public static int toInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static String findMissingElements(ArrayList<Integer> list) {
        String missingElementsString = "";

        return missingElementsString;
    }

    public static void removeListObjet(int seqI) {

        for (int k = missingElements.size() - 1; k >= 0; k--) {
            if (missingElements.get(k) == seqI) {
                missingElements.remove(k);
                ReceiverRBUDP.missingSize = missingElements.size();
            }

        }
    }

    public static void fillarrayList(int numOfPackets) {
        for (int i = 0; i < numOfPackets; i++) {
            missingElements.add(i);
        }
    }

    public static String listToString(ArrayList<Integer> list) {
        String stringList = "";
        for (int k : list) {
            stringList = stringList + k + ",";
        }
        stringList = stringList.substring(0, stringList.length() - 1);
        return stringList;
    }

    public static void listenAndSend(byte[] tempBytes, DatagramPacket temp) {
        String receivedStringList = "";
        ArrayList<Integer> receivedElements = new ArrayList<>();
        try {
            ReceiverRBUDP.socket.setSoTimeout(15);
        } catch (SocketException ex) {
            Logger.getLogger(RBUDP_Thread.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            try {
                tempBytes = new byte[ReceiverRBUDP.packetSize + 4];
                temp = new DatagramPacket(tempBytes, tempBytes.length);
                try {
                    ReceiverRBUDP.socket.receive(temp);
                } catch (SocketTimeoutException e) {
                    Receiver.out.writeUTF(receivedStringList);
                    if (missingElements.size() == 0) {
                        break;
                    } else {
                        listenAndSend(tempBytes, temp);
                        break;
                    }
                }
                byte[] seq = new byte[4];
                System.arraycopy(temp.getData(), 0, seq, 0, 4);
                int seqI = toInt(seq);
                receivedElements.add(seqI);
                receivedStringList = listToString(receivedElements);
                removeListObjet(seqI);
                ReceiverRBUDP.packetlist.add(temp);
            } catch (Exception ex) {
                Logger.getLogger(RBUDP_Thread.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }

    public static void orderBytes() {
        num_of_packets = (ReceiverRBUDP.filesize / ReceiverRBUDP.packetSize) + 1;
        byte[] tempCompleteArray = new byte[num_of_packets * ReceiverRBUDP.packetSize];
        byte[][] orderedArray = new byte[num_of_packets][ReceiverRBUDP.packetSize];
        completeFinalArray = new byte[ReceiverRBUDP.filesize];
        for (int i = 0; i < ReceiverRBUDP.packetlist.size(); i++) {
            byte[] temp = new byte[ReceiverRBUDP.packetSize];
            System.arraycopy(ReceiverRBUDP.packetlist.get(i).getData(), 4, temp, 0, ReceiverRBUDP.packetSize);
            byte[] current = ReceiverRBUDP.packetlist.get(i).getData();
            byte[] seq = new byte[4];
            System.arraycopy(current, 0, seq, 0, 4);
            int seqI = toInt(seq);
            orderedArray[seqI] = temp;
        }
        for (int i = 0; i < orderedArray.length; i++) {
            System.arraycopy(orderedArray[i], 0, tempCompleteArray, i * ReceiverRBUDP.packetSize, ReceiverRBUDP.packetSize);

        }
        System.arraycopy(tempCompleteArray, 0, completeFinalArray, 0, ReceiverRBUDP.filesize);
    }

    public static void createFile() {

        String cwd = System.getProperty("user.dir");
        File file = new File(cwd + "/" + ReceiverRBUDP.filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(completeFinalArray, 0, completeFinalArray.length);
            bos.flush();
        } catch (Exception e) {
            Logger.getLogger(RBUDP_Thread.class.getName()).log(Level.SEVERE, null, e);

        }

    }

    public static double getProgress() {

        double perc = 0.0;
        double nums = ReceiverRBUDP.numberOfPackets + 0.0;
        double miss = ReceiverRBUDP.missingSize + 0.0;
        perc = 1.0 - (miss / nums);
        return perc;
    }
}
