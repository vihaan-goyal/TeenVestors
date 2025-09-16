import javax.swing.*;
import java.awt.*;

public class UtilityCalculatorDialog extends JDialog {
    private JTextField happinessField, frequencyField, convenienceField, lifestyleField, 
                      timeYearsField, lifespanField, priceField;
    private JLabel resultLabel;

    public UtilityCalculatorDialog(JFrame parent) {
        super(parent, "Utility Calculator", true);
        initComponents();
        setupLayout();
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        happinessField = new JTextField(10);
        frequencyField = new JTextField(10);
        convenienceField = new JTextField(10);
        lifestyleField = new JTextField(10);
        timeYearsField = new JTextField(10);
        lifespanField = new JTextField(10);
        priceField = new JTextField(10);
        
        resultLabel = new JLabel("<html><div style='text-align: center;'>Utility Per Dollar: Not calculated yet<br>Fill in all fields and click Calculate</div></html>");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Utility Calculator");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Add input fields with better descriptions
        addField(inputPanel, "Happiness (1-10):", happinessField, 0, gbc, "How happy does this make you?");
        addField(inputPanel, "Usage Frequency (per month):", frequencyField, 1, gbc, "How often will you use this?");
        addField(inputPanel, "Convenience (1-10):", convenienceField, 2, gbc, "How convenient/easy to use?");
        addField(inputPanel, "Lifestyle Impact (1-10):", lifestyleField, 3, gbc, "How much does this improve your life?");
        addField(inputPanel, "Expected Usage (years):", timeYearsField, 4, gbc, "How long will you use this?");
        addField(inputPanel, "Product Lifespan (years):", lifespanField, 5, gbc, "How long until it breaks/wears out?");
        addField(inputPanel, "Price ($):", priceField, 6, gbc, "What does it cost?");

        // Calculate button
        JButton calculateButton = new JButton("Calculate Utility Per Dollar");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 16));
        calculateButton.setBackground(new Color(70, 130, 180));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.addActionListener(e -> calculateUtility());
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 8, 8, 8);
        inputPanel.add(calculateButton, gbc);

        // Result panel
        JPanel resultPanel = new JPanel();
        resultPanel.setBorder(BorderFactory.createTitledBorder("Result"));
        resultPanel.add(resultLabel);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        add(titlePanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);
    }

    private void addField(JPanel panel, String labelText, JTextField field, int row, GridBagConstraints gbc, String tooltip) {
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label, gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setToolTipText(tooltip);
        panel.add(field, gbc);
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
            if (happiness < 1 || happiness > 10 || convenience < 1 || convenience > 10 || lifestyleConvenience < 1 || lifestyleConvenience > 10) {
                JOptionPane.showMessageDialog(this, "Happiness, Convenience, and Lifestyle Impact must be between 1 and 10.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (frequency < 0 || timeYears <= 0 || lifespan <= 0 || price <= 0) {
                JOptionPane.showMessageDialog(this, "Frequency, Time, Lifespan, and Price must be positive numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int utilityResult = InvestmentLogic.utilityPerDollar(happiness, frequency, convenience, 
                                                               lifestyleConvenience, timeYears, lifespan, price);
            
            String interpretation;
            if (utilityResult > 15) interpretation = "Excellent value!";
            else if (utilityResult > 10) interpretation = "Good value";
            else if (utilityResult > 5) interpretation = "Fair value";
            else interpretation = "Poor value - consider alternatives";
            
            resultLabel.setText("<html><div style='text-align: center;'><b>Utility Per Dollar: " + utilityResult + "</b><br>" + interpretation + "</div></html>");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (ArithmeticException e) {
            JOptionPane.showMessageDialog(this, "Error in calculation: " + e.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}