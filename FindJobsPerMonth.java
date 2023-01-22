// Import necessary dependencies
import java.io.*;

public class FindJobsPerMonth {

    public static void main(String[] args) { new FindJobsPerMonth().exec(); }
    
    public void exec() {

        CustomTable[] table = {
            new CustomTable("Jun", 0, 0),
            new CustomTable("Jul", 0, 0),
            new CustomTable("Aug", 0, 0),
            new CustomTable("Sep", 0, 0),
            new CustomTable("Oct", 0, 0),
            new CustomTable("Nov", 0, 0),
            new CustomTable("Dec", 0, 0)
        };
        String line;

        try {
            BufferedReader startFileIn = new BufferedReader(new FileReader("./data_filtered/start_job.txt"));
            BufferedReader endFileIn = new BufferedReader(new FileReader("./data_filtered/end_job.txt"));
            PrintWriter fileOut = new PrintWriter(new FileWriter("./stats/jobs_per_month.txt"), true);

            while ((line = startFileIn.readLine()) != null) {
                if (line.contains("JobId=")) { new CustomTable().distribute(table, line, "x"); }
            }
            
            while ((line = endFileIn.readLine()) != null) {
                if (line.contains("JobId=")) { new CustomTable().distribute(table, line, "y"); }
            }

            for (CustomTable monthData : table) { 
                monthData.set_xlabel("started"); 
                monthData.set_ylabel("ended"); 
                fileOut.println(monthData.toString()); 
            }

            startFileIn.close();
            endFileIn.close();
            fileOut.close();
        }
        catch (FileNotFoundException e) { System.out.println("File was not found."); } 
        catch (IOException e) { System.out.println("Error read from file."); }
    }
}