
import java.io.RandomAccessFile;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileChooser {
    private JFileChooser chooser = new JFileChooser();
    
    public void filePick() throws Exception {
        
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            Sender.path = chooser.getSelectedFile().getAbsolutePath();
            Sender.filename = chooser.getSelectedFile().getName();
            Sender.raf = new RandomAccessFile(chooser.getSelectedFile(), "r");
            Sender.filelength = (int) Math.ceil(Sender.raf.length());
            Sender.numFileParts = (int) Math.round(Sender.filelength/Sender.filePartSize);
        } else {
            JOptionPane.showMessageDialog(chooser, "No valid file selected");
        }
        
    }
    
}
