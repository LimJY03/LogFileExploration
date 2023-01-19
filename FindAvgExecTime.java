// Import necessary dependencies
import java.io.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindAvgExecTime {

    private ArrayList<Long> arr = new ArrayList<>();

    public static void main(String[] args) {
        new FindAvgExecTime().exec();
    }

    public ArrayList<Long> getTimeArr() { 
        this.exec(); 
        return this.arr; 
    }

    public String formatTime(long timeMs) {
        long timeSeconds = timeMs / 1000;
        long timeMinutes = timeSeconds / 60;
        long timeHours = timeMinutes / 60;
        return String.format("%s hours %s minutes %s seconds %s milliseconds\n", timeHours, timeMinutes % 60, timeSeconds % 60, timeMs % 1000);
    }

    private void exec() {
        try{
            BufferedReader fileIn = new BufferedReader(new FileReader("./data/extracted_log"));
            PrintWriter fileOut = new PrintWriter(new FileWriter("./stats/average_exec_time.txt"));
            
            ArrayList<Integer> uniqueJobID = new ArrayList<>();
            ArrayList<String> startJobIdLines = new ArrayList<>();
            ArrayList<String> endJobIdLines = new ArrayList<>();
            
            Pattern jobIdPattern = Pattern.compile("JobId=(\\d+)");
            Pattern timePattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d)");  

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            
            int completedJobCount = 0;
            long totalDuration = 0;            
            String line;

            // Get unique IDs
            while ((line = fileIn.readLine()) != null) {
                
                if (line.contains("sched:") || line.contains("sched/backfill:")) {
                    
                    Matcher jobIdMatcher = jobIdPattern.matcher(line);

                    if (jobIdMatcher.find()) {

                        int id = Integer.parseInt(jobIdMatcher.group(1));

                        if (!uniqueJobID.contains(id)) {
                            uniqueJobID.add(id);
                            startJobIdLines.add(line);
                        }
                    }
                }

                if (line.contains("done")) {

                    Matcher jobIdMatcher = jobIdPattern.matcher(line);

                    if (jobIdMatcher.find()) {
                        endJobIdLines.add(line);
                    }
                }
            }
            
            // Calculate average execution time
            for (String startJobIdLine : startJobIdLines) {
                
                for (String endJobIdLine : endJobIdLines) {
                    
                    Matcher startJobIdMatcher = jobIdPattern.matcher(startJobIdLine);
                    Matcher endJobIdMatcher = jobIdPattern.matcher(endJobIdLine);

                    if (startJobIdMatcher.find() && endJobIdMatcher.find()) {

                        int startJobId = Integer.parseInt(startJobIdMatcher.group(1));
                        int endJobId = Integer.parseInt(endJobIdMatcher.group(1));

                        if (startJobId == endJobId) {

                            LocalDateTime startTime = null, endTime = null;
                            Matcher timeMatcher;

                            timeMatcher = timePattern.matcher(startJobIdLine);
                            if (timeMatcher.find()) { startTime = LocalDateTime.parse(timeMatcher.group(1), formatter); }

                            timeMatcher = timePattern.matcher(endJobIdLine);
                            if (timeMatcher.find()) { endTime = LocalDateTime.parse(timeMatcher.group(1), formatter); }
                            
                            long duration = java.time.Duration.between(startTime, endTime).toMillis();
                            totalDuration += duration;
                            this.arr.add(duration);

                            completedJobCount++;                            
                            break;
                        }
                    }                 
                }
            }               
            
            if (completedJobCount > 0) {
                fileOut.printf(
                    "Average Execution Time:\n%s\nTotal Jobs Completed: %s", 
                    this.formatTime(totalDuration / completedJobCount), completedJobCount
                );
            }
            
            fileIn.close();
            fileOut.close();
        } 
        catch (FileNotFoundException e) { System.out.println("File was not found."); } 
        catch (IOException e) { System.out.println("Error read from file."); }            
    }
}
