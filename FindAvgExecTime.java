// Import necessary dependencies
import java.io.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindAvgExecTime {

    public static void main(String[] args) { new FindAvgExecTime().exec(); }

    private void exec() {

        try{
            
            BufferedReader startFile = new BufferedReader(new FileReader("./data_filtered/start_job.txt"));
            BufferedReader endFile = new BufferedReader(new FileReader("./data_filtered/end_job.txt"));
            PrintWriter fileOut = new PrintWriter(new FileWriter("./stats/average_exec_time.txt"), true);
            PrintWriter timeRawOut = new PrintWriter(new FileWriter("./output/time_raw.txt"), true);
            
            ArrayList<Integer> uniqueJobID = new ArrayList<>();
            LinkedHashMap<Integer, String> startJobIdLines = new LinkedHashMap<>();
            LinkedHashMap<Integer, String> endJobIdLines = new LinkedHashMap<>();
            
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
                        startJobIdLines.put(id, line);
                    }
                }
            }
            
            while ((line = endFile.readLine()) != null) {
                    
                Matcher jobIdMatcher = jobIdPattern.matcher(line);

                if (jobIdMatcher.find()) {
                    endJobIdLines.put(Integer.parseInt(jobIdMatcher.group(1)), line);
                }
            }

            for (int key: startJobIdLines.keySet()) {

                if (endJobIdLines.containsKey(key)) {
                    
                    LocalDateTime startTime = null, endTime = null;
                    Matcher timeMatcher;
    
                    timeMatcher = timePattern.matcher(startJobIdLines.get(key));
                    if (timeMatcher.find()) { startTime = LocalDateTime.parse(timeMatcher.group(1), formatter); }
    
                    timeMatcher = timePattern.matcher(endJobIdLines.get(key));
                    if (timeMatcher.find()) { endTime = LocalDateTime.parse(timeMatcher.group(1), formatter); }
                    
                    long duration = java.time.Duration.between(startTime, endTime).toMillis();
                    totalDuration += duration;
                    completedJobCount++;  

                    String node;
                    if (startJobIdLines.get(key).contains("Partition=")) { node = startJobIdLines.get(key).split("Partition=")[1]; } 
                    else { node = startJobIdLines.get(key).split("in ")[1].split(" on")[0]; }
                    
                    timeRawOut.printf("%s %s %s\n", key, duration, node);
                }
            }

            if (completedJobCount > 0) {
                fileOut.printf(
                    "Average Execution Time:\n%s\nTotal Jobs Completed: %s", 
                    new CommonOps().formatTime(totalDuration / completedJobCount), completedJobCount
                );
            }
            
            startFile.close();
            endFile.close();
            fileOut.close();
            timeRawOut.close();
        } 
        catch (FileNotFoundException e) { System.out.println("File was not found."); } 
        catch (IOException e) { System.out.println("Error read from file."); }            
    }
}
