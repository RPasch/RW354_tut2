import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SenderTCP {
    private static String ipAddress = "";
    //private static OutputStream OutToReceiver;
    private static ObjectOutputStream Out;
    //private static Socket socket = null;
    
    public SenderTCP() {
        sendObject();
    }
    
    private static void sendObject() {
        
        try {
            //Sender.socket = new Socket(Sender.ipAddress, 8000);
            //OutToReceiver = Sender.socket.getOutputStream();
            Out = new ObjectOutputStream(Sender.initOutToServer);
            
            Out.writeObject(Sender.file);
        } catch (Exception e) {
            System.err.println("could not send via TCP : " + e);
        }
        
    }
    
}
