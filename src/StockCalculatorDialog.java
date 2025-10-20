import javax.swing.*;
import java.awt.*;
import java.util.Map;
import javax.swing.border.*;

public class StockCalculatorDialog extends JDialog {
    private JComboBox<String> stockCombo;
    private JTextField investmentField, yearsField;
    private JLabel currentPriceLabel, riskLabel, descriptionLabel;
    private JPanel resultPanel;
    private Map<String, String> stockMap;
    
    // Custom colors for modern look
    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color SUCCESS_GREEN = new Color(46, 204, 113);
    private final Color WARNING_ORANGE = new Color(243, 156, 18);
    private final Color DANGER_RED = new Color(231, 76, 60);
    private final Color DARK_GRAY = new Color(52, 73, 94);
    private final Color CARD_WHITE = new Color(255, 255, 255);
    
    public StockCalculatorDialog(JFrame parent) {
        super(parent, "Stock Investment Calculator", true);
        this.stockMap = EnhancedStockRates.getPopularStocks();
        initComponents();
        setupLayout();
        setSize(950, 900);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    @SuppressWarnings("unused")
    private void initComponents() {
        // Create styled combo box with stock options
        String[] stockOptions = new String[stockMap.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : stockMap.entrySet()) {
            stockOptions[i++] = entry.getKey() + " - " + entry.getValue();
        }
        
        stockCombo = new JComboBox<>(stockOptions);
        stockCombo.setMaximumRowCount(11);
        stockCombo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        stockCombo.setBackground(Color.WHITE);
        stockCombo.setPreferredSize(new Dimension(300, 50));
        stockCombo.addActionListener(e -> updateStockInfo());
        
        investmentField = createStyledTextField("");
        yearsField = createStyledTextField("");
        
        currentPriceLabel = new JLabel("Loading...");
        currentPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        currentPriceLabel.setForeground(PRIMARY_BLUE);
        
        riskLabel = new JLabel("");
        riskLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        descriptionLabel = new JLabel("");
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionLabel.setForeground(DARK_GRAY);
        
        // Load first stock info
        SwingUtilities.invokeLater(() -> updateStockInfo());
    }
    
    private JTextField createStyledTextField(String text) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(200, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(Color.WHITE);
        return field;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(247, 249, 252));
        
        // Main container with padding
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(new Color(247, 249, 252));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Header with gradient effect
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Stock selection card
        JPanel selectionCard = createCard("Select Your Stock", createStockSelectionPanel());
        mainContainer.add(selectionCard);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Investment details card
        JPanel investmentCard = createCard("Investment Details", createInvestmentPanel());
        mainContainer.add(investmentCard);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Stock info card
        JPanel infoCard = createCard("Stock Information", createStockInfoPanel());
        mainContainer.add(infoCard);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Calculate button
        JButton calculateButton = createCalculateButton();
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.setOpaque(false);
        buttonContainer.add(calculateButton);
        mainContainer.add(buttonContainer);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Results card (initially hidden)
        resultPanel = createResultPanel();
        resultPanel.setVisible(false);
        mainContainer.add(resultPanel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Educational footer
        JPanel footerPanel = createFooterPanel();
        mainContainer.add(footerPanel);
        
        // Add to scroll pane for better handling
        JScrollPane scrollPane = new JScrollPane(mainContainer);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // Increase wheel/page scroll speed while keeping bars hidden
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);
        scrollPane.getVerticalScrollBar().setBlockIncrement(160);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(41, 128, 185);
                Color color2 = new Color(109, 213, 237);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, w, h, 20, 20);
            }
        };
        panel.setPreferredSize(new Dimension(800, 100));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        panel.setLayout(new GridBagLayout());
        
        JLabel titleLabel = new JLabel("TeenVestors' Stock Investment Simulator");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Learn how stocks work with real companies!");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 0);
        panel.add(subtitleLabel, gbc);
        
        return panel;
    }
    
    private JPanel createCard(String title, JPanel content) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, content.getPreferredSize().height + 60));
        
        if (title != null && !title.isEmpty()) {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(DARK_GRAY);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
            card.add(titleLabel, BorderLayout.NORTH);
        }
        
        card.add(content, BorderLayout.CENTER);
        return card;
    }
    
    private JPanel createStockSelectionPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        
        JLabel label = new JLabel("Choose a company: ");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(DARK_GRAY);
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(stockCombo);
        
        return panel;
    }
    
    private JPanel createInvestmentPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 15));
        panel.setOpaque(false);
        
        // Investment amount
        JLabel amountLabel = new JLabel("How much to invest?");
        amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        amountLabel.setForeground(DARK_GRAY);
        
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        amountPanel.setOpaque(false);
        JLabel dollarSign = new JLabel("$");
        dollarSign.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dollarSign.setForeground(SUCCESS_GREEN);
        amountPanel.add(dollarSign);
        amountPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        amountPanel.add(investmentField);
        
        // Years
        JLabel yearsLabel = new JLabel("How many years?");
        yearsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        yearsLabel.setForeground(DARK_GRAY);
        
        JPanel yearsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        yearsPanel.setOpaque(false);
        yearsPanel.add(yearsField);
        JLabel yearsText = new JLabel(" years");
        yearsText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        yearsText.setForeground(DARK_GRAY);
        yearsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        yearsPanel.add(yearsText);
        
        panel.add(amountLabel);
        panel.add(yearsLabel);
        panel.add(amountPanel);
        panel.add(yearsPanel);
        
        return panel;
    }
    
    private JPanel createStockInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        // Price display with icon
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pricePanel.setOpaque(false);
        pricePanel.add(currentPriceLabel);
        
        // Risk level with color coding
        JPanel riskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        riskPanel.setOpaque(false);
        riskPanel.add(riskLabel);
        
        // Company description
        JPanel descPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        descPanel.setOpaque(false);
        descPanel.add(descriptionLabel);
        
        panel.add(pricePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(riskPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(descPanel);
        
        return panel;
    }

    @SuppressWarnings("unused")
    private JButton createCalculateButton() {
        JButton button = new JButton("Calculate My Investment Growth");
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(SUCCESS_GREEN);
        button.setPreferredSize(new Dimension(400, 50));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS_GREEN);
            }
        });
        
        button.addActionListener(e -> calculateGrowth());
        return button;
    }
    
    private JPanel createResultPanel() {
        JPanel panel = createCard("Your Investment Results", new JPanel());
        panel.setBackground(new Color(232, 248, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, SUCCESS_GREEN, 2),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel warningLabel = new JLabel("<html><center><b>Remember:</b> This is for learning only! " +
            "Real investing involves risk.<br>Stock prices go up AND down. Always research before investing real money!</center></html>");
        warningLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        warningLabel.setForeground(new Color(127, 140, 141));
        warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(warningLabel);
        return panel;
    }
    
    private void updateStockInfo() {
        String selected = (String) stockCombo.getSelectedItem();
        if (selected == null) return;
        
        String symbol = selected.split(" - ")[0];
        
        // Update risk level with color coding
        String risk = EnhancedStockRates.getRiskLevel(symbol);
        riskLabel.setText("Risk Level: " + risk);
        if (risk.contains("LOW")) {
            riskLabel.setForeground(SUCCESS_GREEN);
            riskLabel.setText("" + risk);
        } else if (risk.contains("HIGH")) {
            riskLabel.setForeground(DANGER_RED);
            riskLabel.setText("" + risk);
        } else {
            riskLabel.setForeground(WARNING_ORANGE);
            riskLabel.setText("" + risk);
        }
        
        // Update description
        descriptionLabel.setText("<html><div style='width:500px;'>" + 
            EnhancedStockRates.getCompanyDescription(symbol) + "</div></html>");
        
        // Fetch price in background
        SwingUtilities.invokeLater(() -> {
            currentPriceLabel.setText("Loading price...");
            currentPriceLabel.setForeground(new Color(127, 140, 141));
        });
        
        new SwingWorker<Double, Void>() {
            @Override
            protected Double doInBackground() throws Exception {
                return EnhancedStockRates.getCurrentStockPrice(symbol);
            }
            
            @Override
            protected void done() {
                try {
                    double price = get();
                    if (price > 0) {
                        currentPriceLabel.setText(String.format("$%.2f per share", price));
                        currentPriceLabel.setForeground(PRIMARY_BLUE);
                    } else {
                        currentPriceLabel.setText("Price unavailable");
                        currentPriceLabel.setForeground(new Color(127, 140, 141));
                    }
                } catch (Exception e) {
                    currentPriceLabel.setText("Error loading price");
                    currentPriceLabel.setForeground(DANGER_RED);
                }
            }
        }.execute();
    }
    
    private void calculateGrowth() {
        try {
            String selected = (String) stockCombo.getSelectedItem();
            String symbol = selected.split(" - ")[0];
            
            double investment = Double.parseDouble(investmentField.getText());
            int years = Integer.parseInt(yearsField.getText());
            
            if (investment <= 0 || years <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter positive values!", 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Show loading state
            resultPanel.removeAll();
            JLabel loadingLabel = new JLabel("Calculating...");
            loadingLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            resultPanel.add(loadingLabel);
            resultPanel.setVisible(true);
            resultPanel.revalidate();
            resultPanel.repaint();
            
            // Calculate in background
            new SwingWorker<Double, Void>() {
                @Override
                protected Double doInBackground() throws Exception {
                    return EnhancedStockRates.calculateStockGrowth(symbol, investment, years);
                }
                
                @Override
                protected void done() {
                    try {
                        double futureValue = get();
                        showResults(symbol, investment, years, futureValue);
                    } catch (Exception e) {
                        resultPanel.setVisible(false);
                        JOptionPane.showMessageDialog(StockCalculatorDialog.this, 
                            "Error calculating results", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid numbers!", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showResults(String symbol, double investment, int years, double futureValue) {
        resultPanel.removeAll();
        resultPanel.setLayout(new BorderLayout());
        
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        
        // Title
        JLabel titleLabel = new JLabel("Your Investment Results");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(DARK_GRAY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        double totalGain = futureValue - investment;
        double percentGain = (totalGain / investment) * 100;
        
        // Create result display with icons and colors
        JPanel resultsGrid = new JPanel(new GridLayout(4, 2, 15, 10));
        resultsGrid.setOpaque(false);
        resultsGrid.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        addResultRow(resultsGrid, "Stock:", symbol);
        addResultRow(resultsGrid, "You invested:", String.format("$%.2f", investment));
        addResultRow(resultsGrid, "After " + years + " years:", String.format("$%.2f", futureValue));
        
        String gainText = String.format("$%.2f (%.1f%%)", totalGain, percentGain);
        JLabel gainLabel = new JLabel(gainText);
        gainLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gainLabel.setForeground(totalGain > 0 ? SUCCESS_GREEN : DANGER_RED);
        addResultRow(resultsGrid, totalGain > 0 ? "Your profit:" : "Your loss:", gainLabel);
        
        // Motivational message
        String message;
        if (percentGain > 100) {
            message = "Amazing! Your money more than doubled!";
        } else if (percentGain > 50) {
            message = "Great job! That's solid growth!";
        } else if (percentGain > 0) {
            message = "Nice! You made money!";
        } else {
            message = "Remember, investing is about learning!";
        }
        
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        messageLabel.setForeground(PRIMARY_BLUE);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        content.add(titleLabel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(resultsGrid);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(messageLabel);
        
        resultPanel.add(content, BorderLayout.CENTER);
        resultPanel.setVisible(true);
        resultPanel.revalidate();
        resultPanel.repaint();
    }
    
    private void addResultRow(JPanel panel, String label, Object value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelComponent.setForeground(DARK_GRAY);
        
        if (value instanceof String) {
            JLabel valueLabel = new JLabel((String) value);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            valueLabel.setForeground(DARK_GRAY);
            panel.add(labelComponent);
            panel.add(valueLabel);
        } else {
            panel.add(labelComponent);
            panel.add((JComponent) value);
        }
    }
    
    // Custom rounded border class
    private static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;
        private int thickness;
        
        RoundedBorder(int radius, Color color, int thickness) {
            this.radius = radius;
            this.color = color;
            this.thickness = thickness;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(thickness));
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }
    }
}