package src;
import java.util.Random;

public class InvestmentLogic {

    // 1. Compound Interest
    public static double calculateCompoundInterest(double principal, double rate, int years) {
        return principal * Math.pow(1 + rate, years);
    }

    // 2. Simple Interest
    public static double calculateSimpleInterest(double principal, double rate, int years) {
        return principal * (1 + rate * years);
    }

    // 3. Compound Interest with Annual Contributions
    public static double calculateWithContributions(double initial, double annualContribution, double rate, int years) {
        double lump = initial * Math.pow(1 + rate, years);
        double contrib = annualContribution * ((Math.pow(1 + rate, years) - 1) / rate);
        return lump + contrib;
    }

    // 4. Appreciating Asset (Linear growth)
    public static double calculateAppreciation(double purchasePrice, double annualGrowth, int years) {
        return purchasePrice * (1 + annualGrowth * years);
    }

    // 5. Depreciating Asset
    public static double calculateDepreciation(double originalValue, double depreciationRate, int years) {
        return originalValue * Math.pow(1 - depreciationRate, years);
    }

    // 6. Simulated Crypto (random walk, returns final year)
    public static double simulateCryptoValue(double initialValue, double volatility, int years) {
        double value = initialValue;
        Random rand = new Random();

        for (int i = 0; i < years; i++) {
            double change = (rand.nextDouble() * 2 * volatility) - volatility;
            value *= (1 + change);
        }

        return value;
    }

    // 7. Inflation-adjusted Value
    public static double adjustForInflation(double nominalValue, double inflationRate, int years) {
        return nominalValue / Math.pow(1 + inflationRate, years);
    }

    //8. Utility Per Dollar (Ms. Fernandez's formula)
    public static int utilityPerDollar(int happiness, int frequency, double convenience, int lifestyleConvenience, int timeYears, int lifespan, int price){
        int adjustedCost = (int)(Math.ceil(timeYears/lifespan) * price);
        return (int)((happiness * frequency * 12) + (convenience * lifestyleConvenience))/adjustedCost;
    }
}
