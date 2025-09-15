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
        int padding = 60;
        int years = values.size() - 1;

        double min = values.stream().min(Double::compareTo).orElse(0.0);
        double max = values.stream().max(Double::compareTo).orElse(1.0);
        if (max == min) max = min + 1;

        // Background grid lines
        g2.setColor(new Color(230, 230, 230));
        int numYTicks = 8;
        for (int i = 0; i <= numYTicks; i++) {
            int y = h - padding - (int) ((i / (double) numYTicks) * (h - 2 * padding));
            g2.drawLine(padding, y, w - padding, y);
        }

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
        g2.drawString("Value", 10, padding - 10);

        // X-axis ticks and labels
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        for (int i = 0; i <= numXTicks; i++) {
            int year = i * years / numXTicks;
            int x = padding + (w - 2 * padding) * year / years;
            g2.drawLine(x, h - padding, x, h - padding + 6);
            g2.drawString(String.valueOf(year), x - 10, h - padding + 20);
        }

        // Y-axis ticks and labels
        for (int i = 0; i <= numYTicks; i++) {
            double value = min + (max - min) * i / numYTicks;
            int y = h - padding - (int) ((value - min) / (max - min) * (h - 2 * padding));
            g2.drawLine(padding - 6, y, padding, y);
            g2.drawString(String.format("%.0f", value), padding - 45, y + 5);
        }

        // Line graph
        g2.setColor(new Color(33, 150, 243)); // Nice blue
        g2.setStroke(new BasicStroke(2.5f));
        for (int i = 0; i < years; i++) {
            int x1 = padding + (w - 2 * padding) * i / years;
            int y1 = h - padding - (int) ((values.get(i) - min) / (max - min) * (h - 2 * padding));
            int x2 = padding + (w - 2 * padding) * (i + 1) / years;
            int y2 = h - padding - (int) ((values.get(i + 1) - min) / (max - min) * (h - 2 * padding));
            g2.drawLine(x1, y1, x2, y2);
        }

        // Highlight year
        if (highlightYear >= 0 && highlightYear < values.size()) {
            int x = padding + (w - 2 * padding) * highlightYear / years;
            int y = h - padding - (int) ((values.get(highlightYear) - min) / (max - min) * (h - 2 * padding));

            g2.setColor(new Color(244, 67, 54)); // Fancy red
            g2.fillOval(x - 6, y - 6, 12, 12);

            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.setColor(new Color(0, 0, 0, 180)); // Shadow

            String labelText = String.format("Year %d: $%.2f", highlightYear, values.get(highlightYear));
            FontMetrics fm = g2.getFontMetrics();
            int labelWidth = fm.stringWidth(labelText);

            g2.drawString(String.format("Year %d: $%.2f", highlightYear, values.get(highlightYear)), x - labelWidth/2, y - 9);
            
                
            // Draw border around the entire panel (the full image)
            g2.setColor(Color.BLACK); // or any color you want
            g2.setStroke(new BasicStroke(5f)); // thickness of border
            g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

}