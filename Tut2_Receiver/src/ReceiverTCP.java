
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
    
    
   // private static InputStream inFromClient;
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

        File file =null; //new File(cwd + "/hello.txt");
        
        in = new ObjectInputStream(Receiver.inFromClient);

        File tmp = (File) in.readObject();
        file = new File(cwd + "/"+tmp.getName());
        
        Scanner sc = new Scanner(tmp);
        sc.useDelimiter(" ");
        String tempString= "";
        while(sc.hasNext()){
            
            tempString =tempString + sc.next()+" ";
        }
        tempString = tempString.substring(0, tempString.length()-1);
        byte everything[] = tempString.getBytes();
        OutputStream outFile = new FileOutputStream(file);
        outFile.write(everything);
        outFile.close();
        System.out.println(file.getName());
        

    }
}
