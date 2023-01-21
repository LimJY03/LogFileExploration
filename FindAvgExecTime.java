// Import necessary dependencies
import java.io.*;
import java.nio.file.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindAvgExecTime {

    public static void main(String[] args) { new FindAvgExecTime().exec(); }

    private void exec() {

        try{
            
            BufferedReader startFile = new BufferedReader(new FileReader("./data_filtered/start_job.txt"));
            PrintWriter fileOut = new PrintWriter(new FileWriter("./stats/average_exec_time.txt"), true);
            PrintWriter timeRawOut = new PrintWriter(new FileWriter("./output/time_raw.txt"), true);
            
            ArrayList<Integer> uniqueJobID = new ArrayList<>();
            ArrayList<String> startJobIdLines = new ArrayList<>();
            ArrayList<String> endJobIdLines = new ArrayList<>(Files.readAllLines(Paths.get("./data_filtered/end_job.txt")));
            
            Pattern jobIdPattern = Pattern.compile("JobId=(\\d+)");
            Pattern timePattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d)");  

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            
            int completedJobCount = 0;
            long totalDuration = 0;            
            String line;

            // Get unique IDs
            while ((line = startFile.readLine()) != null) {
                    
                Matcher jobIdMatcher = jobIdPattern.matcher(line);

                if (jobIdMatcher.find()) {

                    int id = Integer.parseInt(jobIdMatcher.group(1));

                    if (!uniqueJobID.contains(id)) {
                        uniqueJobID.add(id);
                        startJobIdLines.add(line);
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
                            timeRawOut.println(duration);

                            completedJobCount++;                            
                            break;
                        }
                    }                 
                }
            }               
            
            if (completedJobCount > 0) {
                fileOut.printf(
                    "Average Execution Time:\n%s\nTotal Jobs Completed: %s", 
                    new CommonOps().formatTime(totalDuration / completedJobCount), completedJobCount
                );
            }
            
            startFile.close();
            fileOut.close();
            timeRawOut.close();
        } 
        catch (FileNotFoundException e) { System.out.println("File was not found."); } 
        catch (IOException e) { System.out.println("Error read from file."); }            
    }
}
