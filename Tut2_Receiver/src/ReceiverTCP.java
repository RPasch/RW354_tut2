
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 18214304
 */
public class ReceiverTCP {
    
    public final static int FILE_SIZE = 60223860;
   // private static InputStream inFromReceiver;
    private static ObjectInputStream in;
//    private static Socket tcpClientSocket = null;
//    private static ServerSocket tcpServerSocket = null;
    
    public static File f = null;
    
    public ReceiverTCP() throws IOException, ClassNotFoundException{
        receiveObj();
    
    }
    
//    @Override
//    public void run(){
//    
//        
//    
//    }
    
    
    
    
    private static void receiveObj() throws IOException, ClassNotFoundException{
       // tcpServerSocket = new ServerSocket(Receiver.portN);
        //tcpClientSocket = tcpServerSocket.accept();
        //inFromClient = Receiver.normalClientSocket.getInputStream();
       
        
        String cwd = System.getProperty("user.dir");

        String filename = Receiver.in.readUTF();
        
        File file = new File(cwd+"/"+filename);
        
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        
        try {
            byte[] byteArray = new byte[FILE_SIZE];
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bytesRead = Receiver.inFromReceiver.read(byteArray, 0, byteArray.length);
            current = bytesRead;
//            do {
//                bytesRead = Receiver.inFromReceiver.read(byteArray, current, (byteArray.length-current));
//                System.out.println("doesthisreach1111");
//                if (bytesRead >= 0) {
//                    current += bytesRead;
//                    System.out.println("doesthisreach2222");
//                }
//            } while (bytesRead > -1);
            
            bos.write(byteArray, 0, current);
            System.out.println("filename : " + file.getName());
            bos.flush();
            
        } catch (Exception e) {
            System.err.println("shit");
        }
        
//        in = new ObjectInputStream(Receiver.inFromReceiver);
//
//        File tmp = (File) in.readObject();
//        file = new File(cwd + "/"+tmp.getName());
//        
//        Scanner sc = new Scanner(tmp);
//        sc.useDelimiter(" ");
//        String tempString= "";
//        while(sc.hasNext()){
//            
//            tempString =tempString + sc.next()+" ";
//        }
//        tempString = tempString.substring(0, tempString.length()-1);
//        byte everything[] = tempString.getBytes();
//        OutputStream outFile = new FileOutputStream(file);
//        outFile.write(everything);
//        outFile.close();
//        System.out.println(file.getName());
        

    }
}
