import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ClaudeEducationalChatbot extends JDialog {
    private JTextArea chatArea;
    private JTextField inputField;
    private JScrollPane scrollPane;
    private List<String> conversationHistory;
    private boolean useAI = false;
    
    
    // This will read your API key from the file
    private static final String CLAUDE_API_KEY = "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";
    private static final String CLAUDE_URL = "https://api.anthropic.com/v1/messages";
    private static final String CLAUDE_VERSION = "2023-06-01";

    // System prompt to make Claude focus on teen investment education
    private static final String SYSTEM_PROMPT = 
        "You are an investment education assistant specifically for teenagers. " +
        "Your goal is to teach financial literacy in a fun, engaging way that teens can understand. " +
        "Always:\n" +
        "- Use simple, clear language\n" +
        "- Give real-world examples teens can relate to\n" +
        "- Focus on long-term thinking and compound interest\n" +
        "- Emphasize the importance of starting early\n" +
        "- Use analogies and stories when possible\n" +
        "- Keep responses under 200 words\n" +
        "- Be encouraging and positive about their financial future\n" +
        "- Avoid complex jargon without explanation\n" +
        "- Include practical tips they can act on\n\n" +
        "Topics to focus on: compound interest, index funds, emergency funds, " +
        "budgeting, risk vs return, inflation, and building good money habits.";

    public ClaudeEducationalChatbot(JFrame parent) {
        super(parent, "Investment Learning Assistant (AI)", true);
        this.conversationHistory = new ArrayList<>();
        setupUI();
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        setSize(750, 650);
        setMinimumSize(new Dimension(700, 550));

        // Title panel with AI status
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(138, 43, 226)); // Purple for Claude
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Investment Learning Assistant (Powered by AI)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        // AI toggle button
        JButton aiToggle = new JButton(useAI ? "AI: ON" : "Basic Mode: ON");
        aiToggle.setBackground(useAI ? new Color(75, 0, 130) : new Color(158, 158, 158));
        aiToggle.setForeground(Color.WHITE);
        aiToggle.setFocusPainted(false);
        aiToggle.addActionListener(e -> toggleAI(aiToggle));
        
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(aiToggle, BorderLayout.EAST);

        // Chat area with proper text wrapping
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        chatArea.setBackground(new Color(248, 249, 250));
        chatArea.setForeground(new Color(33, 33, 33));
        chatArea.setMargin(new Insets(20, 20, 20, 20));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        
        scrollPane = new JScrollPane(chatArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Quick question buttons
        JPanel quickPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
        quickPanel.setBackground(new Color(245, 245, 245));
        quickPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        String[] quickQuestions = {
            "Explain compound interest",
            "Best investments for teens",
            "How do I start investing?",
            "What's an emergency fund?"
        };

        for (String question : quickQuestions) {
            JButton quickBtn = createQuickButton(question);
            quickPanel.add(quickBtn);
        }

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        inputPanel.setBackground(Color.WHITE);
        
        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        inputField.addActionListener(e -> handleUserInput());
        
        JButton sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(new Color(138, 43, 226));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> handleUserInput());

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Status label for AI responses
        JLabel statusLabel = new JLabel("Ready to help you learn about investing!");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(quickPanel, BorderLayout.NORTH);
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Welcome message
        appendToBotChat("Hi! I'm your investment learning assistant!\n\n" +
                       "I can help you understand compound interest, stocks, budgeting, and building wealth as a teenager.\n\n" +
                       (useAI ? "I'm powered by AI for personalized responses!" : "Click 'Basic Mode: ON' to enable AI for smarter answers!") +
                       "\n\nWhat would you like to learn about?");
    }

    private JButton createQuickButton(String question) {
        JButton quickBtn = new JButton(question);
        quickBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        quickBtn.setBackground(new Color(255, 255, 255));
        quickBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        quickBtn.setFocusPainted(false);
        quickBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        quickBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                quickBtn.setBackground(new Color(230, 230, 230));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                quickBtn.setBackground(Color.WHITE);
            }
        });
        
        quickBtn.addActionListener(e -> {
            inputField.setText(question);
            handleUserInput();
        });
        
        return quickBtn;
    }

    private void toggleAI(JButton button) {
        useAI = !useAI;
        button.setText(useAI ? "AI: ON" : "Basic Mode: ON");
        button.setBackground(useAI ? new Color(75, 0, 130) : new Color(158, 158, 158));
        
        if (useAI && CLAUDE_API_KEY.equals("your-claude-api-key-here")) {
            JOptionPane.showMessageDialog(this, 
                "To use Claude AI, you need to:\n\n" +
                "1. Get an API key from https://console.anthropic.com/\n" +
                "2. In the ClaudeEducationalChatbot.java file, replace:\n" +
                "   'your-claude-api-key-here'\n" +
                "   with your actual API key\n" +
                "3. Recompile and restart the application\n\n" +
                "For now, I'll use basic responses which work great too!", 
                "API Key Setup Required", 
                JOptionPane.INFORMATION_MESSAGE);
            useAI = false;
            button.setText("Basic Mode: ON");
            button.setBackground(new Color(158, 158, 158));
        }
        
        appendToBotChat(useAI ? 
        "AI is now active! I can give you personalized investment advice tailored just for you." :
        "Using basic responses. Enable AI for smarter, personalized answers!");
    }
        

    private void handleUserInput() {
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) return;

        appendToUserChat(userInput);
        conversationHistory.add("User: " + userInput);
        inputField.setText("");

        if (useAI && !CLAUDE_API_KEY.equals("your-claude-api-key-here")) {
            // Show loading message
            appendToBotChat("Thinking...");
            
            // Get AI response in background thread
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return getClaudeResponse(userInput);
                }
                
                @Override
                protected void done() {
                    try {
                        String response = get();
                        // Remove the "Thinking..." message
                        String currentText = chatArea.getText();
                        int lastThinking = currentText.lastIndexOf("ASSISTANT:\nThinking...");
                        if (lastThinking != -1) {
                            chatArea.setText(currentText.substring(0, lastThinking));
                        }
                        appendToBotChat(response);
                        conversationHistory.add("Assistant: " + response);
                    } catch (Exception e) {
                        appendToBotChat("Sorry, I'm having trouble connecting to the AI right now. " +
                                      "Please check your internet connection or try again later.\n\n" +
                                      "Error: " + e.getMessage());
                    }
                }
            };
            worker.execute();
        } else {
            // Use basic responses
            String response = getBasicResponse(userInput.toLowerCase());
            appendToBotChat(response);
            conversationHistory.add("Assistant: " + response);
        }

        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private String getClaudeResponse(String userMessage) throws Exception {
        URL url = new URL(CLAUDE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("x-api-key", CLAUDE_API_KEY);
        conn.setRequestProperty("anthropic-version", CLAUDE_VERSION);
        conn.setDoOutput(true);

        // Create the request body with proper JSON structure
        StringBuilder jsonBody = new StringBuilder();
        jsonBody.append("{");
        jsonBody.append("\"model\": \"claude-3-haiku-20240307\",");
        jsonBody.append("\"max_tokens\": 300,");
        jsonBody.append("\"system\": \"").append(escapeJson(SYSTEM_PROMPT)).append("\",");
        jsonBody.append("\"messages\": [");
        
        // Add conversation history (last few messages for context)
        int historyStart = Math.max(0, conversationHistory.size() - 6);
        boolean hasHistory = false;
        
        for (int i = historyStart; i < conversationHistory.size(); i++) {
            String msg = conversationHistory.get(i);
            if (msg.startsWith("User: ")) {
                if (hasHistory) jsonBody.append(",");
                jsonBody.append("{\"role\": \"user\", \"content\": \"")
                        .append(escapeJson(msg.substring(6))).append("\"}");
                hasHistory = true;
            } else if (msg.startsWith("Assistant: ")) {
                if (hasHistory) jsonBody.append(",");
                jsonBody.append("{\"role\": \"assistant\", \"content\": \"")
                        .append(escapeJson(msg.substring(11))).append("\"}");
                hasHistory = true;
            }
        }
        
        // Add current message
        if (hasHistory) jsonBody.append(",");
        jsonBody.append("{\"role\": \"user\", \"content\": \"").append(escapeJson(userMessage)).append("\"}");
        jsonBody.append("]}");

        // Debug: Print the request (you can remove this later)
        System.out.println("Request: " + jsonBody.toString());

        // Send request
        try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)) {
            writer.write(jsonBody.toString());
            writer.flush();
        }

        // Read response
        int responseCode = conn.getResponseCode();
        
        if (responseCode != 200) {
            // Read error response
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorResponse.append(line);
            }
            errorReader.close();
            
            System.out.println("Error Response: " + errorResponse.toString());
            throw new Exception("HTTP error code: " + responseCode + " - " + errorResponse.toString());
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            
            // Debug: Print the response (you can remove this later)
            String jsonResponse = response.toString();
            System.out.println("Response: " + jsonResponse);
            
            // Parse JSON response - Claude's API returns content in this structure:
            // {"content":[{"text":"response text","type":"text"}], ...}
            try {
                int contentArrayStart = jsonResponse.indexOf("\"content\":[");
                if (contentArrayStart == -1) {
                    throw new Exception("No content array found in response");
                }
                
                int textStart = jsonResponse.indexOf("\"text\":\"", contentArrayStart);
                if (textStart == -1) {
                    throw new Exception("No text field found in response");
                }
                textStart += 8; // Move past "text":"
                
                int textEnd = jsonResponse.indexOf("\"", textStart);
                while (textEnd != -1 && jsonResponse.charAt(textEnd - 1) == '\\') {
                    // Handle escaped quotes
                    textEnd = jsonResponse.indexOf("\"", textEnd + 1);
                }
                
                if (textEnd == -1) {
                    throw new Exception("Malformed text field in response");
                }
                
                String responseText = jsonResponse.substring(textStart, textEnd);
                return unescapeJson(responseText);
                
            } catch (Exception parseException) {
                System.out.println("JSON parsing error: " + parseException.getMessage());
                return "I received a response but couldn't parse it properly. Please try again!";
            }
        }
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    private String unescapeJson(String text) {
        return text.replace("\\\"", "\"")
                  .replace("\\n", "\n")
                  .replace("\\r", "\r")
                  .replace("\\t", "\t")
                  .replace("\\\\", "\\");
    }

    private String getBasicResponse(String input) {
        // Fallback responses when AI is disabled
        if (input.contains("compound interest")) {
            return "Compound interest is when you earn interest on your interest! It's like a snowball effect - the longer you wait, the bigger it gets. Starting with $1000 at 8% annually, you'd have about $2,159 after 10 years!";
        } else if (input.contains("stock") || input.contains("invest")) {
            return "For teens, I recommend starting with index funds like the S&P 500. They're like buying a tiny piece of 500 different companies at once - much safer than picking individual stocks!";
        } else if (input.contains("emergency fund")) {
            return "An emergency fund is money set aside for unexpected expenses. Aim for 3-6 months of expenses in a high-yield savings account. It's your financial safety net!";
        } else if (input.contains("budget")) {
            return "Try the 50/30/20 rule: 50% for needs, 30% for wants, and 20% for savings and investing. The key is to pay your future self first!";
        } else {
            return "That's a great question! I can help you learn about compound interest, investing basics, emergency funds, budgeting, and building wealth as a teenager. What specific topic interests you most?";
        }
    }

    private void appendToUserChat(String message) {
        chatArea.append("YOU:\n");
        chatArea.append(message + "\n\n");
    }

    private void appendToBotChat(String message) {
        chatArea.append("ASSISTANT:\n");
        String formattedMessage = message.replace("• ", "  • ");
        chatArea.append(formattedMessage + "\n\n");
        
        if (message.length() > 200) {
            chatArea.append("----------------------------------------\n\n");
        }
    }
}
