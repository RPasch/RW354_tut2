
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sender {
    public static String ipAddress = "";
    //public static File file = null;
    public static RandomAccessFile raf = null;
    public static String filename = "";
    public static int filelength = 0;
    public static int numFileParts;
    public static int filePartSize = 4096;
    //
    public static String protocol = "";
    public static Socket socket = null;
    static OutputStream outToReceiver;
    static DataOutputStream out;
    static InputStream inFromReceiver;
    static DataInputStream in;
    
    public static void main(String[] args) {
        SenderGUI gui = new SenderGUI();
        gui.setEnabled(true);
        gui.setVisible(true);
        
        
    }
    
    public static void initialConnect() {
        try {
            socket = new Socket(ipAddress, 8000);
            outToReceiver = socket.getOutputStream();
            out = new DataOutputStream(outToReceiver);
            inFromReceiver = socket.getInputStream();
            in = new DataInputStream(inFromReceiver);
            
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void sendProtocol() {
        try {
            out.writeUTF(protocol);
        } catch (IOException ex) {
            System.err.println("could not send protocol");
        }
    }
    
}
