//import java.io.FileWriter;
//import java.io.IOException;
import java.io.*;

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
        if (array == null || array.length == 0) {
            return new String[0];
        }
        
        java.util.Set<String> seen = new java.util.HashSet<>();
        java.util.List<String> result = new java.util.ArrayList<>();
        
        // Keep the last occurrence of each name (reverse order processing)
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] != null && !seen.contains(array[i])) {
                seen.add(array[i]);
                result.add(0, array[i]); // Add to front to maintain relative order
            }
        }
        
        return result.toArray(new String[0]);
    } 
    
public static String findName(String name) {
    try {
        // First get all the lines from the file
        FileReader fr = new FileReader(filePath);
        StringBuilder sb = new StringBuilder();
        int i;
        while ((i = fr.read()) != -1) {
            sb.append((char)i);
        }
        fr.close();
        
        String[] lines = sb.toString().split("\\n");
        
        // Find the most recent entry with this name
        String foundLine = null;
        for (int lineIndex = lines.length - 1; lineIndex >= 1; lineIndex--) {
            String line = lines[lineIndex].trim();
            if (line.isEmpty()) continue;
            
            // Extract the name (first field before the first space)
            int firstSpace = line.indexOf(" ");
            if (firstSpace > 0) {
                String lineName = line.substring(0, firstSpace);
                if (lineName.equals(name)) {
                    foundLine = line;
                    break;
                }
            }
        }
        
        System.out.println("Looking for name: '" + name + "'");
        System.out.println("Found line: '" + foundLine + "'");
        
        return foundLine != null ? foundLine : "";
        
    } catch (IOException e) {
        System.out.println("Error in findName: " + e.getMessage());
        e.printStackTrace();
        return "";
    }
}

public static String[] getNames(Boolean dupes) {
    try {
        FileReader fr = new FileReader(filePath);
        StringBuilder sb = new StringBuilder();
        int i;
        while ((i = fr.read()) != -1) {
            sb.append((char)i);
        }
        fr.close();
        
        String[] lines = sb.toString().split("\\n");
        java.util.List<String> namesList = new java.util.ArrayList<>();
        
        // Skip the first line (index 0) as it's usually empty/header
        for (int lineIndex = 1; lineIndex < lines.length; lineIndex++) {
            String line = lines[lineIndex].trim();
            if (line.isEmpty()) continue;
            
            int firstSpace = line.indexOf(" ");
            if (firstSpace > 0) {
                String name = line.substring(0, firstSpace);
                namesList.add(name);
            }
        }
        
        String[] output = namesList.toArray(new String[0]);
        
        if (dupes) {
            return removeDupes(output);
        } else {
            return output;
        }
        
    } catch (IOException e) {
        System.out.println("Error in getNames: " + e.getMessage());
        e.printStackTrace();
        return new String[0];
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



 