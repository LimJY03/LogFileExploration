// Import necessary dependencies
import java.io.*;

public class LogClassifier {

    private String dateStart = "[2022-06-01", dateEnd = "[2022-12-17";
    private boolean started = false, onEndDay = false;

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

                // Start reading log
                if (line.substring(0, 11).equals(this.dateStart) || this.started) {
                    this.started = true;
                    if (line.contains("sched:") || line.contains("sched/backfill:")) { fileOut_startJob.println(line); }
                    else if (line.contains("done")) { fileOut_endJob.println(line); }
                    else if (line.contains("_slurm_rpc_kill_job:")) { fileOut_killJob.println(line); }
                    else if (line.contains("error: This association")) { fileOut_error.println(line); }
                }

                // Check if can stop reading log
                if (line.substring(0, 11).equals(this.dateEnd)) { this.onEndDay = true; }
                else if (!line.subSequence(0, 11).equals(this.dateEnd) && this.onEndDay) { break; }
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

    public void setStartDate(String month, String day) { this.dateStart = String.format("[2022-%s-%s", month, day); }
    public void setEndDate(String month, String day) { this.dateEnd = String.format("[2022-%s-%s", month, day); }
}