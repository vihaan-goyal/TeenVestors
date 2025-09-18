import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.plaf.DimensionUIResource;

public class Main {
    public static void main(String[] args) {
        
        // Check if Store.txt exists and has saved data
        File storeFile = new File("Store.txt");
        boolean hasExistingData = false;
        
        if (storeFile.exists()) {
            // Check if file has more than just the default entry
            String[] existingNames = Write.getNames(true);
            hasExistingData = existingNames.length > 1 || 
                             (existingNames.length == 1 && !existingNames[0].equals("(default)"));
        }
        
        // If there's existing data, ask user what they want to do
        if (hasExistingData) {
            int choice = JOptionPane.showConfirmDialog(
                null,
                "You have saved calculations from previous sessions.\n\n" +
                "Would you like to start fresh?\n\n" +
                "• Click YES to clear all saved data and start fresh\n" +
                "• Click NO to keep your saved calculations\n" +
                "• Click CANCEL to exit the app",
                "Investment Calculator - Startup Options",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (choice == JOptionPane.YES_OPTION) {
                // User wants to start fresh
                Write.clear();
                JOptionPane.showMessageDialog(
                    null,
                    "Storage cleared! Starting with a fresh calculator.",
                    "Storage Cleared",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else if (choice == JOptionPane.NO_OPTION) {
                // User wants to keep existing data - do nothing
                JOptionPane.showMessageDialog(
                    null,
                    "Your saved calculations have been preserved.",
                    "Data Preserved",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                // User clicked CANCEL or closed dialog - exit app
                System.exit(0);
                return;
            }
        } else {
            // No existing data, create fresh storage
            Write.clear();
        }
        
        GUI gui = new GUI();
        gui.setVisible(true);
        
    }
}