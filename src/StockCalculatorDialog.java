import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class StockCalculatorDialog extends JDialog {
    private JComboBox<String> stockCombo;
    private JTextField investmentField, yearsField;
    private JLabel currentPriceLabel, riskLabel, descriptionLabel, resultLabel, apiStatusLabel;
    private Map<String, String> stockMap;
    private JButton calculateButton;
    
    public StockCalculatorDialog(JFrame parent) {
        super(parent, "Teen Stock Investment Calculator", true);
        this.stockMap = EnhancedStockRates.getPopularStocks();
        initComponents();
        setupLayout();
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Test API connection on startup
        testAPIConnection();
    }
    
    private void initComponents() {
        // Create combo box with stock options
        String[] stockOptions = new String[stockMap.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : stockMap.entrySet()) {
            stockOptions[i++] = entry.getKey() + " - " + entry.getValue();
        }
        
        stockCombo = new JComboBox<>(stockOptions);
        stockCombo.addActionListener(e -> updateStockInfo());
        
        investmentField = new JTextField("1000", 10);
        yearsField = new JTextField("10", 10);
        
        currentPriceLabel = new JLabel("Current Price: Loading...");
        riskLabel = new JLabel("Risk Level: ");
        descriptionLabel = new JLabel("<html><div style='width:400px;'>Company info will appear here...</div></html>");
        resultLabel = new JLabel("<html><div style='text-align:center;'>Results will appear here</div></html>");
        apiStatusLabel = new JLabel("Checking API connection...");
        
        // Style labels
        currentPriceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        riskLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        apiStatusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        calculateButton = new JButton("Calculate Investment Growth");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 14));
        calculateButton.setBackground(new Color(76, 175, 80));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.addActionListener(e -> calculateGrowth());
        
        // Load first stock info
        SwingUtilities.invokeLater(() -> updateStockInfo());
    }
    
    private void testAPIConnection() {
        SwingWorker<Boolean, Void> apiTest = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return EnhancedStockRates.testAPIConnection();
            }
            
            @Override
            protected void done() {
                try {
                    boolean apiWorking = get();
                    if (apiWorking) {
                        apiStatusLabel.setText("Real-time prices available");
                        apiStatusLabel.setForeground(new Color(76, 175, 80));
                    } else {
                        apiStatusLabel.setText("Using estimated prices (API limit reached)");
                        apiStatusLabel.setForeground(new Color(255, 152, 0));
                    }
                } catch (Exception e) {
                    apiStatusLabel.setText("Using estimated prices (offline mode)");
                    apiStatusLabel.setForeground(new Color(255, 152, 0));
                }
            }
        };
        apiTest.execute();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(25, 118, 210));
        JLabel titleLabel = new JLabel("📈 Stock Investment Calculator for Teens");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // API Status
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(apiStatusLabel, gbc);
        
        // Stock selection
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(new JLabel("Choose Stock:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        stockCombo.setPreferredSize(new Dimension(300, 30));
        mainPanel.add(stockCombo, gbc);
        
        // Investment amount
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(new JLabel("Investment Amount ($):"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        investmentField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(investmentField, gbc);
        
        // Years
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(new JLabel("Years to Hold:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        yearsField.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(yearsField, gbc);
        
        // Stock info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("Stock Information"));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(currentPriceLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(riskLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(descriptionLabel);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(infoPanel, gbc);
        
        // Calculate button
        calculateButton.setFocusPainted(false);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 8, 8, 8);
        mainPanel.add(calculateButton, gbc);
        
        // Results panel
        JPanel resultPanel = new JPanel();
        resultPanel.setBorder(BorderFactory.createTitledBorder("Investment Projection"));
        resultPanel.add(resultLabel);
        
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(resultPanel, gbc);
        
        // Educational note
        JLabel educationalNote = new JLabel("<html><div style='width:500px; text-align:center; color:gray;'>" +
            "⚠️ <b>Educational Tool Only:</b> Real investing involves risk and market volatility. " +
            "Past performance doesn't guarantee future results. Stock prices can go up or down! " +
            "Always do research and consider consulting a financial advisor before investing real money." +
            "</div></html>");
        educationalNote.setFont(new Font("Arial", Font.ITALIC, 11));
        
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.insets = new Insets(15, 8, 8, 8);
        mainPanel.add(educationalNote, gbc);
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Close button
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void updateStockInfo() {
        String selected = (String) stockCombo.getSelectedItem();
        if (selected == null) return;
        
        String symbol = selected.split(" - ")[0];
        
        // Update static info immediately
        riskLabel.setText("Risk Level: " + EnhancedStockRates.getRiskLevel(symbol));
        descriptionLabel.setText("<html><div style='width:400px;'>" + 
            EnhancedStockRates.getCompanyDescription(symbol) + "</div></html>");
        currentPriceLabel.setText("Current Price: Loading...");
        
        // Fetch price in background
        SwingWorker<Double, Void> priceWorker = new SwingWorker<Double, Void>() {
            @Override
            protected Double doInBackground() throws Exception {
                return EnhancedStockRates.getCurrentStockPrice(symbol);
            }
            
            @Override
            protected void done() {
                try {
                    double price = get();
                    if (price > 0) {
                        currentPriceLabel.setText(String.format("Current Price: $%.2f per share", price));
                    } else {
                        currentPriceLabel.setText("Current Price: Unable to fetch data");
                    }
                } catch (Exception e) {
                    currentPriceLabel.setText("Current Price: Error loading");
                    System.out.println("Error updating stock price: " + e.getMessage());
                }
            }
        };
        priceWorker.execute();
    }
    
    private void calculateGrowth() {
        try {
            String selected = (String) stockCombo.getSelectedItem();
            String symbol = selected.split(" - ")[0];
            
            double investment = Double.parseDouble(investmentField.getText());
            int years = Integer.parseInt(yearsField.getText());
            
            if (investment <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a positive investment amount.", 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (years <= 0 || years > 50) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid number of years (1-50).", 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Show loading state
            calculateButton.setEnabled(false);
            calculateButton.setText("Calculating...");
            resultLabel.setText("<html><div style='text-align:center;'>Calculating investment projection...</div></html>");
            
            // Calculate in background
            SwingWorker<Double, Void> calculationWorker = new SwingWorker<Double, Void>() {
                @Override
                protected Double doInBackground() throws Exception {
                    return EnhancedStockRates.calculateStockGrowth(symbol, investment, years);
                }
                
                @Override
                protected void done() {
                    try {
                        double futureValue = get();
                        double totalGain = futureValue - investment;
                        double percentGain = (totalGain / investment) * 100;
                        
                        String resultText = String.format(
                            "<html><div style='text-align:center;'>" +
                            "<b>Investment Projection for %s</b><br><br>" +
                            "Initial Investment: <b>$%.2f</b><br>" +
                            "Projected Value (Year %d): <b>$%.2f</b><br>" +
                            "Total Gain/Loss: <b>$%.2f</b><br>" +
                            "Percentage Change: <b>%.1f%%</b><br><br>" +
                            "<small style='color: %s;'>%s</small>" +
                            "</div></html>",
                            symbol, investment, years, futureValue, totalGain, percentGain,
                            totalGain >= 0 ? "#4CAF50" : "#F44336",
                            totalGain >= 0 ? "📈 Projected Gain!" : "📉 Potential Loss - Markets can decline!"
                        );
                        
                        resultLabel.setText(resultText);
                        
                    } catch (Exception e) {
                        resultLabel.setText("<html><div style='text-align:center; color: red;'>Error calculating results: " + 
                                          e.getMessage() + "</div></html>");
                        System.out.println("Calculation error: " + e.getMessage());
                        e.printStackTrace();
                    } finally {
                        calculateButton.setEnabled(true);
                        calculateButton.setText("Calculate Investment Growth");
                    }
                }
            };
            calculationWorker.execute();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid numbers for investment amount and years.", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}