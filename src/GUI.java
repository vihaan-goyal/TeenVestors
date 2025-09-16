import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class GUI extends JFrame {
    private JFrame frame;
    private JPanel topPanel, bottomPanel, inputPanel, outputPanel;
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
    }

    private void buildTopPanel() {
        topPanel.setBackground(new Color(70, 130, 180)); // Steel blue
        topPanel.setPreferredSize(new Dimension(100, 160));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(25, 25, 112))); // Navy border
        topPanel.setLayout(new BorderLayout());

        // Title
        JPanel titleContainer = new JPanel();
        titleContainer.setOpaque(false);
        JLabel title = new JLabel("Investment Calculator for Teens");
        title.setFont(new Font("Verdana", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        titleContainer.add(title);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        // Stock Calculator Button
        JButton stockButton = new JButton("Stock Calculator");
        stockButton.setFont(new Font("Arial", Font.BOLD, 16));
        stockButton.setBackground(new Color(195, 58, 25));
        stockButton.setForeground(new Color(0, 0, 0));
        stockButton.setPreferredSize(new Dimension(200, 40));
        stockButton.setFocusPainted(false);
        stockButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        stockButton.setToolTipText("<html><b>Stock Calculator:</b> Learn about investing in companies like Apple, Tesla, and Google!<br>" +
                                   "See how stocks can grow your money over time.</html>");
        stockButton.addActionListener(e -> openStockCalculator());
        
        // Utility Calculator Button
        JButton utilityButton = new JButton("Utility Calculator");
        utilityButton.setFont(new Font("Arial", Font.BOLD, 16));
        utilityButton.setBackground(new Color(170, 255, 144));
        utilityButton.setForeground(new Color(0, 0, 0));
        utilityButton.setPreferredSize(new Dimension(200, 40));
        utilityButton.setFocusPainted(false);
        utilityButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        utilityButton.setToolTipText("<html><b>Utility Calculator:</b> Decide if something is worth buying!<br>" +
                                     "Calculate the value you get per dollar spent.</html>");
        utilityButton.addActionListener(e -> openUtilityCalculator());
        
        // AI Learning Assistant Button
        JButton aiButton = new JButton("AI Learning Assistant");
        aiButton.setFont(new Font("Arial", Font.BOLD, 16));
        aiButton.setBackground(new Color(138, 43, 226));
        aiButton.setForeground(Color.WHITE);
        aiButton.setPreferredSize(new Dimension(220, 40));
        aiButton.setFocusPainted(false);
        aiButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aiButton.setToolTipText("<html><b>AI Learning Assistant:</b> Chat with AI to learn about investing!<br>" +
                                "Ask questions and get personalized financial education.</html>");
        aiButton.addActionListener(e -> openClaudeChatbot());
        
        // About Button
        JButton aboutButton = new JButton("About");
        aboutButton.setFont(new Font("Arial", Font.BOLD, 16));
        aboutButton.setBackground(new Color(218,165,32));
        aboutButton.setForeground(new Color(0, 0, 0));
        aboutButton.setPreferredSize(new Dimension(120, 40));
        aboutButton.setFocusPainted(false);
        aboutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aboutButton.setToolTipText("<html><b>About:</b> Learn more about this calculator and its features.</html>");
        aboutButton.addActionListener(e -> showAbout());
        
        buttonPanel.add(stockButton);
        buttonPanel.add(utilityButton);
        buttonPanel.add(aiButton);
        buttonPanel.add(aboutButton);
        
        topPanel.add(titleContainer, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
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
        investmentTypeBox.setToolTipText("<html><b>Investment Type:</b> Different ways money can grow or shrink over time.<br>" +
                                         "Hover over each option after selecting to learn more!</html>");
        
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

        // Add tooltips to text fields
        nameField.setToolTipText("<html><b>Name:</b> Give your calculation a nickname to save it for later.<br>" +
                                 "Like 'College Fund' or 'Car Savings'!</html>");
        
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
        calculateButton.setToolTipText("<html><b>Calculate:</b> Click to see how your money will grow over time!</html>");
        
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
        storageBox.setToolTipText("<html><b>Saved Calculations:</b> Pick a previous calculation to load it back.<br>" +
                                  "Great for comparing different investment scenarios!</html>");
        
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
        outputPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        outputPanel.setLayout(new BorderLayout());

        // Results section
        JPanel resultsHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resultsHeader.setOpaque(false);
        
        resultLabel = new JLabel("Future Value: Ready to calculate");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 28));
        resultLabel.setForeground(new Color(25, 25, 112));
        resultsHeader.add(resultLabel);
        
        outputPanel.add(resultsHeader, BorderLayout.NORTH);
        
        // Placeholder for graph
        JPanel graphArea = new JPanel();
        graphArea.setBackground(Color.WHITE);
        graphArea.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Investment Growth Chart",
            0, 0,
            new Font("Arial", Font.BOLD, 16),
            new Color(70, 130, 180)
        ));
        outputPanel.add(graphArea, BorderLayout.CENTER);
    }

    private void buildBottomPanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int panelHeight = screenSize.height - 200; // Account for larger title panel with buttons
        
        inputPanel.setPreferredSize(new Dimension(500, panelHeight)); // Fixed width
        outputPanel.setPreferredSize(new Dimension(screenSize.width - 500, panelHeight));

        bottomPanel.add(inputPanel, BorderLayout.WEST);
        bottomPanel.add(outputPanel, BorderLayout.CENTER);
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

    // Helper method to create consistent input fields with tooltips
    private JPanel createInputField(String labelText, JTextField textField, JLabel label) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 5));
        fieldPanel.setOpaque(false);
        fieldPanel.setMaximumSize(new Dimension(450, 40));

        label.setText(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setPreferredSize(new Dimension(200, 25));
        
        // Add comprehensive tooltips for each label
        setLabelTooltip(label, labelText);

        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setPreferredSize(new Dimension(150, 30));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        // Add tooltips to text fields based on the label
        setTextFieldTooltip(textField, labelText);

        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(textField, BorderLayout.CENTER);
        
        return fieldPanel;
    }
    
    // New method to set tooltips for labels
    private void setLabelTooltip(JLabel label, String labelText) {
        String tooltip = "";
        
        if (labelText.contains("Name")) {
            tooltip = "<html><b>Name:</b> A label for your calculation.<br>" +
                     "Example: 'College Savings' or 'Xbox Fund'<br>" +
                     "This helps you remember what each calculation is for!</html>";
        } else if (labelText.contains("Initial Amount")) {
            tooltip = "<html><b>Initial Amount:</b> The money you start with.<br>" +
                     "Example: If you have $100 in your piggy bank, that's your initial amount!<br>" +
                     "Also called 'principal' in finance.</html>";
        } else if (labelText.contains("Purchase Price")) {
            tooltip = "<html><b>Purchase Price:</b> How much something costs to buy.<br>" +
                     "Example: A car might cost $5,000.<br>" +
                     "This is your starting value for assets.</html>";
        } else if (labelText.contains("Annual Contribution")) {
            tooltip = "<html><b>Annual Contribution:</b> Money you add every year.<br>" +
                     "Example: If you save $50 each birthday, that's your annual contribution!<br>" +
                     "Regular saving supercharges your growth!</html>";
        } else if (labelText.contains("Interest Rate")) {
            tooltip = "<html><b>Interest Rate:</b> The percentage your money grows each year.<br>" +
                     "Example: 5% means for every $100, you earn $5 per year.<br>" +
                     "Higher rates = faster growth!</html>";
        } else if (labelText.contains("Annual Growth")) {
            tooltip = "<html><b>Annual Growth:</b> How much value something gains per year.<br>" +
                     "Example: A house might grow 3% in value each year.<br>" +
                     "This is appreciation!</html>";
        } else if (labelText.contains("Annual Decrease")) {
            tooltip = "<html><b>Annual Decrease:</b> How much value something loses per year.<br>" +
                     "Example: A car might lose 15% of its value each year.<br>" +
                     "This is depreciation!</html>";
        } else if (labelText.contains("Volatility")) {
            tooltip = "<html><b>Volatility:</b> How wildly the price swings up and down.<br>" +
                     "High volatility = big risks and big rewards!<br>" +
                     "Crypto is super volatile - it's like a roller coaster!</html>";
        } else if (labelText.contains("Inflation Rate")) {
            tooltip = "<html><b>Inflation Rate:</b> How much prices go up each year.<br>" +
                     "Example: If inflation is 3%, a $10 pizza costs $10.30 next year.<br>" +
                     "Your money buys less over time!</html>";
        } else if (labelText.contains("Nominal Value")) {
            tooltip = "<html><b>Nominal Value:</b> The face value of money, not adjusted for inflation.<br>" +
                     "Example: $100 today is nominally the same as $100 in 10 years.<br>" +
                     "But it won't buy as much!</html>";
        } else if (labelText.contains("Years")) {
            tooltip = "<html><b>Years:</b> How long you'll let your money grow.<br>" +
                     "The longer you wait, the more it grows!<br>" +
                     "This is why starting young is so powerful!</html>";
        }
        
        if (!tooltip.isEmpty()) {
            label.setToolTipText(tooltip);
        }
    }
    
    // New method to set tooltips for text fields
    private void setTextFieldTooltip(JTextField field, String labelText) {
        String tooltip = "";
        
        if (labelText.contains("Initial Amount") || labelText.contains("Purchase Price") || labelText.contains("Nominal Value")) {
            tooltip = "Enter a dollar amount (example: 1000)";
        } else if (labelText.contains("Annual Contribution")) {
            tooltip = "Enter how much you'll add each year (example: 500)";
        } else if (labelText.contains("Interest Rate") || labelText.contains("Growth") || 
                   labelText.contains("Decrease") || labelText.contains("Volatility") || 
                   labelText.contains("Inflation")) {
            tooltip = "Enter a percentage without the % sign (example: 5 for 5%)";
        } else if (labelText.contains("Years")) {
            tooltip = "Enter number of years (example: 10)";
        }
        
        if (!tooltip.isEmpty()) {
            field.setToolTipText(tooltip);
        }
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
        
        // Update investment type tooltip based on selection
        if (selectedOption.equals("Compound Interest")) {
            investmentTypeBox.setToolTipText("<html><b>Compound Interest:</b> You earn interest on your interest!<br>" +
                                            "Like a snowball rolling downhill, getting bigger and bigger.<br>" +
                                            "Einstein called it the 8th wonder of the world!</html>");
        } else if (selectedOption.equals("Simple Interest")) {
            investmentTypeBox.setToolTipText("<html><b>Simple Interest:</b> You only earn interest on your original amount.<br>" +
                                            "Less powerful than compound interest.<br>" +
                                            "Common for short-term loans.</html>");
        } else if (selectedOption.equals("With Annual Contributions")) {
            investmentTypeBox.setToolTipText("<html><b>Annual Contributions:</b> Adding money regularly to your investment.<br>" +
                                            "Like putting birthday money in savings every year!<br>" +
                                            "Small regular amounts become huge over time!</html>");
        } else if (selectedOption.equals("Appreciating Asset")) {
            investmentTypeBox.setToolTipText("<html><b>Appreciating Asset:</b> Something that gains value over time.<br>" +
                                            "Examples: Houses, rare collectibles, some stocks.<br>" +
                                            "Buy low, sell high!</html>");
        } else if (selectedOption.equals("Depreciating Asset")) {
            investmentTypeBox.setToolTipText("<html><b>Depreciating Asset:</b> Something that loses value over time.<br>" +
                                            "Examples: Cars, phones, computers.<br>" +
                                            "Important to know when buying expensive things!</html>");
        } else if (selectedOption.equals("Simulated Crypto")) {
            investmentTypeBox.setToolTipText("<html><b>Crypto Simulation:</b> Shows how volatile investments behave.<br>" +
                                            "Crypto can go way up OR way down - it's risky!<br>" +
                                            "Never invest more than you can afford to lose!</html>");
        } else if (selectedOption.equals("Inflation-adjusted Value")) {
            investmentTypeBox.setToolTipText("<html><b>Inflation Adjustment:</b> Shows what money is really worth over time.<br>" +
                                            "As prices go up, your money buys less.<br>" +
                                            "This is why investing beats keeping cash under your mattress!</html>");
        }
        
        // Update field labels and visibility
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
        
        // Update tooltips when labels change
        setLabelTooltip(nameLabel, nameLabel.getText());
        setLabelTooltip(amountLabel, amountLabel.getText());
        setLabelTooltip(rateLabel, rateLabel.getText());
        setLabelTooltip(yearsLabel, yearsLabel.getText());
        setLabelTooltip(contributionLabel, contributionLabel.getText());
        
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

            // Show graph in outputPanel
            outputPanel.removeAll();
            
            // Results header
            JPanel resultsHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
            resultsHeader.setOpaque(false);
            resultsHeader.add(resultLabel);
            outputPanel.add(resultsHeader, BorderLayout.NORTH);
            
            // Graph
            outputPanel.add(new ValueProjectionGraphPanel(values, years), BorderLayout.CENTER);
            outputPanel.revalidate();
            outputPanel.repaint();

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

    private void showAbout() {
        JOptionPane.showMessageDialog(frame, 
            "Investment Calculator for Teens\n" +
            "Features:\n" +
            "Investment calculations and projections\n" +
            "Utility Calculator\n" +
            "Stock investment tools\n" +
            "AI Learning Assistant\n" +
            "Educational tooltips on hover\n\n" +
            "Version 2.1 - Now with helpful tooltips!\n\n" +
            "Hover over any label or field to learn more!", 
            "About", 
            JOptionPane.INFORMATION_MESSAGE);
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