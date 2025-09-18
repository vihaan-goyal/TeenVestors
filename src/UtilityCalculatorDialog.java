import javax.swing.*;
import java.awt.*;

@SuppressWarnings("unused")


public class UtilityCalculatorDialog extends JDialog {
    private JTextField happinessField, frequencyField, convenienceField, lifestyleField, 
                      timeYearsField, lifespanField, priceField;
    private JLabel resultLabel;
    private JPanel resultPanel;
    
    // Modern color palette matching StockCalculatorDialog
    private final Color PRIMARY_BLUE = new Color(41, 128, 185);
    private final Color SUCCESS_GREEN = new Color(46, 204, 113);
    private final Color WARNING_ORANGE = new Color(243, 156, 18);
    private final Color DANGER_RED = new Color(231, 76, 60);
    private final Color DARK_GRAY = new Color(52, 73, 94);
    private final Color CARD_WHITE = new Color(255, 255, 255);
    private final Color BACKGROUND_GRAY = new Color(247, 249, 252);

    public UtilityCalculatorDialog(JFrame parent) {
        super(parent, "Utility Calculator", true);
        initComponents();
        setupLayout();
        setSize(950, 800);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        happinessField = createStyledTextField("");
        frequencyField = createStyledTextField("");
        convenienceField = createStyledTextField("");
        lifestyleField = createStyledTextField("");
        timeYearsField = createStyledTextField("");
        lifespanField = createStyledTextField("");
        priceField = createStyledTextField("");
        
        resultLabel = new JLabel("Ready to calculate utility");
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        resultLabel.setForeground(PRIMARY_BLUE);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private JTextField createStyledTextField(String text) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(250, 45));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setBackground(Color.WHITE);
        return field;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_GRAY);
        
        // Main container with padding
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(BACKGROUND_GRAY);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Header with gradient effect
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Happiness & Usage card
        JPanel happinessCard = createCard("Happiness & Usage", createHappinessPanel());
        mainContainer.add(happinessCard);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Value & Time card
        JPanel valueCard = createCard("Value & Time", createValuePanel());
        mainContainer.add(valueCard);
        mainContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Price card
        JPanel priceCard = createCard("Cost", createPricePanel());
        mainContainer.add(priceCard);
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
        
        // Add to scroll pane
        JScrollPane scrollPane = new JScrollPane(mainContainer);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
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
                GradientPaint gp = new GradientPaint(0, 0, SUCCESS_GREEN, w, h, new Color(46, 204, 113, 180));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, w, h, 20, 20);
            }
        };
        panel.setPreferredSize(new Dimension(800, 100));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        panel.setLayout(new GridBagLayout());
        
        JLabel titleLabel = new JLabel("Is It Worth Buying?");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Calculate the true value you get per dollar!");
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
    
    private JPanel createHappinessPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 25, 20));
        panel.setOpaque(false);
        
        panel.add(createFieldWithIcon("Happiness (1-10):", happinessField, 
            "How happy does this make you? 10 = pure joy!"));
        panel.add(createFieldWithIcon("Usage per Month:", frequencyField, 
            "How many times will you use it each month?"));
        panel.add(createFieldWithIcon("Convenience (1-10):", convenienceField, 
            "How convenient is it? 10 = super easy to use!"));
        panel.add(createFieldWithIcon("Lifestyle Impact (1-10):", lifestyleField, 
            "How much does it improve your daily life?"));
        
        return panel;
    }
    
    private JPanel createValuePanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 25, 20));
        panel.setOpaque(false);
        
        panel.add(createFieldWithIcon("Years You'll Use It:", timeYearsField, 
            "How long will you realistically use this?"));
        panel.add(createFieldWithIcon("Product Lifespan (years):", lifespanField, 
            "How long until it breaks or needs replacing?"));
        
        return panel;
    }
    
    private JPanel createPricePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setOpaque(false);
        
        JLabel dollarSign = new JLabel("Price: $");
        dollarSign.setFont(new Font("Segoe UI", Font.BOLD, 18));
        dollarSign.setForeground(DARK_GRAY);
        
        priceField.setPreferredSize(new Dimension(180, 45));
        priceField.setToolTipText("What's the total cost including tax?");
        
        panel.add(dollarSign);
        panel.add(Box.createRigidArea(new Dimension(15, 0)));
        panel.add(priceField);
        
        return panel;
    }
    
    private JPanel createFieldWithIcon(String labelText, JTextField field, String tooltip) {
        JPanel panel = new JPanel(new BorderLayout(15, 8));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(DARK_GRAY);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        field.setToolTipText(tooltip);
        field.setHorizontalAlignment(JTextField.CENTER);
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createCalculateButton() {
        JButton button = new JButton("Calculate Utility Score");
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(SUCCESS_GREEN);
        button.setPreferredSize(new Dimension(350, 55));
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
        
        button.addActionListener(e -> calculateUtility());
        return button;
    }
    
    private JPanel createResultPanel() {
        JPanel panel = createCard("Your Utility Score", new JPanel());
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
        
        JLabel tipLabel = new JLabel("<html><center><b>Pro Tip:</b> A score above 15 is excellent value!<br>" +
            "Compare scores between different items to make smarter buying decisions.</center></html>");
        tipLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        tipLabel.setForeground(new Color(127, 140, 141));
        tipLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(tipLabel);
        return panel;
    }
    
    private void calculateUtility() {
        try {
            int happiness = Integer.parseInt(happinessField.getText());
            int frequency = Integer.parseInt(frequencyField.getText());
            double convenience = Double.parseDouble(convenienceField.getText());
            int lifestyleConvenience = Integer.parseInt(lifestyleField.getText());
            int timeYears = Integer.parseInt(timeYearsField.getText());
            int lifespan = Integer.parseInt(lifespanField.getText());
            int price = Integer.parseInt(priceField.getText());

            // Validate inputs
            if (happiness < 1 || happiness > 10 || convenience < 1 || convenience > 10 || 
                lifestyleConvenience < 1 || lifestyleConvenience > 10) {
                showError("Ratings must be between 1 and 10!");
                return;
            }
            
            if (frequency < 0 || timeYears <= 0 || lifespan <= 0 || price <= 0) {
                showError("Time, frequency, and price must be positive numbers!");
                return;
            }

            int utilityResult = InvestmentLogic.utilityPerDollar(happiness, frequency, convenience, 
                                                               lifestyleConvenience, timeYears, lifespan, price);
            
            showResults(utilityResult, price);
            
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers in all fields!");
        } catch (ArithmeticException e) {
            showError("Error in calculation: " + e.getMessage());
        }
    }
    
    private void showResults(int utilityScore, int price) {
        resultPanel.removeAll();
        resultPanel.setLayout(new BorderLayout());
        
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        
        // Score display
        JLabel scoreLabel = new JLabel(String.valueOf(utilityScore));
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 84));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Interpretation with emoji
        String interpretation;
        Color scoreColor;
        
        if (utilityScore >= 20) {
            interpretation = "AMAZING VALUE!";
            scoreColor = SUCCESS_GREEN;
        } else if (utilityScore >= 15) {
            interpretation = "Excellent Value!";
            scoreColor = SUCCESS_GREEN;
        } else if (utilityScore >= 10) {
            interpretation = "Good Value";
            scoreColor = PRIMARY_BLUE;
        } else if (utilityScore >= 5) {
            interpretation = "Fair Value";
            scoreColor = WARNING_ORANGE;
        } else {
            interpretation = "Poor Value";
            scoreColor = DANGER_RED;
        }
        
        scoreLabel.setForeground(scoreColor);
        
        JLabel interpretationLabel = new JLabel(interpretation);
        interpretationLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        interpretationLabel.setForeground(scoreColor);
        interpretationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Advice panel
        JPanel advicePanel = new JPanel();
        advicePanel.setOpaque(false);
        advicePanel.setLayout(new BoxLayout(advicePanel, BoxLayout.Y_AXIS));
        advicePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        
        String[] advice = getAdvice(utilityScore, price);
        for (String tip : advice) {
            JLabel adviceLabel = new JLabel("• " + tip);
            adviceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            adviceLabel.setForeground(DARK_GRAY);
            adviceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            advicePanel.add(adviceLabel);
            advicePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        // Comparison suggestion
        JLabel compareLabel = new JLabel("<html><center><i>Compare this score with other items<br>you're considering to make the best choice!</i></center></html>");
        compareLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        compareLabel.setForeground(new Color(127, 140, 141));
        compareLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        content.add(scoreLabel);
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(interpretationLabel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(advicePanel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(compareLabel);
        
        resultPanel.add(content, BorderLayout.CENTER);
        resultPanel.setVisible(true);
        resultPanel.revalidate();
        resultPanel.repaint();
    }
    
    private String[] getAdvice(int score, int price) {
        java.util.List<String> advice = new java.util.ArrayList<>();
        
        if (score >= 15) {
            advice.add("This is a smart purchase that will bring you lots of value!");
            advice.add("The utility per dollar is excellent - go for it!");
        } else if (score >= 10) {
            advice.add("This is a decent purchase that provides good value.");
            advice.add("Consider if you really need it or if there are better alternatives.");
        } else if (score >= 5) {
            advice.add("The value is questionable - think carefully before buying.");
            advice.add("Look for alternatives that might give you better utility per dollar.");
            if (price > 100) {
                advice.add("For this price, you should expect much better utility!");
            }
        } else {
            advice.add("This is poor value for money - consider skipping this purchase.");
            advice.add("Save your money for something that brings more joy and utility!");
            advice.add("Ask yourself: Do I really need this? Can I find something better?");
        }
        
        return advice.toArray(new String[0]);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Custom rounded border class
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