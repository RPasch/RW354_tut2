
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import static java.lang.System.out;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverTCP {
    
    private static int FILE_SIZE = 0;
    private static int numFileParts = 0;
    public static File f = null;
    
    
    public ReceiverTCP() throws IOException, ClassNotFoundException{
        receiveObj();
    }
    
    private static void receiveObj() throws IOException, ClassNotFoundException{
        String cwd = System.getProperty("user.dir");
        

        String filename = Receiver.in.readUTF();
        ReceiverGUI.updateTextArea("Receiving " + filename);
        
        FILE_SIZE = Receiver.in.readInt();
        numFileParts = Receiver.in.readInt();
        
        File file = new File(cwd+"/"+filename);
        
        System.out.println(filename + " | " + FILE_SIZE + " | " + numFileParts + " | " + Receiver.filePartSize);
        
        try {
            int tempCount = FILE_SIZE;
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            
            for (int i = 0; i < numFileParts - 1; i++) {
                byte[] byteArray = new byte[Receiver.filePartSize];
                Receiver.in.readFully(byteArray, 0, byteArray.length);
                bos.write(byteArray, 0, byteArray.length);
                tempCount -= Receiver.filePartSize;
            }
            
            byte[] byteArray = new byte[tempCount];
            Receiver.in.readFully(byteArray, 0, byteArray.length);
            bos.write(byteArray, 0, byteArray.length);
            
            bos.flush();
            
        } catch (Exception e) {
            Logger.getLogger(ReceiverRBUDP.class.getName()).log(Level.SEVERE, null, e);
        }

        ReceiverGUI.updateTextArea("Transfer Complete");
        
    }
    
}
