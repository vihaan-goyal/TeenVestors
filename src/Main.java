import java.io.File;

public class Main {
    public static void main(String[] args) {
        
        //clears the storage file for testing purposes, probobly going to be removed in full version
        File f = new File("Store.txt");
        if (!f.exists()) { 
        Write.clear();
        }
        
        GUI gui = new GUI();
        gui.setVisible(true);
    }
}
