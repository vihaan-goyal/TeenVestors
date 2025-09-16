import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ValueProjectionGraphPanel extends JPanel {
    private final List<Double> values;
    private final int highlightYear;

    public ValueProjectionGraphPanel(List<Double> values, int highlightYear) {
        this.values = values;
        this.highlightYear = highlightYear;
        setPreferredSize(new Dimension(600, 400));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (values == null || values.size() < 2) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int padding = 80; // Increased padding to fit better labels
        int years = values.size() - 1;

        double min = values.stream().min(Double::compareTo).orElse(0.0);
        double max = values.stream().max(Double::compareTo).orElse(1.0);
        if (max == min) max = min + 1;

        // Calculate smart Y-axis range and ticks
        YAxisInfo yAxisInfo = calculateSmartYAxis(min, max);
        double yMin = yAxisInfo.min;
        double yMax = yAxisInfo.max;
        double[] yTicks = yAxisInfo.ticks;

        // Background grid lines
        g2.setColor(new Color(230, 230, 230));
        
        // Y-axis grid lines (based on smart ticks)
        for (double tickValue : yTicks) {
            int y = h - padding - (int) ((tickValue - yMin) / (yMax - yMin) * (h - 2 * padding));
            g2.drawLine(padding, y, w - padding, y);
        }

        // X-axis grid lines
        int numXTicks = Math.min(years, 10);
        for (int i = 0; i <= numXTicks; i++) {
            int x = padding + (int)((i / (double) numXTicks) * (w - 2 * padding));
            g2.drawLine(x, padding, x, h - padding);
        }

        // Axes
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(padding, h - padding, w - padding, h - padding);
        g2.drawLine(padding, padding, padding, h - padding);

        // Labels
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("Year", w / 2 - 20, h - 15);
        g2.drawString("Value ($)", 10, padding - 10);

        // X-axis ticks and labels
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        for (int i = 0; i <= numXTicks; i++) {
            int year = i * years / numXTicks;
            int x = padding + (w - 2 * padding) * year / years;
            g2.drawLine(x, h - padding, x, h - padding + 6);
            g2.drawString(String.valueOf(year), x - 10, h - padding + 20);
        }

        // Y-axis ticks and labels (using smart ticks)
        for (double tickValue : yTicks) {
            int y = h - padding - (int) ((tickValue - yMin) / (yMax - yMin) * (h - 2 * padding));
            g2.drawLine(padding - 6, y, padding, y);
            
            // Format the label based on the value size
            String label = formatCurrency(tickValue);
            FontMetrics fm = g2.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            g2.drawString(label, padding - labelWidth - 10, y + 5);
        }

        // Line graph (adjusted for new Y range)
        g2.setColor(new Color(33, 150, 243)); // Nice blue
        g2.setStroke(new BasicStroke(2.5f));
        for (int i = 0; i < years; i++) {
            int x1 = padding + (w - 2 * padding) * i / years;
            int y1 = h - padding - (int) ((values.get(i) - yMin) / (yMax - yMin) * (h - 2 * padding));
            int x2 = padding + (w - 2 * padding) * (i + 1) / years;
            int y2 = h - padding - (int) ((values.get(i + 1) - yMin) / (yMax - yMin) * (h - 2 * padding));
            g2.drawLine(x1, y1, x2, y2);
        }

        // Highlight year (adjusted for new Y range)
        if (highlightYear >= 0 && highlightYear < values.size()) {
            int x = padding + (w - 2 * padding) * highlightYear / years;
            int y = h - padding - (int) ((values.get(highlightYear) - yMin) / (yMax - yMin) * (h - 2 * padding));

            g2.setColor(new Color(244, 67, 54)); // Fancy red
            g2.fillOval(x - 6, y - 6, 12, 12);

            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.setColor(new Color(0, 0, 0, 180)); // Shadow

            String labelText = String.format("Year %d: %s", highlightYear, formatCurrency(values.get(highlightYear)));
            FontMetrics fm = g2.getFontMetrics();
            int labelWidth = fm.stringWidth(labelText);

            g2.drawString(labelText, x - labelWidth/2, y - 9);
        }
                
        // Draw border around the entire panel
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(5f));
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
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
        // Aim for 5-7 ticks (not too many to avoid crowding)
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
        while (tickList.size() > 8) {
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