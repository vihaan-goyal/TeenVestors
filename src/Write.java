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
            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
            writer.println("\n");
            writer.close();
            FileWriter store = new FileWriter(filePath);
            store.write("\n" + "(default) CompoundInterest 10.0 0.1 10 25.937424601000025");
            store.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
      e.printStackTrace();
        }
    }

    public static String[] removeDupes(String[] array) {
            int count = array.length;
            Boolean[] stay = new Boolean[array.length];
            for (int i=0; i < array.length-1; i++) {
                stay[i] = true;
                for (int j=i+1; j<array.length; j++) {
                    if (array[i].equals(array[j])) {
                        if (stay[i] == true) {
                        count--;
                        stay[i] = false;
                        }
                    }
                }
            }

            stay[array.length-1] = true;
            String[] finaly = new String[count];
            int finCount = 0;

            for (int k = 0; k < array.length; k++) {
                if (stay[k]) {
                    finaly[finCount] = array[k];
                    finCount++;
                }
            }
        

            System.out.println("start");
            for (int p = 0; p < finaly.length; p++) {
                System.out.println(finaly[p]);
            }
            System.out.println("end");
            return finaly;
}
    
    public static String findName(String name) {
        try {
            int late = 0;
            Boolean found = false;
        String[] names = getNames(false);
        for (int ii = names.length-1; ii >=0; ii--) {
            System.out.println("this name is " + names[ii]);
            if (names[ii].equals(name) && found == false) {
                late = ii;
                found = true;
            }
            
        }
        System.out.println("latest is " + late + " and the name is " + name);
         
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
            //System.out.println(lines[late+1]);
            if (late < lines.length) {
            return lines[late+1];
            } else {
                return lines[late];
            }

            } catch (IOException e) {
            //System.out.println("An error occurred.");
            e.printStackTrace();
            return new String();
            }
    }

    public static String[] getNames(Boolean dupes) {
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
            System.out.println(sb);
            String[] output = new String[lines.length - 1];
            for (int ii=1; ii < lines.length; ii++) {
                if (lines[ii].indexOf(" ") >= 0) {
                output[ii-1] = lines[ii].substring(0, lines[ii].indexOf(" "));
            }
            }
            //System.out.println("success");
            if (dupes) {
            return removeDupes(output);
            }
            else {
                return output;
            }
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
            //System.out.println("success");
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

    public static void storeAnnualContributions(String name, double annualContribution, double principal, double rate, int years) {
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
            store.write(sb + "\n"  + name + " " + "AnnualContributions " + annualContribution + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.calculateSimpleInterest(principal, rate, years));
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
            store.write(sb + "\n" + name + " Appreciation" + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.calculateAppreciation(principal, rate, years));
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
            store.write(sb + "\n" + name + " Depreciation" + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.calculateDepreciation(principal, rate, years));
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
            store.write(sb + "\n" + name + " CryptoValue" + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.simulateCryptoValue(principal, rate, years));
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
            store.write(sb + "\n" + name + " Inflation" + " " + principal + " " + rate + " " + years + " " + InvestmentLogic.adjustForInflation(principal, rate, years));
            store.close();
            System.out.println("success");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public static void storeutility(String name, int happiness, int frequency, double convenience, int lifestyleConvenience, int timeYears, int lifespan, int price) {
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
            store.write(sb + "\n" + name + " Utility" + " " + happiness + " " + frequency + " " + convenience + " " + lifestyleConvenience + " " + timeYears + " " + lifespan + " " + price + " " + InvestmentLogic.utilityPerDollar(happiness, frequency, convenience, lifestyleConvenience, timeYears, lifespan, price));
            store.close();
            System.out.println("success");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}



 