// Import necessary dependencies
import java.io.*;

public class FindJobKillStats {
    
    public static void main(String[] args) { new FindJobKillStats().exec(); }

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
            BufferedReader fileIn = new BufferedReader(new FileReader("./data_filtered/kill_job.txt"));
            PrintWriter fileOut = new PrintWriter(new FileWriter("./stats/jobs_killed_stats.txt"), true);

            while ((line = fileIn.readLine()) != null) {
                if (line.contains("_slurm_rpc_kill_job: REQUEST_KILL_JOB")) { new CustomTable().distribute(table, line, "x"); }
                else if (line.contains("error: ") && line.contains("Kill task failed")) { new CustomTable().distribute(table, line, "y"); }
            }

            for (CustomTable monthData : table) { 
                monthData.set_xlabel("job kill(s) requested"); 
                monthData.set_ylabel("job kill(s) failed"); 
                fileOut.println(monthData.toString()); 
            }

            fileIn.close();
            fileOut.close();
        }
        catch (FileNotFoundException e) { System.out.println("File was not found."); } 
        catch (IOException e) { System.out.println("Error read from file."); }
    }
}