import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SenderTCP {
    //private static String ipAddress = "";
    static FileInputStream fis = null;
    static BufferedInputStream bis = null;
    private static final int byteInterval = 50000000;
    private static int currentByte = 0;
    
    public SenderTCP() {
        sendObject();
    }
    
    private static void sendObject() {
        
        try {
            Sender.out.writeUTF(Sender.filename);
            Sender.out.writeInt((int) Sender.filelength);
            //byte[] byteArray = new byte[(int)Sender.filelength];
            byte[] byteArray = new byte[byteInterval];
            //fis = new FileInputStream(Sender.file);
            
            bis = new BufferedInputStream(fis);
            bis.read(byteArray, 0, byteArray.length);
            Sender.out.write(byteArray, 0, byteArray.length);
            Sender.out.flush();
        } catch (Exception e) {
            System.err.println("could not send via TCP : " + e);
        }
        
    }
    
    private void send() {
        byte[] byteArray = new byte[byteInterval];
        System.arraycopy(Sender.raf.see, currentByte, fis, currentByte, currentByte);
        
    }
    
    
}
