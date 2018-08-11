
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sender {
    public static String ipAddress = "";
    public static File file = null;
    public static String protocol = "";
    public static Socket socket = null;
    static OutputStream initOutToServer;
    static DataOutputStream initOut;
    
    public static void main(String[] args) {
        SenderGUI gui = new SenderGUI();
        gui.setEnabled(true);
        gui.setVisible(true);
        
        
    }
    
    public static void initialConnect() {
        try {
            socket = new Socket(ipAddress, 8000);
            initOutToServer = socket.getOutputStream();
            initOut = new DataOutputStream(initOutToServer);
            
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void sendProtocol() {
        try {
            initOut.writeUTF(protocol);
            //initOutToServer.close();
            //initOut.close();
            //initialSocket.close();
        } catch (IOException ex) {
            System.err.println("could not send protocol");
        }
    }
    
}
