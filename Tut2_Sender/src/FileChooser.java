
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileChooser {
    private JFileChooser chooser = new JFileChooser();
    
    public void filePick() throws Exception {
        
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            Sender.file = chooser.getSelectedFile();
        } else {
            JOptionPane.showMessageDialog(chooser, "No valid file selected");
        }
        
    }
    
}
