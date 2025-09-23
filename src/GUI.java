import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

@SuppressWarnings("unused")

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
    
    // Modern color palette matching StockCalculatorDialog
    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color SUCCESS_GREEN = new Color(46, 204, 113);
    private final Color WARNING_ORANGE = new Color(243, 156, 18);
    private final Color DANGER_RED = new Color(231, 76, 60);
    private final Color DARK_GRAY = new Color(52, 73, 94);
    private final Color CARD_WHITE = new Color(255, 255, 255);
    private final Color BACKGROUND_GRAY = new Color(247, 249, 252);
    private final Color LIGHT_GRAY = new Color(230, 230, 230);

    // Overflow protection constants
    private static final double MAX_DISPLAY_VALUE = 1e12; // 1 trillion
    private static final double MAX_PRINCIPAL = 1e9;      // 1 billion max principal
    private static final double MAX_RATE = 100.0;         // 100% max rate
    private static final int MAX_YEARS = 100;             // 100 years max
    private static final double MAX_CONTRIBUTION = 1e8;   // 100 million max contribution

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
        
        // Set a proper initial size instead of maximizing
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = Math.min(1200, (int)(screenSize.width * 0.9));
        int height = Math.min(800, (int)(screenSize.height * 0.9));
        frame.setSize(width, height);
        
        // Set minimum size to prevent the window from becoming too small
        frame.setMinimumSize(new Dimension(800, 600));
        
        // Center the window on screen
        frame.setLocationRelativeTo(null);
        
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BACKGROUND_GRAY);
        
        // Add window state listener to handle dragging properly
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                // Ensure the window doesn't become too small when dragged
                if (frame.getWidth() < 800) {
                    frame.setSize(800, frame.getHeight());
                }
                if (frame.getHeight() < 600) {
                    frame.setSize(frame.getWidth(), 600);
                }
            }
        });
    }

    private void initPanels() {
        topPanel = new JPanel();
        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_GRAY);
        inputPanel = new JPanel();
        outputPanel = new JPanel();
    }

    private void buildTopPanel() {
        topPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_BLUE, w, h, new Color(109, 213, 237));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        topPanel.setPreferredSize(new Dimension(100, 160));
        topPanel.setLayout(new BorderLayout());

        // Title container
        JPanel titleContainer = new JPanel();
        titleContainer.setOpaque(false);
        JLabel title = new JLabel("Teen Investment Calculator");
        title.setFont(new Font("Segoe UI", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        titleContainer.add(title);
        
        // Modern button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        // Stock Calculator Button with image background
        JButton stockButton = createImageButton("/images/Stock.png", "", 200, 45);
        stockButton.addActionListener(e -> openStockCalculator());
            
        // Utility Calculator Button with image background
        JButton utilityButton = createImageButton("/images/Utility.png", "", 200, 45);
        utilityButton.addActionListener(e -> openUtilityCalculator());
        
        // AI Learning Assistant Button with image background
        JButton aiButton = createImageButton("/images/AI.png", "", 220, 45);
        aiButton.addActionListener(e -> openClaudeChatbot());
        
        // About Button with image background
        JButton aboutButton = createImageButton("/images/About.png", "", 200, 45);
        aboutButton.addActionListener(e -> showAbout());
        
        buttonPanel.add(stockButton);
        buttonPanel.add(utilityButton);
        buttonPanel.add(aiButton);
        buttonPanel.add(aboutButton);
        
        topPanel.add(titleContainer, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createModernButton(String text, Color bgColor, int width) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Paint rounded background with hover states
                Color currentColor;
                if (getModel().isPressed()) {
                    currentColor = bgColor.darker().darker(); // Even darker when pressed
                } else if (getModel().isRollover()) {
                    currentColor = bgColor.darker(); // Darker when hovered
                } else {
                    currentColor = bgColor; // Normal state
                }
                
                g2d.setColor(currentColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Paint text centered
                FontMetrics fm = g2d.getFontMetrics();
                String buttonText = getText();
                int textWidth = fm.stringWidth(buttonText);
                int textX = (getWidth() - textWidth) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                
                g2d.setColor(getForeground());
                g2d.drawString(buttonText, textX, textY);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                // Don't paint default border
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(bgColor == WARNING_ORANGE || bgColor == SUCCESS_GREEN ? Color.BLACK : Color.WHITE);
        button.setPreferredSize(new Dimension(width, 45));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    // New method to create image buttons
    private JButton createImageButton(String imagePath, String text, int width, int height) {
        class ImageButton extends JButton {
            private boolean isHovered = false;
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
                    Image image = icon.getImage();
                    
                    // Create a rounded clipping area
                    Shape oldClip = g2d.getClip();
                    g2d.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                    
                    // Draw the image within the rounded clip
                    g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                    
                    // Restore the old clip
                    g2d.setClip(oldClip);
                    
                    // Add hover effect with rounded overlay
                    if (isHovered) {
                        g2d.setColor(new Color(0, 0, 0, 80)); // Semi-transparent black overlay
                        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        
                        // Add a subtle border for extra visual feedback
                        g2d.setColor(new Color(255, 255, 255, 150));
                        g2d.setStroke(new BasicStroke(2));
                        g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);
                    }
                    
                    // Add text overlay if provided
                    if (text != null && !text.isEmpty()) {
                        g2d.setColor(Color.WHITE);
                        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                        FontMetrics fm = g2d.getFontMetrics();
                        int textWidth = fm.stringWidth(text);
                        int textHeight = fm.getHeight();
                        int x = (getWidth() - textWidth) / 2;
                        int y = (getHeight() + textHeight) / 2 - 2;
                        
                        // Add text shadow for better visibility
                        g2d.setColor(new Color(0, 0, 0, 100));
                        g2d.drawString(text, x + 1, y + 1);
                        g2d.setColor(Color.WHITE);
                        g2d.drawString(text, x, y);
                    }
                } catch (Exception e) {
                    // Fallback to rounded colored button if image fails to load
                    g2d.setColor(getBackground());
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    if (text != null && !text.isEmpty()) {
                        g2d.setColor(getForeground());
                        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                        FontMetrics fm = g2d.getFontMetrics();
                        int textWidth = fm.stringWidth(text);
                        int x = (getWidth() - textWidth) / 2;
                        int y = (getHeight() + fm.getHeight()) / 2 - 2;
                        g2d.drawString(text, x, y);
                    }
                }
            }
            
            public void setHovered(boolean hovered) {
                this.isHovered = hovered;
                repaint();
            }
        }
        
        ImageButton button = new ImageButton();
        button.setPreferredSize(new Dimension(width, height));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Enhanced hover effect with image darkening
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setHovered(true);
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setHovered(false);
            }
        });
        
        return button;
    }

    private void buildInputPanel() {
        inputPanel.setBackground(BACKGROUND_GRAY);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        inputPanel.setLayout(new BorderLayout());

        // Main input container
        JPanel mainInputContainer = new JPanel();
        mainInputContainer.setLayout(new BoxLayout(mainInputContainer, BoxLayout.Y_AXIS));
        mainInputContainer.setOpaque(false);

        // Investment Type Card
        JPanel typeCard = createCard("Investment Type", createInvestmentTypePanel());
        mainInputContainer.add(typeCard);
        mainInputContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Investment Details Card
        JPanel detailsCard = createCard("Investment Details", createInvestmentDetailsPanel());
        mainInputContainer.add(detailsCard);
        mainInputContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Calculate Button
        JButton calculateButton = createCalculateButton();
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.setOpaque(false);
        buttonContainer.add(calculateButton);
        mainInputContainer.add(buttonContainer);
        mainInputContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        // Saved Calculations Card
        JPanel savedCard = createCard("Load Saved Calculation", createSavedCalculationsPanel());
        mainInputContainer.add(savedCard);

        inputPanel.add(mainInputContainer, BorderLayout.NORTH);
        
        // Initialize field visibility
        updateFieldsVisibility();
    }

    private JPanel createCard(String title, JPanel content) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(20, 0, 20, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, content.getPreferredSize().height + 80));
        
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

    private JPanel createInvestmentTypePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        
        investmentTypeBox = new JComboBox<>(options);
        investmentTypeBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        investmentTypeBox.setPreferredSize(new Dimension(350, 40));
        investmentTypeBox.setBackground(Color.WHITE);
        investmentTypeBox.addActionListener(e -> updateFieldsVisibility());
        investmentTypeBox.setToolTipText("<html><b>Investment Type:</b> Different ways money can grow or shrink over time.<br>" +
                                         "Hover over each option after selecting to learn more!</html>");
        
        panel.add(investmentTypeBox);
        return panel;
    }

    private JPanel createInvestmentDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        nameField = createStyledTextField("");
        amountField = createStyledTextField("");
        contributionField = createStyledTextField("");
        rateField = createStyledTextField("");
        yearsField = createStyledTextField("");

        // Add tooltips to text fields
        nameField.setToolTipText("<html><b>Name:</b> Give your calculation a nickname to save it for later.<br>" +
                                 "Like 'College Fund' or 'Car Savings'!</html>");
        
        // Add input fields with modern styling
        panel.add(createModernInputField("Name (optional):", nameField, nameLabel));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(createModernInputField("Initial Amount ($):", amountField, amountLabel));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Contribution field (initially hidden)
        contributionPanel = createModernInputField("Annual Contribution ($):", contributionField, contributionLabel);
        contributionPanel.setVisible(false);
        panel.add(contributionPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(createModernInputField("Interest Rate (%):", rateField, rateLabel));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panel.add(createModernInputField("Years:", yearsField, yearsLabel));
        
        return panel;
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

    private JPanel createModernInputField(String labelText, JTextField textField, JLabel label) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 5));
        fieldPanel.setOpaque(false);
        fieldPanel.setMaximumSize(new Dimension(450, 45));

        label.setText(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(DARK_GRAY);
        label.setPreferredSize(new Dimension(200, 25));
        
        setLabelTooltip(label, labelText);
        setTextFieldTooltip(textField, labelText);

        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(textField, BorderLayout.CENTER);
        
        return fieldPanel;
    }

    private JButton createCalculateButton() {
        JButton button = new JButton("Calculate My Investment") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Paint rounded background with hover states
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(35, 155, 86)); // Darker when pressed
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(39, 174, 96)); // Lighter when hovered
                } else {
                    g2d.setColor(SUCCESS_GREEN); // Normal state
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Paint text centered
                FontMetrics fm = g2d.getFontMetrics();
                String text = getText();
                int textWidth = fm.stringWidth(text);
                int textX = (getWidth() - textWidth) / 2;
                int textY = (getHeight() + fm.getAscent()) / 2 - 2;
                
                g2d.setColor(getForeground());
                g2d.drawString(text, textX, textY);
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                // Don't paint default border - we want only the rounded background
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(350, 50));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> handleCalculate());
        
        return button;
    }

    private JPanel createSavedCalculationsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        
        storageBox = new JComboBox<>(Write.getNames(true));
        storageBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        storageBox.setPreferredSize(new Dimension(350, 40));
        storageBox.setBackground(Color.WHITE);
        storageBox.addActionListener(e -> loadSave());
        storageBox.setToolTipText("<html><b>Saved Calculations:</b> Pick a previous calculation to load it back.<br>" +
                                  "Great for comparing different investment scenarios!</html>");
        
        panel.add(storageBox);
        return panel;
    }

    private void buildOutputPanel() {
        outputPanel.setBackground(CARD_WHITE);
        outputPanel.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(15, LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        outputPanel.setLayout(new BorderLayout());

        // Results header
        JPanel resultsHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resultsHeader.setOpaque(false);
        
        resultLabel = new JLabel("Ready to calculate your future wealth!");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        resultLabel.setForeground(PRIMARY_BLUE);
        resultsHeader.add(resultLabel);
        
        outputPanel.add(resultsHeader, BorderLayout.NORTH);
        
        // Placeholder for graph
        JPanel graphArea = new JPanel();
        graphArea.setBackground(Color.WHITE);
        graphArea.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel placeholderLabel = new JLabel("Your investment growth chart will appear here");
        placeholderLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        placeholderLabel.setForeground(new Color(127, 140, 141));
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        graphArea.add(placeholderLabel);
        
        outputPanel.add(graphArea, BorderLayout.CENTER);
    }

    private void buildBottomPanel() {
        // Create a scroll pane for the input panel
        JScrollPane inputScrollPane = new JScrollPane(inputPanel);
        inputScrollPane.setPreferredSize(new Dimension(500, 600));
        inputScrollPane.setBorder(null);
        inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inputScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Set a reasonable preferred size for the output panel
        outputPanel.setPreferredSize(new Dimension(700, 600));

        bottomPanel.add(inputScrollPane, BorderLayout.WEST);
        bottomPanel.add(outputPanel, BorderLayout.CENTER);
    }

    // Input validation methods
    private boolean validateInputs(double principal, double rate, int years, double contribution) {
        // Check for reasonable limits
        if (principal > MAX_PRINCIPAL) {
            showValidationError("Initial amount is too large. Maximum: $" + formatCurrencyCompact(MAX_PRINCIPAL));
            return false;
        }
        
        if (rate > MAX_RATE) {
            showValidationError("Interest rate is too high. Maximum: " + MAX_RATE + "%");
            return false;
        }
        
        if (years > MAX_YEARS) {
            showValidationError("Time period is too long. Maximum: " + MAX_YEARS + " years");
            return false;
        }
        
        if (contribution > MAX_CONTRIBUTION) {
            showValidationError("Annual contribution is too large. Maximum: $" + formatCurrencyCompact(MAX_CONTRIBUTION));
            return false;
        }
        
        // Check for potential overflow before calculation
        if (principal > 0 && rate > 0 && years > 0) {
            double growthFactor = Math.pow(1 + rate, years);
            if (growthFactor > (MAX_DISPLAY_VALUE / principal)) {
                showValidationError("This combination of values will result in numbers too large to display.\nTry reducing the principal, rate, or time period.");
                return false;
            }
        }
        
        return true;
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(frame, 
            message, 
            "Input Too Large", 
            JOptionPane.WARNING_MESSAGE);
    }

    // Compact currency formatting for large numbers
    private String formatCurrencyCompact(double value) {
        if (value == 0) {
            return "$0";
        }
        
        if (Math.abs(value) >= 1e12) {
            return String.format("$%.2fT", value / 1e12);
        } else if (Math.abs(value) >= 1e9) {
            return String.format("$%.2fB", value / 1e9);
        } else if (Math.abs(value) >= 1e6) {
            return String.format("$%.2fM", value / 1e6);
        } else if (Math.abs(value) >= 1e3) {
            return String.format("$%.2fK", value / 1e3);
        } else {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
            currencyFormat.setMaximumFractionDigits(0);
            return currencyFormat.format(value);
        }
    }

    // Update result display with overflow protection
    private void updateResultDisplay(double futureValue, double principal) {
        if (futureValue > MAX_DISPLAY_VALUE || Double.isInfinite(futureValue) || Double.isNaN(futureValue)) {
            // Handle overflow case
            resultLabel.setForeground(DANGER_RED);
            resultLabel.setText("Result too large to display!");
            
            // Show overflow message in output panel
            outputPanel.removeAll();
            outputPanel.setBackground(CARD_WHITE);
            outputPanel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(15, DANGER_RED, 2),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
            ));
            
            JPanel errorPanel = new JPanel();
            errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
            errorPanel.setOpaque(false);
            
            JLabel errorTitle = new JLabel("Value Too Large!");
            errorTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
            errorTitle.setForeground(DANGER_RED);
            errorTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel errorMessage = new JLabel("Please use smaller input values");
            errorMessage.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            errorMessage.setForeground(DARK_GRAY);
            errorMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel suggestions = new JLabel("<html><center>Suggestions:<br>• Reduce initial amount<br>• Lower interest rate<br>• Fewer years</center></html>");
            suggestions.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            suggestions.setForeground(DARK_GRAY);
            suggestions.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            errorPanel.add(errorTitle);
            errorPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            errorPanel.add(errorMessage);
            errorPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            errorPanel.add(suggestions);
            
            outputPanel.add(errorPanel, BorderLayout.CENTER);
            outputPanel.revalidate();
            outputPanel.repaint();
            return;
        }
        
        // Normal result display
        double totalChange = futureValue - principal;
        if (totalChange > 0) {
            resultLabel.setForeground(SUCCESS_GREEN);
            resultLabel.setText("Future Value: " + formatCurrencyCompact(futureValue) + "!");
        } else if (totalChange < 0) {
            resultLabel.setForeground(DANGER_RED);
            resultLabel.setText("Future Value: " + formatCurrencyCompact(futureValue));
        } else {
            resultLabel.setForeground(DARK_GRAY);
            resultLabel.setText("Future Value: " + formatCurrencyCompact(futureValue));
        }
    }

    // Helper method to set tooltips for labels
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
    
    // Helper method to set tooltips for text fields
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

            // Get contribution if applicable
            double contribution = 0;
            String selected = (String) investmentTypeBox.getSelectedItem();
            if (selected.equals(options[2])) { // With Annual Contributions
                try {
                    String contributionText = contributionField.getText().trim();
                    if (!contributionText.isEmpty()) {
                        contribution = Double.parseDouble(contributionText);
                    }
                } catch (NumberFormatException e) {
                    contribution = 0;
                }
            }
            
            // Validate inputs before proceeding
            if (!validateInputs(principal, rate, years, contribution)) {
                return; // Stop execution if validation fails
            }

            double futureValue = 0;
            java.util.List<Double> values = new java.util.ArrayList<>();
            values.add(principal);

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
                for (int i = 1; i <= years; i++) {
                    double v = InvestmentLogic.calculateWithContributions(principal, contribution, rate, i);
                    values.add(v);
                }
                futureValue = values.get(values.size() - 1);
                Write.storeAnnualContributions(name, contribution, principal, rate, years);
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
            
            // Update result display with overflow protection
            updateResultDisplay(futureValue, principal);

            // Only show graph if values are within display limits
            if (futureValue <= MAX_DISPLAY_VALUE && !Double.isInfinite(futureValue) && !Double.isNaN(futureValue)) {
                // Show graph in outputPanel with modern styling
                outputPanel.removeAll();
                outputPanel.setBackground(CARD_WHITE);
                outputPanel.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(15, SUCCESS_GREEN, 2),
                    BorderFactory.createEmptyBorder(30, 30, 30, 30)
                ));
                
                // Results header
                JPanel resultsHeader = new JPanel(new FlowLayout(FlowLayout.CENTER));
                resultsHeader.setOpaque(false);
                resultsHeader.add(resultLabel);
                outputPanel.add(resultsHeader, BorderLayout.NORTH);
                
                // Add summary panel
                JPanel summaryPanel = createSummaryPanel(principal, futureValue, years, rate);
                outputPanel.add(summaryPanel, BorderLayout.SOUTH);
                
                // Graph
                ValueProjectionGraphPanel graphPanel = new ValueProjectionGraphPanel(values, years);
                graphPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
                outputPanel.add(graphPanel, BorderLayout.CENTER);
                
                outputPanel.revalidate();
                outputPanel.repaint();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, 
                "Please enter valid numbers in all fields.", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
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
    
    private JPanel createSummaryPanel(double principal, double futureValue, int years, double rate) {
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 40));
        
        // Check for overflow before creating summary
        if (futureValue > MAX_DISPLAY_VALUE || Double.isInfinite(futureValue) || Double.isNaN(futureValue)) {
            return summaryPanel; // Return empty panel
        }
        
        double totalReturn = futureValue - principal;
        double percentReturn = (totalReturn / principal) * 100;
        
        // Cap percentage display at reasonable levels
        if (percentReturn > 999999) {
            percentReturn = 999999;
        }
        
        summaryPanel.add(createMetricCard("Total Return", 
            String.format("%s%s", totalReturn >= 0 ? "+" : "", formatCurrencyCompact(Math.abs(totalReturn))),
            totalReturn >= 0 ? SUCCESS_GREEN : DANGER_RED));
        
        summaryPanel.add(createMetricCard("Percentage Gain", 
            String.format("%.1f%%", Math.min(percentReturn, 999999)),
            percentReturn >= 0 ? SUCCESS_GREEN : DANGER_RED));
        
        summaryPanel.add(createMetricCard("Annual Rate", 
            String.format("%.1f%%", rate * 100),
            PRIMARY_BLUE));
        
        return summaryPanel;
    }
    
    private JPanel createMetricCard(String label, String value, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelComponent.setForeground(DARK_GRAY);
        labelComponent.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueComponent.setForeground(valueColor);
        valueComponent.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(labelComponent);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(valueComponent);
        
        return card;
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
        // Create custom about dialog with modern styling
        JDialog aboutDialog = new JDialog(frame, "About Investment Calculator", true);
        aboutDialog.setLayout(new BorderLayout());
        aboutDialog.setSize(500, 450);
        aboutDialog.setLocationRelativeTo(frame);
        
        // Header panel
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_BLUE, w, h, new Color(109, 213, 237));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        headerPanel.setPreferredSize(new Dimension(500, 80));
        headerPanel.setLayout(new GridBagLayout());
        
        JLabel aboutTitle = new JLabel("Teen Investment Calculator");
        aboutTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        aboutTitle.setForeground(Color.WHITE);
        headerPanel.add(aboutTitle);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        String[] features = {
            "Modern, intuitive interface designed for teens",
            "Multiple investment calculation types",
            "Real-time stock market simulator",
            "AI-powered learning assistant",
            "Utility calculator for smart spending decisions",
            "Save and load your calculations",
            "Beautiful interactive growth charts",
            "Educational tooltips throughout",
            "Overflow protection for large numbers"
        };
        
        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            featureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(featureLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel versionLabel = new JLabel("Version 3.1 - Overflow Protection Update");
        versionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        versionLabel.setForeground(PRIMARY_BLUE);
        versionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(versionLabel);
        
        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        JButton closeButton = createModernButton("Close", PRIMARY_BLUE, 100);
        closeButton.addActionListener(e -> aboutDialog.dispose());
        buttonPanel.add(closeButton);
        
        aboutDialog.add(headerPanel, BorderLayout.NORTH);
        aboutDialog.add(contentPanel, BorderLayout.CENTER);
        aboutDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        aboutDialog.setVisible(true);
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
    
    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
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