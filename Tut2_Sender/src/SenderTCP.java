import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderTCP {
    //private static String ipAddress = "";
    static FileInputStream fis = null;
    static BufferedInputStream bis = null;
    private static int currentByte = 0;
    
    public SenderTCP() {
        sendObject();
    }
    
    private void sendObject() {
        
        try {
            Sender.out.writeUTF(Sender.filename);
            Sender.out.writeInt(Sender.filelength);
            Sender.out.writeInt(Sender.numFileParts);
            //byte[] byteArray = new byte[(int)Sender.filelength];
//            byte[] byteArray = new byte[byteInterval];

            send();
            //fis = new FileInputStream(Sender.file);
            
//            bis = new BufferedInputStream(fis);
//            bis.read(byteArray, 0, byteArray.length);
//            Sender.out.write(byteArray, 0, byteArray.length);
//            Sender.out.flush();
        } catch (Exception e) {
            System.err.println("could not send via TCP : " + e);
        }
        
    }
    
    private void send() {
        int tempCount = Sender.filelength;
        
        try {
            for (int i = 0; i < Sender.numFileParts - 1; i++) {
                byte[] byteArray = new byte[Sender.filePartSize];
                Sender.raf.read(byteArray);
                Sender.out.write(byteArray, 0, byteArray.length);
                Sender.out.flush();
                tempCount -= Sender.filePartSize;
            }
            
            System.out.println("temp count : " + tempCount);
            
            byte[] byteArray = new byte[tempCount];
            Sender.raf.read(byteArray);
            Sender.out.write(byteArray, 0, byteArray.length);
            Sender.out.flush();
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(SenderTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
