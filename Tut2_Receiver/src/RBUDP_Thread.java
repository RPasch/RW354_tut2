
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.rmi.CORBA.Util;

public class RBUDP_Thread extends Thread{

    
            
    public RBUDP_Thread() {
        
        
    }
    
    @Override
    public void run(){
         
        int count = 0;
         int num_of_packets = (ReceiverRBUDP.filesize/ReceiverRBUDP.packetSize)+1;
         String missing_packets = "";
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(RBUDP_Thread.class.getName()).log(Level.SEVERE, null, ex);
//        }
                byte[] tempBytes ;//=new byte[ReceiverRBUDP.packetSize+4];
                DatagramPacket temp ;//= new DatagramPacket(tempBytes, tempBytes.length);
        while(true){
            try {
                //byte[] checkList = 
                //Thread.sleep(1000);
                tempBytes = new byte[ReceiverRBUDP.packetSize+4];
                temp = new DatagramPacket(tempBytes, tempBytes.length);
                ReceiverRBUDP.socket.receive(temp);
                byte[] seq = new byte[4];
                System.arraycopy(temp.getData(), 0, seq, 0, 4);
                int seqI = toInt(seq);
                if(seqI == -1){
                    System.out.println("MINUS ONE HAS BEEN RECEIVED");
                    //ReceiverRBUDP.socket.close();
                    break;  
                }
                ReceiverRBUDP.packetlist.add(temp);
                
                
                
                System.err.println(seqI);
                count++;    
                System.out.println(count +" COUNT ===========");
                if(count%5 == 0){
                    
                }
            } catch (Exception ex) {
                System.err.println("exception in RBUDP_thread : " + ex);
                
              }
            
            
        }
        System.out.println("\n----------\nI+ HAD TO STOP SENDING  \n-----------\n");
    
    }
   
    

//    public int convertByteToInt(byte[] b){           
//        int value= 0;
//        for(int i=0; i<b.length; i++)
//           value = (value << 4) | b[i];     
//        return value;       
//    }
    
     public int toInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
     }
}
