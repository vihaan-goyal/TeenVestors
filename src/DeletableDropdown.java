import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DeletableDropdown extends JPanel {
    private JTextField displayField;
    private JButton dropdownButton;
    private JPopupMenu popup;
    private String selectedItem;
    private ActionListener selectionListener;
    private DeleteCallback deleteCallback;
    
    // Modern colors matching the application theme
    private final Color DANGER_RED = new Color(231, 76, 60);
    private final Color DARK_GRAY = new Color(52, 73, 94);
    private final Color LIGHT_GRAY = new Color(189, 195, 199);
    
    public interface DeleteCallback {
        void onDelete(String item);
    }
    
    public DeletableDropdown() {
        initComponents();
        setupLayout();
    }
    
    private void initComponents() {
        displayField = new JTextField();
        displayField.setEditable(false);
        displayField.setFocusable(false);
        displayField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        displayField.setBackground(Color.WHITE);
        displayField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        dropdownButton = new JButton("▼") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = getModel().isPressed() ? LIGHT_GRAY.darker() : 
                               getModel().isRollover() ? LIGHT_GRAY : Color.WHITE;
                g2d.setColor(bgColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(DARK_GRAY);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(text, x, y);
            }
        };
        dropdownButton.setPreferredSize(new Dimension(30, 40));
        dropdownButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dropdownButton.setBorderPainted(false);
        dropdownButton.setContentAreaFilled(false);
        dropdownButton.setFocusPainted(false);
        dropdownButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dropdownButton.addActionListener(e -> showPopup());
        
        popup = new JPopupMenu();
        popup.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(displayField, BorderLayout.CENTER);
        add(dropdownButton, BorderLayout.EAST);
        setPreferredSize(new Dimension(350, 40));
    }
    
    public void setItems(String[] items) {
        popup.removeAll();
        
        if (items == null || items.length == 0) {
            displayField.setText("");
            selectedItem = null;
            return;
        }
        
        for (String item : items) {
            if (item != null && !item.trim().isEmpty()) {
                JPanel itemPanel = createItemPanel(item);
                popup.add(itemPanel);
            }
        }
        
        // Set the first item as selected if nothing is currently selected
        if (selectedItem == null || selectedItem.isEmpty()) {
            setSelectedItem(items[0]);
        }
    }
    
    private JPanel createItemPanel(String item) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMinimumSize(new Dimension(280, 35));
        panel.setPreferredSize(new Dimension(320, 35));
        panel.setMaximumSize(new Dimension(400, 35));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        
        // Item label (clickable)
        JLabel label = new JLabel(item);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(DARK_GRAY);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Delete button (only show for non-default items)
        final JButton deleteBtn; // Make it final and declare outside the if statement
        if (!item.equals("(default)")) {
            deleteBtn = new JButton("×") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    Color bgColor = getModel().isPressed() ? DANGER_RED.darker() : 
                                   getModel().isRollover() ? DANGER_RED : Color.WHITE;
                    g2d.setColor(bgColor);
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    
                    Color textColor = getModel().isRollover() ? Color.WHITE : DANGER_RED;
                    g2d.setColor(textColor);
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = getText();
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = (getHeight() + fm.getAscent()) / 2 - 2;
                    g2d.drawString(text, x, y);
                }
            };
            deleteBtn.setMinimumSize(new Dimension(25, 25));
            deleteBtn.setPreferredSize(new Dimension(25, 25));
            deleteBtn.setMaximumSize(new Dimension(25, 25));
            deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            deleteBtn.setBorderPainted(false);
            deleteBtn.setContentAreaFilled(false);
            deleteBtn.setFocusPainted(false);
            deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            deleteBtn.setToolTipText("Delete this saved calculation");
            
            final String itemToDelete = item;
            deleteBtn.addActionListener(e -> {
                popup.setVisible(false);
                confirmAndDelete(itemToDelete);
            });
        } else {
            deleteBtn = null; // Set to null for default items
        }
        
        // Hover effects for the entire panel
        MouseAdapter hoverHandler = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (deleteBtn == null || e.getSource() != deleteBtn) {
                    panel.setBackground(new Color(240, 240, 240));
                    panel.repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (deleteBtn == null || e.getSource() != deleteBtn) {
                    panel.setBackground(Color.WHITE);
                    panel.repaint();
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == label) {
                    setSelectedItem(item);
                    popup.setVisible(false);
                    if (selectionListener != null) {
                        selectionListener.actionPerformed(new ActionEvent(DeletableDropdown.this, 
                            ActionEvent.ACTION_PERFORMED, "selection"));
                    }
                }
            }
        };
        
        panel.addMouseListener(hoverHandler);
        label.addMouseListener(hoverHandler);
        
        panel.add(label, BorderLayout.CENTER);
        if (deleteBtn != null) {
            JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            deletePanel.setOpaque(false);
            deletePanel.add(deleteBtn);
            panel.add(deletePanel, BorderLayout.EAST);
        }
        
        return panel;
    }
    
    private void confirmAndDelete(String item) {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete the saved calculation:\n\"" + item + "\"?\n\nThis action cannot be undone.",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION && deleteCallback != null) {
            deleteCallback.onDelete(item);
        }
    }
    
    private void showPopup() {
        if (popup.getComponentCount() == 0) {
            return;
        }
        
        // Calculate proper popup size
        int popupWidth = getWidth();
        int popupHeight = popup.getComponentCount() * 35 + 10; // 35px per item + padding
        
        // Ensure minimum width
        if (popupWidth < 300) {
            popupWidth = 300;
        }
        
        // Set size before showing
        popup.setPopupSize(popupWidth, popupHeight);
        
        // Show popup below the dropdown
        popup.show(this, 0, getHeight());
    }
    
    public void setSelectedItem(String item) {
        this.selectedItem = item;
        displayField.setText(item != null ? item : "");
    }
    
    public String getSelectedItem() {
        return selectedItem;
    }
    
    public void addActionListener(ActionListener listener) {
        this.selectionListener = listener;
    }
    
    public void setDeleteCallback(DeleteCallback callback) {
        this.deleteCallback = callback;
    }
    
    public void setToolTipText(String text) {
        displayField.setToolTipText(text);
        super.setToolTipText(text);
    }
    
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        if (displayField != null) {
            displayField.setPreferredSize(new Dimension(
                preferredSize.width - 30, 
                preferredSize.height
            ));
        }
    }
}