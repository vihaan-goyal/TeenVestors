# Teen Investment Calculator 🧮

A comprehensive financial education application designed to teach teenagers about investing, compound interest, and smart financial decision-making through interactive calculators and visualizations.

## Table of Contents 🎯
- Features
- Installation
- Usage
- Calculator Types
- API Configuration
- Project Structure
- Technical Details
- Educational Components
- Contributing
- License

## Features 📜

### Core Calculators
- **Investment Calculator**: Calculate compound interest, simple interest, and growth with annual contributions
- **Stock Market Simulator**: Real-time stock price fetching and growth projections for popular companies
- **Utility Calculator**: Determine the value-per-dollar of purchases to make smarter spending decisions
- **Asset Calculators**: Track appreciating and depreciating assets over time
- **Inflation Adjuster**: See how inflation affects purchasing power
- **Crypto Simulator**: Understand volatile investments through simulations

### Educational Tools
- **AI Learning Assistant**: Claude-powered chatbot for personalized investment education
- **Interactive Growth Charts**: Visual representations of investment growth over time
- **Educational Tooltips**: Context-sensitive help throughout the application
- **Save/Load Functionality**: Store and compare different investment scenarios

### Modern UI Features
- Gradient-styled headers with smooth animations
- Rounded, card-based layout design
- Responsive hover effects and visual feedback
- Color-coded results (green for gains, red for losses)
- Professional typography using Segoe UI font family

## Installation ✅

### Prerequisites 
- Java Development Kit (JDK) 8 or higher (JDK 17 recommended)
- Java Swing libraries (included with JDK)
- Internet connection for stock price fetching

### Running with VS Code (Recommended)

1. **Prerequisites for VS Code**
   - Install VS Code
   - Install the "Extension Pack for Java" from the VS Code marketplace
   - JDK will be automatically installed (Eclipse Adoptium) or you can use your own

2. **Open and Run**
   - Open the project folder in VS Code
   - Navigate to `src/Main.java`
   - Click the "Run" button (▶️) that appears above the main method
   - Or press `F5` to run in debug mode

### Running from Command Line

1. **Navigate to project directory**
   ```bash
   cd bi_cultural_app_challenge_2025
   ```

2. **Compile all Java files**
   ```bash
   javac -d bin src/*.java
   ```

3. **Run the application**
   ```bash
   java -cp bin Main
   ```

### Using Other IDEs
**IntelliJ IDEA:**
1. Open project as Java project
2. Mark `src` as Sources Root
3. Run `Main.java`

**Eclipse:**
1. Import as "Existing Projects into Workspace"
2. Set `src` as source folder
3. Run `Main.java` as Java Application

## Usage 🪴

### Starting the Application
When you first launch the app, you'll be prompted about existing saved calculations:
- **Yes**: Clear all saved data and start fresh
- **No**: Keep your existing saved calculations
- **Cancel**: Exit the application

### Main Investment Calculator
1. Select an investment type from the dropdown menu
2. Enter your investment details:
   - Initial amount ($)
   - Interest rate (% per year)
   - Time period (years)
   - Annual contributions (if applicable)
3. Click "Calculate My Investment" to see results
4. View the interactive growth chart showing year-by-year progression
5. Optionally save your calculation with a custom name

### Stock Calculator
1. Click the "Stock Calculator" button in the main window
2. Select a company from the dropdown (Apple, Google, Tesla, etc.)
3. Enter your investment amount and time horizon
4. View real-time stock prices and risk levels
5. Calculate projected growth based on historical averages

### Utility Calculator
1. Click the "Utility Calculator" button
2. Rate your potential purchase on:
   - Happiness factor (1-10)
   - Usage frequency per month
   - Convenience rating (1-10)
   - Lifestyle impact (1-10)
3. Enter expected usage duration and product lifespan
4. Input the total price
5. Get a utility score (15+ is excellent value)

### AI Learning Assistant
1. Click the "AI Learning Assistant" button
2. Use quick question buttons or type your own questions
3. Toggle between AI mode (requires API key) and basic mode
4. Learn about compound interest, budgeting, and investment strategies

## Calculator Types ➕

### Investment Growth Models

1. **Compound Interest**: $A = P(1 + r)^t$
   - Money grows exponentially as interest earns interest

2. **Simple Interest**: $A = P(1 + rt)$
   - Linear growth, interest only on principal

3. **With Annual Contributions**: $A = P(1 + r)^t + C\frac{(1 + r)^t - 1}{r}$
   - Combines lump sum growth with regular additions

4. **Appreciating Assets**: $A = P(1 + rt)$
   - Linear appreciation model for real estate, collectibles

5. **Depreciating Assets**: $A = P(1 - r)^t$
   - Exponential decay for cars, electronics

6. **Crypto Simulation**: Random walk with volatility parameter
   - Demonstrates high-risk, high-reward investments

7. **Inflation Adjustment**: $A_{real} = \frac{A_{nominal}}{(1 + i)^t}$
   - Shows real purchasing power over time

## API Configuration 🍽️

### Stock Price API (Finnhub)
1. Get a free API key from [Finnhub.io](https://finnhub.io/)
2. Open `src/EnhancedStockRates.java`
3. Replace `"your-api-key-here"` with your actual API key:
   ```java
   private static final String API_KEY = "your-actual-api-key";
   ```

### Claude AI API (Optional)
1. Get an API key from [Anthropic Console](https://console.anthropic.com/)
2. Open `src/ClaudeEducationalChatbot.java`
3. Replace the API key placeholder:
   ```java
   private static final String CLAUDE_API_KEY = "your-claude-api-key";
   ```

## Project Structure 🏛️

```
bi_cultural_app_challenge_2025/
├── src/
│   ├── Main.java                    # Application entry point
│   ├── GUI.java                     # Main user interface
│   ├── InvestmentLogic.java         # Core calculation algorithms
│   ├── Write.java                   # File I/O for saving calculations
│   ├── ValueProjectionGraphPanel.java # Chart rendering component
│   ├── StockCalculatorDialog.java   # Stock investment interface
│   ├── EnhancedStockRates.java     # Stock API integration
│   ├── UtilityCalculatorDialog.java # Utility calculator interface
│   └── ClaudeEducationalChatbot.java # AI chatbot integration
├── Store.txt                         # Saved calculations storage
├── images/                          # UI button images
│   ├── Stock.png
│   ├── Utility.png
│   ├── AI.png
│   └── About.png
└── README.md                        # This file
```

## Technical Details ⚙️

### Dependencies
- Java Swing for GUI components
- Java AWT for graphics rendering
- HttpURLConnection for API calls
- No external JAR dependencies required

### Data Persistence
- Calculations saved to `Store.txt` in plain text format
- Format: `[name] [type] [parameters] [result]`
- Automatic deduplication of saved entries

### Graphics Rendering
- Custom `Graphics2D` for gradient backgrounds
- Anti-aliased chart rendering
- Dynamic Y-axis scaling with smart tick marks
- Responsive layout adjusting to window size

## Educational Components 🏫

### Learning Objectives
- Understand compound interest and its power
- Compare different investment strategies
- Learn about market volatility and risk
- Make informed purchasing decisions
- Start investing in knowledge early

### Built-in Tutorials
- Hover tooltips explain financial concepts
- Color-coded risk indicators for stocks
- Real-world examples in AI assistant responses
- Visual feedback for gains/losses

## License 🪪

This project was created for educational purposes as part of the 2025 Congressional App Challenge.

## Support 💁‍♂️

For issues or questions:
1. Check the tooltips within the application
2. Use the AI Learning Assistant for educational queries
3. Review saved calculations in the Load dropdown
4. Ensure API keys are properly configured for full functionality

---

**Remember**: This is an educational tool. Always consult with financial advisors before making real investment decisions!
