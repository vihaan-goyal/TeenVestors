//import java.io.FileWriter;
//import java.io.IOException;
import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    public static String[] removeDupes(String[] array) {
            return new HashSet<String>(Arrays.asList(array)).toArray(new String[0]);
}
    
    public static String findName(String name) {
        try {
            int late = 0;
        String[] names = getNames();
        for (int ii = names.length-1; ii >=0; ii--) {
            if (names[ii].equals(name)) {
                late = ii;
                break;
            }
        }
         
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
            String[] lines = sb.toString().split("\\n");
            System.out.println(lines[late+1]);
            return lines[late+1];

            } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return new String();
            }
    }

    public static String[] getNames() {
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
            String[] lines = sb.toString().split("\\n");
            String[] output = new String[lines.length - 1];
            for (int ii=1; ii < lines.length; ii++) {
                if (lines[ii].indexOf(" ") >= 0) {
                output[ii-1] = lines[ii].substring(0, lines[ii].indexOf(" "));
            }
            }
            System.out.println("success");
            return removeDupes(output);
        } catch (IOException e) {
            System.out.println("An error occurred.");
        e.printStackTrace();
        return new String[1];
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
            store.write(sb +  "\n" + name + " " + "CompoundInterest " + principal + " " + rate + " " + years + " " + InvestmentLogic.calculateCompoundInterest(principal, rate, years));
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
            store.write(sb + "\n"  + name + " " + "SimpleInterest " + principal + " " + rate + " " + years + " " + InvestmentLogic.calculateSimpleInterest(principal, rate, years));
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
            store.write(sb + "\n" + name + "Appreciation " + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.calculateAppreciation(principal, rate, years));
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
            store.write(sb + "\n" + name + "Appreciation " + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.calculateDepreciation(principal, rate, years));
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
            store.write(sb + "\n" + name + "Appreciation " + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.simulateCryptoValue(principal, rate, years));
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
            store.write(sb + "\n" + name + "Appreciation " + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.adjustForInflation(principal, rate, years));
            store.close();
            System.out.println("success");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}



 