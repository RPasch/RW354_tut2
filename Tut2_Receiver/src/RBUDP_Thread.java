
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.rmi.CORBA.Util;

public class RBUDP_Thread extends Thread {

    /**
     *
     */
    public static byte[] completeFinalArray ;
    public static ArrayList<Integer> missingElements = new ArrayList<>();
        //public static ArrayList<Integer> missingElements = new ArrayList<>();

    public static int num_of_packets;

    //public static ArrayList<Integer> receivedElements = new ArrayList<>();
    //public static String receivedStringList = "";
    public RBUDP_Thread() {

    }

    @Override
    public void run() {

        int count = 0;
        num_of_packets = (ReceiverRBUDP.filesize / ReceiverRBUDP.packetSize) + 1;
        String missing_packets = "";
//        
        byte[] tempBytes = {};//=new byte[ReceiverRBUDP.packetSize+4];
        DatagramPacket temp = null;//= new DatagramPacket(tempBytes, tempBytes.length);
        fillarrayList(num_of_packets);

        listenAndSend(tempBytes, temp);

        System.out.println("\n----------\n I HAD TO STOP SENDING  \n-----------\n");

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
                System.out.println("missing element.size() = " + missingElements.size());
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
        System.out.println("\n ---------------------\n\n ---------------------\n\n ---------------------\n\n ---------------------\n");
        while (true) {
            try {
                //byte[] checkList = 
                //Thread.sleep(1000);
                tempBytes = new byte[ReceiverRBUDP.packetSize + 4];
                temp = new DatagramPacket(tempBytes, tempBytes.length);
                ReceiverRBUDP.socket.receive(temp);

                byte[] seq = new byte[4];
                System.arraycopy(temp.getData(), 0, seq, 0, 4);
                int seqI = toInt(seq);
                if (seqI == -1) {
                    System.out.println("MINUS ONE HAS BEEN RECEIVED");
                    Receiver.out.writeUTF(receivedStringList);
                    listenAndSend(tempBytes, temp);
                    
                }
                receivedElements.add(seqI);
                receivedStringList = listToString(receivedElements);

                removeListObjet(seqI);
                ReceiverRBUDP.packetlist.add(temp);
                System.err.println(seqI);

                if (missingElements.isEmpty()) {
                    System.out.println("ALL PACKETS WERE RECEIVED");
                    orderBytes();
                    createFile();
                }


            } catch (Exception ex) {
                Logger.getLogger(RBUDP_Thread.class.getName()).log(Level.SEVERE, null, ex);
                //System.err.println("exception in RBUDP_thread : " + ex);

            }

        }

    }

    public static void orderBytes(){
        num_of_packets = (ReceiverRBUDP.filesize / ReceiverRBUDP.packetSize) + 1;
        byte[] tempCompleteArray = new byte[num_of_packets*ReceiverRBUDP.filesize];
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
        for(int i = 0 ; i < orderedArray.length ; i++){
            System.arraycopy(orderedArray[i],0, tempCompleteArray, i*ReceiverRBUDP.packetSize, ReceiverRBUDP.packetSize);

        }
        System.arraycopy(tempCompleteArray, 0, completeFinalArray,0, ReceiverRBUDP.filesize);
    }
    
    
    
    public static void createFile() {
       
        String cwd = System.getProperty("user.dir");
        File file = new File(cwd + "/" + ReceiverRBUDP.filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(completeFinalArray, 0 , completeFinalArray.length);
            bos.flush();
        } catch (Exception e) {
            Logger.getLogger(RBUDP_Thread.class.getName()).log(Level.SEVERE, null, e);

        }
        
        
    }
}
