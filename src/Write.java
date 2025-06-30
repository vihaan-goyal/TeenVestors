package src;
import java.io.FileWriter;
import java.io.IOException;

public class Write {
    public static String filePath = "Store.txt";
    public static void storeCompoundInterest(String name, double principal, double rate, int years) {
        try {
        FileWriter store = new FileWriter(filePath);
        store.write(name + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.calculateCompoundInterest(principal, rate, years));
        store.close();
        System.out.println("success");
        } catch (IOException e) {
            System.out.println("An error occurred.");
      e.printStackTrace();
        }

    }
}
