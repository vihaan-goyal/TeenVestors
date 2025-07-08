// src/GUI.java
package src;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private JFrame frame;  // Main application frame
    private JPanel topPanel, bottomPanel, inputPanel, outputPanel; // Panels for layout sections
    private JTextField amountField, rateField, yearsField; // Input fields
    private JLabel resultLabel; // Label to display the result
     private JComboBox<String> investmentTypeBox; //box for type of calculation
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
            "Inflation-adjusted Value",
            "Ms. Fernandez Utility"
        }; //list of options for calculations (moved to public to make code more readible with refferences, since these are basically constants)

    public GUI() {
        initFrame();          // Initialize the main frame
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
        inputPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, Color.BLACK));

        investmentTypeBox = new JComboBox<>(options);
        investmentTypeBox.setMaximumSize(new Dimension(400, 30));
        investmentTypeBox.setFont(new Font("Arial", Font.PLAIN, 20));
        investmentTypeBox.addActionListener(e -> updateNames());
        inputPanel.add(investmentTypeBox);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        
        // Create and add labeled input fields
        amountField = new JTextField();
        rateField = new JTextField();
        yearsField = new JTextField();

        inputPanel.add(Box.createVerticalGlue());
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
    }

    private void buildOutputPanel() {
        outputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 200)); // Centered flow layout

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

    // Helper method to create labeled input fields
    private JPanel createLabeledField(String labelText, JTextField textField) {
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
    }
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
   
    // update the names on the label fields for different calculation types
    private void updateNames() {
            if ((String) investmentTypeBox.getSelectedItem() == (String) options[0] || (String) investmentTypeBox.getSelectedItem() == (String) options[1]) {
                amountLabel.setText("Initial Amount ($):");
                rateLabel.setText("Interest Rate (%):");
                yearsLabel.setText("Years:");
            }
            else if ((String) investmentTypeBox.getSelectedItem() == (String) options[3]) {
                amountLabel.setText("Purchase Price ($):");
                rateLabel.setText("Predicted Annuel Growth (%):");
                yearsLabel.setText("Years:");
            }
            else if ((String) investmentTypeBox.getSelectedItem() == (String) options[4]) {
                amountLabel.setText("Purchase Price ($):");
                rateLabel.setText("Predicted Annuel Decrease (%):");
                yearsLabel.setText("Years:");
            }
            else if ((String) investmentTypeBox.getSelectedItem() == (String) options[5]) {
                amountLabel.setText("Initial Amount ($):");
                rateLabel.setText("Predicted Volitivity (%):");
                yearsLabel.setText("Years:");
            } 
            else if ((String) investmentTypeBox.getSelectedItem() == (String) options[6]) {
                amountLabel.setText("Nominal Value ($):");
                rateLabel.setText("Predicted Inflation Rate (%):");
                yearsLabel.setText("Years:");
            }
    }
    // Handles the calculation logic and updates the result label
    private void handleCalculate() {
        if ((String) investmentTypeBox.getSelectedItem() == (String) options[0]) {
            try {
                
                double principal = Double.parseDouble(amountField.getText()); // Initial investment
                double rate = Double.parseDouble(rateField.getText()) / 100.0; // Convert % to decimal
                int years = Integer.parseInt(yearsField.getText()); // Duration in years

                double futureValue = InvestmentLogic.calculateCompoundInterest(principal, rate, years);
                Write.storeCompoundInterest("test", principal, rate, years); 
                resultLabel.setText(String.format("Future Value: $%.2f", futureValue)); // Show result

            } catch (NumberFormatException ex) {
                // Show error if input can't be parsed
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if ((String) investmentTypeBox.getSelectedItem() == (String) options[1]) {
            try {
                
                double principal = Double.parseDouble(amountField.getText()); // Initial investment
                double rate = Double.parseDouble(rateField.getText()) / 100.0; // Convert % to decimal
                int years = Integer.parseInt(yearsField.getText()); // Duration in years

                double futureValue = InvestmentLogic.calculateSimpleInterest(principal, rate, years);
                Write.storeSimpleInterest("test", principal, rate, years); 
                resultLabel.setText(String.format("Future Value: $%.2f", futureValue)); // Show result

            } catch (NumberFormatException ex) {
                // Show error if input can't be parsed
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
