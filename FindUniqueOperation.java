// Import necessary dependencies
import java.io.*;
import java.util.LinkedHashMap;

public class FindUniqueOperation {
    
    public static void main(String[] args) {
        
        try {

            // Read file
            BufferedReader fileIn = new BufferedReader(new FileReader("./data/extracted_log"));
            PrintWriter fileOut = new PrintWriter(new FileWriter("./stats/operation_type.txt"), true);

            // Variable declarations
            LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
            String line;

            // Read file
            while ((line = fileIn.readLine()) != null) {
                String keyword = line.split(" ")[1];
                if (map.containsKey(keyword)) { map.put(keyword, map.get(keyword) + 1); }
                else { map.put(keyword, 0); }
            }

            // Sort keywords by descending and write to file
            map = new CommonOps().sortDescending(map);
            map.forEach((keyword, count) -> fileOut.printf("%s: %d\n", keyword, count));

            // Close file
            fileIn.close();
            fileOut.close();
        }
        catch (FileNotFoundException e) {System.out.println("File Not Found!");}
        catch (IOException e) { System.out.printf("IO Exception: %s\n", e); }
    }
}
