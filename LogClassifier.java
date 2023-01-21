// Import necessary dependencies
import java.io.*;

public class LogClassifier {

    public static void main(String[] args) { new LogClassifier().exec(); }

    public void exec() {

        String line;
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader("./data/extracted_log"));
            PrintWriter fileOut_startJob = new PrintWriter(new FileWriter("./data_filtered/start_job.txt"), true);
            PrintWriter fileOut_endJob = new PrintWriter(new FileWriter("./data_filtered/end_job.txt"), true);
            PrintWriter fileOut_killJob = new PrintWriter(new FileWriter("./data_filtered/kill_job.txt"), true);
            PrintWriter fileOut_error = new PrintWriter(new FileWriter("./data_filtered/errors.txt"), true);

            while ((line = fileIn.readLine()) != null) {
                if (line.contains("sched:") || line.contains("sched/backfill:")) { fileOut_startJob.println(line); }
                else if (line.contains("done")) { fileOut_endJob.println(line); }
                else if (line.contains("_slurm_rpc_kill_job:")) { fileOut_killJob.println(line); }
                else if (line.contains("error: This association")) { fileOut_error.println(line); }
            }

            fileIn.close(); 
            fileOut_startJob.close();
            fileOut_endJob.close();
            fileOut_killJob.close();
            fileOut_error.close();
        }
        catch (FileNotFoundException e) { System.out.println("File was not found."); } 
        catch (IOException e) { System.out.println("Error read from file."); }
    }
}