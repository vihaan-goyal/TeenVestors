import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ValueProjectionGraphPanel extends JPanel {
    private final List<Double> values;
    private final int highlightYear;
    
    // Set maximum displayable value to prevent overflow
    private static final double MAX_DISPLAY_VALUE = 1e12; // 1 trillion
    private static final String OVERFLOW_MESSAGE = "Value too large to display";

    public ValueProjectionGraphPanel(List<Double> values, int highlightYear) {
        this.values = values;
        this.highlightYear = highlightYear;
        setPreferredSize(new Dimension(700, 400));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (values == null || values.size() < 2) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Insets insets = getInsets();
        int w = getWidth() - insets.left - insets.right;
        int h = getHeight() - insets.top - insets.bottom;
        int offsetX = insets.left;
        int offsetY = insets.top;
        
        if (w < 100 || h < 100) return;
        
        int padding = 80;
        int years = values.size() - 1;

        int graphWidth = w - 2 * padding;
        int graphHeight = h - 2 * padding;
        
        if (graphWidth < 50 || graphHeight < 50) return;

        double min = values.stream().min(Double::compareTo).orElse(0.0);
        double max = values.stream().max(Double::compareTo).orElse(1.0);
        
        // Check if any values exceed our display limit
        boolean hasOverflow = max > MAX_DISPLAY_VALUE;
        if (hasOverflow) {
            // Show overflow message instead of trying to render
            showOverflowMessage(g2, offsetX, offsetY, w, h);
            return;
        }
        
        if (max == min) max = min + 1;

        YAxisInfo yAxisInfo = calculateSmartYAxis(min, max);
        double yMin = yAxisInfo.min;
        double yMax = yAxisInfo.max;
        double[] yTicks = yAxisInfo.ticks;

        // Background grid lines
        g2.setColor(new Color(240, 240, 240));
        g2.setStroke(new BasicStroke(0.5f));
        
        for (double tickValue : yTicks) {
            int y = offsetY + h - padding - (int) ((tickValue - yMin) / (yMax - yMin) * graphHeight);
            g2.drawLine(offsetX + padding, y, offsetX + w - padding, y);
        }

        int numXTicks = Math.min(years, 10);
        for (int i = 0; i <= numXTicks; i++) {
            int x = offsetX + padding + (int)((i / (double) numXTicks) * graphWidth);
            g2.drawLine(x, offsetY + padding, x, offsetY + h - padding);
        }

        // Main axes
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(offsetX + padding, offsetY + h - padding, offsetX + w - padding, offsetY + h - padding);
        g2.drawLine(offsetX + padding, offsetY + padding, offsetX + padding, offsetY + h - padding);

        // Labels
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        
        String xLabel = "Year";
        int xLabelWidth = fm.stringWidth(xLabel);
        g2.drawString(xLabel, offsetX + (w - xLabelWidth) / 2, offsetY + h - 10);
        
        String yLabel = "Value ($)";
        g2.drawString(yLabel, offsetX + 10, offsetY + 20);

        // X-axis ticks and labels
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        fm = g2.getFontMetrics();
        
        for (int i = 0; i <= numXTicks; i++) {
            int year = i * years / numXTicks;
            int x = offsetX + padding + graphWidth * year / years;
            g2.drawLine(x, offsetY + h - padding, x, offsetY + h - padding + 5);
            
            String yearLabel = String.valueOf(year);
            int labelWidth = fm.stringWidth(yearLabel);
            g2.drawString(yearLabel, x - labelWidth/2, offsetY + h - padding + 20);
        }

        // Y-axis ticks and labels
        for (double tickValue : yTicks) {
            int y = offsetY + h - padding - (int) ((tickValue - yMin) / (yMax - yMin) * graphHeight);
            g2.drawLine(offsetX + padding - 5, y, offsetX + padding, y);
            
            String label = formatCurrencyCompact(tickValue);
            int labelWidth = fm.stringWidth(label);
            g2.drawString(label, offsetX + padding - labelWidth - 10, y + 5);
        }

        // Line graph
        g2.setColor(new Color(33, 150, 243));
        g2.setStroke(new BasicStroke(2.5f));
        for (int i = 0; i < years; i++) {
            int x1 = offsetX + padding + graphWidth * i / years;
            int y1 = offsetY + h - padding - (int) ((values.get(i) - yMin) / (yMax - yMin) * graphHeight);
            int x2 = offsetX + padding + graphWidth * (i + 1) / years;
            int y2 = offsetY + h - padding - (int) ((values.get(i + 1) - yMin) / (yMax - yMin) * graphHeight);
            g2.drawLine(x1, y1, x2, y2);
        }

        // Highlight year
        if (highlightYear >= 0 && highlightYear < values.size()) {
            int x = offsetX + padding + graphWidth * highlightYear / years;
            int y = offsetY + h - padding - (int) ((values.get(highlightYear) - yMin) / (yMax - yMin) * graphHeight);

            g2.setColor(new Color(244, 67, 54));
            g2.fillOval(x - 5, y - 5, 10, 10);

            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.setColor(Color.BLACK);

            String labelText = String.format("Year %d: %s", highlightYear, formatCurrencyCompact(values.get(highlightYear)));
            FontMetrics labelFm = g2.getFontMetrics();
            int labelWidth = labelFm.stringWidth(labelText);

            int labelX = x - labelWidth/2;
            int labelY = y - 10;
            
            if (labelX < offsetX + padding) labelX = offsetX + padding;
            if (labelX + labelWidth > offsetX + w - padding) labelX = offsetX + w - padding - labelWidth;
            if (labelY < offsetY + padding + 15) labelY = y + 20;

            g2.drawString(labelText, labelX, labelY);
        }
    }
    
    private void showOverflowMessage(Graphics2D g2, int offsetX, int offsetY, int w, int h) {
        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2.setColor(new Color(231, 76, 60)); // Red color
        
        String message = OVERFLOW_MESSAGE;
        FontMetrics fm = g2.getFontMetrics();
        int messageWidth = fm.stringWidth(message);
        int messageHeight = fm.getHeight();
        
        // Center the message
        int x = offsetX + (w - messageWidth) / 2;
        int y = offsetY + (h + messageHeight) / 2;
        
        g2.drawString(message, x, y);
        
        // Add helpful text below
        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2.setColor(Color.DARK_GRAY);
        String helpText = "Please use smaller input values";
        fm = g2.getFontMetrics();
        int helpWidth = fm.stringWidth(helpText);
        x = offsetX + (w - helpWidth) / 2;
        y = y + 40;
        
        g2.drawString(helpText, x, y);
    }

    private static class YAxisInfo {
        double min, max;
        double[] ticks;
        
        YAxisInfo(double min, double max, double[] ticks) {
            this.min = min;
            this.max = max;
            this.ticks = ticks;
        }
    }

    private YAxisInfo calculateSmartYAxis(double dataMin, double dataMax) {
        double yMin = 0;
        double yMax = dataMax * 1.1;
        
        double rawStep = yMax / 5; 
        double step = getNiceNumber(rawStep);
        
        yMax = Math.ceil(yMax / step) * step;
        
        java.util.List<Double> tickList = new java.util.ArrayList<>();
        for (double tick = 0; tick <= yMax; tick += step) {
            tickList.add(tick);
        }
        
        java.util.Set<Double> uniqueTicks = new java.util.LinkedHashSet<>(tickList);
        tickList = new java.util.ArrayList<>(uniqueTicks);
        
        while (tickList.size() > 6) {
            java.util.List<Double> newTickList = new java.util.ArrayList<>();
            for (int i = 0; i < tickList.size(); i += 2) {
                newTickList.add(tickList.get(i));
            }
            if (!newTickList.contains(tickList.get(tickList.size() - 1))) {
                newTickList.add(tickList.get(tickList.size() - 1));
            }
            tickList = newTickList;
        }
        
        double[] ticks = tickList.stream().mapToDouble(Double::doubleValue).toArray();
        
        return new YAxisInfo(yMin, yMax, ticks);
    }

    private double getNiceNumber(double value) {
        double exponent = Math.floor(Math.log10(value));
        double fraction = value / Math.pow(10, exponent);
        
        double niceFraction;
        if (fraction <= 1.5) {
            niceFraction = 1;
        } else if (fraction <= 3) {
            niceFraction = 2;
        } else if (fraction <= 7) {
            niceFraction = 5;
        } else {
            niceFraction = 10;
        }
        
        return niceFraction * Math.pow(10, exponent);
    }

    // Compact currency formatting for large numbers
    private String formatCurrencyCompact(double value) {
        if (value == 0) {
            return "$0";
        }
        
        if (Math.abs(value) >= 1e12) {
            return String.format("$%.1fT", value / 1e12);
        } else if (Math.abs(value) >= 1e9) {
            return String.format("$%.1fB", value / 1e9);
        } else {
            // Display thousands and millions with full formatting (commas)
            java.text.NumberFormat currencyFormat = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
            currencyFormat.setMaximumFractionDigits(0);
            return currencyFormat.format(value);
        }
    }
}