import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SenderTCP {
    private static String ipAddress = "";
    //private static ObjectOutputStream Out;
    static FileInputStream fis = null;
    static BufferedInputStream bis = null;
    
    public SenderTCP() {
        sendObject();
    }
    
    private static void sendObject() {
        
        try {
            //Sender.socket = new Socket(Sender.ipAddress, 8000);
            //OutToReceiver = Sender.socket.getOutputStream();
            //Out = new ObjectOutputStream(Sender.outToServer);
            Sender.out.writeUTF(Sender.file.getName());
            Sender.out.writeInt((int) Sender.file.length());
            byte[] byteArray = new byte[(int)Sender.file.length()];
            fis = new FileInputStream(Sender.file);
            bis = new BufferedInputStream(fis);
            bis.read(byteArray, 0, byteArray.length);
            Sender.out.write(byteArray, 0, byteArray.length);
            Sender.out.flush();
            //Out.writeObject(Sender.file);
        } catch (Exception e) {
            System.err.println("could not send via TCP : " + e);
        }
        
    }
    
}
