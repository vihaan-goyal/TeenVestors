package src;
//import java.io.FileWriter;
//import java.io.IOException;
import java.io.*;

public class Write {
    public static String filePath = "Store.txt";
    //clears the storage file
    public static void clear() {
        try {
            FileWriter store = new FileWriter(filePath);
            store.write("");
            store.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
      e.printStackTrace();
        }
    }

    public static void storeCompoundInterest(String name, double principal, double rate, int years) {
        try {
            //creates reader to read the current file
            FileReader fr = new FileReader(filePath);
            int i;
            //creates string to store the current file in
            StringBuilder sb = new StringBuilder();
            //stores the current file in the string
            while ((i = fr.read()) != -1) {
                sb.append((char)i);
            }
            fr.close();
            //creates the filewriter to write the file
            FileWriter store = new FileWriter(filePath);
            store.write(sb + "\n" + "CompoundInterest " + name + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.calculateCompoundInterest(principal, rate, years));
            store.close();
            System.out.println("success");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        e.printStackTrace();
        }

    }

    public static void storeSimpleInterest(String name, double principal, double rate, int years) {
        try {
            //creates reader to read the current file
            FileReader fr = new FileReader(filePath);
            int i;
            //creates string to store the current file in
            StringBuilder sb = new StringBuilder();
            //stores the current file in the string
            while ((i = fr.read()) != -1) {
                sb.append((char)i);
            }
            fr.close();
            //creates the filewriter to write the file
            FileWriter store = new FileWriter(filePath);
            store.write(sb + "\n" + "SimpleInterest " + name + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.calculateSimpleInterest(principal, rate, years));
            store.close();
            System.out.println("success");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public static void storeAppreciation(String name, double principal, double rate, int years) {
        try {
            //creates reader to read the current file
            FileReader fr = new FileReader(filePath);
            int i;
            //creates string to store the current file in
            StringBuilder sb = new StringBuilder();
            //stores the current file in the string
            while ((i = fr.read()) != -1) {
                sb.append((char)i);
            }
            fr.close();
            //creates the filewriter to write the file
            FileWriter store = new FileWriter(filePath);
            store.write(sb + "\n" + "Appreciation " + name + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.calculateAppreciation(principal, rate, years));
            store.close();
            System.out.println("success");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

        public static void storeDepreciation(String name, double principal, double rate, int years) {
        try {
            //creates reader to read the current file
            FileReader fr = new FileReader(filePath);
            int i;
            //creates string to store the current file in
            StringBuilder sb = new StringBuilder();
            //stores the current file in the string
            while ((i = fr.read()) != -1) {
                sb.append((char)i);
            }
            fr.close();
            //creates the filewriter to write the file
            FileWriter store = new FileWriter(filePath);
            store.write(sb + "\n" + "Appreciation " + name + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.calculateDepreciation(principal, rate, years));
            store.close();
            System.out.println("success");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public static void storeCryptoValue(String name, double principal, double rate, int years) {
        try {
            //creates reader to read the current file
            FileReader fr = new FileReader(filePath);
            int i;
            //creates string to store the current file in
            StringBuilder sb = new StringBuilder();
            //stores the current file in the string
            while ((i = fr.read()) != -1) {
                sb.append((char)i);
            }
            fr.close();
            //creates the filewriter to write the file
            FileWriter store = new FileWriter(filePath);
            store.write(sb + "\n" + "Appreciation " + name + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.simulateCryptoValue(principal, rate, years));
            store.close();
            System.out.println("success");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public static void storeInflation(String name, double principal, double rate, int years) {
        try {
            //creates reader to read the current file
            FileReader fr = new FileReader(filePath);
            int i;
            //creates string to store the current file in
            StringBuilder sb = new StringBuilder();
            //stores the current file in the string
            while ((i = fr.read()) != -1) {
                sb.append((char)i);
            }
            fr.close();
            //creates the filewriter to write the file
            FileWriter store = new FileWriter(filePath);
            store.write(sb + "\n" + "Appreciation " + name + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.adjustForInflation(principal, rate, years));
            store.close();
            System.out.println("success");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}



 