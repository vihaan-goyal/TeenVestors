package src;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private JFrame frame;
    private JPanel topPanel, bottomPanel, leftPanel, rightPanel;
    private JTextField amountField, rateField, yearsField;
    private JLabel resultLabel;

    public GUI() {
        initFrame();
        initPanels();
        configureLayout();
    }

    private void initFrame() {
        frame = new JFrame("Investment Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLayout(new BorderLayout());
    }

    private void initPanels() {
        // Top panel (title)
        topPanel = new JPanel();
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.setPreferredSize(new Dimension(100, 100));
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.BLACK));

        JLabel title = new JLabel("Investment Calculator for Teens");
        title.setFont(new Font("Verdana", Font.BOLD, 60));
        title.setForeground(Color.BLACK);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(title);

        // Bottom panel holds left and right panels
        bottomPanel = new JPanel(new BorderLayout());

        // Left panel (inputs)
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 5, Color.BLACK));

        amountField = new JTextField();
        rateField = new JTextField();
        yearsField = new JTextField();

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(createLabeledField("Initial Amount ($):", amountField));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(createLabeledField("Interest Rate (%):", rateField));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(createLabeledField("Years:", yearsField));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 18));
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(calculateButton);
        leftPanel.add(Box.createVerticalGlue());

        // Right panel (output)
        rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 200));
        resultLabel = new JLabel("Future Value: ");
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 32));
        rightPanel.add(resultLabel);

        // Wire up the button to do calculation
        calculateButton.addActionListener(e -> {
            try {
                double principal = Double.parseDouble(amountField.getText());
                double rate = Double.parseDouble(rateField.getText()) / 100.0;
                int years = Integer.parseInt(yearsField.getText());
                double futureValue = principal * Math.pow(1 + rate, years);
                resultLabel.setText(String.format("Future Value: $%.2f", futureValue));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Set dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int panelHeight = screenSize.height - 120;
        leftPanel.setPreferredSize(new Dimension(screenSize.width / 2, panelHeight));
        rightPanel.setPreferredSize(new Dimension(screenSize.width / 2, panelHeight));
    }

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

    private void configureLayout() {
        frame.add(topPanel, BorderLayout.NORTH);
        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    public void show() {
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}
