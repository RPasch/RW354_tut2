
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

        while(true){
            try {

                byte[] tempBytes = new byte[ReceiverRBUDP.packetSize+4];
                DatagramPacket temp = new DatagramPacket(tempBytes, tempBytes.length);
                ReceiverRBUDP.socket.receive(temp);
                ReceiverRBUDP.packetlist.add(temp);
                byte[] seq = new byte[4];
                System.arraycopy(temp.getData(), 0, seq, 0, 4);
                int seqI = toInt(seq);
                
                
                
                System.err.println(seqI);
                count++;    
                System.out.println(count +" COUNT ===========");
                if(count%5 == 0){
                    
                }
            } catch (Exception ex) {
                System.err.println("exception in RBUDP_thread : " + ex);
                
              }
            
            
        }
    
    }
   
    

    public int convertByteToInt(byte[] b){           
        int value= 0;
        for(int i=0; i<b.length; i++)
           value = (value << 4) | b[i];     
        return value;       
    }
    
     public int toInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
     }
}
