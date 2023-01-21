// Import necessary dependencies
import java.io.*;
import java.util.LinkedHashMap;

//Number of jobs causing error and the corresponding user. Error is "[2022-06-01T15:12:23.290] error:This association..."
public class FindError {

    public static void main(String[] args) { new FindError().exec(); }

    public void exec() {

        LinkedHashMap<String, Integer> userError = new LinkedHashMap<>();
        int errorCount = 0;

        try{

            BufferedReader fileIn = new BufferedReader(new FileReader("./data_filtered/errors.txt"));
            PrintWriter fileOut = new PrintWriter(new FileWriter("./stats/user_error.txt"), true);
            String line;
            
            while ((line = fileIn.readLine()) != null) {
                
                errorCount++;

                String error = line.split("error: This association ")[1]; 
                String user = String.format(
                    "%s (%s)", 
                    error.split("user='")[1].split("', partition='\\(null\\)'")[0],
                    error.split("\\(account='free', ")[0]
                );

                if (userError.containsKey(user)) { userError.put(user, userError.get(user) + 1); }
                else { userError.put(user, 1); }
            }

            userError = new CommonOps().sortDescending(userError);
            userError.forEach((user, error) -> fileOut.printf("%s: %s\n", user, error));

            fileOut.printf("\nNumber of Errors: %d", errorCount);
            fileOut.close();

            fileIn.close();
            fileOut.close();
        }
        catch (FileNotFoundException e) { System.out.println("File was not found."); } 
        catch (IOException e) { System.out.println("Error read from file."); } 
    }
}