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
                if (line.contains("JobId=")) {
                    switch (line.split("-")[1]) {
                        case "06" -> table[0].addStart();
                        case "07" -> table[1].addStart();
                        case "08" -> table[2].addStart();
                        case "09" -> table[3].addStart();
                        case "10" -> table[4].addStart();
                        case "11" -> table[5].addStart();
                        case "12" -> table[6].addStart();
                        default -> {}
                    }
                }
            }
            
            while ((line = endFileIn.readLine()) != null) {
                switch (line.split("-")[1]) {
                    case "06" -> table[0].addEnd();
                    case "07" -> table[1].addEnd();
                    case "08" -> table[2].addEnd();
                    case "09" -> table[3].addEnd();
                    case "10" -> table[4].addEnd();
                    case "11" -> table[5].addEnd();
                    case "12" -> table[6].addEnd();
                    default -> {}
                }
            }

            for (CustomTable monthData : table) { fileOut.println(monthData.toString()); }

            startFileIn.close();
            endFileIn.close();
            fileOut.close();
        }
        catch (FileNotFoundException e) { System.out.println("File was not found."); } 
        catch (IOException e) { System.out.println("Error read from file."); }
    }
}

class CustomTable {

    private String month;
    private int start, end;

    public CustomTable(String month, int start, int end) {
        this.month = month;
        this.start = start;
        this.end = end;
    }

    public void addStart() { this.start++; }
    public void addEnd() { this.end++; }
    public String toString() { return String.format("%s: %d started, %d ended", this.month, this.start, this.end); }
}