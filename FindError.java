// Import necessary dependencies
import java.io.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

//Number of jobs causing error and the corresponding user. Error is "[2022-06-01T15:12:23.290] error:This association..."
public class FindError {

    private ArrayList<String> errorMsg = new ArrayList<>();
    private LinkedHashMap<String, Integer> userError = new LinkedHashMap<>();
    private int errorCount = 0;

    public static void main(String[] args) { 

        FindError program = new FindError();

        program.exec();
        program.errorPrintToFile("./output/error.txt");
        program.userErrorPrintToFile("./stats/user_error.txt");
    }

    public void exec() {

        try{

            BufferedReader file = new BufferedReader(new FileReader("./data/extracted_log"));
            String line;
            
            while ((line = file.readLine()) != null) {

                if (line.contains("error: This association")) { 
                    
                    this.errorMsg.add(line); 
                    this.errorCount++;

                    String error = line.split("error: This association ")[1]; 
                    String user = String.format(
                        "%s (%s)", 
                        error.split("user='")[1].split("', partition='\\(null\\)'")[0],
                        error.split("\\(account='free', ")[0]
                    );

                    if (this.userError.containsKey(user)) { this.userError.put(user, this.userError.get(user) + 1); }
                    else { this.userError.put(user, 1); }
                }
            }

            this.userError = new CommonOps().sortDescending(this.userError);

            file.close();
        }
        catch (FileNotFoundException e) { System.out.println("File was not found."); } 
        catch (IOException e) { System.out.println("Error read from file."); } 
    }

    public LinkedHashMap<String, Integer> getUserError() { return this.userError; }
    public ArrayList<String> getErrorLines() { return this.errorMsg; }

    public void errorPrintToFile(String path) {
        if (this.errorMsg == null) { this.exec(); }
        try {
            PrintWriter fileOut = new PrintWriter(new FileWriter(path));
            this.errorMsg.forEach(error -> fileOut.println(error));
            fileOut.close();
        } catch (IOException e) { System.out.println("Error read from file."); }
    }

    public void userErrorPrintToFile(String path) {
        if (this.userError.size() == 0) { this.exec(); }
        try {
            PrintWriter fileOut = new PrintWriter(new FileWriter(path));
            this.userError.forEach((user, error) -> fileOut.printf("%s: %s\n", user, error));
            fileOut.printf("\nNumber of Errors: %d", this.errorCount);
            fileOut.close();
        } catch (IOException e) { System.out.println("Error read from file."); }
    }
}