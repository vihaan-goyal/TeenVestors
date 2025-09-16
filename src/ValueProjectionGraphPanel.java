import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ValueProjectionGraphPanel extends JPanel {
    private final List<Double> values;
    private final int highlightYear;

    public ValueProjectionGraphPanel(List<Double> values, int highlightYear) {
        this.values = values;
        this.highlightYear = highlightYear;
        setPreferredSize(new Dimension(700, 400)); // Made larger
        setBackground(Color.WHITE);
        // Add some margin to prevent clipping
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Reduced border
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (values == null || values.size() < 2) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Get the actual drawing area (excluding border insets)
        Insets insets = getInsets();
        int w = getWidth() - insets.left - insets.right;
        int h = getHeight() - insets.top - insets.bottom;
        int offsetX = insets.left;
        int offsetY = insets.top;
        
        // Ensure minimum size
        if (w < 100 || h < 100) return;
        
        int padding = 80; // Increased padding for better label space
        int years = values.size() - 1;

        // Ensure we have enough space for the graph
        int graphWidth = w - 2 * padding;
        int graphHeight = h - 2 * padding;
        
        if (graphWidth < 50 || graphHeight < 50) return;

        double min = values.stream().min(Double::compareTo).orElse(0.0);
        double max = values.stream().max(Double::compareTo).orElse(1.0);
        if (max == min) max = min + 1;

        // Calculate smart Y-axis range and ticks
        YAxisInfo yAxisInfo = calculateSmartYAxis(min, max);
        double yMin = yAxisInfo.min;
        double yMax = yAxisInfo.max;
        double[] yTicks = yAxisInfo.ticks;

        // Background grid lines
        g2.setColor(new Color(240, 240, 240));
        g2.setStroke(new BasicStroke(0.5f));
        
        // Y-axis grid lines (based on smart ticks)
        for (double tickValue : yTicks) {
            int y = offsetY + h - padding - (int) ((tickValue - yMin) / (yMax - yMin) * graphHeight);
            g2.drawLine(offsetX + padding, y, offsetX + w - padding, y);
        }

        // X-axis grid lines
        int numXTicks = Math.min(years, 10); // Back to more tick marks for better detail
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
        g2.setFont(new Font("SansSerif", Font.BOLD, 14)); // Larger font
        FontMetrics fm = g2.getFontMetrics();
        
        // X-axis label
        String xLabel = "Year";
        int xLabelWidth = fm.stringWidth(xLabel);
        g2.drawString(xLabel, offsetX + (w - xLabelWidth) / 2, offsetY + h - 10);
        
        // Y-axis label
        String yLabel = "Value ($)";
        g2.drawString(yLabel, offsetX + 10, offsetY + 20);

        // X-axis ticks and labels
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Larger tick font
        fm = g2.getFontMetrics();
        
        for (int i = 0; i <= numXTicks; i++) {
            int year = i * years / numXTicks;
            int x = offsetX + padding + graphWidth * year / years;
            g2.drawLine(x, offsetY + h - padding, x, offsetY + h - padding + 5);
            
            String yearLabel = String.valueOf(year);
            int labelWidth = fm.stringWidth(yearLabel);
            g2.drawString(yearLabel, x - labelWidth/2, offsetY + h - padding + 20);
        }

        // Y-axis ticks and labels (using smart ticks)
        for (double tickValue : yTicks) {
            int y = offsetY + h - padding - (int) ((tickValue - yMin) / (yMax - yMin) * graphHeight);
            g2.drawLine(offsetX + padding - 5, y, offsetX + padding, y);
            
            // Format the label based on the value size
            String label = formatCurrency(tickValue);
            int labelWidth = fm.stringWidth(label);
            g2.drawString(label, offsetX + padding - labelWidth - 10, y + 5);
        }

        // Line graph (adjusted for new Y range)
        g2.setColor(new Color(33, 150, 243)); // Nice blue
        g2.setStroke(new BasicStroke(2.5f));
        for (int i = 0; i < years; i++) {
            int x1 = offsetX + padding + graphWidth * i / years;
            int y1 = offsetY + h - padding - (int) ((values.get(i) - yMin) / (yMax - yMin) * graphHeight);
            int x2 = offsetX + padding + graphWidth * (i + 1) / years;
            int y2 = offsetY + h - padding - (int) ((values.get(i + 1) - yMin) / (yMax - yMin) * graphHeight);
            g2.drawLine(x1, y1, x2, y2);
        }

        // Highlight year (adjusted for new Y range)
        if (highlightYear >= 0 && highlightYear < values.size()) {
            int x = offsetX + padding + graphWidth * highlightYear / years;
            int y = offsetY + h - padding - (int) ((values.get(highlightYear) - yMin) / (yMax - yMin) * graphHeight);

            g2.setColor(new Color(244, 67, 54)); // Red
            g2.fillOval(x - 5, y - 5, 10, 10);

            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.setColor(Color.BLACK);

            String labelText = String.format("Year %d: %s", highlightYear, formatCurrency(values.get(highlightYear)));
            FontMetrics labelFm = g2.getFontMetrics();
            int labelWidth = labelFm.stringWidth(labelText);

            // Position label to avoid clipping
            int labelX = x - labelWidth/2;
            int labelY = y - 10;
            
            // Adjust if label would go off screen
            if (labelX < offsetX + padding) labelX = offsetX + padding;
            if (labelX + labelWidth > offsetX + w - padding) labelX = offsetX + w - padding - labelWidth;
            if (labelY < offsetY + padding + 15) labelY = y + 20;

            g2.drawString(labelText, labelX, labelY);
        }
        
        // Remove the thick border - let the panel handle borders
    }

    // Helper class to store Y-axis information
    private static class YAxisInfo {
        double min, max;
        double[] ticks;
        
        YAxisInfo(double min, double max, double[] ticks) {
            this.min = min;
            this.max = max;
            this.ticks = ticks;
        }
    }

    // Calculate smart Y-axis range and tick marks - ALWAYS starting at (0,0)
    private YAxisInfo calculateSmartYAxis(double dataMin, double dataMax) {
        // Always start at 0 for financial data
        double yMin = 0;
        
        // Add some padding at the top (10% above max value)
        double yMax = dataMax * 1.1;
        
        // Calculate a nice tick interval based on the full range
        // Aim for 5-6 ticks (not too many to avoid crowding)
        double rawStep = yMax / 5; 
        
        // Round to a "nice" number
        double step = getNiceNumber(rawStep);
        
        // Adjust upper bound to align with step (keep yMin at 0)
        yMax = Math.ceil(yMax / step) * step;
        
        // Generate tick values starting from 0 with consistent intervals
        java.util.List<Double> tickList = new java.util.ArrayList<>();
        for (double tick = 0; tick <= yMax; tick += step) {
            tickList.add(tick);
        }
        
        // Remove duplicates and ensure we don't have too many ticks
        java.util.Set<Double> uniqueTicks = new java.util.LinkedHashSet<>(tickList);
        tickList = new java.util.ArrayList<>(uniqueTicks);
        
        // If we have too many ticks, thin them out
        while (tickList.size() > 6) {
            java.util.List<Double> newTickList = new java.util.ArrayList<>();
            for (int i = 0; i < tickList.size(); i += 2) {
                newTickList.add(tickList.get(i));
            }
            // Always include the max value
            if (!newTickList.contains(tickList.get(tickList.size() - 1))) {
                newTickList.add(tickList.get(tickList.size() - 1));
            }
            tickList = newTickList;
        }
        
        double[] ticks = tickList.stream().mapToDouble(Double::doubleValue).toArray();
        
        return new YAxisInfo(yMin, yMax, ticks);
    }

    // Get a "nice" number for step size (1, 2, 5, 10, 20, 50, 100, etc.)
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

    // Format currency values with full numbers and commas (no abbreviations)
    private String formatCurrency(double value) {
        if (value == 0) {
            return "$0";
        } else {
            // Use NumberFormat for proper comma formatting without abbreviations
            java.text.NumberFormat currencyFormat = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
            currencyFormat.setMaximumFractionDigits(0); // No cents for cleaner labels
            return currencyFormat.format(value);
        }
    }
}