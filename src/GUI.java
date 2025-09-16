import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class GUI extends JFrame {
    private JFrame frame;
    private JPanel topPanel, bottomPanel, inputPanel, outputPanel, toolsPanel;
    private JTextField nameField, amountField, rateField, yearsField, contributionField;
    private JLabel resultLabel;
    private JComboBox<String> investmentTypeBox;
    private JComboBox<String> storageBox;
    private JLabel nameLabel = new JLabel();
    private JLabel amountLabel = new JLabel();
    private JLabel rateLabel = new JLabel();
    private JLabel yearsLabel = new JLabel();
    private JLabel contributionLabel = new JLabel();
    private JPanel contributionPanel;
    private JPanel graphArea;

    String[] options = {
        "Compound Interest",
        "Simple Interest", 
        "With Annual Contributions",
        "Appreciating Asset",
        "Depreciating Asset",
        "Simulated Crypto",
        "Inflation-adjusted Value"
    };

    public GUI() {
        initFrame();
        initPanels();
        buildTopPanel();
        buildInputPanel();
        buildOutputPanel();
        buildToolsPanel(); // New tools panel
        buildBottomPanel();
        configureLayout();
    }

    private void initFrame() {
        frame = new JFrame("Investment Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
    }

    private void initPanels() {
        topPanel = new JPanel();
        bottomPanel = new JPanel(new BorderLayout());
        inputPanel = new JPanel();
        outputPanel = new JPanel();
        toolsPanel = new JPanel(); // New tools panel
    }

    private void buildTopPanel() {
        topPanel.setBackground(new Color(70, 130, 180)); // Steel blue
        topPanel.setPreferredSize(new Dimension(100, 120));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(25, 25, 112))); // Navy border

        JLabel title = new JLabel("Investment Calculator for Teens");
        title.setFont(new Font("Verdana", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        topPanel.add(title);
    }

    private void buildInputPanel() {
        inputPanel.setBackground(new Color(248, 248, 255)); // Ghost white
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(70, 130, 180)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        inputPanel.setLayout(new BorderLayout());

        // Main input container with proper structure
        JPanel mainInputContainer = new JPanel();
        mainInputContainer.setLayout(new BoxLayout(mainInputContainer, BoxLayout.Y_AXIS));
        mainInputContainer.setOpaque(false);

        // Investment Type Section
        JPanel typeSection = createSection("Investment Type", null);
        investmentTypeBox = new JComboBox<>(options);
        investmentTypeBox.setFont(new Font("Arial", Font.PLAIN, 16));
        investmentTypeBox.setPreferredSize(new Dimension(350, 35));
        investmentTypeBox.setMaximumSize(new Dimension(350, 35));
        investmentTypeBox.addActionListener(e -> updateFieldsVisibility());
        
        JPanel typeContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        typeContainer.setOpaque(false);
        typeContainer.add(investmentTypeBox);
        typeSection.add(typeContainer);
        
        mainInputContainer.add(typeSection);
        mainInputContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Input Fields Section
        JPanel fieldsSection = createSection("Investment Details", null);
        
        nameField = new JTextField();
        amountField = new JTextField();
        contributionField = new JTextField();
        rateField = new JTextField();
        yearsField = new JTextField();

        // Add input fields with consistent structure
        fieldsSection.add(createInputField("Name (optional):", nameField, nameLabel));
        fieldsSection.add(Box.createRigidArea(new Dimension(0, 15)));
        
        fieldsSection.add(createInputField("Initial Amount ($):", amountField, amountLabel));
        fieldsSection.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Contribution field (initially hidden)
        contributionPanel = createInputField("Annual Contribution ($):", contributionField, contributionLabel);
        contributionPanel.setVisible(false);
        fieldsSection.add(contributionPanel);
        fieldsSection.add(Box.createRigidArea(new Dimension(0, 15)));
        
        fieldsSection.add(createInputField("Interest Rate (%):", rateField, rateLabel));
        fieldsSection.add(Box.createRigidArea(new Dimension(0, 15)));
        
        fieldsSection.add(createInputField("Years:", yearsField, yearsLabel));
        
        mainInputContainer.add(fieldsSection);
        mainInputContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        // Calculate Button Section
        JPanel buttonSection = createSection("Calculate", null);
        JButton calculateButton = new JButton("Calculate Investment");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 18));
        calculateButton.setBackground(new Color(70, 130, 180));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setPreferredSize(new Dimension(300, 45));
        calculateButton.setMaximumSize(new Dimension(300, 45));
        calculateButton.setFocusPainted(false);
        calculateButton.setBorder(BorderFactory.createRaisedBevelBorder());
        calculateButton.addActionListener(e -> handleCalculate());
        
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.setOpaque(false);
        buttonContainer.add(calculateButton);
        buttonSection.add(buttonContainer);
        
        mainInputContainer.add(buttonSection);
        mainInputContainer.add(Box.createRigidArea(new Dimension(0, 25)));

        // Saved Calculations Section
        JPanel storageSection = createSection("Load Saved Calculation", null);
        storageBox = new JComboBox<>(Write.getNames(true));
        storageBox.setFont(new Font("Arial", Font.PLAIN, 16));
        storageBox.setPreferredSize(new Dimension(350, 35));
        storageBox.setMaximumSize(new Dimension(350, 35));
        storageBox.addActionListener(e -> loadSave());
        
        JPanel storageContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        storageContainer.setOpaque(false);
        storageContainer.add(storageBox);
        storageSection.add(storageContainer);
        
        mainInputContainer.add(storageSection);

        inputPanel.add(mainInputContainer, BorderLayout.NORTH);
        
        // Initialize field visibility
        updateFieldsVisibility();
    }

    private void buildOutputPanel() {
        outputPanel.setBackground(Color.WHITE);
        outputPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30)); // Reduced bottom padding
        outputPanel.setLayout(new BorderLayout());

        // Results section
        JPanel resultsHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resultsHeader.setOpaque(false);
        
        resultLabel = new JLabel("Future Value: Ready to calculate");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 28));
        resultLabel.setForeground(new Color(25, 25, 112));
        resultsHeader.add(resultLabel);
        
        outputPanel.add(resultsHeader, BorderLayout.NORTH);
        
        // Graph area (made smaller)
        graphArea = new JPanel();
        graphArea.setBackground(Color.WHITE);
        graphArea.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Investment Growth Chart",
            0, 0,
            new Font("Arial", Font.BOLD, 16),
            new Color(70, 130, 180)
        ));
        graphArea.setPreferredSize(new Dimension(600, 300)); // Made smaller to make room for tools
        outputPanel.add(graphArea, BorderLayout.CENTER);
    }

    // NEW: Build the prominent tools panel
    private void buildToolsPanel() {
        toolsPanel.setBackground(new Color(245, 245, 250)); // Light background
        toolsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(70, 130, 180)),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        toolsPanel.setLayout(new BorderLayout());

        // Tools title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        JLabel toolsTitle = new JLabel("Additional Investment Tools");
        toolsTitle.setFont(new Font("Arial", Font.BOLD, 22));
        toolsTitle.setForeground(new Color(25, 25, 112));
        titlePanel.add(toolsTitle);
        
        // Tools buttons container
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        buttonsPanel.setOpaque(false);
        
        // Stock Calculator Button
        JButton stockButton = createToolButton(
            "Stock Investment Calculator", 
            "Explore investing in popular stocks like Apple, Tesla, and more!",
            new Color(34, 139, 34), // Forest green
            e -> openStockCalculator()
        );
        
        // Utility Calculator Button  
        JButton utilityButton = createToolButton(
            "Ms. Fernandez Utility Calculator",
            "Calculate the value and utility of purchases before buying!",
            new Color(255, 140, 0), // Dark orange
            e -> openUtilityCalculator()
        );
        
        // AI Learning Assistant Button
        JButton aiButton = createToolButton(
            "AI Learning Assistant",
            "Chat with AI to learn about investing, compound interest, and more!",
            new Color(138, 43, 226), // Blue violet
            e -> openClaudeChatbot()
        );

        buttonsPanel.add(stockButton);
        buttonsPanel.add(utilityButton);
        buttonsPanel.add(aiButton);
        
        toolsPanel.add(titlePanel, BorderLayout.NORTH);
        toolsPanel.add(buttonsPanel, BorderLayout.CENTER);
        
        // Add a subtle description
        JLabel description = new JLabel(
            "<html><div style='text-align:center; color:#666666; font-style:italic;'>" +
            "Expand your financial knowledge with these interactive tools designed specifically for teens!" +
            "</div></html>"
        );
        description.setFont(new Font("Arial", Font.PLAIN, 13));
        description.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel descPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        descPanel.setOpaque(false);
        descPanel.add(description);
        toolsPanel.add(descPanel, BorderLayout.SOUTH);
    }

    // Helper method to create attractive tool buttons
    private JButton createToolButton(String title, String description, Color color, java.awt.event.ActionListener action) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(280, 90));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        
        // Title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Description label
        JLabel descLabel = new JLabel("<html><div style='text-align:center;'>" + description + "</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        descLabel.setForeground(new Color(240, 240, 240));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        button.add(titleLabel, BorderLayout.NORTH);
        button.add(descLabel, BorderLayout.CENTER);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = color;
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
        
        return button;
    }

    private void buildBottomPanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int panelHeight = screenSize.height - 160; // Account for title and menu
        
        inputPanel.setPreferredSize(new Dimension(500, panelHeight)); // Fixed width
        
        // Create a container for output and tools
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(outputPanel, BorderLayout.CENTER);
        rightPanel.add(toolsPanel, BorderLayout.SOUTH); // Add tools panel at bottom
        
        rightPanel.setPreferredSize(new Dimension(screenSize.width - 500, panelHeight));

        bottomPanel.add(inputPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.CENTER);
    }

    // Helper method to create structured sections
    private JPanel createSection(String title, Color backgroundColor) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
                title,
                0, 0,
                new Font("Arial", Font.BOLD, 16),
                new Color(70, 130, 180)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        if (backgroundColor != null) {
            section.setBackground(backgroundColor);
        } else {
            section.setOpaque(false);
        }
        
        return section;
    }

    // Helper method to create consistent input fields
    private JPanel createInputField(String labelText, JTextField textField, JLabel label) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 5));
        fieldPanel.setOpaque(false);
        fieldPanel.setMaximumSize(new Dimension(450, 40));

        label.setText(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setPreferredSize(new Dimension(200, 25));

        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setPreferredSize(new Dimension(150, 30));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(textField, BorderLayout.CENTER);
        
        return fieldPanel;
    }

    private void updateStorageBox() {
        String currentSelection = (String) storageBox.getSelectedItem();
        storageBox.removeAllItems();
        String[] names = Write.getNames(true);
        
        for (String name : names) {
            if (name != null && !name.trim().isEmpty()) {
                storageBox.addItem(name);
            }
        }
        
        if (currentSelection != null) {
            for (int i = 0; i < storageBox.getItemCount(); i++) {
                if (storageBox.getItemAt(i).equals(currentSelection)) {
                    storageBox.setSelectedIndex(i);
                    return;
                }
            }
        }
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
            
            if (fields.length < 5) {
                System.out.println("Not enough fields in data line");
                return;
            }
            
            String calculationType = fields[1];
            
            clearAllFields();
            
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
                nameField.setText(fields[0]);
                contributionField.setText(fields[2]);
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
            
            updateFieldsVisibility();
            
        } catch (Exception e) {
            System.out.println("Error in loadSave: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearAllFields() {
        nameField.setText("");
        amountField.setText("");
        contributionField.setText("");
        rateField.setText("");
        yearsField.setText("");
    }

    private void updateFieldsVisibility() {
        String selectedOption = (String) investmentTypeBox.getSelectedItem();
        
        if (selectedOption.equals(options[0]) || selectedOption.equals(options[1])) {
            nameLabel.setText("Name (optional):");
            amountLabel.setText("Initial Amount ($):");
            rateLabel.setText("Interest Rate (%):");
            yearsLabel.setText("Years:");
            contributionPanel.setVisible(false);
        }
        else if (selectedOption.equals(options[2])) {
            nameLabel.setText("Name (optional):");
            amountLabel.setText("Initial Amount ($):");
            contributionLabel.setText("Annual Contribution ($):");
            rateLabel.setText("Interest Rate (%):");
            yearsLabel.setText("Years:");
            contributionPanel.setVisible(true);
        }
        else if (selectedOption.equals(options[3])) {
            nameLabel.setText("Name (optional):");
            amountLabel.setText("Purchase Price ($):");
            rateLabel.setText("Predicted Annual Growth (%):");
            yearsLabel.setText("Years:");
            contributionPanel.setVisible(false);
        }
        else if (selectedOption.equals(options[4])) {
            nameLabel.setText("Name (optional):");
            amountLabel.setText("Purchase Price ($):");
            rateLabel.setText("Predicted Annual Decrease (%):");
            yearsLabel.setText("Years:");
            contributionPanel.setVisible(false);
        }
        else if (selectedOption.equals(options[5])) {
            nameLabel.setText("Name (optional):");
            amountLabel.setText("Initial Amount ($):");
            rateLabel.setText("Predicted Volatility (%):");
            yearsLabel.setText("Years:");
            contributionPanel.setVisible(false);
        } 
        else if (selectedOption.equals(options[6])) {
            nameLabel.setText("Name (optional):");
            amountLabel.setText("Nominal Value ($):");
            rateLabel.setText("Predicted Inflation Rate (%):");
            yearsLabel.setText("Years:");
            contributionPanel.setVisible(false);
        }
        
        inputPanel.revalidate();
        inputPanel.repaint();
    }

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

            if (selected.equals(options[0])) {
                for (int i = 1; i <= years; i++) {
                    double v = InvestmentLogic.calculateCompoundInterest(principal, rate, i);
                    values.add(v);
                }
                futureValue = values.get(values.size() - 1);
                Write.storeCompoundInterest(name, principal, rate, years);
            } else if (selected.equals(options[1])) {
                for (int i = 1; i <= years; i++) {
                    double v = InvestmentLogic.calculateSimpleInterest(principal, rate, i);
                    values.add(v);
                }
                futureValue = values.get(values.size() - 1);
                Write.storeSimpleInterest(name, principal, rate, years);
            } else if (selected.equals(options[2])) {
                double annualContribution = 0;
                try {
                    String contributionText = contributionField.getText().trim();
                    if (!contributionText.isEmpty()) {
                        annualContribution = Double.parseDouble(contributionText);
                    }
                } catch (NumberFormatException e) {
                    annualContribution = 0;
                }
                
                for (int i = 1; i <= years; i++) {
                    double v = InvestmentLogic.calculateWithContributions(principal, annualContribution, rate, i);
                    values.add(v);
                }
                futureValue = values.get(values.size() - 1);
                Write.storeAnnualContributions(name, annualContribution, principal, rate, years);
            }
            else if (selected.equals(options[3])) {
                for (int i = 1; i <= years; i++) {
                    double v = InvestmentLogic.calculateAppreciation(principal, rate, i);
                    values.add(v);
                }
                futureValue = values.get(values.size() - 1);
                Write.storeAppreciation(name, principal, rate, years);
            } else if (selected.equals(options[4])) {
                for (int i = 1; i <= years; i++) {
                    double v = InvestmentLogic.calculateDepreciation(principal, rate, i);
                    values.add(v);
                }
                futureValue = values.get(values.size() - 1);
                Write.storeDepreciation(name, principal, rate, years);
            } else if (selected.equals(options[5])) {
                for (int i = 1; i <= years; i++) {
                    double v = InvestmentLogic.simulateCryptoValue(principal, rate, i);
                    values.add(v);
                }
                futureValue = values.get(values.size() - 1);
                Write.storeCryptoValue(name, principal, rate, years);
            } else if (selected.equals(options[6])) {
                for (int i = 1; i <= years; i++) {
                    double v = InvestmentLogic.adjustForInflation(principal, rate, i);
                    values.add(v);
                }
                futureValue = values.get(values.size() - 1);
                Write.storeInflation(name, principal, rate, years);
            }
            
            // Format the result with commas for better readability
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
            resultLabel.setText("Future Value: " + currencyFormat.format(futureValue));

            // Show graph in graphArea
            graphArea.removeAll();
            graphArea.add(new ValueProjectionGraphPanel(values, years), BorderLayout.CENTER);
            graphArea.revalidate();
            graphArea.repaint();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
        
        updateStorageBox();
        
        String nameToSelect = currentName.isEmpty() ? "(default)" : currentName;
        for (int i = 0; i < storageBox.getItemCount(); i++) {
            if (storageBox.getItemAt(i).equals(nameToSelect)) {
                storageBox.setSelectedIndex(i);
                break;
            }
        }
    }

    // Method to open the utility calculator
    private void openUtilityCalculator() {
        UtilityCalculatorDialog utilityDialog = new UtilityCalculatorDialog(frame);
        utilityDialog.setVisible(true);
    }

    // Method to open the stock calculator
    private void openStockCalculator() {
        StockCalculatorDialog stockCalc = new StockCalculatorDialog(frame);
        stockCalc.setVisible(true);
    }

    // Method to open the Claude AI chatbot
    private void openClaudeChatbot() {
        ClaudeEducationalChatbot chatbot = new ClaudeEducationalChatbot(frame);
        chatbot.setVisible(true);
    }
    
    private void configureLayout() {
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.CENTER);
    }

    public void show() {
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}