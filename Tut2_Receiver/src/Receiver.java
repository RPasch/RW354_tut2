
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Receiver {
    public static int portN = 8000;
    static InputStream inFromClient;
    static DataInputStream in;
    static Socket normalClientSocket = null;
    static ServerSocket normalServerSocket = null;
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ReceiverGUI gui  = new ReceiverGUI();
        gui.setEnabled(true);
        gui.setVisible(true);
        getTypOfMsg();
        
        
        
    }
    
    public static void getTypOfMsg() throws IOException, ClassNotFoundException{
        String code = "";
        try {
            
            normalServerSocket = new ServerSocket(portN);
            normalClientSocket = normalServerSocket.accept();
            inFromClient = normalClientSocket.getInputStream();
            in = new DataInputStream(inFromClient);
           
            
            
        } catch (Exception ex) {
            System.err.println("in getTypeOfMsg : " + ex);
        }
        
        
        
        try {
            code = in.readUTF();
            System.out.println(code+ " ---------------------------------");
        } catch (Exception ex) {
            System.err.println("in getTypeOfMsg : " + ex);
        }
         
        try {
           // in.close();
            //inFromClient.close();
           // normalClientSocket.close();
            //normalServerSocket.close();
        } catch (Exception ex) {
            System.err.println("Closing Initial thingies : " + ex);
            }
            
        if(code.equals("TCP")){
            
            CallTCP();
        } else if(code.equals("RBUDP")){
            CallRBUDP();
        }
        
        
    }
    
    
   public static void CallTCP() throws IOException, ClassNotFoundException{
       
       ReceiverTCP tcp = new ReceiverTCP();
   
   }
   
   
   public static void CallRBUDP(){
       ReceiverRBUDP udp = new ReceiverRBUDP();
   
   }
}
