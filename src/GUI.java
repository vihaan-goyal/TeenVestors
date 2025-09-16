import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private JFrame frame;  // Main application frame
    private JPanel topPanel, bottomPanel, inputPanel, outputPanel; // Panels for layout sections
    private JTextField nameField, amountField, rateField, yearsField; // Input fields
    private JLabel resultLabel; // Label to display the result
    private JComboBox<String> investmentTypeBox; //box for type of calculation
    private JComboBox<String> storageBox;
    private JLabel nameLabel = new JLabel();
    private JLabel amountLabel = new JLabel();
    private JLabel rateLabel = new JLabel();
    private JLabel yearsLabel = new JLabel();

    String[] options = {
        "Compound Interest",
        "Simple Interest", 
        "With Annual Contributions",
        "Appreciating Asset",
        "Depreciating Asset",
        "Simulated Crypto",
        "Inflation-adjusted Value"
    }; //list of options for calculations (moved to public to make code more readible with refferences, since these are basically constants)

    public GUI() {
        initFrame();          // Initialize the main frame
        createMenuBar();      //creates the menu bar
        initPanels();         // Create and set up the panels
        buildTopPanel();      // Set up top panel (title)
        buildInputPanel();    // Set up left-side input panel
        buildOutputPanel();   // Set up right-side output panel
        buildBottomPanel();   // Arrange the input and output panels in bottom layout
        configureLayout();    // Add all panels to the frame
    }

    private void initFrame() {
        frame = new JFrame("Investment Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window
        frame.setLayout(new BorderLayout());
    }

    private void initPanels() {
        // Initialize all main panels used in the layout
        topPanel = new JPanel();
        bottomPanel = new JPanel(new BorderLayout());
        inputPanel = new JPanel();
        outputPanel = new JPanel();
    }

    private void buildTopPanel() {
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.setPreferredSize(new Dimension(100, 100));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.BLACK));

        JLabel title = new JLabel("Investment Calculator for Teens");
        title.setFont(new Font("Verdana", Font.BOLD, 60));
        topPanel.add(title);
    }

    private void buildInputPanel() {
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS)); // Vertical layout
        inputPanel.setBackground(Color.LIGHT_GRAY);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 5, Color.BLACK),
            BorderFactory.createEmptyBorder(30, 40, 30, 40) // top, left, bottom, right
    ));


        investmentTypeBox = new JComboBox<>(options);
        investmentTypeBox.setMaximumSize(new Dimension(400, 30));
        investmentTypeBox.setFont(new Font("Arial", Font.PLAIN, 20));
        investmentTypeBox.addActionListener(e -> updateNames());
        inputPanel.add(investmentTypeBox);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        
        // Create and add labeled input fields
        nameField = new JTextField();
        amountField = new JTextField();
        rateField = new JTextField();
        yearsField = new JTextField();

        inputPanel.add(Box.createVerticalGlue());
        inputPanel.add(createLabeledField("Name (optional):", nameField, nameLabel));
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        inputPanel.add(createLabeledField("Initial Amount ($):", amountField, amountLabel));
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        inputPanel.add(createLabeledField("Interest Rate (%):", rateField, rateLabel));
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        inputPanel.add(createLabeledField("Years:", yearsField, yearsLabel));
        inputPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Create and add Calculate button
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 18));
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calculateButton.addActionListener(e -> handleCalculate());

        inputPanel.add(calculateButton);
        inputPanel.add(Box.createVerticalGlue());

        storageBox = new JComboBox<>(Write.getNames(true));
        storageBox.setMaximumSize(new Dimension(400, 30));
        storageBox.setFont(new Font("Arial", Font.PLAIN, 20));
        storageBox.addActionListener(e -> loadSave());
        inputPanel.add(storageBox);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        inputPanel.add(Box.createVerticalGlue());
    }

    private void buildOutputPanel() {
        outputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20)); // Centered flow layout

        resultLabel = new JLabel("Future Value: ");
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        outputPanel.add(resultLabel);
    }

    private void buildBottomPanel() {
        // Get screen size and set input/output panel dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int panelHeight = screenSize.height - 120;
        inputPanel.setPreferredSize(new Dimension(screenSize.width / 2, panelHeight));
        outputPanel.setPreferredSize(new Dimension(screenSize.width / 2, panelHeight));

        // Add input and output panels to the bottom section
        bottomPanel.add(inputPanel, BorderLayout.WEST);
        bottomPanel.add(outputPanel, BorderLayout.EAST);
    }

    private void updateStorageBox() {
        // Store the currently selected item
        String currentSelection = (String) storageBox.getSelectedItem();
        
        // Clear and repopulate
        storageBox.removeAllItems();
        String[] names = Write.getNames(true);
        
        for (String name : names) {
            if (name != null && !name.trim().isEmpty()) {
                storageBox.addItem(name);
            }
        }
        
        // Try to restore the previous selection
        if (currentSelection != null) {
            for (int i = 0; i < storageBox.getItemCount(); i++) {
                if (storageBox.getItemAt(i).equals(currentSelection)) {
                    storageBox.setSelectedIndex(i);
                    return;
                }
            }
        }
    }

    // Helper method to create labeled input fields
    /*private JPanel createLabeledField(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setMaximumSize(new Dimension(500, 40));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        textField.setFont(new Font("Arial", Font.PLAIN, 20));
        textField.setPreferredSize(new Dimension(200, 30));

        panel.add(label);
        panel.add(textField);
        return panel;
    }*/
    // version of helper function for labels needed to be changed later
     private JPanel createLabeledField(String labelText, JTextField textField, JLabel label) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setMaximumSize(new Dimension(500, 40));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setOpaque(false);

        label.setText(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        textField.setFont(new Font("Arial", Font.PLAIN, 20));
        textField.setPreferredSize(new Dimension(200, 30));

        panel.add(label);
        panel.add(textField);
        return panel;
    }

    private void loadSave() {
        try {
            String selectedName = (String) storageBox.getSelectedItem();
            if (selectedName == null || selectedName.isEmpty()) {
                return;
            }
            
            String dataLine = Write.findName(selectedName);
            if (dataLine == null || dataLine.trim().isEmpty()) {
                return;
            }
            
            String[] fields = dataLine.trim().split(" ");
            
            // Debug output
            System.out.println("Selected name: " + selectedName);
            System.out.println("Data line: " + dataLine);
            System.out.println("Fields length: " + fields.length);
            for (int i = 0; i < fields.length; i++) {
                System.out.println("Field " + i + ": " + fields[i]);
            }
            
            if (fields.length < 5) {
                System.out.println("Not enough fields in data line");
                return;
            }
            
            String calculationType = fields[1];
            
            // Clear all fields first
            nameField.setText("");
            amountField.setText("");
            rateField.setText("");
            yearsField.setText("");
            
            if (calculationType.equals("CompoundInterest")) {
                investmentTypeBox.setSelectedIndex(0);
                nameField.setText(fields[0]);
                amountField.setText(fields[2]);
                rateField.setText(String.valueOf(Double.parseDouble(fields[3]) * 100));
                yearsField.setText(fields[4]);
            }
            else if (calculationType.equals("SimpleInterest")) {
                investmentTypeBox.setSelectedIndex(1);
                nameField.setText(fields[0]);
                amountField.setText(fields[2]);
                rateField.setText(String.valueOf(Double.parseDouble(fields[3]) * 100));
                yearsField.setText(fields[4]);
            }
            else if (calculationType.equals("AnnualContributions")) {
                investmentTypeBox.setSelectedIndex(2);
                nameField.setText(fields[0] + " " + fields[2]);
                amountField.setText(fields[3]);
                rateField.setText(String.valueOf(Double.parseDouble(fields[4]) * 100));
                yearsField.setText(fields[5]);
            }
            else if (calculationType.equals("Appreciation")) {
                investmentTypeBox.setSelectedIndex(3);
                nameField.setText(fields[0]);
                amountField.setText(fields[2]);
                rateField.setText(String.valueOf(Double.parseDouble(fields[3]) * 100));
                yearsField.setText(fields[4]);
            }
            else if (calculationType.equals("Depreciation")) {
                investmentTypeBox.setSelectedIndex(4);
                nameField.setText(fields[0]);
                amountField.setText(fields[2]);
                rateField.setText(String.valueOf(Double.parseDouble(fields[3]) * 100));
                yearsField.setText(fields[4]);
            }
            else if (calculationType.equals("CryptoValue")) {
                investmentTypeBox.setSelectedIndex(5);
                nameField.setText(fields[0]);
                amountField.setText(fields[2]);
                rateField.setText(String.valueOf(Double.parseDouble(fields[3]) * 100));
                yearsField.setText(fields[4]);
            }
            else if (calculationType.equals("Inflation")) {
                investmentTypeBox.setSelectedIndex(6);
                nameField.setText(fields[0]);
                amountField.setText(fields[2]);
                rateField.setText(String.valueOf(Double.parseDouble(fields[3]) * 100));
                yearsField.setText(fields[4]);
            }
            
            // Update field labels based on selection
            updateNames();
            
        } catch (Exception e) {
            System.out.println("Error in loadSave: " + e.getMessage());
            e.printStackTrace();
        }
    }

   
    // update the names on the label fields for different calculation types
    // Complete updateNames() method for GUI.java
    // Replace your updateNames() method in GUI.java with this:

    private void updateNames() {
        String selectedOption = (String) investmentTypeBox.getSelectedItem();
        
        if (selectedOption.equals(options[0]) || selectedOption.equals(options[1])) {
            // Compound Interest OR Simple Interest
            nameLabel.setText("Name (optional):");
            amountLabel.setText("Initial Amount ($):");
            rateLabel.setText("Interest Rate (%):");
            yearsLabel.setText("Years:");
        }
        else if (selectedOption.equals(options[2])) {
            // With Annual Contributions
            nameLabel.setText("Annual Contribution (name $):");
            amountLabel.setText("Initial Amount ($):");
            rateLabel.setText("Interest Rate (%):");
            yearsLabel.setText("Years:");
        }
        else if (selectedOption.equals(options[3])) {
            // Appreciating Asset
            nameLabel.setText("Name (optional):");
            amountLabel.setText("Purchase Price ($):");
            rateLabel.setText("Predicted Annual Growth (%):");
            yearsLabel.setText("Years:");
        }
        else if (selectedOption.equals(options[4])) {
            // Depreciating Asset
            nameLabel.setText("Name (optional):");
            amountLabel.setText("Purchase Price ($):");
            rateLabel.setText("Predicted Annual Decrease (%):");
            yearsLabel.setText("Years:");
        }
        else if (selectedOption.equals(options[5])) {
            // Simulated Crypto
            nameLabel.setText("Name (optional):");
            amountLabel.setText("Initial Amount ($):");
            rateLabel.setText("Predicted Volatility (%):");
            yearsLabel.setText("Years:");
        } 
        else if (selectedOption.equals(options[6])) {
            // Inflation-adjusted Value
            nameLabel.setText("Name (optional):");
            amountLabel.setText("Nominal Value ($):");
            rateLabel.setText("Predicted Inflation Rate (%):");
            yearsLabel.setText("Years:");
        }
    }

    // Handles the calculation logic and updates the result label
    private void handleCalculate() {
        String currentName = nameField.getText().split(" ")[0];
    try {
        String name = nameField.getText();
        if (name.equals("")) {
            name = "(default)";
        }
        currentName = nameField.getText().split(" ")[0];
        double principal = Double.parseDouble(amountField.getText());
        double rate = Double.parseDouble(rateField.getText()) / 100.0;
        int years = Integer.parseInt(yearsField.getText());
        

        double futureValue = 0;
        java.util.List<Double> values = new java.util.ArrayList<>();
        values.add(principal);

        String selected = (String) investmentTypeBox.getSelectedItem();

        if (selected.equals(options[0])) { // Compound Interest
            for (int i = 1; i <= years; i++) {
                double v = InvestmentLogic.calculateCompoundInterest(principal, rate, i);
                values.add(v);
            }
            futureValue = values.get(values.size() - 1);
            Write.storeCompoundInterest(name, principal, rate, years);
        } else if (selected.equals(options[1])) { // Simple Interest
            for (int i = 1; i <= years; i++) {
                double v = InvestmentLogic.calculateSimpleInterest(principal, rate, i);
                values.add(v);
            }
            futureValue = values.get(values.size() - 1);
            Write.storeSimpleInterest(name, principal, rate, years);
           
        } else if (selected.equals(options[2])) { // With Annual Contributions
            double annualContribution = 0;
            try {
                // Better parsing - handle the name field input more robustly
                String nameInput = nameField.getText().trim();
                if (nameInput.contains(" ")) {
                    String[] parts = nameInput.split("\\s+", 2); // Split into at most 2 parts
                    name = parts[0];
                    if (parts.length > 1 && !parts[1].isEmpty()) {
                        annualContribution = Double.parseDouble(parts[1]);
                    }
                } else {
                    name = nameInput;
                    annualContribution = 0; // Default if no contribution specified
                }
                
                if (name.isEmpty()) {
                    name = "(default)";
                }
            } catch (NumberFormatException e) {
                // If parsing fails, use name as-is and set contribution to 0
                annualContribution = 0;
            }
            
            for (int i = 1; i <= years; i++) {
                double v = InvestmentLogic.calculateWithContributions(principal, annualContribution, rate, i);
                values.add(v);
            }
            futureValue = values.get(values.size() - 1);
            Write.storeAnnualContributions(name, annualContribution, principal, rate, years);
        }
        else if (selected.equals(options[3])) { // Appreciating Asset
            for (int i = 1; i <= years; i++) {
                double v = InvestmentLogic.calculateAppreciation(principal, rate, i);
                values.add(v);
            }
            futureValue = values.get(values.size() - 1);
            Write.storeAppreciation(name, principal, rate, years);
        } else if (selected.equals(options[4])) { // Depreciating Asset
            for (int i = 1; i <= years; i++) {
                double v = InvestmentLogic.calculateDepreciation(principal, rate, i);
                values.add(v);
            }
            futureValue = values.get(values.size() - 1);
            Write.storeDepreciation(name, principal, rate, years);
        } else if (selected.equals(options[5])) { // Simulated Crypto
            for (int i = 1; i <= years; i++) {
                double v = InvestmentLogic.simulateCryptoValue(principal, rate, i);
                values.add(v);
            }
            futureValue = values.get(values.size() - 1);
            Write.storeCryptoValue(name, principal, rate, years);
        } else if (selected.equals(options[6])) { // Inflation-adjusted Value
            for (int i = 1; i <= years; i++) {
                double v = InvestmentLogic.adjustForInflation(principal, rate, i);
                values.add(v);
            }
            futureValue = values.get(values.size() - 1);
            Write.storeInflation(name, principal, rate, years);
        }
        resultLabel.setText(String.format("Future Value: $%.2f", futureValue));

        // Show graph in outputPanel
        outputPanel.removeAll();
        outputPanel.add(resultLabel);
        outputPanel.add(new ValueProjectionGraphPanel(values, years));
        outputPanel.revalidate();
        outputPanel.repaint();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
        
        updateStorageBox();
        
        // Set the current name as selected if it exists in the dropdown
        String nameToSelect = currentName.isEmpty() ? "(default)" : currentName;
        for (int i = 0; i < storageBox.getItemCount(); i++) {
            if (storageBox.getItemAt(i).equals(nameToSelect)) {
                storageBox.setSelectedIndex(i);
                break;
            }
        }
    }


    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Tools menu
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Utility Calculator menu item
        JMenuItem utilityMenuItem = new JMenuItem("Ms. Fernandez Utility Calculator");
        utilityMenuItem.setFont(new Font("Arial", Font.PLAIN, 14));
        utilityMenuItem.addActionListener(e -> openUtilityCalculator());
        
        toolsMenu.add(utilityMenuItem);
        menuBar.add(toolsMenu);
        
        // Optional: Add Help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.setFont(new Font("Arial", Font.PLAIN, 14));
        aboutMenuItem.addActionListener(e -> showAbout());
        
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        
        frame.setJMenuBar(menuBar);
    }

    // Add this method to handle opening the utility calculator
    private void openUtilityCalculator() {
        UtilityCalculatorDialog utilityDialog = new UtilityCalculatorDialog(frame);
        utilityDialog.setVisible(true);
    }

    // Optional: Add an about dialog
    private void showAbout() {
        JOptionPane.showMessageDialog(frame, 
            "Investment Calculator for Teens\n" +
            "Includes Ms. Fernandez Utility Calculator\n" +
            "Version 1.0", 
            "About", 
            JOptionPane.INFORMATION_MESSAGE);
    }



    // Adds top and bottom panels to the main frame
    private void configureLayout() {
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.CENTER);
    }

    // Shows the frame on screen
    public void show() {
        frame.setVisible(true);
    }

    // Getter for the frame (useful for future extensions)
    public JFrame getFrame() {
        return frame;
    }
}